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

package Redes.IPv4.ARP;

import Redes.Direccion;

/**
 * Paquete ARP usado en las peticiones
 */
public class PeticionARP extends PaqueteARP
{
	/**
	 * Constructor, crea un paquete de tipo peticion ARP
	 * @param dir_hardware_e Direccion fisica del emisor
	 * @param dir_protocolo_e Direccion de red del emisor
	 * @param dir_protocolo_r Direccion de red del receptor/objetivo
	 */
    public PeticionARP(Direccion dir_hardware_e, Direccion dir_protocolo_e, Direccion dir_protocolo_r)
    {  	
    	// 0. Comprobamos los parametros
    	if(dir_protocolo_e==null || dir_protocolo_r==null || dir_hardware_e==null)
    		throw new IllegalArgumentException("Valores nulos no permitidos");
    	else if(dir_protocolo_e.Tipo()!=dir_protocolo_r.Tipo())
    		throw new IllegalArgumentException("Tipos de direcciones de red incompatibles");
    	
    	// 1. Creamos el paquete
    	int n=dir_hardware_e.Longitud();
    	int m=dir_protocolo_e.Longitud();
        Redimensiona(8+n+m+n+m);
    	
    	// 2. Asignamos valores a los campos
    	setHWAddressSpace(dir_hardware_e.Tipo());
        setProtocolAddressSpace(dir_protocolo_e.Tipo());
        setHWAddressLength(dir_hardware_e.Longitud());
        setProtocolAddressLength(dir_protocolo_e.Longitud());
        setOperationCode(kOP_PETICION_ARP);
        setHWAddressSender(dir_hardware_e);
        setProtocolAddressSender(dir_protocolo_e);
        //setHWAddressTarget(X);  //es lo que buscamos
        setProtocolAddressTarget(dir_protocolo_r);
    }
    

    
    /**
     * Constructor
     * @param peticion Buffer con los datos
     */
    public PeticionARP(Redes.Buffer peticion)
    {
        super(peticion);
    }
}
