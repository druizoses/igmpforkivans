/** @author: tlfs & afzs */
package visSim.modelosTablas;

import java.awt.Component;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import util.colores;

/** Clase que aplica colores a las tablas de leyenda de los colores de los eventos de la simulacion */
public class modeloColoresLeyenda extends JLabel implements TableCellRenderer
{
	/**
	 * <code>listaEventos</code> contiene la lista de Eventos de la simulacion
	 */
	private Vector datos;
	
	/** Constructor de la clase
	 * @param listaEventos Eventos de la simulacion
	 */
	public modeloColoresLeyenda(Vector datos)
	{
		this.datos = new Vector(datos);
	}
	
	/** Funcion que aplica los colores en funcion del contenido de cada evento.
	 * @see colores.java
	 */
	public Component getTableCellRendererComponent(JTable table, Object valor, boolean isSelected, boolean hasFocus, int row, int column)
	{
		String cadena = ((String)datos.elementAt(row*2+column));
		
		// Las cadenas en la columna 1 van siempre de color negro
		if (cadena.indexOf("Trama recibida")!=-1 ||
			cadena.indexOf("Envio de datos")!=-1 ||
			cadena.indexOf("Peticion ARP")!=-1 ||
			cadena.indexOf("Respuesta ARP")!=-1 ||
			cadena.indexOf("Trama con destino")!=-1 ||
			cadena.indexOf("Trama circulando")!=-1 ||
			cadena.indexOf("Datagrama IPv4 (sin fragmentar)")!=-1 ||
			cadena.indexOf("Datagrama IPv4 (fragmentado)")!=-1 ||
			cadena.indexOf("Trama ICMP")!=-1)
		{
			setOpaque(false);
			setForeground(colores.negro);
		}

		// En el lado derecho se pone segun el color de cada tipo de trama
		if (cadena.indexOf("*Trama recibida")!=-1)
		{
			setOpaque(true);
			setForeground(colores.colorTramaRecibida);
			setBackground(colores.colorTramaRecibida);
		}
		else if (cadena.indexOf("*Envio de datos")!=-1)
		{
			setOpaque(true);
			setForeground(colores.colorEnviodeDatos);
			setBackground(colores.colorEnviodeDatos);
		}
		else if (cadena.indexOf("*Peticion ARP")!=-1)
		{
			setOpaque(true);
			setForeground(colores.colorPeticionARP);
			setBackground(colores.colorPeticionARP);
		}
		else if (cadena.indexOf("*Respuesta ARP")!=-1)
		{
			setOpaque(true);
			setForeground(colores.colorRespuestaARP);
			setBackground(colores.colorRespuestaARP);
		}

		else if (cadena.indexOf("*Trama con destino")!=-1)
		{
			setOpaque(true);
			setForeground(colores.colorConDestino);
			setBackground(colores.colorConDestino);
		}
		else if (cadena.indexOf("*Trama circulando")!=-1)
		{
			setOpaque(true);
			setForeground(colores.colorCirculando);
			setBackground(colores.colorCirculando);
		}
		else if (cadena.indexOf("*Datagrama IPv4 (sin fragmentar)")!=-1)
		{
			setOpaque(true);
			setForeground(colores.colorDatagramaSinFragmentar);
			setBackground(colores.colorDatagramaSinFragmentar);
		}
		else if (cadena.indexOf("*Datagrama IPv4 (fragmentado)")!=-1)
		{
			setOpaque(true);
			setForeground(colores.colorDatagramaFragmentado);
			setBackground(colores.colorDatagramaFragmentado);
		}
		else if (cadena.indexOf("*Trama ICMP")!=-1)
		{
			setOpaque(true);
			setForeground(colores.colorICMP);
			setBackground(colores.colorICMP);
		}
		
		setText(valor.toString().substring(1));
		
		return this;
    }
}
