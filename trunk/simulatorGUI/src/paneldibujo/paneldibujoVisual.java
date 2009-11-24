/** @author: tlfs & afzs */
package paneldibujo;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPanel;

import objetoVisual.lgfich;
import objetoVisual.listaObjetos;
import util.ordenaVector;
import visSim.dialogoEventos;
import visSim.simuladorVisual;

/** Clase que se extiende de un JPanel y sirve como base a paneldibujo para
 * la representacion visual de equipos
 */
public class paneldibujoVisual extends JPanel implements Printable
{
	/** Controla si hay cambios en la topologia */
	public boolean cambios;
	
	/** Cadena que codifica el estado actual dependiendo de la topologia */
	public String estadoAct;
	
	/** Cadena que almacena el fichero de la topologia del panel actual */
	public String ficheroTopo;
	
	/** Variables para impresion. Declarandolas aqui es posible configurar la impresion antes de imprimir */
	public PageFormat formatoPagina;

	/** Lista de objetos de la topologia y de los seleccionados para su copia */
	public listaObjetos lista, copias;

	/** Objeto JMenuItem utilizado para el envio de mensajes a visual */
	public JMenuItem mnuitem;

	/** Utilizado para no poner la decoracion de los objetos seleccionados al cambiar de ventana */
	public boolean quitaSelecciones;

	/** Contiene todos los eventos tras la simulacion */
	public Vector salidaEventos;
	
	/** Objeto utilizado para la simulacion de envios de datos */
	public simuladorVisual simulacion;
	
	/** Para saber que ya hemos simulado alguna vez, entonces se activa el boton Mostrar Eventos de Simulacion*/
	private boolean simulado;
	public PrinterJob trabajoImpresion;

	/** Coordenadas centrales de la pantalla */
	public int xCentral, yCentral;
	
	private Frame padreFrame;
	
	private int maxNumeroPasos;
	
	/** Constructor de la clase
	 * Inicializa las variables necesarias para poder trabajar con un objeto paneldibujo
	 */
	public paneldibujoVisual(Frame padreFrame)
	{
		this.padreFrame = padreFrame;
		simulacion = null;
		simulado=false;
		lista = new listaObjetos();
		copias = new listaObjetos();
		salidaEventos = new Vector();
		maxNumeroPasos=20000;
		ficheroTopo = "";
		cambios = false;
		quitaSelecciones = false;
		
		setLayout(null);
		setBackground(new java.awt.Color(0,0,0));
	}

	/** Refresca la pantalla para actualizar todo el contenido de paneldibujo */
	public void actualizaPantalla()
	{
		update(getGraphics());
		repaint();
	}
	
	/** Metodo que elimina toda la red disenyada
	 * Hay que comprobar cambios sobre la red para almacenar en el fichero
	 */
	public void borraTodo()
	{
		lista = new listaObjetos();
		
		cambios = false;
		
		removeAll();

		actualizaPantalla();
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
	}

	/** Busca y selecciona un equipo dado su nombre */
	public void busca()
	{
		dialogoBusqueda dialogo = new dialogoBusqueda(padreFrame, xCentral, yCentral, lista.getNombres());
		
		if (dialogo.getBoton().compareTo("Aceptar")==0)
			if (dialogo.getNombreSeleccionado() != null)
			{
				lista.seleccionaTodos(false);
				lista.setSeleccionado(lista.buscaEquipo(dialogo.getNombreSeleccionado()), true);

				ponMensaje(constantesMensajes.cMenu+cambiaMenu());
				ponMensaje(constantesMensajes.cabPanDib+ lista.getNumSeleccionados() + " equipos seleccionados");
				actualizaPantalla();
			}
		
		dialogo.destruye();
	}

	/** Funcion que se encarga de ver los estados en los que nos encontramos y los va concatenando.
	 * devuelve un String con esos estados.
	 * 0->Estado inicial, 1->Cuando se seleccionan objetos, 2->Cuando no hay ningun objeto seleccionado, 3->Cuando hemos copiado,
	 * 4->Cuando hemos cortado, 5->Cuando hemos pegado, 6->Cuando se hacen cambios, 7->Cuando se selecciona todo, 8->Cuando solo hay un objeto seleccionado,
	 * 9->Cuando hay algun equipo en la topologia, 10->Cuando hay algun objeto y no estan todos seleccionados, 11-> 0 ventanas, 12-> >0 ventanas
	 */
	public String cambiaMenu()
	{
		String ultimoEnvio = "";

		if (salidaEventos.size()>0)
		{
			ultimoEnvio = (String)salidaEventos.elementAt(salidaEventos.size()-1);
			ultimoEnvio = ultimoEnvio.substring(0, ultimoEnvio.indexOf("\t"));
		}
		
		////////////////
		Vector compruebaSimu = new Vector(lista.compruebaSimulacion());
		int tamValido = compruebaSimu.size();
		
		/* Eliminando los comentarios a esta porcion de codigo se consigue llamar al simulador
		   pero provoca excepciones. */
		/*for (int i=0; i<compruebaSimu.size(); i++)
			if (((String)compruebaSimu.elementAt(i)).indexOf("No se ha implementado la simulacion")!=-1)
				tamValido--;
		*/
		estadoAct = new cambiaMenuClase(simulado,cambios, lista.getNumSeleccionados(), lista.tam(), copias.tam(), lista.posibleSimular(), tamValido, lista.getlistaAcciones().size(), simulacion==null).getEstado() + "->" + ultimoEnvio;
		return estadoAct;
	}

	/** Se centran en ambos ejes todos los equipos seleccionados */
	public void centrarAmbos()
	{
		centrarVertical();
		centrarHorizontal();
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
	}
	
	/** Se centran horizontalmente todos los equipos seleccionados */
	public void centrarHorizontal()
	{
		lista.centrarHorizontal(getHeight(), getWidth());
		actualizaPantalla();
		cambios = true;
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
	}
	
	/** Se centran verticalmente todos los equipos seleccionados */
	public void centrarVertical()
	{
		lista.centrarVertical(getHeight(), getWidth());
		actualizaPantalla();
		cambios = true;
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
	}

	/** Comprueba que la configuracion actual de la topologia permite realizar
	 * envios de datos segun el funcionamiento del Simulador
	 * @return Vector conteniendo los mensajes que se mostraran al usuario
	 */
	public Vector compruebaSimulacion()
	{
		Vector dev = new Vector(ordenaVector.getOrdenado(lista.compruebaSimulacion()));
		
		new visSim.dialogoIncidencias(padreFrame, xCentral, yCentral, dev);
		
		return dev;
	}
	
	/** Configuracion del formato de impresion */
	public void configuraImpresion(PrinterJob trabajo, PageFormat formato)
	{
		trabajoImpresion = trabajo;
		formatoPagina = formato;
		
		trabajoImpresion.setPrintable(this, formatoPagina);

		// Ponemos un Book para imprimir la topologia en una pagina
		Book libro = new Book();
		libro.append(this, formato, 1);

		// Pasamos el Book a PrinterJob
		trabajoImpresion.setPageable(libro);
	}

	/** Devuelve el vector de objetos que han sido seleccionados para copiar */
	public listaObjetos dameCopias()
	{
		return copias;
	}

	/** Detiene completamente la simulacion de envio de datos */
	public void detenerSimulacion()
	{
		simulacion = null;
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
	}

	/** Devuelve la variable cambios indicando si ha habido cambios en la topologia
	 * @return boolean
	 */
	public boolean getCambios()
	{
		return cambios;
	}

	/** Devuelve el nombre del fichero con su ruta del fichero que almacena la topologia actual */
	public String getFicheroTopo()
	{
		return ficheroTopo;
	}

	/** Metodo para almacenar en fichero la topologia */
	public boolean grabaFichero()
	{
		boolean dev = lgfich.grabaFichero(ficheroTopo, lista);
		
		if (dev)
			cambios = false;

		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
		
		return dev;
	}
	
	/** Metodo para almacenar en fichero JPG la topologia */
	public void grabaJPG(String nomfich)
	{
		lgfich.grabaJPG(nomfich, getWidth(), getHeight(), lista);
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
	}
	
	/** Metodo que envia a la impresora el contenido de la topologia */
	public void imprimir()
	{
		try {
			// Hay que hacer esto en caso de que el usuario no haya configurado la impresion
			if (trabajoImpresion==null)
			{
				trabajoImpresion = PrinterJob.getPrinterJob();
				trabajoImpresion.setPrintable(this);
				trabajoImpresion.setJobName("Impresion de topologia");
				
				// Ponemos un Book para imprimir la topologia en una pagina
				Book libro = new Book();
				libro.append(this, trabajoImpresion.defaultPage(), 1);

				// Pasamos el Book a PrinterJob
				trabajoImpresion.setPageable(libro);
			}
				
			if (trabajoImpresion.printDialog())
				trabajoImpresion.print();
		}
		catch (PrinterException e)
		{
			System.err.println("Error de impresion: "+e.toString());
		}
	}

	/** Muestra el dialogo de eventos con la salida de la simulacion */
	public void muestraEventos()
	{
		dialogoEventos dialogo = new dialogoEventos(padreFrame, xCentral, yCentral, salidaEventos, simulacion);
		
		// Si se ha finalizado la ejecucion desde la ventana entonces desactivamos botones
		if (dialogo.getBoton().compareTo("completa")==0)
		{
			salidaEventos = simulacion.getEventos();
			detenerSimulacion();
		}
		else if (simulacion!=null)
			salidaEventos = simulacion.getEventos();

		dialogo.destruye();
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
	}

	/** Metodo que hace que no se dibujen en pantalla los marcos de seleccion de los equipos */
	public void noDibujaSelecciones(boolean valor)
	{
		quitaSelecciones = valor;
		actualizaPantalla();
	}

	/** Metodo para poner mensajes en el area de texto inferior.
	 * Se ha definido un JMenu y forzando un click sobre el salta el evento de visual.java
	 * Lo que se hara sera poners 3 asteriscos (***) antes del mensaje, para que luego
	 * el manejador los quite y muestre el mensaje de forma correcta
	 * @param msg Mensaje que se envia a visual
	 */
	public void ponMensaje(String msg)
	{
		mnuitem.setText(msg);
		mnuitem.doClick(0);
	}
	
	/** Sobrecarga del metodo print para impresion de la topologia */
	public int print(Graphics pg, PageFormat pageFormat, int pageIndex) throws PrinterException
	{
		if (pageIndex>0)
			return NO_SUCH_PAGE;
		
		pg.translate((int)pageFormat.getImageableX(), (int)pageFormat.getImageableY());

		lista.imprimir(pg, (int)pageFormat.getImageableWidth(), (int)pageFormat.getImageableHeight());
		
		System.gc();
		
		return PAGE_EXISTS;
	}

	/** Selecciona todos los objetos de la topologia segun el valor recibido
	 * @param valor Si true todos los objetos seleccionados, false indica que
	 * ningun objeto va a estar seleccionado.
	 */
	public void seleccionaTodos(boolean valor)
	{
		lista.seleccionaTodos(valor);
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		actualizaPantalla();
	}
	
	/** Establece la variable cambios segun el valor recibido */
	public void setCambios(boolean valor)
	{
		cambios = valor;
	}	

	/** Establece el fichero que va a contener la topologia */
	public void setFicheroTopo(String nomfich)
	{
		ficheroTopo = nomfich;
	}

	/** Realiza la simulacion de un paso en el envio de datos de la topologia */
	public void simulaPaso()
	{
		if (simulacion==null)
			simulacion = new simuladorVisual(lista,maxNumeroPasos);
		
		salidaEventos = new Vector(simulacion.getEventos());

		// Si termina la simulacion entonces eliminamos red
		if (!simulacion.darUnPaso())
			simulacion = null;

		simulado=true;
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
	}
	
	/** Realiza la simulacion de un evento en el envio de datos de la topologia */
	public void simulaEvento()
	{
		if (simulacion==null)
			simulacion = new simuladorVisual(lista,maxNumeroPasos);
		
		salidaEventos = new Vector(simulacion.getEventos());
		int cantidadEventos = salidaEventos.size();
		
		boolean masPasos=true;
		while (salidaEventos.size() == cantidadEventos && masPasos){
			masPasos = simulacion.darUnPaso();
			salidaEventos = new Vector(simulacion.getEventos());
		}

		// Si termina la simulacion entonces eliminamos red
		if (!masPasos)
			simulacion = null;
		
		simulado=true;
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
	}

	/** Realiza la simulacion completa en el envio de datos de la topologia */
	public void simulaTodo()
	{
		if (simulacion==null)
			simulacion = new simuladorVisual(lista,maxNumeroPasos);
		
		simulacion.simulacionCompleta();
		
		salidaEventos = new Vector(simulacion.getEventos());
		
		// Aqui siempre eliminamos red puesto que la simulacion ha finalizado
		simulacion = null;
		
		simulado=true;
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
	}
	
	/** Metodo de refresco de la pantalla */
	public void update(Graphics g) {}

	public int getMaxNumeroPasos() {
		return maxNumeroPasos;
	}

	public void setMaxNumeroPasos(int maxNumeroPasos) {
		this.maxNumeroPasos = maxNumeroPasos;
	}
}
