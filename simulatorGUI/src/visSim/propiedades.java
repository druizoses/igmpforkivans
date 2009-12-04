/** @author: tlfs & afzs
*/
package visSim;

import java.awt.Frame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import objetoVisual.listaConexiones;
import objetoVisual.listaObjetos;
import objetoVisual.switchVisual;
import util.muestraAviso;
import util.nomiconos;
import util.ordenaVector;

/** Esta clase implementa el cuadro de propiedades de los equipos
 * Desde dicho cuadro se puede cambiar el nombre del equipo, sus conexiones,
 * interfaces, rutas, errores a simular y se puede visualizar los eventos
 * de cada equipo concreto tras la simulacion
 */
public class propiedades extends JDialog implements MouseListener
{
	/** Conexiones (visuales) del equipo */
	private listaConexiones conexiones;
	
	/** Interfaces del equipo */
	private listaInterfaces interfaces;
	
	/** Lista completa de la topologia.
	 * Es necesaria pasarla entera pues que es necesario comprobar que
	 * el nombre del equipo no exista ya en la topologia, que las direcciones
	 * IP de las interfaces no se repitan, que sea posible definir las rutas
	 * que el usuario decide definir... */
	private listaObjetos lista;
	
	/** Configuracion de los envios de las maquinas */
	private Vector listaAcciones;
	
	/** Nombre del equipo y boton pulsado para indicarselo a panelDibujo */
	private String nomEquipo, textoBoton;
	
	/** Panel que mostrara las conexiones del equipo y las opciones con las mismas */
	private panelConexion panelConexiones;

	/** Panel que mostrara las errores a simular por el equipo */
	private panelErrores panelError;
	
	/** Panel que mostrara las interfaces del equipo y las opciones con las mismas */
	private panelInterfaz panelInterfaces;

	/** Panel que mostrara las rutas del equipo y las opciones con las mismas */
	private panelRutas panelRuta;
	
	/** Lista de rutas del equipo */
	private listaRutas rutas;

	/** Campo de texto donde se muestra el nombre del equipo */
	private JTextField txtNombre;

	/** Coordenadas centrales y posicion del equipo dentro de la lista */
	private int xCentral, yCentral, posicion;
	
	private Frame esteFrame;
	
	/** Constructor de la clase
	 * @param parent Frame donde se mostrara la ventana
	 * @param xCentral Coordenada x central de la pantalla
	 * @param yCentral Coordenada y central de la pantalla
	 * @param posicion Posicion del equipo dentro de la lista
	 * @param lista Lista de objetos (topologia completa)
	 * @param eventos Eventos simulables por el equipo
	 */
	public propiedades(Frame parent, int xCentral, int yCentral, int posicion, listaObjetos lista, Vector eventos)
	{
		super(parent, true);
		esteFrame = parent;

		this.xCentral = xCentral;
		this.yCentral = yCentral;
		this.lista = new listaObjetos();
		this.lista.copialistaObjetos(lista);
		this.posicion = posicion;
		this.listaAcciones = new Vector(lista.getlistaAcciones());
		
		// Preparamos las cosas de la ventana
		JPanel panelSuperior = new JPanel();

		JLabel imagen = new JLabel(new ImageIcon(lista.getNomIcono(posicion)));
		imagen.setText("         Nombre del equipo:");

		getContentPane().setLayout(null);

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				textoBoton = "Cancelar";
				setVisible(false);
			}
		});
		
		txtNombre = new JTextField();
		txtNombre.setHorizontalAlignment(SwingConstants.LEFT);
		txtNombre.setToolTipText("Nombre del equipo");
		txtNombre.setBorder(null);

		panelSuperior.add(imagen);
		panelSuperior.add(txtNombre);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.setName("Aceptar");
		
		btnAceptar.addMouseListener(this);
		getContentPane().add(btnAceptar);
		btnAceptar.setBounds(125, 280, 95, 26);

		JButton btnCancelar = new JButton("Cancelar");
		
		btnCancelar.setName("Cancelar");
		btnCancelar.addMouseListener(this);
		getContentPane().add(btnCancelar);
		btnCancelar.setBounds(235, 280, 95, 26);
		
		getContentPane().add(panelSuperior);
		panelSuperior.setBounds(0, 0, 250, nomiconos.tam);
      
		JTabbedPane panelTabulado = new JTabbedPane();
		
		/* Creamos los paneles y los anyadimos a panelTabulado */
		// Ademas de pasarle el listado de conexiones del equipo, tambien le pasamos la lista de interfaces que usa para dichas conexiones 
		panelConexiones = new panelConexion(this, ordenaVector.getOrdenado(lista.getConexiones(posicion)), lista.getInterfaces(posicion));
		panelInterfaces = new panelInterfaz(this, lista.getID(posicion), lista.getInterfaces(posicion));
		panelRuta = new panelRutas(this.esteFrame, this, lista.getID(posicion), lista.getRutas(posicion));
		panelError = new panelErrores(lista.getErrores(posicion));
		
		if(!(lista.getObjeto(posicion) instanceof switchVisual)){ //Nombre(posicion).startsWith("Switch"))
			panelTabulado.add("Interfaces", panelInterfaces);
			panelTabulado.add("Rutas", panelRuta);
			panelTabulado.add("Conexiones", panelConexiones);
			panelTabulado.add("Errores a simular", panelError);
		}else{
			//panelTabulado.add("Interfaces", panelInterfaces);
			panelTabulado.add("Conexiones", panelConexiones);
		}
		if (eventos!=null)
			panelTabulado.add("Eventos", new panelEventos(eventos));
		
		getContentPane().add(panelTabulado);
		panelTabulado.setBounds(10, nomiconos.tam+10, 450, 210);
		
		setResizable(false);
		int ancho = 470;	
		int alto = 345;
		setBounds(xCentral-ancho/2, yCentral-alto/2, ancho, alto);

		nomEquipo = lista.getNombre(posicion);
		setTitle("Propiedades de " + nomEquipo);
		txtNombre.setText(nomEquipo);
		txtNombre.setColumns(7);

		setDatos(lista.getInterfaces(posicion), lista.getRutas(posicion), lista.getConexiones(posicion));
	}
	
	/** Libera la memoria ocupada por la ventana */
	public void destruye()
	{
		setVisible(false);
		dispose();
	}
	
	/** Devuelve la direccion de red que corresponde a una direccion IP */
	private String dirRed(String direc)
	{
		return direc.substring(0, direc.lastIndexOf(".")+1) + "0";
	}
	
	/** Devuelve el nombre del boton pulsado al cerrar la ventana */
	public String getBoton()
	{
		return textoBoton;
	}

	/** Devuelve el vector de conexiones del equipo */
	public Vector getConexiones()
	{
		return new Vector(conexiones);
	}
	
	/** Devuelve la configuracion de envios */
	public Vector getAcciones()
	{
		return new Vector(listaAcciones);
	}
	
	/** Devuelve las interfaces modificadas del equipo */
	public listaInterfaces getInterfaces()
	{
		listaInterfaces dev = new listaInterfaces();
		
		for (int i=0; i<interfaces.size(); i++)
			dev.add(interfaces.getInterfaz(i));
		
		return dev;
	}
	
	/** Devuelve las selecciones que el usuario realizar sobre la simulacion de errores */
	public Vector getListaErrores()
	{
		return panelError.getSeleccionesErrores();
	}
	
	/** Devuelve el nombre del equipo */
	public String getnomEquipo()
	{
		return txtNombre.getText();
	}

	/** Devuelve las rutas modificadas del equipo */
	public listaRutas getRutas()
	{
		listaRutas dev = new listaRutas();
		
		for (int i=0; i<rutas.size(); i++)
			dev.add(rutas.getRuta(i));
		
		return dev;
	}
	
	/** Evento click del raton.
	 * Esta funcion controla todos los clicks realizados sobre los distintos
	 * paneles que configuran la ventana de propiedades.
	 */
	public void mouseClicked(MouseEvent evt)
	{
		String nombre = evt.getComponent().getName();
		
		// Al hacer doble click sobre la tabla modificamos la entrada sobre la que se pincha
		if (evt.getClickCount()==2)
		{
			if (nombre.compareTo("tablaInterfaz")==0 && panelInterfaces.estaActivo("tablaInterfaz"))
				nombre = "ModificarInterfaz";
			else if (nombre.compareTo("tablaRuta")==0 && panelRuta.estaActivo("tablaRuta"))
				nombre = "ModificarRuta";
		}

		if (nombre.compareTo("AñadirInterfaz")==0 && panelInterfaces.estaActivo("AñadirInterfaz"))
		{
			dialogoInterfaz dialogo = new dialogoInterfaz(esteFrame, xCentral, yCentral, "Añadir interfaz", "", "", "", interfaces.dameMACNoExistente(lista.getDirecsMac(interfaces, posicion)), "N/A", interfaces.dameNombresInterfaz(true, ""), lista.getListaIPConecta(conexiones, ""), conexiones, lista.getTipos(conexiones));
			
			if (dialogo.getBoton().compareTo("Aceptar")==0)
			{
				interfaces.inserta(dialogo.getNombre(), dialogo.getIP(), dialogo.getMascara(), dialogo.getMAC(), dialogo.getConecta());
				rutas.inserta(dirRed(dialogo.getIP()), dialogo.getMascara(), "0.0.0.0", dialogo.getNombre());
				panelInterfaces.setTabla(interfaces);
				panelRuta.setTabla(rutas);
			}
			
			dialogo.destruye();
		}
		else if (nombre.compareTo("ModificarInterfaz")==0 && panelInterfaces.estaActivo("ModificarInterfaz"))
		{
			int indice = panelInterfaces.getFilaTabla();
			
			if (indice==-1)
				muestraAviso.mensaje(esteFrame, "Seleccione una interfaz");
			else
			{
				dialogoInterfaz dialogo = new dialogoInterfaz(esteFrame, xCentral, yCentral, "Modificar interfaz", interfaces.getNombre(indice), interfaces.getIP(indice), interfaces.getMascara(indice), interfaces.getDirEnlace(indice), interfaces.getconecta(indice), interfaces.dameNombresInterfaz(false, interfaces.getNombre(indice)), lista.getListaIPConecta(conexiones, interfaces.getIP(indice)), conexiones, lista.getTipos(conexiones));

				if (dialogo.getBoton().compareTo("Aceptar")==0)
				{
					nombre = interfaces.getNombre(indice);
					
					// Cambiamos la IP correspondiente en la lista de envios
					/*String temp;
					int j;
					for (int i=0; i<listaAcciones.size(); i+=5)
						for (j=i; j<i+2; j++)
						{
							temp = (String)listaAcciones.elementAt(j); 
							if (temp.indexOf(interfaces.getIP(indice))!=-1)
							{
								temp = temp.substring(0, temp.indexOf(" "));
								temp = temp + "(" + dialogo.getIP() + ")";
								listaAcciones.setElementAt(temp, j);
							}
						}*/
					
					interfaces.modifica(nombre, interfaces.getIP(indice), interfaces.getMascara(indice), interfaces.getDirEnlace(indice), interfaces.getconecta(indice), dialogo.getNombre(), dialogo.getIP(), dialogo.getMascara(), dialogo.getMAC(), dialogo.getConecta());
					panelInterfaces.setTabla(interfaces);
				}
				
				dialogo.destruye();
			}
		}
		else if (nombre.compareTo("BorrarInterfaz")==0 && panelInterfaces.estaActivo("BorrarInterfaz"))
		{
			int indice = panelInterfaces.getFilaTabla();
			
			if (indice==-1)
				muestraAviso.mensaje(esteFrame, "Seleccione una interfaz");
			else
			{
				nombre = interfaces.getNombre(indice);
				String ip = interfaces.getIP(indice);
				
				interfaces.borra(interfaces.getNombre(indice), interfaces.getIP(indice), interfaces.getMascara(indice), interfaces.getDirEnlace(indice), interfaces.getconecta(indice));
				
				// Borramos el envio configurado a traves de esta IP
				/*for (int i=0; i<listaAcciones.size();)
					if (((String)listaAcciones.elementAt(i)).indexOf(ip)!=-1 || ((String)listaAcciones.elementAt(i+1)).indexOf(ip)!=-1)
					{
						listaAcciones.removeElementAt(i);
						listaAcciones.removeElementAt(i);
						listaAcciones.removeElementAt(i);
						listaAcciones.removeElementAt(i);
						listaAcciones.removeElementAt(i);
					}
					else
						i+=5;
				*/
				if (rutas.cuentaRutas(nombre)>0)
					muestraAviso.mensaje(esteFrame, "Es posible que alguna ruta haya quedado inutilizada.");

				panelInterfaces.setTabla(interfaces);
			}
		}
		else if (nombre.compareTo("AñadirRuta")==0 && panelRuta.estaActivo("AñadirRuta"))
		{
			rutas = panelRuta.getRutas();
			Vector nombresInterfaces = new Vector(interfaces.dameNombresInterfaz(true, ""));
			
			if (nombresInterfaces.size()==0)
				muestraAviso.mensaje(esteFrame, "No es posible crear rutas si no existen interfaces.");
			else
			{
				dialogoRuta dialogo = new dialogoRuta(esteFrame, xCentral, yCentral, "Insertar ruta", "", "", "", "", nombresInterfaces);
			
				if (dialogo.getBoton().compareTo("Aceptar")==0)
				{
					rutas.inserta(dialogo.getDestino(), dialogo.getMascara(), dialogo.getGateway(), dialogo.getNombreInterfaz());
					panelRuta.setTabla(rutas);
				}
			
				dialogo.destruye();
			}
		}
		else if (nombre.compareTo("ModificarRuta")==0 && panelRuta.estaActivo("ModificarRuta"))
		{
			rutas = panelRuta.getRutas();
			int indice = panelRuta.getFilaTabla();
			
			if (indice==-1)
				muestraAviso.mensaje(esteFrame, "Seleccione una ruta");
			else
			{
				dialogoRuta dialogo = new dialogoRuta(esteFrame, xCentral, yCentral, "Modificar ruta", rutas.getDestino(indice), rutas.getMascara(indice), rutas.getGateway(indice), rutas.getNombreInterfaz(indice), interfaces.dameNombresInterfaz(true, ""));
				
				if (dialogo.getBoton().compareTo("Aceptar")==0)
				{
					rutas.modifica(rutas.getDestino(indice), rutas.getMascara(indice), rutas.getGateway(indice), rutas.getNombreInterfaz(indice), dialogo.getDestino(), dialogo.getMascara(), dialogo.getGateway(), dialogo.getNombreInterfaz());
					panelRuta.setTabla(rutas);
				}
				
				dialogo.destruye();
			}
		}
		else if (nombre.compareTo("BorrarRuta")==0 && panelRuta.estaActivo("BorrarRuta"))
		{
			rutas = panelRuta.getRutas();
			int indice = panelRuta.getFilaTabla();
			
			if (indice==-1)
				muestraAviso.mensaje(esteFrame, "Seleccione una ruta");
			else
			{
				rutas.borra(rutas.getDestino(indice), rutas.getMascara(indice), rutas.getGateway(indice), rutas.getNombreInterfaz(indice));
				panelRuta.setTabla(rutas);
			}
		}
		else if (nombre.compareTo("BorrarConexion")==0 && panelConexiones.estaActivo("BorrarConexion"))
		{
			nombre = panelConexiones.getDato();
			
			if (nombre == null)
				muestraAviso.mensaje(esteFrame, "Seleccione una conexion");
			else
			{
				// Borramos las interfaces relacionadas en la conexion que eliminamos
				interfaces.borraConectado(nombre);
				panelInterfaces.setTabla(interfaces);
			
				interfaces.modificaConectado(nomEquipo);
				panelInterfaces.setTabla(interfaces);
			
				// Borramos la conexion del 
				conexiones.borra(panelConexiones.getDato());
				lista.borraConexionesDesdeHacia(nombre, nomEquipo);
				// Ademas de las conexiones del equipo, debemos pasarle las interfaces que usa para dichas conexiones.
				panelConexiones.setTabla(ordenaVector.getOrdenado(conexiones), interfaces);
			}
		}
		else if (nombre.compareTo("Aceptar")==0)
		{
			// Eliminamos caracteres de control y espacios en blanco
			txtNombre.setText(txtNombre.getText().trim());

			// Comprobamos si el nombre ya existe en la lista
			if (lista.buscaEquipo(txtNombre.getText(), posicion)!=-1)
			{
				if(!muestraAviso.mensajeConfirmacion(esteFrame, "El nombre ya existe en la topologia, en caso de tratarse de un equipo puede casar efectos indeseados.  Desea continuar?"))
					txtNombre.grabFocus();
				else
				{
					textoBoton = "Aceptar";
					rutas = panelRuta.getRutas();
					
					// Cambiamos el nombre en la lista de envios
					// Un disenyo de listaEnvios como objeto permitiria hacer esto
					// de forma mas limpia. Lo mismo ocurre al modificar o borrar
					// una interface
					if (getnomEquipo().compareTo(lista.getNombre(posicion))!=0)
					{
						String temp;
						int j;
						for (int i=0; i<listaAcciones.size(); i+=5)
							for (j=i; j<i+2; j++)
							{
								temp = (String)listaAcciones.elementAt(j); 
								if (temp.indexOf(lista.getNombre(posicion))!=-1)
								{
									temp = temp.substring(temp.indexOf(" "));
									temp = getnomEquipo() + temp;
									listaAcciones.setElementAt(temp, j);
								}
							}
					}

					
					setVisible(false);

				}
			}
			else
			{
				textoBoton = "Aceptar";
				rutas = panelRuta.getRutas();
				
				// Cambiamos el nombre en la lista de envios
				// Un disenyo de listaEnvios como objeto permitiria hacer esto
				// de forma mas limpia. Lo mismo ocurre al modificar o borrar
				// una interface
				if (getnomEquipo().compareTo(lista.getNombre(posicion))!=0)
				{
					String temp;
					int j;
					for (int i=0; i<listaAcciones.size(); i+=5)
						for (j=i; j<i+2; j++)
						{
							temp = (String)listaAcciones.elementAt(j); 
							if (temp.indexOf(lista.getNombre(posicion))!=-1)
							{
								temp = temp.substring(temp.indexOf(" "));
								temp = getnomEquipo() + temp;
								listaAcciones.setElementAt(temp, j);
							}
						}
				}

				
				setVisible(false);
			}
		}
		else if (nombre.compareTo("Cancelar")==0)
		{
			textoBoton = "Cancelar";
			setVisible(false);
		}
	}

	public void mouseEntered(MouseEvent evt) {}
	public void mouseExited(MouseEvent evt) {}
	public void mousePressed(MouseEvent evt) {}
	public void mouseReleased(MouseEvent evt) {}
	
	/** Hace visible el cuadro de dialogo */
	public void muestra()
	{
		this.setVisible(true);
	}

	/** Establece todos los datos necesarios para gestionar las propiedades del equipo */
	private void setDatos(listaInterfaces interfaces, listaRutas rutas, listaConexiones conexionesEquipo)
	{
		conexiones = new listaConexiones();
		this.interfaces = interfaces.copia();
		this.rutas = rutas.copia();
		this.conexiones.copia(conexionesEquipo);
	}
}
