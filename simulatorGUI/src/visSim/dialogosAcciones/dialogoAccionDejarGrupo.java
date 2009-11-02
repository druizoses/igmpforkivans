package visSim.dialogosAcciones;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import objetoVisual.listaObjetos;
import objetoVisual.accionVisual.accionDejarGrupoVisual;
import objetoVisual.accionVisual.accionVisual;
import visSim.listaInterfaces;


public class dialogoAccionDejarGrupo extends dialogoAccionBase {

	private JTextField txtInstante;
	private JComboBox equiposDisponibles;
	private JComboBox interfaces;
	private JTextField txtGrupo;
	
	public dialogoAccionDejarGrupo(Dialog parent, int xCentral, int yCentral, listaObjetos lista)
	{
		super(parent, xCentral, yCentral, lista);		
	}
	
	protected void init(listaObjetos lista){
		txtInstante = new JTextField(5);
		super.addField("Instante",txtInstante);

		equiposDisponibles = new JComboBox(lista.getNombresEquipos());
		equiposDisponibles.addItemListener(new LocalItemListener(lista));
		super.addField("Equipo",equiposDisponibles);

		interfaces = new JComboBox();
		setListaInterfaces(lista);
		super.addField("Interfaz",interfaces);

		txtGrupo = new JTextField(15);
		super.addField("Grupo",txtGrupo);
	}

	public void setListaInterfaces(listaObjetos lista){
		int iEquipo = lista.buscaEquipo(equiposDisponibles.getSelectedItem().toString());
		listaInterfaces tempInter = lista.getInterfaces(iEquipo);
		equiposDisponibles.setModel(new DefaultComboBoxModel(tempInter));
	}

	protected accionVisual crearAccionVisual(){
		accionDejarGrupoVisual accion = new accionDejarGrupoVisual();
		accion.setInstante(new Integer(txtInstante.getText()));
		accion.setEquipo(equiposDisponibles.getSelectedItem().toString());
		accion.setInterfaz(interfaces.getSelectedItem().toString());
		accion.setDireccionGrupo(txtGrupo.getText());
		return accion;
	}
	
	private class LocalItemListener implements ItemListener {
		
		private listaObjetos lista;
		
		public LocalItemListener(listaObjetos lista) {
			this.lista=lista;
		}
		
		public void itemStateChanged(ItemEvent e){
			setListaInterfaces(lista);
		}
	}

}
