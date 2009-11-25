/** @author: tlfs & afzs */
package util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;

/** Clase para representar graficamente cadenas de texto */
public class utilTexto
{
	
	/** Anchura y altura del texto */
	private int ancho, alto;

	/** Texto */
	private String texto;
	/** Coordenadas del texto */
	private int x, y;
	
	/** Constructor por defecto
	 * @param texto
	 * @param x
	 * @param y
	 * @param ancho
	 * @param alto
	 */
	public utilTexto(String texto, int x, int y, int ancho, int alto)
	{
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.texto = texto;
	}
	
	/** Objeto Image representando el texto */
	public Image dibuja()
	{
		BufferedImage bi = new BufferedImage(getAncho(), getAlto(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = (Graphics2D)bi.getGraphics();

		g.setColor(colores.blanco);
		g.fillRect(0,0, getAncho(), getAlto());
		g.setColor(colores.negro);

		g.drawString(texto, 0, 10);

		return bi;
	}	
	
	/** Altura del texto
	 * @return int
	 */
	public int getAlto()
	{
		return alto;
	}
	
	/** Anchura del texto
	 * @return int
	 */
	public int getAncho()
	{
		return ancho;
	}
	
	
	/** Texto de utilTexto
	 * @return String
	 */
	public String getTexto()
	{
		return texto;
	}
	
	/** Coordenada x del texto
	 * @return int
	 */
	public int getX()
	{
		return x;
	}
	
	/** Coordenada y del texto
	 * @return int
	 */
	public int getY()
	{
		return y;
	}
	
	public static String convertToTime(int segundos)
	{
		Calendar cal = new GregorianCalendar(2000,1,1,0,0,0);
		cal.add(Calendar.MILLISECOND, segundos * 10);
		DateFormat dfm = new SimpleDateFormat("HH:mm:ss.SSS");
		String s = dfm.format(cal.getTime()); 
		return s.substring(0, s.length()-1);
	}
	
}	
