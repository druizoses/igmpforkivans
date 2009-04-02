/** @author: tlfs & afzs */
package visSim;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import util.muestraAviso;
import Redes.IPv4.DireccionIPv4;

/** Esta clase implementa la ventana que se abre al elegir la opcion Nueva o Modificar
 * de la pestanya de interfaces mostrada en las propiedades de los equipos */
public class dialogoInterfaz extends JDialog implements ActionListener
{
	/** Desplegable que muestra al usuario las conexiones fisicas que hay en el equipo */
	private JComboBox comboConexiones;
	
	/** Lista de las conexiones fisicas que hay en el equipo */
	private Vector listaConexiones;
	
	/** Vector que almacena las direcciones IPs de todas las interfaces del equipo */
	private Vector listaIP;

	/** Vector que almacena los nombres de todas las interfaces del equipo.
	 * Se utilizara para no repetir dichos nombres */
	private Vector listaNombres;
	
	/** Contiene una lista de tipos de equipos a los que se encuentra conectado el equipo */
	private Vector listaTipos;

	/** Boton pulsado al finalizar la configuracion de las interfaces */
	private String textoBoton;

	/** Distintos campos de texto mostrados en el dialogo */
	private JTextField txtNombre, txtIP, txtMascara, txtMAC;
	
	private Frame padreFrame;
	
	/** Constructor de la clase. Debe recibir todos los parametros que se comentan a
	 * continuacion para su correcto funcionamiento
	 * @param parent Frame que contiene el cuadro
	 * @param xCentral Coordenada x central de la pantalla
	 * @param yCentral Coordenada y central de la pantalla
	 * @param nombre Nombre de la interfaz que se muestra en el cuadro
	 * @param ip Nombre de la interfaz que se muestra en el cuadro
	 * @param mascara Mascara de la interfaz
	 * @param mac Direccion MAC de la interfaz
	 * @param conecta Equipo al que se encuentra conectado
	 * @param listaNombres Nombres de todas las interfaces del equipo
	 * @param listaIP IPs de todas las interfaces del equipo
	 * @param listaConexiones Conexiones fisicas del equipo
	 * @param listaTipos Tipos a los que conecta la interfaz
	 */
	public dialogoInterfaz(Frame parent, int xCentral, int yCentral, String titulo, String nombre, String ip, String mascara, String mac, String conecta, Vector listaNombres, Vector listaIP, Vector listaConexiones, Vector listaTipos)
	{
		super(parent, true);
		padreFrame = parent;

		// Copiamos la lista de nombres
		this.listaNombres = new Vector(listaNombres);

		// Copiamos la lista de IPs
		this.listaIP = new Vector(listaIP);
		
		// Copiamos la lista de Equipos conectados
		this.listaConexiones = new Vector(listaConexiones);

		// Copiamos la lista de Tipos de Equipos conectados
		this.listaTipos = new Vector(listaTipos);
		
		txtNombre = new JTextField(nombre);
		txtIP = new JTextField(ip);
		txtMascara = new JTextField(mascara);
		txtMAC = new JTextField(mac);

		
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
		
		JLabel jLabel1 = new JLabel("Nombre:");
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		getContentPane().add(jLabel1);
		jLabel1.setBounds(5, 20, 60, 20);

		getContentPane().add(txtNombre);
		txtNombre.setBounds(80, 20, 150, 20);

		this.listaConexiones.insertElementAt("N/A", 0);
		comboConexiones = new JComboBox(this.listaConexiones);
		comboConexiones.setName("comboConexiones");
		comboConexiones.setToolTipText("Conexion del equipo");
		comboConexiones.setSelectedItem(conecta);
		getContentPane().add(comboConexiones);
		comboConexiones.setBounds(250, 20, 80, 20);
		comboConexiones.addActionListener(this);

		JLabel jLabel2 = new JLabel("IP:");
		jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		getContentPane().add(jLabel2);
		jLabel2.setBounds(5, 50, 60, 20);

		getContentPane().add(txtIP);
		txtIP.setBounds(80, 50, 150, 20);
		
		JLabel jLabel3 = new JLabel("Mascara:");
		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		getContentPane().add(jLabel3);
		jLabel3.setBounds(5, 80, 60, 20);

		getContentPane().add(txtMascara);
		txtMascara.setBounds(80, 80, 150, 20);

		JButton btn2 = new JButton("Mascara");
		btn2.setToolTipText("Pone la mascara de red por defecto");
		getContentPane().add(btn2);
		btn2.setBounds(240, 80, 90, 20);
		btn2.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				botonPulsado("Mascara");
			}
		});
		
		JLabel jLabel4 = new JLabel("MAC:");
		jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		getContentPane().add(jLabel4);
		jLabel4.setBounds(5, 110, 60, 20);

		/* La MAC va a ser un campo que el usuario no va a poder cambiar */
		getContentPane().add(txtMAC);
		txtMAC.setBounds(80, 110, 150, 20);
		txtMAC.setEnabled(false);
		
		JButton btn1 = new JButton("Aceptar");
		getContentPane().add(btn1);
		btn1.setBounds(80, 170, 90, 26);
		btn1.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				botonPulsado("Aceptar");
			}
		});

		JButton btn3 = new JButton("Cancelar");
		getContentPane().add(btn3);
		btn3.setBounds(180, 170, 90, 26);
		btn3.addMouseListener(new MouseAdapter()
		{
			public void mouseClicked(MouseEvent evt)
			{
				botonPulsado("Cancelar");
			}
		});

		setResizable(false);
		int ancho = 350;
		int alto = 240;
		setBounds(xCentral-ancho/2, yCentral-alto/2, ancho, alto);
		
		setVisible(true);
	}
	
	/** Evento sobre el JComboBox de los tipos de equipos.
	 * Al elegir un elemento de la lista se muestra al usuario una propuesta de nombre
	 * para esa interfaz
	 */
	public void actionPerformed(ActionEvent e)
	{
		int indice = ((JComboBox)e.getSource()).getSelectedIndex();
		String propuesta = "";
		
		if (indice!=0)
		{
			propuesta = nombresInterfaces.dameRaiz((String)listaTipos.elementAt(indice-1));

			int tam = 0;
			for (int i=0; i<listaNombres.size(); i++)
				if (((String)listaNombres.elementAt(i)).startsWith(propuesta))
					tam++;
			
			txtNombre.setText(propuesta+tam);
		}
	}
	
	/** Distintas acciones sobre los botones
	 * @param nombre Boton que se ha pulsado
	 */
	private void botonPulsado(String nombre)
	{
		/** Comprobaciones previas antes de cerrar el cuadro de propiedades de la interfaz */
		if (nombre.compareTo("Aceptar")==0)
		{
			boolean hayError = false;
		
			// Comprobamos nombre
			if (listaNombres.contains(txtNombre.getText()))
			{
				hayError = true;
				txtNombre.grabFocus();
				muestraAviso.mensaje(padreFrame, "El nombre ya existe");
				
				return;
			}
		
			// Comprobamos nombre
			if (!(txtNombre.getText()).matches("\\w*") || txtNombre.getText().length()==0)
			{
				muestraAviso.mensaje(padreFrame, "Nombre no valido");
				txtNombre.grabFocus();
			
				hayError = true;
			
				return;
			}
		
			// Comprobamos IP
			hayError = IPcorrecta();
		
			// Comprobamos mascara
			try
			{
				new DireccionIPv4(txtMascara.getText());
			}
			catch(Exception e)
			{
				hayError = true;
				txtMascara.setText("Mascara Incorrecta");
				txtMascara.grabFocus();
			}
			
			textoBoton = "Aceptar";
		
			if (!hayError)
				setVisible(false);
		}
		/* Se cancela la interfaz actual */
		else if (nombre.compareTo("Cancelar")==0)
		{
			textoBoton = "Cancelar";
			setVisible(false);
		}
		/* Pone la mascara por defecto en el campo */
		else if (nombre.compareTo("Mascara")==0)
		{
			/* Si la IP es correcta ponemos la mascara */
			if (!IPcorrecta())
			{
				int b0 = (new Integer(txtIP.getText().substring(0, txtIP.getText().indexOf(".")))).intValue();

			   txtMascara.setText("255.255.255.255");  

				if(b0>=1 && b0<=127)                            // clase A
					txtMascara.setText("255.0.0.0");        
				else if(b0>=128 && b0<=191)                     // clase B
				   txtMascara.setText("255.255.0.0");
				else if(b0>=192 && b0<=223)                     // clase C
		   		txtMascara.setText("255.255.255.0");
  			}
		}
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
	
	/** Devuelve el equipo al que conecta la interfaz */
	public String getConecta()
	{
		if (comboConexiones.getItemCount()==0)
			return "N/A";

		return (String)comboConexiones.getSelectedItem();
	}

	/** Devuelve la IP de la interfaz */
	public String getIP()
	{
		return txtIP.getText();
	}

	/** Devuelve la MAC de la interfaz */
	public String getMAC()
	{
		return txtMAC.getText();
	}

	/** Devuelve la Mascara de la interfaz */
	public String getMascara()
	{
		return txtMascara.getText();
	}

	/** Devuelve el nombre de la interfaz */
	public String getNombre()
	{
		return txtNombre.getText();
	}

	/** Crea un objeto de tipo DireccionIPv4 para comprobar que la IP es correcta */
	private boolean IPcorrecta()
	{
		boolean hayError = false;
		String iptexto = txtIP.getText();
		
		try
		{
			new DireccionIPv4(iptexto);
			
			/* Buscamos la IP entre las seleccionadas */
			
			// Si la interfaz no esta conectada solamente buscamos la IP independientemente de la red
			if (getConecta().compareTo("N/A")==0)
			{
				for (int i=0; i<listaIP.size() && !hayError; i++)
					if ( ((String)listaIP.elementAt(i)).indexOf(iptexto)!=-1)
						hayError = true;
			}
			// si no buscamos dentro de la red a la que esta conectado
			else
				hayError = listaIP.contains(iptexto + " - " + getConecta());

			if (hayError)
				muestraAviso.mensaje(padreFrame, "Direccion IP " + iptexto + " existente");
			
			/* Comprobamos que no sea una direccion reservada */
			if (!hayError)
			{
				String bytet = iptexto.substring(iptexto.lastIndexOf(".")+1, iptexto.length());
				
				if (bytet.compareTo("255")==0 || bytet.compareTo("0")==0)
				{
					muestraAviso.mensaje(padreFrame, "Esa direccion IP esta reservada.");
					hayError = true;
				}
			}
		}
		catch(Exception e)
		{
			txtMascara.setText("IP Incorrecta");
			txtIP.grabFocus();
			hayError = true;
		}
		
		return hayError;
	}
}
