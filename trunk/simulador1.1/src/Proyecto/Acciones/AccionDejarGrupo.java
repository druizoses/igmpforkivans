package Proyecto.Acciones;

import Equipos.Equipo;
import Equipos.Ordenador;
import Redes.Interfaz;
import Redes.IPv4.DireccionIPv4;

public class AccionDejarGrupo implements Accion {

	DireccionIPv4 direccionGrupo;
	Interfaz interfaz;
	
	public AccionDejarGrupo(DireccionIPv4 direccionGrupo, Interfaz interfaz) {
		super();
		this.direccionGrupo = direccionGrupo;
		this.interfaz = interfaz;
	}

	public void ejecutar(Equipo e,int instante) {
		Ordenador o = (Ordenador) e;
		o.leaveGroup(interfaz, direccionGrupo, instante);
	}

}
