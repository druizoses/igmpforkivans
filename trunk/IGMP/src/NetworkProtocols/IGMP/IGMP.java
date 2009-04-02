/**
 * 
 */
package NetworkProtocols.IGMP;

import utils.Queue;
import Interface.Interface;
import NetworkProtocols.IpIgmpIndication;
import NetworkProtocols.N2N3Indication;
import NetworkProtocols.NetworkProtocols;
import NetworkProtocols.IGMP.util.Mostrable;
import NetworkProtocols.IGMP.util.Semaphore;
import NetworkProtocols.IP.Datagram;
import NetworkProtocols.IP.IP;
import NetworkProtocols.IP.Address.IpAddress;

/**
 * @author Cito
 *
 * 
 */
public abstract class IGMP {
	
	public static final byte Membership_Query = 0x11;
	public static final byte Membership_Report_v1 = 0x12;
	public static final byte Membership_Report_v2 = 0x16;
	public static final byte Membership_Leave_Group = 0x17;
	
	protected Queue bufferLocal, bufferRemoto;
	protected Semaphore sLocal, sRemoto, hayTareaLocal, hayTareaRemota;
	protected IP miIp;
	protected Mostrable frameSalida=null;
	private ReaderLocal lectorLocal;
	private ReaderRemoto lectorRemoto;
	boolean terminar = false;
	
	protected class IgmpIndication{
		public static final int ADD_GROUP = 1;
		public static final int LEAVE_GROUP = 2;
		private int tipo;
		private Interface inter;
		private IpAddress dirGrupo;
		
		public int getTipo() {
			return tipo;
		}
		public void setTipo(int tipo) {
			this.tipo = tipo;
		}
		public Interface getInter() {
			return inter;
		}
		public void setInter(Interface inter) {
			this.inter = inter;
		}
		public IpAddress getDirGrupo() {
			return dirGrupo;
		}
		public void setDirGrupo(IpAddress dirGrupo) {
			this.dirGrupo = dirGrupo;
		}
		
	}
	
	public IGMP(){
		this.miIp = (IP) NetworkProtocols.getProtocol(NetworkProtocols.PROTO_IP);
		bufferLocal = new Queue();
		bufferRemoto = new Queue();
		hayTareaLocal = new Semaphore(0);
		hayTareaRemota = new Semaphore(0);
		lectorLocal = new ReaderLocal(this);
		lectorRemoto = new ReaderRemoto(this);
		lectorLocal.start();
		lectorRemoto.start();
		sLocal = new Semaphore(1);
		sRemoto = new Semaphore(1);
	}

	/**
	 * Agrega un evento a la lista de eventos locales. 
	 * Método utilizado por los
	 * niveles superiores.
	 * O sea, manda un paquete al nivel inferior para que se mande
	 * a algún destino.
	 */
	private void addLoc(Object x) {
		sLocal.P();
		bufferLocal.pushBack(x);
		sLocal.V();
		hayTareaLocal.V();
	}
	
	/**
	 * Agregarse a algún grupo.
	 * Este método es utilizado cuando un proceso tiene interes en algún grupo.
	 * Puede que ya se esté escuchando este grupo.
	 * @param group: nombre del grupo.
	 * @param inter: interface por la cual se quiere agregar a un grupo.
	 */
	public void addGroup(IpAddress group, Interface inter){
		IgmpIndication aux = new IgmpIndication();
		aux.setDirGrupo(group);
		aux.setInter(inter);
		aux.setTipo(IgmpIndication.ADD_GROUP);
		addLoc(aux);
	}
	
	/**
	 * Liberar un grupo.
	 * Si es el ultimo proceso que deja de interesarse en este grupo, se deja de 
	 * escuchar este grupo.
	 * @param group
	 * @param inter
	 */
	public void leaveGroup(IpAddress group, Interface inter){
		IgmpIndication aux = new IgmpIndication();
		aux.setDirGrupo(group);
		aux.setInter(inter);
		aux.setTipo(IgmpIndication.LEAVE_GROUP);
		addLoc(aux);
	}

	/**
	 * Agrega un evento a la lista de eventos remotos.
	 * Método utilizado por los niveles inferiores.
	 * O sea, manda un paquete al nivel superior.
	 */
	public void addRem(N2N3Indication x) {
		sRemoto.P();
		Datagram data = new Datagram(x.getInfo());
		IpIgmpIndication aux = new IpIgmpIndication(new IGMPMessage(data.getPayload()), x.getInter());
		bufferRemoto.pushBack(aux);
		sRemoto.V();
		hayTareaRemota.V();
	}
	
	
	public Queue getBufferLocal() {
		return bufferLocal;
	}

	public Queue getBufferRemoto() {
		return bufferRemoto;
	}

		
	/**
	 * Procesa un paquete recibido del nivel inferior.
	 */
	public abstract void recibirPaqueteDeIP(IpIgmpIndication x);
	
	
	public Semaphore getHayTareaLocal() {
		return hayTareaLocal;
	}

	public Semaphore getHayTareaRemota() {
		return hayTareaRemota;
	}
	

	/**
	 * Clase utilizada para desincronizar el nivel superior con este.
	 * 
	 * @author Cito
	 */
	private class ReaderLocal extends Thread {
		IGMP miIGMP;
		Semaphore hayTarea;
		Queue tareas;
		
		public ReaderLocal(IGMP igmp){
			this.miIGMP = igmp;
			hayTarea = igmp.getHayTareaLocal();
			tareas = igmp.getBufferLocal();
		}

		public void run() {
			while (!terminar){
				hayTarea.P();
				IgmpIndication x = (IgmpIndication)tareas.peekFront();
				if (x != null){
					tareas.popFront();
					miIGMP.ProcesarPeticion(x);
				}
			}
		}
	}
	
	/**
	 * Clase utilizada para desincronizar el nivel inferior con este.
	 * 
	 * @author Cito
	 */
	private class ReaderRemoto extends Thread {
		IGMP miIGMP;
		Semaphore hayTarea;
		Queue tareas;
		
		public ReaderRemoto(IGMP igmp){
			this.miIGMP = igmp;
			hayTarea = igmp.getHayTareaRemota();
			tareas = igmp.getBufferRemoto();
		}
		
		public void run() {
			while (!terminar){
				hayTarea.P();
				IpIgmpIndication x = (IpIgmpIndication) tareas.peekFront();
				if (x != null){
					tareas.popFront();
					miIGMP.recibirPaqueteDeIP(x);
				}
			}
		}
	}

	/**
	 * Este método es el que llama el Thead encargado de procesar las tareas locales.
	 * @param x
	 */
	protected abstract void ProcesarPeticion(IgmpIndication x);
	
	public abstract boolean InteresaIp(IpAddress dirIp);

	public abstract String[] getGroups();
	
	public abstract String[] getOnlyGroups();
	
	public abstract String[] getGroupsByInterface(Interface inter);

	public void setFrameSalida(Mostrable frameSalida) {
		this.frameSalida = frameSalida;
	}
	
	public void Mostrar(String log){
		if (frameSalida==null)
			System.out.println(log);
		else
			frameSalida.agregarLog(log);
	}
	
	protected void Cambios(){
		if(frameSalida!= null)
			frameSalida.cambiosEnGrupos();
	}

	public void Terminar() {
		terminar=true;
		this.hayTareaLocal.Terminar();
		this.hayTareaRemota.Terminar();
		miIp.Terminar();
	}
}
