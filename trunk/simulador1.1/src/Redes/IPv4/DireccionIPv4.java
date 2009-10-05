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

package Redes.IPv4;

import java.util.StringTokenizer;
import Redes.Direccion;

/**
 * Direccion IP
 */
public class DireccionIPv4 extends Direccion
{
	/**
	 * Constructor a partir de una cadena
	 * @param ip Cadena de texto con la direccion IP
	 * @throws IllegalArgumentException
	 */
	public DireccionIPv4(String ip) throws IllegalArgumentException
	{
	   super(4,0x0800);
		
	   int byteAux;	
	   String cadAux;
       int i;
       
       // 1. Creamos un stringtokenizer para que separe los tokens, usando como 
       // delimitador el caracter '.'
       StringTokenizer st=new StringTokenizer(ip,".");
       
       if(st.countTokens()!=Longitud())
       {
       	  throw new IllegalArgumentException("Direccion IP malformada");
       }
       else
       {
       	  i=0;
       	  while(st.hasMoreTokens())
       	  {
       	  	 try
       	  	 {
       	  	 	cadAux=st.nextToken();
       	  	   	byteAux=Integer.parseInt(cadAux);
       	  	   	if(byteAux>=0 || byteAux<=255)
       	  	   	   setByte(i,byteAux);
       	  	   	
       	  	   	i++;
       	  	 }
       	  	 catch(NumberFormatException e) { }
       	  }
       	  
       	  
       	  // Comprobamos que los 4 bytes estan correctos
       	  for(i=0;i<Longitud();i++)
       	     if(getByte(i)==kNOINICIALIZADO)
       	        throw new IllegalArgumentException("Direccion IP malformada (byte "+i+"º)");
       }
	}
	
	
	
	/**
	 * Constructor en base a los 4 bytes de la direccion IPv4
	 * @param b0  Primer byte
	 * @param b1  Segundo byte
	 * @param b2  Tercer Byte
	 * @param b3  Cuarto byte
	 * @throws IllegalArgumentException
	 */
	public DireccionIPv4(int b0,int b1,int b2,int b3) throws IllegalArgumentException
	{
		super(4,0x0800);

		setByte(0,b0);
		setByte(1,b1);
		setByte(2,b2);
		setByte(3,b3);
	}
	
	
	
	/**
	 * Constructor a partir de una secuencia de 32 bits
	 * @param bits Secuencia de 32 bits
	 * @throws IllegalArgumentException
	 */
	public DireccionIPv4(int bits[]) throws IllegalArgumentException
	{
		super(4,0x800);
		
		if(bits.length!=32)
			throw new IllegalArgumentException("Numero de bits erroneo para un direccion IPv4");
	
		int byteAux=0;
		for(int i=0;i<4;i++)
		{
		    byteAux=0;
			if(bits[i*8]==1)   byteAux+=128;
			if(bits[i*8+1]==1) byteAux+=64;
			if(bits[i*8+2]==1) byteAux+=32;
			if(bits[i*8+3]==1) byteAux+=16;
			if(bits[i*8+4]==1) byteAux+=8;
			if(bits[i*8+5]==1) byteAux+=4;
			if(bits[i*8+6]==1) byteAux+=2;
			if(bits[i*8+7]==1) byteAux+=1;
	        
			setByte(i,byteAux);
		}
	}
	

	
	/**
	 * Devuelve la direccion IP
	 * @return Direccion IP en forma de cadena de texto
	 */
	public String getIP()
	{
		String ip=(""+getByte(0)+"."+getByte(1)+"."+getByte(2)+"."+getByte(3));
		
		return(ip);
	}
	
	
	
	/**
	 * Calcula la mascara por defecto para la direccion IPv4
	 * @return mascara por defecto
	 */
	public MascaraIPv4 getMascaraIPv4()
	{
		int b0=getByte(0);
		MascaraIPv4 mascara=null;
		
		if(b0>=1 && b0<=127)                            // clase A
		   mascara=new MascaraIPv4("255.0.0.0");        
		else if(b0>=128 && b0<=191)                     // clase B
		   mascara=new MascaraIPv4("255.255.0.0");
		else if(b0>=192 && b0<=223)                     // clase C
		   mascara=new MascaraIPv4("255.255.255.0");
		else if(b0>=224 && b0<=239)                     // multidifusion, clase D
		   mascara=new MascaraIPv4("255.255.255.255");  
		else if(b0>=240 && b0<=247)                     // reservado para uso futuro, clase E
		   mascara=new MascaraIPv4("255.255.255.255");
		else                                            // ¿?
		   mascara=new MascaraIPv4("255.255.255.255");
		
		return(mascara);
	}
	
	
	
	/**
	 * Aplica una mascara a la direccion ip y devuelve el resultado
	 * @param mascara Mascara a aplicar
	 * @return direccion ip resultante de aplicar la mascara
	 */
	public DireccionIPv4 getIPdeRed(MascaraIPv4 mascara)
	{
		int b0,b1,b2,b3;
		DireccionIPv4 dirIP=null;
		
		b0=getByte(0)&mascara.getByte(0);
		b1=getByte(1)&mascara.getByte(1);
		b2=getByte(2)&mascara.getByte(2);
		b3=getByte(3)&mascara.getByte(3);
		
        dirIP=new DireccionIPv4(b0,b1,b2,b3);
		
		return(dirIP);
	}
	

	
	/**
	 * Devuelve la direccion de broadcast de la red asociada a esta ip y mascara
	 * @param mascara Mascara de red
	 * @return Direccion de broadcast de la red
	 */
	public DireccionIPv4 getIPdeBroadcast(MascaraIPv4 mascara)
	{
		DireccionIPv4 broadcast=null;
	    DireccionIPv4 dirRed=null;
	    
	    // Calculamos la direccion de la red
	    dirRed=getIPdeRed(mascara);
	    
	    // Obtenemos la secuencia binaria de la direccion de broadcast de la red
	    if(dirRed!=null)
	    {
	    	int bitsRed[]=dirRed.getBits();
	    	int bitsMascara[]=mascara.getBits();
	        
	    	// ponemos a 1 los bits de la direccion de red que en la mascara esten a 0
	    	// para convertir los bits de la dir. de red. en los de la dir de broadcast
	    	for(int i=0;i<32;i++)
	    		if(bitsMascara[i]==0)
	    			bitsRed[i]=1;
	    
	        // Convertimos la secuencia binaria de la dir de broadcast en una
	        // DireccionIPv4
	    	broadcast=new DireccionIPv4(bitsRed);
	    }
	    
		return(broadcast);
	}
	
	
	
	/**
	 * Comprueba si la direccion es una direccion IPv4 de broadcast 
	 * @param ip Direccion IPv4 del interfaz
	 * @param mascara Mascara de red
	 * @return Cierto si la direccion es de broadcast
	 */
	public boolean EsBroadcastDe(DireccionIPv4 ip, MascaraIPv4 mascara)
	{
		boolean retorno=false;
		DireccionIPv4 broadcast;
		
		try
		{
           broadcast=ip.getIPdeBroadcast(mascara);
           if(broadcast.equals(this))
           	  retorno=true;
		}
		catch(Exception e) {}
		
		return(retorno);
	}
	
	
	
	/**
	 * Comprueba si la direccion es la direccion del bucle local
	 * @return Ciero si la direccion es 127.X.X.X (.0.0.1)
	 */
	public boolean EsLoopback()
	{
	    if(getByte(0)==127) // && getByte(1)==0 && getByte(2)==0 && getByte(3)==1)
	        return(true);
	    return(false);
	}
}
