/**
 * 
 */
package Redes.IPv4.IGMP;

import Redes.Buffer;
import Redes.IPv4.DireccionIPv4;

/**
 * Mensaje IGMP
 */
public class MensajeIGMP extends Buffer {

	/**
	 * Membership Query
	 * There are two sub-types of Membership Query messages:
     *   - General Query, used to learn which groups have members on an
     *     attached network.
     *   - Group-Specific Query, used to learn if a particular group
     *     has any members on an attached network.
	 */
	public static final byte MEMBERSHIP_QUERY = 0x11;
	
	/**
	 * Version 1 Membership Report
	 */
	public static final byte MEMBERSHIP_REPORT_V1 = 0x12;

	/**
	 * Version 2 Membership Report
	 */
	public static final byte MEMBERSHIP_REPORT_V2 = 0x16;
	
	/**
	 * Leave Group
	 */
	public static final byte MEMBERSHIP_LEAVE_GROUP = 0x17;
	
	/**
	 * Crea un mensaje IGMP de tipo MEMBERSHIP_QUERY
	 * @param dirGrupo grupo por el cual se realizará la consulta
	 * @return mensaje creado
	 */
	public static MensajeIGMP createMembershipQueryMessage(DireccionIPv4 dirGrupo){
		MensajeIGMP msg = new MensajeIGMP(MensajeIGMP.MEMBERSHIP_QUERY,100,0,dirGrupo);
		msg.setChecksum(msg.getChecksum());
		return msg;
	}
	
	/**
	 * Crea un mensaje IGMP de tipo MEMBERSHIP_REPORT_V1
	 * @param dirGrupo grupo al cual se desea reportar
	 * @return mensaje creado
	 */
	public static MensajeIGMP createMembershipReportV1Message(DireccionIPv4 dirGrupo){
		MensajeIGMP msg = new MensajeIGMP(MensajeIGMP.MEMBERSHIP_REPORT_V1,0,0,dirGrupo);
		msg.setChecksum(msg.getChecksum());
		return msg;
	}
	
	/**
	 * Crea un mensaje IGMP de tipo MEMBERSHIP_REPORT_V2
	 * @param dirGrupo grupo al cual se desea reportar
	 * @return mensaje creado
	 */
	public static MensajeIGMP createMembershipReportV2Message(DireccionIPv4 dirGrupo){
		MensajeIGMP msg = new MensajeIGMP(MensajeIGMP.MEMBERSHIP_REPORT_V2,10,0,dirGrupo);
		msg.setChecksum(msg.getChecksum());
		return msg;
	}
	
	/**
	 * Crea un mensaje IGMP de tipo MEMBERSHIP_LEAVE_GROUP
	 * @param dirGrupo grupo del cual se desea dar de baja
	 * @return mensaje creado
	 */
	public static MensajeIGMP createMembershipLeaveGroupMessage(DireccionIPv4 dirGrupo){
		MensajeIGMP msg = new MensajeIGMP(MensajeIGMP.MEMBERSHIP_LEAVE_GROUP,0,0,dirGrupo);
		msg.setChecksum(msg.getChecksum());
		return msg;
	}
	
    /**
     * Constructor de copia
     * @param buffer Buffer a copiar
     */
	public MensajeIGMP(Buffer buffer) {
		super(buffer);
	}
	
	/**
	 * Constructor para mensajes IGMP estandar
	 * @param tipo tipo de mensaje
	 * @param tiempoResp
	 * @param checksum
	 * @param dirGrupo
	 */
	public MensajeIGMP(byte tipo, int tiempoResp, int checksum,DireccionIPv4 dirGrupo) {
		super();
		super.Redimensiona(8);
		setTipo(tipo);
		setTiempoResp(tiempoResp);
		setChecksum(checksum);
		setDirGrupo(dirGrupo);
	}

    /**
     * Recupera el valor del campo 'tipo'
     * @return Tipo de mensaje IGMP
     */
	public int getTipo() {
		return(super.getByte(0));
	}

    /**
     * Pone un valor en el campo 'tipo' de la cabecera IGMP
     * @param tipo Tipo de mensaje
     */
	public void setTipo(int tipo) {
		super.setByte(0,tipo);
	}

    /**
     * Recupera el valor del campo 'tiempo de respuesta'
     * @return Tiempo de respuesta
     */
	public int getTiempoResp() {
		return(super.getByte(1));
	}

    /**
     * Pone un valor en el campo 'tiempo de respuesta' de la cabecera IGMP
     * @param tiempoResp Tiempo de respuesta
     */
	public void setTiempoResp(int tiempoResp) {
		super.setByte(1,tiempoResp);
	}

    /**
     * Pone un valor en el campo 'checksum' (suma de comprobacion)
     * @param suma Suma de comprobacion
     */
    public void setChecksum(int suma)
    {
        if(suma<0 || suma>65535)
            throw new IllegalArgumentException("El valor del campo suma de comprobacion no es valido");
        super.setByte(2,(suma>>8)&0x00FF);
        super.setByte(3,suma&0x00FF);    
    }

    /**
     * Devuelve la el valor de la suma de comprobacion 
     * @return Suma de comprobacion
     */
    public int getChecksum()
    {
        return((super.getByte(2)<<8)+super.getByte(3));
    }
    
    /**
     * Calcula la suma de comprobacion
     * @return Suma de comprobacion
     */
    public int CalculaSumaDeComprobacion()
    { 
        return(0);
    }  

    /**
     * Recupera el valor del campo 'direccion'
     * @return Direccion de destino del mensaje
     */
	public DireccionIPv4 getDirGrupo() {
		return new DireccionIPv4(super.getByte(4), super.getByte(5), super.getByte(6), super.getByte(7));
	}

    /**
     * Pone un valor en el campo 'direccion' de la cabecera IGMP
     * @param dirGrupo Direccion de destino del mensaje
     */
	public void setDirGrupo(DireccionIPv4 dirGrupo) {
		super.setByte(4,dirGrupo.getByte(0));
		super.setByte(5,dirGrupo.getByte(1));
		super.setByte(6,dirGrupo.getByte(2));
		super.setByte(7,dirGrupo.getByte(3));    
	}

}
