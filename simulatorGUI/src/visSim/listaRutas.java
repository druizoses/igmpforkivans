/** @author: tlfs & afzs */
package visSim;

import java.util.Vector;

/** Esta clase gestiona un conjunto de rutas de cada equipo
 * Extiende un Vector para gestion de la lista*/
public class listaRutas extends Vector
{
	/** Constructor de la clase */
	public listaRutas() {}

	/** Metodo para borrado de una ruta de la lista
	 * La ruta eliminada sera aquella que coincida con todos los parametros recibidos */
	public void borra(String destino, String mascara, String gateway, String nombreInterfaz)
	{
		boolean encontrado = false;
		rutaVisual ruta;
		
		for (int i=0; i<size() && !encontrado; i++)
		{
			ruta = (rutaVisual)elementAt(i);
			if (ruta.getDestino().compareTo(destino)==0 && ruta.getMascara().compareTo(mascara)==0 &&
			    ruta.getGateway().compareTo(gateway)==0 && ruta.getNombreInterfaz().compareTo(nombreInterfaz)==0)
				{
					removeElementAt(i);
					encontrado = true;
				}
		}
	}

	/** Metodo para borrado de una ruta de la lista dado el destino de la misma */
	public void borraRutaConIP(String destino)
	{
		for (int i=0; i<size(); i++)
			if (getDestino(i).compareTo(destino)==0)
				removeElementAt(i);
	}
	
	/** Devuelve un objeto tipo listaRutas resultado de copiar la lista actual */
	public listaRutas copia()
	{
		listaRutas dev = new listaRutas();
		
		for (int i=0; i<size(); i++)
			dev.add(getRuta(i));
		
		return dev;
	}
	
	/** Devuelve el numero de rutas que tiene la interfaz recibida */
	public int cuentaRutas(String nombreInterfaz)
	{
		int dev = 0;
		
		for (int i=0; i<size(); i++)
			if (getNombreInterfaz(i).compareTo(nombreInterfaz)==0)
				dev++;
		
		return dev;
	}

	/** Devuelve el destino de la ruta situada en pos */
	public String getDestino(int pos)
	{
		return getRuta(pos).getDestino();
	}

	/** Devuelve la puerta de enlace de la ruta situada en pos */
	public String getGateway(int pos)
	{
		return getRuta(pos).getGateway();
	}

	/** Devuelve la mascara de la ruta situada en pos */
	public String getMascara(int pos)
	{
		return getRuta(pos).getMascara();
	}

	/** Devuelve el nombre de la interfaz de la ruta situada en pos */
	public String getNombreInterfaz(int pos)
	{
		return getRuta(pos).getNombreInterfaz();
	}
	
	/** Devuelve la ruta situada en pos */
	public rutaVisual getRuta(int pos)
	{
		return (rutaVisual)elementAt(pos);
	}
	
	/** Metodo para insercion de una nueva ruta en la lista */
	public void inserta(String destino, String mascara, String gateway, String nombreInterfaz)
	{
		add(new rutaVisual(destino, mascara, gateway, nombreInterfaz));
	}
	
	/** Metodo para modificar una ruta de la lista */
	public void modifica(String destino, String mascara, String gateway, String nombreInterfaz, String destinoN, String mascaraN, String gatewayN, String nombreInterfazN)
	{
		boolean encontrado = false;
		rutaVisual ruta;
		
		for (int i=0; i<size() && !encontrado; i++)
		{
			ruta = (rutaVisual)elementAt(i);

			if (ruta.getDestino().compareTo(destino)==0 && ruta.getMascara().compareTo(mascara)==0 &&
			    ruta.getGateway().compareTo(gateway)==0 && ruta.getNombreInterfaz().compareTo(nombreInterfaz)==0)
				{
					removeElementAt(i);
					insertElementAt(new rutaVisual(destinoN, mascaraN, gatewayN, nombreInterfazN), i);
					encontrado = true;
				}
		}
	}
	
	/** Establece el destino de la ruta situada en pos */
	public void setDestino(int pos, String destino)
	{
		getRuta(pos).setDestino(destino);
	}

	/** Establece la puerta de enlace de la ruta situada en pos */
	public void setGateway(int pos, String gateway)
	{
		getRuta(pos).setGateway(gateway);
	}

	/** Establece la mascara de la ruta situada en pos */
	public void setMascara(int pos, String mascara)
	{
		getRuta(pos).setMascara(mascara);
	}
	
	/** Establece el nombre de la interfaz de la ruta situada en pos */
	public void setNombreInterfaz(int pos, String nombreInterfaz)
	{
		getRuta(pos).setNombreInterfaz(nombreInterfaz);
	}
	
	/** Devuelve el numero de rutas de la lista */
	public int tam()
	{
		return size();
	}
	
	/** Funcion que intercambia dos rutas de posicion */
	public void intercambia(int origen, int destino)
	{
		rutaVisual temp;

		// Ruta origen sube
		if (origen > destino)
		{
			temp = getRuta(destino);
			removeElementAt(destino);
			
			insertElementAt(temp, origen);
		}
		// Ruta que baja
		else if (origen < destino)
		{
			temp = getRuta(origen);
			removeElementAt(origen);
			
			insertElementAt(temp, destino);
		}
	}
	
	public String toString()
	{
		String dev = "Rutas\n";

		for (int i=0; i<size(); i++)
			dev += "  ["+(i+1)+"] " + getDestino(i) + ", " + getMascara(i) + ", " + getGateway(i) + ", " + getNombreInterfaz(i) + "\n";
		
		return dev;
	}
}
