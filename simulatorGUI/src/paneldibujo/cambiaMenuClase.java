/** @author: tlfs & afzs */
package paneldibujo;

/** Clase que devuelve un String codificando en estado las distintas situaciones
 * de la topologia para activar o desactivar los menus correspondientes.
 */
public class cambiaMenuClase
{
	private String estado;
	
	public cambiaMenuClase(boolean simulado,boolean cambios, int numSeleccionados, int tamLista, int tamCopias, boolean posibleSimular, int tamCompruebaSimulacion, int tamListaEnvios, boolean simulacionNulo)
	{
		estado = "0-";

		if(cambios)   //cambios
			estado += "6-";
		
		switch (numSeleccionados)   //selecciones
		{
			case 0:
				estado += "2-";
				break;
			case 1:
				estado += "1-7-8-";
				break;
			default:
				if (numSeleccionados==tamLista)
					estado += "1-7-";
				else
					estado += "1-";
		}

		if(tamCopias!=0)  //copias
			estado += "3-";

		if(tamLista!=0 && tamLista!=numSeleccionados)  //algun objeto en topologia y no todos selecc
			estado += "10-";
		else
			if(tamLista!=0)  //algun objeto en topologia
				estado += "9-";

		if(posibleSimular)
			estado += "13-"; //haya pc conectados
		
		if (estado.indexOf("13")!=-1 && tamCompruebaSimulacion==0)  //no errores simulacion
			estado += "14-";
		
		if (estado.indexOf("14")!=-1 && tamListaEnvios>0) //haya tramas que mandar
			estado += "15-";
		
		if (!simulacionNulo)
			estado += "16-";
		
		if (simulado) //se va a activar Mostrar Eventos porque ya se ha simulado alguna vez.
			estado +="17-";
	}
	
	public String getEstado()
	{
		return estado;
	}
}
