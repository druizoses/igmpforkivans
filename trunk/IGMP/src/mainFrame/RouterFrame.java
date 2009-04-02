package mainFrame;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;

import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Interface.Interface;
import Interface.Interfaces;
import NetworkProtocols.NetworkProtocols;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import NetworkProtocols.IGMP.router.RouterIGMP;
import NetworkProtocols.IGMP.util.Mostrable;
import javax.swing.JTextArea;
import javax.swing.ListModel;
import javax.swing.SwingConstants;

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
public class RouterFrame extends javax.swing.JFrame implements Mostrable{

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
	private JTextArea jTextArea1;
	RouterIGMP router;
	Interface[] inter;
	private JMenu jMenu2;
	private JList jList3;
	private JList jList2;
	private JList jList1;
	private JMenu jMenu1;
	private JMenuBar jMenuBar1;
	private JScrollPane jScrollPane1;
	CreateRouterFrame createRouterFrame;

	public RouterFrame(Interfaces inters, CreateRouterFrame createRouterFrame) {
		super();
		this.createRouterFrame = createRouterFrame;
		router = new RouterIGMP();
		Set<String> keys = inters.ifaces.keySet();
		inter = new Interface[keys.size()];
		int i=0;
		for (String string : keys) {
			Interface aux = inters.ifaces.get(string);
			router.addInterface(aux);
			inter[i] = aux;
			i++;
		}				
		NetworkProtocols.addProtocol(NetworkProtocols.PROTO_IGMP, router);
		initGUI();
		router.setFrameSalida(this);
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(null);
			this.setTitle("Router IGMP");
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu1 = new JMenu();
					jMenuBar1.add(jMenu1);
					jMenu1.setText("Opciones");
					jMenu1.setHorizontalAlignment(SwingConstants.RIGHT);
					jMenu1.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jMenu1MouseClicked(evt);
						}
					});
				}
				{
					jMenu2 = new JMenu();
					jMenuBar1.add(jMenu2);
					jMenu2.setText("Salir");
					jMenu2.addMouseListener(new MouseAdapter() {
						public void mouseClicked(MouseEvent evt) {
							jButton1MouseClicked(evt);
						}
					});
				}
			}
			{
				ListModel jList1Model = 
					new DefaultComboBoxModel();
				jList1 = new JList();
				getContentPane().add(jList1);
				jList1.setModel(jList1Model);
				jList1.setBounds(12, 7, 136, 110);
				jList1.setBorder(BorderFactory.createTitledBorder(null, "Grupos", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
			}
			{
				jScrollPane1 = new JScrollPane();
				getContentPane().add(jScrollPane1);
				jScrollPane1.setBounds(12, 120, 458, 160);
				jScrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Log", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
				{
					jTextArea1 = new JTextArea();
					jScrollPane1.setViewportView(jTextArea1);
					jTextArea1.setBounds(37, 181, 512, 136);
				}
			}
			{
				ListModel jList3Model = 
					new DefaultComboBoxModel();
				jList3 = new JList();
				getContentPane().add(jList3);
				jList3.setModel(jList3Model);
				jList3.setBounds(325, 7, 145, 113);
				jList3.setBorder(BorderFactory.createTitledBorder("Grupos por interface"));
			}
			{
				jList2 = new JList();
				getContentPane().add(jList2);
				jList2.setModel(new DefaultComboBoxModel(inter));
				jList2.setBounds(220, 7, 99, 113);
				jList2.setBorder(BorderFactory.createTitledBorder(null, "Interfaces", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));
				jList2.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent evt) {
						jList2ValueChanged(evt);
					}
				});
			}
			pack();
			this.setSize(490, 364);
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
		jList1.setModel(new DefaultComboBoxModel(router.getGroups()));
		int aux = jList2.getSelectedIndex();
		if (aux ==-1)
			aux=0;
		jList3.setModel(new DefaultComboBoxModel(router.getGroupsByInterface(inter[aux])));
	}
	
	private void jButton1MouseClicked(MouseEvent evt) {
		router.Terminar();
		createRouterFrame.dispose();
		this.dispose();
	}
	
	private void jList2ValueChanged(ListSelectionEvent evt) {
		int index = jList2.getSelectedIndex();
		if (index != -1){
			jList3.setModel(new DefaultComboBoxModel(router.getGroupsByInterface(inter[index])));
		}
	}
	
	private void jMenu1MouseClicked(MouseEvent evt) {
		OptionsRouterFrame frame = new OptionsRouterFrame(router);
		frame.setVisible(true);
	}
}
