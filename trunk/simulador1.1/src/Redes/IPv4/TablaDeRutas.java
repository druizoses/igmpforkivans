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

package Redes.IPv4;

import Redes.*;
import Equipos.Equipo;
import java.util.Vector;

/**
 * Tabla de rutas
 */
public class TablaDeRutas
{
    /**
     * Direccion de destino
     */
    private Vector destinos;
    
    /**
     * Mascara
     */
    private Vector mascaras;
    
    /**
     * Gateway por el que se enviaran los paquetes que vayan a 'destino'
     */
    private Vector gateways;
    
    /**
     * Interfaz por la que se enviaran los paquetes que vayan a 'destino'
     */
    private Vector interfaces;
    
    /**
     * Tipo de ruta: Directa/Indirecta
     */
    private Vector tipo;
    
    /**
     * Equipo dueño de la tabla de rutas
     */
    private Equipo equipo;
    
    
    
    /**
     * Constructor
     * @param pequipo Equipo al que pertenece la tabla de rutas
     */
    public TablaDeRutas(Equipo pequipo)
    {
    	// 0. Inicializacion
    	destinos=new Vector();
    	mascaras=new Vector();
    	gateways=new Vector();
    	interfaces=new Vector();
    	tipo=new Vector();
    	this.equipo=pequipo;
    }
    
    
       
	/**
	 * Añade una entrada al final de la tabla de rutas
	 * @param destino Direccion IP de destino
	 * @param mascara Mascara
	 * @param gateway Gateway
	 * @param nombreInterfaz Interfaz de salida
	 * @throws IllegalArgumentException
	 */
	public void Anadir(String destino, String mascara, String gateway, String nombreInterfaz) throws IllegalArgumentException
	{
		boolean pordefecto=false;
		boolean directa=false;
		
        try
		{
        	/*System.out.println("Entro a añadir una ruta en la tabla de rutas para los siguientes datos:");
        	System.out.println("destino:"+destino+" mascara:"+mascara+"gateway:"+gateway+"nombreInterfaz:"+nombreInterfaz);*/
        	// 0. Comprobaciones previas
        	if(destino==null || mascara==null || gateway==null || nombreInterfaz==null)
        		throw new IllegalArgumentException("Ruta no valida, especifique todos los datos");
        	if(destino.equalsIgnoreCase("por defecto") || destino.equals("0.0.0.0"))
        		pordefecto=true;
        	if(gateway.equalsIgnoreCase("directa") || gateway.equals("0.0.0.0") || gateway.equals("255.255.255.255"))
        		directa=true;
        	if(pordefecto && directa)
        		throw new IllegalArgumentException("La ruta por defecto debe apuntar a un gateway");
        	
		    // 1. Creamos los objetos, entes de insertar por si se produce un error
	        if(pordefecto)
	        {
	        	destino="0.0.0.0";
	            mascara="255.255.255.255";
	        }
	        DireccionIPv4 destinoAux=new DireccionIPv4(destino);
	        MascaraIPv4 mascaraAux=new MascaraIPv4(mascara);
	        if(directa)
	    	    gateway="0.0.0.0";
	        
	    	DireccionIPv4 gatewayAux=new DireccionIPv4(gateway);
	    	//System.out.println("Interfaces: "+equipo.NumInterfaces());
	        Interfaz interfaz=equipo.getInterfaz(nombreInterfaz);
	        if(interfaz==null)
	        	throw new IllegalArgumentException("La interfaz especificada no pertenece a este equipo");
	        
	        // 2. Insertamos los objetos en la tabla
	        destinos.add(destinoAux);
  	        mascaras.add(mascaraAux);
		    gateways.add(gatewayAux);
		    interfaces.add(interfaz);
		    //System.out.println("Aqui llego e interfaz=equipo.getInterfaz(nombreInterfaz) vale: "+interfaz);
		
		    // 3. Comprobamos si la ruta es directa, y lo almacenamos en la tabla
		    if(directa)
		        tipo.add(new Boolean(true));  // ruta directa
		    else
		        tipo.add(new Boolean(false)); // ruta indirecta, a traves de un gateway valido
		}
        catch(Exception e)
		{
        	if(e.getMessage().length()==0)
         	   throw new IllegalArgumentException("Entrada no valida para la tabla de rutas");
        	throw new IllegalArgumentException("Entrada no valida para la tabla de rutas ("+e.getMessage()+")");
		}
	}
    


	/**
	 * Añade una entrada al final de la tabla de rutas
	 * @param destino Direccion IP de destino
	 * @param mascara Mascara
	 * @param gateway Gateway
	 * @param ipSalida Direccion IP del interfaz de salida
	 * @throws IllegalArgumentException
	 */
	public void Anadir(String destino, String mascara, String gateway, DireccionIPv4 ipSalida) throws IllegalArgumentException
	{
        try
		{
        	// 0. Comprobaciones previas
        	if(destino==null || mascara==null || gateway==null || ipSalida==null)
        		throw new IllegalArgumentException("Ruta no valida, especifique todos los datos");
        	if(destino.equalsIgnoreCase("por defecto") && gateway.equalsIgnoreCase("directa"))
        		throw new IllegalArgumentException("La ruta por defecto debe apuntar a un gateway");
        	
		    // 1. Creamos los objetos, entes de insertar por si se produce un error
	        if(destino.equalsIgnoreCase("por defecto"))
	        {
	        	destino="0.0.0.0";
	        	mascara="255.255.255.255";
	        }
        	DireccionIPv4 destinoAux=new DireccionIPv4(destino);
	        MascaraIPv4 mascaraAux=new MascaraIPv4(mascara);
	        if(gateway.equalsIgnoreCase("directa"))
	    	    gateway="0.0.0.0";
	        DireccionIPv4 gatewayAux=new DireccionIPv4(gateway);
	       
	        // 2. Buscamos la interfaz con la IP de salida dada
	        Interfaz interfaz=null;
	        for(int numInterfaz=0;numInterfaz<equipo.NumInterfaces();numInterfaz++)
	        {
	        	DireccionIPv4 ip=equipo.getInterfaz(numInterfaz).getIP();
	        	if(ipSalida.equals(ip))
	        		interfaz=equipo.getInterfaz(numInterfaz);
	        }	        
	        if(interfaz==null)
	        	throw new IllegalArgumentException("La interfaz especificada no pertenece a este equipo");;
	        
	        // 3. Insertamos los objetos en la tabla
	        destinos.add(destinoAux);
  	        mascaras.add(mascaraAux);
		    gateways.add(gatewayAux);
		    interfaces.add(interfaz);
		
		    // 4.. Comprobamos si la ruta es directa, y lo almacenamos en la tabla
		    if(gateway.equals("0.0.0.0"))
		        tipo.add(new Boolean(true));  // ruta directa
		    else
		        tipo.add(new Boolean(false)); // ruta indirecta, a traves de un gateway valido
		}
        catch(Exception e)
		{
        	if(e.getMessage().length()==0)
         	   throw new IllegalArgumentException("Entrada no valida para la tabla de rutas");
        	throw new IllegalArgumentException("Entrada no valida para la tabla de rutas ("+e.getMessage()+")");
		}
	}
	
	
    
    /**
     * Elimina la entrada especificada de la tabla de rutas
     * @param posicion Posicion de la entrada que se quiere eliminar
     */    
    public void Eliminar(int posicion)
    {
    	if(posicion>=0 || posicion<destinos.size())
    	{
    	    destinos.remove(posicion);
    	    mascaras.remove(posicion);
    	    gateways.remove(posicion);
    	    interfaces.remove(posicion);
            tipo.remove(posicion);
    	}
    }
    
    
    
    /**
     * Intercambia dos entradas de la tabla de rutas
     * @param posicion1 Posicion de una entrada
     * @param posicion2 Posicion de la otra entrada
     */
    public void Intercambiar(int posicion1, int posicion2)
    {
    	try
		{
    		// 1. Obtenemos primero los interfaces (por si se produce un error)
    	    Interfaz i1=(Interfaz)interfaces.get(posicion1);
    	    Interfaz i2=(Interfaz)interfaces.get(posicion2);
    	
    	    interfaces.setElementAt(i1,posicion2);
    	    interfaces.setElementAt(i2,posicion1);
		}
    	catch(Exception e) {
			equipo.NuevoEvento('X',-1,null,"Error al intercambiar las entradas en la tabla de rutas:" + e.getMessage());

    	}
    }

    
    
    /**
     * Devuelve en numero de la entrada con los datos sobre el siguiente salto
     * al que hay que enviar el datagrama dirigido a la IP indicada
     * @param ip Direccion IP del destino final
     * @return Numero de entrada en la tabla de rutas, o -1 si no se encuentra
     *         (-1 indicaria que no hay ruta)
     */
    public int SiguienteSalto(DireccionIPv4 ip)
    {
    	int numEntrada=-1;
    	int numBits=0;
    	
    	// 0. Comprobaciones previas
    	if(ip==null)
    	   return(numEntrada);
    		
        // 1. Buscamos en tabla una entrada que coincida completamente con la IP dada
    	for(int i=0;numEntrada==-1 && i<mascaras.size();i++)
    	{
    		if(ip.equals(destinos.get(i)))
    			numEntrada=i;
    	}

    	// 2. Si no hemos encontrado ruta, buscamos si hay alguna para la subred de la IP dada
    	for(int i=0;i<mascaras.size();i++)
    	{
		    MascaraIPv4 mascara=(MascaraIPv4)mascaras.get(i);
    		
            // 2.1 Aplicamos la mascara de subred a la direccion IPv4 de la entrada
    		DireccionIPv4 ipAux=(DireccionIPv4)destinos.get(i);
    		DireccionIPv4 red1=ipAux.getIPdeRed(mascara);
    		
    		// 2.2 Aplicamos la mascara de subred a la direccion IPv4 del destino
    		DireccionIPv4 red2=ip.getIPdeRed(mascara);
    		
    		// 2.3 Comparamos las dos direcciones de red
    		if(red1.equals(red2))
    		{
    		   if(mascara.NumBits()>numBits)
    		   {
    		   	   numBits=mascara.NumBits();
    		   	   numEntrada=i;
    		   }
    		}    		
    	}
    	
    	// 3. Si no encontramos ruta, buscamos si hay alguna para la red de la IP dada
    	// Este caso es el mismo que el anterior, lo que hay que hacer en el paso 2 es
    	// buscar la ruta que afine mas hacia la IP que se da
    	
    	
    	// 4. Si no se ha encontrado una entrada, usamos la ruta por defecto
    	if(numEntrada==-1)
    		numEntrada=RutaPorDefecto();
    	
    	// 5. Devolvemos el numero de entrada en la tabla, que se tendra que usar
    	//if(numEntrada>=0)
    	//    System.out.println("ruta para "+ip.getIP()+": "+((DireccionIPv4)gateways.get(numEntrada)).getIP()+" ["+numEntrada+"]");
    	
    	return(numEntrada);
    }
    
    
    
    /**
     * Devuelve el numero de entrada de la ruta por defecto en la tabla de rutas
     * @return Numero de entrada de la ruta por defecto
     */
    private int RutaPorDefecto()
	{
    	int numEntrada=-1;
    	DireccionIPv4 destino;
    	
    	for(int i=0;i<destinos.size() && numEntrada==-1;i++)
    	{
    		destino=(DireccionIPv4)destinos.get(i);
    		if(destino.getIP().equals("0.0.0.0"))
    			numEntrada=i;
    	}
    	
    	return(numEntrada);
    }
    
    
    
    /**
     * Devuelve el tipo de ruta
     * @param numEntrada Numero de entrada en la tabla de rutas
     * @return Cierto, si la entrada indicada de la tabla de rutas es una ruta directa 
     */
    public boolean EsDirecta(int numEntrada)
	{
    	if(numEntrada<0 || numEntrada>destinos.size())
    		throw new IllegalArgumentException("Numero de entrada no valida");
    	
    	return(((Boolean)tipo.get(numEntrada)).booleanValue());
    }
    
    
    
    /**
     * Devuelve la direccion IPv4 del gateway de la entrada especicada de la tabla
     * @param numEntrada Numero de entrada en la tabla
     * @return Direccion IPv4 del gateway
     */
    public DireccionIPv4 getGateway(int numEntrada)
	{
    	if(numEntrada<0 || numEntrada>destinos.size())
    		throw new IllegalArgumentException("Nemero de entrada no valida");
    	
    	return((DireccionIPv4)gateways.get(numEntrada));   	
    }
    
    
    
    /**
     * Devuelve la interfaz de la entrada especicada de la tabla
     * @param numEntrada Numero de entrada en la tabla
     * @return Interfaz
     */
    public Interfaz getInterfaz(int numEntrada)
	{
    	if(numEntrada<0 || numEntrada>destinos.size())
    		throw new IllegalArgumentException("Nemero de entrada no valida");
    	
    	return((Interfaz)interfaces.get(numEntrada));   	
    }
}
