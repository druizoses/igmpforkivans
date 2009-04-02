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

package Redes;

import java.util.Vector;
import Equipos.Equipo;
import Proyecto.Objeto;

/**
 * Clase base para las redes
 */
public abstract class Red extends Objeto
{
	/**
	 * Equipos conectados a la red
	 */
	protected Vector ListaEquipos;
	
	/**
	 * MTU, Unidad maxima de transferencia. 
	 */
	int MTU;

	/**
	 * Cola de tramas que circulan por la red
	 */
	protected Vector colaTramas;
	
	/**
	 * Lista de redes a las que debemos enviar tambien los paquetes
	 */
	protected Vector ListaRedes;
	
	
	
	/**
	 * Constructor
	 * @param nombre Nombre de la red
     * @param mtu MTU de la red
     * @throws IllegalArgumentException si la mtu o el nombre no es valida
	 */
    public Red(String nombre, int mtu)
    {
    	// 0. Inicializacion
    	super();
		setNombre(nombre);
    	
		if(mtu<=0)
		   throw new IllegalArgumentException("La MTU debe ser mayor que 0");
		MTU=mtu;

    	ListaEquipos=new Vector();
    	colaTramas=new Vector();
    	ListaRedes=new Vector();
    }
    
    
    
    /** 
     * Añade una equipo a la red
     * @param equipo Equipo a añadir
     * @param nombreInterfaz Nombre de la interfaz
     * @throws IllegalArgumentException
     */  
    public void ConectarA(Equipo equipo, String nombreInterfaz) throws IllegalArgumentException
    {
        if(equipo==null || nombreInterfaz==null)
            throw new IllegalArgumentException("El equipo no posee la interfaz indicada");
        
    	Interfaz interfaz=equipo.getInterfaz(nombreInterfaz);
    	
    	if(interfaz!=null)
    	{
    	   ListaEquipos.add(equipo);
    	   Interfaces.add(interfaz);
       	
           DEBUG("Red: "+getNombre()+" tiene un nuevo equipo ("+equipo.getNombre()+")");
    	}
    }
    
    
 
    /**
     * Conecta una red a otra red (util en hubs, o switches...). La usan otras redes
     * para decirle a esta que tiene que reenviar los datos tambien a la otra red.
     * (¡No usar directamente!)
     * @param red Red a la que se debe conectar esta red
     */
    public void ConectarA(Red red)
    {
        if(red!=null && red!=this)
            ListaRedes.add(red);
    }

    
    /**
     * Conecta esta red a otra red. Si las clases derivadas no redefinen este metodo,
     * no se podran conectar a otras redes. El comportamiento por defecto, es no dejar
     * que una red se conecte a otra, de ahi que se lance la excepcion
     * @param red Red a la que queremos conectar esta red
     * @throws IllegalArgumentException Por defecto una red no se puede conectar a otra
     */
    public void Conectar(Red red)
    {
        throw new IllegalArgumentException("Este tipo de red no se puede conectar directamente a otra red");
    }
    
    
    
    /**
     * Devuelve la MTU de la red
     * @return MTU de la red
     */
    public int getMTU()
    {
    	return(MTU);
    }
    
    
    
    /**
     * Cambia la MTU de una red
     * @param mtu Nueva MTU
     */
    public void setMTU(int mtu)
    {
    	MTU=mtu;
    }
    
    
    
	/**
	 * Envia una trama a la red (metodo a usar por los Equipos)
	 * @param dato Datos de la trama 
	 */
	public void Enviar(Dato dato)
	{
	    if(dato!=null)
	    {    
	        dato.red=this;
		    colaTramas.add(dato);
	    }
	}
	

	
	/**
	 * Envia una trama a la red (se usa cuando se reenvia desde otra red)
	 * @param dato Datos de la trama 
	 */
	public void Retransmitir(Dato dato)
	{
	    if(dato!=null)
	        colaTramas.add(dato);
	}
	
	
	
	/**
	 * Devuelve el numero de equipos conectados a la red
	 * @return Numero de equipos conectados
	 */
	public int NumEquiposConectados()
	{
	    return(ListaEquipos.size()+ListaRedes.size());
	}
	
	
	
	/**
	 * Por defecto, las redes soportan ARP. En el caso de las redes que no lo
	 * soporten deberan sobrecargar este metodo.
	 * @return Cierto, ya que por defecto, si no se indica otra cosa, se soporta ARP
	 */
	public boolean SoportaARP()
	{
		return(true);
	}
}
