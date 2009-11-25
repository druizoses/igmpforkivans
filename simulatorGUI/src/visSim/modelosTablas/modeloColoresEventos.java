/** @author: tlfs & afzs */
package visSim.modelosTablas;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import util.colores;

/** Clase que aplica colores a las tablas de los eventos de la simulacion */
public class modeloColoresEventos extends JLabel implements TableCellRenderer
{
	/**
	 * <code>listaEventos</code> contiene la lista de Eventos de la simulacion
	 */
	private Vector listaEventos;
	
	/** Constructor de la clase
	 * @param listaEventos Eventos de la simulacion
	 */
	public modeloColoresEventos(Vector listaEventos)
	{
		this.listaEventos = new Vector(listaEventos);
		setOpaque(false);
	}
	
	/** Funcion que aplica los colores en funcion del contenido de cada evento.
	 * @see colores.java
	 */
	public Component getTableCellRendererComponent(JTable table, Object valor, boolean isSelected, boolean hasFocus, int row, int column)
	{
		String cadena = ((String)listaEventos.elementAt(row));
		
		if (cadena.indexOf("Trama recibida")!=-1)
			setForeground(colores.colorTramaRecibida);
		else if (cadena.indexOf("Envio de datos")!=-1)
			setForeground(colores.colorEnviodeDatos);
		else if (cadena.indexOf("Peticion ARP")!=-1)
			setForeground(colores.colorPeticionARP);
		else if (cadena.indexOf("Respuesta ARP")!=-1)
			setForeground(colores.colorRespuestaARP);
		else if (cadena.indexOf("con destino")!=-1)
			setForeground(colores.colorConDestino);
		else if (cadena.indexOf("circulando")!=-1)
			setForeground(colores.colorCirculando);
		else if (cadena.indexOf("Datagrama IPv4 (sin fragmentar)")!=-1)
			setForeground(colores.colorDatagramaSinFragmentar);
		else if (cadena.indexOf("Datagrama IPv4")!=-1)
			setForeground(colores.colorDatagramaFragmentado);
		else if (cadena.indexOf("ICMP")!=-1)
			setForeground(colores.colorICMP);
		else
			setForeground(colores.negro);

		if (column == 0)
			setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		else if (column == 1)
			setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		else if (column == 2)
			setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		else
			setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		
		setText(valor.toString());
		
		return this;
    }
}
