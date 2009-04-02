/** @author: tlfs & afzs */
package frameInternoDibujo;

import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.util.Vector;

import java.awt.Frame;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import objetoVisual.listaObjetos;
import paneldibujo.paneldibujo;
import util.nomiconos;

/**
 * La clase frameInternoDibujo es la encargada de representar visualmente una
 * ventana grafica, la cual va a contener una topologia. Contiene metodos para
 * comunicar las distintas opciones de los menus con los metodos de la clase
 * paneldibujo.
 * 
 * @see paneldibujo
 * @see JInternalFrame
 */
public class frameInternoDibujo extends JInternalFrame
{
	/** Objeto paneldibujo que va a estar contenido en el JInternalFrame */
	private paneldibujo panelDibujo;
	
	/** Constructor de la clase. Declara un objeto de tipo JScrollPane el cual
	 * contendra al objeto paneldibujo que representa la topologia.
	 * @param oyente Manejador de eventos recibido desde visual. 
	 * @param nombre Nombre del fichero que contiene la topologia correspondiente a esta ventana
	 */
	public frameInternoDibujo(ActionListener oyente, Frame padreFrame, String nombre)
	{
		panelDibujo = new paneldibujo(oyente, padreFrame);

		JScrollPane scrollerDibujo = new JScrollPane(panelDibujo, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);
		setTitle(nombre);
		setVisible(true);

		getContentPane().add(scrollerDibujo);
		
		panelDibujo.ponCursor(nomiconos.nomCursor, 0,0);
	}

	/** Busqueda de equipos en la topologia */
	public void busca()
	{
		panelDibujo.busca();
	}
	
	/** Llama al metodo cambiaMenu de paneldibujo */
	public String cambiaMenu()
	{
		return panelDibujo.cambiaMenu();
	}

	/** Centra todos los equipos seleccionados */
	public void centrarAmbos()
	{
		panelDibujo.centrarAmbos();
	}

	/** Centra todos los equipos seleccionados de forma horizontal */
	public void centrarHorizontal()
	{
		panelDibujo.centrarHorizontal();
	}
	
	/** Centra todos los equipos seleccionados de forma vertical */
	public void centrarVertical()
	{
		panelDibujo.centrarVertical();
	}
	
	/** Realiza la comprobacion de la posibilidad de simular con la configuracion actual de la topologia */
	public Vector compruebaSimulacion()
	{
		return panelDibujo.compruebaSimulacion();
	}
	
	/** Muestra el panel de configuracion de envios de tramas */
	public void configuraEnvios(int xCentral, int yCentral)
	{
		panelDibujo.configuraEnvios(xCentral, yCentral);
	}

	/** Metodo para configuracion de la impresora
	 * @param trabajo Documento sobre el que se aplica la configuracion
	 * @param formato Formato de pagina configurado
	 */
	public void configuraImpresion(PrinterJob trabajo, PageFormat formato)
	{
		panelDibujo.configuraImpresion(trabajo, formato);
	}

	/** Crea una lista de objetos que representa aquellos que se van a copiar
	 * @see listaObjetos
	 */
	public void copiaSeleccionados()
	{
		panelDibujo.copiaSeleccionados();
	}

	/** Corta de la topologia los equipos previamente seleccionados */
	public void cortar()
	{
		panelDibujo.cortar();
	}
	
	/** Devuelve una lista de objetos representando aquellos que han sido copiados. 
	 * @see listaObjetos
	 */
	public listaObjetos dameCopias()
	{
		return panelDibujo.dameCopias().copiaLista();
	}
	
	/** Detiene la simulacion */
	public void detenerSimulacion()
	{
		panelDibujo.detenerSimulacion();
	}

	/** Elimina de la topologia aquellos equipos previamente seleccionados */
	public void eliminaSeleccionados()
	{
		panelDibujo.eliminaSeleccionados();
	}
	
	/** Devuelve un boolean indicando si ha habido cambios sobre la topologia
	 * @return boolean
	 */
	public boolean getCambios()
	{
		return panelDibujo.getCambios();
	}
	
	/** Devuelve el nombre del fichero que contiene la topologia */
	public String getFicheroTopo()
	{
		return panelDibujo.getFicheroTopo();
	}

	/** Almacena en fichero la topologia contenida en la ventana y pone en el titulo
	 *  de la ventana el nombre del fichero.
	 * 
	 * @return boolean indicando si el almacenamiento ha sido correcto.
	 */
	public boolean grabaFichero()
	{
		setTitle(getFicheroTopo());
		return panelDibujo.grabaFichero();
	}

	/** Almacena la topologia en un fichero grafico con formato JPG
	 * @param nomfich Nombre del fichero donde se almacena la topologia
	 */
	public void grabaJPG(String nomfich)
	{
		panelDibujo.grabaJPG(nomfich);
	}
	
	/** Metodo para imprimir la topologia */
	public void imprimir()
	{
		panelDibujo.imprimir();
	}
	
	/** Lee de fichero una topologia previamente almacenada y pone en el titulo
	 *  de la ventana el nombre del fichero.
	 * @param nomfich Nombre del fichero que contiene la topologia
	 * @return boolean indicando si la lectura ha sido correcta
	 */
	public boolean leeFichero(String nomfich)
	{
		setTitle(nomfich);
		return panelDibujo.leeFichero(nomfich);
	}
	
	/** Muestra la ventana de eventos de la simulacion */
	public void muestraEventos()
	{
		panelDibujo.muestraEventos();
	}
	
	/** Muestra las propiedades del equipo seleccionado
	 * @param x
	 * @param y Coordenadas en donde se mostrara el cuadro de dialogo
	 * @see propiedades
	 */
	public void muestraPropiedadesEquipo(int x, int y)
	{
		panelDibujo.muestraPropiedadesEquipo(x,y);
	}
	
	public void muestraPropiedadesTopologia()
	{
		panelDibujo.muestraPropiedadesTopologia();
	}

	/** Si valor es true entonces no se dibujan las selecciones (marcos) de los equipos
	 * @see simuGrafico
	 */
	public void noDibujaSelecciones(boolean valor)
	{
		panelDibujo.noDibujaSelecciones(valor);
	}
	
	/** Pega en la topologia los equipos que han sido previamente copiados */
	public void pegar()
	{
		panelDibujo.pegar();
	}
	
	/** Establece la lista de objetos representando aquellos que han sido copiados
	 * en otra topologia o en la actual. 
	 * @see listaObjetos
	 */
	public void ponCopias(listaObjetos lista)
	{
		panelDibujo.ponCopias(lista);
	}
	
	/** Establece el cursor del raton dependiendo del boton sobre el que se haya
	 * hecho click en panelbotones
	 * @param cursorIcono Cadena que contiene la imagen del cursor que se establece
	 * @param xCentral Coordenada x del centro de la pantalla
	 * @param yCentral Coordenada y del centro de la pantalla
	 * @see panelbotones
	 */
	public void ponCursor(String cursorIcono, int xCentral, int yCentral)
	{
		panelDibujo.ponCursor(cursorIcono, xCentral, yCentral);
	}
	
	/** Funcion que envia un mensaje a paneldibujo */
	public void ponMensaje(String cadena)
	{
		panelDibujo.ponMensaje(cadena);
	}
	
	/** Establece el titulo de la ventana */
	public void ponTitulo(String titulo)
	{
		setTitle(titulo);
	}
	
	/** Hace que todos los objetos esten seleccionados o dejen de estarlo dependiendo
	 *  del valor recibido */
	public void seleccionaTodos(boolean valor)
	{
		panelDibujo.seleccionaTodos(valor);
	}

	/** Establece la variable cambios segun el valor recibido */
	public void setCambios(boolean valor)
	{
		panelDibujo.setCambios(valor);
	}
	
	/** Establece el nombre del fichero que contiene la topologia de la ventana
	 * y pone dicho nombre en el titulo de la ventana.
	 * @param nomfich Nombre del fichero de la topologia
	 */
	public void setFicheroTopo(String nomfich)
	{
		setTitle(nomfich);
		panelDibujo.setFicheroTopo(nomfich);
	}
	
	/** Realiza un paso de la simulacion */
	public void simulaPaso()
	{
		panelDibujo.simulaPaso();
	}

	/** Realiza la simulacion completa */
	public void simulaTodo()
	{
		panelDibujo.simulaTodo();
	}

}
