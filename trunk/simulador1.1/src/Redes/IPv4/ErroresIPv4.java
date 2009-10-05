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
 * Creado el 11-feb-2004
 */
 
package Redes.IPv4;

/**
 * Errores simulables en el módulo IP
 */
public class ErroresIPv4
{
	/**
	 * Flags asociados a los tipos de errores
	 */
    public static String[] flags={ 
        "NO_ENVIAR_1",
        "NO_ENVIAR_2" 
    };
    
    
    /**
     * Descripciones de los errores que se pueden simular
     */
    public static String descripcion[]={
        "No envia el primer fragmento de los datagramas IP fragmentados",
        "No envia el segundo fragmento de los datagramas IP fragmentados"
    };
}
