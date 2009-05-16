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

public class ModuloIGMPRouter extends ModuloIGMP{

	Map<Interfaz,DatosInterfaz> interfacesMap;
	
	public ModuloIGMPRouter(Equipo equipo) {
		super(equipo);
		interfacesMap = new HashMap<Interfaz, DatosInterfaz>();
		for (int i = 0; i < equipo.NumInterfaces(); i++) {
			Interfaz interfaz = equipo.getInterfaz(i);
			interfacesMap.put(interfaz, new DatosInterfaz());
		}
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
		return false;// ver que direcciones aceptar. Nestor dijo, entre otras boludeces, que con las clase C alcanzaría.
	}

	
	private class DatosInterfaz {
		
		public DatosInterfaz() {
			grupos = new HashMap<DireccionIPv4,Integer>();
			querier = true;
			timerToMembershipQuery = 0;
			countMembershipQuerySent = 0;
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
			
			Integer zero = new Integer(0);
			for (Iterator iterator = grupos.keySet().iterator(); iterator.hasNext();) {
				DireccionIPv4 direccionIPv4 = (DireccionIPv4) iterator.next();
				Integer timerToDeleteGroup = grupos.get(direccionIPv4);
				
				if (timerToDeleteGroup.equals(zero)) {
					grupos.remove(direccionIPv4);
				}
				else {
					//grupos.put(direccionIPv4, timerToDeleteGroup--);
					timerToDeleteGroup--;// ver si actualiza la referencia
				}
			}
			
		}

		/*
		 * Mantiene la lista de grupos asociados a la interfaz y el timer que indica
		 * cuanto tiempo debe esperarse para dar de baja el grupo si no se recibe
		 * un membership report
		 */
		Map<DireccionIPv4,Integer> grupos;
		
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

		public Map<DireccionIPv4, Integer> getGrupos() {
			return grupos;
		}

		public void setGrupos(Map<DireccionIPv4, Integer> grupos) {
			this.grupos = grupos;
		}

		public int getTimerToQuerier() {
			return timerToQuerier;
		}

		public void setTimerToQuerier(int timerToQuerier) {
			this.timerToQuerier = timerToQuerier;
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
					datosInterfaz.resetTimerToMembershipQuery();
				}
				else {
					datosInterfaz.decrementarTimerToQuerier();
				}
			}
			
			// 2- Eliminar grupos si es necesario.
			datosInterfaz.eliminarGruposSinRespuesta();
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
