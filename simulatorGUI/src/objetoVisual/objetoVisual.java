/** @author: tlfs & afzs */
package objetoVisual;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JComponent;

import util.colores;
import util.nomiconos;
import util.utilTexto;
import visSim.interfazVisual;
import visSim.listaInterfaces;
import visSim.listaRutas;
import visSim.nombresSeleccionesErrores;
import visSim.rutaVisual;


public abstract class objetoVisual extends JComponent
{
	/** Variable que controla cuando este objeto este conectando con otro */
	private boolean conecta;
	
	/** Lista de los tipos de equipos a los que se puede conectar */
	public Vector conectables;
	
	/** Lista de conexiones a otros objetos (lineas visuales) */
	public listaConexiones conexiones;
	
	/** Errores a simular por este objeto */
	public Vector errores;

	/** Lista de interfaces del objeto */
	private listaInterfaces interfaces;

	/** Nombre del objeto */
	public String nombre;
	
	/** Lista de rutas del objeto */
	private listaRutas rutas;
	
	/** Se pondra a true cuando este seleccionado con otro equipo */
	private boolean seleccionado;
	
	/** Anyade una interfaz al objeto
	 * @param interfaz Interfaz visual que se anyade
	 */
	public void addInterfaz(interfazVisual interfaz)
	{
		interfaces.inserta(interfaz);
	}

	/** Anyade una ruta al objeto
	 * @param ruta Ruta visual que se anyade
	 */
	public void addRuta(rutaVisual ruta)
	{
		rutas.add(ruta);
	}
	
	/** Cambia el nombre de un equipo en la lista de interfaces
	 * @param nombreViejo Nombre anterior
	 * @param nombreNuevo Nombre nuevo
	 */
	public void cambiaConexionesInterfaz(String nombreViejo, String nombreNuevo)
	{
		interfaces.cambiaNombreEquipo(nombreViejo, nombreNuevo);
	}

	/** Cambia el nombre de un equipo de la lista de conexiones
	 * @param nombreViejo Nombre anterior
	 * @param nombreNuevo Nombre nuevo
	 */
	public void cambiaNombresConexiones(String nombreViejo, String nombreNuevo)
	{
		conexiones.cambiaNombresConexiones(nombreViejo, nombreNuevo);
	}
	
	/** Metodo clone de la clase
	 * Cada equipo debe implementar el metodo segun sus caracteristicas. 
	 */
	public abstract Object clone();
	
	/** Metodo de copia de un objetoVisual.
	 * Realiza la construccion de este objeto a partir del recibido.
	 */
	public void copia(objetoVisual origen)
	{
		conexiones = new listaConexiones();
		interfaces = new listaInterfaces();
		rutas = new listaRutas();
		errores = new Vector();
		
		set(origen.getNombre(), origen.getX(), origen.getY(), origen.getWidth(), origen.getHeight());
		setConexiones(origen.getConexiones());
		setConecta(origen.getConecta());
		setInterfaces(origen.getInterfaces());
		setRutas(origen.getRutas());
	}
	
	/** Metodo dibuja para poner sobre el panel el icono que representa el objeto.
	 * Cuando el dibujo tenga caracteristicas especiales (bus, tokenring, etiqueta) sus correspondientes
	 * clases implementaran el metodo. 
	 * @param grabando
	 * @return Image con la representacion del objeto
	 */
	public Image dibuja(boolean grabando)
	{
		int tamx = nomiconos.tam;
		int tamy = nomiconos.tam;
		
		if (getSeleccionado())
		{
			tamx-=1;
			tamy-=1;
		}

		BufferedImage bi = new BufferedImage(tamx, tamy, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (java.awt.Graphics2D)bi.getGraphics();

		g.setPaintMode();
		g.setColor(colores.fondo);
		g.fillRect(0,0, 100, 100);
		
		if (!grabando)
		{
			// Dibujamos lineas de seleccion
			if (getSeleccionado())
				g.setColor(colores.seleccionado);
			else if (getConecta())
				g.setColor(colores.conecta);

			g.drawLine(0,0, tamx, 0);
			g.drawLine(0, tamy-1, tamx, tamy-1);
			g.drawLine(0,0, 0, tamy-1);
			g.drawLine(tamx-1, 0, tamx-1, tamy-1);
		}

		g.setColor(colores.negro);
		
		g.drawImage(getToolkit().getImage(getNomIcono()), 0, 0, null);

		return bi;
	}

	/** Devuelve la cadena que identifica al objeto dentro del simulador */
	public abstract String getCadenaSimulador();
	
	/** Indica si el objeto esta siendo conectado a otro equipo
	 * Se pone a true cuando se este dibujando una linea entre un par de equipos
	 * @return boolean
	 */
	public boolean getConecta()
	{
		return conecta;
	}
	
	/** Devuelve la lista de equipos a los que este se encuentra conectado */
	public listaConexiones getConexiones()
	{
		return conexiones;
	}

	/** Devuelve un objeto Point conteniendo las coordenadas de conexion de la linea */
	public Point getCoordenadasConexion(int xOtro, int yOtro, int anchoOtro, int altoOtro)
	{
		return new Point(getX()+getWidth()/2, getY()+getHeight()/2);
	}
	
	/** Devuelve un Vector conteniendo los errores simulables por este equipo */
	public Vector getErrores()
	{
		return new Vector(errores);
	}
	
	/** Devuelve la altura del equipo */
	public int getHeight()
	{
		return getBounds().height;
	}
	
	/** Devuelve el identificador del objeto.
	 * Se utilizan los dos primeros caracteres de su icono en minusculas
	 */
	public String getID()
	{
		return (((new nomiconos()).getNombre(getNomIcono())).substring(0,2)).toLowerCase();
	}
	
	/** Devuelve la lilsta de interfaces del equipo */
	public listaInterfaces getInterfaces()
	{
		return interfaces;
	}
	
	/** Devuelve el nombre del equipo */
	public String getNombre()
	{
		return nombre;
	}
	
	/** Devuelve la configuracion de la simulacion de errores de este equipo
	 * En el Vector devuelto se incluye el nombre del error segun Simulador
	 * y su descripcion.
	 **/
	public Vector getNombresSeleccionesErrores()
	{
		return nombresSeleccionesErrores.getErrores(errores);
	}
	
	/** Devuelve la ruta del icono que representa a este equipo
	 *  Cada clase que derive de esta debera implementar el metodo.
	 **/
	public abstract String getNomIcono();

	/** Devuelve las rutas configuradas en este equipo */
	public listaRutas getRutas()
	{
		return rutas;
	}
	
	/** Indica si el objeto se encuentra seleccionado */
	public boolean getSeleccionado()
	{
		return seleccionado;
	}
	
	/** Devuelve el tamanyo del vector conectables */
	public int getTamConectables()
	{
		return conectables.size();
	}
	
	/** Devuelve el nombre del equipo para su representacion visual */
	public utilTexto getTextoVisual()
	{
		if (getNomIcono().compareTo(nomiconos.nomEtiqueta)!=0)
		{
			FontMetrics metrica = this.getFontMetrics(new Font("Dialog.plain", -1, 12));
			return new utilTexto(nombre, getX()+getWidth()/2 - metrica.stringWidth(nombre)/2, getY()+getHeight(), metrica.stringWidth(nombre), metrica.getAscent());
		}
		
		return null;
	}
	
	/** Devuelve las interfaces del equipo en un Vector */
	public Vector getVectorInterfaces()
	{
		Vector dev = new Vector();
		
		for (int i=0; i<interfaces.tam(); i++)
		{
			dev.add(interfaces.getNombre(i));
			dev.add(interfaces.getIP(i));
			dev.add(interfaces.getMascara(i));
			dev.add(interfaces.getDirEnlace(i));
			dev.add(interfaces.getconecta(i));
		}
		
		return dev;
	}
	
	/** Devuelve las rutas del equipo en un Vector */
	public Vector getVectorRutas()
	{
		Vector dev = new Vector();
		
		for (int i=0; i<rutas.tam(); i++)
		{
			dev.add(rutas.getDestino(i));
			dev.add(rutas.getMascara(i));
			dev.add(rutas.getGateway(i));
			dev.add(rutas.getNombreInterfaz(i));
		}
		
		return dev;
	}
	
	/** Devuelve la anchura del equipo */
	public int getWidth()
	{
		return getBounds().width;
	}
	
	/** Devuelve la coordenada x del equipo */
	public int getX()
	{
		return getBounds().x;
	}
	
	/** Devuelve la coordenada y del equipo */
	public int getY()
	{
		return getBounds().y;
	}
	
	/** Inserta una nueva conexion al equipo */
	public void insertaConexion(String nombre)
	{
		conexiones.inserta(nombre);
	}
	
	/** Devuelve la posicion en la lista de interfaces de la primera interfaz libre */
	public int primeraInterfazLibre()
	{
		int dev = -1;
		
		for (int i=0; i<interfaces.size() && dev==-1; i++)
			if (interfaces.getconecta(i).compareTo("N/A")==0)
				dev=i;
		
		return dev;
	}
	
	/** Devuelve un boolean indicando si este equipo puede conectarse al indicado */
	public boolean puedeConectarse(String identificador)
	{
		boolean puede = false;
		
		for (int i=0; i<conectables.size() && !puede; i++)
			if (((String)conectables.elementAt(i)).compareTo(identificador)==0)
				puede = true;

		return puede;
	}
	
	/** Metodo que establece los atributos del objeto
	 * @param nombre Nombre del objeto
	 * @param x Coordenada x del objeto
	 * @param y Coordenada y del objeto
	 * @param tamx
	 * @param tamy
	 */
	public void set(String nombre, int x, int y, int tamx, int tamy)
	{
		conecta = false;
		seleccionado = false;
		conexiones = new listaConexiones();
		interfaces = new listaInterfaces();
		rutas = new listaRutas();
		
		this.nombre = nombre;
		setDoubleBuffered(true);

		setBounds(x, y, tamx, tamy);
	}
	
	/** Establece el valor de la variable conecta */
	public void setConecta(boolean valor)
	{
		conecta = valor;
	}
	
	/** establece las conexiones del equipo a partir de un objeto listaConexiones */
	public void setConexiones(listaConexiones conexiones)
	{
		this.conexiones.copia(conexiones);
	}
	
	/** establece las conexiones del equipo a partir de un vector */
	public void setConexiones(Vector conexiones)
	{
		this.conexiones.copia(conexiones);
	}
	
	/** Establece las coordenadas del equipo
	 * 
	 * @param x Coordenada x
	 * @param y Coordenada y
	 */
	public void setCoord(int x, int y)
	{
		setBounds(x, y, getWidth(), getHeight());
	}
	
	/** Configura los errores simulables por este equipo */
	public void setErrores(Vector errores)
	{
		this.errores = new Vector(errores);
	}
	
	/** Establece la altura del equipo */
	public void setHeight(int valor)
	{
		setBounds(getX(), getY(), getWidth(), valor);
	}
	
	/** Establece las interfaces del equipo a partir de un objeto listaInterfaces
	 * @param lista
	 * @see listaInterfaces
	 */
	public void setInterfaces(listaInterfaces lista)
	{
		if (lista != null)
		{
			interfaces.clear();
		
			interfaces = new listaInterfaces();
		
			for (int i=0; i<lista.tam(); i++)
				addInterfaz(lista.getInterfaz(i));
		}
	}
	
	/** Establece el equipo al que se conecta una interfaz
	 * @param pos Posicion de la interfaz dentro de la lista de interfaces
	 * @param nombre Nombre del equipo al que se conecta la interfaz
	 */
	public void setInterfazConecta(int pos, String nombre)
	{
		interfaces.setconecta(pos, nombre);
	}

	/** Establece el nombre del objeto */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
	
	/** Establece las rutas del equipo a partir de un objeto listaRutas */
	public void setRutas(listaRutas lista)
	{
		if (lista != null)
		{
			rutas.clear();
		
			rutas = new listaRutas();
		
			for (int i=0; i<lista.tam(); i++)
				addRuta(lista.getRuta(i));
		}
	}
	
	/** Establece la seleccion del equipo segun el valor recibido */
	public void setSeleccionado(boolean valor)
	{
		seleccionado = valor;
	}
	
	/** Metodo para establecer las interfaces contenidas en el Vector recibido
	 * @param interfaces Vector de interfaces a anyadir
	 */
	public void setVectorInterfaces(Vector interfaces)
	{
		for (int i=0; i<interfaces.size(); i+=5)
			addInterfaz(new interfazVisual((String)interfaces.elementAt(i), (String)interfaces.elementAt(i+1), (String)interfaces.elementAt(i+2), (String)interfaces.elementAt(i+3), (String)interfaces.elementAt(i+4)));
	}
	
	/** Establece las rutas del equipo a partir de un vector */
	public void setVectorRutas(Vector temp)
	{
		for (int i=0; i<temp.size(); i+=4)
			addRuta(new rutaVisual((String)temp.elementAt(i), (String)temp.elementAt(i+1), (String)temp.elementAt(i+2), (String)temp.elementAt(i+3)));
	}
	
	/** Establece el ancho del equipo */
	public void setWidth(int valor)
	{
		setBounds(getX(), getY(), valor, getHeight());
	}
	
	/** Establece la coordenada X del objeto */
	public void setX(int valor)
	{
		setBounds(valor, getY(), getWidth(), getHeight());
	}

	/** Establece la coordenada Y del objeto */
	public void setY(int valor)
	{
		setBounds(getX(), valor, getWidth(), getHeight());
	}
}
