/** @author: tlfs & afzs */
package util;

/** En esta clase se definen las constantes con las rutas de las imagenes */
public class nomiconos
{
	/** Separador de rutas dependiende del sistema operativo */
	public static final String cadenaSeparador = System.getProperty("file.separator");
	
	/** Carpeta donde se encuentran almacenadas las imagenes */
	public static String carpeta = System.getProperty("user.dir") + cadenaSeparador + "imagenes" + cadenaSeparador;

	/** Imagen para flecha hacia abajo */
	public static final String flechaAbajo = carpeta + "flechaAbajo.gif";

	/** Imagen para flecha hacia arriba */
	public static final String flechaArriba = carpeta + "flechaArriba.gif";

	/** Imagen para ventana acerca de */
	public static final String nomAcerca = carpeta + "I1.gif";

	/** Constante para el icono del anillo */
	public static final String nomAnillo = carpeta + "anillo.gif";

	/** Constante para el icono del raton del anillo */
	public static final String nomAnilloCursor = carpeta + "anillocursor.gif";

	/** Constante para el boton Abrir */
	public static final String nombotonAbrir = carpeta + "botonAbrir.gif";

	/** Constante para el boton Buscar */
	public static final String nombotonBuscar = carpeta + "botonBuscar.gif";

	/** Constante para el boton Comprueba Simulacion */
	public static final String nombotonCompruebaSimula = carpeta + "botonCompruebaS.gif";

	/** Constante para el boton Copiar */
	public static final String nombotonCopiar = carpeta + "botonCopiar.gif";

	/** Constante para el boton Cortar */
	public static final String nombotonCortar = carpeta + "botonCortar.gif";

	/** Constante para el boton Detiene Simulacion */
	public static final String nombotonDetieneSimu = carpeta + "botonDetieneSimu.gif";

	/** Constante para el boton Eventos Simulacion */
	public static final String nombotonEventosSimu = carpeta + "botonEventosS.gif";

	/** Constante para el boton Guardar */
	public static final String nombotonGuardar = carpeta + "botonGuardar.gif";

	/** Constante para el boton Imprimir */
	public static final String nombotonImprimir = carpeta + "botonImprimir.gif";
	
	/** Constante para el boton Nuevo */
	public static final String nombotonNuevo = carpeta + "botonNuevo.gif";

	/** Constante para el boton Pegar */
	public static final String nombotonPegar = carpeta + "botonPegar.gif";

	/** Constante para el boton Separador */
	public static final String nombotonSeparador = carpeta + "botonSeparador.gif";

	/** Constante para el boton Configurar envio */
	public static final String nombotonSimuEnvio = carpeta + "botonSimuEnvio.gif";

	/** Constante para el boton Simular paso a paso */
	public static final String nombotonSimulaPaso = carpeta + "botonSimulaP.gif";
	
	/** Constante para el boton Simular evento a evento */
	public static final String nombotonSimulaEvento = carpeta + "botonSimulaE.gif";	

	/** Constante para el boton Simular todo */
	public static final String nombotonSimulaTodo = carpeta + "botonSimulaT.gif";

	/** Constante para el icono cursor por defecto */
	public static final String nomCursor = carpeta + "cursor.gif";

	/** Constante para el icono de la ethernet */
	public static final String nomEthernet = carpeta + "ethernet.gif";

	/** Constante para el icono del raton de la ethernet */
	public static final String nomEthernetCursor = carpeta + "ethernetCursor.gif";

	/** Constante para el icono de la etiqueta */
	public static final String nomEtiqueta = carpeta + "etiqueta.gif";

	/** Constante para el icono del hub */
	public static final String nomHub = carpeta + "hub.gif";
	
	/** Constante para el icono linea de conexion */
	public static final String nomLinea = carpeta + "linea.gif";

	/** Constante para el icono del raton de la linea de conexion */
	public static final String nomLineaCursor = carpeta + "lineaCursor.gif";

	/** Constante para el icono del modem */
	public static final String nomModem = carpeta + "modem.gif";

	/** Constante para el icono del raton del modem */
	public static final String nomModemCursor = carpeta + "modemCursor.gif";

	/** Constante para el icono del PC */
	public static final String nomPC = carpeta + "pc.gif";

	/** Constante para el icono del puente */
	public static final String nomPuente = carpeta + "puente.gif";

	/** Constante para el icono del Router */
	public static final String nomRouter = carpeta + "router.gif";

	/** Constante para el icono del Router MultiCast*/
	public static final String nomRouterMC = carpeta + "mcRouter.gif";
	
	/** Constante para el icono del switch */
	public static final String nomSwitch = carpeta + "switch.gif";

	/** Constante para el un icono vacio*/
	public static final String nomVacio = carpeta + "vacio";

	/** Constante para el icono de la Wan */
	public static final String nomWan = carpeta + "wan.gif";

	/** Constante para el cursor por defecto */
	public static final int tam = 40;
	
	/** Devuelve el nombre del equipo que corresponde a un icono dado
	 * @param nomicono Nombre del icono
	 * @return String conteniendo el nombre que corresponde al equipo
	 */
	public String getNombre(String nomicono)
	{
		// Quitamos la ruta y la extension
		String dev = nomicono.substring(nomicono.lastIndexOf(cadenaSeparador)+1, nomicono.lastIndexOf("."));
		
		// y ponemos el primer caracter en mayusculas
		return (dev.substring(0, 1)).toUpperCase() + dev.substring(1, dev.length());
	}

	/** Esta clase devuelve el icono del raton que corresponde a un icono del panel
	 * @param nombre Nombre del icono
	 * @return String con el nombre del cursor correspondiente
	 */
	public String getNomIconoCursor(String nombre)
	{
		String dev = nombre;

		if (nombre.compareTo(nomLinea)==0)
			dev = nomLineaCursor;
		else if (nombre.compareTo(nomEthernet)==0)
			dev = nomEthernetCursor;
		else if (nombre.compareTo(nomAnillo)==0)
			dev = nomAnilloCursor;

		return dev;
	}
}
