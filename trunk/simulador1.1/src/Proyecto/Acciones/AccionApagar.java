package Proyecto.Acciones;

import Equipos.Equipo;

public class AccionApagar implements Accion {

	public void ejecutar(Equipo e, int instante) {
		e.apagar(e,instante);
	}

}
