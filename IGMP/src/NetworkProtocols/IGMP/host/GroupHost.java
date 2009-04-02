package NetworkProtocols.IGMP.host;

import Interface.Interface;
import NetworkProtocols.IGMP.Group;
import NetworkProtocols.IGMP.util.Timer;
import NetworkProtocols.IGMP.util.Timerable;
import NetworkProtocols.IP.Address.IpAddress;

public class GroupHost extends Group implements Timerable{
	/**
	 * Supuestamente, no sería parte de este grupo.
	 */
	public static byte Non_Member = 0;
	/**
	 * Host miembro del grupo, pero esperando para enviar un report.
	 */
	public static byte Delaying_Member = 1;
	/**
	 * Host miembro del grupo, sin timer para enviar report.
	 */
	public static byte Idle_Member = 2;
	
	/**
	 * Tiempo, en décimas de segundos, que tiene que esperar antes de
	 * mandar un report por este grupo.
	 */
	private byte estado;
	private HostIGMP miHost;
	private Timer t_;
	
	
	public GroupHost(HostIGMP miHost, IpAddress dirGroup, Interface inter) {
		super(dirGroup,inter);
		this.miHost = miHost;
		setEstado(Idle_Member);
	}

	/**
	 * Cambia el estado a Delaying_Member y empieza el timer con el tiempo
	 * especificado. En cuanto el timer termina, le avisa al host.
	 * @param timer: tiempo en decimas de segundos a esperar.
	 */
	public void iniciarTimer(int timer){
		setEstado(Delaying_Member);
		t_ = new Timer(timer,1, this);
	}
	
	public IpAddress getDirGroup() {
		return dirGroup;
	}

	public void setDirGroup(IpAddress dirGroup) {
		this.dirGroup = dirGroup;
		notify();
	}

	public void setEstado(byte estado) {
		this.estado = estado;
		if (t_ != null)
			t_.Cancelar();
	}

	public Interface getInter() {
		return inter;
	}

	public void setInter(Interface inter) {
		this.inter = inter;
	}

	public void ejecutarTarea(int tarea) {
		miHost.enviarReport(this, inter);
		t_=null;
	}

	public void llegoReport() {
		if (t_ != null){
			t_.Cancelar();
			t_ = null;
		}
	}
	
	
}
