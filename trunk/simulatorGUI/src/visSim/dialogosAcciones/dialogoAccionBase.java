package visSim.dialogosAcciones;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
	
	private JPanel fields;
	private GridBagConstraints constraints;
	
	public dialogoAccionBase(Dialog parent, int xCentral, int yCentral, listaObjetos lista)
	{
		super(parent, true);
		
		fields = new JPanel();
		fields.setLayout(new GridBagLayout());
		constraints = new GridBagConstraints();
	    constraints.insets = new Insets(3, 3, 3, 3);
        constraints.gridx = 0;
        constraints.gridy = 0;

		init(lista);
		
		getContentPane().setLayout(new BorderLayout());
		
		getContentPane().add(fields, BorderLayout.CENTER);
		
		JPanel pnlBotones = new JPanel();
		pnlBotones.setLayout(new FlowLayout());
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				resultadoAceptar();
			}
		});
		pnlBotones.add(btnAceptar);
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				resultadoCancelar();
			}
		});
		pnlBotones.add(btnCancelar);
				
		getContentPane().add(pnlBotones, BorderLayout.SOUTH);
		
		setResizable(false);
		pack();
		doLayout();
		this.setLocation(xCentral-this.getSize().width/2, yCentral-this.getSize().height/2);
	}

	protected void addField(String label, JComponent componente)
	{
		constraints.gridx = 0;
        constraints.weightx = 0;
        constraints.weighty = 0;
        JLabel jlabel = new JLabel(label);
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.LINE_END;
        fields.add(jlabel, constraints);
        constraints.gridx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        fields.add(componente, constraints);
        constraints.gridy++;
	}

	public void resultadoCancelar(){
		boton = dialogoAccionBase.CANCELAR;
		accion = null;
		setVisible(false);
	}
	
	public void resultadoAceptar(){
		if (validarCampos()) {
			boton = dialogoAccionBase.ACEPTAR;
			accion = crearAccionVisual();
			setVisible(false);
		}
	}

	public String getResultado(){
		return boton;
	}

	public accionVisual getAccionVisual(){
		return accion;
	}

	protected abstract void init(listaObjetos lista);

	protected abstract accionVisual crearAccionVisual();
	
	protected abstract boolean validarCampos();
	
}
