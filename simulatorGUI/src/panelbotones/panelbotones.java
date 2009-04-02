/** @author: tlfs & afzs */
package panelbotones;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import objetoVisual.equiposRegistrados;
import util.colores;
import util.nomiconos;

/** Clase panelbotones */
public class panelbotones extends JPanel
{
	/** vector listabotones para almacenar la ruta de los botones*/
	/** vector botonesVisuales para almacenar los botones */
	private Vector listabotones, botonesVisuales;

	/** menu para anyadir los botones */
	private JMenuItem mnuitem;

	/** Constructor de panelbotones 
	 * @param oyente para capturar los eventos sobre el panel
	 */
	public panelbotones(ActionListener oyente)
	{
		setBackground(colores.fondoBarras);
		//se crea el menu y se le anyade el oyente
		mnuitem = new JMenuItem();
		mnuitem.addActionListener(oyente);
		
		//se crea el vector de botones
		botonesVisuales = new Vector();
		
		//se establece el borde y el nombre del panel
		setLayout(new BorderLayout());
		setBorder(new javax.swing.border.EtchedBorder());
		setName("panelBotones");
		
		//se obtienen los iconos de los equipos registrados
		listabotones = new Vector(equiposRegistrados.listaIconos());
		creaBotonesVisuales();
		
		// Anyadimos los botones al panel
		for (int i=0; i<listabotones.size(); i++)
			add((JLabel)botonesVisuales.elementAt(i), BorderLayout.WEST);
		
		// Anyadimos un JLabel vacio para que el ultimo boton no se quede colgando
		add(new JLabel(), BorderLayout.WEST);

		((boton)botonesVisuales.elementAt(0)).setBordeSelec();

		// Ponemos el tamanyo para que el panel contenedor de scroll sepa que hacer
		setPreferredSize(new Dimension(nomiconos.tam+1, (nomiconos.tam+2)*listabotones.size()));
		revalidate();
	}
	
	/** funcion creaBotonesVisuales que crea los botones */
	public void creaBotonesVisuales()
	{
		int tam = listabotones.size();

		//para toda el vector listabotones crea los botones con sus iconos y parametros 
	   for (int i=0; i<tam; i++)
	   {
			final boton botontemp = new boton(""+botonesVisuales.size());
			botontemp.setIcono((String) listabotones.elementAt(i));
			botontemp.setCoord(0, nomiconos.tam*i);
			botontemp.setTip();
			
			botontemp.addMouseListener(new MouseAdapter()
			{
				// Se hace click sobre un equipo
				public void mouseClicked(MouseEvent evt)
				{
					ponMensaje("---" + (new nomiconos()).getNomIconoCursor(botontemp.getIcono()));
					//desactiva el boton que estaba activado
					desactivaActivado();
					botontemp.setBordeSelec();
				}
			});

			botonesVisuales.add(botontemp);
	   }
	}
	
	/** funcion desactivaActivado que desactiva el boton que actualmente esta activado*/
	private void desactivaActivado()
	{
		boolean actualizado = false;
		int tam = botonesVisuales.size();
		
		//recorre los botones y desactiva el boton que actualmente esta activado
		for (int i=0; i<tam && !actualizado; i++)
			if (((boton)botonesVisuales.elementAt(i)).getSelec())
			{
				((boton)botonesVisuales.elementAt(i)).setBordeNormal();
				actualizado = true;
			}
	}

	/** funcion ponMensaje para mandar un mensaje
	 * @param msg es el mensaje
	 */
	private void ponMensaje(String msg)
	{
		mnuitem.setText(msg);
		mnuitem.doClick(0);
	}
}
