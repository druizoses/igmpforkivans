/** @author: tlfs & afzs */
package gmenu;

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.KeyStroke;

import util.colores;

/** Clase gmEquipo para el menu Equipo */
class gmEquipo extends JMenu
{
	/** opcion buscar del menu */
	private gmOpcionMenu gmbusca;
	/** opcion propiedades del menu */
	private gmOpcionMenu gmpropi;

	/** Constructor gmEquipo
	 * @param oyente
	 * @param cade el estado actual
	 */
	public gmEquipo(ActionListener oyente, MouseListener oyenteRaton, String cadena)
	{
		// se establece el texto y la tecla que se subraya al presionar alt
		setText("Equipo");
		setMnemonic('q');
		setBackground(colores.fondoBarras);

		// a cada opcion del menu le ponemos su texto, teclas rapidas, oyente, estados en los que se habilita,
		// estado actual y letra que se subraya al presionar alt
		// y se anyaden al menu Equipo

		gmbusca= new gmOpcionMenu();
		gmbusca.estableceValores("Buscar", KeyStroke.getKeyStroke(KeyEvent.VK_B, InputEvent.CTRL_MASK), oyente, oyenteRaton, ",9,10,", cadena,'B');
		add(gmbusca);

		gmpropi= new gmOpcionMenu();
		gmpropi.estableceValores("Propiedades", KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK), oyente, oyenteRaton, ",8,", cadena,'P');
		add(gmpropi);
	}

	/** funcion habilita que permite habilitar y deshabilitar las opciones del menu
	 * @param esta estado actual
	 */
	public void habilita(String esta)
	{
		gmbusca.establece(",9,10,",esta);
		gmpropi.establece(",8,",esta);
	}
}
