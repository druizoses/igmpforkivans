package objetoVisual.accionVisual;

import Equipos.Equipo;
import Proyecto.Acciones.Accion;
import Proyecto.Acciones.AccionEncender;

public class accionEncenderVisual extends accionVisual{

	public String getDescripcion() {
		return this.ENCENDER+" el equipo: "+this.equipo+" en el instante: "+this.instante;
	}

	public String getTipo() {
		return this.ENCENDER;
	}

	public Accion createAccion(Equipo e) {
		return  new AccionEncender();
	}
}
