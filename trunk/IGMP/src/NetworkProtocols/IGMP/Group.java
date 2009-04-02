package NetworkProtocols.IGMP;

import Interface.Interface;
import NetworkProtocols.IP.Address.IpAddress;

public abstract class Group{
	protected IpAddress dirGroup;
	protected Interface inter;
	

	public Group(IpAddress dirGroup, Interface inter) {
		super();
		this.dirGroup = dirGroup;
		this.inter = inter;
	}

	public IpAddress getDirGroup() {
		return dirGroup;
	}

	public void setDirGroup(IpAddress dirGroup) {
		this.dirGroup = dirGroup;
	}
	
	public String toString(){
		return dirGroup.toString();
	}
}
