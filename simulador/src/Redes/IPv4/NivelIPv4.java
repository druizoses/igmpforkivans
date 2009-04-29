/*
    Simulador de redes IP (KIVA). API de Simulacion, permite simular
    redes de tipo IP que usen IP, ARP, e ICMP.
    Copyright (C) 2004, José María Díaz Gorriz

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Library General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Library General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this library; if not, write to the 
    Free Software Foundation, Inc., 59 Temple Place - Suite 330, 
    Boston, MA  02111-1307  USA.
*/

/*
 * Creado el 21-dic-2003
 */
 
package Redes.IPv4;

import Proyecto.*;
import Redes.*;
import Redes.IPv4.ARP.*;
import Redes.IPv4.ICMP.*;
import Redes.IPv4.IGMP.ModuloIGMP;
import Equipos.Equipo;
import java.util.Vector;


/**
 * Módulo IPv4
 */
public class NivelIPv4 extends Nivel
{
	/**
	 * Tabla de rutas, facimente accesible desde el 'equipo'
	 */
	public TablaDeRutas tablaDeRutas;
	
	/**
	 * Datos con paquetes que estan siendo reensamblados
	 */
	Vector reensamblados;
	
	/**
	 * Modulo ARP asociado al nivel IPv4
	 */
	ModuloARP moduloARP;

	/**
	 * Modulo ICMP asociado al nivel IPv4
	 */
	ModuloICMP moduloICMP;
	
	/**
	 * Modulo IGMP asociado al nivel IPv4
	 */
	ModuloIGMP moduloIGMP;
	
	/**
	 * Cola de datagramas que estan en espera de que se reciba una respuesta ARP
	 */
	Vector enEspera;
	
	/**
	 * Instante actual
	 */
	int instanteActual;
	
	
	
	/**
	 * Inicializador de la clase
	 */
	static
	{
		// El id en IPv4 para ethernet es 0x0800...
		LocalizadorRedes.Registrar("ipv4","ethernet",0x0800);
		LocalizadorRedes.Registrar("ipv4","puntoapunto",0x0001);
		LocalizadorRedes.Registrar("ipv4","anillo",0xFFFF);
	    LocalizadorRedes.Registrar("ipv4","ipv4",0x0000);
	    LocalizadorRedes.Registrar("ipv4","icmp",2048);
	}
	
	
	
	/**
	 * Constructor
	 * @param equipo Equipo que contiene el modulo
	 * @param moduloARP Modulo ARP, que se usara para traducir las direcciones IP
	 * @param moduloICMP Modulo ICMP que se usara para el envio de mensajes informativos o de error
	 */
	public NivelIPv4(Equipo equipo, ModuloARP moduloARP, ModuloICMP moduloICMP)
	{
		super(equipo);
	
        // 0. Comprobacion de paramentros
		if(equipo==null || moduloARP==null || moduloICMP==null)
            throw new IllegalArgumentException("Error creando el nivel IPv4");
        
		// 1. Inicializacion
	    instanteActual=-1;
		this.moduloARP=moduloARP;
		this.moduloICMP=moduloICMP;
		tablaDeRutas=new TablaDeRutas(this.equipo);
		enEspera=new Vector();
		reensamblados=new Vector();
	    
	    // 2. Parametros
	    parametros.add(new Parametro("ARP Timeout","Tiempo máximo de espera de ARP","java.lang.Integer"));
        parametros.setValor("ARP Timeout",new Integer(30));
        parametros.add(new Parametro("IP Reassembly Timeout","Tiempo maximo de reensamblado","java.lang.Integer"));
        parametros.setValor("IP Reassembly Timeout",new Integer(150));
        parametros.add(new Parametro("IP Forwarding","Redirección del tráfico IP","java.lang.Boolean"));
        parametros.setValor("IP Forwarding",new Boolean(false));
        
        // Retardo asociado al procesamiento realizado por el modulo IP
        parametros.setValor("Retardo",new Integer(1));
	}

	
	
	/**
	 * Activa o Desactiva el 'ip forwarding'
	 * @param estado 'true' para habilitar y 'false' para deshabilitar
	 */
	public void IPForwarding(boolean estado)
	{
	    parametros.setValor("IP Forwarding",new Boolean(estado));
	}
	
	
	
	/**
	 * Procesa los datagramas del instante especificado
	 * @param instante Instante de tiempo
	 */
	public void Procesar(int instante)
	{
	    // No cambiar el orden, ya que si un datagrama se envia
	    // desde una ip de este equipo a otra de este mismo 
	    // equipo, el metodo ProcesarSalida() lo pasa a la cola
	    // de entradas, programado para el instante actual (en el que se
	    // este procesando como salida pero como entrada)
	    
	    // 0. Actualizacion
	    instanteActual=instante;
	    
	    // 1. Procesamos las salidas
	    ProcesarSalida();
	    
	    // 2. Procesamos las entradas
		ProcesarEntrada();
	}
	
	
	
	/**
	 * Devuelve el numero de datagramas pendientes de ser procesados
	 * @return Numero de datagramas pendientes de ser procesados
	 */
	public int Pendientes()
	{
	    int pendientes=colaEntrada.size()+colaSalida.size()+enEspera.size();
	    
	    // 1. Contamos tambien si queda algun datagrama por reensamblar
	    for(int i=0;i<this.reensamblados.size();i++)
	    {
	        Reensamblado r=(Reensamblado)reensamblados.get(i);
	        if(!r.ProcesoTerminado())
	            pendientes++;
	    }
	    
	    // 2. Devolvemos el numero de datagramas pendientes de ser procesados
	    return(pendientes);	    
	}
	
	
    
    /**
     * Procesa los datagramas que deban ser procesados en el instante indicado
     */
	private void ProcesarEntrada()
	{		
		// 1. Comprobamos todos los datos de entrada
		for(int i=0;i<colaEntrada.size();i++)
		{
			Dato dato=(Dato)colaEntrada.get(i);
			
			if(dato.instante==instanteActual)
			{
				try
				{
					// 1. Eliminamos el dato de la cola de entrada
					colaEntrada.remove(i);
					i--;
					
					// 2. Comprobamos que el datagrama es correcto
					DatagramaIPv4 datagrama=new DatagramaIPv4(dato.paquete);
					
					if(datagrama.getDF()==0)
					    dato.fragmentable=true;
					else
						dato.fragmentable=false;
					
					if(datagrama.EsCorrecto())
					{
					    // 3. Apuntamos el evento
					    if(EsFragmento(datagrama))
					        equipo.NuevoEvento('R',instanteActual,datagrama,"Fragmento de datagrama IPv4");
					    else
					        equipo.NuevoEvento('R',instanteActual,datagrama,"Datagrama IPv4");
					    
					    // 4. Procesamos el datagrama o fragmento de datagrama 
					    dato.paquete=datagrama;
					    ProcesarDatagrama(dato);
					}
				}
				catch(Exception e) {}
			}
		}
		
		// 2. Comprobamos si se ha reensamblado totalmente algun datagrama fragmentado
		ProcesarReensamblados();
	}
	

	
	/**
	 * Procesa un datagrama de la cola de salida (hacia la red) 
	 */
	private void ProcesarSalida()
	{
		Dato dato;
		
		// 1. Comprobamos todos los datos de salida
		for(int i=0;i<colaSalida.size();i++)
		{
			dato=(Dato)colaSalida.get(i);
			
			if(dato.instante==instanteActual)
			{
				try
				{
					// 1. Eliminamos el dato de la cola de salida
					colaSalida.remove(i);
					i--;
					
					// 2. Enviamos el datagrama
                    if(EsParaMi((DireccionIPv4)dato.direccion,dato.interfaz))
                    {
                        if(dato.paquete instanceof DatagramaIPv4)
                        {
                            DatagramaIPv4 datagrama = (DatagramaIPv4) dato.paquete;
                            if (datagrama.getProtocol()!=2) // Si no es un paquete IGMP
                            	colaEntrada.add(dato);
                            if (datagrama.getDestino().getClaseDireccion().equals("D"))
                            	Enviar(dato,false);
                        }
                        else
                        {    
                            //DireccionIPv4 origen=new DireccionIPv4("127.0.0.1"); //loopback
                            DireccionIPv4 origen=dato.interfaz.getIP();
                            DireccionIPv4 destino=(DireccionIPv4)dato.direccion;
                            DatagramaIPv4 datagrama=new DatagramaIPv4(origen,destino,dato.paquete);
                            datagrama.setProtocol(dato.protocolo);
                            dato.paquete=datagrama;
                            if(dato.fragmentable==true)
                            	datagrama.setDF(0);
                            else
                            	datagrama.setDF(1);
                            if (datagrama.getProtocol()!=2) // Si no es un paquete IGMP
                            	colaEntrada.add(dato);
                            if (datagrama.getDestino().getClaseDireccion().equals("D"))
                            	Enviar(dato,false);
                        }
                    }
                    else
                    {
                        // En enviarconfragmentacion o sin fragmentacion ya se controla
                        // que dato.paquete sea un DatagramaIPv4 o un Buffer y se actua
                        // en consecuencia
					    Enviar(dato,false);
                    }
                }
				catch(Exception e) {}
			}
		}
		
		// 2. Comprobamos si hay algun datagrama pendiente de una respuesta ARP que
		//    ya se haya recibido
		ProcesarEnEsperaARP();
	}
	
	
	
    /**
     * Procesa los datagramas de la cola de espera de respuestas ARP
     */
	private void ProcesarEnEsperaARP()
	{
	    // 0. Inicializamos
	    int arp_timeout=-1;
	    Integer Itimeout=(Integer)parametros.getValor("ARP Timeout");
	    if(Itimeout!=null)
	        arp_timeout=Itimeout.intValue();
	    if(arp_timeout<=0)
	        arp_timeout=50;
	    
		for(int i=0;i<enEspera.size();i++)
		{		    
		    try
		    {
		    	Objeto.DEBUG(equipo.getNombre()+": Procesando datagrama en espera de ARP ->");
		    	
		    	Dato dato=(Dato)enEspera.get(i);
			    
				// 1. Comprobamos si se ha cumplido el 'time out'
		    	if(instanteActual-dato.instante>=arp_timeout)
				{
				    // 1.1 Descartamos el dato
				    enEspera.remove(i);
				    i--;
				    		
				    // 1.2 Enviamos icmp 'destination unreachable, host unreachable'
				    // comprobamos que el dato es datagramaIPv4 y si es Buffer
				    // hacemos una conversion (la que se hace en enviarICMP
				    EnviarICMP(3,1,dato);
			    }

		    	// 2. Si no se ha cumplido, reintentamos el envio, pero deshabilitando
		    	//    el uso de ARP (porque ya se ha usado, y estamos en su cola de espera)
		    	else
			    {
		    	    //ProcesarDatagrama(dato);
		    	    Enviar(dato,true);
			    }
		   }
		   catch(Exception e)
		   {
		   }
		}
	}
	
	
	
	/**
	 * Envia un mensaje ICMP al modulo ICMP
	 * @param tipo Tipo de mensaje
	 * @param codigo Codigo del mensaje
	 * @param dato Dato con los datos del del datagrama
	 */
	private void EnviarICMP(int tipo, int codigo,Dato dato)
	{
	    int numEntrada;
	    int retardo=getRetardo();
	    
	    if(dato.paquete instanceof DatagramaIPv4 && EnvioICMPErrorPermitido((DatagramaIPv4)dato.paquete,tipo))
	    {    
	        DatagramaIPv4 datagramaOriginal=(DatagramaIPv4)dato.paquete;
	        DireccionIPv4 destino=datagramaOriginal.getOrigen();
	        
	        numEntrada=tablaDeRutas.SiguienteSalto(destino);
	        if(numEntrada!=-1)
	        {    
	            MensajeICMP mensaje=new MensajeICMP(tipo,codigo,datagramaOriginal);
	            Dato datoAux=new Dato(instanteActual+retardo,mensaje,0);
	            datoAux.direccion=datagramaOriginal.getOrigen();
	        
	            if(EsParaMi(destino,null))
	            {    
	                //Autoenvio (marcamos en 'virtual' envio, por claridad en los eventos)
	                MensajeICMP m2=new MensajeICMP(mensaje);
	                
	                if(!(moduloICMP.SimularError(ErroresICMP.flags[0]) && m2.getType()==3 && m2.getCode()==2))
	                   equipo.NuevoEvento('E',datoAux.instante,m2,"Mensaje ICMP ["+mensaje.getType()+"|"+mensaje.getCode()+"]");
	                
	                moduloICMP.ProgramarEntrada(datoAux);
	            }
	            else
	                moduloICMP.ProgramarSalida(datoAux);
	        }
	        else if(EsParaMi(destino,null)) //caso especial -> destino = loopback
	        {
	            MensajeICMP mensaje=new MensajeICMP(tipo,codigo,datagramaOriginal);
	            Dato datoAux=new Dato(instanteActual+retardo,mensaje,0);
	            datoAux.direccion=datagramaOriginal.getOrigen();

	            //Autoenvio (marcamos en 'virtual' envio, por claridad en los eventos)
                MensajeICMP m2=new MensajeICMP(mensaje);
                
                if(!(moduloICMP.SimularError(ErroresICMP.flags[0]) && m2.getType()==3 && m2.getCode()==2))
                   equipo.NuevoEvento('E',datoAux.instante,m2,"Mensaje ICMP ["+mensaje.getType()+"|"+mensaje.getCode()+"]");
                
                moduloICMP.ProgramarEntrada(datoAux);
            }	        	
	    }
	    else
	    {
            // Se puede dar el caso de que se quiera enviar un Buffer, y si no
	        // se recibe respuesta ARP del destino, no se muestre ningun mensaje
	        // icmp de error cuando se deberia mostrar un 'host unreachable'
	        // lo que pasa es que no tenemos disponible el 'datagrama' solo un
	        // buffer de datos a pelo
	        // solucion: me envio un mensaje a mi mismo, indicandome ese error
	        // y el paquete que se deberia haber mandado lo creamos ahora
	        DireccionIPv4 origen=new DireccionIPv4("127.0.0.1");
	        DireccionIPv4 destino=(DireccionIPv4)dato.direccion;
	        DatagramaIPv4 datagrama=new DatagramaIPv4(origen,destino,dato.paquete);
	        if(dato.fragmentable)
	        	datagrama.setDF(0);
	        else
	        	datagrama.setDF(1);
	        MensajeICMP mensaje=new MensajeICMP(tipo,codigo,datagrama);
	        Dato datoAux=new Dato(instanteActual+retardo,mensaje,0);
	        datoAux.protocolo=0;
	        
	        //Autoenvio (marcamos el 'virtual' envio, por claridad en los eventos)
	        MensajeICMP m2=new MensajeICMP(mensaje);
	
	        if(!(moduloICMP.SimularError(ErroresICMP.flags[0]) && m2.getType()==3 && m2.getCode()==2))
	            equipo.NuevoEvento('E',datoAux.instante,m2,"Mensaje ICMP ["+mensaje.getType()+"|"+mensaje.getCode()+"]");
	        moduloICMP.ProgramarEntrada(datoAux);
	    }
	}
	
	
	
    /**
     * Comprueba si el envio de un mensaje icmp provocado por el datagrama indicado
     * esta o no permitido
     * @param datagrama Datagrama que provoca en envio del mensaje ICMP
     * @param tipo Tipo de mensaje ICMP a enviar
     * @return Cierto si se debe enviar el mensaje, falso en otro caso
     */
	private boolean EnvioICMPErrorPermitido(DatagramaIPv4 datagrama,int tipo)
	{
		// 1. Comprobamos que el tipo de ICMP sea de error, si no es asi, permitimos
		if(tipo!=3 && tipo!=5 && tipo!=11)
			return(true);
		
		// 2. Comprobamos si hay que enviar ICMP de error
		
		// 2.1 Si el datagrama es un fragmento distinto del primero, denegamos
		if(datagrama.getFragmentOffset()>0)
			return(false);
		
		// 2.2 Un datagrama enviado a la direccion de broadcast, denegamos
		for(int i=0;i<equipo.NumInterfaces();i++)
		{
		    Interfaz interfaz=equipo.getInterfaz(i);
		    if(datagrama.getDestino().EsBroadcastDe(interfaz.getIP(),interfaz.getMascara()))
		    	return(false);
		}

		// 2.3 Un mensaje de error ICMP, denegamos
		if(datagrama.getProtocol()==2048)
		{
			// tipo 3,5,11
			int tipoAux=datagrama.getByte(datagrama.getIHL()*4+0);
			if(tipoAux==3 || tipoAux==5 || tipoAux==11)
			    return(false);
		}
				
		return(true);
	}
	
	
	
	/**
	 * Envia al nivel inferior el dato especificado, fragmentandolo si es necesario
	 * y realizando las consultas necesarias a la tabla ARP (enviando peticiones...)
	 * @param dato Datos de los datos que se tienen que enviar
	 * @param enEsperaARP Flag que indica si el dato proviene de la cola de espera ARP
	 */
	private void Enviar(Dato dato, boolean enEsperaARP)
	{
	    int numEntrada;                //Numero de entrada en la tabla de rutas
	    DireccionIPv4 destino;         //Direccion IP de destino del datagrama
	    DireccionIPv4 destinoFisico;   //Direccion IP de destino o del gateway a usar
	    
		try
		{		    		    
		    // 1. Comprobamos la tabla de rutas
		    destino=(DireccionIPv4)dato.direccion;
		    numEntrada=tablaDeRutas.SiguienteSalto(destino);
	        
	        // 2. Comprobamos la ruta encontrada
	        if(numEntrada!=-1)
	        {
	        	// 2.0 Comprobamos si hay que fragmentar pero
	        	
	            // 2.1 Si la ruta es directa, obtenemos la direccion fisica del destino
	            if(tablaDeRutas.EsDirecta(numEntrada))
	            {
	        	    destinoFisico=destino; 
	            }
	        
	            // 2.2 Si la ruta es indirecta, obtenemos la direccion del gateway
	            else
	            { 
	        	    destinoFisico=tablaDeRutas.getGateway(numEntrada);
	            }
	            
	            // Ha llegado un datagrama que no es para mi, pero segun la tabla de
	            // encaminamiento, tengo que pasarmelo a mi mismo (como siguiente salto
	            // en el camino). Es una situacion absurda, que indica que mi tabla
	            // de encaminamiento esta mal
	            if(EsMiDireccion(destinoFisico))   //¿error en la tabla de encaminamiento?
	            {
	                if(enEsperaARP)                //eliminamos el dato de la cola de
	                    enEspera.remove(dato);     // espera para que no moleste
	                
	                throw new Exception();         //salimos del metodo (break)
	            }
	            
	            
	            // 2.3 Comprobamos la tabla ARP (si procede)
                Direccion dirFisica=moduloARP.cacheARP.getDireccionFisica(destinoFisico);
                Red red=tablaDeRutas.getInterfaz(numEntrada).getRed();
                if(dirFisica==null && red.SoportaARP())
	            {                	                	
                    // Comprobamos si el datagrama ya esta en la cola de espera por ARP 
                	if(!enEsperaARP)
	                {    
	            	    // 2.3.1 Añadimos el datagrama a la cola de espera para ser enviado
	                    Objeto.DEBUG(equipo.getNombre()+": No se ha resuelto la direccion "+destinoFisico.getIP());
	                    enEspera.add(dato);
	            	
	            	    // 2.3.2 Generamos el mensaje para el moduloARP

	                    // No hacemos peticiones ARP para fragmentos distintos del primero
	                	if(dato.paquete instanceof DatagramaIPv4)
	                	{
	                		DatagramaIPv4 datagrama=(DatagramaIPv4)dato.paquete;
	                		if(datagrama.getFragmentOffset()==0)
	                		{
	    	                    Objeto.DEBUG(equipo.getNombre()+": PeticionARP");
	                            Interfaz interfaz=tablaDeRutas.getInterfaz(numEntrada);
	                            PeticionARP pARP=new PeticionARP(interfaz.getDirFisica(),interfaz.getIP(),destinoFisico);
	                            Dato datoAux=new Dato(instanteActual+1,pARP,0);
	                            //datoAux.protocolo=0;  // arp no necesita identificador del nivelIP 
	                            datoAux.interfaz=interfaz;
	    					
	                            // 2.3.3 Enviamos la peticion al modulo ARP
	                            moduloARP.ProgramarSalida(datoAux);
	                		}
	                	}
	                	else
	                	{
	                        Objeto.DEBUG(equipo.getNombre()+": PeticionARP");
                            Interfaz interfaz=tablaDeRutas.getInterfaz(numEntrada);
                            PeticionARP pARP=new PeticionARP(interfaz.getDirFisica(),interfaz.getIP(),destinoFisico);
                            Dato datoAux=new Dato(instanteActual+1,pARP,0);
                            //datoAux.protocolo=0;  // arp no necesita identificador del nivelIP 
                            datoAux.interfaz=interfaz;
					
                            // 2.3.3 Enviamos la peticion al modulo ARP
                            moduloARP.ProgramarSalida(datoAux);
	                	}
	                }
	            }
	            else
	            {
	            	//System.out.println(equipo.getNombre()+": Enviando datagrama a "+destinoFisico.getIP()+" -> "+(((Redes.Ethernet.DireccionEthernet)dirFisica).toString()));               
	            	
	            	// 2.4 Si el datagrama estaba en la cola de espera lo eliminamos de
	            	//     dicha cola
	            	if(enEsperaARP)
	            	    enEspera.remove(dato);
	            	
	                // 2.5 Comprobamos si hay que fragmentar para enviar
	                int tamDatos=dato.paquete.Tam();
	                Interfaz interfaz=tablaDeRutas.getInterfaz(numEntrada);
	                int mtu=interfaz.getRed().getMTU();
	                
	                // 2.6 Enviamos fragmentando o sin fragmentar
	                if(tamDatos<=mtu)
	            	    EnviarSinFragmentacion(dato,dirFisica,interfaz);
	            	else
	            	    EnviarConFragmentacion(dato,dirFisica,interfaz);
	            }
	        }
	        
	        // 3. No hay ruta
	        else
	        {
	            //enviar icmp 'destination unreachable'
	            EnviarICMP(3,0,dato);
	        }
		}
		catch(Exception e)
		{
		
		}
	}
	
	
	
	/**
	 * Procesa un datagrama, previamente se ha comprobado que es correcto
	 * @param dato Datos del datagrama
	 */
	private void ProcesarDatagrama(Dato dato)
	{
		try
		{
	        // 0. Inicializacion
		    DatagramaIPv4 datagrama=(DatagramaIPv4)dato.paquete;
		    boolean ipForwarding=((Boolean)parametros.getValor("IP Forwarding")).booleanValue();
		    	
            // 1. Comprobamos el destinatario del datagrama
		    if(EsParaMi(datagrama.getDestino(),dato.interfaz))
		    {
		        // 1.1 Comprobamos si es un framento
		        if(EsFragmento(datagrama))
		        {
		     	    // Añadimos el framento al buffer correspondiente
		            Reensamblar(dato.instante,datagrama);
		        }
		    
		        // 1.2 Si no es fragmento lo pasamos al nivel superior
		        else
		        {
		            PasarAlNivelSuperior(datagrama);
		        }
		    }
		    
		    // 2. Si no es para mi y esta habilitado el IP Forwarding, lo reenviamos
		    else if(ipForwarding)
		    {    
		        datagrama.setTTL(datagrama.getTTL()-1);
		        if(datagrama.getTTL()==0)
		        {
		            EnviarICMP(11,0,dato);
		        }
		        else
		        {    
		        	// TODO: Si el datagrama se tiene que enviar por la misma interfaz que ha llegado
		        	// habra que enviar un 'icmp redirect'
		        	// Hay que comprobar que el equipo que envia el datagrama y el gateway siguiente
		        	// no esten en la misma red, pq de ser asi, se deberia haber usado aquel gateway
		        	// y no este (y deberemos comunicarselo mediante el envio de un ICMP redirect)
		            dato.direccion=datagrama.getDestino();
		            Enviar(dato,false);
		        }
		    }   
		}
		catch(Exception e) 
		{
			// se ignora el datagrama si se produce algun error
		}
	}
	
	
	
	/**
	 * Procesa los datagramas reensamblados que deban ser procesados en el 
	 * instante actual
	 */
	private void ProcesarReensamblados()
	{	
	    // 0. Inicializamos
	    int reassembly_timeout=-1;
	    Integer Itimeout=(Integer)parametros.getValor("IP Reassembly Timeout");
	    if(Itimeout!=null)
	        reassembly_timeout=Itimeout.intValue();
	    if(reassembly_timeout<=0)
	        reassembly_timeout=150;
	    
	    // 1. Procesamos
		for(int i=0;i<reensamblados.size();i++)
		{
			Reensamblado r=(Reensamblado)reensamblados.get(i);
			
			// Si el datagrama no se ha terminado de reensamblar, datagrama vale null
			// excepto en el caso de que se haya recibido el framgento 0, en cuyo caso
			// es este fragmento el que devuelve
			DatagramaIPv4 datagrama=r.getDatagrama(); 
			
			if(r.ProcesoTerminado())
			{
			    // Eliminamos el reensamblado de la cola de reensamblados
				reensamblados.remove(i);				
				i--;
			
				// Apuntamos el evento
				equipo.NuevoEvento('R',instanteActual+getRetardo()+1,datagrama,"Datagrama IPv4 (reensamblado finalizado)");
				//datagrama.Contenido();
				
				Dato datoAux=new Dato(instanteActual+getRetardo()+1,datagrama,datagrama.getDF());
			    ProcesarDatagrama(datoAux);
			}
			else
			{
				// ¿Se ha cumplido el 'time out' de reensamblado?
				//  si es asi, enviar el correspondiente mensaje ICMP
				//  y eliminar el reensamblado de la lista
				//   ¿pero y si luego llegan mas trozos?
				//   solo se envia el icmp si es el primero fragmento
			    if(instanteActual-r.InstanteInicial()>=reassembly_timeout)
			    {
			        //Eliminamos el reensamblado de la cola de reensamblados
			        reensamblados.remove(i);
			        i--;
			        
			        if(r.FragmentoInicialRecibido())
			        {  
			            // Podemos crear un ICMP reassembly timeout con solo
			            // el primer fragmento del datagrama, ya que este
			            // contiene casi todos los datos relevantes, como ID, origen,
			            // destino, ...
			            Dato datoAux=new Dato(instanteActual,datagrama,datagrama.getDF());
			            EnviarICMP(11,1,datoAux);
			        }
			    }
			}
		}
	}	
	
	
	
	/**
	 * Comprueba si el datagrama es un fragmento de otro datagrama o no
	 * @param datagrama Datagrama IPv4
	 * @return Cierto si el datagrama es un fragmento de otro datagrama
	 */
	private boolean EsFragmento(DatagramaIPv4 datagrama)
	{
		boolean retorno=false;
		
	    // 1. Si es un fragmento debe tener el bit MF a '1'...
		if(datagrama.getMF()==1)
			retorno=true;
		
		// 2. ... o tener un valor no nulo en el campo 'desplazamiento'
		else if(datagrama.getFragmentOffset()>0)
			retorno=true;
		
		return(retorno);
	}
	
	
	
	/**
	 * Añade el fragmento a un reensamblado existente o crea uno nuevo
	 * @param instante Instante de tiempo
	 * @param fragmento Fragmento de datagrama recibido
	 */
	private void Reensamblar(int instante,DatagramaIPv4 fragmento)
	{
		boolean encontrado=false;
		
		// 1. Comprobamos si pertenece a un datagrama que ya esta siendo reensamblado
		for(int i=0;i<reensamblados.size() && !encontrado;i++)
		{
		    Reensamblado r=(Reensamblado)reensamblados.get(i);
		    
		    // extraemos el id de datagrama del primer fragmento para cotejarlo
		    if(r.getID()==fragmento.getID())
		    {
		    	// Procesamos el fragmento
		        r.NuevoFragmento(fragmento);
		        
		        encontrado=true;
		    }
		}
		
		// 2. Si el fragmento pertenece a un nuevo datagrama creamos un reensamblado
		if(!encontrado)
		{
			Reensamblado r=new Reensamblado(instante,fragmento);
			reensamblados.add(r);
		}
	}
	
	
	
    /**
     * Procesa un datagrama recibido de la red y lo envia al nivel superior
     * correspondiente
     * @param datagrama Datagrama con los datos que hay que pasar al nivel superior
     */
	private void PasarAlNivelSuperior(DatagramaIPv4 datagrama)
	{
		Nivel nivel;
		int retardo=getRetardo();
		
		// 1. Obtenemos el nivel superior que debe procesar el datagrama
		nivel=getNivelSuperior("ipv4",datagrama.getProtocol());
		if(nivel!=null)
		{		    
		    Dato datoAux=new Dato(instanteActual,datagrama,datagrama.getDF());
		    datoAux.protocolo=datagrama.getProtocol();
		    
		    // 1.1 Extraemos el campo de datos y se lo pasamos al modulo correspondiente
		    Buffer datos=new Buffer(datagrama.Tam()-datagrama.getIHL()*4);
		    for(int j=0;j<datos.Tam();j++)
		    	datos.setByte(j,datagrama.getByte(datagrama.getIHL()*4+j));
		    datoAux.paquete=datos;
		    datoAux.instante+=retardo;
		    
		    // 1.2 Es posible que el nivel superior necesite la direccion IP del origen
		    datoAux.direccion=datagrama.getOrigen();
		    
		    // 1.3 Pasamos el dato al nivel superior
		    nivel.ProgramarEntrada(datoAux);
		}
		else
		{
			//No hay ningun nivel superior que se pueda encargar de procesar
			//el datagrama recibido, enviamos mensaje icmp
		    // destination unreachable, protocolo unreachable
		    Dato datoAux=new Dato(instanteActual,datagrama,datagrama.getDF());
		    datoAux.instante+=retardo;
		    datoAux.direccion=datagrama.getOrigen();
		    //System.out.println("ICMP a: "+datagrama.getOrigen().getIP());
		    EnviarICMP(3,2,datoAux);
		}
	}	
	
	
	
	/**
	 * Comprueba si la direccion que se especifica pertenece a algun interfaz
	 * @param direccion Direccion que se va a comprobar
	 * @return Cierto si el un datagrama enviado a esa direccion debe ser procesado
	 */
	private boolean EsParaMi(DireccionIPv4 direccion,Interfaz interfaz)
	{
	    boolean esparami=false;
	    
	    // 1. Comprobacion
	    try 
	    {
	        // 1.1 Comprobamos si el datagrama va destinado al bucle local
	        if(direccion.EsLoopback())
	            esparami=true;
	        
	        // 1.2 Comprobamos las direcciones de los interfaces
	        for(int i=0;i<equipo.NumInterfaces() && !esparami;i++)
	            if(direccion.equals(equipo.getInterfaz(i).getIP()))
	                esparami=true;
	         
	        // 1.3 Comprobamos las direcciones de broadacasts de los interfaces
	        for(int i=0;i<equipo.NumInterfaces() && !esparami;i++)
	        {
	            Interfaz interfazEquipo=equipo.getInterfaz(i);
	            MascaraIPv4 mascara=interfazEquipo.getMascara();
	            if(direccion.equals(interfazEquipo.getIP().getIPdeBroadcast(mascara)))
	                esparami=true;
	        }
	        
	        // 1.4 Comprobar multicasts
	        if (moduloIGMP != null && interfaz != null) {
	        	esparami = moduloIGMP.esParaMi(direccion,interfaz);
	        }
	    }
	    catch(Exception e)
	    {
	        // Por si algun componente es 'null'
	    }
	    
	    // 2. Devolucion del resultado
	    return(esparami);
	}
	
	
	
	/**
	 * Comprueba si la direccion que se pasa pertenece al equipo
	 * @param direccion Direccion IPv4
	 * @return Cierto si la direccion pertenece al equipo
	 */
	private boolean EsMiDireccion(DireccionIPv4 direccion)
	{
	    boolean esmia=false;
	    
	    // 1. Comprobacion
	    try 
	    {
	        for(int i=0;i<equipo.NumInterfaces() && !esmia;i++)
	            if(direccion.equals(equipo.getInterfaz(i).getIP()))
	                esmia=true;
	    }
	    catch(Exception e)
	    {
	        // Por si algun componente es 'null'
	    }
	    
	    // 2. Devolucion del resultado
	    return(esmia);
	}
	
	
	
	/**
	 * Enviamos un datagrama al nivel inferior sin usar fragmentacion
	 * @param dato Datos del datagrama
	 * @param dirFisica Direccion fisica del destino para en nivel inferior
	 * @param interfaz Interfaz por la que se enviaran los datos
	 */
	private void EnviarSinFragmentacion(Dato dato,Direccion dirFisica, Interfaz interfaz)
	{
		// 1. Creamos un datagrama IP para encapsular los datos
		DatagramaIPv4 datagrama;
		if(dato.paquete instanceof DatagramaIPv4)
		{    
		    datagrama=(DatagramaIPv4)dato.paquete;
		}
		else    
		{    
		    DireccionIPv4 origen=interfaz.getIP();
		    DireccionIPv4 destino=(DireccionIPv4)dato.direccion;
		    datagrama=new DatagramaIPv4(origen,destino,dato.paquete);
		    datagrama.setProtocol(dato.protocolo);
		    if(dato.fragmentable==false)
		    	datagrama.setDF(1);
		    else
		    	datagrama.setDF(0);
		    
		    datagrama.setProtocol(dato.protocolo);
		}
		
		// 2. Creamos el objeto con la informacion para el nivel inferior
		Dato datoAux=new Dato(instanteActual,datagrama,datagrama.getDF());
		datoAux.direccion=dirFisica;   // destino en el nivel inferior
		datoAux.instante+=getRetardo();
		datoAux.protocolo=getID(interfaz.getNivelEnlace().ID());
		datoAux.interfaz=interfaz;
		
		// 3. Apuntamos el evento
		equipo.NuevoEvento('E',datoAux.instante,datagrama,"Datagrama IPv4 (sin fragmentar)");
		//System.out.println("fragment ID: "+datagrama.getID()+"  offset: "+datagrama.getFragmentOffset()*8);
		//System.out.println("  total length: "+datagrama.getTotalLength());
		//datagrama.Contenido();
		
		// 4. Enviamos los datos al nivel inferior
		interfaz.getNivelEnlace().ProgramarSalida(datoAux);
	}
	
	
	
	/**
	 * Enviamos un datagrama al nivel inferior usando fragmentacion
	 * @param dato Datos del datagrama
	 * @param dirFisica Direccion fisica del destino para en nivel inferior
	 * @param interfaz Interfaz por la que se enviaran los datos
	 */	
	private void EnviarConFragmentacion(Dato dato, Direccion dirFisica, Interfaz interfaz)
	{   
	    // 0. Inicializacion
		boolean fragmentable=true;
	    DatagramaIPv4 datagrama;
	    if(dato.paquete instanceof DatagramaIPv4)
	    {     
	        datagrama=(DatagramaIPv4)dato.paquete;
	        
	        // el datagrama se podra fragmentar si si bit DF esta a 0
	        // ignoramos por tanto el valor de dato.fragmentable
	        if(datagrama.getDF()==1)
	            fragmentable=false;
	        else
	        	fragmentable=true;
	    }
	    else
	    {    
	        DireccionIPv4 origen=interfaz.getIP();
	        DireccionIPv4 destino=(DireccionIPv4)dato.direccion;
	        datagrama=new DatagramaIPv4(origen,destino,dato.paquete);
	        
	        // Actualizamos el campo protocolo con el que nos indique la estructura 'Dato'
	        datagrama.setProtocol(dato.protocolo);
	        
	        // si se ha especificado que la informacion se puede fragmentar,
	        // lo permitimos
	        fragmentable=dato.fragmentable;
	    }
	    int numBloques=(interfaz.getRed().getMTU()-(datagrama.getIHL()*4))/8;
	    int numFragmentos=(int)Math.ceil((datagrama.getTotalLength()-datagrama.getIHL()*4)/(numBloques*8.0));
	    
	    if(fragmentable)
	        datagrama.setDF(0);
	    else
	    	datagrama.setDF(1);
	    
	    // 1. Comprobamos si los datos se pueden fragmentar
	    if(datagrama.getDF()==0)
	    {
	    	// 1.1 Creamos y enviamos el primero fragmento
            if(!SimularError(ErroresIPv4.flags[0]))
	            EnviarPrimerFragmento(instanteActual,datagrama,dirFisica,interfaz);    
	        
	        // 1.2 Creamos el segundo fragmento
	    	for(int nFragmento=1;nFragmento<numFragmentos;nFragmento++)
	    	{    
	    	    if(nFragmento==1 && !SimularError(ErroresIPv4.flags[1]))
	        	    EnviarFragmento(instanteActual,nFragmento,datagrama,dirFisica,interfaz);
	    	    else if(nFragmento>1)
	    	        EnviarFragmento(instanteActual,nFragmento,datagrama,dirFisica,interfaz); 
	    	}
	    }
	    
	    // 2. Los datos no se pueden fragmentar
	    else
	    {
	    	//enviar icmp 'fragmentation needed and DF set'
		    Dato datoAux=new Dato(instanteActual,datagrama,0);
		    datoAux.instante+=getRetardo();
		    datoAux.direccion=datagrama.getOrigen();
		    EnviarICMP(3,4,datoAux);
	    }
	}
	
	
	
	/**
	 * Envia el primero fragmento del datagrama al destino
	 * @param instante Instante de tiempo
	 * @param datagrama Datagrama original, sin fragmentar
	 * @param dirFisica Direccion fisica del destino
	 * @param interfaz Interfaz por la que se enviara el fragmento
	 */
	private void EnviarPrimerFragmento(int instante,DatagramaIPv4 datagrama, Direccion dirFisica, Interfaz interfaz)
	{
		int MTU=interfaz.getRed().getMTU();   //Maximum Transmission Unit
		int IHL=datagrama.getIHL();           //Internet Header Length
		int NFB;                              //Number of Fragment Blocks (que caben en una mtu)
		int tam_fragmento;
		DatagramaIPv4 fragmento;
		Buffer datos;
		
		// (3) NFB <- (MTU-IHL*4)/8;  (tam total-cabecera)/8bytes
		NFB=(MTU-(IHL*4))/8;
		tam_fragmento=NFB*8;
			
		// (1) Copiamos la cabecera original
		// (4) Attach the first NFB*8 data octets;
		datos=new Buffer(tam_fragmento);
		for(int i=0;i<tam_fragmento;i++)
			datos.setByte(i,datagrama.getByte(datagrama.getIHL()*4+i));
		fragmento=new DatagramaIPv4(datagrama,datos);
		fragmento.setDF(0);
		
		// (5)  Correct the header: MF <- 1;  TL <- (IHL*4)+(NFB*8); Recompute Checksum;
		fragmento.setMF(1);
		fragmento.setTotalLength((IHL*4)+tam_fragmento);
		fragmento.setHeaderChecksum(fragmento.CalculaSumaDeComprobacion());
		
		// nota: el fragment offset es el del datagrama original... si es un fragmento distinto
		// del primero en el subfragmento que acabamos de crear estara el offset correcto
		
		// (6)  Submit this fragment to the next step in datagram processing;
		Dato datoAux=new Dato(instante,fragmento,0);
		datoAux.direccion=dirFisica;
		datoAux.instante+=getRetardo();
		datoAux.protocolo=getID(interfaz.getNivelEnlace().ID());
		datoAux.interfaz=interfaz;
		
		// Apuntamos el evento
		equipo.NuevoEvento('E',datoAux.instante,datoAux.paquete,"Fragmento 0 de datagrama IPv4");
		//System.out.println("fragment ID: "+fragmento.getID()+"  offset: "+fragmento.getFragmentOffset()*8);
		//System.out.println("   total lenght: "+fragmento.getTotalLength());
		//fragmento.Contenido();
		
		interfaz.getNivelEnlace().ProgramarSalida(datoAux);
	}
	
	
	
	/**
	 * Envia un fragmento del datagrama al destino
	 * @param instante Instante de tiempo
	 * @param numFragmento Numero de fragmento
	 * @param datagrama Datagrama original, sin fragmentar
	 * @param dirFisica Direccion fisica del destino
	 * @param interfaz Interfaz por la que se enviara el fragmento
	 */
	private void EnviarFragmento(int instante,int numFragmento,DatagramaIPv4 datagrama,Direccion dirFisica,Interfaz interfaz)
    {
		int MTU=interfaz.getRed().getMTU();   //Maximum Transmission Unit
		int IHL=datagrama.getIHL();           //Internet Header Length
		int NFB=(MTU-(IHL*4))/8;              //Number of Fragment Blocks
		DatagramaIPv4 fragmento;
		Buffer datos;
		
		// Calculamos cuanto queda por enviar
		int resto=datagrama.getTotalLength()-datagrama.getIHL()*4-(NFB*8)*numFragmento;
		int tam_fragmento=NFB*8;
		int tam;
		int i;
		
		// (8)  Append the remaining data;
		if(resto>tam_fragmento)
		    tam=tam_fragmento;
		else
			tam=resto;
		datos=new Buffer(tam);
		for(i=0;i<tam;i++)
			datos.setByte(i,datagrama.getByte(datagrama.getIHL()*4+tam_fragmento*numFragmento+i));
			
		// (7)  Selectively copy the internet header (some options
	    //		are not copied, see option definitions); /*pasamos de las opciones*/
		fragmento=new DatagramaIPv4(datagrama,datos);
		fragmento.setDF(0);
		if(tam_fragmento*numFragmento+tam<datagrama.Tam()-datagrama.getIHL()*4)
			fragmento.setMF(1);
		else
			fragmento.setMF(0);
		
		// si el datagrama original era un fragmento con el bit MF a 1 mantenemos el bit MF a 1
		if(datagrama.getMF()==1)
			fragmento.setMF(1);
		
		fragmento.setFragmentOffset(datagrama.getFragmentOffset()+(numFragmento*tam_fragmento/8));
		fragmento.setTotalLength(fragmento.getIHL()*4+tam);
		fragmento.setHeaderChecksum(fragmento.CalculaSumaDeComprobacion());

		
		// (10)  Submit this fragment to the next step
		Dato datoAux=new Dato(instante,fragmento,0);
		datoAux.direccion=dirFisica;
		datoAux.instante+=getRetardo();
		datoAux.protocolo=getID(interfaz.getNivelEnlace().ID());
		datoAux.interfaz=interfaz;
		
        // Apuntamos el evento
		equipo.NuevoEvento('E',datoAux.instante,datoAux.paquete,"Fragmento "+numFragmento+" de datagrama IPv4");
		//System.out.println("fragment ID: "+fragmento.getID()+"  offset: "+fragmento.getFragmentOffset()*8);
		//System.out.println("   total length: "+fragmento.getTotalLength());
		//fragmento.Contenido();
		
		interfaz.getNivelEnlace().ProgramarSalida(datoAux);
	}
	
	
	
	/**
	 * Devuelve el identificador del nivel
	 * @return Identificador del nivel
	 */
	public String ID()
	{
		return("ipv4");
	}
	
	
	
	/**
	 * Comprueba si el dato esta bien formado
	 * @param dato Dato a comprobar
	 * @return Cierto si el dato es correcto
	 */
	protected boolean ComprobarEntrada(Dato dato)
	{
	    boolean correcto=false;
	    
	    if(dato!=null && dato.instante>=0 && dato.paquete!=null)
	        correcto=true;
	        
	    return(correcto);
	}
	
	
	
	/**
	 * Compruenba si el dato de salida es correcto
	 * @param dato Dato a comprobar
	 * @return Cierto si el dato es correcto
	 */
	protected boolean ComprobarSalida(Dato dato)
	{
	    boolean correcto=false;
	    
	    if(dato!=null && dato.instante>=0 && dato.paquete!=null)
	    {    
	        if(dato.paquete instanceof DatagramaIPv4)
	            correcto=true;
	        else 
	            if(dato.direccion!=null && dato.direccion instanceof DireccionIPv4)
	               if(dato.protocolo>=0)
	                  correcto=true;
	    }
	    
	    return(correcto);
	}



	public void setModuloIGMP(ModuloIGMP moduloIGMP) {
		this.moduloIGMP = moduloIGMP;
	}
}






/**
 * En teoria un buffer de reensamblado debe estar identificado por una tubla
 * <origen,destino,protocolo,identificador de datagrama> pero aqui solo
 * vamos a usar el identificador de datagrama ya que nos hemos asegurado
 * que no se van a repetir estos identificadores en ningun datagrama IPv4
 * durante toda la simulacion.
 * (ver atributo ID de la clase DatagramaIPv4)
 */
class Reensamblado
{
	/**
	 * Buffer
	 */
	private Buffer buffer;
	
	/**
	 * Instante inicial en que se inicio la reconstruccion
	 * (util para el 'reasembly timeout (icmp)'
	 */
	private int instanteInicial;
	
	/**
	 * Identificador del datagrama que se esta reensamblando
	 */
	private int datagramaID;
	
	/**
	 * Fragment Reveived Bit Table, mapa binario de bytes recibidos
	 * (ver el algoritmo en el RFC 791, pagina 28)
	 */
	private Buffer rcvbt;
	
	/**
	 * Indica si el proceso de reensamblado ha terminadp
	 */
	private boolean procesoTerminado;
	
	/**
	 * Datos de la cabecera del datagrama fragmentado
	 */
	private CabeceraIPv4 cabecera;
	
	/**
	 * Datagrama reensamblado
	 */
	private DatagramaIPv4 datagrama;
	
	/**
	 * Indicador de fragmento inicial recibido
	 */
	private boolean fragmentoInicialRecibido;
	
    /**
     * Total Data Length, Tamaño total del datagrama, que sera conocido cuando se reciba el
     * ultimo fragmento
     */
	private int TDL;
	
	
	/**
	 * Constructor
	 * @param instante Instante en que se inicia el reensamblado
	 * @param fragmento Fragmento del datagrama recibido
	 */
	public Reensamblado(int instante, DatagramaIPv4 fragmento)
	{
		procesoTerminado=false;
	    instanteInicial=instante;
	    datagramaID=fragmento.getID();
	    buffer=new Buffer(65536);
	    rcvbt=new Buffer(65536);
	    cabecera=null;
	    datagrama=null;
        fragmentoInicialRecibido=false;
	    TDL=0;
        
        //System.out.println("Creando nuevo reensamblado");
        
	    NuevoFragmento(fragmento);
	}
	
	
	
	/**
	 * Devuelve el identificador del datagrama que esta siendo reensamblado
	 * @return ID del datagrama
	 */
	public int getID()
	{
		return(datagramaID);
	}
	
	
	
	/**
	 * Comprueba si el proceso de reensamblaje ha acabado
	 * @return Cierto si el datagrama ya se ha reensamblado
	 */
	public boolean ProcesoTerminado()
	{
		return(procesoTerminado);
	}
	
	
	
	/**
	 * Devuelve el instante en que se inicio el reensamblado del datagrama
	 * @return Instente inicial de reensamblado
	 */
	public int InstanteInicial()
	{
	   return(instanteInicial);	
	}
	
	
	
	/**
	 * Procesa un nuevo fragmento recibido. Implementa el algoritmo descrito en la
	 * pagina 28 del RFC 791, a partir del paso (6)
	 * @param fragmento Fragmento del datagrama
	 */
	public void NuevoFragmento(DatagramaIPv4 fragmento)
	{
		int FO;    // Fragment Offset
		int IHL;   // Internet Header Length
		int TL;    // Total Length
		
		//System.out.println("Nuevo fragmento del datagrama: "+datagramaID);
		//fragmento.Contenido();
		
		// Precaucion, si un fragmento repetido llega despues de que se complete
		// el proceso
		if(procesoTerminado)
			return;
		
		// 0. Inicializacion
		FO=fragmento.getFragmentOffset();
		IHL=fragmento.getIHL();
		TL=fragmento.getTotalLength();
				
		// (8) Put data from fragment into data buffer with BUFID from octet FO*8 to
		//     octet (TL-(IHL*4))+FO*8;
		for(int i=FO*8;i<(TL-(IHL*4))+FO*8;i++)
		{
		    buffer.setByte(i,fragmento.getByte(fragmento.getIHL()*4+i-FO*8));

			// (9) Actualizamos el mapa de bytes recibidos
		    rcvbt.setByte(i,1);
		}
			    
	    // (10) IF MF = 0 THEN TDL <- TL-(IHL*4)+(FO*8)
	    //      Ultimo fragmento: calculamos el tamaño total del datagrama como
	    //      la suma de la long. del fragmento y el frg. offset - la cabecera
	    if(fragmento.getMF()==0)
	    	TDL=FO*8+TL-(IHL*4); 
	    
	    // (11) IF FO = 0 THEN put header in header buffer
	    if(FO==0)
	    {
	        // Copia 'a saco' de la cabecera del fragmento
	        cabecera=new CabeceraIPv4(fragmento);
	        fragmentoInicialRecibido=true;
	    	cabecera.setMF(0);              //ahora solo tenemos un frag., el datagrama
	    	cabecera.setFragmentOffset(0);  //esto ya estaba, ver FO
	    
	        datagrama=new DatagramaIPv4(fragmento);
	        datagrama.setDF(0);
	        datagrama.setMF(0); 
	        datagrama.setFragmentOffset(0);
	    }
	    
	    // (12), (13) y el resto
	    if(TDL!=0 && mapaCompleto(TDL))
	    {	
	    	// Parte de datos
	    	Buffer datos=new Buffer(TDL);
	    	for(int i=0;i<TDL;i++)
	    		datos.setByte(i,buffer.getByte(i));
	    	
	    	// Inicializamos el datagrama
	    	datagrama=new DatagramaIPv4(cabecera.getOrigen(),cabecera.getDestino(),datos);
	    	//datagrama.setDF(...), ya viene en 'datos'
	    	
	    	// Copiamos la cabecera
	    	for(int i=0;i<cabecera.getIHL()*4;i++)
	    		datagrama.setByte(i,cabecera.getByte(i));
	    
	    	datagrama.setTotalLength(TDL);
	    	
	        // Fin del proceso
	    	procesoTerminado=true;
	    }
	}
	
	
	
	/**
	 * Comprueba que el mapa de bytes recibidos esta completo
	 * @param longitud Tamaño total del datagrama
 	 * @return Ciero si el mapa esta completo (todo a unos)
	 */
	private boolean mapaCompleto(int longitud)
	{
		boolean completo=true;
		
		//Comprobamos cada bit del mapa, hasta la longitud data
		for(int i=0;i<longitud && completo;i++)
			if(rcvbt.getByte(i)==0)
			    completo=false;
			
		return(completo);
	}
	
	
	
	/**
	 * Devuelve el datagrama reensablado
	 * @return Datagrama reensablado
	 */
	public DatagramaIPv4 getDatagrama()
	{
	    return(datagrama);   
	}
	
	
	
	/**
	 * Comprueba si se ha recibido el fragmento inicial
	 * @return Cierto si el fragmento inicial se ha recibido
	 */
	public boolean FragmentoInicialRecibido()
	{
	    return(fragmentoInicialRecibido);
	}
}
