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

package Redes.Ethernet;

import java.util.StringTokenizer;
import Redes.Direccion;

/**
 * Direccion MAC
 */
public class DireccionEthernet extends Direccion
{
    /**
     * Constructor en base a una cadena de texto
     * @param mac Direccion MAC
     * @throws IllegalArgumentException si la cadena de texto no se puede convertir
     *         a una direccion MAC
     */
    public DireccionEthernet(String mac) throws IllegalArgumentException
    {
    	super(6,Ethernet.kTIPO);
    	
    	String byteAux;
		boolean correcto=true;
		int valor;
		
		// 0. Comprobaciones previas
		if(mac==null)
		    throw new IllegalArgumentException("Direccion MAC no valida");
		
    	// 1. Parsing
    	StringTokenizer st=new StringTokenizer(mac,":",false);
    	if(st.countTokens()!=Longitud())
    	{
    		throw new IllegalArgumentException("Direccion MAC malformada");
    	}
    	else
    	{
    		for(int i=0;i<Longitud() && correcto;i++)
    		{
    			byteAux=st.nextToken();
    			valor=hexadecimalCorrecto(byteAux);
    			if(valor>=0 && valor<=255)
    				setByte(i,valor);
     			else
                    correcto=false;   			
    		}
    		
    		// comprobamos que los 6 valores son hexadecimales correctos
    	    if(!correcto)
    	       throw new IllegalArgumentException("Direccion MAC malformada");	
    	}
    }
    
    
    
	/**
	 * Constructor en base a los bytes que la forman
	 * @param b0 Byte 0
	 * @param b1 Byte 1
	 * @param b2 Byte 2
	 * @param b3 Byte 3
	 * @param b4 Byte 4
	 * @param b5 Byte 5
	 * @throws IllegalArgumentException
     */
    public DireccionEthernet(int b0,int b1,int b2,int b3,int b4,int b5) throws IllegalArgumentException
	{
		super(6,Ethernet.kTIPO);
    	
		setByte(0,b0);
		setByte(1,b1);
		setByte(2,b2);
		setByte(3,b3);
		setByte(4,b4);
		setByte(5,b5);
	}
    
    
    
    /**
     * Constructor
     * @param direccion Direccion de la misma longitud (6 bytes)
     * @throws IllegalArgumentException si la direccion indicada no es compatible
     */
    public DireccionEthernet(Direccion direccion) throws IllegalArgumentException
	{
    	super(6,Ethernet.kTIPO);
    	
    	if(direccion==null || direccion.Longitud()!=6)
    		throw new IllegalArgumentException("Direccion incorrecta");
    	
    	for(int i=0;i<6;i++)
    		setByte(i,direccion.getByte(i));
    }
    
    
    
    /**
     * Comprueba que la cadena es un valor hexadecimal correcto de 8 bits
     * @param valor Cadena de texto a comprobar
     * @return Valor hexadecimal, o -1 en caso de error
     */
    private int hexadecimalCorrecto(String valor)
    { 
    	int retorno=-1;
    	int valorAux;
    	
    	try {
    		valorAux=Integer.parseInt(valor,16);
    		if(valorAux>=0 && valorAux<=255)
    		   retorno=valorAux;
    	} 
    	catch(Exception e) 
		{ 
    		// se devuelve el valor -1
		}

    	return(retorno);
    }
    
    
    
    /**
     * Convierte la direccion MAC a una cadena de texto
     * @return Direccion MAC en formato de cadena de texto
     */
    public String toString()
	{
    	String dir=new String("");
    	
    	dir+=Integer.toHexString(getByte(0))+":";
    	dir+=Integer.toHexString(getByte(1))+":";
    	dir+=Integer.toHexString(getByte(2))+":";
    	dir+=Integer.toHexString(getByte(3))+":";
    	dir+=Integer.toHexString(getByte(4))+":";
    	dir+=Integer.toHexString(getByte(5));
    	
    	return(dir);
    }
    
    
    
    /**
     * Comprueba si la direccion ethernet es de broadcast
     * @return Cierto si la direccion es de broadcast
     */
    public boolean EsBroadcast()
	{
    	boolean retorno=true;
    	
    	for(int i=0;i<6 && retorno==true;i++)
    		if(getByte(i)!=255)
    			retorno=false;
    		
    	return(retorno);
    }
}
