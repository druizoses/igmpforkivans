/** @author: tlfs & afzs */
package util;

import java.awt.Graphics;
import java.util.Vector;

import objetoVisual.listaObjetos;

/** Clase encargada de representar en un objeto Graphics el contenido de la topologia */
public class simuGrafico
{
	/** Constructor de la clase simuGrafico
	 * @param g Objeto Graphics donde se dibuja la topologia
	 * @param ancho Anchura de la topologia
	 * @param alto Altura de la topologia
	 * @param lista Lista de objetos de la topologia 
	 * @param rectangulo Area de seleccion
	 * @param inicialLinea Linea de conexion actual
	 * @param circuloTR Anillo dibujandose actualmente
	 * @param quitaSelecciones Elimina los marcos de selecciones cuando se va a imprimir o grabar en jpg
	 * @see listaObjetos
	 * @see utilLinea
	 */
	public static void dibuja(Graphics g, int ancho, int alto, listaObjetos lista, utilLinea rectangulo, utilLinea inicialLinea, utilLinea circuloTR, boolean quitaSelecciones)
	{
		int i;
		Vector IPs;

		// Ponemos fondo blanco para rellenar
		g.setColor(colores.blanco);
		g.fillRect(0,0, ancho, alto);
		
		// TODO: Mirar a ver que se hace con esto
		// Si no vamos a a dibujar las selecciones entonces imprimimos o grabamos JPG
		// Se modifica el ancho y el alto de la imagen
		/*if (quitaSelecciones)
		{
			int izq = ancho+1;
			int dcha = 0;
			int arr = alto+1;
			int aba = 0;
			int xTexto, yTexto, anchoTexto, altoTexto;
			
			for (i=0; i<lista.tam(); i++)
			{
				// Comprobaciones para coordenadas de equipos
				if (lista.getX(i) < izq)
					izq = lista.getX(i);
				if (lista.getX(i)+lista.getWidth(i) > dcha)
					dcha = lista.getX(i)+lista.getWidth(i);
				if (lista.getY(i) < arr)
					arr = lista.getY(i);
				if (lista.getY(i)+lista.getHeight(i) > aba)
					aba = lista.getY(i)+lista.getHeight(i);
				
				// Comprobaciones para los textos de los equipos
				if (lista.getTextoVisual(i)!=null)
				{
					xTexto = lista.getTextoVisual(i).getX();
					yTexto = lista.getTextoVisual(i).getY();
					anchoTexto = lista.getTextoVisual(i).getAncho();
					altoTexto = lista.getTextoVisual(i).getAlto();

					if (xTexto < izq) 
						izq = xTexto;
					if (xTexto+anchoTexto > dcha)
						dcha = xTexto+anchoTexto;
					if (yTexto < arr)
						arr = yTexto;
					if (yTexto+altoTexto > aba)
						aba = yTexto+altoTexto;
				}
			
				// Comprobaciones para los textos de las IPs de los equipos
				IPs = new Vector(lista.getIPVisual(i));
				for (int j=0; j<IPs.size(); j++)
					if ((utilTexto)IPs.elementAt(j) != null)
					{
						xTexto = ((utilTexto)IPs.elementAt(j)).getX();
						yTexto = ((utilTexto)IPs.elementAt(j)).getY();
						anchoTexto = ((utilTexto)IPs.elementAt(j)).getAncho();
						altoTexto = ((utilTexto)IPs.elementAt(j)).getAlto();
						
						if (xTexto < izq) 
							izq = xTexto;
						if (xTexto+anchoTexto > dcha)
							dcha = xTexto+anchoTexto+500;
						if (yTexto+10 < arr)
							arr = yTexto;
						if (yTexto+altoTexto+10 > aba)
							aba = yTexto+altoTexto;
					}
			}
			
			// Trasladamos el origen de coordenadas y modificamos el ancho y el alto
			g.translate(-izq+20,-arr+20);
			ancho = dcha+50;
			alto = aba+50;
		}*/
			
		// Todo lo que se dibuje ira en color negro
		g.setColor(colores.negro);

		// Dibujado del area de seleccion
		if (rectangulo!=null)
			if (rectangulo.getX(2) > rectangulo.getX(1) && rectangulo.getY(2)>rectangulo.getY(1))
				g.drawRect(rectangulo.getX(1), rectangulo.getY(1), rectangulo.getX(2)- rectangulo.getX(1), rectangulo.getY(2)- rectangulo.getY(1));
			else if (rectangulo.getX(2) > rectangulo.getX(1) && rectangulo.getY(2)<rectangulo.getY(1))
				g.drawRect(rectangulo.getX(1), rectangulo.getY(2), rectangulo.getX(2)- rectangulo.getX(1), rectangulo.getY(1)- rectangulo.getY(2));
			else if (rectangulo.getX(1) > rectangulo.getX(2) && rectangulo.getY(1)>rectangulo.getY(2))
				g.drawRect(rectangulo.getX(2), rectangulo.getY(2), rectangulo.getX(1)- rectangulo.getX(2), rectangulo.getY(1)- rectangulo.getY(2));
			else if (rectangulo.getX(1) > rectangulo.getX(2) && rectangulo.getY(1)<rectangulo.getY(2))
				g.drawRect(rectangulo.getX(2), rectangulo.getY(1), rectangulo.getX(1)- rectangulo.getX(2), rectangulo.getY(2)- rectangulo.getY(1));

		// Dibujado de la linea de conexion entre equipos
		if (inicialLinea!=null)
			g.drawLine(inicialLinea.getX(1), inicialLinea.getY(1), inicialLinea.getX(2), inicialLinea.getY(2));

		// Dibujado de anillo
		if (circuloTR!=null)
		{
			double radio = Math.sqrt( (circuloTR.getX(2)-circuloTR.getX(1))*(circuloTR.getX(2)-circuloTR.getX(1)) + (circuloTR.getY(2)-circuloTR.getY(1))*(circuloTR.getY(2)-circuloTR.getY(1)));
			
			g.drawOval(circuloTR.getX(1), circuloTR.getY(1), (int)radio, (int)radio);
		}

		// Dibujamos las conexiones de los Equipos
		Vector lineas = new Vector(lista.creaLineas());
		utilLinea tempLinea;
		for (i=0; i<lineas.size(); i++)
		{
			tempLinea = new utilLinea((utilLinea)lineas.elementAt(i));
			g.drawLine(tempLinea.getX(1), tempLinea.getY(1), tempLinea.getX(2), tempLinea.getY(2));
		}
		
		// Ahora tenemos que dibujar los equipos.
		utilTexto txt;
		for (i=0; i<lista.tam(); i++)
		{
			// Dibujo del equipo
			g.drawImage(lista.dibuja(i, quitaSelecciones), lista.getX(i), lista.getY(i), null);

			// Dibujo de su nombre
			txt = lista.getTextoVisual(i);
			if (txt != null)
				g.drawImage(txt.dibuja(), txt.getX(), txt.getY(), null);
			if(!lista.getNombre(i).startsWith("Switch")){
				// Dibujo de las IPs de las interfaces
				IPs = new Vector(lista.getIPVisual(i));
				for (int j=0; j<IPs.size(); j++)
				{
					txt = (utilTexto)IPs.elementAt(j);
					if (txt != null)
						g.drawString(txt.getTexto(), txt.getX(), txt.getY());
				}
			}
		}
	}
}
