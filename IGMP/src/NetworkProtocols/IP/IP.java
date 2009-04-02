package NetworkProtocols.IP;


import utils.*;
import Exceptions.*; 
import Interface.*;
import NetworkProtocols.*;
import NetworkProtocols.IGMP.IGMP;
import NetworkProtocols.IGMP.util.Semaphore;
import NetworkProtocols.IP.Address.*;
import NetworkProtocols.IP.RoutingTable.*;
  
 /** IP se comunica con el nivel inferior, a traves de una cola de datagrams recibidos (buffRem),
 *  y a con el nivel superior, a través de una cola que en principio tiene informacion
 *  a ser encapsulada en datagrama para ser enviada (buffLoc).
 *  El thread (reader) de IP, lee en orden de estas dos colas, y cuando encuentra algun requerimiento
 *  (p.ej. un datagram que llego o un requerimiento de enviar algo por parte de UDP etc), lo procesa
 *  y recien despues continua con otro requerimiento. Es decir, la entrada al IP esta sincronizada
 *  con lo que viene por l alinea y lo que le entregan las aplicaciones, pero el proceso que de 
 *  todo esto hace IP, se realiza aca, a ritmo del IP
 *  Mas adelante, desde el nivel superior se podrá recibir otro tipo de info, por ejemplo
 *  la que proviene de opciones de configuracion, etc.
 *  De igual manera, las interfaces podran anunciar situaciones anormales (ver si todo esto
 *  va directamenet a IP o queda reflejado en los flags.
 *  Las clases que representan al nivel superior a IP, tendran mettodos para que les sea pasada
 *  la info (frames recibidos o info de control)
 */

public class IP implements ProtocolInterface {
  Queue buffRem;  // Buffer de entrada remota a IP, lo llenan las interfaces
  Queue buffLoc;  // Buffer de entrada local a IP, lo llenan las apps o ICMP
  public RoutingTable rTable;   // Tabla de ruteo del nodo
  Interfaces ifaces;     // Interfaces de red
  NetworkProtocols Nt;
  ReaderLocal rdrL;            // Thread de lectura de events del nivel superior e inferior
  ReaderRemoto rdrR;
  IpAddress ipLocal;
  Semaphore hayTareaLocal, sLocal;
  Semaphore hayTareaRemota, sRemoto;
  boolean terminar = false;

  public IpAddress getIpLocal() {
	return ipLocal;
}

public void setIpLocal(IpAddress ipLocal) {
	this.ipLocal = ipLocal;
}

public IP(Interfaces ifs) throws NodeException {
    buffRem = new Queue();       // Instanciacion del buffer de recepcion de eventos remoto
    buffLoc = new Queue();       // Instanciacion del buffer de recepcion de eventos locales
    //rTable = RT;
    rTable = new RoutingTable(); // Instanciacion de la tabla de ruteo
    ifaces = ifs;                // Interfaces de red del router
    hayTareaLocal = new Semaphore(0);
    hayTareaRemota = new Semaphore(0);
    sLocal = new Semaphore(1);
    sRemoto = new Semaphore(1);
    rdrL = new ReaderLocal(this);      // Instanciacion del thread de lectura de eventos
    rdrR = new ReaderRemoto(this);
  }

  // Metodo invocado por la interfaz para agregar un evento a la cola remota
  public void addRem(N2N3Indication x) { 
	  sRemoto.P();
	  buffRem.pushBack(x);
	  sRemoto.V();
	  hayTareaRemota.V();
	  }

  /**
   * Metodo invocado por el nivel superior para agregar un evento a la cola local
   * @param: es del tipo IpIndication.
   */ 
  public void addLoc(Object x) {
	  sLocal.P();
	  buffLoc.pushBack(x);
	  sLocal.V();
	  hayTareaLocal.V();
	  }

  /** Procesa un datagram remoto o alguan indicacion del nivel inferior. Esto esta en buffRem
   * El objeto contiene un datagram, y podria contener ademas otras cosas que seran definidas
   * mas adelante (por ejemplo, primitivas indication con las cuales la interfaz le informe que
   * esta caida, etc
   */
  public void receive_rem(N2N3Indication indN2) {
    //System.out.println("Encontro requerimiento en cola remota tipo "+indN2.getControl());
    // Segun la primitiva recibida, toma la accion que corresponda
    byte ici = indN2.getControl();
    byte [] bb;
    bb = (byte[]) indN2.getInfo();
    Datagram dd = new Datagram(bb);
    switch (ici) {
      case N2N3Indication.FRAME_RECEIVED:   // Recepcion de un frame link layer con indicacion de 
                                            // payload = protocolo IP
        // Determina longitud del arreglo de bytes recibido, longitud total del IP y longitud
        // del header IP, luego hace los cheqieos para determinar si el dgram es correcto
        
        int longrec = bb.length;
        //System.out.println("DATAGRAM recibido por IP long total recibida (" +longrec+" ) " + dd.toString());
        //1-Longiud de header menor que 20 o version distinta de 4 o long recibida es menor que long del datagram
        // o falla checksum, descartar silenciosamente (solo podria registrarse en estadisticas)
        //dd.genChecksum();
        if( (dd.ihl<5) || (dd.version != 4) || (longrec < dd.totalLength) || (longrec < dd.ihl*4) ||
             (dd.verifyChecksum() == false)) {
          System.out.println("Error al chequear datagram, se descarta datagram silenciosamente");
          break;

        }
        
        
        int protocolo = dd.getProtocol();
        switch(protocolo){
        case NetworkProtocols.PROTO_IGMP: {
        	IGMP igmp = (IGMP) NetworkProtocols.getProtocol(protocolo);
        	if (igmp.InteresaIp(dd.getDestAddress()))
        		igmp.addRem(indN2);
        	break;
        }
        }
        
        /*
        // Si el header IP contiene opciones, procesarlas. El proceso agota las consecuencias de las
        // opciones (envio de icmp, etc). Si hay errores que impiden que se procese el paquete, el
        // proceso de opciones retorna false.
        if(dd.ihl > 5) {
          if(proc_opts(dd) == false) break;
        }
        // Determinar proximo router para el paquete. Si no es este equipo aplica el ruteo invocando a
        // send_datagram que invoca a ARP, y luego manda  a la interfaz
        // Si el datagram es para este equipo, debe ensamblarse si corresponde e invocar a locla_delivery,
        // (una vez reensamblado) para entregarlo al nivel superior.
     
        RoutingEntry re = rTable.getNextHop(dd.getDestAddress());   
        
        if(isLocalHost(re)) {
          // envío local, detiene el datagram hasta recibir todos los fragmentos (si es fragmento)
          // una vez que se tiene el datagram completo, se pasa al nivel superior
        } else {
          // envío remoto, decrementa TTL, chequea condiciones de error, se debe fragmentar si es 
          // neceario.No se reensamblan datagrams en este caso
        }
        */
    }
  }

  /**
   * Procesa un requerimiento de envio u otro del nivel superior. Recibe en un byte la info de
   * control de l ainterfa, y un objeto conteniendo la idu.
   * @param idu: es un objeto del tipo IpIndication.
   */
  public void receive_loc(IpIndication idu) {
	  Datagram datagram= new Datagram(4, 11, 6, true, false, false, false, false, 100, 4567, false, true, true,
              8123, 232, NetworkProtocols.PROTO_IGMP, 12336, ipLocal, idu.dirDestino);
	  datagram.setPayload(idu.getDatos());
	  datagram.genChecksum();
	  idu.getInter().send(NetworkProtocols.PROTO_IP, datagram.toByte());
  }

  /** Agregado de una entrada a la tabla de ruteo. Este metodo se invocaria cuando se coloca el
   evento correspondiente en la cola de eventos locales, y el thread de lectura de eventos 
   lo detecta y hace el correpondiente llamado a este metodo. Por ejemplo, se daria cuando el 
   administrador de red agrega manualmente entradas en la tabla de ruteo o cuando lo hace un
   protocolo de ruteo como RIP.
   Por el momento, solo se agrega la entrada, mas adelante, antes del agregado se deberian 
   realizar chequeos de consistencia*/
  public void addRoute(IpAddress dstNet,Mask mask, boolean routeType, IpAddress nextHop, Interface ifc) {
    rTable.addEntry(dstNet,mask, routeType, nextHop, ifc);
  }

      public void rt() {
      rTable.toString();
    }

  public void addDefault(IpAddress nextHop, Interface ifc) throws NodeException {
    rTable.addDefault(nextHop, ifc);
  }

  public void delRoutingEntry(IpAddress dstNet,Mask mask) {
    rTable.delRoute(dstNet,mask);
  }


  class ReaderLocal extends Thread {
    IP miIp;           // Instancia IP
    IpIndication infoloc;              // info local recibida por IP
    Semaphore hayTarea;
    
    
    public ReaderLocal(IP Ip) {
    miIp = Ip;
    this.hayTarea = miIp.getHayTareaLocal();
    this.start();
    }

    public void run() {
      while (!terminar) {
    	  hayTarea.P();
        if( ( infoloc = (IpIndication) buffLoc.peekpopBack()) != null) 
        	miIp.receive_loc(infoloc);
      }
    }
  }

    class ReaderRemoto extends Thread {
        IP miIp;           // Instancia IP
        N2N3Indication inforem;      // info remota recibida  (buffInp), un datagram mas otras cosas
        Semaphore hayTarea;
        
        
        public ReaderRemoto(IP Ip) {
        miIp = Ip;
        this.hayTarea = miIp.getHayTareaRemota();
        this.start();
        }

        public void run() {
          while (!terminar) {
        	  hayTarea.P();
        	  if( ( inforem = (N2N3Indication) buffRem.peekpopBack()) != null) {
        	       miIp.receive_rem(inforem);
        	    }
          }
        }
    }
    
  
  /** Procesa las opciones del datagram, incluye envio de icmps, etc. Devuelve false si hay
   errores que impidan el proceso del datagram, y true en caso contrario.*/
  public boolean proc_opts(Datagram dd) {
    return true;
  }

public Semaphore getHayTareaLocal() {
	return hayTareaLocal;
}

public Semaphore getHayTareaRemota() {
	return hayTareaRemota;
}

public void Terminar(){
	terminar = true;
	this.hayTareaLocal.Terminar();
	this.hayTareaRemota.Terminar();
}
}



