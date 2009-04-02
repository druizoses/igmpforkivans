/** @author: tlfs & afzs */
package visSim;

import java.awt.Dimension;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableColumn;

import visSim.modelosTablas.modeloTablaInterfaces;

/** Esta clase implementa el panel de interfaces mostrado en la ventana de propiedades
 * de los equipos. */
class panelInterfaz extends JPanel
{
	private JButton btn1, btn3, btn4;
	private JTable tabla;
	
	/** Constructor de la clase. Recibe los elementos necesarios para poder mostrarlos
	 * al usuario
	 * @param oyente MouseListener para las acciones del raton
	 * @param id Cadena que identifica al equipo
	 * @param interfaces Lista de interfaces del equipo
	 */
	public panelInterfaz(MouseListener oyente, String id, listaInterfaces interfaces)
	{
		setLayout(null);
		
		btn1 = new JButton("Anyadir");
		btn1.setName("AnyadirInterfaz");
		btn1.addMouseListener(oyente);

		btn3 = new JButton("Modificar");
		btn3.setName("ModificarInterfaz");
		btn3.addMouseListener(oyente);

		btn4 = new JButton("Borrar");
		btn4.setName("BorrarInterfaz");
		btn4.addMouseListener(oyente);

		tabla = new JTable();
		tabla.setName("tablaInterfaz");
		tabla.addMouseListener(oyente);
		tabla.setPreferredScrollableViewportSize(new Dimension(300, 70));
		setTabla(interfaces);

		JScrollPane jspanel = new JScrollPane();
		jspanel.setViewportBorder(tabla.getBorder());
		jspanel.setViewportView(tabla);

      add(btn1);
		btn1.setBounds(89, 150, 81, 26);
		add(btn3);
		btn3.setBounds(185, 150, 81, 26);
		add(btn4);
		btn4.setBounds(281, 150, 81, 26);

		add(jspanel);
      jspanel.setBounds(10,10, 430, 120);

		// Las ethernet y las tokenring no pueden modificar rutas ni interfaces		
		if (!(id.compareTo("pc")==0 || id.compareTo("ro")==0 || id.compareTo("sw")==0))
		{
			btn1.setEnabled(false);
			btn3.setEnabled(false);
			btn4.setEnabled(false);
			tabla.setEnabled(false);
		}
	}
	
	/** Devuelve un booleano indicando si el boton de nombre recibido esta activo */
	public boolean estaActivo(String nombre)
	{
		if (nombre.compareTo("tablaInterfaz")==0)
			return tabla.isEnabled();
		else if (nombre.compareTo("AnyadirInterfaz")==0)
			return btn1.isEnabled();
		else if (nombre.compareTo("ModificarInterfaz")==0)
			return btn3.isEnabled();
		else if (nombre.compareTo("BorrarInterfaz")==0)
			return btn4.isEnabled();
		
		return false;
	}
	
	/** Devuelve el numero de la fila seleccionada en la tabla */
	public int getFilaTabla()
	{
		return tabla.getSelectedRow();
	}

	/** Establece los valores de la tabla segun la lista de interfaces */
	public void setTabla(listaInterfaces interfaces)
	{
		tabla.setModel(new modeloTablaInterfaces(interfaces));

		TableColumn column = tabla.getColumnModel().getColumn(0);
		column.setPreferredWidth(40);
	}
}

