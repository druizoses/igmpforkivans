import Exceptions.AddressException;
import Exceptions.NodeException;
import Interface.Interface;
import Interface.Interfaces;
import Link.Link;
import NetworkProtocols.NetworkProtocols;
import NetworkProtocols.IGMP.router.RouterIGMP;
import NetworkProtocols.IP.IP;
import NetworkProtocols.IP.Address.IpAddress;
import NetworkProtocols.IP.Address.Mask;


public class MainRouterIGMP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Interfaces inter = new Interfaces();
		Interface inter1 = null;
		Interface inter2 = null;
		Link link1 = null;
		Link link2 = null;
		IpAddress ipLocal1 = null;
		IpAddress ipLocal2 = null;
		
		try {
			ipLocal1 = new IpAddress("201P0P0P1");
			ipLocal2 = new IpAddress("201P0P1P1");
		} catch (AddressException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			link1 = new Link("localhost","6000","localhost","6001");
			link2 = new Link("localhost","6002","localhost","6003");
		} catch (NodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			inter1 = inter.addInterface("Interface 1",ipLocal1, new Mask(24),1500);
			inter2 = inter.addInterface("Interface 2",ipLocal2, new Mask(24),1500);
			link1.setInterface(inter1);
			link2.setInterface(inter2);
		} catch (NodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			IP ip = new IP(inter);
			ip.setIpLocal(ipLocal1);
			NetworkProtocols.addProtocol(NetworkProtocols.PROTO_IP, ip);
			RouterIGMP router = new RouterIGMP();
			router.addInterface(inter1);
			router.addInterface(inter2);
			NetworkProtocols.addProtocol(NetworkProtocols.PROTO_IGMP, router);
		} catch (NodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
