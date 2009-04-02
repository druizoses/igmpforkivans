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
import Redes.*;

/**
 * Clase base para todos los componentes simulables (redes y equipos)
 */
public abstract class Objeto 
{
	/**
	 * Lista de interfaces
	 */
	protected Vector Interfaces;
	
	/**
	 * Lista de eventos ocurridos durante la simulacion
	 */
	private Vector eventos;
	
	/**
	 * Lista de parametros;
	 */
	protected ListaParametros parametros;
	
	
	
	
	/**
	 * Constructor
	 */
	public Objeto()
    {
    	Interfaces=new Vector();
    	eventos=new Vector();
    	parametros=new ListaParametros();
        parametros.add(new Parametro("nombre","Nombre","java.lang.String"));
        parametros.setValor("nombre","sin nombre");
    }
	
    
    
    /**
	 * Devuelve el numero de interfaces que tiene el objeto
	 * @return Numero de interfaces
	 */
	public int NumInterfaces()
	{
		return(Interfaces.size());
	}
	
	
	
	/**
	 * Devuelve una Interfaz del objeto
	 * @param numero Numero de interfaz que se desea conseguir
	 * @return Interfaz
	 * @throws IllegalArgumentException
	 */
	public Interfaz getInterfaz(int numero) throws IllegalArgumentException
	{
		if(numero<0 || numero>=Interfaces.size())
			throw new IllegalArgumentException("No existe la interfaz numero "+numero); 

		return((Interfaz)Interfaces.get(numero));
	}
	
	
	
	/**
	 * Devuelve la interfaz con el nombre indicado
	 * @param nombreInterfaz Nombre de la interfaz
	 * @return Una interfaz
	 * @throws IllegalArgumentException
	 */
	public Interfaz getInterfaz(String nombreInterfaz) throws IllegalArgumentException
	{
		boolean encontrada=false;
		Interfaz interfaz=null;
		
		// 0. Comprobacion de entrada
		if(nombreInterfaz==null)
			throw new IllegalArgumentException("El nombre de la interfaz no puede ser nulo");
		
		// 1. Buscamos la interfaz
		for(int i=0;i<NumInterfaces() && !encontrada;i++)
		{
			interfaz=getInterfaz(i);
			//System.out.print("nombreInterfaz: ["+nombreInterfaz+"],  ["+interfaz.getNombre()+"] son iguales? ");
			if(nombreInterfaz.equals(interfaz.getNombre()))
			   encontrada=true;
		}
		
		if(!encontrada)
		   throw new IllegalArgumentException("No existe una interfaz con el nombre indicado");

		// 2. Devolvemos la interfaz
		return(interfaz);
	}

    
    
	/**
	 * Devuelve el numero de paquetes pendientes de ser procesados
	 * @return Numero de paquetes pendientes
	 */
    public abstract int Pendientes();
    
    
	/**
	 * Devuelve el nombre del objeto
	 * @return Nombre del Objeto
	 */
	public String getNombre()
	{
	    String valor=(String)parametros.getValor("nombre");
	    
	    if(valor!=null)
		    return(valor);
	    return("sin nombre");
	}
	
	
	
	/**
	 * Cambia el nombre del objeto
	 * @param nombre Nuevo nombre
	 */
	public void setNombre(String nombre)
	{
	    if(nombre!=null)
		    parametros.setValor("nombre",nombre);
	}
	
	
	
	/**
	 * Muestra un mensaje de depuracion en la pantalla
	 * @param mensaje Mensaje de depuracion
	 */
	public static void DEBUG(String mensaje)
	{
		//System.out.println("[Debug] "+mensaje);
		System.out.flush();
	}
	
	
	
	/**
	 * Registra un nuevo evento
	 * @param indicador Indicador de la accion: 'R'ecibido, 'E'nviado, 'T'ransmitido...
	 * @param instante Instante de tiempo
 	 * @param paquete Paquete de datos asociado al evento
	 * @param mensaje Mensaje descriptivo del evento
	 */
	public void NuevoEvento(char indicador, int instante, Buffer paquete, String mensaje)
	{
	    Evento evento=new Evento(indicador,instante,paquete,mensaje);
	    eventos.add(evento);
	    
	    //System.out.println(instante+": ["+indicador+"] "+parametros.getValor("nombre")+", "+mensaje+" ["+paquete.getClass().getName()+"]");
	    System.out.println(instante+": ["+indicador+"] "+parametros.getValor("nombre")+", "+mensaje);        
	}
	
	

	/**
	 * Devuelve el i-esimo evento de la lista de eventos
	 * @param i Numero de evento
	 * @return i-esimo evento de la lista de eventos
	 */
	public Evento getEvento(int i)
	{
		if(i>=0 && i<eventos.size())
			return((Evento)eventos.get(i));
	    return(null);	
	}
	
	
	
	/**
	 * Devuelve el numero de eventos de la lista de eventos
	 * @return Numero de eventos
	 */
	public int NumEventos()
	{
		if(eventos!=null)
		   return(eventos.size());
		return(0);
	}
	
	
	
    /**
	 * Devuelve la lista de parametros
	 * @return Lista de parametros
	 */	
	public ListaParametros getParametros()
	{
	    return(parametros);
	}

	
	
	/**
	 * Procesa los paquetes programados para un instante determinado
	 * @param instante Instante de tiempo
	 */
	abstract public void Procesar(int instante);
	
	
	
	/**
	 * Devuelve la lista de caracteristicas del objeto
	 * @return Lista de caracteristicas
	 */
	abstract public ListaParametros Caracteristicas();	
}
