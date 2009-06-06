package Proyecto.Acciones;

import Equipos.Equipo;

public class Encender implements Accion {

	public void ejecutar(Equipo e, int instante) {
		e.encender();
	}

}
