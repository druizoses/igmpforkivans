/** @author: tlfs & afzs */
package visSim;

/** Representacion visual de una interfaz
 * Contiene todos los datos necesarios para poder realizar posteriormente
 * la simulacion de envio de datos */
public class interfazVisual
{
	/** Nombre de la maquina a la que conecta la interfaz */
	private String conecta;
	
	/** Direccion de enlace de la interfaz */
	private String dirEnlace;
	
	/** IP de la interfaz */
	private String ip;
	
	/** Mascara de la interfaz */
	private String mascara;

	/** Nombre de la interfaz */
	private String nombre;
	
	/** Constructor de la clase
	 * @param nombre Nombre de la interfaz
	 * @param ip IP de la interfaz
	 * @param mascara Mascara de red
	 * @param dirEnlace Direccion de enlace
	 * @param conecta Nombre del equipo al que se encuentra conectado
	 */
	public interfazVisual(String nombre, String ip, String mascara, String dirEnlace, String conecta)
	{
		this.nombre = nombre;
		this.ip = ip;
		this.mascara = mascara;
		this.dirEnlace = dirEnlace;
		this.conecta = conecta;
	}
	
	public boolean dentroRango()
	{
		String ipRecortada = getIP().substring(0,3);
		int numero = new Integer(ipRecortada).intValue();
		
		return (numero==127);
	}

	/** Devuelve el nombre del equipo al que se encuentra conectado esta interfaz */
	public String getconecta()
	{
		return conecta;
	}

	/** Devuelve la direccion de enlace de la interfaz */
	public String getDirEnlace()
	{
		return dirEnlace;
	}

	/** Devuelve la IP de la interfaz */
	public String getIP()
	{
		return ip;
	}

	/** Devuelve la mascara de red de la interfaz */
	public String getMascara()
	{
		return mascara;
	}
	
	/** Devuelve el nombre de la interfaz */
	public String getNombre()
	{
		return nombre;
	}
	
	/** Devuelve un booleano resultado de comparar dos interfaces */
	public boolean igual(interfazVisual otra)
	{
		boolean dev = false;
		
		// Nombres iguales
		if (getNombre().compareTo(otra.getNombre()) == 0 &&
			getconecta().compareTo(otra.getconecta()) == 0 &&
			getDirEnlace().compareTo(otra.getDirEnlace()) == 0 &&
			getIP().compareTo(otra.getIP()) == 0 &&
			getMascara().compareTo(otra.getMascara()) == 0)
			dev = true;
		
		return dev;
	}

	/** Establece el equipo al que va a estar conectado esta interfaz */
	public void setconecta(String conecta)
	{
		this.conecta = conecta;
	}

	/** Establece la direccion de enlace de la interfaz */
	public void setDirEnlace(String dirEnlace)
	{
		this.dirEnlace = dirEnlace;
	}

	/** Establece la IP de la interfaz */
	public void setIP(String ip)
	{
		this.ip = ip;
	}

	/** Establece la mascara de red de la interfaz */
	public void setMascara(String mascara)
	{
		this.mascara = mascara;
	}
	
	/** Establece el nombre de la interfaz */
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
}
