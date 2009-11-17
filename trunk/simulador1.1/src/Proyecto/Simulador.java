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

import java.util.Vector;

import Equipos.*;
import Redes.*;

/**
 * Nucleo de la gestion de eventos
 */
public class Simulador
{
	/**
	 * Lista de Equipos. Maquinas que generan paquetes de datos
	 */
    Vector ListaEquipos;
    
    /**
     * Lista de Redes. Topologias.
     */
    Vector ListaRedes;
    
    /**
     * Contador temporal, reloj del simulador
     */
    int instante_actual;
    
    /**
     * Numero maximo de iteraciones que se simularan
     */
    int numPasosMax;
    
    boolean iterado;
    
    
    
    /**
     * Constructor.
     */
    public Simulador()
    {
    	// 0. Inicializamos las listas
    	ListaEquipos=new Vector();
    	ListaRedes=new Vector();
    	
    	// 1. Inicializamos el contador de tiempo
    	instante_actual=0;
    	
    	// 2. Numero maximo de itenaciones
    	numPasosMax=-1;
    	iterado=false;
    }
    
    
    
    /**
     * Añade un equipo al sistema
     * @param equipo Equipo que se quiere añadir al sistema
     */
    public void NuevoEquipo(Equipo equipo)
    {
        if(equipo!=null)
        {
            Objeto.DEBUG("Simulador: nuevo equipo: "+equipo.getNombre());
    	    ListaEquipos.add(equipo);
        }
    }
    
    
    
    /**
     * Devuelve el equipo seleccionado de la lista de equipos del simulador
     * @param i Numero de equipo
     * @return Equipo
     */
    public Equipo getEquipo(int i)
    {
       if(i>=0 && i<ListaEquipos.size())
       	  return((Equipo)ListaEquipos.get(i));
       return(null);
    }
    
    
    
    /**
     * Añade una red al sistema
     * @param red Red que se va a añadir al sistema
     */
    public void NuevaRed(Red red)
    {
    	if(red!=null)
    	{
    		Objeto.DEBUG("Simulador: nueva red: "+red.getNombre());
    	    ListaRedes.add(red);    
    	}
    }
    
    

    /**
     * Devuelve una red de la lista de redes que conoce el simulador
     * @param i Numero de red
     * @return Red
     */
    public Red getRed(int i)
    {
        if(i>=0 && i<ListaRedes.size())
         	  return((Red)ListaRedes.get(i));
         return(null);	
    }
    
    
    
    /**
     * Simula el paso de un instante en la simulacion, despues de cada paso
     * conviene consultar el estado de todo el sistema para comprobar los cambios
     * @return El siguiente instante de tiempo (puede ser el actual)
     */
    public boolean SimularUnPaso()
    {
    	boolean se_puede_continuar=false;
    	int numInterfaz = 0;
    	int numRed = 0;
    	
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
    	
    	// 3. Comprobamos si existe algun interfaz conectado de un equipo a otro y no de equipo a red.
    	for(int i=0;i<ListaEquipos.size() && !iterado;i++)
    	{
    		Equipo e=(Equipo)ListaEquipos.get(i);
    		for(numInterfaz=0;numInterfaz<e.NumInterfaces();numInterfaz++){
    			for(numRed=0;numRed<ListaRedes.size();numRed++)
    				if((Red)e.getInterfaz(numInterfaz).getRed() == (Red)ListaRedes.get(numRed))
    					break;
    			if(numRed == ListaRedes.size() && !existeRed(e.getInterfaz(numInterfaz).getRed())){
    				e.getInterfaz(numInterfaz).getRed().Procesar(instante_actual);
    				ListaRedes.add(e.getInterfaz(numInterfaz).getRed());
    				break;
    			}
    		};
    	}
    	
    	iterado = true;
    	
    	// 4. Comprobamos si queda algun paquete por procesar en los equipos o en las redes
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
    	
    	// 5. Pasamos al siguiente instante de tiempo
    	instante_actual++;

    	// 6. Devolvemos el estado del simulador
        if(se_puede_continuar && instante_actual==numPasosMax)
        {
            System.out.println("Salida forzada en "+numPasosMax+" iteraciones");
            se_puede_continuar=false; //salida forzada
        }
        return(se_puede_continuar);
    }
    
    public boolean existeRed(Red red){
    	int i=0;
    	for(i=0;i<ListaRedes.size();i++)
    		if(red == (Red)ListaRedes.get(i))
    			break;
    	if(i == ListaRedes.size())
    		return false;
    	return true;
    }
    
    /**
     * Establece el numero maximo de iteraciones que se ejecutara el simulador
     * @param numPasos Numero de iteraciones
     */
    public void MaximoNumeroDePasos(int numPasos)
    {
        if(numPasos>0)
            numPasosMax=numPasos;
    }

	public int getInstanteActual() {
		return instante_actual;
	}

	public int getNumPasosMax() {
		return numPasosMax;
	}
}
