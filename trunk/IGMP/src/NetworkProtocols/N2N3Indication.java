package NetworkProtocols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.net.*;

import utils.*;

import Exceptions.*; 
import Interface.*;
import Link.*;
import NetworkProtocols.*;
  import NetworkProtocols.IP.*;
    import NetworkProtocols.IP.Address.*;
    import NetworkProtocols.IP.RoutingTable.*;
  import NetworkProtocols.ICMP.*;
import NetworkProtocols.ARP.*;
/**
Esta clase define las primitivas intercambiadas entre las entidades de nivel 2 (link layer) y las
de nivel 3 (network layer). Las primeras serian los drivers, en este caso representados por un 
objeto Interface, y las segundas, los protocolos de nivel 3, como IP.
Debe tenerse en cuenta que estas primitivas no responden exactamente a las de una arquitectura de niveles,
sino que tambien incluyen (aunque de manera muy simplificada) elementos propios de la implementacion
*/

public class N2N3Indication  {
  
  public static final byte FRAME_RECEIVED = 0x01;
  public static final byte LINK_DOWN = 0x02;
  public static final byte LINK_UP = 0x03;

  byte control;
  /**
   * Datagram IP
   */
  byte[] info;
  Interface inter;

  public N2N3Indication(byte ctl, byte[] cnt, Interface inter) {
    control = ctl;
    info = cnt;
    this.inter = inter;
  }
  
  public byte getControl() {return control;}
  
  public byte[] getInfo() {return info;}

public Interface getInter() {
	return inter;
}

}
