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
 * Creado el 15-mar-2004
 */
package Redes.PuntoAPunto;

import Redes.Direccion;

/**
 * Direccion PuntoAPunto
 */
public class DireccionPuntoAPunto extends Direccion 
{
	/**
	 * Constructor
	 * @param dir Direccion Punto a punto (valor de 0 a 255, 255 se usa como broadcast)
	 * @throws IllegalArgumentException Si la direccion no esta comprendida entre 0 y 255
	 */
	public DireccionPuntoAPunto(String dir) throws IllegalArgumentException
	{
	    super(1,PuntoAPunto.kTIPO);
		 
	 	if(dir==null)
	 		throw new IllegalArgumentException("La direccion Punto a punto no es valida");
	 	 
	 	// 1. Parsring
	 	try
		{
	 	    setByte(0,Integer.parseInt(dir));
		}
	 	catch(Exception e)
		{
	 		  throw new IllegalArgumentException("Direccion Punto a punto no valida");
	 	}
	}
	 
	 
	   
	/**
	 * Constructor en base a los bytes que la forman
	 * @param valor Byte 0
	 * @throws IllegalArgumentException
	 */
	public DireccionPuntoAPunto(int valor) throws IllegalArgumentException
	{
	    super(1,PuntoAPunto.kTIPO);
	    	
		setByte(0,valor);
    }
	    
	    
	    
	/**
	 * Constructor
	 * @param direccion Direccion de la misma longitud (1 byte)
	 * @throws IllegalArgumentException si la direccion indicada no es compatible
	 */
	public DireccionPuntoAPunto(Direccion direccion) throws IllegalArgumentException
	{
	   	super(1,PuntoAPunto.kTIPO);
	    	
	   	if(direccion==null || direccion.Longitud()!=1)
	   		throw new IllegalArgumentException("Direccion incorrecta");
	    	
	   	setByte(0,direccion.getByte(0));
	}

	
	    
    /**
     * Convierte la direccion MAC a una cadena de texto (en hexadecimal)
     * @return Direccion en formato de cadena de texto
     */
    public String toString()
	{  	
    	return(Integer.toHexString(getByte(0)));
    }	 
    
    
    
    /**
     * Comprueba si la direccion punto a punto es de broadcast
     * @return Cierto si la direccion es de broadcast
     */
    public boolean EsBroadcast()
	{
    	boolean retorno=true;
    	
   		if(getByte(0)!=255)
   			retorno=false;
    		
    	return(retorno);
    }
}
