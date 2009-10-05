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

package Equipos;

import java.util.Vector;
import Proyecto.*;

/**
 * Cargador de clases asociadas a los equipos
 */
public class LocalizadorEquipos
{	
	/**
	 * Nombre de las clases que definen los distindos Equipos
	 */
	static Vector Clases;
    
	
	/**
	 * Inicializador de la clase
	 */
	static
	{
		Clases=new Vector();		
	}
	
    
    
	/**
	 * Registra un nuevo tipo de equipo 
	 * @param nombre Nombre de la clase
	 */
	public static void Registrar(String nombre)
	{
	    try
		{
			Equipo e=(Equipo)Class.forName("Equipos."+nombre).newInstance();
			if(CompruebaCaracteristicas(e.Caracteristicas())==false)
			    throw new Exception();
			Clases.add(nombre);
		}
	    catch(Exception ex) 
		{
			throw new IllegalArgumentException("No se ha podido cargar la clase 'Equipos."+nombre+"'");
	    }
	}
    

	
	/**
	 * Devuelve el numero de tipos de equipos disponible 
	 * @return Numero de tipos de redes
	 */
	public static int NumEquipos()
	{
		return(Clases.size());
	}
	
	
	
	/**
	 * Crea un nuevo objeto equipo del subtipo especificado
	 * @param tipo Tipo de equipo
	 * @param nombre Nombre del equipo
	 * @return Equipo
	 * @throws IllegalArgumentException
	 */
	public static Equipo New(String tipo, String nombre) throws IllegalArgumentException
	{
		Equipo e=null;
		boolean encontrado=false;
		String tipo_aux;
		
		for(int i=0;i<Clases.size() && !encontrado;i++)
		{
			tipo_aux=(String)Clases.get(i);
			if(tipo_aux.equals(tipo))
			{
				encontrado=true;
				try {
				   e=(Equipo)Class.forName("Equipos."+tipo).newInstance();
				   if(CompruebaCaracteristicas(e.Caracteristicas())==false)
				      throw new Exception();
				} catch(Exception ex) {
					throw new IllegalArgumentException("No se ha podido cargar la clase 'Equipos."+tipo+"'");
			    } 
				e.setNombre(nombre);
			}
		}
		
		if(!encontrado || e==null)
		   throw new IllegalArgumentException("No se ha podido cargar la clase 'Equipos."+tipo+"'");
		
		return(e);
	}
	
	
	
	/**
	 * Comprueba que la lista de caracteristicas tiene los parametros correctos,
	 * ademas comprueba que el fichero de imagen esta accesible
	 * @param c Lista de caracteristicas
	 * @return True si todo esta correcto, false en otro caso
	 */
	private static boolean CompruebaCaracteristicas(ListaParametros c)
	{
		boolean correcto=false;
		
		if(c!=null)
		{
			String nombre=(String)c.getValor("nombre");
			String imagen=(String)c.getValor("imagen");
			
			if(nombre!=null && imagen!=null)
			{
				correcto=true;
			} 
		}
		
		return correcto;
	}
}

