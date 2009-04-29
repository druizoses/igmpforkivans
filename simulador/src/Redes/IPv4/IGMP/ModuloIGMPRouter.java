package Redes.IPv4.IGMP;

import Equipos.Equipo;
import Redes.Dato;
import Redes.Interfaz;
import Redes.IPv4.DireccionIPv4;

public class ModuloIGMPRouter extends ModuloIGMP{

	public ModuloIGMPRouter(Equipo equipo) {
		super(equipo);
		
	}

	@Override
	protected void procesarMensajeEntrante(Dato dato, int instante) {
		// TODO Auto-generated method stub
		
	}

	/*
	 * (non-Javadoc)
	 * @see Redes.IPv4.IGMP.ModuloIGMP#esParaMi(Redes.IPv4.DireccionIPv4, Redes.Interfaz)
	 */
	@Override
	public boolean esParaMi(DireccionIPv4 direccion,Interfaz interfaz) {
		return false;// ver que direcciones aceptar. Nestor dijo que con las clase C alcanzaría.
	}

}
