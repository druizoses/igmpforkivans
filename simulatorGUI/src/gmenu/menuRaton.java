/** @author: tlfs & afzs */
package gmenu;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;

/** Clase menuRaton para el menu del boton derecho del raton */
public class menuRaton extends JPopupMenu
{
	/** menu editar para el raton */
	private gmEditar ed;
	/** menu equipo para el raton */
	private gmEquipo eq;

	/** Constructor del menuRaton
	 * @param oyente para capturar los eventos que se produzcan
	 * @param cadena estado actual
	 */
	public menuRaton(ActionListener oyente, MouseListener oyenteRaton, String cadena)
	{
		// se crean los menus
		ed= new gmEditar(oyente, oyenteRaton, cadena);
		eq= new gmEquipo(oyente, oyenteRaton, cadena);

		// se anyaden los menus 
		add(ed);
		add(eq);
	}
	
	/** Funcion habilitaMenu para habilitar y deshabilitar los submenus
	 * @param estado es el estado actual
	 */
	public void habilitaMenu(String estado)
	{
		ed.habilita(estado);
		eq.habilita(estado);
	}

	/** Funcion mostrar para visualizar el menu
	 * @param compo el componente que se muestra
	 * @param x
	 * @param y coordenadas
	 */
	// Seria interesante encontrar una forma mejor de situar el menu.
	// Si el texto es otro distinto a Opciones hay que volver a ajustar
	public void mostrar(java.awt.Component compo, int x, int y)
	{
		show(compo, (int)(getBounds().getWidth()+x-20), (int)(getBounds().getHeight()+y-20));
	}
}
