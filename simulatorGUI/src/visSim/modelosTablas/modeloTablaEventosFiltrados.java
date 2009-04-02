/** @author: tlfs & afzs */
package visSim.modelosTablas;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/** Clase creada como modelo para la tabla de eventos filtrados para un equipo*/
public class modeloTablaEventosFiltrados extends AbstractTableModel
{
	final String columnNames[] = {"Instante", "Tipo", "Descripcion"};
	final Object[][] data;

	public modeloTablaEventosFiltrados(Vector listaEventos)
	{
		data = new Object[listaEventos.size()][3];
		String cadena;
		
		for (int i=0; i<listaEventos.size(); i++)
		{
			cadena = (String)listaEventos.elementAt(i);
			data[i][0] = cadena.substring(0, cadena.indexOf("\t"));
			
			cadena = cadena.substring(cadena.indexOf("\t")+1, cadena.length());
			data[i][1] = cadena.substring(0, cadena.indexOf("\t"));
			
			cadena = cadena.substring(cadena.indexOf("\t")+1, cadena.length());
			cadena = cadena.substring(cadena.indexOf("\t")+1, cadena.length());
			data[i][2] = cadena;
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

	/** Las celdas de la tabla no se van a poder modificar */
	public boolean isCellEditable(int row, int col)
	{
		return false;
	}
	
	public void setValueAt(Object valor, int row, int col)
	{
		data[row][col] = valor;
	}
}
