/** @author: tlfs & afzs */
package objetoVisual;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Vector;

import util.colores;
import util.nomiconos;
import visSim.panelErrores;

/** Clase que implementa un anillo visual */
public class anilloVisual extends objetoVisual
{
	/** Constructor de la clase.
	 * Inicializa el Vector conectables para conexiones con otros equipos
	 */
	public anilloVisual()
	{
		conectables = new Vector();
		errores = new Vector();
		
		// Activamos errores por defecto
		// Se tendran que poner en panelErrores
		panelErrores panel = new panelErrores(errores);
		errores = new Vector(panel.getSeleccionesErrores());

		conectables.add("hu");	// hub
		conectables.add("pc");	// pc
		conectables.add("pu");	// puente
		conectables.add("ro");	// router
		conectables.add("wa");  // wan
	}

	/** Metodo que implementa el metodo clone de la clase */
	public Object clone()
	{
		anilloVisual temp = new anilloVisual();

		temp.conexiones = new listaConexiones();
		
		temp.set(getNombre(), getX(), getY(), getWidth(), getHeight());
		temp.setConexiones(getConexiones());
		temp.setConecta(getConecta());

		return temp;
	}
	
	/** Metodo que implementa la representacion visual de un anillo
	 * @see objetoVisual.objetoVisual#dibuja(boolean)
	 */
	public Image dibuja(boolean grabando)
	{
		int ancho = getWidth();
		
		BufferedImage bi = new BufferedImage(ancho+1, ancho+1, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (java.awt.Graphics2D)bi.getGraphics();

		g.setPaintMode();

		g.setColor(colores.blanco);
		g.fillOval(0,0, ancho, ancho);

		// Ponemos color de circulo segun el estado
		g.setColor(colores.negro);

		if (!grabando)
		{
			if (getSeleccionado())
			{
				// Ponemos cuadrados para cambiar tamanyo
				g.setColor(colores.cuadrado);
			
				g.drawRect(ancho-4, 0, 4, 4);
				g.drawRect(0,0, 4, 4);
				g.drawRect(0, ancho-4, 4, 4);
				g.drawRect(ancho-4, ancho-4, 4, 4);

				g.setColor(colores.seleccionado);
			}
			else if (getConecta())
				g.setColor(colores.conecta);
		}

		g.drawOval(0,0, ancho, ancho);

		return bi;
	}

  	public String getCadenaSimulador()
	{
		return "Anillo.Anillo";
	}

	public String getNomIcono()
	{
		return nomiconos.nomAnillo;
	}
	
	public void setHeight(int valor)
	{
		int num=0;
		
		num = 10;
		if (valor>=num)
			num = valor;

		setBounds(getX(), getY(), getWidth(), num);
	}

	public void setWidth(int valor)
	{
		int num=0;
		
		num = 10;
		if (valor>=num)
			num = valor;
		
		setBounds(getX(), getY(), num, getHeight());
	}
}
