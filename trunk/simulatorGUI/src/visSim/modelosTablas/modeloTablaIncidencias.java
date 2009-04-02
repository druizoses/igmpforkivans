/** @author: tlfs & afzs */
package visSim.modelosTablas;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

/** Clase creada como modelo para la tabla de propiedades */
public class modeloTablaIncidencias extends AbstractTableModel
{
	final String[] columnNames;
	final Object[][] data;

	public modeloTablaIncidencias(Vector incidencias)
	{
		columnNames = new String[2];
		columnNames[0] = "Equipo";
		columnNames[1] = "Incidencia";
		String cadena;

		if (incidencias.size()==0)
		{
			data = new Object[1][2];
			data[0][1] = "No existen incidencias";
		}
		else
		{
			data = new Object[incidencias.size()][2];
			 for (int i=0; i<incidencias.size(); i++)
			{
				cadena = (String)incidencias.elementAt(i);
				data[i][0] = cadena.substring(0, cadena.indexOf(" -"));
				data[i][1] = cadena.substring(cadena.indexOf("- ")+1, cadena.length());
			}
		}
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
}