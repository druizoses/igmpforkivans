package Proyecto.Acciones;

import Equipos.Equipo;

public class AccionEncender implements Accion {

	public void ejecutar(Equipo e, int instante) {
		e.encender(e,instante);
	}

}
