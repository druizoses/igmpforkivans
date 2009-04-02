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
 
package Redes.IPv4.ARP;

/**
 * Errores simulables por el módulo ARP
 */
public class ErroresARP
{
	/**
	 * Flags asociados a los errores que se pueden simular
	 */
    public static String[] flags={ 
            "NO_ENVIAR_PETICIONES",
            "NO_ENVIAR_RESPUESTAS",
            "IGNORAR_PETICIONES_RECIBIDAS",
            "IGNORAR_RESPUESTAS_RECIBIDAS"
    };
    
    
    /**
     * Descripcion de los errores que se pueden simular
     */
    public static String descripcion[]={
            "No envia peticiones ARP",
            "No envia respuestas ARP",
            "Ignora las peticiones ARP recibidas",
            "Ignora las respuestas ARP recibidas"
    };
}
