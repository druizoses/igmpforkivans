package NetworkProtocols;

import Interface.Interface;
import NetworkProtocols.IGMP.IGMPMessage;

public class IpIgmpIndication {
	private IGMPMessage paquete;
	private Interface inter;
	
	public IpIgmpIndication(IGMPMessage paquete, Interface inter) {
		super();
		this.paquete = paquete;
		this.inter = inter;
	}

	public IGMPMessage getPaquete() {
		return paquete;
	}

	public void setPaquete(IGMPMessage paquete) {
		this.paquete = paquete;
	}

	public Interface getInter() {
		return inter;
	}

	public void setInter(Interface inter) {
		this.inter = inter;
	}
	
	

}
