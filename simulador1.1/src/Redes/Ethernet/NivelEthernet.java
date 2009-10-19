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
import Equipos.*;
import Proyecto.*;

/**
 * Nivel de enlace Ethernet
 */
public class NivelEthernet extends Nivel
{
	/**
	 * Red a la que enviaremos los datos y de la que recibiremos
	 */
	Red red;
	
	/**
	 * Direccion MAC de las tramas que atenderemos, el resto las ignoraremos
	 */
	DireccionEthernet direccion;
    
	/**
	 * Constructor
	 * @param equipo Equipo propietario de la interfaz que posee este nivel
	 * @param red Red que hay por debajo del nivel
	 * @param direccion Direccion asociada a este nivel
	 * @throws IllegalArgumentException si alguno de los parametros no es valido
	 */
    public NivelEthernet(Equipo equipo, Red red, DireccionEthernet direccion)
    {
    	super(equipo);
    	
    	if(red==null || direccion==null)
    	    throw new IllegalArgumentException("No se dispone de los valores necesarios para crear el nivel");
    	
    	this.red=red;
    	this.direccion=direccion;
    	
        // Retardo asociado al procesamiento realizado por el modulo de enlace este
        parametros.setValor("Retardo",new Integer(1));
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
				    DireccionEthernet dirDestino=null;
				    
				    if(dato.direccion!=null)
				        dirDestino=new DireccionEthernet(dato.direccion);
				    else
					    dirDestino=new DireccionEthernet("ff:ff:ff:ff:ff:ff");
				
				    // 3. Montamos la trama
				    Dato datoAux=new Dato(dato);
				    datoAux.direccion=dirDestino;
				    datoAux.paquete=new TramaEthernet(direccion,dirDestino,dato.paquete,dato.protocolo);
                    datoAux.instante+=getRetardo();
                
                    // 4. Apuntamos el evento
                    equipo.NuevoEvento('E',instante,datoAux.paquete,"Trama con destino a "+dirDestino.toString());
                
                    // 5. Le pasamos los datos a la red (si es necesario)
				    if(datoAux.direccion.equals(direccion))
				    {
				        //Esto puede succeder nunca, si el paquete es enviado por este equipo, y a este equipo
				    	//el nivel IP deberia haberlo gestionado correctamente gracias a la tabla de rutas.
				    	//con la tabla de rutas, deberia haberse dado cuenta de que no tenia que enviar el paquete
				    	//al nivel inferior -> ignoramos el paquete, pero anotamos la incidencia
				    	datoAux.red=null;
				    	datoAux.interfaz=null;
				    	equipo.NuevoEvento('X',datoAux.instante,datoAux.paquete,"Posible error en las tablas de rutas, o direcciones MAC duplicadas");
				    }
				    else
				    {
				    	if(equipo instanceof Equipos.Switch){
				    		Dato replica = new Dato(dato); 
				    		replica.instante++;
				    		red.Enviar(replica);
				    	}else
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
    	TramaEthernet trama=null;
    	
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
					
					// Pasamos a comparar el equipo al que pertenece la Interfaz. En caso de que
					// sea un Switch el proceso de entrada sera diferente, ya que el switch no
					// trabaja a nivel de red sino que opera en el subnivel de Fisico/MAC
					
					if(equipo instanceof Switch){
						equipo.ProgramarEntrada(dato);
					}
									
					// 2. Convertimos a trama Ethernet
					trama=(TramaEthernet)dato.paquete;
											
					// 3. Comprobamos que el destinatario es correcto
					//    (y que el origen no soy yo, ¿spoofing mac?)
					if(EsParaMi(trama.getDestino()) /*&& !EsParaMi(trama.getOrigen())*/)
				 	{
					    // 4.0. Apuntamos el evento
						for(int j = 0; j < equipo.NumInterfaces(); j++){
							if(equipo.getInterfaz(j).getNivelEnlace() == this)
								equipo.NuevoEvento('R',instante,dato.paquete,"Trama recibida"+";"+equipo.getInterfaz(j).getNombre());
						}
					    
				        // 4.1 Averiguamos el protocolo que usa la carga util (campo de datos)
				        dato.protocolo=trama.getTipo();
				        
				        // 4.2 Comprobamos que hay un nivel superior que acepta ese protocolo
				        Nivel nivel=getNivelSuperior("ethernet",dato.protocolo);
				        if(nivel!=null)
				        {                        
                            // Extraemos el campo de datos, para pasarlo al nivel superior
				            Buffer datos=new Buffer(trama.Tam()-trama.NumBytesRelleno()-26);
				            for(int j=0;j<datos.Tam();j++)
				            	datos.setByte(j,trama.getByte(22+j));
				            dato.paquete=datos;
				            	
				            // Pasamos el dato al nivel superior
				            dato.instante+=getRetardo();
				            nivel.ProgramarEntrada(dato);
				        }
				        else
				        {
				        	if(!(equipo instanceof Switch))
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
					equipo.NuevoEvento('X',instante,dato.paquete,"Error de conversion en el nivel Ethernet:" + e.getMessage());
				}
			}
		}
    }
    
    
    
    /**
     * Comprueba si la trama va dirigia a este nivel o no
     * @param destino Direccion de destino de la trama
     * @return Cierto si la trama va dirigida a este nivel
     */
    private boolean EsParaMi(DireccionEthernet destino)
	{
    	boolean retorno=false;
    	
    	// 1. Comprobamos a quien va dirigida la trama
    	if(destino!=null)
    	{	
    	   // 1.1 La trama va destinada a mi directamente
    	   if(destino.equals(direccion))
    	   	  retorno=true;
    	   
    	   // 1.2 La trama va destinada a la direccion de broadcast Ethernet
    	   else if(destino.EsBroadcast())
    	      retorno=true;
    	   // 1.3 La trama va destinada a una dirección multicast Ethernet
    	   else if(destino.EsMulticast())
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
    	return("ethernet");
    }
    
    
    
    /**
     * Comprueba que el dato de entrada es correcto
     * @param dato Dato que se quiere comprobar
     * @return Cierto si el dato es correcto
     */
    public boolean ComprobarEntrada(Dato dato)
    {
        boolean correcto=false;
        
        if(dato!=null && dato.instante>=0 && dato.paquete!=null)
        {
           if(dato.paquete instanceof TramaEthernet)
               correcto=true;
        }
        
        if(!correcto)
        {
            System.out.println("dato: "+dato+" "+dato.instante);
            System.out.println("NivelEthernet: ERROR "+dato.paquete.getClass().getName());
        }
        return(correcto);
    }
    
    
    
    /**
     * Comprueba si el dato de salida es correcto
     * @param dato Dato que se quiere comprobar
     * @return Cierto si es correcto
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
            System.out.println("NivelEthernet: ERROR "+dato.paquete.getClass().getName());
        }
            
        return(correcto);
    }
}
