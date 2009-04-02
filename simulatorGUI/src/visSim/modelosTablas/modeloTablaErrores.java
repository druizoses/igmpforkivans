/** @author: tlfs & afzs */
package visSim.modelosTablas;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/** Clase creada como modelo para los errores simulables en cada equipo*/
public class modeloTablaErrores extends AbstractTableModel
{
	final String columnNames[] = {"Error", "Descripcion", "Simular"};
	final Object[][] data;

	public modeloTablaErrores(Vector listaErrores)
	{
		data = new Object[listaErrores.size()/3][3];
		
		for (int i=0; i<listaErrores.size(); i+=3)
		{
			data[i/3][0] = (String)listaErrores.elementAt(i);
			data[i/3][1] = (String)listaErrores.elementAt(i+1);
			data[i/3][2] = new Boolean((String)listaErrores.elementAt(i+2));
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
	
	/** Solo se permiten ediciones en la ultima columna */
	public boolean isCellEditable(int row, int col)
	{
		if (col<2)
			return false;

		return true;
	}

	public void setValueAt(Object value, int row, int col)
	{
		data[row][col] = value;
		fireTableCellUpdated(row, col);
	}

}
