package objetoVisual.accionVisual;

import java.awt.Dialog;

import objetoVisual.listaObjetos;
import visSim.dialogosAcciones.dialogoAccionApagar;
import visSim.dialogosAcciones.dialogoAccionBase;
import visSim.dialogosAcciones.dialogoAccionEncender;
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
	
	public dialogoAccionBase createDialog(Dialog parent, int xCentral, int yCentral, listaObjetos lista) {
		dialogoAccionEncender dlgAccion = new dialogoAccionEncender(parent,xCentral, yCentral, lista);
		dlgAccion.setInstante(this.instante);
		dlgAccion.setEquipo(this.equipo);
		return dlgAccion;
	}
	
}
