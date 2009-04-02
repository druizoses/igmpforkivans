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

import Redes.Direccion;
import Redes.IPv4.*;
import Equipos.Equipo;
import Proyecto.ListaParametros;
import Proyecto.Parametro;

/**
 * Clase base para los distintos tipos de interfaces
 */
public abstract class Interfaz 
{     
    /**
     * Red a la que esta conectado el interfaz
     */
    protected Red red;

    /**
     * Nivel de enlace que usa este interfaz
     * (inicializado por las clases derivadas)
     */
    protected Nivel nivelEnlace;

    /**
     * Lista de parametros
     */
    protected ListaParametros parametros;
    
    
    
    
    /**
     * Contructor
     * @param nombre Nombre del interfaz
     * @param ip Direccion IPv4
     * @param mascara Mascara de la direccion IPv4
     * @throws IllegalArgumentException si alguno de los parametros tiene valor incorrecto
     */
    public Interfaz(String nombre,String ip, String mascara) throws IllegalArgumentException
    {
        // 0. Inicializacion 
        parametros=new ListaParametros();
        parametros.add(new Parametro("nombre","Nombre","java.lang.String"));
        parametros.add(new Parametro("ip","Dirección IP","Redes.IPv4.DireccionIPv4"));
        parametros.add(new Parametro("mascara","Máscara IP","Redes.IPv4.MascaraIPv4"));
        
    	// 1. Nombre del interfaz
        if(nombre==null)
            throw new IllegalArgumentException("El nombre de la interfaz no puede ser nulo");
    	parametros.setValor("nombre",nombre);
    	
		// 2. Datos referentes al nivel de Red
    	parametros.setValor("ip",new DireccionIPv4(ip));
    	parametros.setValor("mascara",new MascaraIPv4(mascara));
    }
    
    
    
    /**
     * Asocia la interfaz con una red comprobando que son de tipos compatibles
     * @param pred Red conectada al interfaz
     */
    public void Conectar(Red pred)
    {
    	if(pred.getClass().getName().startsWith("Redes."+getClaseRed()+"."))
    	   this.red=pred;
    }
    
    
    
    /**
     * Devuelve la red a la que esta conectada la interfaz
     * @return Red a la que esta conectada la interfaz
     */
    public Red getRed()
    {
        return(red);
    }
    
    
    
    /**
     * Devuelve el nombre de la interfaz
     * @return Nombre de la interfaz
     */
    public String getNombre()
    {
    	return((String)parametros.getValor("nombre"));
    }
    
   
    
    /**
     * Devuelve el nivel de enlace asociado a la interfaz
     * @return Nivel de enlace
     */
    public Nivel getNivelEnlace()
    {
    	return(nivelEnlace);
    }
    
    
    
    /**
     * Devuelve la direccion ip asociada a la interfaz
     * @return Direccion IP
     */
    public DireccionIPv4 getIP()
    {
        return((DireccionIPv4)parametros.getValor("ip"));
    }
    
    

    /**
     * Devuelve la mascara asociada a la interfaz
     * @return Mascara IP
     */
    public MascaraIPv4 getMascara()
    {
        return((MascaraIPv4)parametros.getValor("mascara"));
    }
     
   
    
    /**
     * Devuelve la direccion de nivel fisico (enlace) de la interfaz
     * @return Direccion fisica de la interfaz
     */
    public Direccion getDirFisica()
	{
	    return((Direccion)parametros.getValor("dirfisica"));
	}    
    
    
    
    /**
     * Devuelve la clase de red a la que se puede conectar el Interfaz
     * @return Nombre de la clase de Red
     */
    public String getClaseRed()
    {
        return((String)parametros.getValor("clasered"));
    }
    
    
    
    /**
     * Cuando un equipo conecta el interfaz a la red, le asigna una red, y
     * le crea un nivel de enlace asociado.
     * @param equipo Equipo al que pertenece la interfaz
     */
    public abstract void CreaNivelEnlace(Equipo equipo);
}
