package Link;

import java.net.*;
import java.util.ArrayList;

import Exceptions.*; 
import Interface.*;
import NetworkProtocols.IGMP.util.Semaphore;
  
public class Link {
  // Socket y direcciones local y remota del soporte UDP
  InetAddress localaddr = null;    // Direccion local del UDP
  public int localport = 0;;
  InetAddress remoteaddr = null;   // Direccion remota del UDP
  public int remoteport = 0;
  DatagramSocket socket;	   // Socket UDP soporte de comunicaciones sobre el link
  Linkreceiver receiver;           // Thread receptor del link
  Linksender sender ;              // Thread emisor del link
  Interface intf = null;           // Interfaz asociada al link
  public Semaphore hayTarea;

  // Crea el link, asociandolo con las direcciones local y remota
  // (esto cambiara con links de acceso multiple)
  public Link(String laddr, String lport, String raddr, String rport) throws NodeException {

    //getremoteaddr(laddr, lport);
    //getlocaladdr(raddr, rport);
	getremoteaddr(raddr, lport);
	getlocaladdr(laddr, rport);

    try {
      socket = new DatagramSocket(localport,localaddr);    // Crea el socket
    }
    catch(SocketException e) {
      String d = localaddr.getHostName() + " - " + localaddr.getCanonicalHostName() + ":" + localport;
      throw new NodeException(NodeException.SOCKETERROR, d);
    }

    receiver = new Linkreceiver(this, socket);                         //Thread recepcion
    sender = new Linksender(this, socket, remoteaddr, remoteport);     // Thread emision
    //System.out.println("Creado link, origen: "+laddr+":"+lport+"  Destino: "+raddr+":"+rport);
  }

  // Configura la interfaz local asociada al link
  public void setInterface(Interface inf) { 
    intf = inf;
    this.hayTarea = inf.getHayTarea();
    sender.setQueue(intf.getsendQueue());
    inf.setLink(this);
    }

  // Agrega un segmento a la cola de segmentos de control
  // El frame recibido tiene ya la encapsulacion a nivel link, realizada por la interfaz
//  public void addFrame(byte[] frame) {sender.addFrame(frame);}

  // Recepcion de frame, invoca a la interfaz
  public void receive( byte[] bb) {
    //System.out.println("recibe en LINK"+ bb[4]+bb[5]+bb[6]+bb[7]);
    if (intf != null) intf.receive(bb);
  }

  // A partir de los strings direccion-port obtiene las direcciones (InetAddress y ports)
  public void getlocaladdr(String addr, String prt) throws NodeException {
   try {
     localport = Integer.parseInt(prt);
     if (addr.equalsIgnoreCase("localhost"))
    	 localaddr = InetAddress.getByName(addr);
     else{
    	 String[] auxStr = split(addr);
    	 byte[] aux = new byte[4];
    	 for (int i=0; i<auxStr.length; i++)
    		 aux[i] = (byte)Integer.parseInt(auxStr[i]);
    	 localaddr = InetAddress.getByAddress(aux);
     }
   }
   catch(UnknownHostException e) {
     throw new NodeException(NodeException.UNKNOWNHOST, addr);
   }
   catch(NumberFormatException e)  {
     throw new NodeException(NodeException.BADPORT, prt);
   }
  }

  private String[] split(String str){
	  ArrayList<String> list = new ArrayList<String>();	  
	  int i=0;	  
	  while(i<str.length()){
		  String aux="";
		  while (i<str.length() && str.charAt(i) != '.'){
			  aux= aux+str.charAt(i);
			  i++;
		  }
		  list.add(aux);
		  i++;
	  }
	  String[] salida= new String[list.size()];
	  for (int j = 0; j < salida.length; j++) {
		salida[j] = list.get(j);
	  }
	  return salida;
  }
  
  // A partir de los strings direccion-port obtiene las direcciones (InetAddress y ports)
  public void getremoteaddr(String addr, String prt) throws NodeException {
   try {
     remoteport = Integer.parseInt(prt);
     if (addr.equalsIgnoreCase("localhost"))
    	 remoteaddr = InetAddress.getByName(addr);
     else{
    	 String[] auxStr = split(addr);
    	 byte[] aux = new byte[4];
    	 for (int i=0; i<auxStr.length; i++)
    		 aux[i] = (byte)Integer.parseInt(auxStr[i]);
    	 remoteaddr = InetAddress.getByAddress(aux);
     }
   }
   catch(UnknownHostException e) {
     throw new NodeException(NodeException.UNKNOWNHOST, addr);
   }
   catch(NumberFormatException e)  {
     throw new NodeException(NodeException.BADPORT, prt);
   }
  }
  
  public void Terminar(){
	  receiver.Terminar();
	  sender.terminar();
  }

public int getLocalport() {
	return localport;
}

public int getRemoteport() {
	return remoteport;
}
}



