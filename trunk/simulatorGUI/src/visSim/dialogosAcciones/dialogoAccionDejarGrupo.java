package visSim.dialogosAcciones;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import objetoVisual.listaObjetos;
import objetoVisual.accionVisual.accionDejarGrupoVisual;
import objetoVisual.accionVisual.accionVisual;
import util.utilTexto;
import visSim.listaInterfaces;


public class dialogoAccionDejarGrupo extends dialogoAccionBase {

	private JTextField txtInstante;
	private JLabel lblInstante;
	private JComboBox equiposDisponibles;
	private JComboBox interfaces;
	private JTextField txtGrupo;
	
	public dialogoAccionDejarGrupo(Dialog parent, int xCentral, int yCentral, listaObjetos lista)
	{
		super(parent, xCentral, yCentral, lista);
		this.setTitle("Dejar Grupo");
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
		equiposDisponibles.addItemListener(new LocalItemListener(lista));
		super.addField("Equipo",equiposDisponibles);

		interfaces = new JComboBox();
		setListaInterfaces(lista);
		super.addField("Interfaz",interfaces);

		txtGrupo = new JTextField(15);
		super.addField("Grupo",txtGrupo);
	}

	public void setListaInterfaces(listaObjetos lista)
	{
		int iEquipo = lista.buscaEquipo(equiposDisponibles.getSelectedItem().toString());
		listaInterfaces tempInter = lista.getInterfaces(iEquipo);
		Vector nombresInterfaces = new Vector();
		for (int j=0; j<tempInter.tam(); j++)
			nombresInterfaces.add(tempInter.getInterfaz(j).getNombre());
		interfaces.setModel(new DefaultComboBoxModel(nombresInterfaces));
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
	
	public void setInterfaz(String interfaz) {
		this.interfaces.setSelectedItem(interfaz);		
	}

	public void setGrupo(String direccionGrupo) {
		this.txtGrupo.setText(String.valueOf(direccionGrupo));		
	}
}
