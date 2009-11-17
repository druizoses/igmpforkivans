package objetoVisual.accionVisual;

import java.awt.Dialog;

import objetoVisual.listaObjetos;
import visSim.dialogosAcciones.dialogoAccionApagar;
import visSim.dialogosAcciones.dialogoAccionBase;
import visSim.dialogosAcciones.dialogoAccionEncender;
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

	public dialogoAccionBase createDialog(Dialog parent, int xCentral, int yCentral, listaObjetos lista) {
		dialogoAccionApagar dlgAccion = new dialogoAccionApagar(parent,xCentral, yCentral, lista);
		dlgAccion.setInstante(this.instante);
		dlgAccion.setEquipo(this.equipo);
		return dlgAccion;
	}

}
