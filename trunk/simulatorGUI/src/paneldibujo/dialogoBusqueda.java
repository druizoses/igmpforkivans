/** @author: tlfs & afzs */
package paneldibujo;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import util.muestraAviso;
import util.ordenaVector;

/** Clase que implementa la busqueda de equipos en la topologia, con la posibilidad
 * de utilizar expresiones regulares para ello.
 */
public class dialogoBusqueda extends JDialog
{
	/** Campo utilizado para la introduccion del nombre o expresion a buscar */
	private JTextField campo;

	/** Vector con los nombres de los equipos de la topologia */
	private Vector listaNombres;

	/** Tabla donde se mostraran los resultados de la busqueda */
	private JTable tabla;

	/** Clase utilizada para conocer el boton que ha sido pulsado una vez cerrado el cuadro */
	private String textoBoton;
	
	private Frame padreFrame;
	
	/** Constructor de la clase
	 * @param parent Frame que contendra el JDialog
	 * @param xCentral Coordenada x central de la pantalla
	 * @param yCentral Coordenada y central de la pantalla
	 * @param listaNombres Lista de nombres de equipos de la topologia
	 */
	public dialogoBusqueda(Frame parent, int xCentral, int yCentral, Vector listaNombres)
	{
		super(parent, true);
		padreFrame = parent;
		
		setTitle("Busqueda");
		
		this.listaNombres = new Vector(listaNombres);
		
		// Preparamos las cosas de la ventana
		getContentPane().setLayout(null);

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				textoBoton = "Cancelar";
				setVisible(false);
			}
		});
		
		tabla = new JTable();
		tabla.setName("tabla");
		tabla.setModel(new modeloTablaBusqueda(new Vector()));
		tabla.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				ratonClicked(evt);
			}
		});

		JButton btn1 = new JButton("Buscar");
		btn1.setName("Buscar");
		btn1.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				ratonClicked(evt);
			}
		});

		JButton btn2 = new JButton("Aceptar");
		btn2.setName("Aceptar");
		btn2.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				ratonClicked(evt);
			}
		});

		campo = new JTextField();
		
		tabla.setPreferredScrollableViewportSize(new Dimension(300, 70));

		JScrollPane jspanel = new JScrollPane();
		jspanel.setViewportBorder(tabla.getBorder());
		jspanel.setViewportView(tabla);

		getContentPane().add(jspanel);
		jspanel.setBounds(20,80, 160, 75);

		JLabel eti = new JLabel("Introduzca el criterio de busqueda");
		getContentPane().add(eti);
		eti.setBounds(20, 10, 180, 16);
		getContentPane().add(campo);
		campo.setBounds(20, 30, 63, 20);
		
		getContentPane().add(btn1);
		btn1.setBounds(100, 30, 81, 20);
		getContentPane().add(btn2);
		btn2.setBounds(60, 180, 81, 26);

		setResizable(false);
		int ancho = 200;
		int alto = 250;
		setBounds(xCentral-ancho/2, yCentral-alto/2, ancho, alto);
		
		setVisible(true);
	}

	/** Funcion que libera la memoria utilizada por el cuadro */
	public void destruye()
	{
		dispose();
	}
	
	/** Funcion que devuelve el texto del boton que ha sido pulsado antes de cerrar la ventana */
	public String getBoton()
	{
		return textoBoton;
	}
	
	/** Devuelve el nombre del equipo seleccionado en la tabla */
	public String getNombreSeleccionado()
	{
		return (String)tabla.getValueAt(tabla.getSelectedRow(), 0);
	}
	
	/** Funcion que hace visible el cuadro en la pantalla */
	public void muestra()
	{
		this.setVisible(true);
	}

	/** Controla el evento click del raton sobre los distintos componentes del cuadro */
	private void ratonClicked(MouseEvent evt)
	{
		String nombre = evt.getComponent().getName();
		
		if (nombre.compareTo("Buscar")==0)
		{
			Vector lista = new Vector();
			Matcher matcher;
			
			try
			{
				for (int i=0; i<listaNombres.size(); i++)
				{
					matcher = Pattern.compile(campo.getText(), Pattern.CASE_INSENSITIVE|Pattern.DOTALL).matcher((String)listaNombres.elementAt(i));

					while (matcher.find())
						if (matcher.group().length()>0)
							if (!lista.contains(listaNombres.elementAt(i)))
								lista.add(listaNombres.elementAt(i));
				}
				
				// Ordenamos el vector
				lista = new Vector(ordenaVector.getOrdenado(lista));

				tabla.setModel(new modeloTablaBusqueda(lista));
			}
			catch (PatternSyntaxException e)
			{
				muestraAviso.mensaje(padreFrame, "Ha ocurrido un error ajeno a la aplicacion debido a la expresion regular utilizada.");
			}
		}
		else if (nombre.compareTo("tabla")==0 && evt.getClickCount()==2)
		{
			textoBoton = "Aceptar";
			setVisible(false);
		}
		else if (nombre.compareTo("Aceptar")==0)
		{
			textoBoton = "Aceptar";
			setVisible(false);
		}
	}
}

/** Clase creada como modelo para la tabla de busqueda */
class modeloTablaBusqueda extends AbstractTableModel
{
	final String[] columnNames;
	final Object[][] data;

	public modeloTablaBusqueda(Vector nombres)
	{
		columnNames = new String[1];
		columnNames[0] = "Nombres coincidentes";
		
		data = new Object[nombres.size()][1];
		
		for (int i=0; i<nombres.size(); i++)
			data[i][0] = (String)nombres.elementAt(i);
	}
	
	public int getColumnCount()
	{
		return columnNames.length;
	}
	
	public String getColumnName(int col)
	{
		return columnNames[col];
	}
	
	public int getRowCount()
	{
		return data.length;
	}
	
	public Object getValueAt(int row, int col)
	{
		if (row>=0 && col>=0)
			return data[row][col];
		
		return null;
	}
	
	/** Las celdas de la tabla no se van a poder modificar */
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
}
