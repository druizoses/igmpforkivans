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

import java.util.Vector;

/**
 * Lista de parametros
 */
public class ListaParametros
{
    /**
     * Lista de parametros
     */
    Vector parametros;
       
    
    
    /**
     * Constructor
     */
    public ListaParametros()
    {
        parametros=new Vector();
    }
        
    
    
    /**
     * Devuelve el nombre del parametro
     * @param nombre Nombre del parametro
     * @return Valor del parametro
     */
    public Parametro get(String nombre)
    {
        Parametro p=null;
        boolean encontrado=false;
        
        if(nombre!=null)
        {    
            for(int i=0;!encontrado && i<parametros.size();i++)
            {
                p=(Parametro)parametros.get(i);
                if(p.getNombre().equals(nombre))
                    encontrado=true;
            }
        }
        
        return(p);
    }
    
    
    
    /**
     * Devuelve el parametro indicado
     * @param numParametro Numero de parametro
     * @return valor del parametro
     */
    public Parametro get(int numParametro)
    {
        if(numParametro>=0 && numParametro<parametros.size())
            return((Parametro)parametros.get(numParametro));
        return(null);
    }
    
    
    
    /**
     * Añade un parametro a la lista
     * @param p Parametro
     */
    public void add(Parametro p)
    {
        parametros.add(p);
    }
    
    
    
    /**
     * Da un valor a un parametro
     * @param nombre Nombre del parametro
     * @param valor Valor
     */
    public void setValor(String nombre, Object valor)
    {
        Parametro p=get(nombre);
        
        if(p!=null)
            p.set(valor);
    }
    
    
    
    /**
     * Devuelve el valor de un parametro 
     * @param nombre Nombre del parametro
     * @return Valor
     */
    public Object getValor(String nombre)
    {
        Parametro p=get(nombre);
        if(p!=null)
            return(p.get());
        return(null);
    }
    
    
    
    /**
     * Devuelve el numero de parametros de la lista
     * @return Numero de parametros
     */
    public int size()
    {
        return(parametros.size());
    }
}
