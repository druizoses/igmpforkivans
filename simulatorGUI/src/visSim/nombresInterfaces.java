/** @author: tlfs & afzs */
package visSim;

/** Clase que contiene constantes para dar nombres a las interfaces */
public class nombresInterfaces
{
	public final static String nombreEthernet = "eth";
	public final static String nombrePAP = "pap";
	public final static String nombreAnillo = "tr";
	
	public static String dameRaiz(String tipo)
	{
		String propuesta = nombresInterfaces.nombrePAP;

		// Si no es modem entonces ethernet
		if (tipo.compareTo("mo")!=0)
		{
			propuesta = nombresInterfaces.nombreEthernet;

			if (tipo.compareTo("an")==0)
				propuesta = nombresInterfaces.nombreAnillo;
		}
		
		return propuesta;
	}
}
