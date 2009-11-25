package visSim.dialogosAcciones;

import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import objetoVisual.listaObjetos;
import objetoVisual.accionVisual.accionApagarVisual;
import objetoVisual.accionVisual.accionVisual;
import util.utilTexto;


public class dialogoAccionApagar extends dialogoAccionBase {

	private JTextField txtInstante;
	private JLabel lblInstante;
	private JComboBox equiposDisponibles;
	
	public dialogoAccionApagar(Dialog parent, int xCentral, int yCentral, listaObjetos lista)
	{
		super(parent, xCentral, yCentral, lista);
		this.setTitle("Apagar Equipo");
	}
	
	protected void init(listaObjetos lista){
		txtInstante = new JTextField(5);
		super.addField("Instante",txtInstante);
		txtInstante.setText("0");
		txtInstante.setCaretPosition(0);
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
		accionApagarVisual accion = new accionApagarVisual();
		accion.setInstante(new Integer(txtInstante.getText()));
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
