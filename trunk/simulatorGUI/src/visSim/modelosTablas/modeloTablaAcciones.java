/** @author: tlfs & afzs */
package visSim.modelosTablas;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import objetoVisual.accionVisual.accionVisual;


/** Clase creada como modelo para la tabla de envios*/
public class modeloTablaAcciones extends AbstractTableModel
{
	final String columnNames[] = {"Instante","Accion", "Descripcion"};
	final Object[][] data;

	public modeloTablaAcciones(Vector listaAcciones)
	{
		if (listaAcciones != null) {
			data = new Object[listaAcciones.size()][3];
			
			for (int i=0; i<listaAcciones.size(); i+=1)
			{
				accionVisual accion = (accionVisual)listaAcciones.elementAt(i);
				data[i][0] = accion.getInstante();
				data[i][1] = accion.getTipo();
				data[i][2] = accion.getDescripcion();
			}
		} else
			data = new Object[0][3];
	}

	public Class getColumnClass(int c)
	{
		return getValueAt(0, c).getClass();
	}

	public int getColumnCount()
	{
		return columnNames.length;
	}
	
	public String getColumnName(int col)
	{
		return columnNames[col];
	}
	
	public int getRowCount()
	{
		return data.length;
	}
	
	public Object getValueAt(int row, int col)
	{
		return data[row][col];
	}

	/** Las celdas de la tabla se pueden modificar */
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
	
	public void setValueAt(Object valor, int row, int col)
	{
		data[row][col] = valor;
		fireTableCellUpdated(row, col);
	}
}
