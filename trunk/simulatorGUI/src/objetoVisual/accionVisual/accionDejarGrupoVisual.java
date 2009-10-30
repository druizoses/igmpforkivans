package objetoVisual.accionVisual;

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
	
}
