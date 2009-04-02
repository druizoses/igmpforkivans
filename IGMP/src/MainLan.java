import java.net.InetAddress;
import java.net.UnknownHostException;

import utils.Lan;


public class MainLan {

	/**
	 * @param args
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException {
		Lan lan = new Lan();
		InetAddress dir2 = InetAddress.getLocalHost();
		lan.addInterface(dir2, 6002, dir2, 6000);
		lan.addInterface(dir2, 6003, dir2, 6001);
		lan.addInterface(dir2, 6005, dir2, 6004);
	}

}
