/** @author: tlfs & afzs */
package util;

import java.awt.Frame;
import javax.swing.JOptionPane;

/** Clase creada para mostrar un mensaje al usuario */
public class muestraAviso
{
	/** Muestra el mensaje en un JOptionPane
	 * @param mensaje
	 */
	public static void mensaje(Frame padre, String mensaje)
	{
		JOptionPane.showMessageDialog(padre, mensaje, "Aviso", JOptionPane.ERROR_MESSAGE);
	}
	
	public static boolean mensajeConfirmacion(Frame padre, String mensaje)
	{
		int opcion = JOptionPane.showConfirmDialog(padre, mensaje);
		if(opcion == 0)
			return true;
		else
			return false;
	}
}
