package Redes.IPv4.IGMP;

import java.util.Iterator;
import java.util.Set;

import Equipos.Equipo;
import Redes.Dato;
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
					//envio todos los grupos a los que estoy suscripto
					int tipo=mensajeIGMP.getTipo();
					equipo.NuevoEvento('R',instante,mensajeIGMP,"Mensaje IGMP ["+tipo+"] "+MensajeIGMP.Descripcion(tipo));
					for (Iterator it = grupos.iterator();it.hasNext();){
						DireccionIPv4 dirGrupo = (DireccionIPv4)it.next();
						enviarReport(dirGrupo, instante);
					}
				}
				else{
					//si estoy registrado a ese grupo, mandarle la respuesta
					int tipo=mensajeIGMP.getTipo();
					equipo.NuevoEvento('R',instante,mensajeIGMP,"Mensaje IGMP ["+tipo+"] "+MensajeIGMP.Descripcion(tipo));
					DireccionIPv4 dirGrupo = mensajeIGMP.getDirGrupo();
					if (grupos.contains(dirGrupo)){
						enviarReport(dirGrupo,instante);
					}
				}
				break;
			}
			case MensajeIGMP.MEMBERSHIP_REPORT_V2:{
				/* Si es un report de un grupo con confirmacion programada, la cancelo. */
				int tipo=mensajeIGMP.getTipo();
				DireccionIPv4 dirGrupo = mensajeIGMP.getDirGrupo();
				equipo.NuevoEvento('R',instante,mensajeIGMP,"Mensaje IGMP ["+tipo+"] "+MensajeIGMP.Descripcion(tipo));
				boolean confirmacionEncontrada = false;
				for (Iterator it = colaSalida.iterator();it.hasNext()  &&  !confirmacionEncontrada;){
					Dato datoAux = (Dato) it.next();
					MensajeIGMP mensaje = (MensajeIGMP) datoAux.paquete; 
					if ((mensaje.getTipo() == MensajeIGMP.MEMBERSHIP_REPORT_V2) && (mensaje.getDirGrupo().equals(dirGrupo))){
						colaSalida.remove(datoAux);
						confirmacionEncontrada = true;
					}
				}
				break;
			}
			default:{
				/* Llego otra cosa que no le tengo que dar bola. */
				break;
			}
		}
    }

	/**
	 * Método que indica que el ordenador desea unirse a un grupo
	 * @param dirGroup Direccion del grupo al que se desea unir
	 * @param instante
	 */
	public void joinGroup(DireccionIPv4 dirGroup,int instante){
		if (!grupos.contains(dirGroup)){
			grupos.add(dirGroup);
			enviarReport(dirGroup,instante);
		}
	}
	
	/**
	 * Método que indica que el ordenador desea dejar a un grupo
	 * @param dirGroup Direccion del grupo que desea dejar
	 * @param instante
	 */
	public void leaveGroup(DireccionIPv4 dirGroup,int instante){
		if (grupos.contains(dirGroup)){
			grupos.remove(dirGroup);
			enviarLeave(dirGroup,instante);
		}
	}
	
	/**
	 * Envia un reporte de pertenecia un grupo
	 * @param dirGroup Direccion del grupo
	 * @param instante
	 */
	private void enviarReport(DireccionIPv4 dirGroup,int instante){
		MensajeIGMP mensaje = MensajeIGMP.createMembershipReportV2Message(dirGroup);
		int retardo = (int) Math.round((Math.random() * super.getRetardo())); //Revisar si anda con getRetardo o si hay que crear otro parametro
		Dato datoAux=new Dato(instante+retardo,mensaje,0);
        datoAux.direccion=dirGroup;
		super.ProgramarSalida(datoAux);
	}
	
	/**
	 * Envia un mensaje de dejar grupo
	 * @param dirGroup Direccion del grupo
	 * @param instante
	 */
	private void enviarLeave(DireccionIPv4 dirGroup,int instante){
		MensajeIGMP mensaje = MensajeIGMP.createMembershipLeaveGroupMessage(dirGroup);
		Dato datoAux=new Dato(instante+1,mensaje,0);
        datoAux.direccion=ModuloIGMP.ALL_ROUTERS_MULTICAST_GROUP;
		super.ProgramarSalida(datoAux);
	}
	
}
