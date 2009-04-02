/** @author: tlfs & afzs */
package visSim;

import java.util.Vector;

import Equipos.Equipo;
import Redes.IPv4.ErroresIPv4;
import Redes.IPv4.ARP.ErroresARP;
import Redes.IPv4.ICMP.ErroresICMP;

/** Clase que construye una lista de errores simulables por los equipos
 * Los errores que se pueden simular se refieren a errores IP, ICMP y ARP y
 * se encuentran implementados en la clase Redes.IPv4
 **/
public class nombresSeleccionesErrores
{
	
	/** En la lista devuelta se incluye una lista de los errores que el
	 * usuario puede configurar. Por defecto no se simula ningun error,
	 * en las propiedades del equipo se pueden modificar los valores.
	 * @param erroresConfigurados Lista de errores configurados
	 * @return Vector
	 */
	public static Vector getErrores(Vector erroresConfigurados)
	{
		Vector dev = new Vector();
		int i,j;
		
		// Errores IP
		for (i=0, j=0; i<ErroresIPv4.flags.length; i++, j++)
		{
			dev.add(ErroresIPv4.flags[i]);
			dev.add(new Integer(Equipo.kIPv4));
			
			try
			{
				dev.add(erroresConfigurados.elementAt(j));
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				dev.add("false");
			}
		}

		// Errores ARP
		for (i=0; i<ErroresARP.flags.length; i++, j++)
		{
			dev.add(ErroresARP.flags[i]);
			dev.add(new Integer(Equipo.kARP));

			try
			{
				dev.add(erroresConfigurados.elementAt(j));
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				dev.add("false");
			}
		}

		// Errores ICMP
		for (i=0; i<ErroresICMP.flags.length; i++, j++)
		{
			dev.add(ErroresICMP.flags[i]);
			dev.add(new Integer(Equipo.kICMP));
			
			try
			{
				dev.add(erroresConfigurados.elementAt(j));
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				dev.add("false");
			}
		}
		
		return dev;
	}
}
