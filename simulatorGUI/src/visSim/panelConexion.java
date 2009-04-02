/** @author: tlfs & afzs */
package visSim;

import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import visSim.modelosTablas.modeloTablaConexiones;

/** Esta clase implementa el panel de conexiones mostrado en la ventana de propiedades
 * de los equipos. */
class panelConexion extends JPanel
{
	private JButton btnBorrar;
	private JTable tabla;
	
	/** Constructor de la clase. Recibe los elementos necesarios para poder mostrarlos
	 * al usuario
	 * @param oyente MouseListener para las acciones del raton
	 * @param id Cadena que identifica al equipo
	 * @param conexiones Vector de nombres de equipos a los que se encuentra conectado
	 */
	public panelConexion(MouseListener oyente, Vector conexiones, Vector interfaces)
	{
		setLayout(null);
		
		btnBorrar = new JButton("Borrar");
		btnBorrar.setName("BorrarConexion");
		btnBorrar.addMouseListener(oyente);

		tabla = new JTable();
		tabla.setName("tablaConexion");
		tabla.setPreferredScrollableViewportSize(new Dimension(300, 70));
		// Ademas de las conexiones le pasamos las interfaces para que las muestre en la tabla
		setTabla(conexiones, interfaces);

		JScrollPane jspanel = new JScrollPane();
		jspanel.setViewportBorder(tabla.getBorder());
		jspanel.setViewportView(tabla);

		add(btnBorrar);
		btnBorrar.setBounds(185, 150, 81, 26);

		add(jspanel);
      jspanel.setBounds(10,10, 430, 120);
	}
	
	/** Devuelve un booleano indicando si el boton de nombre recibido esta activo */
	public boolean estaActivo(String nombre)
	{
		if (nombre.compareTo("BorrarConexion")==0)
			return btnBorrar.isEnabled();
		
		return false;
	}
	
	/** Devuelve el nombre del equipo de la fila seleccionada en la tabla */
	public String getDato()
	{
		if (tabla.getSelectedRow()!=-1)
			return (String)tabla.getValueAt(tabla.getSelectedRow(), 0);
		
		return null;
	}

	/** Establece los valores de la tabla segun el vector de conexiones */
	public void setTabla(Vector conexiones, Vector interfaces)
	{
		tabla.setModel(new modeloTablaConexiones(conexiones, interfaces));
	}
}
