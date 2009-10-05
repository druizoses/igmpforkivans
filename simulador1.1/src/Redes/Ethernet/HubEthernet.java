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
 * Creado el 06-ene-2004
 */
 
package Redes.Ethernet;

import Redes.*;
import Redes.Ethernet.TramaEthernet;
import Equipos.Equipo;
import Proyecto.ListaParametros;
import Proyecto.Parametro;

/**
 * Hub Ethernet (componente simulable)
 */
public class HubEthernet extends Ethernet
{
	/**
	 * Lista de caracteristicas
	 */
	private static ListaParametros caracteristicas;
	
    /**
     * Retardo de procesamiento
     */
    private static int kRETARDO=1;
   
    /**
     * Numero de Hub (unico para cada hub)
     */
    private static int numHub;
	
    
    
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
   	    caracteristicas.setValor("nombre","Hub Ethernet");
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
    public HubEthernet()
    {
        super();
        
        // 1. Incrementados el numero de hubs ethernet
        numHub++;
        setNombre("Hub Ethernet #"+numHub);
    }    
        
    
    
    /**
     * Procesa la tramas programadas para el instante indicado
     * @param instante Instante de tiempo
     */
    public void Procesar(int instante)
    {
        for(int i=0;i<colaTramas.size();i++)
        {
            Dato dato=(Dato)colaTramas.get(i);
            
            if(dato.instante==instante);
            {
                colaTramas.remove(i);
                i--;
                    
                if(dato.paquete instanceof TramaEthernet)
                {    
                    TramaEthernet trama=(TramaEthernet)dato.paquete;

                    // 1. Enviamos la trama a todas las redes
                    for(int numRed=0;numRed<ListaRedes.size();numRed++)
                    {
                        Red red=(Red)ListaRedes.get(numRed);

                        // No enviamos la trama a la red que nos la envio (¡que tonteria!)
                        if(dato.red!=red)
                        {    
                            Dato datoAux1=new Dato(dato);                        
                            datoAux1.paquete=new TramaEthernet(trama);
                            datoAux1.instante+=kRETARDO;
                            NuevoEvento('T',datoAux1.instante,datoAux1.paquete,"Reenviando trama a "+red.getNombre());

                            red.Retransmitir(datoAux1);
                        }
                    }
                    
                    // 2. Enviamos la trama a los equipos
                    for(int numEquipo=0;numEquipo<ListaEquipos.size();numEquipo++)
                    {
                        Equipo equipo=(Equipo)ListaEquipos.get(numEquipo);
                        Dato datoAux2=new Dato(dato);
                        TramaEthernet t=new TramaEthernet(trama);
                        datoAux2.paquete=t;
                        datoAux2.interfaz=getInterfaz(numEquipo);
                        datoAux2.instante=datoAux2.instante+kRETARDO;
                        
                        //NuevoEvento('T',datoAux2.instante,datoAux2.paquete,"Enviando trama a "+equipo.getNombre());

                        //DEBUG(getNombre()+": enviando trama a "+equipo.getNombre());
                        NuevoEvento('T',instante,datoAux2.paquete,getNombre()+": enviando trama a "+equipo.getNombre());
                        // no enviamos la trama al equipo que la generó
                        /*for(int numInterface=0;numInterface<equipo.NumInterfaces();numInterface++)
                        	if((Ethernet)equipo.getInterfaz(numInterface).getRed() == this)
                        		equipo.ProgramarEntrada(datoAux2, equipo.getInterfaz(numInterface));*/
                        if(datoAux2.interfaz!=dato.interfaz)
                            equipo.ProgramarEntrada(datoAux2);
                    }
                }
                else //trama no ethernet? a saber que se ha 'conectao' (una calabaza? jejeje)
                {
                    System.out.println("¡Por dios! ¿que habeis conectado al jab?");
                }
            }
        }
    }
    
    
    
    /**
     * Metodo que permite conectar este tipo de red a otras redes.
     * (No comprobamos el tipo de red que se pasa, por tanto se puede conectar
     *  un hub Ethernet a una token ring sin problemas, lo que pasa es que la
     *  red token ring ignorara las tramas que no sean token ring...)
     * @param red Red a la que se quiere conectar esta red
     */
    public void Conectar(Red red)
    {
        if(red!=null && red!=this)
        {    
           ListaRedes.add(red);
           red.ConectarA(this);
        }
    }
}
