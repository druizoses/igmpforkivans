package visSim.dialogosAcciones;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import objetoVisual.listaObjetos;
import objetoVisual.accionVisual.accionVisual;


public abstract class dialogoAccionBase extends JDialog {

	private String boton;
	private accionVisual accion;

	public static final String ACEPTAR = "aceptar";
	public static final String CANCELAR = "cancelar";
	
	public dialogoAccionBase(Dialog parent, int xCentral, int yCentral, listaObjetos lista)
	{
		super(parent, true);
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		init(lista);
		
		JPanel pnlBotones = new JPanel();
		pnlBotones.setLayout(new FlowLayout());
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				aceptar();
			}
		});
		pnlBotones.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnAceptar.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				cancelar();
			}
		});
		pnlBotones.add(btnCancelar);
				
		getContentPane().add(pnlBotones);
		
		setResizable(false);
		pack();
		doLayout();
		this.setLocation(xCentral-this.getSize().width/2, yCentral-this.getSize().height/2);
		
	}
	
	protected void addField(String label, JComponent componente)
	{
		JPanel pnl = new JPanel();
		pnl.setLayout(new FlowLayout(FlowLayout.LEFT,5,5));

		JLabel lbl = new JLabel(label);
		pnl.add(lbl);

		pnl.add(componente);
		
		getContentPane().add(pnl);
	}

	public void cancelar(){
		boton = dialogoAccionBase.CANCELAR;
		accion = null;
		setVisible(false);
	}
	
	public void aceptar(){
		boton = dialogoAccionBase.ACEPTAR;
		accion = crearAccionVisual();
		setVisible(false);
	}

	public String getResultado(){
		return boton;
	}

	public accionVisual getAccionVisual(){
		return accion;
	}

	protected abstract void init(listaObjetos lista);

	protected abstract accionVisual crearAccionVisual();
	
}
