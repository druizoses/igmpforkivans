package NetworkProtocols.IGMP;

import java.util.Enumeration;
import java.util.Hashtable;

import NetworkProtocols.IP.Address.IpAddress;



public class GroupList {
	Hashtable<IpAddress, Uniones> hash;
	
	class Uniones{
		Group group;
		int cant;
		
		public Uniones(){
			cant=1;
		}
		
		public void add(){
			cant++;
		}
		
		public void leave(){
			cant--;
		}

		public Group getGroup() {
			return group;
		}

		public void setGroup(Group group) {
			this.group = group;
		}
	}
	
	
	public GroupList(){
		this.hash = new Hashtable<IpAddress, Uniones>();
	}
	
	public boolean addGroup(Group group){
		if (hash.containsKey(group.getDirGroup())){
			hash.get(group.getDirGroup()).add();
			return false;
		}
		else{
			Uniones aux = new Uniones();
			aux.setGroup(group);
			hash.put(group.getDirGroup(), aux);
			return true;
		}
	}
	
	public void removeGroup(Group group){
		removerGroup(group.getDirGroup());
	}
	
	/**
	 * Devuelve true si se elimina el grupo de la lista
	 * @param group
	 * @return
	 */
	public Group removerGroup(IpAddress group){
		Uniones aux = hash.get(group);
		if (aux.cant == 1){
			hash.remove(group);
			return aux.group;
		}
		else
			aux.leave();
		return null;
	}
	
	public Group getGroup(IpAddress group){
		return hash.get(group).getGroup();
	}
	
	public int size(){
		return hash.size();
	}
	
	public Enumeration<IpAddress> getDirGroups(){
		return hash.keys();
	}
	
	public boolean contains(IpAddress group){
		return hash.containsKey(group);
	}

	public String[] getGroups() {
		String[] salida = new String[hash.size()];
		Enumeration<IpAddress> list = hash.keys();
		int i=0;
		while (list.hasMoreElements()){
			IpAddress aux = list.nextElement();
			salida[i] = aux.toString() + " ("+hash.get(aux).cant+")";
			i++;
		}
		return salida;
	}

	public String[] getOnlyGroups() {
		String[] salida = new String[hash.size()];
		Enumeration<IpAddress> list = hash.keys();
		int i=0;
		while (list.hasMoreElements()){
			IpAddress aux = list.nextElement();
			salida[i] = aux.toString(); 
			i++;
		}
		return salida;
	}
}
