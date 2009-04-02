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
import Equipos.Equipo;
import Proyecto.Objeto;

/**
 * Nivel de enlace de las redes PuntoAPunto
 */
public class NivelPuntoAPunto extends Nivel 
{
	/**
	 * Red a la que enviaremos los datos y de la que recibiremos
	 */
	Red red;
	
	/**
	 * Direccion de las tramas que atenderemos, el resto las ignoraremos
	 */
	DireccionPuntoAPunto direccion;
	
	
	
	/**
	 * Constructor
	 * @param equipo Equipo propietario de la interfaz que posee este nivel
	 * @param red Red que hay por debajo del nivel
	 * @param direccion Direccion asociada a este nivel
	 * @throws IllegalArgumentException si alguno de los parametros no es valido
	 */
    public NivelPuntoAPunto(Equipo equipo, Red red, DireccionPuntoAPunto direccion)
    {
    	super(equipo);
    	
    	if(red==null || direccion==null)
    	    throw new IllegalArgumentException("No se dispone de los valores necesarios para crear el nivel");
    	
    	this.red=red;
    	this.direccion=direccion;
    }
    
    
    
    /**
     * Procesa los paquetes programados para un instante
     * @param instante Instante de tiempo
     */ 
    public void Procesar(int instante)
    {
    	// 1. Procesamos la cola de salida
    	ProcesarSalida(instante);
    	
    	// 2. Procesamos la cola de entrada
    	ProcesarEntrada(instante);
    }
 
    
    
    /**
     * Devuelve el numero de tramas pendientes de ser procesadas
     * @return Numero de tramas pendientes de ser procesadas
     */
    public int Pendientes()
    {
        return(colaEntrada.size()+colaSalida.size());	    
    }
    
    
    
    /**
     * Envia las tramas de salida que toquen, a la red
     * @param instante Instante de tiempo
     */
    private void ProcesarSalida(int instante)
    {
        Dato dato=null;
    	
    	for(int i=0;i<colaSalida.size();i++)
		{
			dato=(Dato)colaSalida.get(i);
			if(dato.instante==instante)
			{				
				try
				{
				    // 1. Eliminamos el dato de la cola de salida
				    colaSalida.remove(i);
				    i--;

				    // 2. Calculamos la direccion de destino de los datos
                    DireccionPuntoAPunto dirDestino=null;
				    if(dato.direccion==null)
				    {
				    	// 3. Broadcast en una punto a punto, esto empieza a molar...jejeje
				        //    El nivel fisico se hace cargo del semi-broadcast, mira la interfaz
				    	//    de los equipos y envia el paquete al otro equipo
				    	dirDestino=new DireccionPuntoAPunto(255);
				    }
				    else
				    {	
				        dirDestino=new DireccionPuntoAPunto(dato.direccion);
				    }
				    
				    // 3. Montamos la trama
				    Dato datoAux=new Dato(dato);
				    datoAux.direccion=dirDestino;
				    datoAux.paquete=new TramaPuntoAPunto(dirDestino,dato.paquete,dato.protocolo);
                    datoAux.instante+=getRetardo();
                 
                    // 4. Apuntamos el evento
                    equipo.NuevoEvento('E',datoAux.instante,datoAux.paquete,"Trama con destino a "+dirDestino.toString());
                
                    // 5. Le pasamos los datos a la red (si es necesario)
				    if(datoAux.direccion.equals(direccion))
				    {
				        //Esto no puede succeder nunca, si el paquete es enviado por este equipo, y a este equipo
				        //el nivel IP deberia haberlo gestionado correctamente gracias a la tabla de rutas.
				        //con la tabla de rutas, deberia haberse dado cuenta de que no tenia que enviar el paquete
				        //al nivel inferior -> ignoramos el paquete, pero anotamos la incidencia
				        datoAux.red=null;
				        datoAux.interfaz=null;
				        equipo.NuevoEvento('X',datoAux.instante,datoAux.paquete,"Posible error en las tablas de rutas, o direcciones MAC duplicadas");
				    }
				    else
				    {
				        red.Enviar(datoAux);
				    }
				}
				catch(IllegalArgumentException e) 
				{ 
				    // si el paquete es erroneo o la direccion especificada en
				    // dato.direccion no es una direccion MAC valida
				}
			}
		}
    }
    
    
    
	/**
	 * Envia las tramas de entrada que toquen, al nivel superior (p. ej: nivelIPv4)
	 * @param instante Instante de tiempo
	 */
    private void ProcesarEntrada(int instante)
    {
    	Dato dato=null;
    	TramaPuntoAPunto trama=null;
    	
		// Comprobamos cada trama de la cola de entrada...
    	for(int i=0;i<colaEntrada.size();i++)
		{
			dato=(Dato)colaEntrada.get(i);
			
			// ...buscando una que se tenga que procesar en el instante actual 
			if(dato.instante==instante)
			{
				// ignoramos las tramas no Ethernet
				try
				{
				    // 1. Eliminamos la trama de la cola de entrada
					colaEntrada.remove(i);
					i--;
									
					// 2. Convertimos a trama Ethernet
					trama=(TramaPuntoAPunto)dato.paquete;
					
					// 3. Comprobamos que el destinatario es correcto
					//    (y que el origen no soy yo, ¿spoofing mac?)
					//    (si la me llega un paquete, seguro que es para mi, la red gestiona esto,
					//    (tampoco esta de mas comprobarlo... :| )
					if(EsParaMi(trama.getDestino()))
				 	{
					    // 4.0. Apuntamos el evento
					    equipo.NuevoEvento('R',instante,dato.paquete,"Trama recibida");
					    
				        // 4.1 Averiguamos el protocolo que usa la carga util (campo de datos)
				        dato.protocolo=trama.getTipo();
				        
				        // 4.2 Comprobamos que hay un nivel superior que acepta ese protocolo
				        Nivel nivel=getNivelSuperior("puntoapunto",dato.protocolo);
				        if(nivel!=null)
				        {                        
                            // Extraemos el campo de datos, para pasarlo al nivel superior
				            Buffer datos=new Buffer(trama.Tam()-3);
				            for(int j=0;j<datos.Tam();j++)
				            	datos.setByte(j,trama.getByte(3+j));
				            dato.paquete=datos;
				            	
				            // Pasamos el dato al nivel superior
				            dato.instante+=getRetardo();
				            nivel.ProgramarEntrada(dato);
				        }
				        else
				        {
				        	equipo.NuevoEvento('X',dato.instante,dato.paquete,"Se ha recibido una trama que no esta asociada a ningun nivel superior");
				            // si se ha recibido un tipo de trama que no corresponde
				            // a ningun nivel, la ignoramos
				        }
					}
					else
					{
						// La trama que se ha recibido no debe ser procesada por
						// este nivel, ya que va dirigida a otro interfaz
					}
				}
				catch(Exception e) 
				{
					Objeto.DEBUG("Error de conversion en el nivel PuntoAPunto"); 
				}
			}
		}
    }
    
    
    
    /**
     * Comprueba si la trama va dirigia a este nivel o no
     * @param destino Direccion de destino de la trama
     * @return Cierto si la trama va dirigida a este nivel
     */
    private boolean EsParaMi(DireccionPuntoAPunto destino)
	{
    	boolean retorno=false;
    	
    	// 1. Comprobamos a quien va dirigida la trama
    	if(destino!=null)
    	{	
    	   // 1.1 La trama va destinada a mi directamente
    	   if(destino.equals(direccion) || destino.EsBroadcast())
    	   	  retorno=true;
    	}
    
    	// 2. Devolvemos el resultado
    	return(retorno);
    }
    
    
    
    /**
     * Devuelve el identificador del nivel
     * @return Identificador del nivel
     */
    public String ID()
	{
    	return("puntoapunto");
    }
    
    
    /**
     * Comprueba que el dato de entrada sea correcto
     * @param dato Dato a comprobar
     * @return Cierto si el dato es correcto, falso en otro caso
     */
    public boolean ComprobarEntrada(Dato dato)
    {
        boolean correcto=false;
        
        if(dato!=null && dato.instante>=0 && dato.paquete!=null)
        {
           if(dato.paquete instanceof TramaPuntoAPunto)
               correcto=true;
        }
        
        if(!correcto)
        {
            System.out.println("dato: "+dato+" "+dato.instante);
            System.out.println("NivelPuntoAPunto: ERROR "+dato.paquete.getClass().getName());
        }
        return(correcto);
    }
    
    
    
	/**
	 * Comprueba que el dato de salida sea correcto
	 * @param dato Dato a comprobar
	 * @return Cierto, si el dato es correcto, falso, en otro caso
	 */
    public boolean ComprobarSalida(Dato dato)
    {
        boolean correcto=false;
        
        if(dato!=null && dato.instante>=0 && dato.paquete!=null && dato.interfaz!=null)
        {
        	if(dato.protocolo>=0)
                correcto=true;
        }
        
        if(!correcto)
        {
            System.out.println("dato: "+dato+" "+dato.instante);
            System.out.println("NivelPuntoAPunto: ERROR "+dato.paquete.getClass().getName());
        }
            
        return(correcto);
    }
}
