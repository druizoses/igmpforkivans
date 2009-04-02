/** @author: tlfs & afzs */
package barrabotones;

import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import util.colores;

/** Clase bboton para la barra de botones */
public class bboton extends JButton
{
	private String valores;

	/** Constructor por defecto de la clase */
	public bboton()
	{
		setBackground(colores.fondoBarras);
		valores = new String();
	}

	/** Funcion establece permite que se vayan cambiado los estados del menu y por lo tanto tambien de los botones
	 * @param valor valores en los que cada boton se habilitan
	 * @param estado estado actual
	 */
	public void establece(String valor, String estado)
	{
		int i,j;
		String c;
		
		// se pone deshabilitado
		setEnabled(false);
		
		valores=new String(valor);
		
		//si el estado coincide con uno de sus estados habilitados se habilita
		i=0;
		if(valores.compareTo(",99,")==0)
			setEnabled(true);
		else
			//si se habilita con el 18 quiere decir que tiene que haber cambios y algun equipo en el fichero (6,9)
			if(valores.compareTo(",18,")==0)
			{
				if (estado.lastIndexOf("-6-")!=-1 && (estado.lastIndexOf("-9-")!=-1 || estado.lastIndexOf("-10-")!=-1))
					setEnabled(true);
			}
			else
				while(i<estado.length())
				{
					for(j=i; j<estado.length() && estado.charAt(j)!='-'; j++);
					c=new String(estado.substring(i,j));
					i=j+1;
					if(valores.lastIndexOf(","+c+",")!=-1)
						setEnabled(true);
				}
	}
	
	/** Funcion setPBoton que establece los valores del boton
	 * @param nombre el nombre del boton
	 * @param nomicono la ruta de la imagen
	 * @param oyente el oyente del boton
	 * @param valor los estados en los que va a estar habilitado
	 * @param estado el estado actual
	 * @param texto el texto que aparece cuando se acerca el raton al boton
	 */
	public void setPBoton(String nombre, String nomicono, MouseListener oyente, String valor, String estado,String texto)
	{
		int i,j;
		String c;
		
		if(nomicono!=null)
			setIcon(new ImageIcon(nomicono)); //establece el icono
		setName(nombre); //establece el nombre
		setBorder(null);
		setToolTipText(texto); //establece el texto

		setEnabled(false); //lo deshabilita
		
		valores=new String(valor);
		
		i=0;
		//se comprueban los valores que cada boton tiene para habilitarse
		if(valores.compareTo(",99,")==0)
			setEnabled(true);
		else
			//si se habilita con el 18 quiere decir que tiene que haber cambios y algun equipo en el fichero (6,9)
			if(valores.compareTo(",18,")==0)
			{
				if (estado.lastIndexOf("-6-")!=-1 && (estado.lastIndexOf("-9-")!=-1 || estado.lastIndexOf("-10-")!=-1))
					setEnabled(true);
			}
			else
				while(i<estado.length())
				{
					for(j=i; j<estado.length() && estado.charAt(j)!='-'; j++);
					c=new String(estado.substring(i,j));
					i=j+1;
					if(valores.lastIndexOf(","+c+",")!=-1)
						setEnabled(true);
				}

		addMouseListener(oyente); //se anyade el oyente
	}
	
	public void estableceTexto(String texto)
	{
		setText("         "+texto);
	}
}
