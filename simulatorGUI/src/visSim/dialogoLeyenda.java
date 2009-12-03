/** @author: tlfs & afzs */
package visSim;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import visSim.modelosTablas.modeloColoresLeyenda;
import visSim.modelosTablas.modeloTablaLeyenda;

/** Clase creada para mostrar por pantalla los eventos de la simulacion */
public class dialogoLeyenda extends JDialog
{
	/** Constructor de la clase
	 * @param parent Frame sobre el que se muestra el cuadro
	 * @param xCentral Coordenada x central
	 * @param yCentral Coordenada y central
	 */
	public dialogoLeyenda(Frame parent, int xCentral, int yCentral)
	{
		super(parent, true);
		
		setTitle("Leyenda de colores");
		
		// Preparamos las cosas de la ventana
		getContentPane().setLayout(null);

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				dispose();
			}
		});
		
		ponDatos();

		setResizable(false);
		int ancho = 250;
		int alto = 220;
		setBounds(xCentral-ancho/2, yCentral-alto/2, ancho, alto);
	}
	
	private void ponDatos()
	{
		Vector datos = new Vector();
		datos.add("-Trama recibida");
		datos.add("*Trama recibida");
		datos.add("-Envio de datos");
		datos.add("*Envio de datos");
		datos.add("-Peticion ARP");
		datos.add("*Peticion ARP");
		datos.add("-Respuesta ARP");
		datos.add("*Respuesta ARP");
		datos.add("-Trama con destino");
		datos.add("*Trama con destino");
		datos.add("-Trama circulando");
		datos.add("*Trama circulando");
		datos.add("-Datagrama IPv4 (sin fragmentar)");
		datos.add("*Datagrama IPv4 (sin fragmentar)");
		datos.add("-Datagrama IPv4 (fragmentado)");
		datos.add("*Datagrama IPv4 (fragmentado)");
		datos.add("-Trama ICMP");
		datos.add("*Trama ICMP");
		
		JTable tabla = new JTable(new modeloTablaLeyenda(datos));
		tabla.setDefaultRenderer(String.class, new modeloColoresLeyenda(datos));
		tabla.setPreferredScrollableViewportSize(new Dimension(220, 70));
		
		// Ajustamos los anchos de las columnas
		tabla.getColumnModel().getColumn(0).setPreferredWidth(175);
		tabla.getColumnModel().getColumn(1).setPreferredWidth(60);
		
		JScrollPane jspanel = new JScrollPane();
		jspanel.setViewportBorder(tabla.getBorder());
		jspanel.setViewportView(tabla);
		
		getContentPane().add(jspanel);
		jspanel.setBounds(5,5, 230, 175);
	}
}
