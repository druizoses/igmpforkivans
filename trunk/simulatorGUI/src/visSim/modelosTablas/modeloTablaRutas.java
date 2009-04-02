/** @author: tlfs & afzs */
package visSim.modelosTablas;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import visSim.rutaVisual;

/** Clase creada como modelo para la tabla de rutas*/
public class modeloTablaRutas extends AbstractTableModel
{
	final String columnNames[] = {"Destino", "Mascara", "Gateway", "Interfaz"};
	final Object[][] data;

	public modeloTablaRutas(Vector rutas)
	{
		data = new Object[rutas.size()][4];
		
		for (int i=0; i<rutas.size(); i++)
		{
			data[i][0] = ((rutaVisual)rutas.elementAt(i)).getDestino();
			data[i][1] = ((rutaVisual)rutas.elementAt(i)).getMascara();
			data[i][2] = ((rutaVisual)rutas.elementAt(i)).getGateway();
			data[i][3] = ((rutaVisual)rutas.elementAt(i)).getNombreInterfaz();
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
