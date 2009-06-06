package Proyecto.Acciones;

import Equipos.Equipo;

public class Apagar implements Accion {

	public void ejecutar(Equipo e, int instante) {
		e.apagar();
	}

}
