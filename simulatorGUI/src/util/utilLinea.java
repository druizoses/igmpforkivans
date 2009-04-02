/** @author: tlfs & afzs */
package util;

import java.awt.Point;

/** Clase para dibujado de lineas de conexion de la topologia */
public class utilLinea
{
	/** Coordenadas inicial y final de la linea */
	private Point cinicial, cfinal;

	/** Constructor de utilLinea */
	public utilLinea()
	{
		cinicial = new Point();
		cfinal = new Point();
	}
	
	/** Constructor de utilLinea a partir de 4 puntos
	 * @param x1 Coordenada x del inicio de la linea
	 * @param y1 Coordenada y del inicio de la linea
	 * @param x2 Coordenada x del fin de la linea
	 * @param y2 Coordenada y del fin de la linea
	 */
	public utilLinea(int x1, int y1, int x2, int y2)
	{
		cinicial = new Point(x1, y1);
		cfinal = new Point(x2, y2);
	}
	
	/** Constructor de copia
	 * @param copia utilLinea desde el que construimos el nuevo objeto utilLinea
	 */
	public utilLinea(utilLinea copia)
	{
		cinicial = new Point(copia.cinicial);
		cfinal = new Point (copia.cfinal);
	}
	
	/** Devuelve el valor de la coordenada x
	 * @param x Si 1 devuelve la coordenada inicial
	 * @return int con el valor de la coordenada
	 */
	public int getX(int x)
	{
		if (x==1)
			return (int)cinicial.getX();
		return (int)cfinal.getX();
	}

	/** Devuelve el valor de la coordenada y
	 * @param y Si 1 devuelve la coordenada inicial
	 * @return int con el valor de la coordenada
	 */
	public int getY(int y)
	{
		if (y==1)
			return (int)cinicial.getY();
		return (int)cfinal.getY();
	}
	
	/** Establece la coordenada x
	 * @param valor Nuevo valor de la coordenada x
	 * @param x Si 1 entonces modifica la coordenada inicial
	 */
	public void setX(int valor, int x)
	{
		if (x==1)
			cinicial.move(valor, (int)cinicial.getY());
		else
			cfinal.move(valor, (int)cfinal.getY());
	}

	/** Establece la coordenada y
	 * @param valor Nuevo valor de la coordenada y
	 * @param y Si 1 entonces modifica la coordenada inicial
	 */
	public void setY(int valor, int y)
	{
		if (y==1)
			cinicial.move((int)cinicial.getX(), valor);
		else
			cfinal.move((int)cfinal.getX(), valor);
	}
}
