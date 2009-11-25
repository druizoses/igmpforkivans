package visSim.dialogosAcciones;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.NumberFormat;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import objetoVisual.listaObjetos;
import objetoVisual.accionVisual.accionEncenderVisual;
import objetoVisual.accionVisual.accionVisual;
import util.utilTexto;


public class dialogoAccionEncender extends dialogoAccionBase {

	private JTextField txtInstante;
	private JLabel lblInstante;
	private JComboBox equiposDisponibles;
	
	public dialogoAccionEncender(Dialog parent, int xCentral, int yCentral, listaObjetos lista)
	{
		super(parent, xCentral, yCentral, lista);
		this.setTitle("Encender Equipo");
	}
	
	protected void init(listaObjetos lista){
		txtInstante = new JTextField(5);
		txtInstante.setText("0");
		txtInstante.setCaretPosition(0);
		super.addField("Instante",txtInstante);
		lblInstante = new JLabel("(" + utilTexto.convertToTime(new Integer(txtInstante.getText())) + ")");
		txtInstante.addFocusListener(new FocusAdapter(){
			@Override
			public void focusLost(FocusEvent e) {
				lblInstante.setText("(" + utilTexto.convertToTime(new Integer(txtInstante.getText())) + ")");
			}});
		super.addField("",lblInstante);

		equiposDisponibles = new JComboBox(lista.getNombresEquipos());
		super.addField("Equipo",equiposDisponibles);
	}

	protected accionVisual crearAccionVisual(){
		accionEncenderVisual accion = new accionEncenderVisual();
		accion.setInstante(Integer.parseInt(txtInstante.getText()));
		accion.setEquipo(equiposDisponibles.getSelectedItem().toString());
		return accion;
	}
	
	protected boolean validarCampos(){
		try {
			Integer.valueOf(txtInstante.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "El instante debe ser un numero entero", "Error en la validacion", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	public void setInstante(int instante) {
		this.txtInstante.setText(String.valueOf(instante));
		lblInstante.setText("(" + utilTexto.convertToTime(new Integer(txtInstante.getText())) + ")");
	}

	public void setEquipo(String equipo) {
		this.equiposDisponibles.setSelectedItem(equipo);
	}

}
