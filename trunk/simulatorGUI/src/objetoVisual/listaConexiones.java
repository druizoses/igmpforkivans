/** @author: tlfs & afzs */
package objetoVisual;

import java.util.Vector;

/** Clase implementada para gestionar las conexiones de los equipos */
public class listaConexiones extends Vector
{
	/** Constructor de la clase */
	public listaConexiones()
	{
	}
	
	/** Elimina un elemento de la lista
	 * @param nombre Nombre cuyo elemento se elimina
	 */
	public void borra(String nombre)
	{
		remove(nombre);
	}
	
	/** Elimina todas los elementos de la lista que coinciden con el nombre recibido */
	public void borraConexionesHacia(String nombre)
	{
		while (removeElement(nombre));
	}
	
	/** Cambia los nombres de las conexiones
	 * @param nombreViejo
	 * @param nombreNuevo
	 */
	public void cambiaNombresConexiones(String nombreViejo, String nombreNuevo)
	{
		for (int i=0; i<size(); i++)
				if (((String)elementAt(i)).compareTo(nombreViejo)==0)
					set(i, nombreNuevo);
	}
	
	/** Constructor de copia a partir de otro objeto listaConexiones */
	public void copia(listaConexiones conexiones)
	{
		removeAllElements();
		new Vector();
		
		for (int i=0; i<conexiones.tam(); i++)
			add(conexiones.elementAt(i));
	}

	/** Constructor de copia a partir de un Vector de conexiones */
	public void copia(Vector conexiones)
	{
		removeAllElements();
		
		new Vector();
		
		for (int i=0; i<conexiones.size(); i++)
			add(conexiones.elementAt(i));
	}

	/** Se eliminan las conexiones a equipos no existentes en la lista.
	 * @param nombres Vector de nombres que se eliminan
	 */
	public void eliminaNoExistentes(Vector nombres)
	{
		// Recorremos todo el vector para eliminar los equipos que no estan en nombres
		for (int i=0; i<size(); )
			if (nombres.contains(elementAt(i)))
				i++;
			else
				removeElementAt(i);
	}
	
	/** Devuelve el nombre de la conexion situada en pos */
	public String getNombre(int pos)
	{
		return (String)elementAt(pos);
	}
	
	/** Inserta una nueva conexion a la lista */
	public void inserta(String nombre)
	{
		add(nombre);
	}
	
	/** Devuelve el tamanyo del vector */
	public int tam()
	{
		return size();
	}

}
