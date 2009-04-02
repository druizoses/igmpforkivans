package mainFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;

import Exceptions.AddressException;
import NetworkProtocols.IP.Address.IpAddress;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class CreateHostFrame extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel jPanel1;
	private JLabel jLabel3;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JTextField jTextField6;
	private JLabel jLabel6;
	private JLabel jError;
	private JButton jButton2;
	private JButton jButton1;
	private JTextField jTextField5;
	private JTextField jTextField4;
	private JTextField jTextField3;
	private JPanel jPanel2;
	private JTextField jTextField2;
	private JLabel jLabel2;
	private JTextField jTextField1;
	private JLabel jLabel1;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CreateHostFrame inst = new CreateHostFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public CreateHostFrame() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			this.setTitle("Creación de un nuevo Host");
			this.setResizable(false);
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1);
				jPanel1.setBounds(12, 40, 396, 81);
				jPanel1.setBorder(BorderFactory.createTitledBorder("Interface"));
				jPanel1.setLayout(null);
				{
					jLabel1 = new JLabel();
					jPanel1.add(jLabel1);
					jLabel1.setText("IP Local:");
					jLabel1.setBounds(17, 19, 107, 14);
				}
				{
					jTextField1 = new JTextField();
					jTextField1.setText("127.0.0.1");
					jPanel1.add(jTextField1);
					jTextField1.setBounds(142, 16, 120, 21);
					jTextField1.setEditable(false);
					jTextField1.setFocusable(false);
				}
				{
					jLabel2 = new JLabel();
					jPanel1.add(jLabel2);
					jLabel2.setText("Nombre Interface:");
					jLabel2.setBounds(17, 51, 113, 14);
				}
				{
					jTextField2 = new JTextField();
					jTextField2.setText("eth0");
					jPanel1.add(jTextField2);
					jTextField2.setBounds(142, 48, 120, 21);
				}
			}
			{
				jPanel2 = new JPanel();
				getContentPane().add(jPanel2);
				jPanel2.setBounds(12, 123, 396, 98);
				jPanel2.setLayout(null);
				jPanel2.setBorder(BorderFactory.createTitledBorder("Link"));
				{
					jLabel3 = new JLabel();
					jPanel2.add(jLabel3);
					jLabel3.setText("Puerto de lectura:");
					jLabel3.setBounds(11, 19, 113, 14);
				}
				{
					jLabel4 = new JLabel();
					jPanel2.add(jLabel4);
					jLabel4.setText("IP destino:");
					jLabel4.setBounds(11, 45, 65, 14);
				}
				{
					jLabel5 = new JLabel();
					jPanel2.add(jLabel5);
					jLabel5.setText("Puerto de escritura:");
					jLabel5.setBounds(11, 71, 135, 14);
				}
				{
					jTextField3 = new JTextField();
					jTextField3.setText("6001");
					jPanel2.add(jTextField3);
					jTextField3.setBounds(148, 16, 100, 21);
				}
				{
					jTextField4 = new JTextField();
					jTextField4.setText("localhost");
					jPanel2.add(jTextField4);
					jTextField4.setBounds(148, 42, 100, 21);
				}
				{
					jTextField5 = new JTextField();
					jTextField5.setText("6000");
					jPanel2.add(jTextField5);
					jTextField5.setBounds(146, 68, 102, 21);
				}
			}
			{
				jButton1 = new JButton();
				getContentPane().add(jButton1);
				jButton1.setText("Crear host");
				jButton1.setBounds(300, 227, 108, 21);
				jButton1.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jButton1MouseClicked(evt);
					}
				});
			}
			{
				jButton2 = new JButton();
				getContentPane().add(jButton2);
				jButton2.setText("Salir");
				jButton2.setBounds(24, 227, 75, 21);
				jButton2.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jButton2MouseClicked(evt);
					}
				});
			}
			{
				jError = new JLabel();
				getContentPane().add(jError);
				jError.setBounds(104, 227, 190, 12);
			}
			{
				jLabel6 = new JLabel();
				getContentPane().add(jLabel6);
				jLabel6.setText("Nombre:");
				jLabel6.setBounds(57, 12, 68, 14);
				jLabel6.setFont(new java.awt.Font("Tahoma",1,12));
			}
			{
				jTextField6 = new JTextField();
				getContentPane().add(jTextField6);
				jTextField6.setText("Host");
				jTextField6.setBounds(155, 9, 172, 21);
				jTextField6.setFont(new java.awt.Font("Tahoma",1,12));
			}
			pack();
			this.setSize(428, 287);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jButton1MouseClicked(MouseEvent evt) {
		boolean error=false;
		IpAddress ipLocal = null;
		String nombreInterface = jTextField2.getText();
		int puertoLectura=0, puertoEscritura=0;
		String ipDestino="";
		try {
			ipLocal = new IpAddress(jTextField1.getText().replace('.', 'P'));
		} catch (AddressException e) {
			jError.setText("Error en la IP Local..");
			error = true;
		}
		if (!error)
			try{
				puertoLectura = Integer.parseInt(jTextField3.getText());
			}catch (NumberFormatException e) {
				jError.setText("Error en el puerto de lectura..");
				error = true;
			}
		ipDestino = jTextField4.getText();
		/*
		if (!error){
			ipDestino = jTextField4.getText();
			if (ipDestino.equalsIgnoreCase("localhost") || ipDestino.split(".").length==4)
				;
			else{
				jError.setText("Error en la IP de destino..");
				error = true;
			}
		}
		*/
		if (!error)
			try{
				puertoEscritura = Integer.parseInt(jTextField5.getText());
			}catch (NumberFormatException e) {
				jError.setText("Error en el puerto de escritura..");
				error = true;
			}
		if (!error){			
			HostFrame host = new HostFrame(ipLocal,nombreInterface,puertoLectura,ipDestino, puertoEscritura);
			host.setVisible(true);
			host.setTitle(this.jTextField6.getText());
			this.dispose();
		}
	}
	
	private void jButton2MouseClicked(MouseEvent evt) {
		this.dispose();
	}

}
