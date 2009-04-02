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
 * Creado el 14-feb-2004
 */
 
package Redes.IPv4.ICMP;

/**
 * Errores simulables por el módulo ICMP
 */
public class ErroresICMP
{
	/**
	 * Flags asociados a los errores que se pueden simular
	 */
    public static String[] flags={ 
            "IGNORAR_ICMP_3_2",   // como no se ha implementado ningun nivel de trasporte al
                                  // recibir un datagrama ip no se puede pasar a ningun nivel
                                  // superior (menos icmp) y se envia un Protocol Unreachable 
    		"IGNORAR_ICMP_REPLY"
    };
    
    
    /**
     * Descripcion de los errores que se pueden simular
     */
    public static String descripcion[]={
            "Ignora los mensajes ICMP Protocolo Inalcanzable",
			"Ignora los mensajes ICMP Echo Reply"
    };
}
