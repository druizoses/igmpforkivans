/** @author: tlfs & afzs */
package objetoVisual;

import java.awt.Image;
import java.awt.Point;
import java.util.Vector;

import util.utilTexto;
import visSim.listaInterfaces;
import visSim.listaRutas;

/** Clase que implementa la gestion de objetos visuales en la topologia */
public class listaObjetosVisuales extends Vector
{
	public int incDistanx;
	public int incDistany;
	
	public Vector listaAcciones;
	
	public propiedadesTopologia propiedades;
	
	public listaObjetosVisuales()
	{
		incDistanx=0;
		incDistany=0;
		
		listaAcciones = new Vector();
		propiedades = new propiedadesTopologia();
	}

	/** Elimina las conexiones hacia una maquina
	 * @param pos Posicion del objeto
	 * @param nombre Nombre de la maquina de la que se eliminan las conexiones
	 */
	public void borraConexionesHacia(int pos, String nombre)
	{
		getConexiones(pos).borraConexionesHacia(nombre);
	}
	
	/** Devuelve la imagen que representa visualmente un objeto */
	public Image dibuja(int pos, boolean grabando)
	{
		return getObjeto(pos).dibuja(grabando);
	}
	
	public boolean EsCero()
	{
		return (incDistanx==0 && incDistany==0);
	}	
	
	/** Devuelve la cadena con que el simulador identifica al objeto situado en pos */
	public String getCadenaSimulador(int pos)
	{
		return getObjeto(pos).getCadenaSimulador();
	}

	/** Devuelve un booleano que indica si el objeto esta seleccionado para conexion */
	public boolean getConecta(int pos)
	{
		return getObjeto(pos).getConecta();
	}

	/** Devuelve las conexiones de un equipo */
	public listaConexiones getConexiones(int pos)
	{
		return getObjeto(pos).getConexiones();
	}

	/** Devuelve las coordenadas de conexion del objeto */
	public Point getCoordenadasConexion(int pos, int xOtro, int yOtro, int anchoOtro, int altoOtro)
	{
		return getObjeto(pos).getCoordenadasConexion(xOtro, yOtro, anchoOtro, altoOtro);	
	}

	/** Devuelve un Vector con los valores de la simulacion de cada tipo de error */
	public Vector getErrores(int pos)
	{
		return new Vector(((objetoVisual)elementAt(pos)).getErrores());
	}
	
	/** Devuelve la altura del objeto */
	public int getHeight(int pos)
	{
		return getObjeto(pos).getHeight();
	}
	
	/** Devuelve el identificador del equipo situado en pos */
	public String getID(int pos)
	{
		return getObjeto(pos).getID();
	}
	
	/** Devuelve la lista de interfaces de un equipo */
	public listaInterfaces getInterfaces(int pos)
	{
		return getObjeto(pos).getInterfaces();
	}

	public Vector getlistaAcciones()
	{
		return new Vector(listaAcciones);
	}
	
	/** Devuelve el nombre del equipo que se encuentra en pos */
	public String getNombre(int pos)
	{
		return getObjeto(pos).getNombre();
	}
	
	/** Devuelve la configuracion de la simulacion de errores del equipo situado en pos */
	public Vector getNombresSeleccionesErrores(int pos)
	{
		return new Vector(((objetoVisual)elementAt(pos)).getNombresSeleccionesErrores());
	}

	/** Devuelve el icono del equipo que se encuentra en pos */
	public String getNomIcono(int pos)
	{
		return getObjeto(pos).getNomIcono();
	}
	
	/** Devuelve el objeto que se encuentra en una posicion */
	public objetoVisual getObjeto(int pos)
	{
		return (objetoVisual)elementAt(pos);
	}
	
	/** Devuelve las propiedades de la topologia */
	public propiedadesTopologia getPropiedades()
	{
		return propiedades;
	}
		
	/** Devuelve la lista de rutas de un equipo */
	public listaRutas getRutas(int pos)
	{
		return getObjeto(pos).getRutas();
	}
	
	/** Devuelve un booleano que indica si el objeto esta seleccionado */
	public boolean getSeleccionado(int pos)
	{
		return getObjeto(pos).getSeleccionado();
	}

	/** Devuelve la posicion del primer equipo seleccionado para conexion o -1 si no encuentra ninguno */
	public int getSeleccionadoConexion()
	{
		int tam = size();

		for (int i=0; i<tam; i++)
			if (getConecta(i))
				return i;
		
		return -1;
	}

	/** Devuelve el objeto utilTexto del equipo situado en pos */ 
	public utilTexto getTextoVisual(int pos)
	{
		return getObjeto(pos).getTextoVisual();
	}
	
	/** Devuelve un Vector conteniendo las interfaces del equipo situado en pos */
	public Vector getVectorInterfaces(int pos)
	{
		return getObjeto(pos).getVectorInterfaces();
	}

	/** Devuelve un Vector conteniendo las rutas del equipo situado en pos */
	public Vector getVectorRutas(int pos)
	{
		return getObjeto(pos).getVectorRutas();
	}

	/** Devuelve la anchura del objeto */
	public int getWidth(int pos)
	{
		return getObjeto(pos).getWidth();
	}
	
	/** Devuelve la coordenada X del equipo que se encuentra en pos */
	public int getX(int pos)
	{
		return getObjeto(pos).getX();
	}

	/** Devuelve la coordenada Y del equipo que se encuentra en pos */
	public int getY(int pos)
	{
		return getObjeto(pos).getY();
	}

	public void GuardaDistan(int dx, int dy)
	{
		incDistanx=dx;
		incDistany=dy;
	}

	/** Inserta un nuevo objeto al vector */
	public void insertaObjeto(objetoVisual obj)
	{
		addElement(obj);
	}
	
	/** Devuelve un booleano indicando si el equipo situado en pos puede conectarse al equipo recibido */
	public boolean puedeConectarse(int pos, String equipo)
	{
		return getObjeto(pos).puedeConectarse(equipo);
	}

	/** Aquellos equipos seleccionados dejan de estarlo */
	public void quitaPinchadoConexion()
	{
		setConecta(getSeleccionadoConexion(), false);
	}
	
	/** Pone el atributo seleccion al valor recibido */
	public void seleccionaTodos(boolean valor)
	{
		int tam = size();

		for (int i=0; i<tam; i++)
			setSeleccionado(i, valor);
	}

	/** Establece un booleano que indica si el objeto esta seleccionado para conexion */
	public void setConecta(int pos, boolean valor)
	{
		if (pos!=-1)
			getObjeto(pos).setConecta(valor);
	}

	/** Establece las conexiones de un equipo */
	public void setConexiones(int pos, Vector conexiones)
	{
		getObjeto(pos).setConexiones(new Vector(conexiones));
	}
	
	/** Establece los errores que simula el equipo situado en pos */
	public void setErrores(int pos, Vector errores)
	{
		((objetoVisual)elementAt(pos)).setErrores(errores);
	}

	/** Establece las interfaces del equipo situado en pos */
	public void setInterfaces(int pos, listaInterfaces lista)
	{
		getObjeto(pos).setInterfaces(lista);
	}
	
	public void setlistaAcciones(Vector envios)
	{
		listaAcciones = new Vector(envios);
	}

	/** Establece el nombre del equipo que se encuentra en pos */
	public void setNombre(int pos, String nombre)
	{
		getObjeto(pos).setNombre(nombre);
	}
	
	/** Establece las propiedades de la topologia */
	public void setPropiedades(propiedadesTopologia propiedades)
	{
		this.propiedades = new propiedadesTopologia(propiedades);
	}

	/** Establece las rutas del equipo situado en pos */
	public void setRutas(int pos, listaRutas rutas)
	{
		getObjeto(pos).setRutas(rutas);
	}

	/** Establece un booleano que indica si el objeto esta seleccionado */
	public void setSeleccionado(int pos, boolean valor)
	{
		if (pos!=-1)
			getObjeto(pos).setSeleccionado(valor);
	}
	
	/** Establece la coordenada X del equipo que se encuentra en pos */
	public void setX(int pos, int valor)
	{
		((objetoVisual)elementAt(pos)).setX(valor);
	}

	/** Establece la coordenada Y del equipo que se encuentra en pos */
	public void setY(int pos, int valor)
	{
		((objetoVisual)elementAt(pos)).setY(valor);
	}
	
	/** Devuelve el numero de objetos de la lista */
	public int tam()
	{
		return size();
	}
}
