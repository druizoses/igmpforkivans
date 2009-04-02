/** @author: tlfs & afzs */
package objetoVisual;

import java.util.Date;

public class propiedadesTopologia
{
	private String autor;
	private String comentario;
	private String fecha;
	
	public propiedadesTopologia()
	{
		autor = System.getProperty("user.name");
		fecha = new Date().toString();
		comentario = "";
	}

	public propiedadesTopologia(propiedadesTopologia origen)
	{
		this.autor = origen.getAutor();
		this.comentario = origen.getComentario();
		this.fecha = origen.getFecha();
	}

	public propiedadesTopologia(String autor, String fecha, String comentario)
	{
		this.autor = autor;
		this.comentario = comentario;
		this.fecha = fecha;
	}
	
	/** Devuelve true si las dos propiedades son iguales */
	public boolean compara(propiedadesTopologia otra)
	{
		boolean dev = true;
		
		if (otra.getAutor().compareTo(autor)!=0 || otra.getFecha().compareTo(fecha)!=0 || otra.getComentario().compareTo(comentario)!=0)
			dev = false;
		
		return dev;
	}

	public String getAutor()
	{
		return autor;
	}
	
	public String getComentario()
	{
		return comentario;
	}
	
	public String getFecha()
	{
		return fecha;
	}
	
	public void setAutor(String autor)
	{
		this.autor = autor;
	}
	
	public void setComentario(String comentario)
	{
		this.comentario = comentario;
	}
	
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}
	
	public String toString()
	{
		return "Autor: " + autor + ", Fecha: " + fecha + " Comentario: " + comentario; 
	}
}
