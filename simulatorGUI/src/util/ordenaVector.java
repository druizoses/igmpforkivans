/** @author: tlfs & afzs */
package util;

import java.util.Vector;

/** Clase que ordena un vector mediante el metodo Burbuja */
public class ordenaVector
{
	/** Devuelve el vector ordenado */
	public static Vector getOrdenado(Vector origen)
	{
		int i, j;
		String nombre;
		Vector destino = new Vector(origen);
		int tam = destino.size();
		
		// Metodo Burbuja de ordenacion
		for (i=0; i<tam; i++)
			for (j=i+1; j<tam; j++)
				if (((String)destino.elementAt(i)).compareTo((String)destino.elementAt(j))>0)
				{
					nombre = (String)destino.elementAt(i);
					destino.setElementAt(destino.elementAt(j), i);
					destino.setElementAt(nombre, j);
				}
		
		return destino;
	}

	/** Metodo utilizado al capturar los eventos de la simulacion 
	 * La ordenacion se lleva a cabo segun el numero de evento. 
	 */
	public static Vector getOrdenadoNumero(Vector origen)
	{
		int i, j, numero1;
		String nombre;
		Vector destino = new Vector(origen);
		int tam = destino.size();
		
		// Metodo Burbuja de ordenacion
		for (i=0; i<tam; i++)
			for (j=i+1; j<tam; j++)
			{
				nombre = (String)destino.elementAt(i);
				nombre = nombre.substring(0, nombre.indexOf("\t"));
				numero1 = new Integer(nombre).intValue();

				nombre = (String)destino.elementAt(j);
				nombre = nombre.substring(0, nombre.indexOf("\t"));
				
				if (numero1>=new Integer(nombre).intValue())
				{
					nombre = (String)destino.elementAt(i);
					destino.setElementAt(destino.elementAt(j), i);
					destino.setElementAt(nombre, j);
				}
			}

		return destino;
	}
}
