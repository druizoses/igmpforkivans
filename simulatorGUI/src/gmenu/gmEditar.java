/** @author: tlfs & afzs */
package gmenu;

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import util.colores;

/** Clase gmEditar para el menu Editar */
class gmEditar extends JMenu
{
	/** opcion copiar del menu*/
	private gmOpcionMenu gmcopia;
	/** opcion pegar del menu*/
	private gmOpcionMenu gmpega;
	/** opcion cortar del menu*/
	private gmOpcionMenu gmcorta;
	/** opcion eliminar del menu*/
	private gmOpcionMenu gmelimina;

	/** opcion quitar seleccion del menu*/
	private gmOpcionMenu gmquitasel;
	/** opcion seleccionar todo del menu*/
	private gmOpcionMenu gmselecciona;

	/** opcion centrar ambos del menu*/
	private gmOpcionMenu gmcentra;
	/** opcion centrar horizontal del menu*/
	private gmOpcionMenu gmcentrah;
	/** opcion centrar vertical del menu*/
	private gmOpcionMenu gmcentrav;

	/** Constructor gmEditar
	 * @param oyente
	 * @param cade el estado actual
	 */
	public gmEditar(ActionListener oyente, MouseListener oyenteRaton, String cade)
	{
		// se establece el texto y la tecla que se subraya al presionar alt
		setText("Editar");
		setMnemonic('E');
		setBackground(colores.fondoBarras);

		// a cada opcion del menu le ponemos su texto, teclas rapidas, oyente, estados en los que se habilita,
		// estado actual y letra que se subraya al presionar alt
		// y se anyaden al menu Editar

		gmcopia= new gmOpcionMenu();
		gmcopia.estableceValores("Copiar", KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK), oyente, oyenteRaton, ",1,7,8,", cade,'C');
		add(gmcopia);

		gmpega= new gmOpcionMenu();
		gmpega.estableceValores("Pegar", KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK), oyente,oyenteRaton, ",3,4,5,", cade,'P');
		add(gmpega);

		gmcorta= new gmOpcionMenu();
		gmcorta.estableceValores("Cortar", KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK), oyente,oyenteRaton, ",1,7,8,", cade,'r');
		add(gmcorta);

		gmelimina= new gmOpcionMenu();
		gmelimina.estableceValores("Eliminar", KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), oyente,oyenteRaton, ",1,7,8,", cade,'E');
		add(gmelimina);

		add(new JSeparator());

		gmselecciona= new gmOpcionMenu();
		gmselecciona.estableceValores("Seleccionar todo", KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK), oyente,oyenteRaton, ",10,", cade,'S');
		add(gmselecciona);

		gmquitasel= new gmOpcionMenu();
		gmquitasel.estableceValores("Quitar seleccion", KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK), oyente,oyenteRaton, ",1,7,8,", cade,'Q');
		add(gmquitasel);

		add(new JSeparator());

		gmcentrav= new gmOpcionMenu();
		gmcentrav.estableceValores("Centrar vertical", KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_MASK), oyente,oyenteRaton, ",1,7,8,", cade,'v');
		add(gmcentrav);

		gmcentrah= new gmOpcionMenu();
		gmcentrah.estableceValores("Centrar horizontal", KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK), oyente,oyenteRaton, ",1,7,8,", cade,'h');
		add(gmcentrah);

		gmcentra= new gmOpcionMenu();
		gmcentra.estableceValores("Centrar ambos", KeyStroke.getKeyStroke(KeyEvent.VK_K, InputEvent.CTRL_MASK), oyente,oyenteRaton, ",1,7,8,", cade,'a');
		add(gmcentra);
	}
	

	/** funcion habilita que permite habilitar y deshabilitar las opciones del menu
	 * @param esta estado actual
	 */
	public void habilita(String esta)
	{
		gmcopia.establece(",1,7,8,",esta);
		gmpega.establece(",3,4,5,",esta);
		gmcorta.establece(",1,7,8,",esta);
		gmelimina.establece(",1,7,8,",esta);
		
		gmselecciona.establece(",10,",esta);
		gmquitasel.establece(",1,7,8,",esta);

		gmcentrav.establece(",1,7,8,",esta);
		gmcentrah.establece(",1,7,8,",esta);
		gmcentra.establece(",1,7,8,",esta);
	}
}
