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

package Redes;

/**
  * Clase base para los distintos tipos de direcciones
  */
public class Direccion
{
	/**
	 * Constante para indicar que un byte de la direccion no esta inicializado
	 */
	public static final int kNOINICIALIZADO=-1;
	
	/**
	 * Bytes que forman la direccion IPv4, x.x.x.x -> byte0.byte1.byte2.byte3
	 */
	int[] bytes; 
	
	/**
	 * Longitud de la direccion en bytes
	 */
    int longitud;	
 	
 	/**
 	 * Tipo de la direccion segun el RFC 1700, pagina 168
 	 */
 	int tipo;
	
 	
 	
	/**
	 * Constructor
	 * @param longitud Longitud de la direccion
	 * @param tipo Tipo de direccion, segun el RFC 1700, pagina 168
	 * @throws IllegalArgumentException si la longitud no es valida
	 */
    public Direccion(int longitud,int tipo) throws IllegalArgumentException
    {
    	// 0. Comprobaciones previas
    	if(longitud<=0)
    	   throw new IllegalArgumentException("Longitud no valida para una direccion");
    	
    	this.longitud=longitud;
    	bytes=new int[longitud];
    	for(int i=0;i<longitud;i++)
    	   bytes[i]=kNOINICIALIZADO;
    	this.tipo=tipo;
    }
    
    
    
    /**
     * Constructor de copia
     * @param direccion Direccion a copiar
     * @throws IllegalArgumentException si la direccion no es valida 
     */
    public Direccion(Direccion direccion)
    {
    	// 0. Comprobaciones previas
        if(direccion==null || direccion.longitud<=0)
            throw new IllegalArgumentException("No se puede construir una direccion a partir de la nada");
        
        tipo=direccion.Tipo();
    	bytes=null;
    	longitud=direccion.Longitud();
   		bytes=new int[longitud];
   	    for(int i=0;i<longitud;i++)
   		   bytes[i]=direccion.getByte(i);
    }
    
    
    
    /**
     * Pone un valor en un determinado byte de la direccion
     * @param pos Posicion del byte a modificar
     * @param valor Nuevo valor para el byte indicado
     * @throws IllegalArgumentException si la posicion o el valor no son validos
     */
    public void setByte(int pos, int valor) throws IllegalArgumentException
    {
    	if(pos>=0 && pos<longitud && valor>=0 && valor<=255)
    		bytes[pos]=valor;
        else
            throw new IllegalArgumentException("Inicializacion no valida");
    }
    
    
    
    /**
     * Recupera un determinado byte de la direccion
     * @param pos Posicion que ocupa el byte que se quiere recuperar en la direccion
     * @return Byte indicado de la direccion
     * @throws IllegalArgumentException si la posicion esta fuera de rango
     */
    public int getByte(int pos) throws IllegalArgumentException
    {
    	// 0. Comprobaciones previas
    	if(pos>=longitud || pos<0)
    	   throw new IllegalArgumentException("Posicion no valida en la dirccion");
    	
    	return(bytes[pos]);
    }
    
    
    
    /**
     * Devuelve la longitud en bytes de la direccion
     * @return Longitud en bytes
     */
    public int Longitud()
    {
    	return(longitud);
    }
    
    
    
    /**
     * Devuelve el tipo de la direccion
     * @return Tipo de la direccion
     */
    public int Tipo()
    {
    	return(tipo);
    }
    
    
    
	/**
	 * Comprueba que las direcciones sean iguales
	 * @param direccion Direccion
	 * @return Cierto, si las direcciones son iguales
	 */
	public boolean equals(Direccion direccion)
	{
		boolean iguales=true;
    	
		if(direccion!=null && direccion.Longitud()==Longitud() && tipo==direccion.Tipo())
		{	
		   for(int i=0;i<longitud && iguales==true;i++)
		   {	
		      if(getByte(i)!=direccion.getByte(i))
			      iguales=false;
		   }
		}
		else
		{
			iguales=false;
		}

		return(iguales);
	}
	
	
	
	/**
	 * Devuelve la direccion en forma binaria
	 * @return Secuencia binaria
	 */
	public int[] getBits()
	{
		int byteAux;
		int bits[];
		
		bits=new int[longitud*8];
		
		for(int i=0;i<longitud;i++)
		{    
			byteAux=getByte(i);
			if((byteAux&128)>0) bits[i*8]=1;   else bits[i*8]=0;
			if((byteAux&64)>0)  bits[i*8+1]=1; else bits[i*8+1]=0;
			if((byteAux&32)>0)  bits[i*8+2]=1; else bits[i*8+2]=0;
			if((byteAux&16)>0)  bits[i*8+3]=1; else bits[i*8+3]=0;
			if((byteAux&8)>0)   bits[i*8+4]=1; else bits[i*8+4]=0;
			if((byteAux&4)>0)   bits[i*8+5]=1; else bits[i*8+5]=0;
			if((byteAux&2)>0)   bits[i*8+6]=1; else bits[i*8+6]=0;
			if((byteAux&1)>0)   bits[i*8+7]=1; else bits[i*8+7]=0;
		}
		
		return(bits);
	}
}
