/** @author: tlfs & afzs
*/
import frameInternoDibujo.listaFramesInternos;
import gmenu.gmenu;
import gmenu.AcercaDe.AcercaDe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import panelbotones.panelbotones;
import paneldibujo.constantesMensajes;
import paneldibujo.dialogoCerrar;
import paneldibujo.dialogoFichero;
import util.colores;
import util.muestraAviso;
import util.nomiconos;
import ayuda.frameAyuda;
import barrabotones.barrabotones;
import gmenu.gmOpcionMenu;

/** La clase visual es la encargada de preparar en pantalla los distintos componentes de que consta
 * la aplicacion. Implementa MouseListener y ActionListener para la gestion de eventos con el raton
 * los menus y los botones.
 */
public class visual extends JFrame implements MouseListener, ActionListener
{
	/** Barra de botones mostrada en la parte superior de la pantalla */
	private barrabotones barraBotones;
	
	/** Barra de texto para mostrar mensajes al usuario */
	private JTextArea barraTexto;
	
	/** Panel mostrado antes de finalizar la ejecucion de la aplicacion */
	private dialogoCerrar cierre;
	
	/** Numero de topologias actualmente cargadas o utilizandose */
	private int cuentaVentanas;
	
	/** Cadena que codifica el estado actual dependiendo de la topologia */
	private String estadoAct;
	
	/** Icono seleccionado en panelbotones */
	private String iconoActual;
	
	/** Menu principal de la aplicacion */
	private gmenu menuppal;
	
	/** String que contiene el nombre del programa */
	private String nombrePrograma;

	/** Panel derecho que va a contener todas las ventanas de las distintas topologias */
	private JDesktopPane panelDerecho;
	
	/** Objeto que almacena las coordenadas del punto central de la pantalla */
	private Point puntoCentralW;
	
	/** Separacion de panelDerecho y panelbotones */
	private JSplitPane separa;
	
	/** Lista de ventanas (topologias) */
	private listaFramesInternos ventanas;
	
	/** Constructor de la clase visual. Prepara los elementos en pantalla e inicializa
	 * las variables necesarias para ello.
	 */
	public visual()
	{
		barraTexto = new JTextArea(1, 1);
		
		cuentaVentanas = 0; //inicializa la variable a 0
		
		nombrePrograma = "KiVa";

		// Establecemos como icono actual el icono por defecto
		iconoActual= nomiconos.nomCursor;
		
		setTitle(nombrePrograma + " - ");
		
		// Ponemos mensaje al comienzo
		ponTextoBarra("Bienvenido a " + nombrePrograma);
		
		// Ponemos evento de cierre de la aplicacion
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		WindowListener l = new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				if (mnuOpcSalir().compareTo("Cancelar")!=0)
					terminaEjecucion();
			}
		};

		addWindowListener(l);
		
		addWindowListener(new WindowAdapter()
		{
			/** El metodo pone la ventana principal, al abrirla, por encima de
			 * todas las que se encuentren abiertas
			 */
			public void windowOpened(WindowEvent evt)
			{
				evt.getWindow().toFront();
			}
		});
		
		// Cambiamos la interfaz a tipo Windows
		
		try { UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel"); }
		catch (Exception e)
			 { System.out.println("No se ha podido cambiar la interfaz"); }

		// Scroller que va a contener a paneldibujo
		JScrollPane scrollerBotones = new JScrollPane(new panelbotones(this));
		
		//se crea panel derecho y lista de ventanas
		panelDerecho = new JDesktopPane();
		ventanas = new listaFramesInternos();
		
		// Ponemos los tamanyos minimos de ambos paneles
		scrollerBotones.setMinimumSize(new Dimension(71, 500));
		panelDerecho.setMinimumSize(new Dimension(5000, 5000));
		
		panelDerecho.setBackground(colores.fondoPanel);

		separa = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollerBotones, panelDerecho);
		separa.setDividerLocation(nomiconos.tam+22);

		// Anyadimos el JSplitPane, la barra de botones y el panel de botones
		barraBotones = new barrabotones(this, "0");
		getContentPane().add(barraBotones, BorderLayout.NORTH);
		getContentPane().add(separa, BorderLayout.CENTER);
		getContentPane().add(barraTexto, BorderLayout.SOUTH);

		//se crea el menu principal (estado inicial 0)
		menuppal = new gmenu(this, this, "0", ventanas.dameListaNombres());
		setJMenuBar(menuppal);

		// Sacamos la ventana principal del programa centrada
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		puntoCentralW = new Point(ge.getCenterPoint());

		// El ancho y alto de la pantalla se establece a un 90% de la resolucion
		// Para cambiar el ancho y el alto de la ventana principal basta con cambiar estos valores
		int ancho = (int)(puntoCentralW.x*2*0.9);;
		int alto = (int)(puntoCentralW.y*2*0.9);
		int x = puntoCentralW.x-ancho/2;
		int y = puntoCentralW.y-alto/2;
		
		// puntoCentral contiene las coordenadas en donde se colocara la ventana para que quede centrada
		setBounds(x, y, ancho, alto);
	}

	/** Acciones de las opciones de los menus y de los "eventos" (mensajes) recibidos de paneldibujo.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 * @see paneldibujo
	 */
	public void actionPerformed(ActionEvent evt)
	{
		String opcion = evt.getActionCommand();
		int pos = ventanas.getActivo();
		
		//segun la opcion elegida se llama a un metodo
		if (opcion.compareTo("Salir")==0)							mnuOpcSalir();
		else if (opcion.compareTo("Nuevo")==0)						mnuOpcNuevo();
		else if (opcion.compareTo("Abrir")==0)						mnuOpcAbrir();
		else if (opcion.compareTo("Cerrar")==0)						mnuOpcCerrar();
		else if (opcion.compareTo("Guardar")==0)					mnuOpcGuardar();
		else if (opcion.compareTo("Guardar como...")==0)			mnuOpcGuardarComo();
		else if (opcion.compareTo("Imprimir")==0)					ventanas.imprimir(pos);
		else if (opcion.compareTo("Configurar impresion")==0)		ventanas.configuraImpresion();
		else if (opcion.compareTo("Propiedades ")==0)				ventanas.muestraPropiedadesTopologia(pos);
		else if (opcion.compareTo("Copiar")==0)      				ventanas.copiaSeleccionados(pos);
		else if (opcion.compareTo("Cortar")==0)						ventanas.cortar(pos);
		else if (opcion.compareTo("Pegar")==0)						ventanas.pegar(pos);
		else if (opcion.compareTo("Eliminar")==0)					ventanas.eliminaSeleccionados(pos);
		else if (opcion.compareTo("Seleccionar todo")==0)			ventanas.seleccionaTodos(pos, true);
		else if (opcion.compareTo("Centrar vertical")==0)			ventanas.centrarVertical(pos);
		else if (opcion.compareTo("Centrar horizontal")==0)			ventanas.centrarHorizontal(pos);
		else if (opcion.compareTo("Centrar ambos")==0)				ventanas.centrarAmbos(pos);
		else if (opcion.compareTo("Quitar seleccion")==0)			ventanas.seleccionaTodos(pos, false);
		else if (opcion.compareTo("Propiedades")==0)				ventanas.muestraPropiedadesEquipo(pos, puntoCentralW.x, puntoCentralW.y);
		else if (opcion.compareTo("Buscar")==0)						ventanas.busca(pos);
		else if (opcion.compareTo("Cascada")==0)					ventanas.ponerEnCascada();
		else if (opcion.compareTo("Mosaico Horizontal")==0)			ventanas.distribuirHorizontal(panelDerecho.getWidth(), panelDerecho.getHeight());
		else if (opcion.compareTo("Mosaico Vertical")==0)			ventanas.distribuirVertical(panelDerecho.getWidth(), panelDerecho.getHeight());
		else if (opcion.compareTo("Minimizar todas")==0)			ventanas.minimizaTodas();
		else if (opcion.compareTo("Simular Envios")==0)				ventanas.simulaTodo();
		else if (opcion.compareTo("Simular paso a paso")==0)		ventanas.simulaPaso();
		else if (opcion.compareTo("Detener simulacion")==0)			ventanas.detenerSimulacion();
		else if (opcion.compareTo("Mostrar Sucesos de la Simulacion")==0) ventanas.muestraEventos();
		else if (opcion.compareTo("Configurar Envios")==0)			ventanas.configuraEnvios(puntoCentralW.x, puntoCentralW.y);
		else if (opcion.compareTo("Comprobar Simulacion")==0)		ventanas.compruebaSimulacion();
		else if (opcion.compareTo("Contenido")==0)					new frameAyuda(puntoCentralW.x, puntoCentralW.y);
		else if (opcion.compareTo("Cerrar todas")==0)				mnuOpcCerrarTodas();				
		else if (opcion.compareTo("Acerca de")==0)					new AcercaDe(this, true, puntoCentralW.x, puntoCentralW.y).muestra();
		// Mensaje de panelbotones para cambiar el icono del cursor
		else if (opcion.substring(0, 3).compareTo(constantesMensajes.cabPanBot)==0)
		{
			//se coge el icono actual
			iconoActual = opcion.substring(3, opcion.length());
			ventanas.ponCursor(iconoActual, puntoCentralW.x, puntoCentralW.y);
		
			final int pos2 = ventanas.getActivo();
			//se obtiene el nuevo estado del menu
			estadoAct=new String (ventanas.cambiaMenu());
			
			//para que cambie el menu con el nuevo estado
			if (pos2!=-1)
				ventanas.ponMensaje(pos2,constantesMensajes.cMenu+estadoAct);
		}
		// Mensaje de paneldibujo
		else if (opcion.substring(0, 3).compareTo(constantesMensajes.cabPanDib)==0)
		{
			// En el "mensaje enviado" vienen las coordenadas para mostrar el menu oculto
			if (opcion.substring(3, 15).compareTo(constantesMensajes.muestraRaton)==0)
			{
				String cadena = opcion.substring(opcion.indexOf(",")+1);
				int x = (new Integer((cadena.substring(0, cadena.lastIndexOf(","))))).intValue();
				int y = (new Integer((cadena.substring(cadena.lastIndexOf(",")+1, cadena.lastIndexOf("*"))))).intValue();
				
				String nomVentana = cadena.substring(cadena.lastIndexOf("*")+1);
				
				menuppal.muestra(x+ventanas.getX(ventanas.buscaVentana(nomVentana))+20,y+ventanas.getY(ventanas.buscaVentana(nomVentana))+20);
			}
			else if (opcion.substring(3, 13).compareTo(constantesMensajes.cambiaTopo)==0)
				setTitle(nombrePrograma + " - " + ventanas.cambiaTitulo(opcion.substring(14, opcion.lastIndexOf(",")), opcion.substring(opcion.lastIndexOf(",")+1, opcion.length()).compareTo("true")==0));
			else if (opcion.substring(3, 10).compareTo(constantesMensajes.copiado)==0)
				ventanas.ponCopiados(opcion.substring(11, opcion.length()));
			//para cambiar el menu
			else if(opcion.substring(3, 13).compareTo(constantesMensajes.cambiaMenu)==0)
			{
				String cadena = opcion.substring(13,opcion.lastIndexOf("-"));
				String eventos = opcion.substring(opcion.indexOf("->")+2, opcion.length());
				
				if (ventanas.tam()==0)
					cadena += "-11-"; //estado 'no hay ventanas'
				else
					cadena += "-12-"; //estado 'hay 1 o mas ventanas'
				
				//funcion para que los menus se habiliten o deshabiliten segun las las acciones que se puedan realizar.
				//La cadena va a contener los estados en los q nos encontramos.
				menuppal.habilitaMenu(cadena);
				
				//funcion para que la barra de botones se habilite o deshabilite segun las las acciones que se puedan realizar.
				//La cadena va a contener los estados en los q nos encontramos
				if (eventos.length()!=0)
					barraBotones.habilita(cadena,"Evento " + eventos + " transcurrido");
				else
					barraBotones.habilita(cadena,"");
			}
			else
				ponTextoBarra(opcion.substring(3));
		}
		else if (opcion.substring(0, 3).compareTo(constantesMensajes.cabMsgUsr)==0)
			muestraAviso.mensaje(this, opcion.substring(3, opcion.length()));
		else if (opcion.endsWith(".net"))
		{
			opcion = opcion.substring(opcion.lastIndexOf("] ")+2, opcion.length());
			ventanas.ponActiva(ventanas.buscaVentana(opcion), true);
		}
		
		ponTextoBarra("");
	}
	
	/** Metodo que inserta una nueva ventana y crea todos los eventos necesarios sobre la misma */
	public void insertaVentana(String nombre)
	{
		//incrementa el numero de ventanas y anyade una.
		cuentaVentanas++;
		ventanas.add(this, this, (nombre+ new Integer(cuentaVentanas)));
		
		final int pos = ventanas.tam()-1;
			
		final JInternalFrame ifr = (ventanas.getFrameInterno(pos));
		final Frame padreFrame = this;
		panelDerecho.add(ifr);
		ifr.setBounds((pos+1)*10, (pos+1)*10, 300, 250);
		
		//desactiva las ventanas, la nueva la activa
		ventanas.desactivaTodos(pos);
		ventanas.ponActiva(pos, true);
		ventanas.maximiza(pos);
		setTitle(nombrePrograma + " - " + ifr.getTitle());
		
		//al menu se le pasa la lista de ventanas
		menuppal.pasaLista(ventanas.dameListaNombres());

		//se obtiene el nuevo estado
		estadoAct=new String (ventanas.cambiaMenu());
		estadoAct = estadoAct.substring(0, estadoAct.indexOf("->")) + "12-" + estadoAct.substring(estadoAct.indexOf("->"), estadoAct.length());
		
		//se cambia el menu con este nuevo estado
		ventanas.ponMensaje(pos,"***CambiaMenu"+estadoAct);

		// Anyadimos eventos sobre los JInternalFrames para que al hacer click sobre uno se desactiven los demas
		ifr.addInternalFrameListener(new InternalFrameListener()
		{
			/** function internalFrameActivated para activar una ventana
			 * @param e evento del frame
			 */
			public void internalFrameActivated(InternalFrameEvent e)
			{
				// Tenemos que buscar la ventana por su titulo a partir del JInternalFrame
				String nombre = e.getInternalFrame().getTitle();
				
				// Comprobamos si en el titulo esta la cadena (*) para eliminarla
				if (nombre.endsWith("(*)"))
					nombre = nombre.substring(0, nombre.lastIndexOf("*")-2);

				final int pos2 = ventanas.buscaVentana(nombre);
				
				estadoAct=new String (ventanas.cambiaMenu(pos2));
				ventanas.ponMensaje(pos2,"***CambiaMenu12-"+estadoAct+"");

				// Desactivamos todas las ventanas excepto la actual
				ventanas.desactivaTodos(pos2);
				ventanas.ponActiva(pos2, true);
				ventanas.noDibujaSelecciones(pos2, false);
				ifr.moveToFront();
				setTitle(nombrePrograma + " - " + ifr.getTitle());
			}

			/** function internalFrameClosed al cerrar se le pasa la lista de ventanas al menu principal
			 * @param e evento del frame
			 */
			public void internalFrameClosed(InternalFrameEvent e)
			{
				menuppal.pasaLista(ventanas.dameListaNombres());
			}
			
			/** function internelFrameClosing Antes de cerrar comprobamos si hay que almacenar el fichero
			 * @param e evento del frame
			 */
			public void internalFrameClosing(InternalFrameEvent e)
			{
				String nombre = e.getInternalFrame().getTitle();
				
				if (nombre.endsWith("(*)"))
					nombre = nombre.substring(0, nombre.lastIndexOf("*")-2);

				final int pos2 = ventanas.buscaVentana(nombre);
				
				// Por dar algun valor distinto a CANCEL_OPTION
				int valor = JOptionPane.YES_OPTION;

				// Si no hay cambios la ventana se va a cerrar sin preguntar nada al usuario
				if (ventanas.getCambios(pos2))
				{
					valor = JOptionPane.showConfirmDialog(padreFrame, "Desea almacenar los cambios antes de cerrar?", "Almacenar topologia - " + ventanas.getFicheroTopo(pos2), JOptionPane.YES_NO_CANCEL_OPTION);
					if (valor == JOptionPane.YES_OPTION)
						mnuOpcGuardar();
				}
				
				// Si al solicitar almacenar no se da a cancelar				
				if (valor != JOptionPane.CANCEL_OPTION)
				{
					// Ponemos la accion de cierre por defecto si anteriormente se habia modificado
					if (ifr.getDefaultCloseOperation() == WindowConstants.DO_NOTHING_ON_CLOSE)
						ifr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

					ventanas.cierraVentana(pos2);
				}
				// Si al solicitar almacenar se da a cancelar no se cierra la ventana
				else
					ifr.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

				//si no hay ventanas abiertas
				if (ventanas.tam()==0)
				{
					//funcion para que los menus se habiliten o deshabiliten segun las las acciones que se puedan realizar."-11-" es el estado en el q nos encontramos.
					menuppal.habilitaMenu("-11-");
					setTitle(nombrePrograma + " - ");
					//funcion para que la barra de botones se habilite o deshabilite segun las las acciones que se puedan realizar. "-11-" es el estado en el q nos encontramos.
					barraBotones.habilita("-11-","");
				}
			}
			
			/** function internalFrameDeactivated para desactivar una ventana
			 * @param e evento del frame
			 */
			public void internalFrameDeactivated(InternalFrameEvent e)
			{
				// Tenemos que buscar la ventana por su titulo a partir del JInternalFrame
				String nombre = e.getInternalFrame().getTitle();
				
				// Comprobamos si en el titulo esta la cadena (*) para eliminarla
				if (nombre.endsWith("(*)"))
					nombre = nombre.substring(0, nombre.lastIndexOf("*")-2);
				
				final int pos2 = ventanas.buscaVentana(nombre);

				if (pos2!=-1)
				{
					if (ventanas.tam()==0) //no hay ventanas
					{
						//funcion para que los menus se habiliten o deshabiliten segun las las acciones que se puedan realizar. "-11-" es el estado en el q nos encontramos.
						menuppal.habilitaMenu("-11-");
						//funcion para que la barra de botones se habilite o deshabilite segun las las acciones que se puedan realizar. "-11-" es el estado en el q nos encontramos.
						barraBotones.habilita("-11-","");
					}
					else
						//funcion para que la barra de botones se habilite o deshabilite segun las las acciones que se puedan realizar. estadoAct son los estados en los q nos encontramos.
						barraBotones.habilita(ventanas.cambiaMenu(pos2),"");

					// Al desactivar la ventana no se muestran objetos seleccionados
					ventanas.noDibujaSelecciones(pos2, true);
				}
			}

			/** function internalFrameDeiconified cuando la ventana se restaura le pasa la lista de ventanas al menu
			 * @param e evento del frame
			 */
			public void internalFrameDeiconified(InternalFrameEvent e)
			{
				menuppal.pasaLista(ventanas.dameListaNombres());
			}

			/** function internalFrameIconified cuando la ventana se minimiza le pasa la lista de ventanas al menu
			 * @param e evento del frame
			 */
			public void internalFrameIconified(InternalFrameEvent e)
			{
				menuppal.pasaLista(ventanas.dameListaNombres());
			}

			/** function internalFrameOpened cuando la ventana se abre le pasa la lista de ventanas al menu
			 * @param e evento del frame
			 */
			public void internalFrameOpened(InternalFrameEvent e)
			{
				menuppal.pasaLista(ventanas.dameListaNombres());
			}
		});
	}

	/** Metodo para abrir una topologia previamente almacenada */	
	private void mnuOpcAbrir()
	{
		int valor, pos;

		dialogoFichero eligeFichero = new dialogoFichero("net");
			
		valor = eligeFichero.mostrar(this, "Abrir", "");
			
		if (valor == JFileChooser.APPROVE_OPTION)
		{
			String fichero = eligeFichero.getNomFich();
			
			//busca el fichero en las ventanas
			pos = ventanas.buscaVentana(fichero);
			valor = JOptionPane.NO_OPTION;
			
			// El fichero ya se encuentra abierto
			if (pos!=-1)
			{
				valor = JOptionPane.showConfirmDialog(this, "Desea volver a la version en disco de " + fichero + "?", "Abrir topologia - " + fichero, JOptionPane.YES_NO_CANCEL_OPTION);
				
				// Si el usuario elige leer de nuevo el fichero tenemos que cerrar el actual
				if (valor==JOptionPane.YES_OPTION)
				{
					ventanas.ponActiva(pos, true);
					mnuOpcCerrar();
				}
			}
			
			//si el fichero no esta abierto
			if (pos==-1 || valor==JOptionPane.YES_OPTION)
			{
				// Creamos una nueva y en ella leemos el fichero
				mnuOpcNuevo();
				pos = ventanas.getActivo();
		
				ventanas.setFicheroTopo(pos, fichero);
				ventanas.maximiza(pos);
					
				//si no se carga el fichero
				if (!ventanas.leeFichero(pos, fichero))
				{
					ponTextoBarra("Error durante la lectura de " + fichero);
					
					// Cerramos la ventana que se acaba de crear
					mnuOpcCerrar();
				}
				else
				{
					//ponemos el titulo del fichero en la ventana
					ponTextoBarra(fichero);
					setTitle(nombrePrograma + " - " + fichero);
				}
			}
		}
		//al menu se le pasa la lista de ventanas
		menuppal.pasaLista(ventanas.dameListaNombres());
	}
	
	/** Cierra ventana activa */
	private void mnuOpcCerrar()
	{
		//encuentra la posicion de la ventana activa
		int pos = ventanas.getActivo();
		
		//si encontrada se cierra
		if (pos!=-1)
		{
			JInternalFrame ifr = (ventanas.getFrameInterno(pos));
			ifr.doDefaultCloseAction();
		}
	}

	/** Cierra todas las ventanas permitiendo seleccionar al usuario cuales almacenar */
	@SuppressWarnings("unchecked")
	private void mnuOpcCerrarTodas()
	{
		//obtiene la lista de ficheros modificados
		Vector temp = new Vector(ventanas.dameListaModificados());
		
		cierre = new dialogoCerrar(this, temp, puntoCentralW.x, puntoCentralW.y, this, "Guardar / Cerrar");

		//si hay modificados se muestra el dialogo cerrar
		if (temp.size()>0)
			cierre.muestra();
		
		Vector lista = new Vector(cierre.getListaSeleccionados());
		
		// Si no se ha pulsado cancelar entonces almacenamos y cerramos
		if (cierre.getTextoBoton().compareTo("")==0)
		{
			for (int i=0; i<lista.size(); i++)
			{
				// Activamos la ventana
				ventanas.ponActiva(ventanas.buscaVentana((String)lista.elementAt(i)), true);
				// y la guardamos
				mnuOpcGuardar();
			}
			
			while (ventanas.tam()>0)
			{
				ventanas.ponActiva(0, true);
				ventanas.setCambios(0, false);
				ventanas.getFrameInterno(0).doDefaultCloseAction();
			}
		}
		
		cierre.dispose();
	}
	
	/** Metodo para almacenar la topologia actual */
	private void mnuOpcGuardar()
	{
		//se obtiene la ventana activa
		int pos = ventanas.getActivo();
		
		// Por inicializarla con algun valor
		int valor = JOptionPane.YES_OPTION;
		
		//si existe
		if (pos!=-1)
		{
			String ficheroTopo = ventanas.getFicheroTopo(pos);

			dialogoFichero eligeFichero = new dialogoFichero("net");
			
			// El fichero aun no ha sido almacenado; lo grabamos en la carpeta Mis Documentos
			if (ficheroTopo.indexOf(System.getProperty("file.separator"))==-1)
				ficheroTopo = eligeFichero.getDirectorio()+ficheroTopo;
			
			// Si ya existe se lo decimos al usuario
			if (new File(eligeFichero.getDirectorio()+ficheroTopo).exists())
				valor = JOptionPane.showConfirmDialog(this, "Ya existe un fichero con ese nombre.\nDesea guardar el fichero actual?", "Almacenar topologia", JOptionPane.YES_NO_CANCEL_OPTION);

			if (valor == JOptionPane.YES_OPTION)
			{
				ventanas.setFicheroTopo(pos, ficheroTopo);
				ventanas.grabaFichero(pos);
				setTitle(nombrePrograma + " - " + ficheroTopo);
				ventanas.setCambios(pos, false);
			}
		}
	}

	/** Metodo que permite al usuario almacenar una topologia con un nombre distinto */
	private void mnuOpcGuardarComo()
	{
		//se obtiene el fichero activo
		int pos = ventanas.getActivo();
		
		//si existe
		if (pos!=-1)
		{
			// Nos quedamos con el nombre de fichero que ya tenemos por si el usuario graba en JPG
			// no perder el nombre .net
			String ficheroTopo;

			dialogoFichero eligeFichero = new dialogoFichero("todo");

			// Aun no se ha dado nombre
			int valor = eligeFichero.mostrar(this, "Guardar Como...", ventanas.getFicheroTopo(pos));
		
			if (valor == JFileChooser.APPROVE_OPTION)
			{
				ficheroTopo = eligeFichero.getNomFich();
				
				// Si ya existe un fichero con el mismo nombre y ruta se lo decimos al usuario
				if (new File(ficheroTopo).exists())
					valor = JOptionPane.showConfirmDialog(this, "Ya existe un fichero con ese nombre.\nDesea guardar el fichero actual?", "Almacenar topologia", JOptionPane.YES_NO_CANCEL_OPTION);
				
				// Si el usuario confirma entonces se chafa el fichero existente
				if (valor == JOptionPane.YES_OPTION)
					if (ficheroTopo.compareTo("")!=0)
						//si se elige la opcion jpg
						if (eligeFichero.getFiltro().compareTo("Ficheros JPG (.jpg)")==0)
						{
							if (!ficheroTopo.endsWith(".jpg"))
								ficheroTopo += ".jpg";
					
							ventanas.grabaJPG(pos, ficheroTopo);
						}
						//si se elige la opcion net
						else if (eligeFichero.getFiltro().compareTo("Ficheros de red (.net)")==0)
						{
							if (!ficheroTopo.endsWith(".net"))
								ficheroTopo += ".net";

							ventanas.setFicheroTopo(pos, ficheroTopo);
							ventanas.grabaFichero(pos);
							setTitle(nombrePrograma + " - " + ficheroTopo);
						}
			}
		}
		//se pasa al menu principal la lista de ventanas
		menuppal.pasaLista(ventanas.dameListaNombres());
	}

	/** Metodo para crear una nueva topologia */ 
	private void mnuOpcNuevo()
	{
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      puntoCentralW = new Point(ge.getCenterPoint());

		// Insertamos una ventana nueva
		insertaVentana("NuevoFichero");
		// y ponemos cursor actual a todas
		ventanas.ponCursor(iconoActual, puntoCentralW.x, puntoCentralW.y);
	}
	
	/** Cierra la aplicacion tras mostrar una lista de topologias modificadas. */ 
	@SuppressWarnings("unchecked")
	private String mnuOpcSalir()
	{
		//obtiene la lista de ficheros modificados
		Vector temp = new Vector(ventanas.dameListaModificados());
		String dev = "";
		
		//si hay modificados
		if (temp.size()>0)
		{
			//muestra el dialogo de cerrar
			cierre = new dialogoCerrar(this, temp, puntoCentralW.x, puntoCentralW.y, this, "Guardar / Salir");
			cierre.muestra();
			
			dev = cierre.getTextoBoton();
		}
		else
			terminaEjecucion();
		
		return dev;
	}
	
	/** Eventos de raton para los botones del panel superior
	 * @param evt evento del raton
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@SuppressWarnings("unchecked")
	public void mouseClicked(MouseEvent evt)
	{
		String nombre = evt.getComponent().getName();
		int pos = ventanas.getActivo();
		
		if (evt.getComponent().isEnabled())
		{
			// Opcion nuevo de barraBotones
			if (nombre.compareTo("btnN")==0)		mnuOpcNuevo();

			// Opcion abrir de barraBotones
			else if (nombre.compareTo("btnA")==0)	mnuOpcAbrir();

			// Opcion Guardar de barraBotones
			else if (nombre.compareTo("btnG")==0)	mnuOpcGuardar();
		
			// Opcion cortar de barraBotones
			else if (nombre.compareTo("btnX")==0)	ventanas.cortar(pos);
		
			// Opcion copiar de barraBotones
			else if (nombre.compareTo("btnC")==0)	ventanas.copiaSeleccionados(pos);
		
			// Opcion pegar de barraBotones
			else if (nombre.compareTo("btnP")==0)	ventanas.pegar(pos);

			// Opcion imprimir de barraBotones
			else if (nombre.compareTo("btnI")==0)	ventanas.imprimir(pos);
		
			// Opcion buscar de barraBotones
			else if (nombre.compareTo("btnB")==0)	ventanas.busca(pos);

			// Opcion comprueba simulacion
			else if (nombre.compareTo("btnCompS")==0)	ventanas.compruebaSimulacion();

			// Opcion simular todo
			else if (nombre.compareTo("btnSi")==0)	ventanas.simulaTodo();

			// Opcion simular un paso
			else if (nombre.compareTo("btnSip")==0)	ventanas.simulaPaso();

			// Opcion envia datos
			else if (nombre.compareTo("btnSE")==0)	ventanas.configuraEnvios(puntoCentralW.x, puntoCentralW.y);

			// Opcion detiene simulacion
			else if (nombre.compareTo("btnDS")==0)	ventanas.detenerSimulacion();
		
			// Opcion eventos de la simulacion
			else if (nombre.compareTo("btnEvS")==0)	ventanas.muestraEventos();

			// Boton Guardar del dialogo de salir
			else if (nombre.compareTo("Guardar / Salir")==0)
			{
				Vector lista = new Vector(cierre.getListaSeleccionados());
		
				for (int i=0; i<lista.size(); i++)
				{
					// Activamos la ventana
					ventanas.buscaVentana((String)lista.elementAt(i));
					// y la guardamos
					mnuOpcGuardar();
				}
			
				terminaEjecucion();
			}
			else if (nombre.compareTo("Guardar / Cerrar")==0)	cierre.setVisible(false);
		}
	}
	
	public void mouseEntered(MouseEvent evt)
	{
		try
		{
			gmOpcionMenu opcionMenu = (gmOpcionMenu)evt.getComponent();
			String opcion = opcionMenu.getText();

			if (opcion.compareTo("Salir")==0)							ponTextoBarra("Cierra la aplicacion");
			else if (opcion.compareTo("Nuevo")==0)						ponTextoBarra("Crea una nueva topologia");
			else if (opcion.compareTo("Abrir")==0)						ponTextoBarra("Abre una topologia");
			else if (opcion.compareTo("Cerrar")==0)					ponTextoBarra("Cierra la topologia actual");
			else if (opcion.compareTo("Guardar")==0)					ponTextoBarra("Guarda la topologia actual");
			else if (opcion.compareTo("Guardar como...")==0)		ponTextoBarra("Guarda con nombre distinto la topologia actual");
			else if (opcion.compareTo("Imprimir")==0)					ponTextoBarra("Imprime la topologia");
			else if (opcion.compareTo("Configurar impresion")==0)	ponTextoBarra("Configuracion parametros de impresion");
			else if (opcion.compareTo("Propiedades ")==0)			ponTextoBarra("Propiedades de la topologia actual");
			else if (opcion.compareTo("Copiar")==0)      			ponTextoBarra("Copia los equipos seleccionados");
			else if (opcion.compareTo("Cortar")==0)					ponTextoBarra("Corta los equipos seleccionados");
			else if (opcion.compareTo("Pegar")==0)						ponTextoBarra("Pega los equipos previamente copiados");
			else if (opcion.compareTo("Eliminar")==0)					ponTextoBarra("Elimina los equipos seleccionados");
			else if (opcion.compareTo("Seleccionar todo")==0)		ponTextoBarra("Selecciona la topologia completa");
			else if (opcion.compareTo("Centrar vertical")==0)		ponTextoBarra("Centrado vertical de los equipos seleccionados");
			else if (opcion.compareTo("Centrar horizontal")==0)	ponTextoBarra("Centrado horizontal de los equipos seleccionados");
			else if (opcion.compareTo("Centrar ambos")==0)			ponTextoBarra("Centrado por ambos ejes de los equipos seleccionados");
			else if (opcion.compareTo("Quitar seleccion")==0)		ponTextoBarra("Quita la seleccion actual");
			else if (opcion.compareTo("Propiedades")==0)				ponTextoBarra("Muestra las propiedades del equipo");
			else if (opcion.compareTo("Buscar")==0)					ponTextoBarra("Busqueda de equipos en la topologia");
			else if (opcion.compareTo("Cascada")==0)					ponTextoBarra("Pone en cascada todas las ventanas");
			else if (opcion.compareTo("Mosaico Horizontal")==0)	ponTextoBarra("Pone en mosaico horizontal todas las ventanas");
			else if (opcion.compareTo("Mosaico Vertical")==0)		ponTextoBarra("Pone en mosaico vertical todas las ventanas");
			else if (opcion.compareTo("Minimizar todas")==0)		ponTextoBarra("Minimiza todas las ventanas");
			else if (opcion.compareTo("Simular Envios")==0)			ponTextoBarra("Simulacion de envios a traves de la topologia");
			else if (opcion.compareTo("Simular paso a paso")==0)	ponTextoBarra("Simulacion de un paso en la transmision de paquetes");
			else if (opcion.compareTo("Detener simulacion")==0)	ponTextoBarra("Detiene la simulacion actual");
			else if (opcion.compareTo("Mostrar Sucesos de la Simulacion")==0) ponTextoBarra("Muestra los eventos ocurridos durante la simulacion");
			else if (opcion.compareTo("Configurar Envios")==0)		ponTextoBarra("Configuracion de envios de la topologia");
			else if (opcion.compareTo("Comprobar Simulacion")==0)	ponTextoBarra("Comprobacion de errores en la topologia");
			else if (opcion.compareTo("Contenido")==0)				ponTextoBarra("Muestra la ayuda de la aplicacion");
			else if (opcion.compareTo("Cerrar todas")==0)			ponTextoBarra("Cierra todas las ventanas");				
			else if (opcion.compareTo("Acerca de")==0)				ponTextoBarra("Muestra ayuda acerca de la aplicacion");
			else if (opcion.endsWith(".net"))							ponTextoBarra("Muestra la ventana que contiene la topologia " + opcion.substring(opcion.lastIndexOf("] ")+2, opcion.length()));
		}
		catch (Exception e)
		{/*Para cuando no sea opcion de menu.*/}
	}
	
	public void mouseExited(MouseEvent evt)
	{
		ponTextoBarra("");
	}
	
	public void mousePressed(MouseEvent evt){}
	public void mouseReleased(MouseEvent evt){}
	
	/** Pone un mensaje en la parte inferior de la pantalla
	 * @param texto Mensaje que se va a mostrar
	 */
	private void ponTextoBarra(String texto)
	{
		if (texto.length()>0)
		{
			barraTexto.setText(texto);
		
			// Parametros de la barra de texto
			barraTexto.setBackground(new java.awt.Color(204, 204, 204));
			barraTexto.setEditable(false);
			barraTexto.setBorder(new javax.swing.border.EtchedBorder());
			barraTexto.setFocusable(false);
		}
	}

	/** Metodo que finaliza la ejecucion de la aplicacion */	
	private void terminaEjecucion()
	{
		System.gc();
		System.runFinalization();
		System.exit(0);
	}

	/** Funcion principal */
	public static void main(String args[])
	{
		(new visual()).setVisible(true);
	}
}
