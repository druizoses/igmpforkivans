/** @author: tlfs & afzs */
package visSim;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import util.copiaPortapapeles;
import visSim.modelosTablas.modeloColoresEventos;
import visSim.modelosTablas.modeloTablaEventosFiltrados;

/** Esta clase implementa el panel de eventos ocurridos durante la simulacion de un equipo concreto.
 * Se muestra en la ventana de propiedades de los equipos. */
class panelEventos extends JPanel
{
	private JTable tabla;
	
	/** El constructor de la clase recibe todos los eventos, previamente filtrados, de la simulacion */
	public panelEventos(Vector eventos)
	{
		setLayout(null);
		final Vector copiaEventos = new Vector(eventos);

		/* Con este boton anyadimos la posibilidad de copiar al portapapeles los eventos de la simulacion de
		   esta maquina. */
		JButton btnPortapapeles = new JButton("Copiar");
		btnPortapapeles.setToolTipText("Copia el contenido de la tabla al portapapeles");
		btnPortapapeles.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				copiaPortapapeles.copia(copiaEventos);
			}
		});

		tabla = new JTable();
		tabla.setName("tablaInterfaz");
		tabla.setPreferredScrollableViewportSize(new Dimension(300, 140));
		tabla.setDefaultRenderer(String.class, new modeloColoresEventos(eventos));
		setTabla(eventos);

		JScrollPane jspanel = new JScrollPane();
		jspanel.setViewportBorder(tabla.getBorder());
		jspanel.setViewportView(tabla);

		add(btnPortapapeles);
		btnPortapapeles.setBounds(175, 150, 100, 26);

		add(jspanel);
      jspanel.setBounds(10,10, 430, 120);
	}
	
	/** Establece las propiedades de la tabla que mostrara los eventos */
	private void setTabla(Vector eventos)
	{
		tabla.setModel(new modeloTablaEventosFiltrados(eventos));
		
		TableColumn column = tabla.getColumnModel().getColumn(0);
		column.setPreferredWidth(40);

		column = tabla.getColumnModel().getColumn(1);
		column.setPreferredWidth(30);

		column = tabla.getColumnModel().getColumn(2);
		column.setPreferredWidth(230);
	}
}
