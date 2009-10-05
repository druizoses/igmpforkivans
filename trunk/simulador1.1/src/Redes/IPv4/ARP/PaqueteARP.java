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

import Redes.Buffer;
import Redes.Direccion;

/**
 * Paquete ARP
 */
public class PaqueteARP extends Buffer
{	
	/**
	 * Operacion: PETICION ARP
	 */
	public static final int kOP_PETICION_ARP = 1;
	
	/**
	 * Operacion: RESPUESTA ARP
	 */
	public static final int kOP_RESPUESTA_ARP = 2;
	
	
	
	/**
	 * Constructor, crea un paquete de tipo ARP
	 */
    public PaqueteARP()
    {
    }
    
    
    
    /**
     * Constructor, crea un paquete copiando bit a bit el contenido de otro
     * @param buffer Paquete que se quiere copiar
     */
    public PaqueteARP(Buffer buffer)
	{
    	super(buffer);
    }
    
         
    
    /**
     * Pone un valor en el campo 'hardware address space'
     * @param tipo Tipo de las direcciones fisicas
     */
	public void setHWAddressSpace(int tipo)
	{
		// 0. Comprobacion
		if(tipo<0 || tipo>65535)
			throw new IllegalArgumentException("Espacio de direcciones hardware invalido");
    	
		setByte(0,tipo>>8);
		setByte(1,tipo&0x00FF);
	}
    
	
	
	/**
	 * Obtiene el tipo de las direcciones fisicas
	 * @return Valor del campo 'hardware address space'
	 */
	public int getHWAddressSpace()
	{
		return((getByte(0)<<8)+getByte(1));
	}
    
	
	
	/**
	 * Pone un valor en el campo 'protocol address space'
	 * @param tipo Tipo direcciones de red
	 */
	public void setProtocolAddressSpace(int tipo)
	{
		// 0. Comprobacion
		if(tipo<0 || tipo>65535)
			throw new IllegalArgumentException("Espacio de direcciones de protocolo invalido");
    	
		setByte(2,tipo>>8);
		setByte(3,tipo&0x00FF);    	
	}

	
	
	/**
	 * Obtiene el tipo de las direcciones de red
	 * @return Valor del campo 'protocol address space'
	 */
	public int getProtocolAddressSpace()
	{
		return((getByte(2)<<8)+getByte(3));
	}
	
	
	
	/**
	 * Pone un valor en el campo 'hardware address length'
	 * @param longitud Longitud de las direccion fisicas, en bytes
	 */
	public void setHWAddressLength(int longitud)
	{
		// 0. Comprobacion
		if(longitud<0 || longitud>255)
			throw new IllegalArgumentException("Longitud de direcciones fisicas invalido");
    	
		setByte(4,longitud);		
	}
	
	
	
	/**
	 * Obtiene la longitud, en bytes, de las direcciones fisicas
	 * @return Valor del campo 'hardware address length'
	 */
	public int getHWAddressLength()
	{
		return(getByte(4));
	}
	
	
	
	/**
	 * Pone un valor en el campo 'protocol address lenght'
	 * @param longitud Longitud en bytes de las direcciones de red
	 */
	public void setProtocolAddressLength(int longitud)
	{
		// 0. Comprobacion
		if(longitud<0 || longitud>255)
			throw new IllegalArgumentException("Longitud de direcciones de red invalido");
    	
		setByte(5,longitud);
	}
	
	
	
	/**
	 * Obtiene la longitud de las direcciones de red
	 * @return Valor del campo 'protocol address lenght'
	 */
	public int getProtocolAddressLength()
	{
		return(getByte(5));
	}
	
	
	
	/**
	 * Especifica que operacion realizara el paquete ARP
	 * @param operacion tipo de operacion, 1 para solocitud, 2 para respuesta
	 */
	public void setOperationCode(int operacion)
	{
		// 0. Comprobacion
		if(operacion<0 || operacion>65535)
			throw new IllegalArgumentException("Codigo de operacion invalido");
    	
		setByte(6,operacion>>8);
		setByte(7,operacion&0x00FF); 		
	}
	
	
	
	/**
	 * Devuelve el tipo de operacion
	 * @return Valor del campo 'operation code'
	 */
	public int getOperationCode()
	{
		return((getByte(6)<<8)+getByte(7));
	}
	
	
	
	/**
	 * Pone la direccion fisica del emisor en el paquete
	 * @param direccion Direccion fisica del emisor
	 */
	public void setHWAddressSender(Direccion direccion)
	{
		for(int i=0;i<getHWAddressLength();i++)
			setByte(8+i,direccion.getByte(i));
	}
	
	
	
	/**
	 * Devuelve la direccion fisica del emisor
	 * @return Direccion fisica
	 */
	public Direccion getHWAddressSender()
	{
		Direccion emisor=new Direccion(getHWAddressLength(),getHWAddressSpace());
		for(int i=0;i<emisor.Longitud();i++)
			emisor.setByte(i,getByte(8+i));
		
		return(emisor);
	}
	
	

	/**
	 * Pone la direccion de red del emisor en el paquete
	 * @param direccion Direccion de red del emisor
	 */
	public void setProtocolAddressSender(Direccion direccion)
	{
		for(int i=0;i<getProtocolAddressLength();i++)
			setByte(8+getHWAddressLength()+i,direccion.getByte(i));
	}
	
	
	
	/**
	 * Devuelve la direccion de protocolo del emisor
	 * @return Direccion del protocolo
	 */
	public Direccion getProtocolAddressSender()
	{
		Direccion emisor=new Direccion(getProtocolAddressLength(),getProtocolAddressSpace());
		for(int i=0;i<emisor.Longitud();i++)
			emisor.setByte(i,getByte(8+getHWAddressLength()+i));
		
		return(emisor);
	}    	
	

	
	/**
	 * Pone la direccion fisica del objetivo en el paquete
	 * @param direccion Direccion fisica del objetivo
	 */
	public void setHWAddressTarget(Direccion direccion)
	{
		for(int i=0;i<getHWAddressLength();i++)
			setByte(8+getHWAddressLength()+getProtocolAddressLength()+i,direccion.getByte(i));
	}
	
	 
	 
	/**
	 * Devuelve la direccion fisica del objetivo
	 * @return Direccion fisica
	 */
	public Direccion getHWAddressTarget()
	{
		Direccion objetivo=new Direccion(getHWAddressLength(),getHWAddressSpace());
		for(int i=0;i<objetivo.Longitud();i++)
			objetivo.setByte(i,getByte(8+getHWAddressLength()+getProtocolAddressLength()+i));
		
		return(objetivo);
	}
	 
	
	
	/**
	 * Pone la direccion de red del objetivo en el paquete
	 * @param direccion Direccion de red del objetivo
	 */	
	public void setProtocolAddressTarget(Direccion direccion)
	{
 	    for(int i=0;i<getProtocolAddressLength();i++) 
		    setByte(8+2*getHWAddressLength()+getProtocolAddressLength()+i,direccion.getByte(i));
	}
	
	 
	 
	/**
	 * Devuelve la direccion del protocolo del objetivo
	 * @return Direccion del protocolo
	 */
	public Direccion getProtocolAddressTarget()
	{
		Direccion objetivo=new Direccion(getProtocolAddressLength(),getProtocolAddressSpace());
		for(int i=0;i<objetivo.Longitud();i++)
			objetivo.setByte(i,getByte(8+2*getHWAddressLength()+getProtocolAddressLength()+i));
		
		return(objetivo);
	}
}
