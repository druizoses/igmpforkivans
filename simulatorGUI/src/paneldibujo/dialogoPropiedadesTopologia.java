/** @author: tlfs & afzs */
package paneldibujo;

import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import objetoVisual.propiedadesTopologia;

/** Clase que implementa la busqueda de equipos en la topologia, con la posibilidad
 * de utilizar expresiones regulares para ello.
 */
public class dialogoPropiedadesTopologia extends JDialog
{
	/** Clase utilizada para conocer el boton que ha sido pulsado una vez cerrado el cuadro */
	private String textoBoton;
	
	private JTextField txtAutor, txtFecha;
	private JTextArea texto;
	
	/** Constructor de la clase
	 * @param parent Frame que contendra el JDialog
	 * @param xCentral Coordenada x central de la pantalla
	 * @param yCentral Coordenada y central de la pantalla
	 * @param listaNombres Lista de nombres de equipos de la topologia
	 */
	public dialogoPropiedadesTopologia(Frame parent, int xCentral, int yCentral, propiedadesTopologia propTopo)
	{
		super(parent, true);
		
		setTitle("Propiedades de la topologia");
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent evt)
			{
				textoBoton = "Cancelar";
				setVisible(false);
			}
		});
		
		// Preparamos las cosas de la ventana
		getContentPane().setLayout(null);

		JLabel label1 = new JLabel("Autor:");
		label1.setToolTipText("Nombre del autor del fichero");
		getContentPane().add(label1);
		label1.setBounds(20, 20, 50, 16);

		txtAutor = new JTextField(propTopo.getAutor());
		getContentPane().add(txtAutor);
		txtAutor.setBounds(80, 20, 200, 20);

		JLabel label2 = new JLabel("Fecha: ");
		label2.setToolTipText("Fecha de creacion");
		getContentPane().add(label2);
		label2.setBounds(20, 50, 60, 16);

		txtFecha = new JTextField(propTopo.getFecha());
		getContentPane().add(txtFecha);
		txtFecha.setBounds(80, 50, 200, 20);
		txtFecha.setSelectionStart(0);
		
		JLabel label3 = new JLabel("Comentario: ");
		getContentPane().add(label3);
		label3.setBounds(20, 80, 100, 16);

		texto = new JTextArea(propTopo.getComentario());
		texto.setLineWrap(true);
		texto.setWrapStyleWord(true);

		JScrollPane jspanel = new JScrollPane(texto);
		jspanel.setViewportBorder(texto.getBorder());
		jspanel.setViewportView(texto);

		getContentPane().add(jspanel);
		jspanel.setBounds(80, 80, 200, 150);

		JButton btn2 = new JButton("Aceptar");
		btn2.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				ratonClicked("Aceptar");
			}
		});
		getContentPane().add(btn2);
		btn2.setBounds(110, 250, 80, 26);

		setResizable(false);
		int ancho = 300;
		int alto = 320;
		setBounds(xCentral-ancho/2, yCentral-alto/2, ancho, alto);
	}

	/** Funcion que devuelve el texto del boton que ha sido pulsado antes de cerrar la ventana */
	public String getBoton()
	{
		return textoBoton;
	}
	
	/** Funcion que hace visible el cuadro en la pantalla */
	public void muestra()
	{
		this.setVisible(true);
	}

	/** Controla el evento click del raton sobre los distintos componentes del cuadro */
	private void ratonClicked(String nombre)
	{
		if (nombre.compareTo("Aceptar")==0)
		{
			textoBoton = "Aceptar";
			setVisible(false);
		}
	}
	
	public propiedadesTopologia getPropiedades()
	{
		return new propiedadesTopologia(txtAutor.getText(), txtFecha.getText(), texto.getText()); 
	}
}
