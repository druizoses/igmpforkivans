/**
 * 
 */
package NetworkProtocols.IGMP.router;

import Interface.Interface;
import NetworkProtocols.IGMP.Group;
import NetworkProtocols.IGMP.IGMP;
import NetworkProtocols.IGMP.IGMPMessage;
import NetworkProtocols.IGMP.util.Timer;
import NetworkProtocols.IGMP.util.Timerable;
import NetworkProtocols.IP.Address.IpAddress;

/**
 * @author Cito
 *
 */
public class GroupRouter extends Group implements Timerable{
	
	RouterIGMP miRouter;
	int timer_g, timer_ga, timer_rexmt;
	static final int Tarea_timer_g = 1;
	static final int Tarea_timer_ga = 2;
	static final int Tarea_timer_rexmt = 3;
	static int Robustness_Variable = 2;
	static int Query_Interval = 125000;
	static int Query_Response_Interval = 10000;
	static int Group_Membership_Interval = Robustness_Variable*Query_Interval + Query_Response_Interval;
	static int Startup_Query_Interval = Query_Interval / 4;
	static int Last_Member_Query_Interval = 1000;
	static int Last_Member_Query_Count = Robustness_Variable;
	private Timer t_g, t_ga, t_rexmt;



	public GroupRouter(RouterIGMP miRouter, IpAddress dirGroup, Interface inter) {
		super(dirGroup, inter);
		this.miRouter = miRouter;
		this.timer_g = Group_Membership_Interval;
		this.timer_ga = Last_Member_Query_Interval * Last_Member_Query_Count;
		this.timer_rexmt = timer_ga / 4;
		this.t_g = new Timer(timer_g,Tarea_timer_g, this);
	}



	/**
	 * Tareas que se ejecutan cuando alguno de los timer se terminan.
	 */
	public void ejecutarTarea(int tarea) {		
		switch(tarea){
		case Tarea_timer_g:{
			miRouter.Mostrar("Terminó timer_g del grupo "+super.dirGroup);
			//System.out.println("Terminó timer_g del grupo "+super.dirGroup);
			miRouter.leave(super.dirGroup, super.inter);
			break;
		}
		
		case Tarea_timer_ga:{
			miRouter.Mostrar("Terminó timer_ga del grupo "+super.dirGroup);
			//System.out.println("Terminó timer_ga del grupo "+super.dirGroup);
			miRouter.leave(super.dirGroup, super.inter);
			break;
		}
		
		case Tarea_timer_rexmt:{
			miRouter.Mostrar("Terminó timer_rexmt del grupo "+super.dirGroup);
			//System.out.println("Terminó timer_rexmt del grupo "+super.dirGroup);
			IGMPMessage frame = new IGMPMessage();
			frame.setTipo(IGMP.Membership_Query);
			frame.setTiempoResp((byte)(10-128));
			frame.setDirGrupo(super.dirGroup);
			frame.createChecksum();
			miRouter.EnviarQueryEspecifico(frame, super.inter, super.dirGroup);
			break;
		}
		}
	}
	
	public void llegoReport(){
		t_g.Cancelar();
		t_g = new Timer(timer_g,Tarea_timer_g, this);
	}
	
	public void llegoLeave(){
		t_g.Cancelar();
		t_ga = new Timer(timer_ga, Tarea_timer_ga, this);
		t_rexmt = new Timer(timer_rexmt, Tarea_timer_rexmt, this);
		
		IGMPMessage frame = new IGMPMessage();
		frame.setTipo(IGMP.Membership_Query);
		frame.setTiempoResp((byte)(10-128));
		frame.setDirGrupo(super.dirGroup);
		frame.createChecksum();
		miRouter.EnviarQueryEspecifico(frame, super.inter, super.dirGroup);
	}
}
