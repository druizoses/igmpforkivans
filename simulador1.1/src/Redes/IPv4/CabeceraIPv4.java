/*
    Simulador de redes IP (KIVA). API de Simulacion, permite simular
    redes de tipo IP que usen IP, ARP, e ICMP.
    Copyright (C) 2004, Jos� Mar�a D�az Gorriz

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
 * Creado el 25-dic-2003
 */
 
package Redes.IPv4;

/**
 * Cabecera de un datagrama IP
 */
public class CabeceraIPv4 extends DatagramaIPv4
{
    /**
     * Constructor
     * @param datagrama Datagrama del que se extraeran los datos de la cabecera
     * @throws IllegalArgumentException
     */
	public CabeceraIPv4(DatagramaIPv4 datagrama) throws IllegalArgumentException
    {
	    super(datagrama.getIHL()*4);
	    
	    for(int i=0;i<datagrama.getIHL()*4;i++)
	        setByte(i,datagrama.getByte(i));
    }
}
