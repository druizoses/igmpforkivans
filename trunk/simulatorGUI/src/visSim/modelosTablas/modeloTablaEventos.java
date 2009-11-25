/** @author: tlfs & afzs */
package visSim.modelosTablas;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import util.utilTexto;

/** Clase creada como modelo para la tabla de eventos de la simulacion*/
public class modeloTablaEventos extends AbstractTableModel
{
	final String columnNames[] = {"Instante", "Tiempo", "Tipo", "Equipo", "Descripcion", "Interfaz"};
	final Object[][] data;

	public modeloTablaEventos(Vector listaEventos)
	{
		data = new Object[listaEventos.size()][6];
		String cadena;
		
		for (int i=0; i<listaEventos.size(); i++)
		{
			cadena = (String)listaEventos.elementAt(i);
			String sAux = cadena.substring(0, cadena.indexOf("\t"));
			int instante = Integer.valueOf(sAux);
			data[i][0] = sAux;
			data[i][1] = utilTexto.convertToTime(instante);
			
			cadena = cadena.substring(cadena.indexOf("\t")+1, cadena.length());
			data[i][2] = cadena.substring(0, cadena.indexOf("\t"));
			
			cadena = cadena.substring(cadena.indexOf("\t")+1, cadena.length());
			data[i][3] = cadena.substring(0, cadena.indexOf("\t"));

			cadena = cadena.substring(cadena.indexOf("\t")+1, cadena.length());
			if(cadena.indexOf(";") != -1){
				data[i][4] = cadena.substring(0, cadena.indexOf(";"));
				
				cadena = cadena.substring(cadena.indexOf(";")+1, cadena.length());
				data[i][5] = cadena;
			}else{
				data[i][4] = cadena.substring(0, cadena.length());
				data[i][5] = "";
			}
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
