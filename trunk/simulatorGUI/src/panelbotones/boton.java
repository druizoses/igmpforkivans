/** @author: tlfs & afzs */
package panelbotones;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import util.colores;

/** Clase boton para panel botones */
class boton extends JLabel
{
	private String nombre;
	private String nomicono;
	
	private boolean selec;

	/** Constructor de boton
	 * @param nombre nombre del boton
	 */
	public boton(String nombre)
	{
		setName(nombre);
		setBorder(new javax.swing.border.EtchedBorder());
		setBackground(colores.fondoBarras);
		
		this.nombre = nombre;
	}
	
	/** funcion getIcono para devolver la ruta del icono
	 * devuelve String la ruta 
	 */
	public String getIcono()
	{
		return nomicono;
	}
	
	/** funcion getNombre
	 * devuelve String el nombre del boton
	 */
	public String getNombre()
	{
		return nombre;
	}
	
	/** funcion getSelec
	 * devuelve boolean si esta o no seleccionado el boton */
	public boolean getSelec()
	{
		return selec;
	}
	
	/** funcion setBordeNormal establece el borde */
	public void setBordeNormal()
	{
		setBorder(new javax.swing.border.EtchedBorder());
		selec = false;
	}
	
	/** funcion setBordeSelec establece el borde cuando esta seleccionado */
	public void setBordeSelec()
	{
		setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 204, 0), 1, true));
		selec = true;
	}
	
	/** funcion setCoord establece las coordenadas del boton
	 * @param x
	 * @param y coordenadas
	 */
	public void setCoord(int x, int y)
	{
		setBounds(x, y, util.nomiconos.tam+2, util.nomiconos.tam);
	}
	
	/** funcion setIcono establece el icono
	 * @param nomicono ruta del icono
	 */
	public void setIcono(String nomicono)
	{
		setIcon(new ImageIcon(nomicono));
		this.nomicono = nomicono;
	}

	/** funcion setTip pone el nombre del boton en el cursor */
	public void setTip()
	{
		setToolTipText((new util.nomiconos().getNombre(getIcono())));
	}
}