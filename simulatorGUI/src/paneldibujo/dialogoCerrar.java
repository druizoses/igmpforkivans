/** @author: tlfs & afzs */
package paneldibujo;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

/** Clase creada para mostrar al usuario una lista de ficheros modificados
 * antes de cerrar la aplicacion para poder almacenar los que el seleccione
 */
public class dialogoCerrar extends JDialog
{
	/** Objeto utilizado para situar los nombres de los ficheros y un componente
	 * que permite seleccionar cuales se desean almacenar */
	JRadioButton filas[];
	
	/** Objecto sobre el que se pondra la lista de ficheros */
	Box ficheros;
	
	private String textoBoton;
	
	/** Constructor de la clase
	 * @param lFicheros Lista de nombres de ficheros
	 * @param xCentral Coordenada x central de la pantalla
	 * @param yCentral Coordenada y central de la pantalla
	 * @param oyente MouseListener para controlar los eventos de raton. Se hace de
	 * esta forma para que la clase que llama pueda controlar las distintas acciones
	 * de los botones.
	 */
	public dialogoCerrar(Frame padreParent, Vector lFicheros, int xCentral, int yCentral, MouseListener oyente, String botonCerrar)
	{
		super(padreParent, true);
		setResizable(false);
		setTitle("Almacenar");
		textoBoton = "";
		
		getContentPane().setLayout(new BorderLayout());

		ficheros = new Box(BoxLayout.Y_AXIS);
		ficheros.setBorder(new javax.swing.border.EtchedBorder());

		construyeLista(lFicheros);
		
		JLabel saludo = new JLabel("Elija la opcion que desee.");
		
		JScrollPane panelBarras = new JScrollPane(ficheros);
		
		JPanel panelDerecho = new JPanel();
		panelDerecho.setBorder(new javax.swing.border.EtchedBorder());
		panelDerecho.setLayout(new GridLayout(4, 1));
		
		JButton btn1 = new JButton(botonCerrar);
		btn1.setName(botonCerrar);
		btn1.addMouseListener(oyente);

		JButton btn2 = new JButton("Todos");
		btn2.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				ratonClicked("Todos");
			}
		});

		JButton btn3 = new JButton("Ninguno");
		btn3.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				ratonClicked("Ninguno");
			}
		});

		JButton btn4 = new JButton("Cancelar");
		btn4.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				ratonClicked("Cancelar");
			}
		});
		
		panelDerecho.add(btn2);
		panelDerecho.add(btn3);
		panelDerecho.add(btn1);
		panelDerecho.add(btn4);
		
		getContentPane().add(saludo, BorderLayout.NORTH);
		getContentPane().add(panelBarras, BorderLayout.WEST);
		getContentPane().add(panelDerecho, BorderLayout.EAST);

		pack();

		int anchoCuadro = getWidth();
		int altoCuadro = getHeight();

		setBounds(xCentral-anchoCuadro/2, yCentral-altoCuadro/2, anchoCuadro, altoCuadro);
	}
	
	/** Clase que pone la lista de ficheros en el objeto Box */
	public void construyeLista(Vector lFicheros)
	{
		filas = new JRadioButton[lFicheros.size()];
		
		for (int i=0; i<lFicheros.size(); i++)
		{
			filas[i] = new JRadioButton((String)lFicheros.elementAt(i), true);
			ficheros.add(filas[i]);
		}
	}
	
	/** Devuelve una lista de ficheros seleccionados para almacenar */
	public Vector getListaSeleccionados()
	{
		Vector dev = new Vector();
		
		for (int i=0; i<filas.length; i++)
			if (filas[i].isSelected())
				dev.add( filas[i].getText() );

		return dev;
	}
	
	/** Hace visible el cuadro */
	public void muestra()
	{
		setVisible(true);
	}
	
	/** Eventos sobre los botones */
	private void ratonClicked(String nombre)
	{
		textoBoton = nombre;
		
		if (nombre.compareTo("Todos")==0)			seleccionaTodos(true);
		else if (nombre.compareTo("Ninguno")==0)	seleccionaTodos(false);
		else if (nombre.compareTo("Cancelar")==0)	setVisible(false);
	}
	
	/** Funcion que hace que todos los ficheros esten almacenados segun el valor recibido */
	public void seleccionaTodos(boolean valor)
	{
		for (int i=0; i<filas.length; i++)
			filas[i].setSelected(valor);
	}
	
	public String getTextoBoton()
	{
		return textoBoton;
	}
}
