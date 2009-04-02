package Redes.NuevaRed;

import Equipos.Equipo;
import Redes.*;
import Proyecto.Parametro;

/**
 * Interfaz con las redes de tipo NuevaRed
 */
public class InterfazNuevaRed extends Interfaz 
{
	/**
	 * Constructor
	 * @param nombre Nombre de la interfaz
	 * @param ip Direccion IP asociada
	 * @param mascara Mascara de la direccion IP
	 * @param dirEnlace Direccion de enlace
	 * @throws IllegalArgumentException si algun paramentro no es valido
	 */
    public InterfazNuevaRed(String nombre,String ip,String mascara,String dirEnlace) throws IllegalArgumentException
	{
		super(nombre,ip,mascara);
		
		parametros.add(new Parametro("dirfisica","Dirección Física","Redes.NuevaRed.DireccionNuevaRed"));
		parametros.setValor("dirfisica",new DireccionNuevaRed(dirEnlace));
		parametros.add(new Parametro("clasered","Paquete de redes compatibles","java.lang.String"));
		parametros.setValor("clasered","Ethernet");
		
		this.nivelEnlace=null;
	}
	
    
	
	/**
	 * Crea el nivel de enlace asociado a la interfaz
	 * @param equipo Equipo propietario de la interfaz
	 * @throws IllegalArgumentException si el equipo es nulo o si no se han
	 *         registrado la red o la direccion fisica de la interfaz
	 */
	public void CreaNivelEnlace(Equipo equipo)
	{
		// 1. Comprobamos si tenemos disponibles todos los datos y creamos el nivel
		//    de enlace con ellos
		if(equipo!=null && red!=null && getDirFisica()!=null)
		   this.nivelEnlace=new NivelNuevaRed(equipo,red,(DireccionNuevaRed)getDirFisica());
		else
			throw new IllegalArgumentException("Falta informacion para crear el nivel del enlace del interfaz");
	}
}
