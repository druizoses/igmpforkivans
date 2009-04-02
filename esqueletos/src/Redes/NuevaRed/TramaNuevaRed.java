package Redes.NuevaRed;

import Redes.*;

/**
 * Trama Ethernet
 */
public class TramaNuevaRed extends Buffer
{	
    /**
     * Constructor
     * @param origen direccion origen
     * @param destino direccion destino
     * @param p datos contenidos
     * @param tipo tipo de los datos contenidos
     * @throws IllegalArgumentException Si el tipo de datos contenidos no son de tipo conocido
     */
    public TramaNuevaRed(DireccionNuevaRed origen,DireccionNuevaRed destino, Buffer p, int tipo) throws IllegalArgumentException
    {	       
    }
    
    
    
    /**
     * Constructor
     * @param trama Buffer con los datos de la trama
     */
    public TramaNuevaRed(Buffer trama)
    {
        super(trama);
    }
}
