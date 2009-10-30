/** @author: tlfs & afzs */
package visSim;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import objetoVisual.accionVisual.accionApagarVisual;
import objetoVisual.accionVisual.accionVisual;

import util.muestraAviso;
import visSim.modelosTablas.modeloTablaAcciones;


/** Clase creada para la configuracion de envios de tramas en la topologia.
 * En dicha configuracion se situan las maquinas origen y destino, el tamanyo
 * de la trama de datos y el numero de copias que se envian. 
 */
public class dialogoAcciones extends JDialog
{
	/** Vector que contiene la configuracion del envio de tramas */
	private Vector listaAcciones;

	/** Nombres de las maquinas junto con sus IPs de la topologia */	
	private Vector nombresOrdenadores;

	/** Tabla donde se mostraran los envios */
	private JTable tabla;
	
	private JComboBox accionesDisponibles;
	
	/** Nombre del boton pulsado al finalizar la configuracion. Utilizado en paneldibujo
	 * @see paneldibujo
	 */
	private String textoBoton;
	
	private Frame padreFrame;
	
	/** Constructor de la clase
	 * @param parent Frame sobre el que se muestra el cuadro
	 * @param xCentral Coordenada x central
	 * @param yCentral Coordenada y central
	 */
	public dialogoAcciones(Frame parent, int xCentral, int yCentral)
	{
		super(parent, true);
		padreFrame = parent;
		
		accionesDisponibles = new JComboBox(new Object[]{accionVisual.ENCENDER,accionVisual.APAGAR,accionVisual.ENVIAR_PAQUETE_IP,accionVisual.UNIRSE_A_GRUPO,accionVisual.DEJAR_GRUPO});
		
		setTitle("Configuracion de acciones");
		
		nombresOrdenadores = new Vector();
		this.listaAcciones = new Vector();
		
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
		
		JButton btn1 = new JButton("Anyadir");
		btn1.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				botonPulsado("Anyadir");
			}
		});

		JButton btn2 = new JButton("Aceptar");
		btn2.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				botonPulsado("Aceptar");
			}
		});

		JButton btn4 = new JButton("Borrar");
		btn4.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				botonPulsado("Borrar");
			}
		});

		JButton btn3 = new JButton("Borrar todos");
		btn3.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				botonPulsado("Borrar todos");
			}
		});

		tabla = new JTable(new modeloTablaAcciones(listaAcciones));

		ponColumnas();
		tabla.setPreferredScrollableViewportSize(new Dimension(300, 70));

		JScrollPane jspanel = new JScrollPane();
		jspanel.setViewportBorder(tabla.getBorder());
		jspanel.setViewportView(tabla);

		getContentPane().add(jspanel);
		jspanel.setBounds(5,5, 450, 150);
		
		getContentPane().add(accionesDisponibles);
		getContentPane().add(btn1);
		btn1.setBounds(4, 170, 81, 26);
		getContentPane().add(btn4);
		btn4.setBounds(100, 170, 81, 26);
		getContentPane().add(btn3);
		btn3.setBounds(196, 170, 100, 26);
		getContentPane().add(btn2);
		btn2.setBounds(380, 190, 81, 26);

		setResizable(false);
		int ancho = 470;
		int alto = 250;
		setBounds(xCentral-ancho/2, yCentral-alto/2, ancho, alto);
	}
	
	/** Funcion que controla los eventos de los botones 
	 * @param nombre Boton sobre el que se pincha click
	 */
	private void botonPulsado(String nombre)
	{
		if (nombre.compareTo("Anyadir")==0)
		{
			if (accionesDisponibles.getSelectedItem().equals(accionVisual.ENCENDER)){
				
			}
			else if (accionesDisponibles.getSelectedItem().equals(accionVisual.APAGAR)){
			}
			else if (accionesDisponibles.getSelectedItem().equals(accionVisual.ENVIAR_PAQUETE_IP)){
			}
			else if (accionesDisponibles.getSelectedItem().equals(accionVisual.UNIRSE_A_GRUPO)){
			}
			else if (accionesDisponibles.getSelectedItem().equals(accionVisual.DEJAR_GRUPO)){
			}
			
			//agregar la nueva accion a la lista
			
				tabla.setModel(new modeloTablaAcciones(listaAcciones));
				ponColumnas();
			
		}
		else if (nombre.compareTo("Borrar")==0)
		{
			int indice = tabla.getSelectedRow();
			
			if (indice>=0)
			{
				listaAcciones.remove(indice);	
				tabla.setModel(new modeloTablaAcciones(listaAcciones));
				ponColumnas();
			}
		}
		else if (nombre.compareTo("Borrar todos")==0)
		{
			listaAcciones = new Vector();

			tabla.setModel(new modeloTablaAcciones(listaAcciones));
			ponColumnas();
		}
		else if (nombre.compareTo("Aceptar")==0)
		{
			textoBoton = "Aceptar";
			setVisible(false);
			
		}
	}
	
//	/** Comprobacion de los envios configurados. No se permite nombres en blanco
//	 * ni superar o no llegar a tamMax o tamEnvios
//	 * @return boolean indicando si la configuracion es correcta
//	 * @see tamMax
//	 * @see tamEnvios
//	 */
//	private boolean compruebaEnvios()
//	{
//		int numero;
//		Vector temp = new Vector(getDatosTabla());
//		int tam = temp.size();
//		
//		for (int i=0; i<tam; i+=tabla.getColumnCount())
//		{
//			// No existe alguna de las direcciones
//			if ( ((String)temp.elementAt(i)).length()==0 || ((String)temp.elementAt(i+1)).length()==0)
//				return false;
//			
//			numero = (new Integer((String)temp.elementAt(i+2))).intValue();
//			if (numero<1 || numero>tamMax) return false;
//
//			numero = (new Integer((String)temp.elementAt(i+3))).intValue();
//			if (numero<1 || numero>tamEnvios) return false;
//		}
//		return true;
//	}

	/** Libera la memoria ocupada por el cuadro de dialogo */
	public void destruye()
	{
		dispose();
	}
	
	/** Devuelve el nombre del boton que ha sido pulsado
	 * @return String
	 */
	public String getBoton()
	{
		return textoBoton;
	}
	
//	/** Devuelve los datos de la tabla
//	 * @return Vector
//	 */
//	public Vector getDatosTabla()
//	{
//		Vector dev = new Vector();
//		
//		for (int i=0; i<tabla.getRowCount(); i++)
//			for (int j=0; j<tabla.getColumnCount(); j++)
//				dev.add(tabla.getValueAt(i, j));
//		
//		return dev;
//	}

//	/** Devuelve un vector de valores booleanos indicando la simulacion de cada uno de los errores */
//	public Vector getSeleccionesFragmentar()
//	{
//		Vector dev = new Vector();
//		
//		for (int i=0; i<tabla.getRowCount(); i++)
//			dev.add(tabla.getValueAt(i, 4).toString());
//		
//		return dev;
//	}
	
	/** Hace visible el cuadro de dialogo */
	public void muestra()
	{
		this.setVisible(true);
	}
	
	/** Metodo para poner objetos JComboBox para los nombres de las maquinas */
	private void ponColumnas()
	{
		tabla.getColumnModel().getColumn(1).setPreferredWidth(5);
		tabla.getColumnModel().getColumn(2).setPreferredWidth(25);

//		if (nombresOrdenadores != null)
//			for (int i=0; i<1; i++)
//			{
//				tabla.getColumnModel().getColumn(i).setCellEditor(new DefaultCellEditor(new JComboBox(nombresOrdenadores)));
//				tabla.getColumnModel().getColumn(i).setPreferredWidth(100);
//			
//				DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
//				
//				tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);		
//			}
	}
	
	public Vector getListaAcciones(){
		return listaAcciones;
	}
	
	/** Establece los datos de la tabla
	 * @param nombresOrdenadores Lista con los nombres de los ordenadores
	 * @param listaAcciones Configuracion de la lista de envios
	 */
	public void setTabla(Vector nombresOrdenadores, Vector listaAcciones)
	{
		this.listaAcciones = new Vector(listaAcciones);
		this.nombresOrdenadores = new Vector(nombresOrdenadores);
		tabla.setModel(new modeloTablaAcciones(listaAcciones));

		ponColumnas();
	}
	
}
