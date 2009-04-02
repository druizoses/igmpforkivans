/*
    Simulador de redes IP (KIVA). API de Simulacion, permite simular
    redes de tipo IP que usen IP, ARP, e ICMP.
    Copyright (C) 2004, José María Díaz Gorriz

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Library General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Library General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this library; if not, write to the 
    Free Software Foundation, Inc., 59 Temple Place - Suite 330, 
    Boston, MA  02111-1307  USA.
*/

package Redes.Ethernet;

import Equipos.Equipo;
import Redes.*;
import Proyecto.Parametro;

/**
 * Interfaz con las redes de tipo Ethernet
 */
public class InterfazEthernet extends Interfaz 
{
	/**
	 * Constructor
	 * @param nombre Nombre de la interfaz
	 * @param ip Direccion IP asociada
	 * @param mascara Mascara de la direccion IP
	 * @param dirEnlace Direccion de enlace
	 * @throws IllegalArgumentException si algun paramentro no es valido
	 */
    public InterfazEthernet(String nombre,String ip,String mascara,String dirEnlace) throws IllegalArgumentException
	{
		super(nombre,ip,mascara);
		
		parametros.add(new Parametro("dirfisica","Dirección Física","Redes.Ethernet.DireccionEthernet"));
		parametros.setValor("dirfisica",new DireccionEthernet(dirEnlace));
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
		   this.nivelEnlace=new NivelEthernet(equipo,red,(DireccionEthernet)getDirFisica());
		else
			throw new IllegalArgumentException("Falta informacion para crear el nivel del enlace del interfaz");
	}
}
