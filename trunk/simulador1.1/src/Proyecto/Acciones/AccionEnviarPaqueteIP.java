package Proyecto.Acciones;

import Equipos.Equipo;
import Redes.Buffer;
import Redes.Dato;
import Redes.Interfaz;
import Redes.IPv4.DireccionIPv4;

public class AccionEnviarPaqueteIP implements Accion {

	DireccionIPv4 direccionDestino;
	int tamanioPaquete;
	boolean fragmentable;
	int copias;
	Interfaz interfaz;
	
	public AccionEnviarPaqueteIP(DireccionIPv4 direccionDestino, int tamanioPaquete,
			boolean fragmentable, int copias,Interfaz interfaz) {
		super();
		this.direccionDestino = direccionDestino;
		this.tamanioPaquete = tamanioPaquete;
		this.fragmentable = fragmentable;
		this.copias = copias;
		this.interfaz=interfaz;
	}

	public void ejecutar(Equipo e,int instante) {
		for(int i=0;i<copias;i++)
		{
	       Buffer buffer=new Buffer(tamanioPaquete);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=direccionDestino;
	       dato.protocolo=0;
	       dato.fragmentable=fragmentable;
	       dato.instante=instante;
	       dato.interfaz=interfaz;
	       e.ProgramarSalida(dato);
		}
	}

}
