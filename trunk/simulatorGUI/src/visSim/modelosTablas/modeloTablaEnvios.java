/** @author: tlfs & afzs */
package visSim.modelosTablas;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;


/** Clase creada como modelo para la tabla de envios*/
public class modeloTablaEnvios extends AbstractTableModel
{
	final String columnNames[] = {"Origen", "Destino", "Tamanyo", "Copias", " Fragmentable?"};
	final Object[][] data;

	public modeloTablaEnvios(Vector listaEnvios)
	{
		data = new Object[listaEnvios.size()/5][5];
		
		for (int i=0; i<listaEnvios.size(); i+=5)
		{
			data[i/5][0] = (String)listaEnvios.elementAt(i);
			data[i/5][1] = (String)listaEnvios.elementAt(i+1);
			data[i/5][2] = (String)listaEnvios.elementAt(i+2);
			data[i/5][3] = (String)listaEnvios.elementAt(i+3);
			data[i/5][4] = new Boolean(listaEnvios.elementAt(i+4).toString());
		}
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
		return true;
	}
	
	public void setValueAt(Object valor, int row, int col)
	{
		data[row][col] = valor;
		fireTableCellUpdated(row, col);
	}
}
