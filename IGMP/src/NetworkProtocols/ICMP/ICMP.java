package NetworkProtocols.ICMP;

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
import NetworkProtocols.ARP.*;

//

public class ICMP implements ProtocolInterface {

  public ICMP(Interfaces ifs, NetworkProtocols nt) throws NodeException {
  }

  // Metodo invocado por la interfaz para agregar un evento a la cola remota
  public void addRem(N2N3Indication x) { }

  // Metodo invocado por el nivel superior para agregar un evento a la cola local
  public void addLoc(Object x) { }
}



