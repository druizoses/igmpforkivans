package objetoVisual.accionVisual;

import Equipos.Equipo;
import Proyecto.Acciones.Accion;
import Proyecto.Acciones.AccionApagar;

public class accionApagarVisual extends accionVisual{

	public String getDescripcion() {
		return this.APAGAR+" el equipo: "+this.equipo+" en el instante: "+this.instante;
	}

	public String getTipo() {
		return this.APAGAR;
	}

	public Accion createAccion(Equipo e) {
		return  new AccionApagar();
	}

}
