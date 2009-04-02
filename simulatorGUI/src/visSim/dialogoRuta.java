/** @author: tlfs & afzs */
package visSim;

import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import Redes.IPv4.DireccionIPv4;

/** Esta clase implementa la ventana que se abre al elegir la opcion Nueva o Modificar
 * de la pestanya de rutas mostrada en las propiedades de los equipos */
public class dialogoRuta extends JDialog
{
	JComboBox comboInterfaz;
	private String textoBoton;
	private JTextField txtDestino, txtMascara, txtGateway;
	
	/** Constructor de la clase
	 * @param parent Frame que contiene el cuadro
	 * @param xCentral Coordenada x central de la pantalla
	 * @param yCentral Coordenada y central de la pantalla
	 * @param destino IP destino de la ruta
	 * @param mascara Mascara de red
	 * @param gateway Puerta de la ruta
	 * @param interfaz Interfaz perteneciente a esta ruta
	 * @param listaNombresInterfaces
	 */
	public dialogoRuta(Frame parent, int xCentral, int yCentral, String titulo, String destino, String mascara, String gateway, String interfaz, Vector listaNombresInterfaces)
	{
		super(parent, true);
		getContentPane().setLayout(null);
		setTitle(titulo);

		addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(java.awt.event.WindowEvent evt)
			{
				textoBoton = "Cerrar";
				setVisible(false);
				dispose();
			}
		});
		
		JLabel jLabel1 = new JLabel("Destino: ");
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		getContentPane().add(jLabel1);
		jLabel1.setBounds(5, 20, 60, 20);

		txtDestino = new JTextField(destino);
		getContentPane().add(txtDestino);
		txtDestino.setBounds(80, 20, 150, 20);

		JLabel jLabel2 = new JLabel("Mascara:");
		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		getContentPane().add(jLabel2);
		jLabel2.setBounds(5, 50, 60, 20);

		txtMascara = new JTextField(mascara);
		getContentPane().add(txtMascara);
		txtMascara.setBounds(80, 50, 150, 20);

		JButton btn2 = new JButton("Mascara");
		btn2.setToolTipText("Pone la mascara de red por defecto");
		getContentPane().add(btn2);
		btn2.setBounds(240, 50, 90, 20);
		btn2.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				ratonClicked("Mascara");
			}
		});

		JLabel jLabel3 = new JLabel("Gateway: ");
		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		getContentPane().add(jLabel3);
		jLabel3.setBounds(5, 80, 60, 20);

		txtGateway = new JTextField(gateway);
		getContentPane().add(txtGateway);
		txtGateway.setBounds(80, 80, 150, 20);
		
		JLabel jLabel4 = new JLabel("Interfaz");
		jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		getContentPane().add(jLabel4);
		jLabel4.setBounds(5, 110, 60, 20);

		comboInterfaz = new JComboBox(listaNombresInterfaces);
		comboInterfaz.setSelectedItem(interfaz);
		getContentPane().add(comboInterfaz);
		comboInterfaz.setBounds(80, 110, 150, 20);
		
		
		JButton btn1 = new JButton("Aceptar");
		getContentPane().add(btn1);
		btn1.setBounds(80, 170, 90, 26);
		btn1.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				ratonClicked("Aceptar");
			}
		});

		JButton btn3 = new JButton("Cancelar");
		getContentPane().add(btn3);
		btn3.setBounds(180, 170, 90, 26);
		btn3.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				ratonClicked("Cancelar");
			}
		});

		setResizable(false);
		int ancho = 350;
		int alto = 240;
		setBounds(xCentral-ancho/2, yCentral-alto/2, ancho, alto);

		setVisible(true);
	}

	/** Crea un objeto de tipo DireccionIPv4 para comprobar que la IP es correcta */
	private boolean compruebaIP(String iptexto)
	{
		boolean correcto = true;

		try
		{
			new DireccionIPv4(iptexto);
		}
		catch(Exception e)
		{
			correcto = false;
		}

		return correcto;
	}
	
	/** Libera la memoria ocupada por el cuadro de dialogo */
	public void destruye()
	{
		dispose();
	}
	
	/** Devuelve el nombre del boton que ha sido pulsado */
	public String getBoton()
	{
		return textoBoton;
	}

	/** Devuelve la IP destino de la ruta */
	public String getDestino()
	{
		return txtDestino.getText();
	}

	/** Devuelve el Gateway de la ruta */
	public String getGateway()
	{
		return txtGateway.getText();
	}

	/** Devuelve la mascara de la ruta */
	public String getMascara()
	{
		return txtMascara.getText();
	}

	public String getNombreInterfaz()
	{
		if (comboInterfaz.getItemCount()==0)
			return "N/A";

		return (String)comboInterfaz.getSelectedItem();
	}
	
	/** Evento Click del raton */
	private void ratonClicked(String nombre)
	{
		if (nombre.compareTo("Aceptar")==0)
		{
			if (!compruebaIP(txtDestino.getText()))
				txtDestino.setText("Destino incorrecto");
			else if (!compruebaIP(txtMascara.getText()))
				txtMascara.setText("Mascara incorrecta");
			else if (!compruebaIP(txtGateway.getText()))
				txtGateway.setText("Gateway incorrecto");
			else
			{
				textoBoton = "Aceptar";
				setVisible(false);
			}
		}
		else if (nombre.compareTo("Cancelar")==0)
		{
			textoBoton = "Cancelar";
			setVisible(false);
		}
		else if (nombre.compareTo("Mascara")==0)
		{
			/* Si la IP es correcta ponemos la mascara */
			if (compruebaIP(txtDestino.getText()))
			{
				int b0 = (new Integer(txtDestino.getText().substring(0, txtDestino.getText().indexOf(".")))).intValue();

			   txtMascara.setText("255.255.255.255");  

				if(b0>=1 && b0<=127)                            // clase A
					txtMascara.setText("255.0.0.0");        
				else if(b0>=128 && b0<=191)                     // clase B
				   txtMascara.setText("255.255.0.0");
				else if(b0>=192 && b0<=223)                     // clase C
		   		txtMascara.setText("255.255.255.0");
  			}
  			else
  				txtMascara.setText("Destino incorrecto");
		}
	}
}
