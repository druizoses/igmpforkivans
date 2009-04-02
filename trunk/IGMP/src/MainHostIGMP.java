import Exceptions.AddressException;
import Exceptions.NodeException;
import Interface.Interface;
import Interface.Interfaces;
import Link.Link;
import NetworkProtocols.NetworkProtocols;
import NetworkProtocols.IGMP.host.HostIGMP;
import NetworkProtocols.IP.IP;
import NetworkProtocols.IP.Address.IpAddress;
import NetworkProtocols.IP.Address.Mask;


public class MainHostIGMP {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Interfaces inter = new Interfaces();
		Interface inter1 = null;
		Link link1 = null;
		IpAddress ipLocal = null;
		try {
			ipLocal = new IpAddress("201P0P0P2");
		} catch (AddressException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			link1 = new Link("localhost","6001","localhost","6000");
		} catch (NodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			inter1 = inter.addInterface("Interface 2",ipLocal, new Mask(24),1500);
			link1.setInterface(inter1);
		} catch (NodeException e) {
			e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		}
		
		try {
			IP ip = new IP(inter);
			ip.setIpLocal(ipLocal);
			NetworkProtocols.addProtocol(NetworkProtocols.PROTO_IP, ip);
			HostIGMP host = new HostIGMP();
			NetworkProtocols.addProtocol(NetworkProtocols.PROTO_IGMP, host);
			IpAddress dir1 = new IpAddress("224P2P2P3");
			IpAddress dir2 = new IpAddress("224P2P2P4");
			IpAddress dir3 = new IpAddress("224P2P2P5");
			host.addGroup(dir1, inter1);
			Thread.sleep(5000);
			host.addGroup(dir2, inter1);
			Thread.sleep(5000);
			host.addGroup(dir3, inter1);
			Thread.sleep(5000);
			host.addGroup(dir1, inter1);
			Thread.sleep(5000);
			host.leaveGroup(dir1, inter1);
			Thread.sleep(5000);
			host.leaveGroup(dir2, inter1);
			Thread.sleep(5000);
			host.leaveGroup(dir3, inter1);
			Thread.sleep(5000);
			host.leaveGroup(dir1, inter1);
		} catch (NodeException e) {
			e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
