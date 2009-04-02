package mainFrame;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.SwingUtilities;

import Exceptions.AddressException;
import Exceptions.NodeException;
import Interface.Interface;
import Interface.Interfaces;
import Link.Link;
import NetworkProtocols.NetworkProtocols;
import NetworkProtocols.IP.IP;
import NetworkProtocols.IP.Address.IpAddress;
import NetworkProtocols.IP.Address.Mask;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
public class CreateRouterFrame extends javax.swing.JFrame {

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
	private JLabel jError;
	private JButton jButton3;
	private JButton jButton2;
	private JButton jButton1;
	private JList jList1;
	private JTextField jTextField5;
	private JTextField jTextField4;
	private JTextField jTextField3;
	private JTextField jTextField2;
	private JTextField jTextField1;
	private JLabel jLabel2;
	private JLabel jLabel1;
	private Interfaces inters = new Interfaces();
	IP ip=null;
	IpAddress IPLocal = null;

	/**
	* Auto-generated main method to display this JFrame
	*/
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CreateRouterFrame inst = new CreateRouterFrame();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public CreateRouterFrame() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			this.setTitle("Creación de un router IGMP");
			this.setResizable(false);
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1);
				jPanel1.setBounds(7, 12, 510, 177);
				jPanel1.setBorder(BorderFactory.createTitledBorder("Interfaces"));
				jPanel1.setLayout(null);
				{
					jLabel1 = new JLabel();
					jPanel1.add(jLabel1);
					jLabel1.setText("Nombre de la Interface:");
					jLabel1.setBounds(17, 19, 152, 14);
				}
				{
					jLabel2 = new JLabel();
					jPanel1.add(jLabel2);
					jLabel2.setText("IP local:");
					jLabel2.setBounds(17, 45, 152, 14);
				}
				{
					jLabel3 = new JLabel();
					jPanel1.add(jLabel3);
					jLabel3.setText("Puerto de lectura:");
					jLabel3.setBounds(17, 71, 152, 14);
				}
				{
					jLabel4 = new JLabel();
					jPanel1.add(jLabel4);
					jLabel4.setText("IP destino:");
					jLabel4.setBounds(17, 97, 152, 14);
				}
				{
					jLabel5 = new JLabel();
					jPanel1.add(jLabel5);
					jLabel5.setText("Puerto de escritura:");
					jLabel5.setBounds(17, 123, 152, 14);
				}
				{
					jTextField1 = new JTextField();
					jPanel1.add(jTextField1);
					jTextField1.setText("eth0");
					jTextField1.setBounds(181, 12, 108, 21);
					jTextField1.setToolTipText("Nombre ficticio de la interface.\nPor cuestiones de comodidad para\nobservar el funcionamiento.");
				}
				{
					jTextField2 = new JTextField();
					jPanel1.add(jTextField2);
					jTextField2.setBounds(181, 42, 108, 21);
					jTextField2.setText("127.0.0.1");
					jTextField2.setEditable(false);
					jTextField2.setToolTipText("IP local ficticia. No utilizada.");
					jTextField2.setFocusable(false);
				}
				{
					jTextField3 = new JTextField();
					jPanel1.add(jTextField3);
					jTextField3.setText("6000");
					jTextField3.setBounds(181, 68, 52, 21);
				}
				{
					jTextField4 = new JTextField();
					jPanel1.add(jTextField4);
					jTextField4.setBounds(181, 94, 108, 21);
					jTextField4.setToolTipText("IP real del dispositivo remoto.");
					jTextField4.setText("localhost");
				}
				{
					jTextField5 = new JTextField();
					jPanel1.add(jTextField5);
					jTextField5.setText("6001");
					jTextField5.setBounds(181, 120, 52, 21);
				}
				{
					jButton1 = new JButton();
					jPanel1.add(jButton1);
					jButton1.setText("Agregar");
					jButton1.setBounds(181, 149, 115, 21);
					jButton1.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButton1MouseClicked(evt);
						}
					});
				}
				{
					ListModel jList1Model = 
						new DefaultComboBoxModel();
					jList1 = new JList();
					jPanel1.add(jList1);
					jList1.setModel(jList1Model);
					jList1.setBounds(333, 22, 165, 144);
					jList1.setBorder(BorderFactory.createTitledBorder(null, "Interfaces actuales:", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
					jList1.setBackground(new java.awt.Color(192,192,192));
					jList1.setFocusable(false);
				}
				{
					jError = new JLabel();
					jPanel1.add(jError);
					jError.setBounds(17, 151, 356, 10);
				}
			}
			{
				jButton2 = new JButton();
				getContentPane().add(jButton2);
				jButton2.setText("Crear Router");
				jButton2.setBounds(378, 195, 139, 21);
				jButton2.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jButton2MouseClicked(evt);
					}
				});
			}
			{
				jButton3 = new JButton();
				getContentPane().add(jButton3);
				jButton3.setText("Salir");
				jButton3.setBounds(12, 195, 86, 21);
				jButton3.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jButton3MouseClicked(evt);
					}
				});
			}
			pack();
			this.setSize(537, 255);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jButton1MouseClicked(MouseEvent evt) {
		boolean error = false;
		int puertoLectura=0, puertoEscritura=0;
		String ipDestino="";
		IpAddress ipLocal=null;
		Link link=null;
		try {
			ipLocal = new IpAddress(jTextField2.getText().replace('.', 'P'));
			if (IPLocal==null)
				IPLocal = ipLocal;
		} catch (AddressException e) {
			error = true;
			jError.setText("Error en la dirección de IP local..");
		}
		if(!error)
			try{
				puertoLectura = Integer.parseInt(jTextField3.getText());
			}catch(NumberFormatException e){
				error= true;
				jError.setText("Error en el puerto de lectura..");
			}
			ipDestino = jTextField4.getText();
			
		if(!error)
			try{
				puertoEscritura = Integer.parseInt(jTextField5.getText());
			}catch(NumberFormatException e){
				error= true;
				jError.setText("Error en el puerto de escritura..");
			}
		if(!error)
			try {
				link = new Link("localhost",""+puertoLectura,ipDestino,""+puertoEscritura);
			} catch (NodeException e1) {
				error = true;
				jError.setText("Error..");
			}
		if(!error){
			try {
				Interface inter = inters.addInterface(jTextField1.getText(),ipLocal,new Mask(24),1500);
				link.setInterface(inter);
				((DefaultComboBoxModel)jList1.getModel()).addElement(inter);
			} catch (NodeException e) {
				error = true;
				jError.setText("Error..");
			} catch (AddressException e) {
				error = true;
				jError.setText("Error..");
			}
		}
		if(!error)
			jError.setText("");
	}
	
	private void jButton3MouseClicked(MouseEvent evt) {
		this.dispose();
	}
	
	private void jButton2MouseClicked(MouseEvent evt) {
		try {
			ip = new IP(inters);
			ip.setIpLocal(IPLocal);
			NetworkProtocols.addProtocol(NetworkProtocols.PROTO_IP, ip);
			RouterFrame frame = new RouterFrame(inters, this);
			this.setVisible(false);
			frame.setVisible(true);
		} catch (NodeException e) {
			e.printStackTrace();
		}
	}

}
