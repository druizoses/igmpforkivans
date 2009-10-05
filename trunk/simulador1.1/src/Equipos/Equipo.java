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

import Proyecto.*;
import Redes.*;
import Redes.IPv4.*;

/**
 * Clase base para los distintos equipos
 */
public abstract class Equipo extends Objeto
{
    /**
     * Constante asociada a la simulacion de errores en IP
     */
    public static final int kIPv4  = 1;
    
    /**
     * Constante asociada a la simulacion de errores en ARP
     */
    public static final int kARP   = 2;
    
    /**
     * Constante asociada a la simulacion de errores en ICMP
     */
    public static final int kICMP  = 3;
    
	/**
	 * Tabla de rutas
	 */
	public TablaDeRutas tablaDeRutas;
	

	
	/**
     * Constructor
     */
    public Equipo()
    {
        super();
        tablaDeRutas=null; // se inicializa con la del nivel IP!!!
    } 

    
    
	/**
	 * Añade una interfaz al equipo
	 * @param interfaz Interfaz
	 * @throws IllegalArgumentException
	 */
	public void setInterfaz(Interfaz interfaz)
	{
	    // 0. Comprobamos que la interfaz este conectada a una red
	    if(interfaz==null)
	        throw new IllegalArgumentException("La intefaz no puede ser nula");
	    
	    Red red=interfaz.getRed();
	    if(red==null)
	        throw new IllegalArgumentException("La interfaz especificada no esta conectada a ninguna red");
	    
	    // 1. Creamos el nivel de enlace
	    interfaz.CreaNivelEnlace(this);

	    // 2. Añadimos la interfaz a la lista
	    Interfaces.add(interfaz);
	    
	    // 3. Comprobamos que la interfaz se puede conectar a la red
	    String claseInterfaz=(String)red.Caracteristicas().getValor("clase_interfaz");
	    if(claseInterfaz.equals(interfaz.getClass().getName()))
	    {
	        DEBUG("Conectando "+getNombre()+" con "+red.getNombre()+" mediante la interfaz "+interfaz.getNombre()+" ("+interfaz.getIP().getIP()+")");
	        
	        red.ConectarA(this,interfaz.getNombre());
	    }
	    else
	    {
	        Interfaces.remove(interfaz);
	        throw new IllegalArgumentException("La red no es de del mismo tipo que la interfaz");
	    }
	}
	
	public void procesarRedInterna(){
		for(int i=0;i<NumInterfaces();i++)
			;
	}
	
	

	/**
     * Programa un paquete para que se procese como entrada en un determinado instante
     * @param dato Datos del paquete, instante, ...
     */
    abstract public void ProgramarEntrada(Dato dato); 

    
    
	/**
	 * Programa un paquete para que se procese como salida en un determinado instante
	 * @param dato Datos del paquete, instante, ...
	 */
	abstract public void ProgramarSalida(Dato dato); 
    
	

	/**
	 * Controla la simulacion de errores en los distintos niveles de la pila
	 * @param nivel Nivel donde se simulara el error
	 * @param flag Flag asociado al error
	 * @param activar Flag que activa/desactiva la simulacion
	 * @return Falso si se ha producido algun error en el proceso
	 */
	abstract public boolean SimularError(int nivel, String flag, boolean activar);
	
	
	/**
	 * Controla el comportamiento de los distindos modulos que forman la pila de
	 * comunicaciones
	 * @param nivel Nivel del parametro que se quiere cambiar
	 * @param parametro Nombre del parametro
	 * @param valor Valor
	 */
	abstract public void ConfiguraPila(int nivel, String parametro, Object valor);
}
