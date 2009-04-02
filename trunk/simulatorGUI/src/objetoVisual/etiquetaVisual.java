/** @author: tlfs & afzs */
package objetoVisual;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JOptionPane;

import util.colores;
import util.nomiconos;

/** Clase que implementa una etiqueta visual */
public class etiquetaVisual extends objetoVisual
{
	/** Constructor de la clase.
	 * Una etiqueta no puede conectarse a nada
	 */
	public etiquetaVisual()
	{
		conectables = new Vector();
		errores = new Vector();
	}

	/** Metodo que implementa el metodo clone de la clase */
	public Object clone()
	{
		etiquetaVisual temp = new etiquetaVisual();

		temp.conexiones = new listaConexiones();
		
		temp.set(getNombre(), getX(), getY(), getWidth(), getHeight());
		temp.setConexiones(getConexiones());
		temp.setConecta(getConecta());

		return temp;
	}

	/** Metodo que implementa la representacion visual de una etiqueta */
	public Image dibuja(boolean grabando)
	{
		int tamx = getWidth(getNombre());
		int tamy = getHeight();
		
		// Cambiamos ancho y alto para que coja los eventos si cambia la cadena
		setHeight(tamy);
		setWidth(tamx);
		
		BufferedImage bi = new BufferedImage(tamx, tamy, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (java.awt.Graphics2D)bi.getGraphics();

		g.setPaintMode();
		g.setColor(colores.blanco);
		g.fillRect(0,0, tamx, tamy);
		
		if (!grabando)
		{
			// Dibujamos lineas de seleccion
			if (getSeleccionado())
				g.setColor(colores.seleccionado);
			else if (getConecta())
				g.setColor(colores.conecta);

			g.drawLine(0,0, tamx, 0);
			g.drawLine(0, tamy-1, tamx, tamy-1);
			g.drawLine(0,0, 0, tamy-1);
			g.drawLine(tamx-1, 0, tamx-1, tamy-1);
		}
		
		g.setColor(colores.negro);

		g.drawString(getNombre(), 1, 10);

		return bi;
	}
	
	public String getCadena(String viejonombre)
	{
		String nuevonombre = JOptionPane.showInputDialog("Introduzca el texto de la etiqueta", viejonombre);
		
		if (nuevonombre == null || nuevonombre.length()==0)
			nuevonombre = viejonombre;

		return nuevonombre;
	}

  	public String getCadenaSimulador()
	{
		return "";
	}

	public int getHeight()
	{
		Font fuente = new Font("Dialog.plain", -1, 12);
		FontMetrics metrica = this.getFontMetrics(fuente);

		return (new Integer(metrica.getAscent())).intValue();
	}
	
	public String getNomIcono()
	{
		return nomiconos.nomEtiqueta;
	}
	
	public int getWidth(String cadena)
	{
		Font fuente = new Font("Dialog.plain", -1, 12);
		FontMetrics metrica = this.getFontMetrics(fuente);

		return (new Integer(metrica.stringWidth(cadena))).intValue()+5;
	}
}
