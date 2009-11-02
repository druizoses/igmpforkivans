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


public class dialogoAccionEnviarPaqueteIP extends dialogoAccionBase {

	private JTextField txtInstante;
	private JComboBox equiposDisponibles;
	private JComboBox interfaces;
	private JTextField txtDireccionDestino;
	private JTextField txtTamanio;
	private JTextField txtCopias;
	private JCheckBox chkFragmentable;
	
	public dialogoAccionEnviarPaqueteIP(Dialog parent, int xCentral, int yCentral, listaObjetos lista)
	{
		super(parent, xCentral, yCentral, lista);		
	}
	
	protected void init(listaObjetos lista)
	{
		txtInstante = new JTextField(5);
		super.addField("Instante",txtInstante);

		equiposDisponibles = new JComboBox(lista.getNombresEquipos());
		equiposDisponibles.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				setListaInterfaces(lista);
			}
		});
		super.addField("Equipo",equiposDisponibles);

		interfaces = new JComboBox();
		setListaInterfaces(lista);
		super.addField("Interfaz",interfaces);

		txtDireccionDestino = new JTextField(15);
		super.addField("Direccion destino",txtDireccionDestino);

		txtTamanio = new JTextField(5);
		super.addField("Tamaño",txtTamanio);
		txtTamanio.setText("1000");

		txtCopias = new JTextField(5);
		super.addField("Tamaño",txtTamanio);
		txtCopias.setText("1");

		chkFragmentable = new JCheckBox();
		super.addField("Fragmentable",chkFragmentable);
	}

	public void setListaInterfaces(listaObjetos lista)
	{
		int iEquipo = lista.buscaEquipo(e.getItem());
		listaInterfaces tempInter = lista.getInterfaces(iEquipo);
		equiposDisponibles.setModel(new DefaultComboBoxModel(tempInter));
	}

	protected accionVisual crearAccionVisual()
	{
		accionEnviarPaqueteIPVisual accion = new accionEnviarPaqueteIPVisual();
		accion.setInstante(txtInstante.getText());
		accion.setEquipo(equiposDisponibles.getSelectedItem());
		accion.setInterfaz(interfaces.getSelectedItem());
		accion.setDireccionDestino(txtDireccionDestino.getText());
		accion.setTamanioPaquete(txtTamanio.getText());
		accion.setCopias(txtCopias.getText());
		accion.setFragmentable(chkFragmentable.isSelected());
		return accion;
	}

}
