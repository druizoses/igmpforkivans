package NetworkProtocols.IGMP.host;

import java.util.Enumeration;
import java.util.Random;

import Exceptions.AddressException;
import Interface.Interface;
import NetworkProtocols.IpIgmpIndication;
import NetworkProtocols.IGMP.GroupList;
import NetworkProtocols.IGMP.IGMP;
import NetworkProtocols.IGMP.IGMPMessage;
import NetworkProtocols.IP.IpIndication;
import NetworkProtocols.IP.Address.IpAddress;


public class HostIGMP extends IGMP {
	GroupList groups;
	static int Dir_Todos_Host = (int) ( ((224<<24)&0x00ff000000)+ (1&0x00ff));
	
	public HostIGMP(){
		super();
		groups = new GroupList();
	}

	public void recibirPaqueteDeIP(IpIgmpIndication x) {
		IGMPMessage frame = x.getPaquete();
		byte tipo = frame.getTipo();
		
		switch (tipo){
		case Membership_Query:{
			/* Ahora tengo que verificar si el query general o especifico.
			 * se suma 128 para corregir el rango del byte de java.
			*/
			int timer = (frame.getTiempoResp()+128)*100;
			byte[] dir = frame.getDirGrupo().toByte();
			if (dir[0]==0 && dir[1]==0 && dir[2]==0 && dir[3]==0){
				/* es un query general */
				Mostrar("Llegó un Query general.");
				PrepararAllReport(timer);
			}
			else{
				/* Suponiendo que la es una direccion de grupo valida, es un query específico */
				IpAddress dirGrupo = frame.getDirGrupo();
				Mostrar("Llegó un Query específico para el grupo "+dirGrupo);
				GroupHost group = (GroupHost) groups.getGroup(dirGrupo);
				PrepararReport(group, timer);
			}
			break;
		}
		
		case Membership_Report_v2:{
			/* Si es un report de un grupo el tiene un timer, cancelo el timer. */
			GroupHost group = (GroupHost) groups.getGroup(frame.getDirGrupo());
			Mostrar("Llegó Membership report Versión 2 para el grupo "+group);
			group.llegoReport();
			break;
		}
		
		default:{
			/* Llegó otra cosa que no le tengo que dar bola. */
			break;
		}
		}
	}
	
	private void PrepararAllReport(int timerMax){
		Random rnd = new Random();
		
		for(Enumeration<IpAddress> e = groups.getDirGroups(); e.hasMoreElements();){
			GroupHost group = (GroupHost) groups.getGroup(e.nextElement());
			int timer = Math.abs((int)rnd.nextInt(timerMax));
			PrepararReport(group, timer);
		}
	}
	
	private void PrepararReport(GroupHost group, int timer){
		group.iniciarTimer(timer);
	}


	/**
	 * Método utilizado por los grupos escuchados por este host, para 
	 * avisarle que se cumplio el timer, y tiene que mandar el report.
	 * @param group: grupo que le aviso.
	 */
	public void enviarReport(GroupHost group, Interface inter){
		Mostrar("Enviando Report para "+group.getDirGroup());
		IGMPMessage frame = new IGMPMessage();
		frame.setTipo(Membership_Report_v2);
		frame.setTiempoResp((byte)10);
		frame.setDirGrupo(group.getDirGroup());
		frame.createChecksum();
		IpIndication paquete = new IpIndication();
		paquete.setDatos(frame.toByte());
		paquete.setInter(inter);
		paquete.setDirDestino(group.getDirGroup());
		miIp.addLoc(paquete);
	}
	
	public void enviarLeave(GroupHost group, Interface inter){
		Mostrar("Enviando Leave para "+group.getDirGroup());
		IGMPMessage frame = new IGMPMessage();
		frame.setTipo(Membership_Leave_Group);
		frame.setTiempoResp((byte)0);
		frame.setDirGrupo(group.getDirGroup());
		frame.createChecksum();
		IpIndication paquete = new IpIndication();
		paquete.setDatos(frame.toByte());
		paquete.setInter(inter);
		try {
			paquete.setDirDestino(new IpAddress("224P0P0P2"));
		} catch (AddressException e) {
			e.printStackTrace();
		}
		miIp.addLoc(paquete);
	}

	protected void ProcesarPeticion(IgmpIndication x) {
		int tipo = x.getTipo();
		switch(tipo){
		case IgmpIndication.ADD_GROUP:{
			GroupHost group= new GroupHost(this,x.getDirGrupo(),x.getInter());
			if(groups.addGroup(group)){
				enviarReport(group, x.getInter());				
			}
			Cambios();
			break;
		}
		
		case IgmpIndication.LEAVE_GROUP:{
			GroupHost g = (GroupHost)groups.removerGroup(x.getDirGrupo());
			if (g!= null){
				enviarLeave(g, x.getInter());
			}
			Cambios();
			break;
		}
		}
	}

	public boolean InteresaIp(IpAddress dirIp) {
		return groups.contains(dirIp) || (dirIp.toInt()==Dir_Todos_Host);
	}

	public String[] getGroups() {
		return groups.getGroups();
	}

	public String[] getGroupsByInterface(Interface inter) {
		/* como supongo que un host tiene un unica interfaz. */
		return getGroups();
	}

	public String[] getOnlyGroups() {
		return groups.getOnlyGroups();
	}

	public void Terminar(){
		super.Terminar();
		miIp.Terminar();
	}
}
