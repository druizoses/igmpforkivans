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
 * Creado el 19-dic-2003
 */
 
package Redes.IPv4.ARP;

import Redes.*;
import Equipos.*;
import Proyecto.*;

/**
 * Módulo ARP
 */
public class ModuloARP extends Nivel 
{	
	/**
	 * Cache ARP, facilmente accesible por otros modulos como el IPv4
	 */
	public CacheARP cacheARP;



	/**
     * Constructor
     * @param equipo Equipo poseedor de este modulo
     */
	public ModuloARP(Equipo equipo)
	{
		super(equipo);
		
		// Este modulo entiende los identificadores Ethernet para ARP
		// (o lo que es lo mismo: puede tener un nivel ethernet por debajo)
		LocalizadorRedes.Registrar("arp","ethernet",0x0806);
		LocalizadorRedes.Registrar("arp","puntoapunto",0xFFFA);
		LocalizadorRedes.Registrar("arp","anillo",0xFFFB);
		
		// Y el identificador de modulo ARP
		LocalizadorRedes.Registrar("arp","arp",0);
		
		cacheARP=new CacheARP();
		
        // Retardo asociado al procesamiento realizado por el modulo ARP
        parametros.setValor("Retardo",new Integer(1));
	}
	
	
	
	/**
	 * Procesa las respuestas ARP que llegan
	 * @param instante Instante de tiempo
	 */
	public void Procesar(int instante)
	{
		// 1. Procesamos las entradas
	    ProcesarEntradas(instante);
	    
	    // 2. Procesamos salida
	    ProcesarSalidas(instante);
	}

	
	
	/**
	 * Devuelve el numero de paquetes ARP pendientes de ser procesados
	 * @return Numero de paquetes ARP pendientes de ser procesados
	 */
	public int Pendientes()
	{
	    return(colaEntrada.size()+colaSalida.size());	    
	}
	
	
	
	/**
	 * Procesa las respuestas ARP que llegan. Este metodo se define por seguir
	 * la estructura de metodos de los demas niveles (un metodo para procesar salidas,
	 * y otro metodo para procesar las entradas)
	 * @param instante Instante de tiempo
	 */
	private void ProcesarEntradas(int instante)
	{
		Dato dato=null;
    	
		// Comprobamos la cola de entrada...
		for(int i=0;i<colaEntrada.size();i++)
		{
			dato=(Dato)colaEntrada.get(i);
			
			// ... y si hay algun paquete ARP programado para ser procesado ahora... 
			if(dato.instante==instante)
			{
				try
				{			
					// 1. Eliminamos el dato de la cola de entrada
                    colaEntrada.remove(i);
                    i--;
                    
                    // 2. Convertimos el paquete (buffer) a paquete ARP
				    PaqueteARP p=new PaqueteARP(dato.paquete);
				    
				    // 3. Actuamos en consecuencia
                    if(p.getOperationCode()==PaqueteARP.kOP_PETICION_ARP)
                    {	
                        if(!SimularError(ErroresARP.flags[2]))
                        {    
                           equipo.NuevoEvento('R',instante,p,"Peticion ARP");
                    	   ProcesaPeticionARP(instante,p,dato.interfaz);
                        }
                    }
                    else if(p.getOperationCode()==PaqueteARP.kOP_RESPUESTA_ARP)
                    {	
                        if(!SimularError(ErroresARP.flags[3]))
                        {    
                           equipo.NuevoEvento('R',instante,p,"Respuesta ARP");
                    	   ProcesaRespuestaARP(p);
                        }
                    }
				}
				catch(Exception e)
				{
				    // posibles errores si el paquete recibido no era correcto,
				    // por tanto lo ignoramos y continuamos
					
				    //Objeto.DEBUG(equipo.getNombre()+": mala conversion ARP");
				}
			}
		}
	}
	
	
	
	/**
	 * Procesa una peticion ARP
	 * @param instante Instante de tiempo
	 * @param peticion Peticion ARP
	 * @param interfaz Interfaz por la que llego la peticion ARP
	 */
	private void ProcesaPeticionARP(int instante,PaqueteARP peticion,Interfaz interfaz)
	{
		Direccion dirProtocolo;
		
		// 0. Comprobamos que la peticion haya entrado por la interfaz
		//    con la direccion IP indicada en la peticion, si no es asi
		//    la ignoramos.
		dirProtocolo=peticion.getProtocolAddressTarget();
		if(interfaz.getIP().equals(dirProtocolo))
		{
			Objeto.DEBUG(equipo.getNombre()+": Enviando respuesta ARP");
			
		   	// 1. Enviamos una respuestaARP con la direccion fisica pedida
			RespuestaARP respuesta=new RespuestaARP(peticion,interfaz.getDirFisica());
			Dato dato=new Dato(instante,respuesta);
			dato.direccion=peticion.getHWAddressSender();
			dato.instante+=getRetardo();
			dato.protocolo=getID(interfaz.getNivelEnlace().ID());
			dato.interfaz=interfaz;
			
			// 2. Apuntamos el evento
			equipo.NuevoEvento('E',dato.instante,dato.paquete,"Respuesta ARP");
			
			// 3. Enviamos la respuesta
			if(!SimularError(ErroresARP.flags[1]))
			    interfaz.getNivelEnlace().ProgramarSalida(dato);
			
			// 4. Almacenamos los datos en la cache ARP, previa comprobacion
			int protocolo=peticion.getProtocolAddressSpace();
			Direccion direccion=peticion.getProtocolAddressSender();
			if(cacheARP.Existe(protocolo,direccion))
				cacheARP.Actualiza(protocolo,direccion,peticion.getHWAddressSender());
			else
				cacheARP.NuevaEntrada(protocolo,direccion,peticion.getHWAddressSender());
		}
	}
	
	
	
	/**
	 * Insertamos la informacion de la respuesta ARP en la cache ARP
	 * @param respuesta Respuesta ARP
	 */
	private void ProcesaRespuestaARP(PaqueteARP respuesta)
	{	
		// 1. Averiguamos el protocolo de la direccion de red
		int protocolo=respuesta.getProtocolAddressSpace();
		
		// 2. Obtenemos la direccion de nivel de red
		Direccion dirProtocolo=respuesta.getProtocolAddressSender();
		
		// 3. Obtenemos la direccion de nivel fisico
		Direccion dirFisica=respuesta.getHWAddressSender();	
	
	    // 4. Almacenamos los datos en la cache ARP, previa comprobacion
		if(cacheARP.Existe(protocolo,dirProtocolo))
			cacheARP.Actualiza(protocolo,dirProtocolo,dirFisica);
		else
	 	    cacheARP.NuevaEntrada(protocolo,dirProtocolo,dirFisica);
	}
	
	
	
	/**
	 * Procesa una peticion ARP de salida
	 * @param instante Intente de tiempo
	 */
	private void ProcesarSalidas(int instante)
	{
		for(int i=0;i<colaSalida.size();i++)
		{
			Dato dato=(Dato)colaSalida.get(i);
			
			if(dato.instante==instante)
			{
			    // 1. Eliminamos el dato de la cola de salida
				colaSalida.remove(i);
				i--;
				
				if(dato.paquete instanceof PeticionARP)
				{
				    if(!SimularError(ErroresARP.flags[0]))
				    {    
				       // 2. Añadimos el retardo por el procesamiento del modulo ARP
			           dato.instante+=getRetardo();
			        
			           // 3. Apuntamos el evento
			           equipo.NuevoEvento('E',dato.instante,dato.paquete,"Peticion ARP");
			    
			           // 4. Enviamos la peticion
			           dato.direccion=null; //broadcast
			           dato.protocolo=getID(dato.interfaz.getNivelEnlace().ID());
			           dato.interfaz.getNivelEnlace().ProgramarSalida(dato);
				    }
				}
				else
				{
				    // si se ha programado algo que no es una peticion ARP,
				    // lo ignoramos
				}
			}
		}
	}

	
	
	/**
	 * Devuelve el identificador del nivel
	 * @return Identificador del nivel
	 */
	public String ID()
	{
		return("arp");
	}
	
	
	
	/**
	 * Comprueba que todos los datos necesarios para que el modulo ARP funcione
	 * correctamente estan especificados (para salidas). Estos datos son:
	 *    paquete: Un PaqueteARP, PeticionARP o RespuestaARP
	 *    instante: Un entero mayor o igual que 0
	 *    interfaz: Interfaz por la que se enviara el paquete ARP
	 * @param dato Dato
	 * @return Cierto si el dato es correcto
	 */
	public boolean ComprobarSalida(Dato dato)
	{
	    boolean correcto=false;
	    
	    if(dato!=null && dato.paquete instanceof PaqueteARP)
	    {
	        if(dato.instante>=0 && dato.interfaz!=null && equipo!=null)
	        {    
	            for(int i=0;!correcto && i<equipo.NumInterfaces();i++)
	            {
	                if(equipo.getInterfaz(i)==dato.interfaz)
	                    correcto=true;
	            }
	        }
	    }

	    return(correcto);
	}
	
	
	
	/**
	 * Comprueba que todos los datos necesarios para que el modulo ARP funcione
	 * correctamente estan especificados (para entradas). Estos datos son:
	 *    paquete: Un PaqueteARP, PeticionARP o RespuestaARP
	 *    instante: Un entero mayor o igual que 0
	 * @param dato Dato
	 * @return Cierto si el dato es correcto
	 */	
	public boolean ComprobarEntrada(Dato dato)
	{
	    boolean correcto=false;
	    
	    if(dato!=null && dato.paquete!=null && dato.instante>=0)
           correcto=true;
	    return(correcto);	    
	}

}
