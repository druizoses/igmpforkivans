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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Equipos.Equipo;
import Proyecto.Acciones.Accion;
import Redes.Red;

/**
 * Nucleo de la gestion de eventos
 */
public class SimuladorExtendido extends Simulador
{
	private class EquipoAccion {
		
		private Equipo equipo;
		private Accion accion;
		
		public EquipoAccion(Equipo equipo,Accion accion) {
			this.equipo=equipo;
			this.accion=accion;
		}

		public void ejecutar(int instante) {
			accion.ejecutar(equipo, instante);
		}
	}
    
	private Map<Integer, List<EquipoAccion>> map = null; 
	
	/**
     * Constructor.
     */
    public SimuladorExtendido()
    {
    	super();
    	map = new HashMap<Integer, List<EquipoAccion>>(); 
    }
    
    public void agregarALaSimulacion(Equipo e,Accion accion, int instante){
    	Integer instanteI= Integer.valueOf(instante);
    	if (!map.containsKey(instanteI)) {
    		map.put(instanteI, new ArrayList<EquipoAccion>());
    	}
    	map.get(instanteI).add(new EquipoAccion(e,accion));
    }
    
    /**
     * Simula el paso de un instante en la simulacion, despues de cada paso
     * conviene consultar el estado de todo el sistema para comprobar los cambios
     * @return El siguiente instante de tiempo (puede ser el actual)
     */
    public boolean SimularUnPaso()
    {
    	Objeto.DEBUG("\n\n");
    	Objeto.DEBUG("Instante "+instante_actual);

    	// 1. Comprobamos si una accion a simular en el instante de tiempo actual.
    	Integer instante_actualI = Integer.valueOf(instante_actual);
    	if (map.containsKey(instante_actualI))
    	{
    		for (EquipoAccion element : map.get(instante_actualI)) {
				element.ejecutar(instante_actual);
			}
    	}
    	
    	// 2. Comprobamos si algun equipo tiene algo que procesar en
    	//    el instante de tiempo actual
    	for(int i=0;i<ListaEquipos.size();i++)
    	{
    		Equipo e=(Equipo)ListaEquipos.get(i);
    		e.Procesar(instante_actual);
    	}
    	
    	// 3. Comprobamos si alguna red tiene que enviar alguna trama
    	for(int i=0;i<ListaRedes.size();i++)
    	{
    		Red r=(Red)ListaRedes.get(i);
    		r.Procesar(instante_actual);
    	}
    	
    	// 4. Pasamos al siguiente instante de tiempo
    	instante_actual++;

    	// 5. Devolvemos el estado del simulador
        if(instante_actual==numPasosMax)
        {
        	Objeto.DEBUG("Fin de la simulación");
        	return false;
        }
        return true;
    }
}
