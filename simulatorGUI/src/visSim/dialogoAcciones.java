/** @author: tlfs & afzs */
package visSim;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;

import objetoVisual.listaObjetos;
import objetoVisual.accionVisual.accionApagarVisual;
import objetoVisual.accionVisual.accionVisual;

import util.muestraAviso;
import util.utilTexto;
import visSim.dialogosAcciones.dialogoAccionApagar;
import visSim.dialogosAcciones.dialogoAccionBase;
import visSim.dialogosAcciones.dialogoAccionDejarGrupo;
import visSim.dialogosAcciones.dialogoAccionEncender;
import visSim.dialogosAcciones.dialogoAccionEnviarPaqueteIP;
import visSim.dialogosAcciones.dialogoAccionUnirseAGrupo;
import visSim.modelosTablas.modeloTablaAcciones;


/** Clase creada para la configuracion de envios de tramas en la topologia.
 * En dicha configuracion se situan las maquinas origen y destino, el tamanyo
 * de la trama de datos y el numero de copias que se envian. 
 */
public class dialogoAcciones extends JDialog
{
	/** Vector que contiene la configuracion del envio de tramas */
	private listaObjetos lista;

	/** Tabla donde se mostraran los envios */
	private JTable tabla;
	
	private JComboBox accionesDisponibles;
	
	private JTextField txtPasos;
	
	private JLabel lblPasos;
	
	/** Nombre del boton pulsado al finalizar la configuracion. Utilizado en paneldibujo
	 * @see paneldibujo
	 */
	private String textoBoton;
	
	private Frame padreFrame;
	
	int xCentral, yCentral;
	
	/** Constructor de la clase
	 * @param parent Frame sobre el que se muestra el cuadro
	 * @param xCentral Coordenada x central
	 * @param yCentral Coordenada y central
	 * @param maxNumeroPasos 
	 */
	public dialogoAcciones(Frame parent, int xCentral, int yCentral, int maxNumeroPasos)
	{
		super(parent, true);
		padreFrame = parent;
		
		accionesDisponibles = new JComboBox(new Object[]{accionVisual.ENCENDER,accionVisual.APAGAR,accionVisual.ENVIAR_PAQUETE_IP,accionVisual.UNIRSE_A_GRUPO,accionVisual.DEJAR_GRUPO});
		
		setTitle("Configuracion de acciones");
		
		// Preparamos las cosas de la ventana
		getContentPane().setLayout(new BorderLayout());

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				textoBoton = "Cancelar";
				setVisible(false);
			}
		});
		
		JButton btn1 = new JButton("A�adir");
		btn1.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				botonPulsado("A�adir");
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

		JButton btn7 = new JButton("Editar");
		btn7.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				botonPulsado("Editar");
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

		tabla = new JTable(new modeloTablaAcciones(null));

		ponColumnas();
		tabla.setPreferredScrollableViewportSize(new Dimension(700, 70));

		JScrollPane jspanel = new JScrollPane();
		jspanel.setViewportBorder(tabla.getBorder());
		jspanel.setViewportView(tabla);

		getContentPane().add(BorderLayout.CENTER,jspanel);
		
		JPanel botones = new JPanel();
		botones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		botones.add(accionesDisponibles);
		botones.add(btn1);
		botones.add(btn7);
		botones.add(btn4);
		botones.add(btn3);
		botones.add(btn2);
		getContentPane().add(BorderLayout.SOUTH,botones);
		
		JPanel pnlPasos = new JPanel();
		pnlPasos.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblPasos = new JLabel("Cantidad de pasos:");
		pnlPasos.add(lblPasos);
		
		lblPasos = new JLabel(utilTexto.convertToTime(maxNumeroPasos));
		final JLabel lblPasosAux = lblPasos;
		
		txtPasos = new JTextField(5);
		txtPasos.setText(String.valueOf(maxNumeroPasos));
		pnlPasos.add(txtPasos);
		txtPasos.addFocusListener(new FocusAdapter(){
			@Override
			public void focusLost(FocusEvent e) {
				lblPasosAux.setText(utilTexto.convertToTime(getCantidadPasos()));
			}});

		pnlPasos.add(new JLabel(" (Equivale a "));
		pnlPasos.add(lblPasos);		
		pnlPasos.add(new JLabel(")"));

		getContentPane().add(BorderLayout.NORTH,pnlPasos);

		this.xCentral = xCentral;
		this.yCentral = yCentral;
		
		setResizable(false);
		int ancho = 830;//470;
		int alto = 250;
		setBounds(xCentral-ancho/2, yCentral-alto/2, ancho, alto);
	}
	
	public int getCantidadPasos(){
		return Integer.valueOf(txtPasos.getText());
	}
	
	/** Funcion que controla los eventos de los botones 
	 * @param nombre Boton sobre el que se pincha click
	 */
	private void botonPulsado(String nombre)
	{
		if (nombre.compareTo("A�adir")==0)
		{
			dialogoAccionBase dlgAccion = null;
			if (accionesDisponibles.getSelectedItem().equals(accionVisual.ENCENDER)){
				dlgAccion = new dialogoAccionEncender(this,this.xCentral, this.yCentral, lista);
			}
			else if (accionesDisponibles.getSelectedItem().equals(accionVisual.APAGAR)){
				dlgAccion = new dialogoAccionApagar(this,this.xCentral, this.yCentral, lista);
			}
			else if (accionesDisponibles.getSelectedItem().equals(accionVisual.ENVIAR_PAQUETE_IP)){
				dlgAccion = new dialogoAccionEnviarPaqueteIP(this,this.xCentral, this.yCentral, lista);
			}
			else if (accionesDisponibles.getSelectedItem().equals(accionVisual.UNIRSE_A_GRUPO)){
				dlgAccion = new dialogoAccionUnirseAGrupo(this,this.xCentral, this.yCentral, lista);
			}
			else if (accionesDisponibles.getSelectedItem().equals(accionVisual.DEJAR_GRUPO)){
				dlgAccion = new dialogoAccionDejarGrupo(this,this.xCentral, this.yCentral, lista);
			}
			
			dlgAccion.setVisible(true);

			String resultado = dlgAccion.getResultado();
			//agregar la nueva accion a la lista
			if (resultado != null && resultado.equals(dialogoAccionBase.ACEPTAR)){
				accionVisual accion = dlgAccion.getAccionVisual();
				lista.listaAcciones.add(accion);
				tabla.setModel(new modeloTablaAcciones(lista.listaAcciones));
				ponColumnas();
			}
		}

		else if (nombre.compareTo("Editar")==0)
		{
			int indice = tabla.getSelectedRow();
			
			if (indice>=0)
			{
				accionVisual acc = (accionVisual) lista.listaAcciones.elementAt(indice);
				tabla.setModel(new modeloTablaAcciones(lista.listaAcciones));
				ponColumnas();
				dialogoAccionBase dlgAccion = acc.createDialog(this,this.xCentral, this.yCentral, lista);
				dlgAccion.setVisible(true);
				String resultado = dlgAccion.getResultado();
				if (resultado != null && resultado.equals(dialogoAccionBase.ACEPTAR)){
					accionVisual accion = dlgAccion.getAccionVisual();
					lista.listaAcciones.set(indice, accion);
					tabla.setModel(new modeloTablaAcciones(lista.listaAcciones));
					ponColumnas();
				}
			}
		}
		
		else if (nombre.compareTo("Borrar")==0)
		{
			int indice = tabla.getSelectedRow();
			
			if (indice>=0)
			{
				lista.listaAcciones.remove(indice);	
				tabla.setModel(new modeloTablaAcciones(lista.listaAcciones));
				ponColumnas();
			}
		}
		else if (nombre.compareTo("Borrar todos")==0)
		{
			lista.listaAcciones.removeAllElements();
			tabla.setModel(new modeloTablaAcciones(lista.listaAcciones));
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
		tabla.getColumnModel().getColumn(0).setPreferredWidth(50);
		tabla.getColumnModel().getColumn(1).setPreferredWidth(80);
		tabla.getColumnModel().getColumn(2).setPreferredWidth(100);
		tabla.getColumnModel().getColumn(3).setPreferredWidth(600);
		
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
	
	public listaObjetos getLista(){
		return lista;
	}
	
	/** Establece los datos de la tabla
	 * @param nombresOrdenadores Lista con los nombres de los ordenadores
	 */
	public void setTabla(listaObjetos lista)
	{
		this.lista = lista;
		tabla.setModel(new modeloTablaAcciones(lista.listaAcciones));

		ponColumnas();
	}
	
}
