/** @author: tlfs & afzs */
package objetoVisual;

import java.awt.image.BufferedImage;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Vector;

import util.nomiconos;
import util.simuGrafico;


/** Clase encargada de leer y almacenar en fichero los datos de las topologias */
public class lgfich
{
	/** Metodo para almacenar en fichero la topologia
	 * @param nomfich Nombre del fichero donde se almacena
	 * @param lista de objetos
	 * @return boolean
	 */
	public static boolean grabaFichero(String nomfich, listaObjetos lista)
	{
		boolean dev = true;
		XMLEncoder enc = null;
		
		try{
			enc = new XMLEncoder(new java.io.BufferedOutputStream(new java.io.FileOutputStream(nomfich)));
			
			enc.writeObject(lista.getPropiedades().getAutor());
			enc.writeObject(lista.getPropiedades().getFecha());
			enc.writeObject(lista.getPropiedades().getComentario());
			
			// Escribimos la configuracion de envios
			enc.writeObject(lista.getlistaAcciones());
			
			for (int i=0; i<lista.tam(); i++)
			{
				if (lista.getNomIcono(i).compareTo(nomiconos.nomVacio)!=0)
				{
					// Escribimos el nombre de la clase de este objeto
					enc.writeObject(lista.elementAt(i).getClass());
					enc.writeObject(lista.getNombre(i));
					enc.writeObject("" + lista.getX(i));
					enc.writeObject("" + lista.getY(i));
					enc.writeObject("" + lista.getWidth(i));
					enc.writeObject("" + lista.getHeight(i));
					enc.writeObject(lista.getConexiones(i));
					enc.writeObject(lista.getVectorInterfaces(i));
					enc.writeObject(lista.getVectorRutas(i));
				}
			}
			enc.close();
		}
		catch(Exception a)
		{
			dev = false;
			System.err.println("error");
		}
		
		return dev;
	}

	/** Metodo que almacena la topologia en un fichero grafico JPG
	 * @param nomfich Nombre del fichero donde se almacena
	 * @param ancho Ancho de la topologia
	 * @param alto Altura de la topologia
	 * @param lista lista de objetos
	 */
	public static void grabaJPG(String nomfich, int ancho, int alto, listaObjetos lista)
	{
		BufferedImage bi = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
		java.awt.Graphics2D g = bi.createGraphics();

		simuGrafico.dibuja(g, ancho, alto, lista, null, null, null, true);
		
		try
		{
			javax.imageio.ImageIO.write(bi, "jpg", new File(nomfich)); 
		}
		catch(IOException e)
		{
			System.out.println("***Error al escribir el archivo JPG");
		}
	}

	/** Metodo para leer el fichero de la topologia
	 * @param nomfich Fichero que se va a leer
	 * @param lista lista de objetos donde se ubicaran los datos leidos
	 * @return boolean indicando si la lectura ha sido correcta
	 */
	public static boolean leeFichero(String nomfich, listaObjetos lista)
	{
		String cadena;
		objetoVisual eqtemp;
		boolean sinFallos = true;
		boolean sigueLeyendo = true;

		// Eliminamos todos los objetos del vector
		lista.clear();
		eqtemp=null;
		
		try
		{
			XMLDecoder d = new XMLDecoder(new BufferedInputStream(new FileInputStream(nomfich)));

			lista.setPropiedades(new propiedadesTopologia((String)d.readObject(), (String)d.readObject(), (String)d.readObject()));
			lista.setlistaAcciones(new Vector((Vector)d.readObject()));
			
			while(sigueLeyendo)
			{
				try
				{
					cadena = (d.readObject()).toString();
					cadena = cadena.substring(6, cadena.length());
					
					try
					{
						eqtemp = (objetoVisual)Class.forName(cadena).newInstance();
					}
					catch(Exception e)
					{
						System.err.println("Error en lectura del nombre de la clase");
						System.err.println(e);
					}

					// Establecemos datos
					eqtemp.set(d.readObject().toString(), (new Integer((d.readObject()).toString())).intValue(), (new Integer((d.readObject()).toString())).intValue(), (new Integer((d.readObject()).toString())).intValue(), (new Integer((d.readObject()).toString())).intValue());
					
					// Leemos las conexiones, interfaces y rutas del equipo
					eqtemp.setConexiones(new Vector((Vector)d.readObject()));
					eqtemp.setVectorInterfaces(new Vector((Vector)d.readObject()));
					eqtemp.setVectorRutas(new Vector((Vector)d.readObject()));
				
					lista.add(eqtemp);
				}
				catch (ArrayIndexOutOfBoundsException a)
				{
					sigueLeyendo = false;
					d.close();
				}
			}
		}
		catch(Exception e)
		{
			sinFallos = false;
			System.out.println("Error en la lectura o fichero incompatible con esta version.");
		}
		
		return sinFallos;
	}
}
