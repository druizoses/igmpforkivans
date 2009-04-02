/** @author: tlfs & afzs */
package visSim;

import java.awt.Dimension;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import visSim.modelosTablas.modeloRendererTablaErrores;
import visSim.modelosTablas.modeloTablaErrores;
import Redes.IPv4.ErroresIPv4;
import Redes.IPv4.ARP.ErroresARP;
import Redes.IPv4.ICMP.ErroresICMP;

/** Esta clase implementa el panel de errores a simular mostrado en la ventana de propiedades
 * de los equipos. */
public class panelErrores extends JPanel
{
	private Vector listaErrores;
	private JTable tabla;
	
	/** Al constructor de la clase le vamos a pasar las selecciones del usuario para la simulacion
	 * de cada uno de los errores.
	 * Los nombres y las descripciones de los errores simulables se obtienen en tiempo de ejecucion
	 * a partir de la clase Redes.IPv4
	 */
	public panelErrores(Vector erroresSelecciones)
	{
		// Preparamos las cosas de la ventana
		setLayout(null);

		tabla = new JTable();
		tabla.setName("tablaRuta");
		
		construyeVectorErrores(erroresSelecciones);
		tabla.setModel(new modeloTablaErrores(listaErrores));
		tabla.setDefaultRenderer(String.class, new modeloRendererTablaErrores());
		
		// Ponemos los anchos a las columnas
		tabla.getColumnModel().getColumn(0).setPreferredWidth(75);
		tabla.getColumnModel().getColumn(1).setPreferredWidth(275);
		tabla.getColumnModel().getColumn(2).setPreferredWidth(60);
		tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tabla.setPreferredScrollableViewportSize(new Dimension(300, 70));

		JScrollPane jspanel = new JScrollPane();
		jspanel.setViewportView(tabla);

		add(jspanel);
		jspanel.setBounds(10,10, 430, 120);
	}
	
	/** Metodo que construye el vector con los errores IPv4, ICMP y ARP y sus
	 * descripciones en funcion de los implementados en el simulador.
	 * Cuando finaliza la ejecucion de este metodo, el vector listaErrores contiene
	 * nombre del error, descripcion y un valor booleano indicando si se simula ese
	 * error. El vector se envia posteriormente al modelo de la tabla para construir
	 * la misma
	 */
	private void construyeVectorErrores(Vector erroresSelecciones)
	{
		listaErrores = new Vector();
		int i,j;
		
		// Errores IP
		for (i=0, j=0; i<ErroresIPv4.flags.length; i++, j++)
		{
			listaErrores.add(ErroresIPv4.flags[i]);
			listaErrores.add(ErroresIPv4.descripcion[i]);
			
			try
			{
				listaErrores.add(erroresSelecciones.elementAt(j));
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				listaErrores.add("false");
			}
		}

		for (i=0; i<ErroresARP.flags.length; i++, j++)
		{
			listaErrores.add(ErroresARP.flags[i]);
			listaErrores.add(ErroresARP.descripcion[i]);

			try
			{
				listaErrores.add(erroresSelecciones.elementAt(j));
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				listaErrores.add("false");
			}
		}

		for (i=0; i<ErroresICMP.flags.length; i++, j++)
		{
			listaErrores.add(ErroresICMP.flags[i]);
			listaErrores.add(ErroresICMP.descripcion[i]);

			try
			{
				listaErrores.add(erroresSelecciones.elementAt(j));
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				// Activamos la simulacion de 3_2
				// Con esto, si cambiase el orden dentro de la clase del Simulador habria
				// que localizarlo para cambiar la posicion.
				// En un disenyo correcto de esto se incluiria en el mismo vector
				// nombre, descripcion y true o false; no obstante aun habria dependencia
				// en cuanto al nombre que define la 3_2 o su descripcion.
				if (i==0)
					listaErrores.add("true");
				else
					listaErrores.add("false");
			}
		}
	}

	/** Devuelve un vector de valores booleanos indicando la simulacion de cada 
	 * uno de los errores */
	public Vector getSeleccionesErrores()
	{
		Vector dev = new Vector();
		
		for (int i=0; i<tabla.getRowCount(); i++)
			dev.add(tabla.getValueAt(i, 2).toString());
		
		return dev;
	}
}
