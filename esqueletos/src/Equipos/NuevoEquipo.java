package Equipos;

import Redes.*;
import Proyecto.*;

/**
 * Nuevo Equipo (Componente simulable)
 */
public class NuevoEquipo extends Equipo
{	
    /**
     * Modulos de la pila del nuevoEquipo
     */
   
	/**
	 * Niveles de enlace
	 */
    /* definidos en los interfaces */
		
	/**
	 * Caracteristicas genericas (globales) de un NuevoEquipo
	 */
    private static ListaParametros caracteristicas;
	
    
	/**
	 * Inicializador de la clase
	 */
    static
	{
    	caracteristicas=new ListaParametros();
    	caracteristicas.add(new Parametro("nombre","Nombre generico","java.lang.String"));
        caracteristicas.setValor("nombre",new String("NuevoEquipo"));
        caracteristicas.add(new Parametro("imagen","Icono asociado a la clase","java.lang.String"));
        caracteristicas.setValor("imagen","---");
	}
	
	
	
    /**
     * Constructor
     */
    public NuevoEquipo()
    {
    	// 1. Definimos los niveles de la pila
    	
        // 2. Interconectamos los niveles
        
        // Los niveles de enlace son gestionados por la tabla de rutas del nivel IP
        // y por los interfaces.
    
        // 3. Enlazamos la tabla de rutas (si la usa...)
    }
    
	
    
    /**
     * Registra una nueva interfaz para el ordenador y enlaza los niveles
     * @param interfaz Nueva Interfaz del equipo
     */
    public void setInterfaz(Interfaz interfaz)
    {
        super.setInterfaz(interfaz);
        
        // 1. Otras acciones...
    }
	
  
    
    
	/**
	 * Procesa los paquetes programados para un determinado instante
	 * @param instante Instante
	 */
	public void Procesar(int instante)
	{  
		// 1. Comprobamos si hay algo que procesar en algun modulo
	}
	

	
	/**
	 * Devuelve el numero de paquetes que quedan por procesar
	 * @return Numero de paquetes que quedan por procesar
	 */
	public int Pendientes()
	{
        int pendientes=0;
	    
        // 1. Añadimos a 'pendientes' todos los paquetes pendientes llamando
        //    al método Pendientes() de cada módulo.

        return(pendientes);
	}
	
	
	
	/**
	 * Recibimos un dato de la red y se lo pasamos al nivel adecuado
	 * @param dato Dato a programar
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
        
        // Programamos el dato en el módulo que toque, dependiento del tipo
        // del objeto dato.paquete
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
        
        // Activar el flag correspondiente en el modulo correspondiente
        
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
     * Devuelve la lista de caracteristicas del Ordenador
     * @return Lista de caracteristicas
     */
    public ListaParametros Caracteristicas()
	{
    	return(caracteristicas);
    }
}
