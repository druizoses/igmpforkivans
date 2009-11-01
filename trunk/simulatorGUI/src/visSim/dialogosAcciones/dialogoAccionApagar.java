package visSim.dialogosAcciones;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class dialogoAccionApagar extends JDialog {

	private JTextField txtInstante;
	private JComboBox equiposDisponibles;
	
	public dialogoAccionApagar(Dialog parent, int xCentral, int yCentral, Vector nombresOrdenadores)
	{
		super(parent, true);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		JPanel pnlInstante = new JPanel();
		pnlInstante.setLayout(new FlowLayout());
		
		JLabel lblInstante = new JLabel("Instante");
		pnlInstante.add(lblInstante);
		
		txtInstante = new JTextField(5);
		pnlInstante.add(txtInstante);
		
		getContentPane().add(pnlInstante);
		
		
		JPanel pnlEquipo = new JPanel();
		pnlEquipo.setLayout(new FlowLayout());
		
		JLabel lblEquipo = new JLabel("Equipo");
		pnlEquipo.add(lblEquipo);
		
		equiposDisponibles = new JComboBox(nombresOrdenadores);
		pnlEquipo.add(equiposDisponibles);
		
		getContentPane().add(pnlEquipo);

		
		JPanel pnlBotones = new JPanel();
		pnlEquipo.setLayout(new FlowLayout());
		
		JButton btnAceptar = new JButton("Aceptar");
		pnlBotones.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		pnlBotones.add(btnCancelar);
				
		getContentPane().add(pnlBotones);
		
		setResizable(false);
		pack();
		doLayout();
		this.setLocation(xCentral-this.getSize().width/2, yCentral-this.getSize().height/2);
		
	}
	
}
