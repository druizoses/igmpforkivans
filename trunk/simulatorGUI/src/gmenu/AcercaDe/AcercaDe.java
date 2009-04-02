/** @author: tlfs & afzs */
package gmenu.AcercaDe;

import java.awt.Color;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.border.EtchedBorder;

import util.nomiconos;

/** Clase acercade para mostrar la informacion del programa y los autores */
public class AcercaDe extends JDialog
{
	/** Constructor de la clase
	 * @param pframe es el objeto
	 * @param bol booleano a true para crear un dialogo modal
	 * @param xCentral
	 * @param yCentral coordenadas */
	public AcercaDe(Frame pframe, boolean bol, int xCentral, int yCentral)
	{
		// crea el dialogo
		super(pframe, bol);
		
		//se crea la etiqueta
		JLabel eti1 = new JLabel();
		//se le anyade un icono
		eti1.setIcon(new ImageIcon(nomiconos.nomAcerca));
		eti1.setBorder(new EtchedBorder());
		// para que no se cambie el tamanyo
		setResizable(false);
		
		//se anyade la etiqueta al panel en la coordenadas adecuadas
		getContentPane().add(eti1);
		eti1.setBounds(10, 50, 105, 105);
		
		//se crea texto en la ventana
		JTextPane paneltext = new JTextPane();
		getContentPane().setLayout(null);
		
		setTitle("Acerca de");
		addWindowListener(new WindowAdapter()
		{
			//para cerrar la ventana
			public void windowClosing(WindowEvent evt)
			{
				setVisible(false);
				dispose();
			}
		});
		
		//se anyade el texto a la ventana modal
		paneltext.setBackground(new Color(204, 204, 204));
		paneltext.setEditable(false);
		paneltext.setText("\nProyecto Simulador de redes\n\nTutores:\n  D. Francisco A. Candelas Herias\n  D. Fernando Torres Medina\n\nAutores:\n  Jose Maria Diaz Gorriz\n  Antonio Fco. Zaragoza Soler\n  Teresa Lourdes Fabuel Serrano");
		paneltext.setAutoscrolls(false);
		getContentPane().add(paneltext);
		paneltext.setBounds(140, 0, 280, 310);
		
		//se establecen las coordenadas
		setBounds(xCentral-200, yCentral-125, 400, 250);
	}
	
	/** funcion muestra que visualiza la ventana de acercade */
	public void muestra()
	{
		this.setVisible(true);
	}
}
