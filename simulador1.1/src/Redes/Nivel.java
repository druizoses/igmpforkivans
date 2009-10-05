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

package Redes;

import java.util.Vector;

import Equipos.Equipo;
import Proyecto.ListaParametros;
import Proyecto.Parametro;

/**
 * Clase base para los módulos de la pila de comunicaciones
 */
public abstract class Nivel
{
    /**
	 * Cola de paquetes que deben ser enviados
	 */
	protected Vector colaSalida;
	
	/**
	 * Cola de paquetes que llegan al nivel
	 */
	protected Vector colaEntrada;
    
	/**
	 * Equipo contenedor de este nivel
	 */
	protected Equipo equipo;
	
	/**
	 * Niveles inferiores
	 */
    private Vector nivelesInferiores;
	
	/**
	 * Niveles superiores
	 */
	private Vector nivelesSuperiores;
	
	/**
	 * Lista de errores que se deberan simular
	 */
	private Vector Errores;

	/**
	 * Lista de parametros;
	 */
	public ListaParametros parametros;
	
	
    /**
	 * Constructor
	 * @param equipo Equipo propietario del nivel
	 * @throws IllegalArgumentException si el equipo no es valido
	 */
	public Nivel(Equipo equipo)
	{
        if(equipo==null)
            throw new IllegalArgumentException("El nivel deber permanecer a algun equipo");
	    
	    // 0. Inicializacion
		this.equipo=equipo;
		colaSalida=new Vector();
		colaEntrada=new Vector();
		nivelesInferiores=new Vector();
		nivelesSuperiores=new Vector();
		Errores=new Vector();
		parametros=new ListaParametros();
		
		parametros.add(new Parametro("Retardo","Retardo de procesamiento","java.lang.Integer"));
		parametros.setValor("Retardo",new Integer(1)); // retardo por defecto		
	}
    
	
	
	
	
	
	/* ----------------------- GESTION DE LAS COLAS --------------------------- */
	
	/**
	 * Programa un paquete para que se procese en un instante determinado
	 * @param dato Datos del paquete, instante de procesamiento, ...
	 * @return Devuelve cierto, si el dato se ha podido programar, falso en otro caso
	 */
	public boolean ProgramarSalida(Dato dato)
	{   
        if(ComprobarSalida(dato))
        {    
	       colaSalida.add(dato);
	       return(true);
        }
        else
        {
            equipo.NuevoEvento('X',0,new Buffer(10),"Error programando la salida");
        }
        return(false);
	}
    
	
	
	/**
	 * Programa un paquete para que se procese en un instante determinado
	 * @param dato Datos del paquete, instante de procesamiento, ...
	 * @return Devuelve cierto si el dato se ha podido programar, falso en otro caso
	 */
	public boolean ProgramarEntrada(Dato dato)
	{
	    if(ComprobarEntrada(dato))
	    {    
	       colaEntrada.add(dato);
	       return(true);
	    }
	    else
	    {
	        equipo.NuevoEvento('X',0,new Buffer(10),"Error programando la entrada");
	    }
	    return(false);
	}

	
    
	
	/* ----------------- PROTOCOLOS SOPORTADOS POR EL NIVEL -------------------  */
	
	/**
	 * Comprueba si el protocolo indicado esta soportado
	 * @param nivel_inferior Modulo al que pertenece el identificador de protocolo
	 * @param protocolo Identificador del protocolo
	 * @return Cierto si el protocolo esta soportado por el nivel
	 */
	private boolean Soporta(String nivel_inferior,int protocolo)
	{
    	boolean soportado=false;
	    IDNivel id;
    	int i=0;
    	
    	if(nivel_inferior!=null && protocolo>=0)
    	{
    	    i=0;
    	    while(!soportado && (id=LocalizadorRedes.getIDNivel(i))!=null)
    	    {
    	    	if(id.nivel.equals(ID()))
    	    	{
    	            if(id.id_nivel_inferior.equals(nivel_inferior) && protocolo==id.codigo)
    	            	soportado=true;
    	    	}
    	    	
    	    	i++;
    	    }
    	}
    	
    	return(soportado);
	}
	
	
	
	/**
	 * Devuelve el identificador del nivel, entendible por el modulo especificado
	 * @param nivel_inferior Modulo
	 * @return Identificador de este nivel, para que lo use el 'Modulo'
	 */
	protected int getID(String nivel_inferior)
	{	
	    IDNivel id;
        int codigo=-1;
	    int i=0;
    	
    	if(nivel_inferior!=null)
    	{
    	    while(codigo==-1 && (id=LocalizadorRedes.getIDNivel(i))!=null)
    	    {
    	    	if(id.nivel.equals(ID()) && id.id_nivel_inferior.equals(nivel_inferior))
    	            codigo=id.codigo;
    	    	
    	    	i++;
    	    }
    	}
		
		return(codigo);
	}
	
	
	
	
	/* -------------------- NIVELES SUPERIORES E INFERIORES -------------------- */
	
	/**
	 * Devuelve el nivel superior capaz de procesar las tramas del protocolo indicado
	 * @param modulo Modulo asociado al identificador de protocolo
	 * @param protocolo Identificador de protocolo, segun RFC 790 (u otro)
	 * @return Nivel asociado al protocolo o null si no existe
	 */
	public Nivel getNivelSuperior(String modulo,int protocolo)
	{
		Nivel nivelAux=null;
		Nivel retorno=null;
		boolean encontrado=false;
		
		// 1. Buscamos el nivel
        if(modulo!=null && protocolo>=0)
        {    
		    for(int i=0;i<nivelesSuperiores.size() && !encontrado;i++)
		    {
			    nivelAux=(Nivel)nivelesSuperiores.get(i);
			    if(nivelAux.Soporta(modulo,protocolo))
			    {
				    retorno=nivelAux;
				    encontrado=true;
			    }
		    }
        }
		
		// 2. Devolvemos el nivel superior asociado a ese protocolo
		return(retorno);
	}
	
	
	
	/**
	 * Añade a la lista de niveles superiores el nivel indicado
	 * @param nivel Nivel superior
	 */
	public void setNivelSuperior(Nivel nivel)
	{
		if(nivel!=null)
		   nivelesSuperiores.add(nivel);
	}
	
	
	
	/**
	 * Devuelve el nivel inferior que entienda el protocolo indicado
	 * @param modulo Modulo asociado al identificador de protocolo
	 * @param protocolo Identificador del protocolo del nivel inferior
	 * @return Nivel inferior
	 */
	public Nivel getNivelInferior(String modulo, int protocolo)
	{
		Nivel nivelAux=null;
		Nivel retorno=null;
		boolean encontrado=false;
		
		// 1. Buscamos el nivel, si existe
		if(modulo!=null && protocolo>=0)
		{    
		    for(int i=0;i<nivelesInferiores.size() && !encontrado;i++)
		    {
			    nivelAux=(Nivel)nivelesInferiores.get(i);
			    if(nivelAux.Soporta(modulo,protocolo))
			    {
				    retorno=nivelAux;
				    encontrado=true;
			    }
		    }
		}
		
		// 2. Devolvemos el nivel superior asociado a ese protocolo
		return(retorno);
	}
	
	
	
	/**
	 * Añade a la lista de niveles inferiores el nivel indicado
	 * @param nivel Nivel inferior
	 */
	public void setNivelInferior(Nivel nivel)
	{
		if(nivel!=null)
		   nivelesInferiores.add(nivel);
	}

	

	
	/* -------------------- GESTION DE ERRORES SIMULADOS -------------------- */
	
	/**
	 * Activa o desactiva el flag indicado para que se simulen, o no,errores 
	 * de ese tipo
	 * @param flag Flag asociado al error
	 * @param activar Se pone a cierto para activar el flag, y a falso para
	 *                desactivarlo
	 * @return Falso si se produce algun error en el proceso
	 */
	public boolean SimularError(String flag, boolean activar)
	{
	    boolean encontrado=false;
	    boolean correcto=true;
	    
	    if(flag!=null && flag.length()>0)
	    {
	        for(int i=0;!encontrado && i<Errores.size();i++)
	        {
	            String elemento=(String)Errores.get(i);
	            if(flag.compareToIgnoreCase(elemento)==0)
	            {    
	               encontrado=true;
	               
	               if(!activar)
	                   Errores.remove(i);
	            }
	        }
	        
	        if(!encontrado && activar)
	            Errores.add(flag);
	    }
	    else
	    {
	        correcto=false;
	    }
	    
	    return(correcto);
	}
	
	
	
	/**
	 * Consulta la lista de errores a simular y devuelve cierto si el error
	 * asociado al flag se simulara o no
	 * @param flag Flag asociado al error
	 * @return Cierto si se simularan errores del tipo indicado
	 */
	public boolean SimularError(String flag)
	{
	    boolean activo=false;
	    
	    if(flag!=null && flag.length()>0)
	    {
	        for(int i=0;!activo && i<Errores.size();i++)
	        {
	            String elemento=(String)Errores.get(i);
	            if(flag.compareToIgnoreCase(elemento)==0)
	            {    
	                activo=true;
	            }
	        }
	    }
	    
	    return(activo);
	}
	
	
	
	/**
	 * Devuelve el retardo de procesamiento del nivel o el valor por defecto 1
	 * @return Retardo, en unidades de tiempo del simulador.
	 */
	protected int getRetardo()
	{
	    int retardo=-1;
	    Integer Iretardo=(Integer)parametros.getValor("Retardo");
	    if(Iretardo!=null)
	        retardo=Iretardo.intValue();
	    if(retardo<=0)
	        retardo=1;  // valor por defecto
	    
	    return(retardo);
	}
	
	
	
    /**
     * Procesa todos los paquetes de un determinado nivel, programados en un instante
     * @param instante Instante
     */
    public abstract void Procesar(int instante);
    
    
 
    /**
     * Devuelve el numero de paquetes pendientes de ser procesados
     * @return Numero de paquetes pendientes de ser procesados
     */
    public abstract int Pendientes();
    
    
    
    /**
     * Comprueba que los datos de salida son correctos
     * @param dato Dato a comprobar
     * @return Cierto si los datos son validos
     */
    protected abstract boolean ComprobarSalida(Dato dato);
    
    
    /**
     * Comprueba que los datos de entrada son correctos
     * @param dato Dato a comprobar
     * @return Cierto si los datos son validos
     */
    protected abstract boolean ComprobarEntrada(Dato dato);
    
    
    
    /**
     * Devuelve el identificador del nivel
     * @return Identificador del nivel
     */
    public abstract String ID();
}
