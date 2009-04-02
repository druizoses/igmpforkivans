/** @author: tlfs & afzs */
/** Contiene la estructura general de los menus del programa */
package gmenu;

import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenuBar;

import util.colores;
import java.awt.event.MouseListener;

/** Clase gmenu para hacer los menus */
public class gmenu extends JMenuBar
{
	/** menu Archivo */
	private gmArchivo ar;
	/** menu editar */
	private gmEditar ed;
	/** menu equipo */
	private gmEquipo eq;
	/** menu simulador */
	private gmSimula si;
	/** menu ventana */
	private gmVentana ve;
	/** menu ayuda */
	private gmAyuda ay;
	/** menu que aparece con el boton derecho del raton */
	private menuRaton mo;
	
	/** Constructor de la clase
	 * @param oyente para que coja todos los eventos,
	 * @param cadena donde estaran los estados actuales,
	 * @param listaVentanas cada ventana tiene su menu.
	 */
	public gmenu(ActionListener oyente, MouseListener oyenteRaton, String cadena,Vector listaVentanas)
	{
		// se crean los menus
		ar= new gmArchivo(oyente, oyenteRaton, cadena);
		ed= new gmEditar(oyente, oyenteRaton,  cadena);
		eq= new gmEquipo(oyente, oyenteRaton,  cadena);
		si= new gmSimula(oyente, oyenteRaton,  cadena);
		ve= new gmVentana(oyente, oyenteRaton,  cadena,listaVentanas);
		ay= new gmAyuda(oyente, oyenteRaton,  cadena);
		setBackground(colores.fondoBarras);
		
		// se anyaden los menus
		add(ar);
		add(ed);
		add(eq);
		add(si);
		add(ve);
		add(ay);
		
		//  se crea el menu del raton y se anyade
		mo = new menuRaton(oyente, oyenteRaton, cadena);
		add(mo);	
		
	}

	/** Funcion habilitaMenu
	 * @param estado para que habilite o deshabilite segun los estados asignados
	 */
	public void habilitaMenu(String estado)
	{
		ar.habilita(estado);
		ed.habilita(estado);
		eq.habilita(estado);
		si.habilita(estado);
		ve.habilita(estado);
		ay.habilita(estado);
		mo.habilitaMenu(estado);
	}
	
	/** Funcion muestra para ver el menu del raton
	 * @param x
	 * @param y coordenadas donde se muestra el menu en pantalla
	 */
	public void muestra(int x, int y)
	{
		mo.mostrar(getComponent(), x, y);
	}
	
	/** Funcion pasaLista al menu ventana se le pasa la lista de ventanas
	 * @param listaVentanas la lista de las ventanas
	 */
	public void pasaLista(Vector listaVentanas)
	{
		ve.pasaLista(listaVentanas);
	}
}
