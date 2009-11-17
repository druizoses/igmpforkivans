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
 * Creado el 01-ene-2004
 */
 
package Redes.IPv4.ICMP;

import Redes.*;
import Equipos.Equipo;
import Redes.IPv4.DireccionIPv4;

/**
 * Módulo ICMP
 */
public class ModuloICMP extends Nivel
{   
	/**
	 * Inicializador de la clase
	 */
	static
	{
		// Este nivel conoce el identificador que IPv4 usa para ICMPv4
		LocalizadorRedes.Registrar("icmp","ipv4",1);
	}
	
	
    /**
     * Constructor
     * @param equipo Equipo propietario de este modulo
     */
    public ModuloICMP(Equipo equipo)
    {
        super(equipo);
        
        // Retardo asociado al procesamiento realizado por el modulo ICMP
        parametros.setValor("Retardo",new Integer(1));
    }
    
    
    
    /**
     * Procesa los mensajes ICMP programados para el instante indicado
     * @param instante Instante de tiempo
     */
	public void Procesar(int instante)
	{
        // 1. Procesamos las entradas
        ProcesarEntrada(instante);
		
	    // 2. Procesamos las salidas
        ProcesarSalida(instante);
	}


	
	/**
	 * Devuelve el numero de mensajes pendientes de ser procesados
	 * @return Numero de mensaje pendientes de ser procesados
	 */
	public int Pendientes()
	{
	    return(colaEntrada.size()+colaSalida.size());	    
	}
	
	
	
	/**
	 * Devuelve el identificador del modulo
	 * @return Identificador unico del modulo
	 */
	public String ID()
	{
		return("icmp");
	}
	
	

	/**
	 * Procesa los mensajes de la cola de salida (envios)
	 * @param instante Instante de tiempo
	 */
	private void ProcesarSalida(int instante)
	{
	    for(int i=0;i<colaSalida.size();i++)
	    {
	        Dato dato=(Dato)colaSalida.get(i);
	        
	        if(dato.instante==instante)
	        {
	            // 1. Extraemos el dato de la cola
	            colaSalida.remove(i);
	            i--;

	            // 2. Mensaje ICMP
	            if(dato.paquete instanceof MensajeICMP)
	            {    
	                MensajeICMP mensajeICMP=(MensajeICMP)dato.paquete;
	                
	                if(!(SimularError(ErroresICMP.flags[0]) && mensajeICMP.getType()==3 && mensajeICMP.getCode()==2))
	                {
	                   // Enviamos el mensaje
	                   Nivel nivel=getNivelInferior("ipv4",0);
	                   if(nivel!=null)
	                   {
	                       // Creamos el dato
	                       Dato datoAux=new Dato(instante,mensajeICMP);
	                       datoAux.direccion=dato.direccion;
	                       datoAux.protocolo=getID(nivel.ID());
	                       datoAux.interfaz=dato.interfaz;
	                
	                       // Registramos el evento
	                       int tipo=mensajeICMP.getType();
	                       int codigo=mensajeICMP.getCode();
	                       equipo.NuevoEvento('E',datoAux.instante,mensajeICMP,"Mensaje ICMP ["+tipo+"|"+codigo+"] "+MensajeICMP.Descripcion(tipo,codigo));
	                       datoAux.instante+=getRetardo();
	                    
	                       // Pasamos el dato al nivel inferior
	                       //datoAux.protocolo=getID(nivel.ID());
	                       nivel.ProgramarSalida(datoAux);
	                    }
	                }
	            }
	        }
	    }
	}
	
	
	
	/**
	 * Procesa los mensajes de un determinado instante de tiempo
	 * @param instante Instante de tiempo
	 */
	private void ProcesarEntrada(int instante)
	{
	    for(int i=0;i<colaEntrada.size();i++)
	    {
	        Dato dato=(Dato)colaEntrada.get(i);
	        
	        if(dato.instante==instante)
	        {
	            // 1. Eliminamos el mensaje de la cola
	            colaEntrada.remove(i);
	            i--;
	            
	            // 2. Mensaje ICMP
	            MensajeICMP mensajeICMP=new MensajeICMP(dato.paquete);
	            
	            if(!(SimularError(ErroresICMP.flags[0]) && mensajeICMP.getType()==3 && mensajeICMP.getCode()==2))
	            {
	               // 3. Registramos el evento
	               int tipo=mensajeICMP.getType();
	               int codigo=mensajeICMP.getCode();
	               equipo.NuevoEvento('R',instante,mensajeICMP,"Mensaje ICMP ["+tipo+"|"+codigo+"] "+MensajeICMP.Descripcion(tipo,codigo));
	            
	               // 4. Actuamos segun el tipo de mensaje recibido
	               ProcesaMensaje(instante,mensajeICMP,dato);
	               
	               // 5. Enviamos el mensaje al nivel superior
	               //...TCP/UDP/RAW sin implementar aun...
	            }
	        }
	    }
	}
	
	
	
	/**
	 * Procesa un mensaje ICMP
	 * @param instante Instante de tiempo en el que se procesa el mensaje
	 * @param mensajeICMP MensajeICMP que va a ser procesado
	 * @param dato Dato del equipo que genero el mensaje
	 */
	private void ProcesaMensaje(int instante, MensajeICMP mensajeICMP,Dato dato)
	{
		if(mensajeICMP!=null)
		{
			int tipo=mensajeICMP.getType();
			int codigo=mensajeICMP.getCode();
			
			switch(tipo)
			{
				// Mensaje 'Echo', tenemos que responder enviando un 'Echo reply'
				case 8:
				{
					if(codigo==0)
					{
						// Creamos el 'echo reply' 
						MensajeICMP echoReply=new MensajeICMP(mensajeICMP,0,0);
						
						Dato datoAux=new Dato(instante+1,echoReply);
						datoAux.direccion=dato.direccion;
						datoAux.interfaz=dato.interfaz;						
						
						ProgramarSalida(datoAux);
					}
				}
			}
		}
	}
	
	
	
    /**
     * Comprueba que el dato de entrada sea correcto
     * @param dato Dato a comprobar
     * @return Cierto si el dato es correcto, falso en otro caso
     */
	public boolean ComprobarEntrada(Dato dato)
	{
	    boolean correcto=false;
	 
	    if(dato!=null && dato.instante>=0)
	        correcto=true;
	    
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
	    
	    if(dato!=null && dato.instante>=0 && dato.paquete instanceof MensajeICMP)
	        if(dato.direccion!=null && dato.direccion instanceof DireccionIPv4)
	            correcto=true;
	    
	    return(correcto);
	    
	}
	
	
	
	/**
	 * Comprueba si es correcto el envio del mensaje ICMP 
	 * @param mensaje Mensaje ICMP a enviar
	 * @return Cierto, si se debe enviar el mensaje, falso en otro caso
	 */
	private boolean ProcedeEnvio(MensajeICMP mensaje)
	{
		boolean procede=true;
		int tipo=mensaje.getType();
		
		
		if(tipo==3 || tipo==5 || tipo==11)
		{
			// Comprobamos el tipo de paquete que en teoria provoca el envio
			// del ICMP
			int protocolo=mensaje.getByte(8+9);
			if(protocolo==IDenIP())
			{
				procede=false;
			}
		}
		
		return(procede);
	}
	
	
	
	/**
	 * Devuelve el identificador que usa IP para referirse a ICMP
	 * @return Identificador de ICMP en en modulo IP
	 */
	public static int IDenIP()
	{
	    IDNivel id=null;
	    int i=0;
	    
	    do
	    {
	        id=LocalizadorRedes.getIDNivel(i);
	        i++;
	        if(id!=null)
	        {
		        System.out.println(id.nivel+" "+id.id_nivel_inferior+" "+id.codigo);
	            if(id.nivel.equals("ipv4") && id.id_nivel_inferior.equals("icmp"))
	                return(id.codigo);
	        }
	    } while(id!=null);
	    
	    return(0);
	}
}
