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
 * Creado el 15-mar-2004
 */
 
package Redes.PuntoAPunto;

import Redes.*;
import Proyecto.*;
import Equipos.Equipo;

/**
 * Red Punto a Punto (componente simulable)
 */
public class PuntoAPunto extends Red
{ 
	private static ListaParametros caracteristicas;
	
	public static int kTIPO=0xFF00;
	
	public static int kRETARDO=1;
	
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
    	caracteristicas.setValor("nombre",new String("Punto a punto"));
        caracteristicas.setValor("dibujo",new String("punto a punto"));
        caracteristicas.setValor("se conecta a redes",new Boolean(false));
        caracteristicas.setValor("se conecta a equipos",new Boolean(true));  // se conecta a dos equipos solo
        
        caracteristicas.add(new Parametro("clase_trama","Clase que define las tramas","java.lang.String"));
        caracteristicas.setValor("clase_trama","Redes.PuntoAPunto.TramaPuntoAPunto");
        caracteristicas.add(new Parametro("clase_direccion","Clase que define las direcciones","java.lang.String"));
        caracteristicas.setValor("clase_direccion","Redes.PuntoAPunto.DireccionPuntoAPunto");
        caracteristicas.add(new Parametro("clase_interfaz","Clase que define las interfaces","java.lang.String"));
        caracteristicas.setValor("clase_interfaz","Redes.PuntoAPunto.InterfazPuntoAPunto");
        caracteristicas.add(new Parametro("clase_nivel","Clase que define el nivel de enlace","java.lang.String"));
        caracteristicas.setValor("clase_nivel","Redes.PuntoAPunto.NivelPuntoAPunto");
	}

    
    
    /**
     * Constructor
     */
    public PuntoAPunto()
    {
        super("sin nombre",8000);
    }
    
    
    
    /**
     * Procesa las tramas programadas para un determinado instante
     * @param instante Instante de tiempo
     */
    public void Procesar(int instante)
    {
    	for(int i=0;i<colaTramas.size();i++)
    	{
    		Dato dato=(Dato)colaTramas.get(i);
    		if(dato.instante==instante)
    		{
    			// 1. Eliminamos el dato de la cola de tramas 
    			colaTramas.remove(i);
    			i--;

    			// 2. Procesamos la trama
    			if(dato.paquete instanceof TramaPuntoAPunto)
    			{
        			// 2.0 Evento
        			NuevoEvento('T',dato.instante+kRETARDO,dato.paquete,"Datos circulando por la red");

    				// 2.1 Comprobamos el destinatario de la trama
    				TramaPuntoAPunto trama=(TramaPuntoAPunto)dato.paquete;
    				DireccionPuntoAPunto dir=trama.getDestino();
    				
    				// 2.2 Buscamos el equipo destino en la lista de direcciones o tratamos si
    				//     es un semi-broadcast
                    Equipo equipo=null;
                    int numEquipo=0;
    				if(ListaEquipos.size()==2)
    				{
    					if(!dir.EsBroadcast())
    					{
    				        for(numEquipo=0;equipo==null && numEquipo<2;numEquipo++)
    				        {
    				    	    DireccionPuntoAPunto dirAux=(DireccionPuntoAPunto)(getInterfaz(numEquipo).getDirFisica());
    				            if(dir.equals(dirAux))
    				            {
    				    	        equipo=(Equipo)ListaEquipos.get(numEquipo);
    				                numEquipo--;
    				            }
    				        }
    					}
    					else
    					{
    				        for(numEquipo=0;equipo==null && numEquipo<2;numEquipo++)
    				        {
    				        	Interfaz interfaz=getInterfaz(numEquipo);
   				                if(interfaz!=dato.interfaz)
   				                {
    				    	        equipo=(Equipo)ListaEquipos.get(numEquipo);
   				                    numEquipo--;
   				                }
   				            }   						
    					}
    				}
    				
    				// 2.3 Si hay un equipo con la direccion de destino, le pasamos la trama
    				if(equipo!=null)
    				{
    					Dato datoAux=new Dato(dato);
    					datoAux.paquete=new TramaPuntoAPunto(trama);
    					datoAux.interfaz=getInterfaz(numEquipo);
    					datoAux.instante+=kRETARDO;
    					datoAux.red=this;
    					
    					equipo.ProgramarEntrada(datoAux);
    				}
    			}
    			else
    			{
        			// Evento de error en la red
        			NuevoEvento('X',dato.instante+kRETARDO,dato.paquete,"Datos erroneos circulando por la red");
    			}
    		}
    	}
    }


    
    /**
     * Devuelve el numero de tramas que quedan por procesar
     * @return Numero de tramas
     */
    public int Pendientes()
    {
        return(colaTramas.size());
    }
    
    
    
    /**
     * Devuelve la lista de caracteristicas genericas de este tipo de red
     * @return Lista de caracteriticas de las redes Punto a Punto
     */
    public ListaParametros Caracteristicas()
	{
    	return(caracteristicas);
    }
    
    
    
    /**
     * Los enlaces punto a punto no soportan ARP
     * @return Falso
     */
    public boolean SoportaARP()
    {
    	return(false);
    }
}

