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

/*
 * Creado el 02-mar-2004
 */
package Redes.Ethernet;

import Proyecto.ListaParametros;
import Proyecto.Parametro;
import Redes.*;
import Equipos.Equipo;

/**
 * Puente Ethernet (componente simulable)
 */
public class PuenteEthernet extends Ethernet 
{
	private static ListaParametros caracteristicas;
	
	static int numPuente;
	
	
	
	/**
	 * Inicializador de la clase
	 */
    static
	{
    	caracteristicas=new ListaParametros();
    	caracteristicas.add(new Parametro("nombre","Nombre generico","java.lang.String"));
    	caracteristicas.add(new Parametro("dibujo","Tipo de dibujo en el esquema","java.lang.String"));
    	caracteristicas.add(new Parametro("se conecta a redes","¿La red se puede conectar a otras redes directamente?","java.lang.Boolean"));
    	caracteristicas.add(new Parametro("se conecta a equipos","¿El dispositivo se puede conectar a equipos directamente?","java.lang.Boolean"));
    	caracteristicas.setValor("se conecta a equipos",new Boolean(false));
    	caracteristicas.setValor("nombre",new String("Puente Ethernet"));
        caracteristicas.setValor("dibujo",new String("equipo"));
        caracteristicas.setValor("se conecta a redes",new Boolean(true));
	    
        caracteristicas.add(new Parametro("clase_trama","Clase que define las tramas","java.lang.String"));
        caracteristicas.setValor("clase_trama","Redes.Ethernet.TramaEthernet");
        caracteristicas.add(new Parametro("clase_direccion","Clase que define las direcciones","java.lang.String"));
        caracteristicas.setValor("clase_direccion","Redes.Ethernet.DireccionEthernet");
        caracteristicas.add(new Parametro("clase_interfaz","Clase que define las interfaces","java.lang.String"));
        caracteristicas.setValor("clase_interfaz","Redes.Ethernet.InterfazEthernet");
        caracteristicas.add(new Parametro("clase_nivel","Clase que define el nivel de enlace","java.lang.String"));
        caracteristicas.setValor("clase_nivel","Redes.Ethernet.NivelEthernet");
	}	

    
    
    /**
	 * Constructor
	 */
    public PuenteEthernet()
	{
    	numPuente++;
    }
    
    
    
    /**
     * Procesa las tramas programadas para el instante especificado
     * @param instante Instante de tiempo
     */
    public void Procesar(int instante)
	{
		Dato dato=null;
		
        // 1. Buscamos las tramas para el instante indicado
		for(int i=0;i<colaTramas.size();i++)
		{
			dato=(Dato)colaTramas.get(i);
			if(dato.instante==instante)
			{				
			    // 1.1 Eliminamos el dato de la cola de tramas de la red
			    colaTramas.remove(i);
			    i--;
			    
			    // 1.2 Evento
			    //NuevoEvento('T',dato.instante+kRETARDO,dato.paquete,"Datos circulando por la red");
			    NuevoEvento('T',instante,dato.paquete,"Datos circulando por la red");
			    // 1.3 Enviamos la trama a las demas redes
			    if(dato.paquete instanceof TramaEthernet)
			    {
			        TramaEthernet trama=(TramaEthernet)dato.paquete;
			        			        
			        for(int numRed=0;numRed<ListaRedes.size();numRed++)
			        {
			            Red dispositivo=(Red)ListaRedes.get(numRed);
			            
			            if(dato.red!=dispositivo)
			            {    
			                Dato datoAux2=new Dato(dato);
			                TramaEthernet t=new TramaEthernet(trama);
			                datoAux2.paquete=t;
			                datoAux2.interfaz=null; //no usan interfaces
			                datoAux2.instante=datoAux2.instante+kRETARDO;
			                datoAux2.red=this;
			                //DEBUG(getNombre()+": reenviando trama a "+dispositivo.getNombre());
			                NuevoEvento('T',instante,datoAux2.paquete,getNombre()+": reenviando trama a "+dispositivo.getNombre());
			          
			                dispositivo.Retransmitir(datoAux2);
			            }
			        }
                }
			    else
			    {
			        //las tramas no ethernet son ignoradas
			    }
			}
		}    	
    }
    
    
    
    /**
     * Conecta un equipo a un puente, lanza una excepcion, ya que no esta permitido
     * @param equipo Equipo
     * @param nombreInterfaz Nombre de la interfaz del equipo
     * @throws IllegalArgumentException
     */
    public void ConectarA(Equipo equipo, String nombreInterfaz) throws IllegalArgumentException
	{
    	throw new IllegalArgumentException("No se puede conectar un puente a un equipo directamente");
    }
    

    
    /**
     * Metodo que permite conectar este tipo de red a otras redes.
     * (No comprobamos el tipo de red que se pasa, por tanto se puede conectar
     *  un hub Ethernet a una token ring sin problemas, lo que pasa es que la
     *  red token ring ignorara las tramas que no sean token ring...)
     * @param red Red a la que se quiere conectar el puente
     */
    public void Conectar(Red red)
    {
        if(red!=null && red!=this)
        {    
           ListaRedes.add(red);
           red.ConectarA(this);
        }
    }    
    

    
    /**
     * Devuelve la lista de caracteristicas
     * @return Lista de caracteristicas
     */
    public ListaParametros Caracteristicas()
	{
    	return(caracteristicas);
    }
}
