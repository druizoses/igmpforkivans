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

import java.lang.reflect.*;
import java.util.Hashtable;

/**
 * Estructura de datos, sobre la comunicacion, que fluye entre los equipos 
 * y las redes.
 */
public class Dato
{
	/**
	 * Instante de tiempo en el que hay que procesar el paquete
	 */
	public int instante;
	
	/**
	 * Paquete de datos
	 */
	public Buffer paquete;
		
	/**
	 * En el caso de que el paquete sea de entrada y se deba pasar a un nivel
	 * superior de un conjunto de varios, este campo sirve para distinguir
	 * de entre esos varios. Por ejemplo, IP a UDP o TCP, este atributo
	 * identificaria si el paquete iria destinado al nivel UDP o al TCP.
	 * Si el paquete es de salida y el nivel inferior necesita un identificador
	 * de tipo o algo parecido, se le pasa a traves de este atributo
	 * (debe haberse registrado mediante el metodo setID() del nivel)
	 */
	public int protocolo;
	
	/**
	 * Direccion destino del nivel inferior al que esta procesando el paquete
	 */
	public Direccion direccion;
	
	/**
	 * Interfaz por donde entro la trama al equipo (o por donde debe salir)
	 */
	public Interfaz interfaz;
	
	/**
	 * Cuando un dato pasa por una red, la 'red' rellena este campo
	 * (util en el funcionamiento de los puentes) 
	 */
	public Red red;
	
	/**
	 * Indica si el dato se puede fragmentar o no
	 */
	public boolean fragmentable;
	
	public Hashtable ctrlSwitch;
	
	
	
	/** 
	 * Constructor
	 * @param instante  Instante en que se procesara el paquete
	 * @param p Paquete a procesar
	 */
	public Dato(int instante, Buffer p)
	{
		this.instante=instante;
		this.paquete=p;
		protocolo=-1;
		direccion=null;
		interfaz=null;
		fragmentable=true;
		red=null;
		ctrlSwitch=null;
    }
	

	
	/** 
	 * Constructor
	 * @param instante  Instante en que se procesara el paquete
	 * @param p Paquete a procesar
	 * @param nofragmentar Vale 0 si el paquete se puede fragmentar (ver nivel ip)
	 */
	public Dato(int instante, Buffer p, int nofragmentar)
	{
		this.instante=instante;
		this.paquete=p;
		protocolo=-1;
		direccion=null;
		interfaz=null;
		if(nofragmentar==1)
		   this.fragmentable=false;
		else
			this.fragmentable=true;
		red=null;
		ctrlSwitch=null;
    }
	
	
	/**
	 * Constructor de copia
	 * @param d Dato a copiar
	 * @throws IllegalArgumentException si el dato a copiar es nulo
	 */
	public Dato(Dato d)
	{
	    if(d==null)
	        throw new IllegalArgumentException("No se puede copiar un dato nulo");
	    
		instante=d.instante;
		if(d.paquete!=null)
		{
		    try
		    {    
		        Class argumentos[];
		        argumentos=new Class[1];
		        argumentos[0]=Buffer.class; //d.paquete.getClass();
		        Constructor constructor=d.paquete.getClass().getDeclaredConstructor(argumentos);
		        Object args[]=new Object[1];
		        args[0]=d.paquete;
		        paquete=(Buffer)constructor.newInstance(args);
		    }
		    catch(Exception e)
		    {
		        System.out.println("Paquete no duplicado!!!");
		        e.printStackTrace();
		    }
		}
		else
		{
			paquete=null;
		}
		
		protocolo=d.protocolo;
		
		if(d.direccion!=null)
		{
		    try
		    {
		        Class argumentos[];
		        argumentos=new Class[1];
		        argumentos[0]=Direccion.class; //d.paquete.getClass();
		        Constructor constructor=d.direccion.getClass().getDeclaredConstructor(argumentos);
		        Object args[]=new Object[1];
		        args[0]=d.direccion;
		        direccion=(Direccion)constructor.newInstance(args);
		    }
		    catch(Exception e)
		    {
		        System.out.println("Direccion no duplicada!!!");
		        e.printStackTrace();
		    }
		}
		else
		{
			direccion=null;
		}
		
		interfaz=d.interfaz;
	    fragmentable=d.fragmentable;
	    red=d.red;
	    if(d.ctrlSwitch!=null)
	    	ctrlSwitch = new Hashtable(d.ctrlSwitch);
	    	
	}
}
