/*
    Simulador de redes IP (KIVA). API de Simulacion, permite simular
    redes de tipo IP que usen IP, ARP, e ICMP.
    Copyright (C) 2004, José María Díaz Gorriz

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Library General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Library General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this library; if not, write to the 
    Free Software Foundation, Inc., 59 Temple Place - Suite 330, 
    Boston, MA  02111-1307  USA.
*/

/*
 * Creado el 14-feb-2004
 */
 
package Proyecto;

/**
 * Parametro
 */
public class Parametro
{
    /**
     * Descripcion del parametros
     */
    private String descripcion;
    
    /**
     * Clase (nombre completo)
     */
    private String clase;
    
    /**
     * Nombre del parametro
     */
    private String nombre;
    
    /**
     * Valor
     */
    private Object valor;
    
    
    
    /**
     * Constructor
     * @param nombre Nombre del parametro
     * @param descripcion Descripcion del parametro
     * @param clase Clase Java del parametro
     */
    public Parametro(String nombre, String descripcion, String clase)
    {
        // 0. Pre-Inicializacion
        this.descripcion=null;
        this.clase=null;
        this.valor=null;
        this.nombre=null;
        
        // 1. Comprobacion de parametros
        if(nombre==null || descripcion==null || clase==null)
            throw new IllegalArgumentException("Valores iniciales incorrectos para los parametros");
        this.descripcion=descripcion;
        this.clase=clase;
        this.nombre=nombre;
    }
    
    
    
    /**
     * Da un valor al parametro
     * @param objeto Valor
     */
    public void set(Object objeto)
    {
        if(objeto!=null && clase.equals(objeto.getClass().getName()))
            valor=objeto;
    }
    
    
    
    /**
     * Devuelve el valor del parametro
     * @return Valor del parametro
     */
    public Object get()
    {
        return(valor);
    }
    
    
    /**
     * Devuelve el nombre del parametro
     * @return Nombre del parametro
     */
    public String getNombre()
    {
        return(nombre);
    }
    
    
    
    /**
     * Devuelve la descripcion del parametro
     * @return Descripcion
     */
    public String getDescripcion()
    {
        return(descripcion);
    }
}
