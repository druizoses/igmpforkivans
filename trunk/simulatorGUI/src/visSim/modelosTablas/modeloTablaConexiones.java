/** @author: tlfs & afzs */
package visSim.modelosTablas;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import visSim.interfazVisual;

/** Clase creada como modelo para la tabla de conexiones de los equipos
 * @see propiedades.java
 */
public class modeloTablaConexiones extends AbstractTableModel
{
	final String columnNames[] = {"Destino","Interfaz"};
	final Object[][] data;

	public modeloTablaConexiones(Vector conexiones, Vector interfaces)
	{
		data = new Object[conexiones.size()][2];
		
		for (int i=0; i<conexiones.size(); i++){
			data[i][0] = (String)conexiones.elementAt(i);
			// Conforme vamos anyadiendo a la tabla conexiones, realizamos una busqueda en el vector de
			// interfaces para conocer el que pertenece a la conexion y anyadirlo tambien a la tabla.
			for(int j = 0; j < interfaces.size(); j++){
				interfazVisual iv = (interfazVisual) interfaces.elementAt(j);
				if(((String)conexiones.elementAt(i)).compareTo((String)iv.getconecta()) == 0){
					data[i][1] = (String) iv.getNombre();
					j = interfaces.size();
				}
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
