package Redes.NuevaRed;

import Redes.Direccion;

/**
 * Direccion Enlace
 */
public class DireccionNuevaRed extends Direccion
{
    /**
     * Constructor en base a una cadena de texto
     * @param dir Direccion de enlace
     * @throws IllegalArgumentException si la cadena de texto no se puede convertir
     *         a una direccion MAC
     */
    public DireccionNuevaRed(String dir) throws IllegalArgumentException
    {
    }
        
    
    
    /**
     * Constructor
     * @param direccion Direccion de la misma longitud (n bytes)
     * @throws IllegalArgumentException si la direccion indicada no es compatible
     */
    public DireccionNuevaRed(Direccion direccion) throws IllegalArgumentException
	{
    }
    
    
    
    /**
     * Convierte la direccion a una cadena de texto
     * @return Direccion en formato de cadena de texto
     */
    public String toString()
	{
    }
}
