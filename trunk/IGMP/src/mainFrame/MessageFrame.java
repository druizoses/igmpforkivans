package mainFrame;
import com.cloudgarden.layout.AnchorLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JTextField;
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
public class MessageFrame extends javax.swing.JDialog {
	private JTextField jTextField1;
	private JButton jButton1;

	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public MessageFrame(JFrame frame, String tittle, String text) {
		super(frame);
		initGUI();
		this.setTitle(tittle);
		this.jTextField1.setText(text);
	}
	
	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout(
					"max(p;5dlu), 53dlu, 86dlu, 104dlu", 
					"max(p;5dlu), 18dlu, 7dlu, 14dlu");
			getContentPane().setLayout(thisLayout);
			{
				jTextField1 = new JTextField();
				getContentPane().add(jTextField1, new CellConstraints("2, 2, 3, 1, default, default"));
				jTextField1.setEditable(false);
				jTextField1.setFocusable(false);
				jTextField1.setInheritsPopupMenu(true);
				jTextField1.setVerifyInputWhenFocusTarget(false);
			}
			{
				jButton1 = new JButton();
				getContentPane().add(jButton1, new CellConstraints("4, 4, 1, 1, default, default"));
				jButton1.setText("Aceptar");
				jButton1.addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent evt) {
						jButton1MouseClicked(evt);
					}
				});
			}
			this.setSize(400, 118);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void jButton1MouseClicked(MouseEvent evt) {
		this.setVisible(false);
	}

}
