package visSim.dialogosAcciones;

import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import objetoVisual.listaObjetos;
import objetoVisual.accionVisual.accionEnviarPaqueteIPVisual;
import objetoVisual.accionVisual.accionVisual;
import visSim.listaInterfaces;


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
		this.setTitle("Enviar paquete IP");
	}
	
	protected void init(listaObjetos lista)
	{
		txtInstante = new JTextField(5);
		txtInstante.setText("0");
		txtInstante.setCaretPosition(0);
		super.addField("Instante",txtInstante);

		equiposDisponibles = new JComboBox(lista.getNombresEquipos());
		equiposDisponibles.addItemListener(new LocalItemListener(lista));
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
		super.addField("Copias",txtCopias);
		txtCopias.setCaretPosition(0);
		txtCopias.setText("1");

		chkFragmentable = new JCheckBox();
		super.addField("Fragmentable",chkFragmentable);
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

	protected accionVisual crearAccionVisual()
	{
		accionEnviarPaqueteIPVisual accion = new accionEnviarPaqueteIPVisual();
		accion.setInstante(new Integer(txtInstante.getText()));
		accion.setEquipo(equiposDisponibles.getSelectedItem().toString());
		accion.setInterfaz(interfaces.getSelectedItem().toString());
		accion.setDireccionDestino(txtDireccionDestino.getText());
		accion.setTamanioPaquete(new Integer(txtTamanio.getText()));
		accion.setCopias(new Integer(txtCopias.getText()));
		accion.setFragmentable(chkFragmentable.isSelected());
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
	}

	public void setEquipo(String equipo) {
		this.equiposDisponibles.setSelectedItem(equipo);
	}
	
	public void setCopias(int copias) {
		this.txtCopias.setText(String.valueOf(copias));
	}
	
	public void setFragmentable(boolean fragmentable) {
		this.chkFragmentable.setSelected(fragmentable);
	}
	
	public void setTamanio(int fragmentable) {
		this.txtTamanio.setText(String.valueOf(fragmentable));
	}
	
	public void setDireccionDestino(String direccionDestino) {
		this.txtDireccionDestino.setText(String.valueOf(direccionDestino));		
	}

	public void setInterfaz(String interfaz) {
		this.interfaces.setSelectedItem(interfaz);		
	}
}
