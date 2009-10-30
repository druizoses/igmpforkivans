/** @author: tlfs & afzs */
package frameInternoDibujo;

import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.beans.PropertyVetoException;
import java.util.Vector;

import java.awt.Frame;
import javax.swing.JInternalFrame;

import objetoVisual.listaObjetos;

/** La clase listaFramesInternos gestiona las ventanas de las topologias
 * que el usuario tiene abiertas.
 */
public class listaFramesInternos extends Vector
{
	/** Lista de objetos para buffer de copiado */
	private listaObjetos objetosCopiados;
	
	/** Formato de pagina para imprimir */
	private PageFormat pf;

	/** Trabajo de impresion */
	private PrinterJob prnJob;

	/** Constructor de la clase */
	public listaFramesInternos()
	{
		prnJob = null;
		pf = null;
		objetosCopiados = null;
	}

	/** Crea y anyade un nuevo frameInternoDibujo al vector */
	public void add(ActionListener oyente, Frame framePadre, String nombre)
	{
		add(new frameInternoDibujo(oyente, framePadre, nombre));
		setFicheroTopo(size()-1, nombre + ".net");
		
		// Ahora copiamos el buffer para pegar objetos (si existe)
		if (objetosCopiados != null)
			getFrameInterno(size()-1).ponCopias(objetosCopiados.copiaLista());
	}
	
	/** Abre la ventana de busqueda de equipos en la topologia indicada en pos */
	public void busca(int pos)
	{
		getFrameInterno(pos).busca();
	}
	
	/** Busca una ventana en la lista dado el nombre del fichero de la topologia */
	public int buscaVentana(String nombreVentana)
	{
		for (int i=0; i<tam(); i++)
			if (getFicheroTopo(i).compareTo(nombreVentana)==0)
				return i;
		
		return -1;
	}
	
	/** Llama al metodo cambiaMenu de la ventana activa */
	public String cambiaMenu()
	{
		if (getActivo()!=-1)
			return (getFrameInterno(getActivo()).cambiaMenu());
		return "";
	}
	
	/**  Llama al metodo cambiaMenu de la ventana indicada en pos */
	public String cambiaMenu(int pos)
	{
		return getFrameInterno(pos).cambiaMenu();
	}

	/** Modifica el titulo de la ventana
	 * @param nombreVentana Ventana de la cual se le cambia el titulo
	 * @param valor Valor que indica si se pone un * para indicar que la topologia ha sido modificada
	 * @return Nombre de la ventana mas el asterisco (si se ha tenido que poner)
	 */
	public String cambiaTitulo(String nombreVentana, boolean valor)
	{
		int pos = buscaVentana(nombreVentana);
		String complemento="";
		
		if (valor)
			complemento = " (*)";
		
		((JInternalFrame)getFrameInterno(pos)).setTitle(nombreVentana + complemento);
		
		return nombreVentana + complemento;
	}

	/** Llama al metodo centrarAmbos de la ventana indicada en pos*/
	public void centrarAmbos(int pos)
	{
		(getFrameInterno(pos)).centrarAmbos();
	}

	/** Llama al metodo centrarHorizontal de la ventana indicada en pos*/
	public void centrarHorizontal(int pos)
	{
		(getFrameInterno(pos)).centrarHorizontal();
	}

	/** Llama al metodo centrarVertical de la ventana indicada en pos*/
	public void centrarVertical(int pos)
	{
		(getFrameInterno(pos)).centrarVertical();
	}
	
	/** Cierra la ventana indicada en pos */
	public void cierraVentana(int pos)
	{
		removeElementAt(pos);
	}

	/** Llama al metodo compruebaSimulacion de la ventana activa */
	public Vector compruebaSimulacion()
	{
		if (getActivo()!=-1)
			return getFrameInterno(getActivo()).compruebaSimulacion();
		
		return null;
	}
	
	/** Llama al metodo condiguraEnvios de la ventana activa 
	 * @param xCentral Coordenada x central de la pantalla
	 * @param yCentral Coordenada y central de la pantalla
	 */
	public void configuraAcciones(int xCentral, int yCentral)
	{
		if (getActivo()!=-1)
			getFrameInterno(getActivo()).configuraAcciones(xCentral, yCentral);
	}

	/** Metodo de configuracion del formato de pagina para impresion
	 * Haciendo la implementacion en esta clase se consigue que todas las
	 * ventanas se puedan imprimir con el mismo formato de pagina.
	 */
	public void configuraImpresion()
	{
		int tam = size();

		if (prnJob == null)
		{
			prnJob = PrinterJob.getPrinterJob();
			pf = prnJob.pageDialog(prnJob.defaultPage());
		}
		else
			pf = prnJob.pageDialog(pf);
		
		for (int i=0; i<tam; i++)
			(getFrameInterno(i)).configuraImpresion(prnJob, pf);
	}
	
	/** Copia de equipos seleccionados
	 * @param pos Posicion de la ventana
	 */
	public void copiaSeleccionados(int pos)
	{
		(getFrameInterno(pos)).copiaSeleccionados();
	}
	
	/** Llama al metodo cortar de la ventana indicada en pos */
	public void cortar(int pos)
	{
		(getFrameInterno(pos)).cortar();
		
		// Al cortar tb nos quedamos con la lista de objetos copiados
		objetosCopiados = (getFrameInterno(pos)).dameCopias().copiaLista();
	}

	/** Devuelve una lista de nombres de topologias modificadas (las que
	 * aun no han sido almacenadas antes de cerrar la aplicacion)
	 */
	public Vector dameListaModificados()
	{
		Vector lista = new Vector();
		
		for (int i=0; i<tam(); i++)
			if (getCambios(i))
				lista.add(getFrameInterno(i).getFicheroTopo());
		
		return new Vector(lista);
	}
	
	/** Devuelve una lista de nombres de todas las topologias */
	public Vector dameListaNombres()
	{
		Vector lista = new Vector();
		
		for (int i=0; i<tam(); i++)
			lista.add(getFrameInterno(i).getFicheroTopo());
		
		return new Vector(lista);
	}
	
	/** Desactiva todas las ventanas excepto la indicada en el parametro */
	public void desactivaTodos(int posNoDesactivar)
	{
		int tam = size();

		for (int i=tam-1; i>=0; i--)
			if (i!=posNoDesactivar)
				ponActiva(i, false);
	}

	/** Detiene la simulacion de la ventana indicada en pos */
	public void detenerSimulacion()
	{
		if (getActivo()!=-1)
			getFrameInterno(getActivo()).detenerSimulacion();
	}

	/** Distribuye todas las ventanas de forma horizontal segun el ancho y
	 * el alto de la ventana de la aplicacion */
	public void distribuirHorizontal(int ancho, int alto)
	{
		minimizaTodas();
		if (ancho != 0)
		{
			int propAlto = alto/tam();

			for (int i=0; i<tam(); i++)
				try
				{
					// Si esta como icono lo abrimos
					((JInternalFrame)getFrameInterno(i)).setIcon(false);
					((JInternalFrame)getFrameInterno(i)).setBounds(1, i*propAlto+1, ancho, propAlto);

					((JInternalFrame)getFrameInterno(i)).setSelected(true);
				}
				catch(PropertyVetoException e)
				{
					System.err.println("No se puede situar " + getFicheroTopo(i));
				}
		}
	}
	
	/** Distribuye todas las ventanas de forma vertical segun el ancho y
	 * el alto de la ventana de la aplicacion */
	public void distribuirVertical(int ancho, int alto)
	{
		minimizaTodas();
		if (ancho != 0)
		{
			int propAncho = ancho/tam();

			for (int i=0; i<tam(); i++)
				try
				{
					// Si esta como icono lo abrimos
					((JInternalFrame)getFrameInterno(i)).setIcon(false);
					((JInternalFrame)getFrameInterno(i)).setBounds(i*propAncho+1, 1, propAncho, alto);
					((JInternalFrame)getFrameInterno(i)).setSelected(true);
				}
				catch(PropertyVetoException e)
				{
					System.err.println("No se puede situar " + getFicheroTopo(i));
				}
		}
	}

	/** Elimina los equipos seleccionados de la ventana indicada en pos */
	public void eliminaSeleccionados(int pos)
	{
		(getFrameInterno(pos)).eliminaSeleccionados();
	}
	
	/** Devuelve la posicion de la ventana activa */
	public int getActivo()
	{
		int tam = size();

		for (int i=0; i<tam; i++)
			if (((JInternalFrame)getFrameInterno(i)).isSelected())
				return i;
		
		return -1;
	}
	
	/** Devuelve un boolean informando si ha habido cambios en la ventana indicada en pos */
	public boolean getCambios(int pos)
	{
		return (getFrameInterno(pos)).getCambios();
	}

	/** Devuelve el nombre del fichero de la ventana indicada en pos */
	public String getFicheroTopo(int pos)
	{
		return (getFrameInterno(pos)).getFicheroTopo();
	}
	
	/** Devuelve el frameInternoDibujo que se encuentre en la posicion recibida */
	public frameInternoDibujo getFrameInterno(int pos)
	{
		return (frameInternoDibujo)elementAt(pos);
	}

	/** Devuelve la coordenada x de la ventana indicada en pos */
	public int getX(int pos)
	{
		return ((JInternalFrame)getFrameInterno(pos)).getX();
	}

	/** Devuelve la coordenada y de la ventana indicada en pos */
	public int getY(int pos)
	{
		return ((JInternalFrame)getFrameInterno(pos)).getY();
	}

	/** Llama al metodo grabaFichero de la ventana indicada en pos*/
	public boolean grabaFichero(int pos)
	{
		return (getFrameInterno(pos)).grabaFichero();
	}

	/** Llama al metodo grabaJPG de la ventana indicada en pos*/
	public void grabaJPG(int pos, String nomfich)
	{
		(getFrameInterno(pos)).grabaJPG(nomfich);
	}
	
	/** Llama al metodo imprimir de la ventana indicada en pos*/
	public void imprimir(int pos)
	{
		(getFrameInterno(pos)).imprimir();
	}

	/** Llama al metodo leeFichero de la ventana indicada en pos*/
	public boolean leeFichero(int pos, String nomfich)
	{
		return (getFrameInterno(pos)).leeFichero(nomfich);
	}
	
	/** Maximiza la ventana indicada en pos */
	public void maximiza(int pos)
	{
		try
		{
			((JInternalFrame)getFrameInterno(pos)).setMaximum(true);
		}
		catch(PropertyVetoException e)
		{
			System.err.println("");
		}
	}
	
	/** Minimiza todas las ventanas */
	public void minimizaTodas()
	{
		for (int i=0; i<tam(); i++)
			try
			{
				((JInternalFrame)getFrameInterno(i)).setIcon(true);
			}
			catch(PropertyVetoException e)
			{
				System.err.println("No se puede minimizar " + getFicheroTopo(i));
			}
	}
	
	/** Muestra los eventos producidos tras la simulacion en la ventana activa */ 
	public void muestraEventos()
	{
		if (getActivo()!=-1)
			getFrameInterno(getActivo()).muestraEventos();
	}

	/** Llama al metodo muestraPropi de la ventana indicada en pos*/
	public void muestraPropiedadesEquipo(int pos, int x, int y)
	{
		(getFrameInterno(pos)).muestraPropiedadesEquipo(x,y);
	}
	
	public void muestraPropiedadesTopologia(int pos)
	{
		getFrameInterno(pos).muestraPropiedadesTopologia();
	}
	
	/** Llama al metodo noDibujaSelecciones de la ventana indicada en pos*/
	public void noDibujaSelecciones(int pos, boolean valor)
	{
		getFrameInterno(pos).noDibujaSelecciones(valor);
	}

	/** Llama al metodo pegar de la ventana indicada en pos*/
	public void pegar(int pos)
	{
		(getFrameInterno(pos)).pegar();
	}
	
	/** Pone activa la ventana indicada en pos segun el valor recibido */
	public void ponActiva(int pos, boolean valor)
	{
		try
		{
			((JInternalFrame)getFrameInterno(pos)).setSelected(valor);
			if (valor)
				((JInternalFrame)getFrameInterno(pos)).moveToFront();
			else
				((JInternalFrame)getFrameInterno(pos)).moveToBack();
		}
		catch(PropertyVetoException ex)
		{
			System.err.println("Error al seleccionar.");
		}
	}
	
	/** Pone los objetos copiados en la ventana indicada en el nombre */
	public void ponCopiados(String nombreVentana)
	{
		int pos = buscaVentana(nombreVentana);
		
		objetosCopiados = (getFrameInterno(pos)).dameCopias().copiaLista();
		
		for (int i=0; i<tam(); i++)
			getFrameInterno(i).ponCopias(objetosCopiados.copiaLista());
	}
	
	/** Metodo para cambiar el cursor del raton a todas las ventanas */
	public void ponCursor(String Cursor, int xCentral, int yCentral)
	{
		int tam = size();

		for (int i=0; i<tam; i++)
			(getFrameInterno(i)).ponCursor(Cursor, xCentral, yCentral);
	}

	/** Metodo para poner en cascada todas las ventanas */
	public void ponerEnCascada()
	{
		minimizaTodas();
		for (int i=0; i<tam(); i++)
			try
			{
				// Si esta como icono lo abrimos
				((JInternalFrame)getFrameInterno(i)).setIcon(false);
				((JInternalFrame)getFrameInterno(i)).setBounds((i+1)*10, (i+1)*10, 300, 250);
				
				// Ponemos la ultima ventana como seleccionada
				((JInternalFrame)getFrameInterno(i)).setSelected(true);
			}
			catch(PropertyVetoException e)
			{
				System.err.println("No se puede situar " + getFicheroTopo(i));
			}
	}
	
	/** Llama al metodo ponMensaje de la ventana indicada en pos*/
	public void ponMensaje(int pos, String cadena)
	{
		getFrameInterno(pos).ponMensaje(cadena);
	}
	
	/** Llama al metodo seleccionaTodos de la ventana indicada en pos*/
	public void seleccionaTodos(int pos, boolean valor)
	{
		(getFrameInterno(pos)).seleccionaTodos(valor);
	}

	/** Establece la variable cambios segun el valor recibido */
	public void setCambios(int pos, boolean valor)
	{
		(getFrameInterno(pos)).setCambios(valor);
	}
	
	/** Llama al metodo setFicheroTopo de la ventana indicada en pos*/
	public void setFicheroTopo(int pos, String nomfich)
	{
		(getFrameInterno(pos)).setFicheroTopo(nomfich);
	}
	
	/** Llama al metodo simulaPaso de la ventana activa*/
	public void simulaPaso()
	{
		if (getActivo()!=-1)
			getFrameInterno(getActivo()).simulaPaso();
	}

	/** Llama al metodo simulaTodo de la ventana activa*/
	public void simulaTodo()
	{
		if (getActivo()!=-1)
			getFrameInterno(getActivo()).simulaTodo();
	}

	/** Devuelve el numero de ventanas que hay en la lista */
	public int tam()
	{
		return size();
	}

	/** Devuelve el tamanyo de la lista de objetos copiados */
	public int tamObjetosCopiados()
	{
		if (objetosCopiados != null)
			return objetosCopiados.size();
		
		return 0;
	}

	/** Metodo para poner delante del resto de ventanas la ventana indicada en pos */
	public void traeAlFrente(int pos)
	{
		((JInternalFrame)getFrameInterno(pos)).moveToFront();
	}

	public void simulaEvento() {
		if (getActivo()!=-1)
			getFrameInterno(getActivo()).simulaEvento();		
	}
}
