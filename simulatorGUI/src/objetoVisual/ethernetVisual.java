/** @author: tlfs & afzs */
package objetoVisual;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Vector;
import util.colores;
import util.nomiconos;
import visSim.panelErrores;

/** Clase que implementa una ethernet visual */
public class ethernetVisual extends objetoVisual
{
	/** Constructor de la clase.
	 * Inicializa el Vector conectables para conexiones con otros equipos
	 */
	public ethernetVisual()
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
		conectables.add("sw");	// switch
		conectables.add("wa");  // wan
	}

	/** Metodo que implementa el metodo clone de la clase */
	public Object clone()
	{
		ethernetVisual temp = new ethernetVisual();

		temp.conexiones = new listaConexiones();
		
		temp.set(getNombre(), getX(), getY(), getWidth(), getHeight());
		temp.setConexiones(getConexiones());
		temp.setConecta(getConecta());

		return temp;
	}
	
	public Image dibuja(boolean grabando)
	{
		int coor = 0;
		int ancho = getWidth();
		int alto = getHeight();
		
		BufferedImage bi = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (java.awt.Graphics2D)bi.getGraphics();

		g.setPaintMode();
		g.setColor(colores.fondo);
		g.fillRect(0,0, 100, 100);

		g.setColor(colores.negro);
		if (getConecta())
			g.setColor(colores.conecta);

		// Bus horizontal
		if (ancho!=5)
		{
			if (getSeleccionado() && !grabando)
			{
				g.setColor(colores.cuadrado);
				g.drawRect(0,0, 3, alto-1);
				g.drawRect(ancho-4,0, 3, alto-1);

				g.setColor(colores.seleccionado);
				
				coor = 4;
			}
			else
			{
				g.drawLine(0,0, 0, 5);
				g.drawLine(ancho-1, 0, ancho-1, 5);
			}
			
			// Dibujamos las 3 lineas que componen el bus
			for (int i=3; i>=1; i--)
				g.drawLine(coor,i, ancho-coor-1, i);
		}
		// Bus vertical
		else
		{
			if (getSeleccionado() && !grabando)
			{
				g.setColor(colores.cuadrado);
				g.drawRect(0,0, ancho-1, 3);
				g.drawRect(0,alto-4, ancho-1, 3);

				g.setColor(colores.seleccionado);
				
				coor = 4;
			}
			else
			{
				g.drawLine(0,0, 5, 0);
				g.drawLine(0,alto-1, 5, alto-1);
			}

			// Dibujamos las 3 lineas que componen el bus
			for (int i=3; i>=1; i--)
				g.drawLine(i,coor, i,alto-coor-1);
		}

		return bi;
	}

	public String getCadenaSimulador()
	{
		return "Ethernet.Ethernet";
	}
	
	/** Metodo que devuelve un objeto Point con las coordenadas a las que se debe conectar un
	 * equipo que se encuentre conectado a la ethernet
	 * @param xOtro Coordenada x del equipo que conecta
	 * @param yOtro Coordenada y del equipo que conecta
	 * @param anchoOtro Ancho del equipo que conecta
	 * @param altoOtro Altura del equipo que conecta
	 */
	public Point getCoordenadasConexion(int xOtro, int yOtro, int anchoOtro, int altoOtro)
	{
		Point dev = new Point();
		int x1 = getX();
		int y1 = getY();
		int y2 = getHeight();
		int x2 = getWidth();
		
		if (x2==5)
			x2 = x1;
		else
		{
			x2 -= x1;
			if (x2 < x1)
				x2 = -x2;
		}
		
		if (y2==5)
			y2 = y1;
		else
		{
			y2 -= y1;
			
			if (y2 <= y1)
				y2 = -y2;
		}

		//Bus horizontal
		if (y1==y2)
		{
			if (xOtro+anchoOtro/2 < x1)
				dev.setLocation(x1, y1+1);
			else if (xOtro+anchoOtro/2>=x1+getWidth())
				dev.setLocation(x1+getWidth(), y1+1);
			else
				dev.setLocation(xOtro+anchoOtro/2, y1+1);
		}
		else
		{
			if (yOtro+altoOtro/2 < y1)
				dev.setLocation(x1+1, y1);
			else if (yOtro+altoOtro/2>=y1+getHeight())
				dev.setLocation(x1+1, y1+getHeight());
			else
				dev.setLocation(x1+1, yOtro+altoOtro/2);
		}
		
		return dev;
	}

	public String getNomIcono()
	{
		return nomiconos.nomEthernet;
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
