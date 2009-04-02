/** @author: tlfs & afzs */
package visSim.modelosTablas;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/** Clase que aplica un tooltip a las columnas de la tabla */
public class modeloRendererTablaErrores extends JLabel implements TableCellRenderer
{
	public modeloRendererTablaErrores() {}
	
	/** Funcion que aplica un tooltip a cada columna */
	public Component getTableCellRendererComponent(JTable table, Object valor, boolean isSelected, boolean hasFocus, int row, int column)
	{
		String cadena = (String)valor;
		
		setText(cadena);
		setToolTipText(cadena);
		
		return this;
    }
}
