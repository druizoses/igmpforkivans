/**
 * 
 */
package Redes.IPv4.IGMP;

import Redes.Buffer;

/**
 * Mensaje IGMP
 */
public class MensajeIGMP extends Buffer {

	/*
	 * Membership Query
	 * There are two sub-types of Membership Query messages:
     *   - General Query, used to learn which groups have members on an
     *     attached network.
     *   - Group-Specific Query, used to learn if a particular group
     *     has any members on an attached network.
	 */
	public static final byte MEMBERSHIP_QUERY = 0x11;
	
	/*
	 * Version 1 Membership Report
	 */
	public static final byte MEMBERSHIP_REPORT_V1 = 0x12;

	/*
	 * Version 2 Membership Report
	 */
	public static final byte MEMBERSHIP_REPORT_V2 = 0x16;
	
	/*
	 * Leave Group
	 */
	public static final byte MEMBERSHIP_LEAVE_GROUP = 0x17;
	
    /**
     * Constructor de copia
     * @param buffer Buffer a copiar
     */
	public MensajeIGMP(Buffer buffer) {
		super(buffer);
	}

}
