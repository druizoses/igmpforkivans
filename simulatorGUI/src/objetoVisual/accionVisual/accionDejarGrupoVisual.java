package objetoVisual.accionVisual;

import java.awt.Dialog;

import objetoVisual.listaObjetos;
import visSim.dialogosAcciones.dialogoAccionApagar;
import visSim.dialogosAcciones.dialogoAccionBase;
import visSim.dialogosAcciones.dialogoAccionDejarGrupo;
import Equipos.Equipo;
import Proyecto.Acciones.Accion;
import Proyecto.Acciones.AccionDejarGrupo;
import Redes.IPv4.DireccionIPv4;


public class accionDejarGrupoVisual extends accionVisual{
	String direccionGrupo;
	String interfaz;
	
	public String getDireccionGrupo() {
		return direccionGrupo;
	}
	public void setDireccionGrupo(String direccionGrupo) {
		this.direccionGrupo = direccionGrupo;
	}
	public String getInterfaz() {
		return interfaz;
	}
	public void setInterfaz(String interfaz) {
		this.interfaz = interfaz;
	}
	public String getDescripcion() {
		return "El equipo "+this.getEquipo()+" deja el grupo "+this.direccionGrupo+" en el instante "+this.instante;
	}
	public String getTipo() {
		return this.DEJAR_GRUPO;
	}
	
	public Accion createAccion(Equipo e) {
		return new AccionDejarGrupo(new DireccionIPv4(this.direccionGrupo),e.getInterfaz(this.interfaz));
	}
	
	public dialogoAccionBase createDialog(Dialog parent, int xCentral, int yCentral, listaObjetos lista) {
		dialogoAccionDejarGrupo dlgAccion = new dialogoAccionDejarGrupo(parent,xCentral, yCentral, lista);
		dlgAccion.setInstante(this.instante);
		dlgAccion.setEquipo(this.equipo);
		return dlgAccion;
	}
	
}
