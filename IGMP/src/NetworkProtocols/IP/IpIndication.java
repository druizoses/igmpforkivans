package NetworkProtocols.IP;

import Interface.Interface;
import NetworkProtocols.IP.Address.IpAddress;

public class IpIndication {
	byte[] datos;
	Interface inter;
	IpAddress dirDestino;
	
	public IpIndication(byte[] datos, Interface inter, IpAddress dirDestino) {
		super();
		this.datos = datos;
		this.inter = inter;
		this.dirDestino = dirDestino;
	}

	public IpAddress getDirDestino() {
		return dirDestino;
	}

	public void setDirDestino(IpAddress dirDestino) {
		this.dirDestino = dirDestino;
	}

	public IpIndication() {
	}
	
	public byte[] getDatos() {
		return datos;
	}
	public void setDatos(byte[] datos) {
		this.datos = datos;
	}
	public Interface getInter() {
		return inter;
	}
	public void setInter(Interface inter) {
		this.inter = inter;
	}
}
