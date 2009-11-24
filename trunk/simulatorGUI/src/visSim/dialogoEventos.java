/** @author: tlfs & afzs */
package visSim;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.LayoutManager;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;
import javax.swing.JOptionPane;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JProgressBar;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;

import paneldibujo.dialogoFichero;

import util.copiaPortapapeles;
import visSim.modelosTablas.modeloColoresEventos;
import visSim.modelosTablas.modeloTablaEventos;

/** Clase creada para mostrar por pantalla los eventos de la simulacion
 */
public class dialogoEventos extends JDialog
{
	private Vector copiaEventos;
	private JScrollPane jspanel;
	private dialogoLeyenda leyenda;
	private JTable tabla;
	private String textoBoton;
	private Frame parent;
	public JProgressBar progreso;
	public JPanel panelProceso;
	public JLabel jlExportar;
	private JOptionPane Exportado;
	private int numFilas; 
	private Runnable tarea = null;
	public JTextField txtInstante;
	
	/** Constructor de la clase
	 * @param parent Frame sobre el que se muestra el cuadro
	 * @param xCentral Coordenada x central
	 * @param yCentral Coordenada y central
	 */
	public dialogoEventos(Frame parent, int xCentral, int yCentral, Vector listaEventos, final simuladorVisual simulacion)
	{
		super(parent, true);
		
		textoBoton = "";
		
		numFilas = 0;
		
		this.parent = parent;
		
		leyenda = new dialogoLeyenda(parent, xCentral, yCentral);

		setTitle("Eventos en la simulacion");
		
		copiaEventos = new Vector(listaEventos);
		
		txtInstante = new JTextField(5);
		txtInstante.setEnabled(false);
		
		jlExportar = new JLabel("Exportando:");
		
		getContentPane().add(jlExportar);
		jlExportar.setBounds(2, 218, 80, 10);
		jlExportar.setVisible(false);
		
		if (simulacion != null)
			txtInstante.setText(String.valueOf(simulacion.getInstanteSimulador()));
		
		JPanel pnlInstante = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		pnlInstante.add(new JLabel("Instante actual:"));
		pnlInstante.add(txtInstante);
		
		// Preparamos las cosas de la ventana
		getContentPane().setLayout(new BorderLayout());
		
		JPanel botones = new JPanel(new FlowLayout());
		
		panelProceso = new JPanel();//Dialog(this, true);
		panelProceso.setPreferredSize(new Dimension(200,200));
		panelProceso.setMaximumSize(new Dimension(200,200));
		panelProceso.setMinimumSize(new Dimension(200,200));
		progreso = new JProgressBar();
		progreso.setMinimum(0);

		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				setVisible(false);
			}
		});
		
		JButton btnCopiar = new JButton("Copiar");
		btnCopiar.setToolTipText("Copia el contenido de la tabla al portapapeles");
		btnCopiar.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				copiaPortapapeles.copia(copiaEventos);
			}
		});

		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				setVisible(false);
			}
		});

		tabla = new JTable(new modeloTablaEventos(copiaEventos));
		
		jspanel = new JScrollPane();
		jspanel.setViewportView(tabla);
		
		/*getContentPane().add(jspanel);
		jspanel.setBounds(5,5, 450, 150);*/
		ponDatos(listaEventos);

		final JButton btnDarPaso = new JButton("Da un paso");
		btnDarPaso.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				if (simulacion!=null)
				{
					simulacion.darUnPaso();
					leeEventos(simulacion);
					jspanel.validate();
					jspanel.getVerticalScrollBar().setValue(jspanel.getVerticalScrollBar().getMaximum() + jspanel.getVerticalScrollBar().getUnitIncrement(1));
					txtInstante.setText(String.valueOf(simulacion.getInstanteSimulador()));
				}
			}
		});

		final JButton btnDaEvento = new JButton("Siguiente Evento");
		btnDaEvento.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				if (simulacion!=null && simulacion.getInstanteSimulador() < simulacion.getMaxCantPasos())
				{
					Vector salidaEventos = new Vector(simulacion.getEventos());
					int cantidadEventos = salidaEventos.size();
					
					boolean masPasos=true;
					while (salidaEventos.size() == cantidadEventos && masPasos){
						masPasos = simulacion.darUnPaso();
						salidaEventos = new Vector(simulacion.getEventos());
					}
					leeEventos(simulacion);
					jspanel.validate();
					jspanel.getVerticalScrollBar().setValue(jspanel.getVerticalScrollBar().getMaximum() + jspanel.getVerticalScrollBar().getUnitIncrement(1));
				}
				txtInstante.setText(String.valueOf(simulacion.getInstanteSimulador()));
			}
		});

		final JButton btnSimular = new JButton("Simulacion completa");
		btnSimular.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				if (simulacion!=null)
				{
					textoBoton = "completa";
					simulacion.simulacionCompleta();
					leeEventos(simulacion);
					btnDarPaso.setEnabled(false);
					btnSimular.setEnabled(false);
					btnDaEvento.setEnabled(false);
					jspanel.validate();
					jspanel.getVerticalScrollBar().setValue(jspanel.getVerticalScrollBar().getMaximum());
				}
				if (simulacion != null)
					txtInstante.setText(String.valueOf(simulacion.getInstanteSimulador()));
			}
		});

		JButton btnLeyenda = new JButton("Leyenda");
		btnLeyenda.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				if(leyenda.isVisible())
					leyenda.setVisible(false);
				else
					leyenda.setVisible(true);
			}
		});
		
		JButton btnExportar = new JButton("Exportar");
		
		final dialogoEventos dialogo1 = this; 
		
		btnExportar.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				JButton fuente = null;
				
				fuente = (JButton)evt.getSource();
				//dialogoEventos padre = (dialogoEventos) fuente.getParent().getParent().getParent().getParent();
				dialogo1.lanzarDialogoProgreso();
			}
		});

		// Si no ha comenzado la simulacion, los botones de simulacion se muestran desactivados
		if (simulacion==null)
		{
			btnDarPaso.setEnabled(false);
			btnSimular.setEnabled(false);
			btnDaEvento.setEnabled(false);
		}

		botones.add(btnCopiar);
		botones.add(btnDarPaso);
		botones.add(btnDaEvento);
		botones.add(btnSimular);
		botones.add(btnLeyenda);
		botones.add(btnExportar);
		botones.add(btnAceptar);
		
		getContentPane().add(pnlInstante,BorderLayout.NORTH);
		getContentPane().add(jspanel,BorderLayout.CENTER);
		getContentPane().add(botones,BorderLayout.SOUTH);

		setResizable(false);
		int ancho = 670;
		int alto = 265;
		setBounds(xCentral-ancho/2, yCentral-alto/2, ancho, alto);
		
		setVisible(true);
	}
	
	public void lanzarDialogoProgreso(){
		
		progreso.setMaximum(tabla.getRowCount()-1);
		progreso.setValue(0);
		progreso.setIndeterminate(false);
		progreso.setPreferredSize(new Dimension(100,100));
		getContentPane().add(progreso);
		progreso.setBounds(80, 215, 100, 15);
		
		dialogoFichero elige = new dialogoFichero("csv");
		
		
		if(elige.mostrar(this.parent, "Exportar a...", "fichero.csv") != JFileChooser.CANCEL_OPTION){//elige.getNomFich() != null || elige.getNomFich().compareTo("") != 0){

			jlExportar.setVisible(true);
			progreso.setVisible(true);
//			 Creamos el dialogo de progreso
			tarea = new Runnable() {
				
			    public void run() {
			    	panelProceso.validate();
			    	panelProceso.setVisible(true);
				}
			};
			
			javax.swing.SwingUtilities.invokeLater(tarea);
			
			try{
				java.io.FileOutputStream fileInforme=new java.io.FileOutputStream(elige.getSelectedFile(),false);
				java.io.BufferedOutputStream buffer=new java.io.BufferedOutputStream(fileInforme,100);
				//Dibujamos las cabeceras
					for(int i = 0; i < tabla.getColumnCount(); i++)
						buffer.write((tabla.getColumnName(i)+";").getBytes());
				buffer.write("\n".getBytes());
				for(int i=0;i < tabla.getRowCount(); i++){
					for(int j=0; j < tabla.getColumnCount(); j++)
						buffer.write((tabla.getValueAt(i, j)+";").getBytes());
					buffer.write("\n".getBytes());
					actualizaProgreso(i);
				}
				
				buffer.close();
				fileInforme.close();
				cerrarProgreso();
			}catch(Exception e){
				;
			};

			
			//VentanaDialogo.obtenerJProgressBarDialogo(dialogo).setMaximum(getTablaResultados().getRowCount());

			;
		}
	}
		
	
	public void actualizaProgreso(int valor){
		progreso.setValue(valor);
	}
	
	public void cerrarProgreso(){
		
		Exportado = new JOptionPane();
		Exportado.showMessageDialog(this, "Fichero generado correctamente");
		jlExportar.setVisible(false);
		progreso.setVisible(false);
		//progreso = null;
	}
	
	public void destruye()
	{
		dispose();
		leyenda.dispose();
	}

	/** Devuelve el nombre del boton que ha sido pulsado
	 * @return String
	 */
	public String getBoton()
	{
		return textoBoton;
	}
	
	private void leeEventos(simuladorVisual simulacion)
	{
		copiaEventos = new Vector(simulacion.getEventos());
		ponDatos(copiaEventos);
	}

	/** Metodo que actualiza los datos de la tabla, pone los colores y establece
	 * los anchos de las columnas
	 * @param datos Vector de datos que muestra la tabla
	 */
	private void ponDatos(Vector datos)
	{
		tabla.setModel(new modeloTablaEventos(datos));
		tabla.setDefaultRenderer(String.class, new modeloColoresEventos(datos));
		tabla.setPreferredScrollableViewportSize(new Dimension(300, 70));
		
		// Ajustamos los anchos de las columnas
		tabla.getColumnModel().getColumn(0).setPreferredWidth(30);
		tabla.getColumnModel().getColumn(1).setPreferredWidth(15);
		tabla.getColumnModel().getColumn(2).setPreferredWidth(25);
		tabla.getColumnModel().getColumn(3).setPreferredWidth(190);
		tabla.getColumnModel().getColumn(4).setPreferredWidth(25);
		
		// TODO: Esto produce un parpadeo en cada refresco
		//jspanel.getVerticalScrollBar().setValue(Integer.MAX_VALUE);
	}
}
