/** @author: tlfs & afzs */
package gmenu;

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.KeyStroke;

import util.colores;

/** Clase gmAyuda para el menu Ayuda */
class gmAyuda extends JMenu
{
	/** opcion acerca de del menu */
	private gmOpcionMenu gmacerca;
	/** opcion contenido del menu*/
	private gmOpcionMenu gmconte;
	
	/** Constructor gmAyuda
	 * @param oyente
	 * @param cade el estado actual
	 */
	public gmAyuda(ActionListener oyente, MouseListener oyenteRaton, String cade)
	{
		// se establece el texto y la tecla que se subraya al presionar alt
		setText("Ayuda");
		setMnemonic('y');
		setBackground(colores.fondoBarras);
		
		// Submenus del menu Ayuda
		// a cada opcion del menu le ponemos su texto, teclas rapidas, oyente, estados en los que se habilita,
		// estado actual y letra que se subraya al presionar alt
		// y se anyaden al menu Archivo
		
		gmconte= new gmOpcionMenu();
		gmconte.estableceValores("Contenido", KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), oyente, oyenteRaton, ",99,", cade,'C');
		add(gmconte);

		gmacerca= new gmOpcionMenu();
		gmacerca.estableceValores("Acerca de", KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.CTRL_MASK), oyente, oyenteRaton,  ",99,", cade, 'A');
		add(gmacerca);
	}


	/** funcion habilita que permite habilitar y deshabilitar las opciones del menu
	 * @param esta estado actual
	 */
	public void habilita(String esta)
	{
		gmconte.establece(",99,",esta);
		gmacerca.establece(",99,",esta);
	}
}
