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
import objetoVisual.accionVisual.accionApagarVisual;
import objetoVisual.accionVisual.accionVisual;


public class dialogoAccionApagar extends dialogoAccionBase {

	private JTextField txtInstante;
	private JComboBox equiposDisponibles;
	
	public dialogoAccionApagar(Dialog parent, int xCentral, int yCentral, listaObjetos lista)
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
		accionApagarVisual accion = new accionApagarVisual();
		accion.setInstante(new Integer(txtInstante.getText()));
		accion.setEquipo(equiposDisponibles.getSelectedItem().toString());
		return accion;
	}

}
