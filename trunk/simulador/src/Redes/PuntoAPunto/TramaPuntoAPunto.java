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

import Redes.Buffer;

/**
 * Trama en las redes PuntoAPunto
 */
public class TramaPuntoAPunto extends Buffer 
{
    /**
     * Constructor
     * @param destino direccion destino
     * @param p datos contenidos
     * @param tipo tipo de los datos contenidos
     * @throws IllegalArgumentException Si el tipo de datos contenidos no son de tipo conocido
     */
    public TramaPuntoAPunto(DireccionPuntoAPunto destino, Buffer p, int tipo) throws IllegalArgumentException
    {	       
		// Tamaño de la trama
		Redimensiona(1+2+p.Tam());  // 1 de la direccion +2 del tipo de contenido...
		
        // Direccion destino
        setDestino(destino);
        
        // Tipo de datos contenidos
        setTipo(tipo);
        
        // Contenido
        for(int i=0;i<p.Tam();i++)
        	setByte(3+i,p.getByte(i));
    }
    
    
    
    /**
     * Constructor
     * @param trama Buffer con los datos de la trama
     */
    public TramaPuntoAPunto(Buffer trama)
    {
        super(trama);
    }
    
    
    
    /**
     * Cambia la direccion de destino
     * @param destino Direccion de destino
     */
    public void setDestino(DireccionPuntoAPunto destino)
    {
		setByte(0,destino.getByte(0));
    }
    
	
	
	/**
	 * Devuelve la direccion de destino
	 * @return direccion de destino
	 */
	public DireccionPuntoAPunto getDestino()
	{
		return(new DireccionPuntoAPunto(getByte(0)));
	}
    
    
    
    /**
     * Cambia el tipo de datos contenidos (tipo de la carga util)
     * @param tipo Tipo
     * @throws IllegalArgumentException si el tipo no esta en el rango [0-65535]
     */
    public void setTipo(int tipo)
    {
    	if(tipo<0 || tipo>65535)
    		throw new IllegalArgumentException("El valor para el tipo no es correcto");
    	
    	// Byte 1 -> byte alto
    	setByte(1,tipo>>8);
    	
    	// Byte 2 -> byte bajo
    	setByte(2,tipo&0x00FF);
    }
    
    
    
    /**
     * Devuelve el tipo de los datos contenidos
     * @return Tipo de los datos del campo de datos
     */
    public int getTipo()
    {
    	return((getByte(1)<<8)+getByte(2));
    }
}
