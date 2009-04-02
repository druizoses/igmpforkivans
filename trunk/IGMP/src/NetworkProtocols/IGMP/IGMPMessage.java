package NetworkProtocols.IGMP;

import NetworkProtocols.IP.Address.IpAddress;

/**
 * Representa un frame IGMP v2.
 * @author Cito
 *
 */
public class IGMPMessage {
	/**
	 * Incluye Version y tipo de IGMP v1
	 * 8 bits
	 */
	private byte tipo;
	/**
	 * Tiempo máxima para recibir la respuesta. Se usa sólo en mensajes de tipo Membership Query.
	 * Especifica el valor, en décimas de segundo, que un host debe esperar como máximo para
	 * contestar a un Membership Query.
	 * 8 bits
	 */
	private byte tiempoResp = 100;
	/**
	 * Complemento a uno del mensaje.
	 * 16 bits
	 */
	private int checksum;
	/**
	 * Dirección del grupo
	 * 32 bits
	 */
	private IpAddress dirGrupo;
	
	
	/**
	 * Crea un frame IGMP en base a sus componentes.
	 * @param tipo
	 * @param tiempoResp
	 * @param checksum
	 * @param dirGrupo
	 */
	public IGMPMessage(byte tipo, byte tiempoResp, int checksum,
			IpAddress dirGrupo) {
		super();
		this.tipo = tipo;
		this.tiempoResp = tiempoResp;
		this.checksum = checksum;
		this.dirGrupo = dirGrupo;
	}
	
	public IGMPMessage(){
		super();
	}
	
	/**
	 * Genera un frame IGMP desde un arreglo de bytes.
	 */
	public IGMPMessage(byte[] datos){
		/* Verificar que los datos tengan 8 bytes.. */
		this.tipo = datos[0];
		this.tiempoResp = datos[1];
		this.checksum = (int) ((datos[2]<<8)&0x00ffff) + (int) (datos[3]&0x00ff);
		this.dirGrupo = new IpAddress(datos[4], datos[5], datos[6], datos[7]);
		/* Verificar el Checksum */
	}
	
	/**
	 * 
	 * @return devuelve un arreglo de 8 bytes que sería la coficación del
	 * frame igmp a bytes.
	 */
	public byte[] toByte(){
		byte[] salida = new byte[8];
		salida[0] = this.tipo;
		salida[1] = this.tiempoResp;
		salida[2] = (byte)(this.checksum >> 8);
		salida[3] = (byte)(this.checksum & 0x00ff);
		byte[] dir = this.dirGrupo.toByte();
		for (int i=0; i<4; i++)
			salida[i+4] = dir[i];
		return salida;
	}

	public byte getTipo() {
		return tipo;
	}

	public void setTipo(byte tipo) {
		this.tipo = tipo;
	}

	public byte getTiempoResp() {
		return tiempoResp;
	}

	public void setTiempoResp(byte tiempoResp) {
		this.tiempoResp = tiempoResp;
	}

	public int getChecksum() {
		return checksum;
	}

	public IpAddress getDirGrupo() {
		return dirGrupo;
	}

	public void setDirGrupo(IpAddress dirGrupo) {
		this.dirGrupo = dirGrupo;
	}

	public void createChecksum() {
		// IMPLEMENTAR
	}
	
	public String toString(){
		return "tipo: "+tipo;
	}
}
