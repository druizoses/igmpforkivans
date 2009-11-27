package Redes.IPv4.IGMP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Equipos.Equipo;
import Redes.Dato;
import Redes.Interfaz;
import Redes.IPv4.DireccionIPv4;

public class ModuloIGMPOrdenador extends ModuloIGMP{
	
	/*
	 * Mantiene para cada interfaz los grupos asociados y si fue el ultimo en reportar la pertenencia al mismo.
	 */
	private Map<Interfaz, Map<DireccionIPv4,Boolean>> grupos;
	
	
	public ModuloIGMPOrdenador(Equipo equipo) {
		super(equipo);
		grupos  = new HashMap<Interfaz, Map<DireccionIPv4,Boolean>>();
	}

	protected void procesarMensajeEntrante(Dato dato,int instante) {
		MensajeIGMP mensajeIGMP=new MensajeIGMP(dato.paquete);
		Interfaz interfaz = dato.interfaz;
		switch (mensajeIGMP.getTipo()){
			case MensajeIGMP.MEMBERSHIP_QUERY:{
				if (mensajeIGMP.getDirGrupo().equals(new DireccionIPv4("0.0.0.0"))){
					//envio todos los grupos a los que estoy suscripto
					int tipo=mensajeIGMP.getTipo();
					equipo.NuevoEvento('R',instante,mensajeIGMP,"Mensaje IGMP ["+tipo+"] "+MensajeIGMP.Descripcion(mensajeIGMP)+";"+interfaz.getNombre());
					for (Iterator it = grupos.get(interfaz).keySet().iterator();it.hasNext();){
						DireccionIPv4 dirGrupo = (DireccionIPv4)it.next();
						enviarReport(interfaz, dirGrupo, instante, mensajeIGMP.getTiempoResp());
						grupos.get(interfaz).put(dirGrupo, true);
					}
				}
				else{
					//si estoy registrado a ese grupo, mandarle la respuesta
					int tipo=mensajeIGMP.getTipo();
					equipo.NuevoEvento('R',instante,mensajeIGMP,"Mensaje IGMP ["+tipo+"] "+MensajeIGMP.Descripcion(mensajeIGMP)+";"+interfaz.getNombre());
					DireccionIPv4 dirGrupo = mensajeIGMP.getDirGrupo();
					if (grupos.get(interfaz).containsKey(dirGrupo)){
						enviarReport(interfaz, dirGrupo, instante, mensajeIGMP.getTiempoResp());
						grupos.get(interfaz).put(dirGrupo, true);
					}
				}
				break;
			}
			case MensajeIGMP.MEMBERSHIP_REPORT_V2:{
				/* Si es un report de un grupo con confirmacion programada, la cancelo. */
				int tipo=mensajeIGMP.getTipo();
				DireccionIPv4 dirGrupo = mensajeIGMP.getDirGrupo();
				equipo.NuevoEvento('R',instante,mensajeIGMP,"Mensaje IGMP ["+tipo+"] "+MensajeIGMP.Descripcion(mensajeIGMP)+";"+interfaz.getNombre());
				List<Dato> datosARemover = new ArrayList<Dato>();
				for (Iterator it = colaSalida.iterator();it.hasNext();){
					Dato datoAux = (Dato) it.next();
					MensajeIGMP mensaje = (MensajeIGMP) datoAux.paquete; 
					if ((mensaje.getDirGrupo().equals(dirGrupo))){ //los mensajes puden ser MEMBERSHIP_REPORT_V2 ó MEMBERSHIP_LEAVE_GROUP
						//colaSalida.remove(datoAux);
						equipo.NuevoEvento('R',instante,mensajeIGMP,"Mensaje IGMP ["+tipo+"] Cancelando envío de Version 2 Membership Report Grupo: "+mensajeIGMP.getDirGrupo().getIP()+";"+interfaz.getNombre());
						datosARemover.add(datoAux);
					}
				}
				colaSalida.removeAll(datosARemover);
				
				if (grupos.get(interfaz).containsKey(dirGrupo))
					grupos.get(interfaz).put(dirGrupo, false);
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
 	 * @param interfaz Interfaz por la cual se envia el mensaje
	 * @param dirGroup Direccion del grupo al que se desea unir
	 * @param instante
	 */
	public void joinGroup(Interfaz interfaz,DireccionIPv4 dirGroup,int instante){
		if (!grupos.get(interfaz).containsKey(dirGroup)){
			grupos.get(interfaz).put(dirGroup, true);
			enviarReport(interfaz,dirGroup,instante, 0);
		}
	}
	
	/**
	 * Método que indica que el ordenador desea dejar a un grupo	 
	 * @param interfaz Interfaz por la cual se envia el mensaje
	 * @param dirGroup Direccion del grupo que desea dejar
	 * @param instante
	 */
	public void leaveGroup(Interfaz interfaz,DireccionIPv4 dirGroup,int instante){
		if (grupos.get(interfaz).containsKey(dirGroup)){
			
			List<Dato> datosARemover = new ArrayList<Dato>();
			for (Iterator it = colaSalida.iterator();it.hasNext();){
				Dato datoAux = (Dato) it.next();
				MensajeIGMP mensaje = (MensajeIGMP) datoAux.paquete; 
				if ((mensaje.getDirGrupo().equals(dirGroup))){
					datosARemover.add(datoAux);
				}
			}
			colaSalida.removeAll(datosARemover);
			
			if (grupos.get(interfaz).get(dirGroup)) {
				enviarLeave(interfaz,dirGroup,instante);
			}
			else {
				MensajeIGMP mensaje =MensajeIGMP.createMembershipLeaveGroupMessage(dirGroup);
				equipo.NuevoEvento('E',instante,mensaje,"Mensaje IGMP ["+mensaje.getTipo()+"] No se envía mensaje de leave group (no es su responsabilidad)"+";"+interfaz.getNombre());
			}
			
			grupos.get(interfaz).remove(dirGroup);
		}
	}
	
	/**
	 * Envia un reporte de pertenecia un grupo
	 * @param interfaz Interfaz por la cual se envia el mensaje
	 * @param dirGroup Direccion del grupo
	 * @param instante
	 * @param responseTime 
	 */
	private void enviarReport(Interfaz interfaz,DireccionIPv4 dirGroup,int instante, int responseTime){
		MensajeIGMP mensaje = MensajeIGMP.createMembershipReportV2Message(dirGroup);
		int retardo = (int) Math.round((Math.random() * (responseTime * ModuloIGMP.PASOS_POR_SEGUNDO)));
		Dato datoAux=new Dato(instante+1+retardo,mensaje,1);
        datoAux.direccion=ModuloIGMP.ALL_SYSTEMS_MULTICAST_GROUP;
        datoAux.interfaz=interfaz;
		super.ProgramarSalida(datoAux);
	}
	
	/**
	 * Envia un mensaje de dejar grupo
	 * @param interfaz Interfaz por la cual se envia el mensaje
	 * @param dirGroup Direccion del grupo
	 * @param instante
	 */
	private void enviarLeave(Interfaz interfaz,DireccionIPv4 dirGroup,int instante){
		MensajeIGMP mensaje = MensajeIGMP.createMembershipLeaveGroupMessage(dirGroup);
		Dato datoAux=new Dato(instante+1,mensaje,1);
        datoAux.direccion=ModuloIGMP.ALL_ROUTERS_MULTICAST_GROUP;
        datoAux.interfaz=interfaz;
		super.ProgramarSalida(datoAux);
	}
	
	/*
	 * El mensaje debe ser aceptado si esta dirijido a uno de los grupos a los que esta suscripto.
	 * @see Redes.IPv4.IGMP.ModuloIGMP#esParaMi(Redes.IPv4.DireccionIPv4, Redes.Interfaz)
	 */
	@Override
	public boolean esParaMi(DireccionIPv4 direccion,Interfaz interfaz) {
		return grupos.get(interfaz).containsKey(direccion) || ModuloIGMP.ALL_SYSTEMS_MULTICAST_GROUP.equals(direccion);
	}

	@Override
	protected void ProcesarTimers(int instante) {}

	@Override
	public void addInterfaz(Interfaz interfaz) {
		grupos.put(interfaz, new HashMap<DireccionIPv4,Boolean>());
	}
}
