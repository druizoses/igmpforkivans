import Exceptions.AddressException;
import NetworkProtocols.IGMP.IGMP;
import NetworkProtocols.IGMP.IGMPMessage;
import NetworkProtocols.IP.Address.IpAddress;


public class Probando1 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IGMPMessage m1 = null;
		try {
			m1 = new IGMPMessage(IGMP.Membership_Report_v2,(byte)10,0,new IpAddress("224P2P2P3"));
			m1.createChecksum();
		} catch (AddressException e) {
			e.printStackTrace();
		}
		System.out.println(m1);
		IGMPMessage m2 = new IGMPMessage(m1.toByte());
		System.out.println(m2);
		
	}

}
