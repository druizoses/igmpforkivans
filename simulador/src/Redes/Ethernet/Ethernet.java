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

package Redes.Ethernet;

import Redes.*;
import Equipos.Equipo;
import Proyecto.ListaParametros;
import Proyecto.Parametro;

/**
 * Red Ethernet (componente simulable)
 */
public class Ethernet extends Red
{
	/**
	 * Tipo Ethernet
	 */
	public static final int kTIPO=0xFFFF;
	
	/**
	 * Retardo de la red, debe ser mayor que lo que tarde un puente en procesar
	 * una trama
	 */
	public static final int kRETARDO=1;
	
	/**
	 * Lista de caracteristicas genericas
	 */
	private static ListaParametros caracteristicas;
    
    
	/**
	 * Inicializador de la clase
	 */
    static
	{
    	caracteristicas=new ListaParametros();
    	caracteristicas.add(new Parametro("nombre","Nombre generico","java.lang.String"));
    	caracteristicas.add(new Parametro("dibujo","Tipo de dibujo en el esquema","java.lang.String"));
    	caracteristicas.add(new Parametro("se conecta a redes","¿El dispositivo se puede conectar a otras redes directamente?","java.lang.Boolean"));
    	caracteristicas.add(new Parametro("se conecta a equipos","¿El dispositivo se puede conectar a equipos directamente?","java.lang.Boolean"));
    	caracteristicas.setValor("nombre",new String("Ethernet"));
        caracteristicas.setValor("dibujo",new String("bus"));
        caracteristicas.setValor("se conecta a redes",new Boolean(false));
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
    public Ethernet()
    {
        super("sin nombre",1500);
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
			    NuevoEvento('T',dato.instante+kRETARDO,dato.paquete,"Datos circulando por la red");
			    
			    // 1.4 Enviamos la trama a todos los equipos de la red
			    if(dato.paquete instanceof TramaEthernet)
			    {
			        TramaEthernet trama=(TramaEthernet)dato.paquete;
			        
			        // 1.4.1 Equipos
			        for(int numEquipo=0;numEquipo<ListaEquipos.size();numEquipo++)
                    {
                        Equipo equipo=(Equipo)ListaEquipos.get(numEquipo);
                        Dato datoAux1=new Dato(dato);
                        TramaEthernet t=new TramaEthernet(trama);
                        datoAux1.paquete=t;
                        datoAux1.interfaz=getInterfaz(numEquipo);
                        datoAux1.instante=datoAux1.instante+kRETARDO;
                   	    datoAux1.red=this;
                        DEBUG(getNombre()+": enviando trama a "+equipo.getNombre());
                    
                        // no enviamos la trama al equipo que la generó
                        if(datoAux1.interfaz!=dato.interfaz)
                            equipo.ProgramarEntrada(datoAux1);
			        }
			        
			        // 1.4.2 Redes
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
			                DEBUG(getNombre()+": reenviando trama a "+dispositivo.getNombre());
			          
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
     * Devuelve el numero de tramas pendientes de ser procesadas
     * @return Numero de tramas pendientes
     */
    public int Pendientes()
    {
        // 1. Devolvemos el numero de tramas que hay en la cola de tramas
        return(colaTramas.size());
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
