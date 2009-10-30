package objetoVisual.accionVisual;

import Equipos.Equipo;
import Proyecto.Acciones.Accion;
import Proyecto.Acciones.AccionUnirseAGrupo;
import Redes.IPv4.DireccionIPv4;


public class accionUnirseAGrupoVisual extends accionVisual{
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
		return "Equipo "+this.equipo+" se une al grupo "+this.direccionGrupo+" por al interfaz "+this.interfaz+" en el instante "+this.instante;
	}

	public String getTipo() {
		return this.UNIRSE_A_GRUPO;
	}

	public Accion createAccion(Equipo e) {
		return new AccionUnirseAGrupo(new DireccionIPv4(this.direccionGrupo),e.getInterfaz(this.interfaz));
	}
	
}
