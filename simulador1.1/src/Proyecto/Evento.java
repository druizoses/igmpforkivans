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
 * Creado el 07-ene-2004
 */
 
package Proyecto;

import Redes.Buffer;

/**
 * Estado de un componente de la simulacion en un instante determinado
 */
public class Evento
{
    /**
     * Instante de tiempo
     */
    public int instante;
    
    /**
     * Mensaje descriptivo
     */
    public String mensaje;
    
    /**
     * Paquete de datos asociado al evento
     */
    public Buffer paquete;
    
    /**
     * Indicativo de la accion ('R'ecepcion / 'E'mision)
     */
    public char indicador;
    
    
    
    /**
     * Constructor
     * @param pindicador Indicador de la accion
     * @param pinstante Instante de tiempo en que se produce el evento
     * @param ppaquete Paquete de datos asociado al evento
     * @param pmensaje Mensaje descriptivo
     */
    public Evento(char pindicador, int pinstante, Buffer ppaquete, String pmensaje)
    {
        this.indicador=pindicador;
        this.instante=pinstante;
        this.paquete=ppaquete;  // ¡se copia la referencia!
        this.mensaje=pmensaje;
    }
}
