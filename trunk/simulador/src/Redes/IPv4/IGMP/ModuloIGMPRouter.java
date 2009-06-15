package Redes.IPv4.IGMP;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import Equipos.Equipo;
import Redes.Dato;
import Redes.Interfaz;
import Redes.IPv4.DireccionIPv4;

public class ModuloIGMPRouter extends ModuloIGMP{

	Map<Interfaz,DatosInterfaz> interfacesMap;
	
	public ModuloIGMPRouter(Equipo equipo) {
		super(equipo);
		interfacesMap = new HashMap<Interfaz, DatosInterfaz>();
	}
	
	public void addInterfaz(Interfaz interfaz) {
		interfacesMap.put(interfaz, new DatosInterfaz(interfaz,this));
	}


	@Override
	protected void procesarMensajeEntrante(Dato dato, int instante) {
		MensajeIGMP mensajeIGMP=new MensajeIGMP(dato.paquete);
		Interfaz interfaz = dato.interfaz;
		switch (mensajeIGMP.getTipo()){
			case MensajeIGMP.MEMBERSHIP_QUERY:{
				if (mensajeIGMP.getDirGrupo().equals(new DireccionIPv4("0.0.0.0"))){
					//si la direccion ip del que envio el mensaje igmp es menor a la mia me pongo como non-querier
					if (((DireccionIPv4)dato.direccion).compareTo(interfaz.getIP()) < 0) {
						interfacesMap.get(interfaz).setQuerier(false);
					}
				}
			}
			case MensajeIGMP.MEMBERSHIP_REPORT_V2:{
				interfacesMap.get(interfaz).activarGrupo(mensajeIGMP.getDirGrupo());
			}
			case MensajeIGMP.MEMBERSHIP_LEAVE_GROUP:{
				interfacesMap.get(interfaz).desactivarGrupo(mensajeIGMP.getDirGrupo());
			}
			default:{
				/* Llego otra cosa que no le tengo que dar bola. */
				break;
			}
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see Redes.IPv4.IGMP.ModuloIGMP#esParaMi(Redes.IPv4.DireccionIPv4, Redes.Interfaz)
	 */
	@Override
	public boolean esParaMi(DireccionIPv4 direccion,Interfaz interfaz) {
		return ModuloIGMP.ALL_SYSTEMS_MULTICAST_GROUP.equals(direccion) || ModuloIGMP.ALL_ROUTERS_MULTICAST_GROUP.equals(direccion);
	}

	
	private class DatosInterfaz {
		
		Interfaz interfaz;
		
		ModuloIGMPRouter moduloIGMPRouter;
		
		public DatosInterfaz(Interfaz interfaz,ModuloIGMPRouter moduloIGMPRouter) {
			gruposActivos = new HashMap<DireccionIPv4,GroupTimers>();
			querier = true;
			timerToMembershipQuery = 0;
			countMembershipQuerySent = 0;
			this.interfaz=interfaz;
			this.moduloIGMPRouter=moduloIGMPRouter;
		}

		public void desactivarGrupo(DireccionIPv4 dirGrupo) {
			if (this.isQuerier()) {
				GroupTimers groupTimers = gruposActivos.get(dirGrupo);
				if (groupTimers.getCountSpecificQueryToSend()==0)
					groupTimers.setCountSpecificQueryToSend(ModuloIGMP.LAST_MEMBER_QUERY_COUNT);
			}
		}

		public void resetTimerToMembershipQuery(){
			countMembershipQuerySent++;
			
			if (countMembershipQuerySent < ModuloIGMP.STARTUP_QUERY_COUNT) {
				timerToMembershipQuery = ModuloIGMP.STARTUP_QUERY_INTERVAL;
			}
			else {
				timerToMembershipQuery = ModuloIGMP.QUERY_INTERVAL;
			}
		}
		
		public void eliminarGruposSinRespuesta(){
			
			for (Iterator iterator = gruposActivos.keySet().iterator(); iterator.hasNext();) {
				DireccionIPv4 direccionIPv4 = (DireccionIPv4) iterator.next();
				
				GroupTimers groupTimers = (GroupTimers)gruposActivos.get(direccionIPv4);
				
				int timerToDeleteGroup = groupTimers.getTimerToDeleteGroup();
				
				if (timerToDeleteGroup == 0) {
					gruposActivos.remove(direccionIPv4);
				}
				else {
					groupTimers.decrementarTimerToDeleteGroup();
				}
			}
			
		}

		/*
		 * Mantiene la lista de grupos asociados a la interfaz y el timer que indica
		 * cuanto tiempo debe esperarse para dar de baja el grupo si no se recibe
		 * un membership report
		 */
		Map<DireccionIPv4,GroupTimers> gruposActivos;
		
		boolean querier;
		
		int timerToMembershipQuery;
		
		int timerToQuerier;
		
		int countMembershipQuerySent;
	
		public void decrementarTimerToMembershipQuery() {
			timerToMembershipQuery--;
		}
		
		public void decrementarTimerToQuerier() {
			timerToQuerier--;
		}
		
		public boolean isQuerier() {
			return querier;
		}
		
		public void setQuerier(boolean querier) {
			this.querier = querier;
		}

		public int getTimerToMembershipQuery() {
			return timerToMembershipQuery;
		}

		public void setTimerToMembershipQuery(int timerToMembershipQuery) {
			this.timerToMembershipQuery = timerToMembershipQuery;
		}

		public int getCountMembershipQuerySent() {
			return countMembershipQuerySent;
		}

		public void setCountMembershipQuerySent(int countMembershipQuerySent) {
			this.countMembershipQuerySent = countMembershipQuerySent;
		}

		public Map<DireccionIPv4, GroupTimers> getGruposActivos() {
			return gruposActivos;
		}

		public void setGruposActivos(Map<DireccionIPv4, GroupTimers> gruposActivos) {
			this.gruposActivos = gruposActivos;
		}

		public int getTimerToQuerier() {
			return timerToQuerier;
		}

		public void setTimerToQuerier(int timerToQuerier) {
			this.timerToQuerier = timerToQuerier;
		}
		
		public void activarGrupo(DireccionIPv4 dirGrupo) {
			gruposActivos.put(dirGrupo, new GroupTimers());
		}

		public void enviarSpecificQueries(int instante) {
			for (Iterator iterator = gruposActivos.keySet().iterator(); iterator.hasNext();) {
				DireccionIPv4 direccionIPv4 = (DireccionIPv4) iterator.next();
				GroupTimers groupTimers = (GroupTimers)gruposActivos.get(direccionIPv4);
				
				int countSpecificQueryToSend = groupTimers.getCountSpecificQueryToSend();
				
				if (countSpecificQueryToSend != 0) {
					int timerToNextSpecificQuery = groupTimers.getTimerToNextSpecificQuery();

					if (timerToNextSpecificQuery == 0) {
						enviarSpecificMembershipQueryMessage(instante, interfaz,direccionIPv4);
						groupTimers.setTimerToNextSpecificQuery(ModuloIGMP.LAST_MEMBER_QUERY_INTERVAL);
					}
					else {
						groupTimers.decrementarTimerToNextSpecificQuery();
					}
					
					gruposActivos.remove(direccionIPv4);
				}
			}
			
		}
		private void enviarSpecificMembershipQueryMessage(int instante,Interfaz interfaz,DireccionIPv4 direccionIPv4) {
			MensajeIGMP mensaje = MensajeIGMP.createMembershipQueryMessage(direccionIPv4);
			Dato datoAux=new Dato(instante,mensaje,1);
	        datoAux.direccion=ModuloIGMP.ALL_SYSTEMS_MULTICAST_GROUP;
	        datoAux.interfaz=interfaz;
			this.moduloIGMPRouter.ProgramarSalida(datoAux);
		}
		
	}

	
	
	protected void ProcesarTimers(int instante) {
		for (Iterator<Interfaz> iterator = interfacesMap.keySet().iterator(); iterator.hasNext();) {
			Interfaz interfaz = iterator.next();
			DatosInterfaz datosInterfaz = interfacesMap.get(interfaz);
			
			// 1- Enviar Membership Query si es necesario.
			if (datosInterfaz.isQuerier()){
				if (datosInterfaz.getTimerToMembershipQuery() == 0) {
					enviarGeneralMembershipQueryMessage(instante, interfaz);
					datosInterfaz.resetTimerToMembershipQuery();
				}
				else {
					datosInterfaz.decrementarTimerToMembershipQuery();
				}
			} else {
				if (datosInterfaz.getTimerToQuerier() == 0) {
					datosInterfaz.setQuerier(true);
					enviarGeneralMembershipQueryMessage(instante, interfaz);
					datosInterfaz.setCountMembershipQuerySent(ModuloIGMP.STARTUP_QUERY_COUNT);
					datosInterfaz.resetTimerToMembershipQuery();
				}
				else {
					datosInterfaz.decrementarTimerToQuerier();
				}
			}
			
			// 2- Eliminar grupos si es necesario.
			datosInterfaz.eliminarGruposSinRespuesta();
			
			// 3- 
			datosInterfaz.enviarSpecificQueries(instante);
			
			
		}
	}
	
	private class GroupTimers {
		
		int timerToDeleteGroup;
		
		int timerToNextSpecificQuery;
		
		int countSpecificQueryToSend;

		public GroupTimers() {
			timerToDeleteGroup=ModuloIGMP.GROUP_MEMBERSHIP_INTERVAL;
			timerToNextSpecificQuery=0;
			countSpecificQueryToSend=0;
		}
		
		public void decrementarTimerToNextSpecificQuery() {
			this.timerToNextSpecificQuery--;
		}
		
		public void decrementarTimerToDeleteGroup() {
			this.timerToDeleteGroup--;
		}
		
		public int getTimerToDeleteGroup() {
			return timerToDeleteGroup;
		}

		public void setTimerToDeleteGroup(int timerToDeleteGroup) {
			this.timerToDeleteGroup = timerToDeleteGroup;
		}

		public int getTimerToNextSpecificQuery() {
			return timerToNextSpecificQuery;
		}

		public void setTimerToNextSpecificQuery(int timerToNextSpecificQuery) {
			this.timerToNextSpecificQuery = timerToNextSpecificQuery;
		}

		public int getCountSpecificQueryToSend() {
			return countSpecificQueryToSend;
		}

		public void setCountSpecificQueryToSend(int countSpecificQueryToSend) {
			this.countSpecificQueryToSend = countSpecificQueryToSend;
		}
		
	}
	
	private void enviarGeneralMembershipQueryMessage(int instante,Interfaz interfaz) {
		MensajeIGMP mensaje = MensajeIGMP.createMembershipQueryMessage(new DireccionIPv4("0.0.0.0"));
		Dato datoAux=new Dato(instante,mensaje,1);
        datoAux.direccion=ModuloIGMP.ALL_SYSTEMS_MULTICAST_GROUP;
        datoAux.interfaz=interfaz;
		super.ProgramarSalida(datoAux);
	}
}
