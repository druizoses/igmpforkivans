/** @author: tlfs & afzs */
package visSim;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import visSim.modelosTablas.modeloTablaIncidencias;

/** Esta clase muestra en un JDialog las incidencias de la topologia una vez
 * que se ha comprobado si es posible simular con la configuracion actual de
 * la topologia
 */
public class dialogoIncidencias extends JDialog
{
	/** Constructor de la clase
	 * @param parent Frame que contiene el JDialog
	 * @param xCentral Coordenada x central de la pantalla
	 * @param yCentral Coordenada y central de la pantalla
	 * @param incidencias Vector de String conteniendo las descripciones de las incidencias
	 */
	public dialogoIncidencias(Frame parent, int xCentral, int yCentral, Vector incidencias)
	{
		super(parent, true);
		getContentPane().setLayout(null);
		
		setTitle("Incidencias de la topologia");
		
		JButton btn1 = new JButton("Aceptar");
		btn1.setName("Aceptar");
		btn1.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				dispose();
			}
		});

		JTable  tabla = new JTable();
		tabla.setName("tablaInterfaz");
		tabla.setPreferredScrollableViewportSize(new Dimension(300, 70));
		tabla.setModel(new modeloTablaIncidencias(incidencias));

		JScrollPane jspanel = new JScrollPane();
		jspanel.setViewportBorder(tabla.getBorder());
		jspanel.setViewportView(tabla);

      getContentPane().add(btn1);
		btn1.setBounds(175, 135, 81, 26);

		getContentPane().add(jspanel);
      jspanel.setBounds(4,10, 430, 110);

		// Ajustamos los anchos de las columnas
		TableColumn column = null;
		column = tabla.getColumnModel().getColumn(0);
		column.setPreferredWidth(7);

		column = tabla.getColumnModel().getColumn(1);
		column.setPreferredWidth(293);

		setResizable(false);
		int ancho = 450;
		int alto = 200;
		setBounds(xCentral-ancho/2, yCentral-alto/2, ancho, alto);
		
		setVisible(true);
	}
}
