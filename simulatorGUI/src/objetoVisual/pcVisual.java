/** @author: tlfs & afzs */
package objetoVisual;

import java.util.Vector;

import util.nomiconos;
import visSim.panelErrores;

/** Clase que implementa un modem visual */
public class pcVisual extends objetoVisual
{
	/** Constructor de la clase.
	 * Inicializa el Vector conectables para conexiones con otros equipos
	 */
	public pcVisual()
	{
		conectables = new Vector();
		errores = new Vector();
		
		// Activamos errores por defecto
		// Se tendran que poner en panelErrores
		panelErrores panel = new panelErrores(errores);
		errores = new Vector(panel.getSeleccionesErrores());
		
		conectables.add("et");	// bus
		conectables.add("hu");	// hub
		conectables.add("mo");	// modem
		conectables.add("pu");	// puente
		conectables.add("ro");	// router
		conectables.add("sw");	// switch
		conectables.add("an");	// tokenring
		conectables.add("wa");  // wan
	}

	/** Metodo que implementa el metodo clone de la clase */
	public Object clone()
	{
		pcVisual temp = new pcVisual();

		temp.conexiones = new listaConexiones();
		
		temp.set(getNombre(), getX(), getY(), getWidth(), getHeight());
		temp.setConexiones(getConexiones());
		temp.setConecta(getConecta());
		
		return temp;
	}

  	public String getCadenaSimulador()
	{
		return "Ordenador";
	}

	public String getNomIcono()
	{
		return nomiconos.nomPC;
	}
}
