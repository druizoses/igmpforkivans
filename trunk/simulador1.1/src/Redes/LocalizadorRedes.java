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
import Proyecto.ListaParametros;
import java.lang.reflect.*;

/**
 * Cargador de clases asociadas a las redes
 */
public class LocalizadorRedes
{
	/**
	 * Nombre de los paquetes que contienen los datos de las Redes
	 */
    static Vector Clases;
    
    /**
     * Identificadores de los niveles
     */
    static Vector IDs;
    
    
    
    /**
     * Inicializador de la clases
     */
    static 
	{
    	Clases=new Vector();
    	IDs=new Vector();
	}
    
    
    
    /**
     * Registra un nuevo paquete en el Localizador de redes
     * @param nombre Nombre del paquete/tipo de red
     * @throws IllegalArgumentException
     */
    public static void Registrar(String nombre) throws IllegalArgumentException
    {
    	try
    	{   
    	    // Comprobamos que la clase se puede cargar 
    	    Red red=(Red)Class.forName("Redes."+nombre).newInstance();
    	    if(red==null)
    	    	throw new IllegalArgumentException("Error cargando el paquete Redes."+nombre);
    	    red=null;
    	    Clases.add(nombre);
    	}
    	catch(ClassNotFoundException ex1)
    	{
    		throw new IllegalArgumentException("Redes."+nombre);
    	}
    	catch(IllegalAccessException ex2)
    	{
			throw new IllegalArgumentException("Redes."+nombre);
    	}
		catch(InstantiationException ex3)
		{
			throw new IllegalArgumentException("Redes."+nombre);
		}
    }
    
    
    
    /**
     * Devuelve el numero de tipos de redes reconocidas por el sistema
     * @return Numero de tipos de redes
     */
    public static int NumRedes()
    {
    	return(Clases.size());
    }
    
    
    
	/**
	 * Crea un nuevo objeto red del subtipo especificado
	 * @param tipo Tipo de red (Nombre completo como: Redes.Ethernet.Ethernet)
	 * @param nombre Nombre de la red
	 * @return Red
	 * @throws IllegalArgumentException
	 */
	public static Red New(String tipo,String nombre) throws IllegalArgumentException
	{
		Red r=null;
		boolean encontrado=false;
		String tipo_aux;
		
		for(int i=0;i<Clases.size() && !encontrado;i++)
		{
			tipo_aux=(String)Clases.get(i);
			if(tipo_aux.equals(tipo))
			{
				encontrado=true;
				try {
				   r=(Red)Class.forName("Redes."+tipo).newInstance();
				   if(CompruebaCaracteristicas(r.Caracteristicas())==false)
				   	   throw new Exception();
				   r.setNombre(nombre);
				} catch(Exception ex) {
					throw new IllegalArgumentException("Redes."+tipo);
				} 
			}
		}
		
		if(!encontrado || r==null)
		   throw new IllegalArgumentException(tipo);
		
		return(r);
	}


	
	/**
	 * Crea un nuevo interfaz del subtipo especificado
	 * @param nombre Nombre del interfaz
	 * @param ip Direccion IP del interfaz
	 * @param mascara Mascara IP asociada a la direccion IP
	 * @param dirEnlace Direccion del nivel de enlace
	 * @param tipo Tipo de interfaz 
     * @throws IllegalArgumentException si el tipo especificado no es un nombre de paquete valido
	 * @return Interfaz Nueva interfaz creada
	 */
	public static Interfaz NewInterfaz(String nombre,String ip,String mascara,String dirEnlace,String tipo) throws IllegalArgumentException 
	{
	    Interfaz interfaz=null;
        String claseInterfaz=null;
	    
		try
		{
		    Red r=New(tipo,"sin nombre");
		    claseInterfaz=(String)r.Caracteristicas().getValor("clase_interfaz");
		    
			Class argumentos[];
			argumentos=new Class[4];
			argumentos[0]=Class.forName("java.lang.String");
			argumentos[1]=Class.forName("java.lang.String");
			argumentos[2]=Class.forName("java.lang.String");
			argumentos[3]=Class.forName("java.lang.String");
			Constructor constructor=Class.forName(claseInterfaz).getConstructor(argumentos);
			Object args[]=new Object[4];
			args[0]=new String(nombre);
			args[1]=new String(ip);
			args[2]=new String(mascara);
			args[3]=new String(dirEnlace);
    	    
			interfaz=(Interfaz)constructor.newInstance(args);
		}
		catch(Exception e2)
		{
			throw new IllegalArgumentException("No se ha encontrado "+claseInterfaz);
		}
		
		return(interfaz);
    }
	
	
	
	/**
	 * Comprueba que las caracteristicas de la red estan todas, ademas
	 * son correctas
	 * @param c Lista de caracteristicas
	 * @return True si todo esta correcto, false en otro caso
	 */
    private static boolean CompruebaCaracteristicas(ListaParametros c)
	{
    	boolean correcto=false;
    	
    	if(c!=null)
    	{
    		String nombre=(String)c.getValor("nombre");
    		String clase_interfaz=(String)c.getValor("clase_interfaz");
    		String clase_nivel=(String)c.getValor("clase_nivel");
    		String clase_direccion=(String)c.getValor("clase_direccion");
    		String clase_trama=(String)c.getValor("clase_trama");
    		String dibujo=(String)c.getValor("dibujo");
    		Boolean se_conecta_a_redes=(Boolean)c.getValor("se conecta a redes");
    		
    		if(nombre!=null && clase_interfaz!=null && clase_direccion!=null)
    		{
    			if(clase_nivel!=null && clase_trama!=null && se_conecta_a_redes!=null)
    			{
    				if(dibujo!=null)
    				{
    					dibujo=dibujo.toLowerCase();
    					if(dibujo.equals("equipo"))
    					{
    						String imagen=(String)c.getValor("imagen");
    						if(imagen!=null)
    						{
    							correcto=true;
    						}
    					}
    					else if(dibujo.equals("bus") || dibujo.equals("anillo") || dibujo.equals("punto a punto"))
    					{
    						correcto=true;
    					}
    				}
    			}
    		}
    	}
    	
		return(correcto);
    	
    }
    
    
    
    /**
     * Registra codigos usados por los distintos modulos de la pila para identificarse
	 * @param nivel Nivel que conoce el identificador del nivel inferior
	 * @param id_nivel_inferior Identificador del nivel inferior
	 * @param codigo Codigo que el nivel inferior usa para referirse al nivel
     */
    public static void Registrar(String nivel, String id_nivel_inferior, int codigo)
	{
	    if(nivel!=null && id_nivel_inferior!=null && codigo>=0 && codigo<=65535)
	    	IDs.add(new IDNivel(nivel,id_nivel_inferior,codigo));
	    else
	    	throw new IllegalArgumentException("Valores no validos de identificadores de nivel");
    }
    
    
    
    /**
     * Devuelve una tripleta de identificadores de nivel
     * @param i i-esima tripleta a consultar
     * @return Identificador de nivel
     */
    public static IDNivel getIDNivel(int i)
	{
    	if(i>=0 && i<IDs.size())
    		return((IDNivel)IDs.get(i));
    	return(null);
    }
}
