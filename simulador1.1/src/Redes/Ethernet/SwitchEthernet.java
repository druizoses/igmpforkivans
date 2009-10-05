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
 * Creado el 28-ene-2004
 */
 
package Redes.Ethernet;

import Equipos.Equipo;
import Redes.*;
//import java.util.Hashtable;
import java.util.Vector;
import Proyecto.*;

/**
 * Switch Ethernet (componente simulable)
 */
public class SwitchEthernet extends Ethernet
{
	private static ListaParametros caracteristicas;
	
    /**
     * Tabla que asocia direcciones a 'puertos' (destinos de envio)
     * DireccionEthernet -> Equipo o Red
     */
	Puertos puertos;
    
    
	/**
	 * Inicializador de la clase
	 */
    static
	{
    	caracteristicas=new ListaParametros();
    	caracteristicas.add(new Parametro("dibujo","Tipo de dibujo en el esquema","java.lang.String"));
    	caracteristicas.add(new Parametro("se conecta a redes","¿La red se puede conectar a otras redes directamente?","java.lang.Boolean"));
    	caracteristicas.add(new Parametro("nombre","Nombre generico","java.lang.String"));
    	caracteristicas.add(new Parametro("se conecta a equipos","¿El dispositivo se puede conectar a equipos directamente?","java.lang.Boolean"));
    	caracteristicas.setValor("nombre",new String("Switch Ethernet"));
        caracteristicas.setValor("dibujo",new String("equipo"));
        caracteristicas.setValor("se conecta a redes",new Boolean(true));
        caracteristicas.setValor("se conecta a equipos",new Boolean(true));
        
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
    public SwitchEthernet()
    {
        super();
        
        puertos=new Puertos();
    }
    
    
    
    /**
     * Procesa las tramas programadas para un instante dado
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
                
                // 1.2 El i-esimo elemento de la lista ahora es otro, asi que hay que
                //     volver a procesarlo (i-- que se compensa con el i++ del for)
                i--;
                
                // 1.3 Evento
                //NuevoEvento('T',dato.instante+kRETARDO,dato.paquete,"Datos circulando por la red");
                
                // 1.4 Enviamos la trama al equipo que tenga la direccion indicada
                if(dato.paquete instanceof TramaEthernet && dato.direccion instanceof DireccionEthernet)
                {
                    TramaEthernet trama=(TramaEthernet)dato.paquete;
                    DireccionEthernet direccion=(DireccionEthernet)dato.direccion;
                    Objeto dispositivo=puertos.get(direccion);
                    
                    // 1.4.1 Si el destino es la direccion de broadcast, o no conocemos a que puerto
                    //       esta conectado, lo enviamos por todos los puertos
                    if(dispositivo==null || direccion.EsBroadcast())
                    {    
                        // Enviamos a todos los equipos conectados
                        for(int numEquipo=0;numEquipo<ListaEquipos.size();numEquipo++)
                        {
                            Equipo equipo=(Equipo)ListaEquipos.get(numEquipo);
                            Dato d=new Dato(dato);
                            TramaEthernet t=new TramaEthernet(trama);
                            d.paquete=t;
                            d.interfaz=getInterfaz(numEquipo);
                            d.instante=d.instante+kRETARDO;
                            DEBUG(getNombre()+": enviando trama a "+equipo.getNombre());
                        
                            NuevoEvento('T',d.instante,d.paquete,"Enviando trama a "+equipo.getNombre());

                            // no enviamos la trama al equipo que la generó
                            /*for(int numInterface=0;numInterface<equipo.NumInterfaces();numInterface++)
                            	if((Ethernet)equipo.getInterfaz(numInterface).getRed() == this)
                            		equipo.ProgramarEntrada(d, equipo.getInterfaz(numInterface));*/
                            if(d.interfaz!=dato.interfaz)
                                equipo.ProgramarEntrada(d);
                        }
                        
                        // Retransmitimos a todas las redes conectadas
                        for(int numRed=0;numRed<ListaRedes.size();numRed++)
                        {
                            Red red=(Red)ListaRedes.get(numRed);
                            if(red!=dato.red)
                            {
                               Dato d=new Dato(dato);
                               TramaEthernet t=new TramaEthernet(trama);
                               d.paquete=t;
                               d.interfaz=null; //getInterfaz(numRed);
                               d.instante=d.instante+kRETARDO;
                               DEBUG(getNombre()+": enviando trama a "+red.getNombre()); 
                               
                               NuevoEvento('T',d.instante,d.paquete,"Reenviando trama a "+red.getNombre());
                               
                               red.Retransmitir(d);
                            }
                        }
                    }
                    
                    // 1.4.2 La direccion es la de un equipo concreto, comprobamos a quien enviar
                    else
                    {
                        if(dispositivo instanceof Equipo)
                        {
                            Equipo equipo=(Equipo)dispositivo;
                            Dato d=new Dato(dato);
                            TramaEthernet t=new TramaEthernet(trama);
                            d.paquete=t;
                            d.instante=d.instante+kRETARDO;
                            DEBUG(getNombre()+": enviando trama a "+dispositivo.getNombre());
                            d.interfaz=getInterfaz(ListaEquipos.indexOf(equipo));
                            
                            NuevoEvento('T',d.instante,d.paquete,"Enviando trama a "+equipo.getNombre());
                            
                            equipo.ProgramarEntrada(d);
                        }
                        else if(dispositivo instanceof Red)
                        {
                            Red red=(Red)dispositivo;
                            if(red!=dato.red)
                            {
                                Dato d=new Dato(dato);
                                TramaEthernet t=new TramaEthernet(trama);
                                d.paquete=t;
                                d.instante=d.instante+kRETARDO;
                                dato.interfaz=null;
                                DEBUG(getNombre()+": enviando trama a "+dispositivo.getNombre());
                                
                                NuevoEvento('T',d.instante,d.paquete,"Reenviando trama a "+red.getNombre());
                                
                                red.Retransmitir(d);
                            }
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
	 * Envia una trama a la red, y se anota su MAC (metodo a usar por los Equipos) 
	 * @param dato Datos de la trama 
	 */
	public void Enviar(Dato dato)
	{
	    if(dato!=null && dato.interfaz!=null)
	    {    
	        dato.red=this;
		    colaTramas.add(dato);
		    
		    // 'Anotamos la MAC' de la trama que llega y le asociamos el equipo emisor
		    // esto equivaldria a que el switch detecta que le llegan tramas con una
		    // determinada direccion MAC por un puerto y despues envia todas las tramas
		    // que vayan a dicha MAC por el puerto que tiene anotado
		    
		    try
			{
		        TramaEthernet trama=(TramaEthernet)dato.paquete;	  
		        DireccionEthernet origen=trama.getOrigen();
		        
			    for(int i=0;i<ListaEquipos.size();i++)
			    {
			    	Direccion direccion=(DireccionEthernet)(getInterfaz(i).getDirFisica());
			        if(direccion.equals(origen))
			        {
			    	    Equipo equipo=(Equipo)ListaEquipos.get(i);
			    	    puertos.put(origen,equipo);
			    	    
			    	    //System.out.println("Memorizando MAC: "+origen.toString()+" con "+equipo.getNombre());
			        }
			    }
			}
		    catch(Exception e)
			{
		    	System.out.println("Error al hacer la gestion de MACs/puertos en el switch ethernet");
		    	e.printStackTrace();
			}
	    }
	    else
	    	System.out.println("Error, interfaz no especificada o dato nulo");
	}
    
	
	
	/**
	 * Envia una trama a la red (se usa cuando se reenvia desde otra red)
	 * @param dato Datos de la trama 
	 */
	public void Retransmitir(Dato dato)
	{
	    if(dato!=null)
	    {    
		    colaTramas.add(dato);
		    
		    // 'Anotamos la MAC'
		    for(int i=0;i<ListaEquipos.size();i++)
		    {
		    	Direccion direccion=(DireccionEthernet)(getInterfaz(i).getDirFisica());
		        if(direccion.equals(dato.direccion))
		        {
		    	    Red red=(Red)ListaRedes.get(i);
		    	    puertos.put(dato.direccion,red);
		        }
		    }
	    }
	}
}



class Puertos
{
    /**
     * Clave
     */
	Vector key;
	
	/**
	 * Valor
	 */
	Vector value;
	

	/**
	 * Constructor
	 */
	public Puertos()
	{
		key=new Vector();
		value=new Vector();
	}
	
	
	
	/**
	 * Devuelve el Objeto asociado a la direccion especificada
	 * @param direccion Direccion
	 * @return Objeto asociado a la direccion
	 */
	public Objeto get(Direccion direccion)
	{
		if(direccion!=null)
		{
			String dir=direccion.toString();
		    for(int i=0;i<key.size();i++)
		    {
			   String dirAux=((Direccion)key.get(i)).toString();
			   if(dir.equals(dirAux))
			   	   return((Objeto)value.get(i));
		    }   	
		}
		
		return(null);
	}
	
	
	
	
	/**
	 * Inserta un elemento en el vector, sin repetir
	 * @param direccion Clave
	 * @param valor Valor asociado a la clave
	 */
	public void put(Direccion direccion, Objeto valor)
	{
		int posicion=-1;
		
        // 1. Buscamos en la tabla por si el elemento ya estaba
		if(direccion!=null)
		{
			String dir=direccion.toString();
		    for(int i=0;posicion==-1 && i<key.size();i++)
		    {
			   String dirAux=((Direccion)key.get(i)).toString();
			   if(dir.equals(dirAux))
			   	   posicion=i;
		    }   	
		}
		
		// 2. Insertamos el valor o cambiamos el que haya
		if(posicion==-1)
		{
			key.add(direccion);
			value.add(valor);
		}
		else
		{
			value.set(posicion,valor);
		}
	}
}
