/** @author: tlfs & afzs */
package gmenu;

import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenu;
import javax.swing.KeyStroke;

import util.colores;

/** Clase gmSimula para el menu Simula */
class gmSimula extends JMenu
{
	/** opcion simular envios del menu */
	private gmOpcionMenu gmsimula;
	/** opcion simular paso a paso */
	private gmOpcionMenu gmsimulapaso;
	/** opcion configurar envios del menu */
	private gmOpcionMenu gmenvios;
	/** opcion comprobar simulacion del menu */
	private gmOpcionMenu gmcomprueba;
	/** opcion mostrar sucesos de la simulacion del menu */
	private gmOpcionMenu gmEventosSimulacion;
	/** opcion detener simulacion del menu */
	private gmOpcionMenu gmdetener;

	/** Constructor gmSimula
	 * @param oyente
	 * @param cade el estado actual
	 */
	public gmSimula(ActionListener oyente, MouseListener oyenteRaton, String cadena)
	{
		// se establece el texto y la tecla que se subraya al presionar alt
		setText("Simulador");
		setMnemonic('S');
		setBackground(colores.fondoBarras);
		
		// a cada opcion del menu le ponemos su texto, teclas rapidas, oyente, estados en los que se habilita,
		// estado actual y letra que se subraya al presionar alt
		// y se anyaden al menu Simula

		gmcomprueba = new gmOpcionMenu();
		gmcomprueba.estableceValores("Comprobar Simulacion", KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK), oyente, oyenteRaton, ",13,", cadena,'C');
		add(gmcomprueba);

		gmenvios = new gmOpcionMenu();
		gmenvios.estableceValores("Configurar Envios", null, oyente,oyenteRaton,  ",14,", cadena,'E');
		add(gmenvios);

		gmsimula = new gmOpcionMenu();
		gmsimula.estableceValores("Simular Envios", KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK), oyente,oyenteRaton,  ",15,", cadena,'S');
		add(gmsimula);

		gmsimulapaso = new gmOpcionMenu();
		gmsimulapaso.estableceValores("Simular paso a paso", KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK), oyente,oyenteRaton,  ",15,", cadena,'p');
		add(gmsimulapaso);

		gmdetener = new gmOpcionMenu();
		gmdetener.estableceValores("Detener simulacion", KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_MASK), oyente,oyenteRaton,  ",16,", cadena,'D');
		add(gmdetener);

		gmEventosSimulacion = new gmOpcionMenu();
		gmEventosSimulacion.estableceValores("Mostrar Sucesos de la Simulacion", KeyStroke.getKeyStroke(KeyEvent.VK_M, InputEvent.CTRL_MASK), oyente,oyenteRaton,  ",17,", cadena,'v');
		add(gmEventosSimulacion);
	}

	/** funcion habilita que permite habilitar y deshabilitar las opciones del menu
	 * @param esta estado actual
	 */
	public void habilita(String esta)
	{
		gmcomprueba.establece(",13,",esta);
		gmenvios.establece(",14,",esta);
		gmsimula.establece(",15,",esta);
		gmsimulapaso.establece(",15,",esta);
		gmdetener.establece(",16,",esta);
		gmEventosSimulacion.establece(",17,",esta);
	}
}
