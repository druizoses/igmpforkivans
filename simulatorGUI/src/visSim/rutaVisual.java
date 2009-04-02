/** @author: tlfs & afzs
*/
package visSim;

/** Clase que implementa una rutaVisual.
 * Contiene todos los parametros necesarios para poder realizar posteriormente la simulacion.
 */
public class rutaVisual
{
	/** IP destino de la ruta */
	private String destino;
	
	/** Puerta de enlace de la ruta */
	private String gateway;
	
	/** Mascara de red de la ruta */
	private String mascara;
	
	/** Nombre de la interfaz a la que se encuentra vinculada la ruta */
	//TODO: Esto se eliminara cuando se arreglen las rutas en el simulador
	private String nombreInterfaz;
	
	/** Constructor de la clase
	 * @param destino IP destino de la ruta
	 * @param mascara Mascara de red
	 * @param gateway Puerta de enlace
	 * @param nombreInterfaz Nombre de la interfaz vinculada
	 */
	public rutaVisual(String destino, String mascara, String gateway, String nombreInterfaz)
	{
		this.destino = destino;
		this.mascara = mascara;
		this.gateway = gateway;
		this.nombreInterfaz = nombreInterfaz;
	}

	/** Devuelve el destino de la ruta */
	public String getDestino()
	{
		return destino;
	}

	/** Devuelve la puerta de enlace de la ruta */
	public String getGateway()
	{
		return gateway;
	}

	/** Devuelve la mascara de la red de la ruta */
	public String getMascara()
	{
		return mascara;
	}
	
	/** Devuelve el nombre de la interfaz de la ruta */
	public String getNombreInterfaz()
	{
		return nombreInterfaz;
	}

	/** Devuelve un booleano resultado de comparar dos rutas */
	public boolean igual(rutaVisual otra)
	{
		boolean dev = false;
		
		// Nombres iguales
		if (getDestino().compareTo(otra.getDestino()) == 0 &&
			getGateway().compareTo(otra.getGateway()) == 0 &&
			getMascara().compareTo(otra.getMascara()) == 0 &&
			getNombreInterfaz().compareTo(otra.getNombreInterfaz()) == 0)
			dev = true;
		
		return dev;
	}

	/** Establece el destino de la ruta */
	public void setDestino(String destino)
	{
		this.destino = destino;
	}

	/** Establece la puerta de enlace de la ruta */
	public void setGateway(String gateway)
	{
		this.gateway = gateway;
	}

	/** Establece la mascara de la ruta */
	public void setMascara(String mascara)
	{
		this.mascara = mascara;
	}
	
	/** Establece el nombre de la interfaz de la ruta */
	public void setNombreInterfaz(String nombreInterfaz)
	{
		this.nombreInterfaz = nombreInterfaz;
	}
}
