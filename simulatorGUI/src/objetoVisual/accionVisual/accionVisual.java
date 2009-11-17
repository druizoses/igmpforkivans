package objetoVisual.accionVisual;

import java.awt.Dialog;

import objetoVisual.listaObjetos;
import visSim.dialogosAcciones.dialogoAccionBase;
import Equipos.Equipo;
import Proyecto.Acciones.Accion;

public abstract class accionVisual {
	
	public static final String APAGAR="Apagar Equipo";
	public static final String DEJAR_GRUPO="Dejar Grupo";
	public static final String ENCENDER="Encender Equipo";	
	public static final String ENVIAR_PAQUETE_IP="Enviar paquete IP";
	public static final String UNIRSE_A_GRUPO="Unirse a un Grupo";
	
	int instante;
	String equipo;
	
	public int getInstante() {
		return instante;
	}
	public void setInstante(int instante) {
		this.instante = instante;
	}
	public String getEquipo() {
		return equipo;
	}
	public void setEquipo(String equipo) {
		this.equipo = equipo;
	}
	
	public abstract String getTipo();
	
	public abstract String getDescripcion();
	
	public abstract Accion createAccion(Equipo e);
	
	public abstract dialogoAccionBase createDialog(Dialog parent, int xCentral, int yCentral, listaObjetos lista);
	
}
