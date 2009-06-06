package Proyecto.Acciones;

import Equipos.Equipo;
import Equipos.Ordenador;
import Redes.Interfaz;
import Redes.IPv4.DireccionIPv4;

public class UnirseAGrupo implements Accion {

	DireccionIPv4 direccionGrupo;
	Interfaz interfaz;
	
	public UnirseAGrupo(DireccionIPv4 direccionGrupo, Interfaz interfaz) {
		super();
		this.direccionGrupo = direccionGrupo;
		this.interfaz = interfaz;
	}

	public void ejecutar(Equipo e,int instante) {
		Ordenador o = (Ordenador) e;
		o.joinGroup(interfaz, direccionGrupo, instante);
	}

}
