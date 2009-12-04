/** @author: tlfs & afzs */
package visSim;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import util.muestraAviso;
import util.nomiconos;
import visSim.modelosTablas.modeloTablaRutas;

/** Esta clase implementa el panel de rutas mostrado en la ventana de propiedades
 * de los equipos. */
public class panelRutas extends JPanel
{
	private JButton btn1, btn2, btn3;
	private listaRutas rutas;
	private JTable tabla;
	
	private Frame padreFrame;
	
	/** Constructor de la clase. Recibe los elementos necesarios para poder mostrarlos
	 * al usuario
	 * @param oyente MouseListener para las acciones del raton
	 * @param id Cadena que identifica al equipo
	 * @param rutas Lista de rutas del equipo
	 */
	public panelRutas(Frame padreFrame, MouseListener oyente, String id, listaRutas rutas)
	{
		// Preparamos las cosas de la ventana
		setLayout(null);
		this.padreFrame = padreFrame;
		
		this.rutas = rutas.copia();

		tabla = new JTable();
		tabla.setName("tablaRuta");
		tabla.addMouseListener(oyente);

		btn1 = new JButton("Añadir");
		btn1.setName("AñadirRuta");
		btn1.addMouseListener(oyente);

		btn2 = new JButton("Modificar");
		btn2.setName("ModificarRuta");
		btn2.addMouseListener(oyente);

		btn3 = new JButton("Borrar");
		btn3.setName("BorrarRuta");
		btn3.addMouseListener(oyente);
		
		JButton btn4 = new JButton("");
		btn4.setIcon(new ImageIcon(nomiconos.flechaArriba));
		btn4.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				botonPulsado("Subir");
			}
		});

		JButton btn5 = new JButton("");
		btn5.setIcon(new ImageIcon(nomiconos.flechaAbajo));
		btn5.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				botonPulsado("Bajar");
			}
		});

		tabla.setPreferredScrollableViewportSize(new Dimension(300, 70));
		tabla.setModel(new modeloTablaRutas(rutas));

		JScrollPane jspanel = new JScrollPane();
		jspanel.setViewportBorder(tabla.getBorder());
		jspanel.setViewportView(tabla);

		add(jspanel);
		jspanel.setBounds(40,10, 400, 120);

		add(btn1);
		btn1.setBounds(89, 150, 81, 26);
		add(btn2);
		btn2.setBounds(185, 150, 81, 26);
		add(btn3);
		btn3.setBounds(281, 150, 81, 26);
		add(btn4);
		btn4.setBounds(10, 10, 20, 20);
		add(btn5);
		btn5.setBounds(10, 30, 20, 20);

		// Las ethernet y las tokenring no pueden modificar rutas ni interfaces		
		if (!(id.compareTo("pc")==0 || id.compareTo("ro")==0 || id.compareTo("mc")==0))
		{
			btn1.setEnabled(false);
			btn2.setEnabled(false);
			btn3.setEnabled(false);
			tabla.setEnabled(false);
		}
	}
	
	/** Funcion que controla eventos sobre los botones de subir o bajar */
	private void botonPulsado(String nombre)
	{
		rutas = getRutas();
		int fila = tabla.getSelectedRow();
		
		if (fila==-1)
			muestraAviso.mensaje(padreFrame, "No hay ninguna fila seleccionada.");
		else
		if (nombre.compareTo("Subir")==0)
		{
			// Solo subimos si la fila es, al menos, la 1
			if (fila>0)
			{
				rutas.intercambia(fila, fila-1);
				setTabla(rutas);
				tabla.setRowSelectionInterval(fila-1, fila-1);
			}
		}
		else if (nombre.compareTo("Bajar")==0)
		{
			// Solo bajamos si la fila es, como mucho, la penultima
			if (fila < tabla.getRowCount()-1)
			{
				rutas.intercambia(fila, fila+1);
				setTabla(rutas);
				tabla.setRowSelectionInterval(fila+1, fila+1);
			}
		}
		else
			System.err.println("Boton no conocido");
	}

	/** Devuelve un booleano indicando si el boton de nombre recibido esta activo */
	public boolean estaActivo(String nombre)
	{
		if (nombre.compareTo("tablaRuta")==0)
			return tabla.isEnabled();
		else if (nombre.compareTo("AñadirRuta")==0)
			return btn1.isEnabled();
		else if (nombre.compareTo("ModificarRuta")==0)
			return btn2.isEnabled();
		else if (nombre.compareTo("BorrarRuta")==0)
			return btn3.isEnabled();
		
		return false;
	}
	
	/** Devuelve el numero de la fila seleccionada en la tabla */
	public int getFilaTabla()
	{
		return tabla.getSelectedRow();
	}
	
	/** Devuelve la configuracion de las rutas dentro del panel */
	public listaRutas getRutas()
	{
		return rutas.copia();
	}

	/** Establece los valores de la tabla segun la lista de rutas */
	public void setTabla(listaRutas rutas)
	{
		tabla.setModel(new modeloTablaRutas(rutas));
		this.rutas = rutas.copia();
	}
}
