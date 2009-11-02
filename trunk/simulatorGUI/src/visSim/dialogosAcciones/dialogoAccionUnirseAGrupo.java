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


public class dialogoAccionUnirseAGrupo extends dialogoAccionBase {

	private JTextField txtInstante;
	private JComboBox equiposDisponibles;
	private JComboBox interfaces;
	private JTextField txtGrupo;
	
	public dialogoAccionUnirseAGrupo(Dialog parent, int xCentral, int yCentral, listaObjetos lista)
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

		txtGrupo = new JTextField(15);
		super.addField("Grupo",txtGrupo);
	}

	public void setListaInterfaces(listaObjetos lista)
	{
		int iEquipo = lista.buscaEquipo(e.getItem());
		listaInterfaces tempInter = lista.getInterfaces(iEquipo);
		equiposDisponibles.setModel(new DefaultComboBoxModel(tempInter));
	}

	protected accionVisual crearAccionVisual()
	{
		accionUnirseAGrupoVisual accion = new accionUnirseAGrupoVisual();
		accion.setInstante(txtInstante.getText());
		accion.setEquipo(equiposDisponibles.getSelectedItem());
		accion.setInterfaz(interfaces.getSelectedItem());
		accion.setDireccionGrupo(txtGrupo.getText());
		return accion;
	}

}
