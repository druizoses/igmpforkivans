/** @author: tlfs & afzs */
package ayuda;

import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/** Clase frameAyuda que muestra la ayuda de la aplicacion */
public class frameAyuda extends JFrame
{
	/** Constructor de frameAyuda
	 * @param xCentral
	 * @param yCentral coordenadas para mostrar la ventana de ayuda
	 */
	public frameAyuda(int xCentral, int yCentral)
	{
		JEditorPane panelAyuda = new JEditorPane();

		//para que el frame no se pueda modificar
		panelAyuda.setEditable(false);
		
		//la llamada al fichero html al que referencia
		URL urlAyuda = frameAyuda.class.getResource("ayudaFicheros" + System.getProperty("file.separator") + "index.html");
		
		//para coger las excepciones si no puede abrir la ayuda
		if (urlAyuda != null)
		{
			try
			{
				panelAyuda.setPage(urlAyuda);
			}
			catch (Exception e)
			{
				System.err.println("Error al abrir la ayuda.");
			}
		}
		else
			System.err.println("No se encuentra el fichero de la ayuda");

		final JEditorPane finalPane = panelAyuda;
		
		//capturar eventos en la ayuda
		panelAyuda.addHyperlinkListener(new HyperlinkListener()
		{
			public void hyperlinkUpdate(HyperlinkEvent ev)
			{
				try
				{
					if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
						finalPane.setPage(ev.getURL());
				}
				catch(Exception ex)
				{
					ex.printStackTrace(System.err);
				}
			}
		});
		
		//se crea el scroll
		JScrollPane scrollAyuda = new JScrollPane(panelAyuda);
		
		this.getContentPane().add(scrollAyuda);
		
		//se anyade el titulo de la aplicacion
		this.setTitle("Ayuda de la aplicacion");

		//se establecen las coordenadas
		int ancho = 500;
		int alto = 500;
		this.setBounds(xCentral-ancho/2, yCentral-alto/2, ancho, alto);
		this.setVisible(true);
	}
}
