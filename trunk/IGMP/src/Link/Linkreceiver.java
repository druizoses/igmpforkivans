package Link;

import java.net.*;
import java.io.*;

import Exceptions.*; 
  
 /**
  * acepta frames (via UDP) provenientes de la otra punta del link (esto cambiara para links
 de acceso multiple) y los entrega a la interfaz, quien se encargara de desencapsular y determinar
 a quien debe pasarlos (IP, ARP, etc)
 */
public class Linkreceiver extends Thread  {
  DatagramSocket socket = null;
  DatagramPacket dgpacket;
  byte buff[];
  Link lnk;
  // Esto podria cambiar si es un link de acceso multiple
  int remport;
  InetAddress remaddr;
  boolean terminar=false;

  public Linkreceiver(Link l, DatagramSocket sk) throws NodeException {
    socket = sk;
    lnk = l;
    buff = new byte[20000];
    dgpacket = new DatagramPacket(buff, 20000);
    this.start();
  }

  public void run() {
    try {
      while (!terminar) {
        socket.receive(dgpacket);
        byte[] bb = new byte[dgpacket.getLength()];
        System.arraycopy(buff, 0 , bb, 0, dgpacket.getLength());
        // aca entregar frame al Link, que lo entrega a la interfaz
        //System.out.println("Link recibió algo ");
        lnk.receive(bb);
      }
    }
    catch (SocketException se) {
    }
    catch (IOException ioex) {
      //System.out.println("Exception in DCCPreceiver.");
      socket.close();
      //aca mandar la exception
    }
    //System.out.println("DCCPreceiver thread finished.");
  }

  public void close() throws IOException {
    socket.close();
  }
  
  public void Terminar(){
	  terminar = true;
	  socket.close();
	  
  }
}
