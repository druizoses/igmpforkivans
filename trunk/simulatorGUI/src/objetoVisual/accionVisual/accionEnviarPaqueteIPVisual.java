package objetoVisual.accionVisual;

import java.awt.Dialog;

import objetoVisual.listaObjetos;
import visSim.dialogosAcciones.dialogoAccionBase;
import visSim.dialogosAcciones.dialogoAccionEnviarPaqueteIP;
import Equipos.Equipo;
import Proyecto.Acciones.Accion;
import Proyecto.Acciones.AccionEnviarPaqueteIP;
import Redes.IPv4.DireccionIPv4;



public class accionEnviarPaqueteIPVisual extends accionVisual{
	
	String direccionDestino;
	int tamanioPaquete;
	boolean fragmentable;
	int copias;
	String interfaz;
	
	public String getDireccionDestino() {
		return direccionDestino;
	}
	public void setDireccionDestino(String direccionDestino) {
		this.direccionDestino = direccionDestino;
	}
	public int getTamanioPaquete() {
		return tamanioPaquete;
	}
	public void setTamanioPaquete(int tamanioPaquete) {
		this.tamanioPaquete = tamanioPaquete;
	}
	public boolean isFragmentable() {
		return fragmentable;
	}
	public void setFragmentable(boolean fragmentable) {
		this.fragmentable = fragmentable;
	}
	public int getCopias() {
		return copias;
	}
	public void setCopias(int copias) {
		this.copias = copias;
	}
	public String getInterfaz() {
		return interfaz;
	}
	public void setInterfaz(String interfaz) {
		this.interfaz = interfaz;
	}
	
	public String getDescripcion() {
		if (fragmentable)
			return "Equipo "+this.equipo+" envia un paquete IP de tamaño "+this.tamanioPaquete+", fragmentable, con "+this.copias+" copias, a la dirección "+this.direccionDestino+" en el instate "+this.instante;
		else
			return "Equipo "+this.equipo+" envia un paquete IP de tamaño "+this.tamanioPaquete+", con "+this.copias+" copias, a la dirección "+this.direccionDestino+" en el instate "+this.instante;
	}
	
	public String getTipo() {
		return this.ENVIAR_PAQUETE_IP;
	}

	public Accion createAccion(Equipo e) {
		return new AccionEnviarPaqueteIP(new DireccionIPv4(this.direccionDestino), this.tamanioPaquete, this.fragmentable, this.copias, e.getInterfaz(this.interfaz));
	}
	
	public dialogoAccionBase createDialog(Dialog parent, int xCentral, int yCentral, listaObjetos lista) {
		dialogoAccionEnviarPaqueteIP dlgAccion = new dialogoAccionEnviarPaqueteIP(parent,xCentral, yCentral, lista);
		dlgAccion.setInstante(this.instante);
		dlgAccion.setEquipo(this.equipo);	
		dlgAccion.setDireccionDestino(this.direccionDestino);
		dlgAccion.setTamanio(this.tamanioPaquete);
		dlgAccion.setFragmentable(this.fragmentable);
		dlgAccion.setCopias(this.copias);
		dlgAccion.setInterfaz(this.interfaz);		
		return dlgAccion;
	}
	
}
