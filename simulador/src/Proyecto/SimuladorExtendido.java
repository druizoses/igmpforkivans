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

package Proyecto;

import Equipos.Equipo;
import Proyecto.Acciones.Accion;
import Redes.Red;

/**
 * Nucleo de la gestion de eventos
 */
public class SimuladorExtendido extends Simulador
{
    /**
     * Constructor.
     */
    public SimuladorExtendido()
    {
    	super();
    }
    
    public void agregarALaSimulacion(Equipo e,Accion accion, int instante){
    	// armar una tabla de hash o algo asi y meter las acciones ahi
    }
    
    /**
     * Simula el paso de un instante en la simulacion, despues de cada paso
     * conviene consultar el estado de todo el sistema para comprobar los cambios
     * @return El siguiente instante de tiempo (puede ser el actual)
     */
    public boolean SimularUnPaso()
    {
    	
    	
    	
    	boolean se_puede_continuar=false;
    	
    	Objeto.DEBUG("\n\n");
    	Objeto.DEBUG("Instante "+instante_actual);
    	
    	// 1. Comprobamos si algun equipo tiene algo que procesar en
    	//    el instante de tiempo actual
    	for(int i=0;i<ListaEquipos.size();i++)
    	{
    		Equipo e=(Equipo)ListaEquipos.get(i);
    		e.Procesar(instante_actual);
    	}
    	
    	// 2. Comprobamos si alguna red tiene que enviar alguna trama
    	for(int i=0;i<ListaRedes.size();i++)
    	{
    		Red r=(Red)ListaRedes.get(i);
    		r.Procesar(instante_actual);
    	}
    	
    	// 3. Comprobamos si queda algun paquete por procesar en los equipos o en las redes
    	for(int i=0;!se_puede_continuar && i<ListaEquipos.size();i++)
    	{    
    	    Equipo e=(Equipo)ListaEquipos.get(i);
    	    if(e.Pendientes()>0)
    	        se_puede_continuar=true;
    	}
    	for(int i=0;!se_puede_continuar && i<ListaRedes.size();i++)
    	{
    	    Red r=(Red)ListaRedes.get(i);
    	    if(r.Pendientes()>0)
    	        se_puede_continuar=true;
    	}
    	
    	// 4. Pasamos al siguiente instante de tiempo
    	instante_actual++;

    	// 5. Devolvemos el estado del simulador
        if(se_puede_continuar && instante_actual==numPasosMax)
        {
            System.out.println("Salida forzada en "+numPasosMax+" iteraciones");
            se_puede_continuar=false; //salida forzada
        }
        return(se_puede_continuar);
    }
}
