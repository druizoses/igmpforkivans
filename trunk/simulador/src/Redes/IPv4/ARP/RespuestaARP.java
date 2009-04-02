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
 * Creado el 17-dic-2003
 */
 
package Redes.IPv4.ARP;

import Redes.Direccion;

/**
 * Paquete ARP usado en las respuestas
 */
public class RespuestaARP extends PaqueteARP 
{
	/**
	 * Constructor, crea un paquete de tipo respuesta ARP
	 * @param peticion Peticion ARP a la que se responde
	 * @param dir_fisica Direccion fisica del equipo que responde
	 */
	public RespuestaARP(PaqueteARP peticion, Direccion dir_fisica)
	{  	
		// 0. Comprobamos los parametros
		if(peticion==null || dir_fisica==null)
			throw new IllegalArgumentException("Valores nulos no permitidos");
    	
		// 1. Creamos el paquete
		int n=peticion.getHWAddressLength();
		int m=peticion.getProtocolAddressLength();
		Redimensiona(8+n+m+n+m);
		
		// 2. Asignamos valores a los campos
		setHWAddressSpace(peticion.getHWAddressSpace());
		setProtocolAddressSpace(peticion.getProtocolAddressSpace());
		setHWAddressLength(peticion.getHWAddressLength());
		setProtocolAddressLength(peticion.getProtocolAddressLength());
		setOperationCode(kOP_RESPUESTA_ARP);
		
		setHWAddressSender(dir_fisica);         // direccion fisica de respuesta
		setProtocolAddressSender(peticion.getProtocolAddressTarget());
		setHWAddressTarget(peticion.getHWAddressSender());
		setProtocolAddressTarget(peticion.getProtocolAddressSender());
	}
	
	
	
	/**
	 * Constructor
	 * @param respuesta Buffer con los datos
	 */
	public RespuestaARP(Redes.Buffer respuesta)
	{
	    super(respuesta);
	}
}
