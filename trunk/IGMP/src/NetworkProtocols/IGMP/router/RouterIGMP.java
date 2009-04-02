package NetworkProtocols.IGMP.router;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import Exceptions.AddressException;
import Interface.Interface;
import NetworkProtocols.IpIgmpIndication;
import NetworkProtocols.IGMP.IGMP;
import NetworkProtocols.IGMP.IGMPMessage;
import NetworkProtocols.IGMP.util.Timer;
import NetworkProtocols.IGMP.util.Timerable;
import NetworkProtocols.IP.IpIndication;
import NetworkProtocols.IP.Address.IpAddress;

public class RouterIGMP extends IGMP implements Timerable{
 	
	
	static int Estado_Sin_Miembros = 0;
	static int Estado_Con_Miembros = 1;
	static int Estado_Chequeo_Miembros = 2;
	public static int Timer_Query_Specific = 125000;
	/**
	 * MRT_value - 128, ya que el tipo byte de java tiene un rango de
	 * -128 a 127.
	 * Al momento de usarse este valor, debe sumarsele 128.
	 */
	public static byte MRT_Query_Specific = (byte) 100-128;
	Timer t_query_specific;
	/**
	 * Esta colección contiene una lista de grupos por cada interfaz.
	 */
	Hashtable<IpAddress, ArrayList<Interface> > hashGroups;
	Hashtable<Interface, Hashtable<IpAddress, GroupRouter> > hashInterGroup;
	
	public RouterIGMP(){
		hashGroups = new Hashtable<IpAddress, ArrayList<Interface>>();
		hashInterGroup = new Hashtable<Interface, Hashtable<IpAddress,GroupRouter>>();
		t_query_specific = new Timer(Timer_Query_Specific, 1, this);
	}
	
	/**
	 * Agrega una interface al router.
	 * @param inter
	 */
	public void addInterface(Interface inter){
		hashInterGroup.put(inter, new Hashtable<IpAddress, GroupRouter>());
	}

	/**
	 * Este método es invocado cuando llega algo del nivel inferior, y nos es comunicado por ip.
	 */
	public void recibirPaqueteDeIP(IpIgmpIndication x) {
		Interface inter = x.getInter();
		IGMPMessage paquete = x.getPaquete();
		IpAddress dirGrupo = paquete.getDirGrupo();
		int tipo = paquete.getTipo();		
		switch(tipo){
		case IGMP.Membership_Report_v2: {			
			if (hashInterGroup.get(inter).containsKey(dirGrupo)){
				/*
				 * si la interface por la que vino el paquete, ya contiene a este grupo.
				 */
				Mostrar("Llego un Report para "+dirGrupo+" por "+inter);
				hashInterGroup.get(inter).get(dirGrupo).llegoReport();
			}
			else
				/*
				 * sino, tengo que crear un nuevo grupo para esta interface
				 */
				this.join(dirGrupo, inter);
			break;
		}
		
		case IGMP.Membership_Leave_Group: {
			// Verificamos que esta interfaz ya contenga este grupo.
			Mostrar("Llego un Leave para "+dirGrupo+" por "+inter);
			if (hashInterGroup.get(inter).containsKey(dirGrupo))
				hashInterGroup.get(inter).get(dirGrupo).llegoLeave();
			break;
		}
		}
		Cambios();
	}

	/**
	 * Manda un query a través de una interfaz. La direccion de destino, para IP, la saca
	 * del frame.
	 * @param frame
	 * @param inter
	 */
	public void EnviarQueryEspecifico(IGMPMessage frame, Interface inter, IpAddress IpDest){
		Mostrar("Enviando query para el destino " + IpDest+" por "+inter);
		IpIndication x = new IpIndication();
		x.setDatos(frame.toByte());
		x.setDirDestino(IpDest);
		x.setInter(inter);
		super.miIp.addLoc(x);
	}
	
	/**
	 * Manda un query general a todas las interfaces.
	 */
	public void EnviarQueryGeneral(){
		Enumeration<Interface> inters = hashInterGroup.keys();
		IpAddress ipDest=null;
		try {
			ipDest = new IpAddress("224P0P0P1");
		} catch (AddressException e1) {
			e1.printStackTrace();
		}
		for (; inters.hasMoreElements();){
			Interface inter = inters.nextElement();
			IGMPMessage frame = null;
			try {
				frame = new IGMPMessage(IGMP.Membership_Query,MRT_Query_Specific,0,new IpAddress("0P0P0P0"));
			} catch (AddressException e) {
				e.printStackTrace();
			}
			frame.createChecksum();
			EnviarQueryEspecifico(frame, inter,ipDest);
		}
	}
	
	/**
	 * Este método es utilizado por los grupos, para avisar que uno de ellos,
	 * en alguna interfaz, se liberó. Si era la ultima interface interesada en
	 * ese grupo, se elimina el mismo del router.
	 * @param dirGrupo: grupo liberado.
	 * @param inter: en qué interface se liberó.
	 */
	public void leave(IpAddress dirGrupo, Interface inter){
		ArrayList<Interface> interfaces = hashGroups.get(dirGrupo);
		interfaces.remove(inter);
		if (interfaces.size() == 0){
			Mostrar("Se elimino el grupo "+dirGrupo);
			hashGroups.remove(dirGrupo); //Se elimina este grupo del router.
			Cambios();
		}
		
		hashInterGroup.get(inter).remove(dirGrupo);
	}
	
	/**
	 * Este método es utilizado por esta clase, cuando llega un report de algún grupo 
	 * el cual es el primero para esa interfaz.
	 * @param dirGrupo
	 * @param inter
	 */
	public void join(IpAddress dirGrupo, Interface inter){
		Mostrar("Llegó Report para "+dirGrupo+" por "+inter);
		if (hashGroups.containsKey(dirGrupo)){ //si el grupo ya existe en el router
			ArrayList<Interface> interfaces = hashGroups.get(dirGrupo);
			if(!interfaces.contains(inter))
				interfaces.add(inter);
		}
		else{
			Mostrar("Nuevo grupo en el router "+dirGrupo);
			ArrayList<Interface> aux = new ArrayList<Interface>();
			aux.add(inter);
			hashGroups.put(dirGrupo, aux);
		}
		
		GroupRouter group = new GroupRouter(this, dirGrupo, inter);
		hashInterGroup.get(inter).put(dirGrupo, group );
	}
	
	/**
	 * Este método, en el router, no tendría que tener cuerpo.
	 */
	protected void ProcesarPeticion(IgmpIndication x) {	}


	public void ejecutarTarea(int tarea) {
		if (tarea == 1){
			EnviarQueryGeneral();
			t_query_specific = new Timer(Timer_Query_Specific, 1, this);
		}
	}

	public boolean InteresaIp(IpAddress dirIp) {
		return true;
	}

	public String toString(){
		return hashGroups.keys().toString();
	}

	public String[] getGroups() {
		String[] salida = new String[hashGroups.size()];
		Enumeration<IpAddress> list = hashGroups.keys();
		int i=0;
		while (list.hasMoreElements()){
			IpAddress aux = list.nextElement();
			salida[i] = aux.toString() + " ("+hashGroups.get(aux).size()+")";
			i++;
		}
		return salida;
	}

	public String[] getGroupsByInterface(Interface inter) {
		String[] salida = new String[hashInterGroup.get(inter).size()];
		Enumeration<IpAddress> list = hashInterGroup.get(inter).keys();
		int i=0;
		while (list.hasMoreElements()){
			IpAddress aux = list.nextElement();
			salida[i] = aux.toString();
			i++;
		}
		return salida;
	}

	public String[] getOnlyGroups() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void Terminar(){
		Enumeration<Interface> inters = hashInterGroup.keys();
		for (; inters.hasMoreElements();)
			inters.nextElement().Terminar();
		super.Terminar();
	}
}
