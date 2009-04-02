/** @author: tlfs & afzs */
package gmenu;

import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JSeparator;

import util.colores;

/** Clase gmVentana para el menu Ventana */
class gmVentana extends JMenu
{
	/** oyente para carturar los eventos */
	private ActionListener oyente;
	private MouseListener oyenteRaton;
	/** vector de las opciones del menu */
	private Vector vectorOpciones;

	/** opcion cascada del menu */
	private gmOpcionMenu gmcasc;
	/** opcion mosaico horizontal */
	private gmOpcionMenu gmmosh;
	/** opcion mosaico vertical */
	private gmOpcionMenu gmmosv;

	/** opcion cerrar todas del menu */
	private gmOpcionMenu gmcerrt;
	/** opcion minimizar todas del menu */
	private gmOpcionMenu gmminit;
	
	/** variable para separar las opciones del menu */
	private gmOpcionMenu gmsepara;
	/** variable para separar las opciones del menu */
	private gmOpcionMenu gmsepara2;

	/** Constructor gmVentana
	 * @param oyente
	 * @param cade el estado actual
	 * @param listaVentanas la lista de ficheros que hay abiertos
	 */
	public gmVentana(ActionListener oyente, MouseListener oyenteRaton, String cadena, Vector listaVentanas)
	{
		vectorOpciones= new Vector();
		setBackground(colores.fondoBarras);
		
		this.oyente = oyente;
		this.oyenteRaton = oyenteRaton;
		
		// se establece el texto y la tecla que se subraya al presionar alt
		setText("Ventana");
		setMnemonic('V');

		// a cada opcion del menu le ponemos su texto, teclas rapidas, oyente, estados en los que se habilita,
		// estado actual y letra que se subraya al presionar alt
		// y se anyaden a un vector auxiliar

		gmcasc= new gmOpcionMenu();
		gmcasc.estableceValores("Cascada", null, oyente, oyenteRaton, ",12,", cadena,'C');
		vectorOpciones.addElement(gmcasc);
	
		gmmosh= new gmOpcionMenu();
		gmmosh.estableceValores("Mosaico Horizontal", null, oyente,oyenteRaton, ",12,", cadena,'H');
		vectorOpciones.addElement(gmmosh);

		gmmosv= new gmOpcionMenu();
		gmmosv.estableceValores("Mosaico Vertical", null, oyente,oyenteRaton, ",12,", cadena,'V');
		vectorOpciones.addElement(gmmosv);

		gmsepara= new gmOpcionMenu();
		gmsepara.estableceValores("Separador", null, oyente,oyenteRaton, "", cadena,' ');
		vectorOpciones.addElement(gmsepara);

		gmminit= new gmOpcionMenu();
		gmminit.estableceValores("Minimizar todas", null, oyente,oyenteRaton, ",12,", cadena,'M');
		vectorOpciones.addElement(gmminit);

		gmcerrt= new gmOpcionMenu();
		gmcerrt.estableceValores("Cerrar todas", null, oyente,oyenteRaton, ",12,", cadena,'t');
		vectorOpciones.addElement(gmcerrt);

		gmsepara2= new gmOpcionMenu();
		gmsepara2.estableceValores("Separador", null, oyente,oyenteRaton, "", cadena,' ');
		vectorOpciones.addElement(gmsepara2);
		
		cargaMenu(listaVentanas);
	}

	/** Funcion cargaMenu va recorriendo todos los elementos del vector y los va anyadiendo
	 * al menu ventana como tambien la lista de ficheros abiertos
	 * @param listaVentanas la lista de ficheros abiertos
	 */
	private void cargaMenu(Vector listaVentanas)
	{
		int i;
		
		gmOpcionMenu mnuItem = new gmOpcionMenu();
		
		for(i=0; i<vectorOpciones.size(); i++)
		{
			mnuItem=(gmOpcionMenu)(vectorOpciones.elementAt(i));
			if( (mnuItem.getText()).compareTo("Separador")==0 )
				add(new JSeparator());
			else	
				add( mnuItem );
		}
		
		pasaLista(listaVentanas);
	}
	
	/** funcion habilita que permite habilitar y deshabilitar las opciones del menu
	 * @param esta estado actual
	 */
	public void habilita(String esta)
	{
		gmcasc.establece(",12,",esta);
		gmmosh.establece(",12,",esta);
		gmmosv.establece(",12,",esta);
		
		gmminit.establece(",12,",esta);
		gmcerrt.establece(",12,",esta);
	}
	
	/** Funcion pasaLista actualiza la lista de ficheros abiertos
	 * @param listaVentanas la lista de ficheros abiertos
	 */
	public void pasaLista(Vector listaVentanas)
	{
		// se crea la opcion del menu
		gmOpcionMenu mnuItem = new gmOpcionMenu();
		
		int tamIni = getItemCount();
		char a;
		
		// Eliminamos la lista anterior
		for (int i=vectorOpciones.size(); i<tamIni; i++)
			remove(vectorOpciones.size());
		
		// y ponemos la nueva
		for (int i=0; i<listaVentanas.size(); i++)
		{
			mnuItem = new gmOpcionMenu();
			
			a= new Integer(i+1).toString().charAt(0);
			mnuItem.estableceValores("[" + (i+1) + "] " + (String)listaVentanas.elementAt(i), null, oyente,oyenteRaton, ",0,", "0-1-9-",a);
			add (mnuItem);
		}
	}
}
