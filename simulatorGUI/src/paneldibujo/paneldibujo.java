/** @author: tlfs & afzs */
package paneldibujo;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JMenuItem;

import objetoVisual.anilloVisual;
import objetoVisual.ethernetVisual;
import objetoVisual.etiquetaVisual;
import objetoVisual.hubVisual;
import objetoVisual.lgfich;
import objetoVisual.listaObjetos;
import objetoVisual.modemVisual;
import objetoVisual.objetoVisual;
import objetoVisual.pcVisual;
import objetoVisual.propiedadesTopologia;
import objetoVisual.puenteVisual;
import objetoVisual.routerMultiCastVisual;
import objetoVisual.routerVisual;
import objetoVisual.switchVisual;
import objetoVisual.wanVisual;
import util.nomiconos;
import util.simuGrafico;
import util.utilLinea;
import visSim.dialogoEnvios;
import visSim.listaInterfaces;
import visSim.listaRutas;
import visSim.propiedades;

/** Clase que extiende la clase paneldibujoVisual para implementar un JPanel
 * sobre el cual se va a dibujar la topologia */
public class paneldibujo extends paneldibujoVisual
{
	/** Icono seleccionado en el panel de botones */
	private String iconoActual;

	/** Utilizamos una utilLinea para dibujar el rectangulo y la circunferencia de la token ring*/
	private utilLinea inicialLinea, rectangulo, circuloTR;
	
	/** Valor que indica si un equipo se esta moviendo actualmente */
	private boolean moviendo;

	/** Vector que contiene el nombre de aquellos objetos que van a ser copiados */
	private Vector nombresCopias;
	
	private Frame padreFrame;

	/** Controla si se esta redimensionando un bus o una token ring (o lo que se implemente posteriormente) */
	private boolean redimensionando;
	
	/** Estas coordenadas nos van a servir posteriormente para la operacion de pegado
	 * Cambian cuando hay un evento Move del raton
	 */
	private int xRaton, yRaton;
	
	/**
	 * 
	 */
	
	private objetoVisual inicio = null;
	
	/** Constructor de la clase
	 * @param oyente para el menu oculto utilizado para enviar mensajes a visual
	 */
	public paneldibujo(ActionListener oyente, Frame padreFrame)
	{
		super(padreFrame);
		iconoActual = "";
		this.padreFrame = padreFrame;
		redimensionando = false;
		moviendo = false;

		mnuitem = new JMenuItem();
		mnuitem.addActionListener(oyente);
		
		// Anyadimos eventos del raton
		addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				if (evt.getButton() == MouseEvent.BUTTON3)
					ponMensaje(constantesMensajes.cMRaton + xRaton + "," + yRaton + "*" + ficheroTopo);
				else
					ratonClicked(evt.getX(), evt.getY());
			}

			public void mousePressed(MouseEvent evt) {}
			
			public void mouseReleased(MouseEvent evt)
			{
				xRaton = evt.getX();
				yRaton = evt.getY();
				ratonReleased();
			}
		});
		
		addMouseMotionListener(new MouseMotionListener()
		{

 			public void mouseDragged(MouseEvent evt)
 			{
 				xRaton = evt.getX();
 				yRaton = evt.getY();
 				
				ratonDragged(evt.getX(), evt.getY());
 			}
			public void mouseMoved(MouseEvent evt)
			{
				// Almacenamos coordenadas del raton para pegado
				xRaton = evt.getX();
				yRaton = evt.getY();
 				
				ratonMoved(evt.getX(),evt.getY());
			}
		});
		setBackground(new java.awt.Color(0,0,0));
	}
	
	/** Configuracion de envios de las maquinas de la topologia
	 * @param xCentral Coordenada x central de la pantalla
	 * @param yCentral Coordenada y central de la pantalla
	 */
	public void configuraEnvios(int xCentral, int yCentral)
	{
		dialogoEnvios dialogo = new dialogoEnvios(padreFrame, xCentral, yCentral);
		dialogo.setTabla(lista.getNombresOrdenadores(), new Vector(lista.getlistaEnvios()));
		dialogo.muestra();
		
		if (dialogo.getBoton().compareTo("Aceptar")==0)
		{
			// Comprobamos si han cambiado con respecto a las que habian
			Vector envios = new Vector(dialogo.getDatosTabla());
			
			// Primero en cuanto a tamanyo
			if (envios.size() != lista.getlistaEnvios().size())
				cambios = true;
			
			// Luego en cuanto a contenido
			if (!envios.equals(lista.getlistaEnvios()))
				cambios = true;

			if (cambios)
				ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);

			lista.setlistaEnvios(new Vector(dialogo.getDatosTabla()));
			ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		}
		
		dialogo.destruye();
	}
	
	/** Crea una lista con los objetos seleccionados para luego poder pegarlos */
	public void copiaSeleccionados()
	{
		nombresCopias = new Vector(lista.copiaSeleccionados());
		
		if (nombresCopias.size()==0)
		{
			nombresCopias = null;
			ponMensaje("***No hay objetos que copiar");
		}
		else
		{
			copias = new listaObjetos();

			// Llevamos a la lista copias los objetos que se encuentran en nombresCopias
			copias.pega(lista, nombresCopias);
			
			// Borramos las conexiones de los que no estan en nombres
			copias.eliminaNoExistentes(nombresCopias);
			
			ponMensaje(constantesMensajes.cCopi + ficheroTopo);
			ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		}
	}
	
	/** Metodo cortar. Primero copiamos y luego eliminamos */
	public void cortar()
	{
		copiaSeleccionados();
		eliminaSeleccionados();
		cambios = true;
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
	}

	/** Metodo que crea una maquina visual y anyade todos los eventos necesarios
	 * @param x Coordenada x del equipo
	 * @param y Coordenada y del equipo
	 * @param tamx Anchura
	 * @param tamy Alturax
	 * @param nombre Nombre que recibe dentro de la topologia
	 * @param icono Icono que lo representa para saber de que tipo se trata
	 * @param seleccionado Indica si el equipo esta seleccionado
	 * @param conexiones Vector de conexiones visuales
	 * @param interfaces Vector de interfaces del equipo
	 * @param rutas Vector de rutas del equipo
	 */
	private void creaMaquina(int x, int y, int tamx, int tamy, String nombre, String icono, boolean seleccionado, Vector conexiones, listaInterfaces interfaces, listaRutas rutas)
	{
		final objetoVisual temp;

		/* Codigo para crear el objeto segun el icono recibido */
		/*   Como se podria hacer con reflection o alguna otra cosa?? */		
		if (icono.compareTo(nomiconos.nomPC)==0)				temp = new pcVisual();
		else if (icono.compareTo(nomiconos.nomEthernet)==0)		temp = new ethernetVisual();
		else if (icono.compareTo(nomiconos.nomSwitch)==0)		temp = new switchVisual();
		else if (icono.compareTo(nomiconos.nomHub)==0)			temp = new hubVisual();
		else if (icono.compareTo(nomiconos.nomPuente)==0)		temp = new puenteVisual();
		else if (icono.compareTo(nomiconos.nomAnillo)==0)		temp = new anilloVisual();
		else if (icono.compareTo(nomiconos.nomModem)==0)		temp = new modemVisual();
		else if (icono.compareTo(nomiconos.nomEtiqueta)==0)		temp = new etiquetaVisual();
		else if (icono.compareTo(nomiconos.nomWan)==0)			temp = new wanVisual();
		else if (icono.compareTo(nomiconos.nomRouter)==0)		temp = new routerVisual();
		else													temp = new routerMultiCastVisual();
		
		temp.set(nombre, x, y, tamx, tamy);
		temp.setInterfaces(interfaces);
		temp.setRutas(rutas);
		temp.setSeleccionado(seleccionado);
		
		if (conexiones!=null)
			temp.setConexiones(new Vector(conexiones));
			
		// Anyadimos los eventos de las maquinas
		
		temp.addMouseMotionListener(new MouseMotionListener()
		{
			// Al arrastrar
			public void mouseDragged(MouseEvent evt)
			{
				// Aqui habra que redibujar las lineas del objeto movido
				if (iconoActual.compareTo(nomiconos.nomLineaCursor)!=0)  //movemos un objeto
				{
					int cx = evt.getX();
					int cy = evt.getY();
				
					// Cuando el raton pase por encima de un bus y este este seleccionado y ademas al pasar sea por los
					// cuadrados entonces se podra redimensionar.
					if (temp instanceof ethernetVisual && temp.getSeleccionado())
					{
						// Bus horizontal
						if (temp.getWidth()!=5)
						{
							if (((cx >=-5 && cx<= 3 && cy>=0 && cy<=temp.getHeight()) ||
							     (cx>=temp.getWidth()-3 && cx<=temp.getWidth()+3 && cy>=0 && cy<=temp.getHeight())) &&
							     !moviendo)
								redimensionando = true;
							else
								moviendo = true;
								
							// Si se esta redimensionando hay q ver si desde la izqda o desde la dcha
							if (redimensionando)
							{
								// Tomamos la mitad por poner un valor y que no de problemas
								if (cx<temp.getWidth()/2)
								{
									temp.setX(temp.getX()+cx);
									lista.mueveEquiposSeleccionados(0, 0);
									temp.setWidth(temp.getWidth()-cx);
								}
								else
								{
									temp.setWidth(cx);
									lista.mueveEquiposSeleccionados(0, 0);
								}
							}
							else
								mueveObjetos(cx, cy);
						}
						// Bus vertical
						else
						{
							if (((cy >=-5 && cy<= 3) || (cy>=temp.getHeight()-3 && cy<=temp.getHeight()+3)) && !moviendo)
								redimensionando = true;
							else
								moviendo = true;
							
							if (redimensionando)
							{
								if (cy< temp.getHeight()/2)
								{
									temp.setY(temp.getY()+cy);
									lista.mueveEquiposSeleccionados(0, 0);
									temp.setHeight(temp.getHeight()-cy);
								}
								else
								{
									temp.setHeight(cy);
									lista.mueveEquiposSeleccionados(0, 0);
								}
							}
							else
								mueveObjetos(cx, cy);
						}
					}
					// Se cambia el tamanyo de una Token Ring
					else if (temp instanceof anilloVisual && temp.getSeleccionado())
					{
							// Superior izquierda
						if (((cx>=0 && cx<=4 && cy>=0 && cy<=4) ||
							// superior derecha
						    (cx>=temp.getWidth()-4 && cx<=temp.getWidth() && cy>=0 && cy<=4) ||
						    (cx>=temp.getWidth()-4 && cx<=temp.getWidth() && cy>=temp.getHeight()-4 && cy<=temp.getHeight()) ||
						    (cx>=0 && cx<=4 && cy>=temp.getHeight()-4 && cy<=temp.getHeight())) && !moviendo)
							redimensionando = true;
						else
							moviendo = true;
						
						if (redimensionando)
						{
							if (cx<temp.getWidth()/2)
							{
								temp.setX(temp.getX()+cx);
								temp.setWidth(temp.getWidth()-cx);
								temp.setHeight(temp.getWidth()-cx);
								lista.mueveEquiposSeleccionados(0, 0);
							}
							else
							{
								temp.setX(temp.getX());
								temp.setWidth(cx);
								temp.setHeight(cx);
								lista.mueveEquiposSeleccionados(0, 0);
							}
						}
						else
							mueveObjetos(cx, cy);
					}
					else
					{
						if (!temp.getSeleccionado())
						{
							lista.seleccionaTodos(false);
							temp.setSeleccionado(true);
						}
						
						mueveObjetos(cx, cy);
					}
				}
				cambios = true;
				actualizaPantalla();
				ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
				ponMensaje(constantesMensajes.cMenu+cambiaMenu());
			}
				
			public void mouseMoved(MouseEvent evt)
			{
				// Ponemos borde de conexion al pasar por encima del objeto y siempre que se
				// puedan conectar
				if (iconoActual.compareTo(nomiconos.nomLineaCursor)==0 && inicialLinea!=null)
					if (temp.puedeConectarse(lista.getID(lista.getSeleccionadoConexion())))
						temp.setConecta(true);

				ratonMoved(temp.getX()+evt.getX(), temp.getY()+evt.getY());
			}
		});
		
		temp.addMouseListener(new MouseAdapter()
		{
				
			// Se hace click sobre un equipo
			public void mouseClicked(MouseEvent evt)
			{
				// Se pincha con el boton izquierdo
				if (evt.getButton()==MouseEvent.BUTTON1)
				{
					////////////////////////////////////////////////////////////
					// si el icono es el cursor entonces seleccionamos el objeto
					////////////////////////////////////////////////////////////
					if(iconoActual.compareTo(nomiconos.nomCursor)==0)
					{
						// Al hacer doble click...
						if (evt.getClickCount()==2)
						{
							if (temp instanceof etiquetaVisual)
							{
								String cadena = new etiquetaVisual().getCadena(temp.getNombre());

								// Si cambia la etiqueta entonces el fichero se muestra como modificado
								if (temp.getNombre().compareTo(cadena)!=0)
								{
									cambios = true;
									ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
									ponMensaje(constantesMensajes.cMenu+cambiaMenu());
									temp.setNombre(cadena);
								}
							}
							else
								muestraPropiedadesEquipo(xCentral, yCentral);
						}
						else
						{
							lista.seleccionaTodos(false);
						
							// Hacemos que en la lista se encuentre seleccionado el equipo
							int aux=lista.buscaEquipo(temp.getNombre());
							if(aux!=-1)
								lista.setSeleccionado(aux, true);

							temp.setSeleccionado(true);

							ponMensaje(constantesMensajes.cMenu+cambiaMenu());
							ponMensaje("***1 equipo seleccionado");
							actualizaPantalla();
						}
					}
					/////////////////////////
					// dibujamos una conexion
					/////////////////////////
					else if(iconoActual.compareTo(nomiconos.nomLineaCursor)==0)
					{
						// Si a pesar de no haber puesto el marco rojo el usuario insiste le ponemos un mensaje
						if (inicialLinea!=null)
							if (!lista.compatiblesConexion())
								ponMensaje("!!!Estos equipos no pueden conectarse.");

						// Comienza nueva conexion sobre ese equipo
						if (inicialLinea==null && temp.getTamConectables()>0)
						{
							lista.seleccionaTodos(false);
							temp.setConecta(true);
							inicio = temp;
							inicialLinea = new utilLinea(temp.getX()+temp.getWidth()/2, temp.getY()+temp.getHeight()/2, temp.getX()+temp.getWidth()/2, temp.getY()+temp.getHeight()/2);
						}
						else if (lista.compatiblesConexion())
						{
							// Creamos las conexiones de los equipos con conecta = true;
							lista.creaConexion();
							
							cambios = true;
							ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);

							lista.quitaPinchadoConexion();
							lista.quitaPinchadoConexion();
							ponMensaje(constantesMensajes.cMenu+cambiaMenu());
							
							if(temp instanceof switchVisual){
								for(int i=0;i<((switchVisual)temp).conexiones.size();i++){
									if(!temp.getInterfaces().existeInterfaz("eth"+i)){
										temp.getInterfaces().inserta("eth"+i, "0.0.0.0", "0.0.0.0", "00:00:00:00:00:00", inicio.nombre);
										break;
									}else
										if(i == temp.getInterfaces().size() - 1){
											temp.getInterfaces().inserta("eth"+temp.getInterfaces().size(), "0.0.0.0", "0.0.0.0", "00:00:00:00:00:00", inicio.nombre);
											i = temp.getInterfaces().size();
											break;
										}
								}
							}
							if(inicio instanceof switchVisual){
								for(int i=0;i<((switchVisual)inicio).conexiones.size();i++){
									if(!inicio.getInterfaces().existeInterfaz("eth"+i)){
										inicio.getInterfaces().inserta("eth0", "0.0.0.0", "0.0.0.0", "00:00:00:00:00:00", temp.nombre);
										break;
									}else
										if(i == inicio.getInterfaces().size() - 1){
											inicio.getInterfaces().inserta("eth"+inicio.getInterfaces().size(), "0.0.0.0", "0.0.0.0", "00:00:00:00:00:00", temp.nombre);
											i = inicio.getInterfaces().size();
											break;
										}
								}
							}

							// Actualizamos panel
							actualizaPantalla();
							inicialLinea=null;
						}
					}
				}
				// Se pincha con el boton derecho
				else if (evt.getButton()==MouseEvent.BUTTON3)
					if(iconoActual.compareTo(nomiconos.nomCursor)==0)
					{
						lista.seleccionaTodos(false);
						temp.setSeleccionado(true);

						lista.setSeleccionado(lista.buscaEquipo(temp.getNombre()), true);

						ponMensaje(constantesMensajes.cMenu+cambiaMenu());
						ponMensaje("***1 equipo seleccionado");
						ponMensaje(constantesMensajes.cMRaton + (temp.getX()+evt.getX()) + "," + (temp.getY()+evt.getY()) + "*" + ficheroTopo);
					}
			}
			public void mouseExited(MouseEvent evt)
			{
				// Quitamos el borde si el equipo actual no es el origen de la linea de conexion
				if (iconoActual.compareTo(nomiconos.nomLineaCursor)==0 && inicialLinea!=null)
					if (inicialLinea.getX(1) != temp.getX()+temp.getWidth()/2 && inicialLinea.getY(1) != temp.getY()+temp.getHeight()/2)
						temp.setConecta(false);
			}

			/* Aqui controlamos que los objetos no salgan del limite de la pantalla */
			public void mouseReleased(MouseEvent evt)
			{
				int xMin = lista.getX(lista.getPosXMin());
				int yMin = lista.getY(lista.getPosYMin());
				
				/* Valores que deben incrementar las coordenadas de los equipos con respecto al que mas se ha salido */
				int incX, incY;
				
				redimensionando = false;
				moviendo = false;

				// Si hay alguno fuera de los limites arrastramos todos los seleccionados
				if (xMin < 0 || yMin<0)
				{
					incX = 0;
					if (xMin<0)
						incX = -xMin;
					
					incY=0;
					if (yMin<0)
						incY = -yMin;
					
					for (int i=0; i<lista.tam(); i++)
						if (lista.getSeleccionado(i))
						{
							lista.setX(i, lista.getX(i) + incX);
							lista.setY(i, lista.getY(i) + incY);
						}
				}

				lista.GuardaDistan(0,0);
				actualizaPantalla();
			}
		});

		lista.insertaObjeto(temp);
		
		ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		
		add(temp, null);
		repaint();
	}
	
	/** Se crean todas las maquinas de la topologia a partir de una listaObjetos recibida */
	private void creaMaquinas(listaObjetos listatemp)
	{
		objetoVisual eqtemp;
		
		int tamano = listatemp.tam();

		// Tras leer el fichero tenemos que crear los objetos para lista		
		for (int i=0; i<tamano; i++)
		{
			eqtemp = (listatemp.getObjeto(i));
			if (eqtemp.getNomIcono().compareTo(nomiconos.nomVacio)!=0)
				creaMaquina(eqtemp.getX(), eqtemp.getY(), eqtemp.getWidth(), eqtemp.getHeight(), eqtemp.getNombre(), eqtemp.getNomIcono(), eqtemp.getSeleccionado(), eqtemp.getConexiones(), eqtemp.getInterfaces(), eqtemp.getRutas());
		}
		
		actualizaPantalla();
	}
	
	/** Elimina los objetos seleccionados */
	public void eliminaSeleccionados()
	{
		if (lista.getNumSeleccionados()==0)
			ponMensaje("***No hay equipos que eliminar");
		else
		{
			lista.eliminaSeleccionados();
			listaObjetos listatemp = lista.copiaLista();
			propiedadesTopologia nuevaPropi = new propiedadesTopologia(lista.getPropiedades());
			Vector envios = new Vector(lista.getlistaEnvios());
			
			lista.clear();

			lista = new listaObjetos();
		
			removeAll();
			creaMaquinas(listatemp);
			
			lista.setPropiedades(nuevaPropi);
			lista.setlistaEnvios(envios);

			cambios = true;
			actualizaPantalla();
			ponMensaje(constantesMensajes.cMenu+cambiaMenu());
			ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
			listatemp.clear();
		}
	}

	/** Gestion de las barras de desplazamiento del panel */
	private void gestionBarras()
	{
		int x, y, W, H;
		Dimension area = new Dimension(0,0);
		int tamano = lista.tam();
		
		for (int i=0; i<tamano; i++)
		{
			x = lista.getX(i);
			y = lista.getY(i);
			W = lista.getWidth(i);
			H = lista.getHeight(i);
			
			if (x+W > area.width)
				area.width = x+W;
		
			if (y+H > area.height)
				area.height = y+H;

			// Comprobaciones para el texto (nombre) del equipo
			if (lista.getTextoVisual(i)!=null)
			{
				x = lista.getTextoVisual(i).getX();
				y = lista.getTextoVisual(i).getY();
				W = lista.getTextoVisual(i).getAncho();
				H = lista.getTextoVisual(i).getAlto();

				if (x+W > area.width)
					area.width = x+W;
			
				if (y+H > area.height)
					area.height = y+H;
			}

			this.setPreferredSize(area);
			this.revalidate();
		}
	}
	
	/** Metodo para leer el fichero de la topologia */
	public boolean leeFichero(String nomfich)
	{
		lista = new listaObjetos();
		listaObjetos listatemp = new listaObjetos();
		
		boolean dev = lgfich.leeFichero(nomfich, listatemp);
		
		creaMaquinas(listatemp);
		lista.setPropiedades(new propiedadesTopologia(listatemp.getPropiedades()));
		lista.setlistaEnvios(new Vector(listatemp.getlistaEnvios()));
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		
		return dev;
	}

	/** Se llama a propiedades con los datos del primer equipo que se encuentre seleccionado */
	public void muestraPropiedadesEquipo(int xCentral, int yCentral)
	{
		int pos = lista.getPrimerSeleccionado();
		
		// Pasar el mensaje a visual.java
		if (pos==-1)
			ponMensaje("***No hay equipos seleccionados");
		// no mostramos propiedades de etiqueta
		else if (lista.getNomIcono(pos).compareTo(nomiconos.nomEtiqueta)!=0)
		{
			Vector eventos = new Vector();

			// Si se ha ejecutado la simulacion filtramos los datos para pasarlos a las propiedades
			if (salidaEnvios != null)
				for (int i=0; i<salidaEnvios.size(); i++)
					if ( ((String)salidaEnvios.elementAt(i)).indexOf(lista.getNombre(pos))!=-1)
						eventos.add(salidaEnvios.elementAt(i));
			
			propiedades propi = new propiedades(padreFrame, xCentral, yCentral, pos, lista, eventos);
			propi.muestra();
			
			// El usuario ha aceptado los cambios
			if (propi.getBoton().compareTo("Aceptar")==0)
			{
				// Cuando el usuario cambia el nombre de la maquina hay que cambiarlo tambien en las
				// conexiones y en las interfaces
				if (lista.getNombre(pos).compareTo(propi.getnomEquipo())!=0)
				{
					lista.cambiaNombresConexiones(lista.getNombre(pos), propi.getnomEquipo());
					lista.setNombre(pos, propi.getnomEquipo());
					cambios = true;
				}
				
				////////////////////////////////
				// Comprobamos cambios en interfaces
				listaInterfaces nuevasInterfaces = propi.getInterfaces();
				listaInterfaces viejasInterfaces = lista.getInterfaces(pos);
				
				// Primero segun tamanyo 
				if (nuevasInterfaces.size() != viejasInterfaces.size())
					cambios = true;
				else
				// Luego segun contenido
				for (int i=0; i<nuevasInterfaces.size(); i++)
					if (!nuevasInterfaces.getInterfaz(i).igual(viejasInterfaces.getInterfaz(i)))
						cambios = true;
				
				lista.setlistaEnvios(new Vector(propi.getEnvios()));
					
				nuevasInterfaces.clear();
				lista.setInterfaces(pos, propi.getInterfaces());

				////////////////////////////////
				// Comprobamos cambios en rutas
				listaRutas nuevasRutas = propi.getRutas();
				listaRutas viejasRutas = lista.getRutas(pos);

				// Primero segun tamanyo 
				if (nuevasRutas.size() != viejasRutas.size())
					cambios = true;
				else
				// Luego segun contenido
				for (int i=0; i<nuevasRutas.size(); i++)
					if (!nuevasRutas.getRuta(i).igual(viejasRutas.getRuta(i)))
						cambios = true;

				lista.setRutas(pos, nuevasRutas);
				nuevasRutas.clear();
				viejasRutas.clear();

				////////////////////////////////
				// Comprobamos cambios en conexiones
				Vector nuevasConexiones = new Vector(propi.getConexiones());
				Vector viejasConexiones = new Vector(lista.getConexiones(pos));
				
				// Primero segun tamanyo 
				if (nuevasConexiones.size() != viejasConexiones.size())
					cambios = true;
				else
				// Luego segun contenido
				for (int i=0; i<nuevasConexiones.size(); i++)
					if (((String)nuevasConexiones.elementAt(i)).compareTo((String)viejasConexiones.elementAt(i))!=0)
						cambios = true;
				
				lista.setConexiones(pos, new Vector(propi.getConexiones()));

				lista.setSeleccionado(pos, true);

				lista.setErrores(pos, propi.getListaErrores());

				ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
				ponMensaje(constantesMensajes.cMenu+cambiaMenu());
				ponMensaje(constantesMensajes.cabPanDib+ lista.getNumSeleccionados() + " equipos seleccionados");
			}
			//else if (propi.getBoton().compareTo("Cancelar")==0)
				//propi.hide();
				
			
			propi.destruye();
		}
	}
	
	public void muestraPropiedadesTopologia()
	{
		dialogoPropiedadesTopologia dialogo = new dialogoPropiedadesTopologia(padreFrame, xCentral, yCentral, lista.getPropiedades());
		dialogo.muestra();
		
		propiedadesTopologia nuevas = dialogo.getPropiedades();
		dialogo.dispose();
		
		if (!nuevas.compara(lista.getPropiedades()))
		{
			if (lista.getNombres().size()!=0)
				cambios = true;
			lista.setPropiedades(nuevas);
			ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
			ponMensaje(constantesMensajes.cMenu+cambiaMenu());
		}
	}

	/** Funcion de apoyo al evento Dragged de los equipos para el movimiento de los mismos */
	public void mueveObjetos(int cx, int cy)
	{
		if(lista.EsCero())
			lista.GuardaDistan(cx, cy);
		
		// Movemos todos los equipos seleccionados
		lista.mueveEquiposSeleccionados(cx, cy);
	}
	
	/** Metodo para refrescar la pantalla */
	public void paintComponent(Graphics g)
	{
		simuGrafico.dibuja(g, getWidth(), getHeight(), lista, rectangulo, inicialLinea, circuloTR, quitaSelecciones);
		gestionBarras();
	}
	
	/** Pega en el panel los equipos previamente copiados */
	public void pegar()
	{
		// PONER MENSAJE EN AREA INFERIOR
		if (nombresCopias==null)
			ponMensaje("***No hay objetos que pegar");
		else
		{
			// Se cambian los nombres para que no haya conflictos con los actuales
			copias.cambiaNombres(lista);

			// Se cambian las coordenadas a la actual del raton
			copias.cambiaCoord(xRaton, yRaton);
			
			// En la lista actual hacemos que ninguno este seleccionado
			lista.seleccionaTodos(false);
			
			// y en la nueva que todos esten seleccionados
			copias.seleccionaTodos(true);

			// Se crean las nuevas maquinas
			creaMaquinas(copias);
			cambios = true;
			ponMensaje(constantesMensajes.cMenu+cambiaMenu());
			ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
		}
	}

	/** Metodo utilizado para copiar objetos entre distintas topologias */	
	public void ponCopias(listaObjetos nuevaLista)
	{
		nombresCopias = new Vector();
		copias = new listaObjetos();
		copias = nuevaLista.copiaLista();
		
		ponMensaje(constantesMensajes.cMenu+cambiaMenu());
	}
	
	/** Establece el cursor del raton que muestra paneldibujo */
	public void ponCursor(String nombre, int xCentral, int yCentral)
	{
		iconoActual = nombre;
		this.xCentral = xCentral;
		this.yCentral = yCentral;
		
		// Cuando se pinche en un boton se quitan todas las selecciones y se actualiza la pantalla
		// De esta forma no se quedan seleccionados los equipos mientras se crean nuevos
		lista.seleccionaTodos(false);
		lista.quitaPinchadoConexion();
		inicialLinea = null;
		actualizaPantalla();
		
		// Cursor por defecto
		Cursor nuevoCursor = new Cursor(Cursor.DEFAULT_CURSOR);

		if (iconoActual.compareTo(nomiconos.nomCursor)!=0)
		{
			Image img = Toolkit.getDefaultToolkit().getImage(nombre);
			nuevoCursor = Toolkit.getDefaultToolkit().createCustomCursor(img, new Point(0,0), nombre);
		}
		
		setCursor(nuevoCursor);
	}

	/**Evento CLICK del raton
	 * En esta funcion se controla, segun el valor de iconoActual, si se
	 * crea una nueva conexion, si se crea una ethernet, una token ring,
	 * si se ha pinchado sobre el panel o para crear un equipo nuevo en
	 * la topologia. 
	 * @param x Coordenada x donde se hace click
	 * @param y Coordenada y donde se hace click
	 */
	private void ratonClicked(int x, int y)
	{
		//si el icono es Linea creamos una conexion
		if(iconoActual.compareTo(nomiconos.nomLineaCursor)==0)
		{
			// Quitamos el que se habia pinchado para conectar
			lista.quitaPinchadoConexion();

			if (inicialLinea!=null)
			{
				cambios = true;
				inicialLinea=null;
			}

			ponMensaje(constantesMensajes.cMenu+cambiaMenu());
			ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
		}
		// Se va a dibujar un bus
		else if (iconoActual.compareTo(nomiconos.nomEthernetCursor)==0)
		{
			// Si la linea (bus) aun no existe se crea
			if (inicialLinea==null)
				inicialLinea = new utilLinea(x,y, x,y);
			// En caso contrario se mete en la lista de buses
			else
			{
				int tamx = 5;
				int tamy = 5;
				int xInicial = 1;
				int yInicial = 1;
				
				// Horizontal positiva
				if (inicialLinea.getX(1) < inicialLinea.getX(2))
				{
					xInicial = inicialLinea.getX(1);
					yInicial = inicialLinea.getY(1);
					tamx = inicialLinea.getX(2)-inicialLinea.getX(1);
				}
				// Horizontal negativa
				else if (inicialLinea.getX(1) > inicialLinea.getX(2))
				{
					xInicial = inicialLinea.getX(2);
					yInicial = inicialLinea.getY(2);
					tamx = inicialLinea.getX(1)-inicialLinea.getX(2);
				}
				
				// Vertical hacia abajo
				else if (inicialLinea.getY(1) < inicialLinea.getY(2))
				{
					xInicial = inicialLinea.getX(1);
					yInicial = inicialLinea.getY(1);
					tamy = inicialLinea.getY(2) - inicialLinea.getY(1);
				}
				else
				{
					xInicial = inicialLinea.getX(2);
					yInicial = inicialLinea.getY(2);
					tamy = inicialLinea.getY(1) - inicialLinea.getY(2);
				}
				
				creaMaquina(xInicial, yInicial, tamx, tamy, "Ethernet" + lista.devMaximo(nomiconos.nomEthernet), nomiconos.nomEthernet, false, null, null, null);
			
				// Actualizamos panel
				actualizaPantalla();
				
				cambios = true;
				inicialLinea=null;
				
				ponMensaje(constantesMensajes.cMenu+cambiaMenu());
				ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
			}
		}
		// Se dibuja una Token Ring
		else if (iconoActual.compareTo(nomiconos.nomAnilloCursor)==0)
		{
			if (circuloTR==null)
				circuloTR = new utilLinea(x,y, x,y);
			else
			{
				double radio = Math.sqrt( (circuloTR.getX(2)-circuloTR.getX(1))*(circuloTR.getX(2)-circuloTR.getX(1)) + (circuloTR.getY(2)-circuloTR.getY(1))*(circuloTR.getY(2)-circuloTR.getY(1)));
				
				creaMaquina(circuloTR.getX(1), circuloTR.getY(1), (int)radio, (int)radio, "Anillo" + lista.devMaximo(nomiconos.nomAnillo), nomiconos.nomAnillo, false, null, null, null);

				actualizaPantalla();
				
				circuloTR=null;
				cambios = true;
				ponMensaje(constantesMensajes.cMenu+cambiaMenu());
				ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
			}
		}
		// Se hace click con cursor seleccionado
		// No habra ningun equipo seleccionado
		else if (iconoActual.compareTo(nomiconos.nomCursor)==0)
		{
			ponMensaje(constantesMensajes.cMenu+cambiaMenu());
			lista.seleccionaTodos(false);
		}
		// Se trata de cualquier otro objeto visual
		else
		{
			cambios = true;
			
			String nombre = new nomiconos().getNombre(iconoActual) + lista.devMaximo(iconoActual);
			int tamx = nomiconos.tam;
			int tamy = nomiconos.tam;
			
			if (iconoActual.compareTo(nomiconos.nomEtiqueta)==0)
			{
				// Quitando el comentario hacemos q cada vez q se crea una etiqueta se solicite un nombre
				//nombre = (new etiquetaVisual()).getCadena(nombre, lista.devMaximo(nomiconos.nomEtiqueta));
				tamx = (new etiquetaVisual()).getWidth(nombre);
				tamy = (new etiquetaVisual()).getHeight();
			}
			
			creaMaquina(x, y, tamx, tamy, nombre, iconoActual, false, null, null, null);
			
			// Cuando el usuario quiera poner un modem inmediatamente aparece otro a la derecha
			if (iconoActual.compareTo(nomiconos.nomModem)==0)
			{
				nombre = new nomiconos().getNombre(iconoActual) + lista.devMaximo(iconoActual);

				creaMaquina(x+100, y, tamx, tamy, nombre, iconoActual, false, null, null, null);
				
				lista.setConecta(lista.tam()-1, true);
				lista.setConecta(lista.tam()-2, true);
				lista.creaConexion();
			}

			ponMensaje(constantesMensajes.cMenu+cambiaMenu());
			ponMensaje(constantesMensajes.cTopo + ficheroTopo + "," + cambios);
		}
		
		actualizaPantalla();
	}
	
	/**Evento Dragged del raton
	 * Se controla cuando el raton esta siendo arrastrado para seleccionar equipos
	 * @param x Coordenada x donde se hace click
	 * @param y Coordenada y donde se hace click
	 */
	public void ratonDragged(int x, int y)
	{
		// Si estamos con el cursor por defecto entonces seleccionamos
		if (iconoActual.compareTo(nomiconos.nomCursor)==0)
		{
			if (rectangulo == null)
				rectangulo = new utilLinea(x, y, x ,y);
			else
			{
				rectangulo.setX(x, 2);
				rectangulo.setY(y, 2);
			}

			// Actualizamos panel
			actualizaPantalla();
		}
	}
	
	/** Evento Moved del raton 
	 * Se controla el dibujado de un anillo, una ethernet o una conexion entre
	 * equipos. Para ello se da los valores correspondientes a circuloTR o inicialLinea
	 * @param x Coordenada x donde se hace click
	 * @param y Coordenada y donde se hace click
	 */
	public void ratonMoved(int x,int y)
	{
		if (iconoActual.compareTo(nomiconos.nomAnilloCursor)==0)
		{
			if (circuloTR!=null)
			{
				circuloTR.setX(x, 2);
				circuloTR.setY(y,2);
			}
		}
		else if(inicialLinea!=null)
		{
			// Controlamos si se dibuja un bus. La cosa cambia al dibujar
			if (iconoActual.compareTo(nomiconos.nomEthernetCursor)==0)
			{
				// Margen de coordenada y a partir del cual se dibuja la linea
				int margen = 30;
				double distx = Math.sqrt( (inicialLinea.getX(1)-x)*(inicialLinea.getX(1)-x) );
				
				// Se dibujo la linea horizontal o vertical segun corresponda
				inicialLinea.setX(x, 2);
				inicialLinea.setY(inicialLinea.getY(1), 2);
				
				if (x < inicialLinea.getX(1) + margen && distx<=margen)
				{
					inicialLinea.setX(inicialLinea.getX(1), 2);
					inicialLinea.setY(y, 2);
				}
			}
			// Se sibuja conexion entre equipos
			// Establecemos sus coordenadas. El primer par es el mismo de antes
			// El segundo par es el nuevo
			else
			{
				inicialLinea.setX(x, 2);
				inicialLinea.setY(y, 2);
			}
		}
			
		// Actualizamos panel
		actualizaPantalla();
	}
	
	/** Al presionar la primera vez se inserta el punto con las coordenadas donde se ha Pressed
	   Conforme se vaya moviendo el raton se modifica ese punto para que el final sea el de las
	   coordenadas actuales del raton.
	   Al soltar el boton hay que comprobar si se ha soltado sobre un objeto, entonces se anyadira
	   a la lista la linea nueva que sera la que forme la conexion entre 2 equipos distintos.
	*/
	public void ratonReleased()
	{
		// Si estamos con el cursor por defecto entonces creamos las selecciones
		if (iconoActual.compareTo(nomiconos.nomCursor)==0)
		{
			lista.seleccionaTodos(false);

			if (rectangulo != null)
				lista.creaSelecciones(rectangulo);
			
			ponMensaje(constantesMensajes.cMenu+cambiaMenu());
			ponMensaje(constantesMensajes.cabPanDib+ lista.getNumSeleccionados() + " equipos seleccionados");

			// Actualizamos panel
			actualizaPantalla();
			rectangulo = null;
		}
	}
}
