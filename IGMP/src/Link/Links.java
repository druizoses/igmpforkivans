package Link;

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
  
public class Links {

  public Map<String, Link> Links = null;  // Tabla con los links

  public Links(){
    Links=new HashMap<String, Link>();
  }

  // Agrega un Link a la lista. Se deberia chequear validez
  public Link addLink(String name, String laddr, String lport, String raddr, String rport)
      throws NodeException  {
    Link ln = new Link(laddr, lport, raddr, rport);
    Links.put(name, ln);
    return ln;
  }


  public String toString() {
    Set<String> keySet = Links.keySet();
    for (String elementKey : keySet) {
      Link i = Links.get(elementKey);
      //System.out.println("Link: "+ i.toString());
    }
    return "eee";
  }

}



