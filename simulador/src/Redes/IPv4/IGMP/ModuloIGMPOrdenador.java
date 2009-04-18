package Redes.IPv4.IGMP;

import java.util.Set;

import Equipos.Equipo;
import Redes.IPv4.DireccionIPv4;

public class ModuloIGMPOrdenador extends ModuloIGMP{
	Set<DireccionIPv4> grupos;
	
	public ModuloIGMPOrdenador(Equipo equipo) {
		super(equipo);
	}

	protected void procesarMensajeEntrante(MensajeIGMP mensajeIGMP,int instante) {
		switch (mensajeIGMP.getTipo()){
		case MensajeIGMP.MEMBERSHIP_QUERY:{
			if (mensajeIGMP.getDirGrupo().equals(new DireccionIPv4("0.0.0.0"))){
				int tipo=mensajeIGMP.getTipo();
				equipo.NuevoEvento('R',instante,mensajeIGMP,"Mensaje IGMP ["+tipo+"] "+MensajeIGMP.Descripcion(tipo));
				//madarle todos los grupos a los que estoy registrado
			}
			else{
				//........
				/* Suponiendo que la es una direccion de grupo valida, es un query espec�fico */
				IpAddress dirGrupo = frame.getDirGrupo();
				Mostrar("Lleg� un Query espec�fico para el grupo "+dirGrupo);
				GroupHost group = (GroupHost) groups.getGroup(dirGrupo);
				PrepararReport(group, timer);
			}
			break;
		}
		
		case Membership_Report_v2:{
			/* Si es un report de un grupo el tiene un timer, cancelo el timer. */
			GroupHost group = (GroupHost) groups.getGroup(frame.getDirGrupo());
			Mostrar("Lleg� Membership report Versi�n 2 para el grupo "+group);
			group.llegoReport();
			break;
		}
		
		default:{
			/* Lleg� otra cosa que no le tengo que dar bola. */
			break;
		}
		}
    }
		
	}
	

}
