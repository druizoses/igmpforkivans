package mainFrame;

import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import Exceptions.AddressException;
import Exceptions.NodeException;
import Interface.Interface;
import Interface.Interfaces;
import Link.Link;
import NetworkProtocols.NetworkProtocols;
import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import NetworkProtocols.IGMP.host.HostIGMP;
import NetworkProtocols.IGMP.util.Mostrable;
import NetworkProtocols.IP.IP;
import NetworkProtocols.IP.Address.IpAddress;
import NetworkProtocols.IP.Address.Mask;


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
public class HostFrame extends javax.swing.JFrame implements Mostrable{

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList jList1;
	private JButton jButton1;
	private JTextArea jTextArea1;
	private JButton jButton2;
	private JLabel jLabel3;
	private JComboBox jComboBox1;
	private JPanel jPanel2;
	private JTextField jTextField1;
	private JLabel jLabel2;
	private JPanel jPanel1;
	private HostIGMP host;
	Interface inter1 = null;
	Link link1 = null;
	private JButton jButton3;
	private JScrollPane jScrollPane1;
	private JLabel jLabel4;

	/**
	* Auto-generated main method to display this JFrame
	*/
	
	public HostFrame(IpAddress ipLocal, String nombreInterface, int puertoLectura, String ipDestino, int puertoEscritura) {
		super();
		initGUI();
		Interfaces inter = new Interfaces();		
		try {
			link1 = new Link("localhost",""+puertoLectura,ipDestino,""+puertoEscritura);
		} catch (NodeException e) {
			e.printStackTrace();
		}
		try {
			inter1 = inter.addInterface(nombreInterface,ipLocal, new Mask(24),1500);
			link1.setInterface(inter1);
		} catch (NodeException e) {
			e.printStackTrace();
		} catch (AddressException e) {
			e.printStackTrace();
		}
		
		try {
			IP ip = new IP(inter);
			ip.setIpLocal(ipLocal);
			NetworkProtocols.addProtocol(NetworkProtocols.PROTO_IP, ip);
			host = new HostIGMP();
			host.setFrameSalida(this);
			NetworkProtocols.addProtocol(NetworkProtocols.PROTO_IGMP, host);
		} catch (NodeException e) {
			e.printStackTrace();
		}
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			this.setTitle("Host");
			{
				ListModel jList1Model = 
					new DefaultComboBoxModel();
				jList1 = new JList();
				getContentPane().add(jList1);
				jList1.setModel(jList1Model);
				jList1.setBounds(12, 27, 146, 123);
				jList1.setBorder(BorderFactory.createTitledBorder(null, "Grupos:", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			}
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1);
				jPanel1.setBounds(186, 21, 351, 64);
				jPanel1.setBorder(BorderFactory.createTitledBorder("Agregar Grupo"));
				jPanel1.setLayout(null);
				{
					jLabel2 = new JLabel();
					jPanel1.add(jLabel2);
					jLabel2.setText("Dirección del grupo:");
					jLabel2.setBounds(12, 19, 119, 14);
				}
				{
					jTextField1 = new JTextField();
					jPanel1.add(jTextField1);
					jTextField1.setBounds(136, 16, 95, 21);
				}
				{
					jButton1 = new JButton();
					jPanel1.add(jButton1);
					jButton1.setText("Agregar");
					jButton1.setBounds(243, 16, 87, 21);
					jButton1.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButton1KeyPressed(null);
						}
					});
					jButton1.addKeyListener(new KeyAdapter() {
						public void keyPressed(KeyEvent evt) {
							jButton1KeyPressed(evt);
						}
					});
				}
				{
					jLabel4 = new JLabel();
					jPanel1.add(jLabel4);
					jLabel4.setBounds(12, 39, 322, 15);
				}
			}
			{
				jPanel2 = new JPanel();
				getContentPane().add(jPanel2);
				jPanel2.setBounds(186, 97, 351, 53);
				jPanel2.setBorder(BorderFactory.createTitledBorder("Liberar Grupo"));
				jPanel2.setLayout(null);
				{
					ComboBoxModel jComboBox1Model = 
						new DefaultComboBoxModel();
					jComboBox1 = new JComboBox();
					jPanel2.add(jComboBox1);
					jComboBox1.setModel(jComboBox1Model);
					jComboBox1.setBounds(138, 24, 93, 21);
				}
				{
					jLabel3 = new JLabel();
					jPanel2.add(jLabel3);
					jLabel3.setText("Dirección del grupo:");
					jLabel3.setBounds(17, 27, 121, 14);
				}
				{
					jButton2 = new JButton();
					jPanel2.add(jButton2);
					jButton2.setText("Liberar");
					jButton2.setBounds(243, 24, 84, 21);
					jButton2.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButton2MouseClicked(evt);
						}
					});
				}
			}
			{
				jScrollPane1 = new JScrollPane();
				getContentPane().add(jScrollPane1);
				jScrollPane1.setBounds(12, 156, 525, 148);
				//jScrollPane1.setBorder(BorderFactory.createTitledBorder("Log"));
				jScrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Log", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
				{
					jTextArea1 = new JTextArea();
					jScrollPane1.setViewportView(jTextArea1);
					jTextArea1.setBounds(12, 164, 525, 116);
					jTextArea1.setPreferredSize(new java.awt.Dimension(498, 121));
				}
			}
			{
				jButton3 = new JButton();
				getContentPane().add(jButton3);
				jButton3.setText("Salir");
				jButton3.setBounds(461, 305, 76, 21);
				jButton3.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jButton3MouseClicked(evt);
					}
				});
			}
			pack();
			this.setSize(568, 360);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void agregarLog(String log) {
		Date date = Calendar.getInstance().getTime();
		String aux = date.getHours()+":"+date.getMinutes()+"."+date.getSeconds();
		jTextArea1.setText(aux+"-> "+log+"\n"+jTextArea1.getText());
	}

	public void cambiosEnGrupos() {
		String[] aux = host.getGroups();
		jList1.setModel(new DefaultComboBoxModel(aux));
		jComboBox1.setModel(new DefaultComboBoxModel(host.getOnlyGroups()));
	}
	
	private void jButton1KeyPressed(KeyEvent evt) {
		IpAddress group=null;
		try {
			group = new IpAddress(jTextField1.getText().replace('.', 'P'));
			host.addGroup(group, inter1);
			jLabel4.setText("");
		} catch (AddressException e) {
			jLabel4.setText("Error en la dirección del grupo");
		}
	}
	
	private void jButton2MouseClicked(MouseEvent evt) {
		IpAddress group=null;
		try {
			String aux = jComboBox1.getModel().getSelectedItem().toString();
			aux = aux.replace('.', 'P');
			group = new IpAddress(aux);
			host.leaveGroup(group, inter1);
		} catch (AddressException e) {
		}
	}
	
	private void jButton3MouseClicked(MouseEvent evt) {
		host.Terminar();
		link1.Terminar();
		this.dispose();
	}

}
