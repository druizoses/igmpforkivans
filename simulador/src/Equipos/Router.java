/*
    Simulador de redes IP (KIVA). API de Simulacion, permite simular
    redes de tipo IP que usen IP, ARP, e ICMP.
    Copyright (C) 2004, Jos� Mar�a D�az Gorriz

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
 * Creado el 17-ene-2004
 */
 
package Equipos;

import Redes.*;
import Redes.IPv4.*;
import Redes.IPv4.ICMP.*;
import Redes.IPv4.IGMP.MensajeIGMP;
import Redes.IPv4.IGMP.ModuloIGMP;
import Redes.IPv4.ARP.*;
import Proyecto.*;

/**
 * Router (componente simulable)
 */
public class Router extends Equipo
{
	private static ListaParametros caracteristicas;
	
    /**
     * Modulo ICMP
     */
    ModuloICMP moduloICMP;
    
    
    
    /**
     * Nivel IPv4
     */
    NivelIPv4 nivelIPv4;
    
    /**
     * Modulo ARP
     */
    ModuloARP moduloARP;

    /**
     * Niveles de enlace
     */
    /* definidos en los interfaces */
    
	/**
	 * Inicializador de la clase
	 */
    static
	{
    	caracteristicas=new ListaParametros();
    	caracteristicas.add(new Parametro("nombre","Nombre generico","java.lang.String"));
        caracteristicas.setValor("nombre",new String("Router"));
        caracteristicas.add(new Parametro("imagen","Icono asociado a la clase","java.lang.String"));
        caracteristicas.setValor("imagen","---");        
	}
		

    
    /**
     * Constructor
     */
    public Router()
    {
    	super();
        // Enlazamos la tabla de rutas
        tablaDeRutas=nivelIPv4.tablaDeRutas;
    }

    public void encender(){
    	super.encender();
    	nivelIPv4.tablaDeRutas=tablaDeRutas;
    }
    
    protected void iniciar(){
        // 1. Definimos los niveles de la pila
        moduloARP=new ModuloARP(this);
        moduloICMP=new ModuloICMP(this);
        nivelIPv4=new NivelIPv4(this,moduloARP,moduloICMP);

        // 2. Interconectamos los niveles
        moduloICMP.setNivelInferior(nivelIPv4);
        nivelIPv4.setNivelInferior(moduloARP);
        nivelIPv4.setNivelSuperior(moduloICMP);
        
        // 3. Activamos el IP Forwarding
        nivelIPv4.IPForwarding(true);
    }
    
    /**
     * Registra una nueva interfaz para el router y enlaza los niveles
     * @param interfaz Nueva Interfaz del equipo
     */
    public void setInterfaz(Interfaz interfaz)
    {
        // 1. A�adimos la interfaz
        super.setInterfaz(interfaz);
        
        // 2. Enlazamos el nivel de enlace
        interfaz.getNivelEnlace().setNivelSuperior(moduloARP);
        interfaz.getNivelEnlace().setNivelSuperior(nivelIPv4);
    }
    
    
    
    
    /**
     * Procesa los paquetes programados para un determinado instante
     * @param instante Instante
     */
    public void Procesar(int instante)
    {  
    	if (encendido){
	        // 1. Comprobamos si hay algo que procesar en el modulo ICMP
	        moduloICMP.Procesar(instante);
	        
	        // 2. Comprobamos si hay algo en el nivel IPv4
	        nivelIPv4.Procesar(instante);
	        
	        // 3. Comprobamos si el modulo ARP tiene que enviar peticiones
	        moduloARP.Procesar(instante);
	        
	        // 4. Comprobamos si hay algo que procesar en los niveles de enlace
	        for(int i=0;i<NumInterfaces();i++)
	            getInterfaz(i).getNivelEnlace().Procesar(instante);
    	}
    }
    

    
    /**
     * Devuelve el numero de paquetes que quedan por procesar
     * @return Numero de paquetes que quedan por procesar
     */
    public int Pendientes()
    {
        int pendientes=0;
        
        if (encendido){
	        // 1. Comprobamos si hay algo que procesar en los niveles de enlace
	        for(int i=0;i<NumInterfaces();i++)
	            pendientes+=getInterfaz(i).getNivelEnlace().Pendientes();
	        
	        // 2. Devolvemos el numero de paquetes que nos han quedado por procesar;
	        pendientes+=moduloARP.Pendientes()+moduloICMP.Pendientes()+nivelIPv4.Pendientes();
        }
        return(pendientes);
    }
    
    
    
    /**
     * Recibimos un dato de la red y se lo pasamos al nivel adecuado
     * @param dato Datos recibidos
     */
    public void ProgramarEntrada(Dato dato)
    {
        if(dato!=null && dato.interfaz!=null)
            dato.interfaz.getNivelEnlace().ProgramarEntrada(dato);
    }

    

    /**
     * Programa un dato para que se procese en un determinado instante
     * en un determinado modulo
     * @param dato Dato a programar
     */
    public void ProgramarSalida(Dato dato)
    {	
        if(dato==null)
            return;
    	
        // 1. Paquete ARP
        if(dato.paquete instanceof PaqueteARP)
        {
            // 1.1 Comprobamos que el dato es correcto para el modulo ARP y se puede programar
            if(moduloARP.ProgramarSalida(dato))
                NuevoEvento('E',dato.instante,dato.paquete,"Envio de datos programado en ARP");
        }
        
        // 2. Mensaje ICMP
        else if(dato.paquete instanceof MensajeICMP)
        {
            if(moduloICMP.ProgramarSalida(dato))
                NuevoEvento('E',dato.instante,dato.paquete,"Envio de datos programado en ICMP");
        }
         
        // 3. Otros (DatagramaIPv4 y otros tipos de paquete)
        else
        {   if(nivelIPv4.ProgramarSalida(dato))
            NuevoEvento('E',dato.instante,dato.paquete,"Envio de datos programado en IP");
        }
    }
    
    
    
    /**
     * Controla la simulacion de errores en los distintos niveles de la pila
     * @param nivelID Nivel donde se simulara el error
     * @param flag Flag asociado al error
     * @param activar Flag que activa/desactiva la simulacion
     * @return Falso si se ha producido algun error en el proceso
     */
    public boolean SimularError(int nivelID, String flag, boolean activar)
    {
        boolean correcto=true; // de momento todo va bien
        
        switch(nivelID)
        {
            case Equipo.kARP:
            {
                correcto=moduloARP.SimularError(flag,activar);
                break;
            }
        
            case Equipo.kIPv4:
            {
                correcto=nivelIPv4.SimularError(flag,activar);
                break;
            }
        
            case Equipo.kICMP:
            {
                correcto=moduloICMP.SimularError(flag,activar);
                break;
            }
            
            default:
            {
                correcto=false;  // nivel no permitido (desconocido)   
            }
        }
        
        return(correcto);
    }    
    
    
    /**
     * Controla el comportamiento de los distindos modulos que forman la pila de 
     * comunicaciones
     * @param nivelID Nivel del parametro que se quiere cambiar
     * @param parametro Nombre del parametro
     * @param valor Valor
     */
    public void ConfiguraPila(int nivelID, String parametro, Object valor)
    {    
        switch(nivelID)
        {
            case Equipo.kARP:
            {
                moduloARP.parametros.setValor(parametro,valor);
                break;
            }
        
            case Equipo.kIPv4:
            {
                nivelIPv4.parametros.setValor(parametro,valor);
                break;
            }
        
            case Equipo.kICMP:
            {
                moduloICMP.parametros.setValor(parametro,valor);
                break;
            }
        }
    }
    
    
    
    /**
     * Controla los parametros de la pila (nivel de enlace)
     * @param nombreInterfaz Nombre de la interfaz
     * @param parametro Nombre del parametro
     * @param valor Valor
     */    
    public void ConfiguraPila(String nombreInterfaz,String parametro,Object valor)
    {
        Interfaz interfaz=this.getInterfaz(nombreInterfaz);
        if(interfaz!=null)
            interfaz.getNivelEnlace().parametros.setValor(parametro,valor);
    }
    
    
    
    /**
     * Devuelve la lista de caracteristicas de un Router
     * @return Lista de caracteristicas
     */
    public ListaParametros Caracteristicas()
	{
    	return(caracteristicas);
    }
}
