package Redes.NuevaRed;
 
import Redes.*;
import Equipos.*;
import Proyecto.*;

/**
 * Nivel de enlace Nuew
 */
public class NivelNuevaRed extends Nivel
{
	/**
	 * Red a la que enviaremos los datos y de la que recibiremos
	 */
	Red red;
	
	/**
	 * Direccion MAC de las tramas que atenderemos, el resto las ignoraremos
	 */
	DireccionNuevaRed direccion;
	
	
	
	/**
	 * Constructor
	 * @param equipo Equipo propietario de la interfaz que posee este nivel
	 * @param red Red que hay por debajo del nivel
	 * @param direccion Direccion asociada a este nivel
	 * @throws IllegalArgumentException si alguno de los parametros no es valido
	 */
    public NivelNuevaRed(Equipo equipo, Red red, DireccionNuevaRed direccion)
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
				
				    // 3. Montamos la trama
                
                    // 4. Apuntamos el evento
                
                    // 5. Le pasamos los datos a la red (si es necesario)
				}
				catch(IllegalArgumentException e) 
				{ 
				}
			}
		}
    }
    
    
    
	/**
	 * Envia las tramas de entrada que toquen, al nivel superior
	 * @param instante Instante de tiempo
	 */
    private void ProcesarEntrada(int instante)
    {
    	Dato dato=null;
    	TramaNuevaRed trama=null;
    	
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
									
					// 2. Convertimos a trama al tipo NuevaRed
					trama=(TramaNuevaRed)dato.paquete;
					
					// 3. Procesado de la trama
				}
				catch(Exception e) 
				{ 
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
    	return("nuevared");
    }
    
    
    
    /**
     * Comprueba que el dato de entrada es correcto
     * @param dato Dato que se quiere comprobar
     * @return Cierto si el dato es correcto
     */
    public boolean ComprobarEntrada(Dato dato)
    {
    }
    
    
    
    /**
     * Comprueba si el dato de salida es correcto
     * @param dato Dato que se quiere comprobar
     * @return Cierto si es correcto
     */
    public boolean ComprobarSalida(Dato dato)
    {
    }
}
