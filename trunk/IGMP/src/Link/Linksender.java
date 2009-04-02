package Link;

import java.net.*;
import java.io.*;

public class Linksender extends Thread {

  // Variables relativas al socket UDP de soporte de transmision
  DatagramSocket socket;     // Socket de envio
  int remport=0;             // Port del socket de envio
  InetAddress remaddr=null;  //IP del socket de envio
  utils.Queue TQueue;               // Cola de transmision de frames
  Link link;
  boolean terminar = false;
  

  public Linksender(Link lnk, DatagramSocket sock, InetAddress dip, int dport)  {
    socket = sock;
    this.link = lnk;
    remport = dport;
    remaddr = dip;
    //TQueue = new Utils.Queue();
    TQueue = null;
    this.start();
  }

  // Agrega un frame a la cola de frames a enviar
//  public void addFrame(byte[] frame) {
//    TQueue.pushBack(frame);
//  }

  public void setQueue(utils.Queue T) {
    TQueue = T;
  }

  private void enviar(byte[] frame)  {
    try {
    	socket.send(new DatagramPacket(frame, frame.length, remaddr, remport));
    	//System.out.println("Link envió algo ");
    }
    catch(IOException e) {
      //throw new NodeException(NodeException.EXCEPCION_ENVIO);
      //System.out.println("Excepcion al enviar");
    	socket.close();
    }
  }

  // En el ciclo, envia os frames
  public void run() {
    while (!terminar&&link!=null) {
    	if (link != null && link.hayTarea != null){
	      link.hayTarea.P();
	      if(TQueue != null) {
	        while(TQueue.size() != 0 ) {
	          byte[] frame =  (byte[]) TQueue.peekFront();
	          enviar(frame);
	          TQueue.popFront();
	        }  
	      }
    	}
    }
  }
  
  public void terminar(){
	  terminar = true;
	  link.hayTarea.Terminar();
  }
}

