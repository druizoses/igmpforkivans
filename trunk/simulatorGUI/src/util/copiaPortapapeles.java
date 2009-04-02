/** @author: tlfs & afzs */
package util;

import java.util.Vector;

import javax.swing.JTextArea;

/** Clase creada para copiar el contenido del vector recibido al portapapeles
 * @see panelEventos
 * @see dialogoEventos
 */
public class copiaPortapapeles
{
	/** Metodo que copia el contenido del Vector al portapapeles
	 * @param copiar
	 */
	public static void copia(Vector copiar)
	{
		JTextArea areaTexto = new JTextArea("");
			
		for (int i=0; i<copiar.size(); i++)
			areaTexto.append((String)copiar.elementAt(i) + "\n");
		
		// Seleccionamos todo el texto y copiamos
		areaTexto.setSelectionStart(0);
		areaTexto.setSelectionEnd(areaTexto.getText().length());
		areaTexto.copy();
	}
}
