package mainFrame;
import NetworkProtocols.IGMP.router.RouterIGMP;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;


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
public class OptionsRouterFrame extends javax.swing.JFrame {

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private JLabel jLabel1;
	private JLabel jLabel2;
	private JButton jButton1;
	private JLabel jLabel4;
	private JLabel jLabel3;
	private JButton jButton3;
	private JButton jButton2;
	private JTextField jTextField2;
	private JTextField jTextField1;
	private RouterIGMP router;
	
	public OptionsRouterFrame(RouterIGMP router) {
		super();
		this.router = router;
		initGUI();
	}
	
	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout(
					"10dlu, max(p;5dlu), 66dlu, 17dlu, 48dlu, 42dlu", 
					"5dlu, max(p;5dlu), max(p;5dlu), max(p;5dlu), 5dlu, max(p;5dlu)");
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			getContentPane().setLayout(thisLayout);
			this.setTitle("Opciones del Router IGMP");
			this.setResizable(false);
			{
				jLabel1 = new JLabel();
				getContentPane().add(jLabel1, new CellConstraints("2, 2, 1, 1, default, default"));
				jLabel1.setText("Timer Query Specific");
			}
			{
				jLabel2 = new JLabel();
				getContentPane().add(jLabel2, new CellConstraints("2, 4, 1, 1, default, default"));
				jLabel2.setText("MRT Query Specific");
			}
			{
				jTextField1 = new JTextField();
				jTextField1.setText(""+router.Timer_Query_Specific);
				getContentPane().add(jTextField1, new CellConstraints("3, 2, 1, 1, default, default"));
				jTextField1.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent evt) {
						jTextField1KeyPressed(evt);
					}
				});
			}
			{
				jTextField2 = new JTextField();
				jTextField2.setText(""+(router.MRT_Query_Specific+128));
				getContentPane().add(jTextField2, new CellConstraints("3, 4, 1, 1, default, default"));
				jTextField2.addKeyListener(new KeyAdapter() {
					public void keyPressed(KeyEvent evt) {
						jTextField2KeyPressed(evt);
					}
				});
			}
			{
				jButton1 = new JButton();
				getContentPane().add(jButton1, new CellConstraints("5, 2, 1, 1, default, default"));
				jButton1.setText("Guardar");
				jButton1.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jButton1MouseClicked(evt);
					}
				});
			}
			{
				jButton2 = new JButton();
				getContentPane().add(jButton2, new CellConstraints("5, 4, 1, 1, default, default"));
				jButton2.setText("Guardar");
				jButton2.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jButton2MouseClicked(evt);
					}
				});
			}
			{
				jButton3 = new JButton();
				getContentPane().add(jButton3, new CellConstraints("6, 6, 1, 1, default, default"));
				jButton3.setText("Salir");
				jButton3.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jButton3MouseClicked(evt);
					}
				});
			}
			{
				jLabel3 = new JLabel();
				getContentPane().add(jLabel3, new CellConstraints("4, 2, 1, 1, default, default"));
				jLabel3.setText("ms");
			}
			{
				jLabel4 = new JLabel();
				getContentPane().add(jLabel4, new CellConstraints("4, 4, 1, 1, default, default"));
				jLabel4.setText("ds");
			}
			pack();
			this.setSize(389, 137);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jButton1MouseClicked(MouseEvent evt) {
		int value = Integer.parseInt(jTextField1.getText());
		router.Timer_Query_Specific = value;
		jButton1.setEnabled(false);
	}
	
	private void jButton2MouseClicked(MouseEvent evt) {
		int value = Integer.parseInt(jTextField2.getText());
		if ((value<0)||(value > 255)){
			MessageFrame frame = new MessageFrame(this, "error", "valor fuera de rango (0..255)");
			frame.setVisible(true);
		}else{
			router.MRT_Query_Specific = (byte)(value-128);
			jButton2.setEnabled(false);
		}
	}
	
	private void jTextField1KeyPressed(KeyEvent evt) {
		jButton1.setEnabled(true);
	}

	private void jTextField2KeyPressed(KeyEvent evt) {
		jButton2.setEnabled(true);
	}
	
	private void jButton3MouseClicked(MouseEvent evt) {
		this.setVisible(false);
	}
}
