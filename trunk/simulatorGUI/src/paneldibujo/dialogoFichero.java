/** @author: tlfs & afzs */
package paneldibujo;

import java.io.File;
import java.awt.Frame;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/** Clase que implementa un JFileChooser para leer o almacenar las topologias */
public class dialogoFichero extends JFileChooser
{
	/** En el constructor establecemos el tipo de fichero
	 * @param ext Si vale "jpg" entonces se almacena la topologia en formato
	 * grafico, en caso contrario, "todo", se trata de la opcion Guardar Como
	 */
	public dialogoFichero(String ext)
	{
		if (ext.compareTo("jpg")==0)
			setFileFilter(new filtroJPG());
		else
		{
			if (ext.compareTo("todo")==0)
				setFileFilter(new filtroJPG());

			setFileFilter(new filtroNet());
		}
	}
	
	/** Devuelve el directorio seleccionado */
	public String getDirectorio()
	{
		return getCurrentDirectory().getPath() + System.getProperty("file.separator");
	}
	
	/** Devuelve el filtro establecido para el JFileChooser */
	public String getFiltro()
	{
		return getFileFilter().getDescription();
	}
	
	/** Devuelve la ruta absoluta y el nombre del fichero */
	public String getNomFich()
	{
		return getSelectedFile().getAbsolutePath();
	}
	
	/** Muestra el cuadro de dialogo
	 * 
	 * @param titulo Titulo que va a tener
	 * @param nombre Nombre del fichero (Guardar Como)
	 * @return int con el resultado devuelto por el JFileChooser
	 */
	public int mostrar(Frame padreFrame, String titulo, String nombre)
	{
		setDialogTitle(titulo);
		this.setSelectedFile(new File(nombre));

		if (titulo.compareTo("Abrir")==0)
			return showOpenDialog(padreFrame);
		else if (titulo.compareTo("Guardar Como...")==0)
			return showSaveDialog(padreFrame);
		else if (titulo.compareTo("Exportar a...") == 0)
			return showSaveDialog(padreFrame);
			
		return -1;
	}
}

/** Clase que define el filtro para archivos .jpg */
class filtroJPG extends FileFilter
{
	
	public boolean accept(File f)
	{
		if(f!=null && f.isDirectory())
			return true;
		
		String extension = getExtension(f);
		
		if(extension != null && extension.compareTo("jpg")==0)
			return true;

		return false;
	}
	public void filtroFichero() {}
	
	public String getDescription()
	{
		return "Ficheros JPG (.jpg)";
	}
	
	public String getExtension(File f)
	{
		if(f != null)
		{
			String filename = f.getName();
			
			int i = filename.lastIndexOf('.');
			
			if(i>0 && i<filename.length()-1)
				return filename.substring(i+1).toLowerCase();
		}
		return null;
	}

}

/** Clase que define el filtro para archivos .net */
class filtroNet extends FileFilter
{
	
	public boolean accept(File f)
	{
		if(f!=null && f.isDirectory())
			return true;
		
		String extension = getExtension(f);
		
		if(extension != null && (extension.compareTo("net")==0 || extension.compareTo("net2")==0 || extension.compareTo("csv")==0))
			return true;

		return false;
	}
	public void filtroFichero() {}
	
	public String getDescription()
	{
		return "Ficheros de red (.net)";
	}
	
	public String getExtension(File f)
	{
		if(f != null)
		{
			String filename = f.getName();
			
			int i = filename.lastIndexOf('.');
			
			if(i>0 && i<filename.length()-1)
				return filename.substring(i+1).toLowerCase();
		}
		return null;
	}
}
