/** @author: tlfs & afzs */
package objetoVisual;

import java.util.Vector;

import util.nomiconos;

/** Clase creada para devolver un Vector conteniendo los iconos de los equipos
 *  que se pueden utilizar en la aplicacion.
 */
public class equiposRegistrados
{
	public static Vector listaIconos()
	{
		Vector dev = new Vector();
		
		dev.add(nomiconos.nomCursor);
		dev.add(nomiconos.nomEtiqueta);
		dev.add(nomiconos.nomLinea);
		dev.add(nomiconos.nomPC);
		dev.add(nomiconos.nomRouter);
		dev.add(nomiconos.nomRouterMC);
		dev.add(nomiconos.nomSwitch);
		dev.add(nomiconos.nomHub);
		dev.add(nomiconos.nomModem);
		dev.add(nomiconos.nomPuente);
		dev.add(nomiconos.nomEthernet);
		dev.add(nomiconos.nomAnillo);
		dev.add(nomiconos.nomWan);
		
		return dev;
	}
}
