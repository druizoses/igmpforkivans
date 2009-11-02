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

import objetoVisual.listaObjetos;
import objetoVisual.accionVisual.accionEncenderVisual;
import objetoVisual.accionVisual.accionVisual;


public class dialogoAccionEncender extends dialogoAccionBase {

	private JTextField txtInstante;
	private JComboBox equiposDisponibles;
	
	public dialogoAccionEncender(Dialog parent, int xCentral, int yCentral, listaObjetos lista)
	{
		super(parent, xCentral, yCentral, lista);		
	}
	
	protected void init(listaObjetos lista){
		txtInstante = new JTextField(5);
		super.addField("Instante",txtInstante);

		equiposDisponibles = new JComboBox(lista.getNombresEquipos());
		super.addField("Equipo",equiposDisponibles);
	}

	protected accionVisual crearAccionVisual(){
		accionEncenderVisual accion = new accionEncenderVisual();
		accion.setInstante(new Integer(txtInstante.getText()));
		accion.setEquipo(equiposDisponibles.getSelectedItem().toString());
		return accion;
	}

}
