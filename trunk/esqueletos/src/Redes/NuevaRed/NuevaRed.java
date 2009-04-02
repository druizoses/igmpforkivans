package Redes.NuevaRed;

import Redes.*;
import Equipos.Equipo;
import Proyecto.ListaParametros;
import Proyecto.Parametro;

/**
 * Red NuevaRed (componente simulable)
 */
public class NuevaRed extends Red
{
	/**
	 * Tipo NuevaRed
	 */
	public static final int kTIPO=0xFFFF;
	
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
    	caracteristicas.setValor("nombre",new String("NuevaRed"));
        caracteristicas.setValor("dibujo",new String("bus"));
        caracteristicas.setValor("se conecta a redes",new Boolean(false));
        caracteristicas.setValor("se conecta a equipos",new Boolean(true));
	    
        caracteristicas.add(new Parametro("clase_trama","Clase que define las tramas","java.lang.String"));
        caracteristicas.setValor("clase_trama","Redes.NuevaRed.TramaNuevaRed");
        caracteristicas.add(new Parametro("clase_direccion","Clase que define las direcciones","java.lang.String"));
        caracteristicas.setValor("clase_direccion","Redes.NuevaRed.DireccionNuevaRed");
        caracteristicas.add(new Parametro("clase_interfaz","Clase que define las interfaces","java.lang.String"));
        caracteristicas.setValor("clase_interfaz","Redes.NuevaRed.InterfazNuevaRed");
        caracteristicas.add(new Parametro("clase_nivel","Clase que define el nivel de enlace","java.lang.String"));
        caracteristicas.setValor("clase_nivel","Redes.NuevaRed.NivelNuevaRed");
	}
    
    
	
	/**
	 * Constructor
	 */
    public NuevaRed()
    {
        super("sin nombre",XXXXX);
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
			    if(dato.paquete instanceof TramaNuevaRed)
			    {
			        TramaNuevaRed trama=(TramaNuevaRed)dato.paquete;
			        
			        // 1.4.1 Envio a Equipos
			        
			        // 1.4.2 Envio a Redes
                }
			    else
			    {
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
