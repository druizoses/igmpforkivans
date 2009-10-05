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

/*
 * Creado el 25-mar-2004
 */
package Redes;

/**
 * Identificador de los niveles de la pila de comunicaciones
 */
public class IDNivel
{
	/**
	 * Nivel que conoce el identificador del nivel inferior
	 */
	public String nivel;
	
	/**
	 * Identificador del nivel inferior
	 */
	public String id_nivel_inferior;
	
	/**
	 * Codigo que el nivel inferior usa para referirse al nivel
	 */
	public int codigo;
	
	
	
	/**
	 * Constructor
	 * @param nivel Nivel que conoce el identificador del nivel inferior
	 * @param id_nivel_inferior Identificador del nivel inferior
	 * @param codigo Codigo que el nivel inferior usa para referirse al nivel
	 */
	public IDNivel(String nivel, String id_nivel_inferior, int codigo)
	{
	    this.nivel=nivel;
	    this.id_nivel_inferior=id_nivel_inferior;
	    this.codigo=codigo;
	}
}
