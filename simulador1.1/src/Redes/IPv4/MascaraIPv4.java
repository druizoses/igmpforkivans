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

/**
 * Máscara IP
 */
public class MascaraIPv4 extends DireccionIPv4
{
    /**
     * Constructor en base a una cadena
     * @param mascara
     * @throws IllegalArgumentException
     */
    public MascaraIPv4(String mascara) throws IllegalArgumentException
    {
		super(mascara);
    	
		int mascara_en_binario[];   //representacion binaria de la mascara
		int b;                      //byte de la direccion IP que se esta procesando
		int i,j;                    //contadores
    
        // 1. Comprobaciones extras sobre el formato de la mascara
		
		// 1.1 Inicializamos a 0 la mascara binaria
		mascara_en_binario=new int[32];
		for(i=0;i<32;i++)
		  mascara_en_binario[i]=0;
		
		// 1.2 Activamos los bits correspondientes
		for(i=3;i>=0;i--)
		{
			b=getByte(i);
		    j=0;
			if((b&128)>0)     mascara_en_binario[j*8+0]=1;
			else if((b&64)>0) mascara_en_binario[j*8+1]=1;
			else if((b&32)>0) mascara_en_binario[j*8+2]=1;
			else if((b&16)>0) mascara_en_binario[j*8+3]=1;
			else if((b&8)>0)  mascara_en_binario[j*8+4]=1;
			else if((b&4)>0)  mascara_en_binario[j*8+5]=1;
			else if((b&2)>0)  mascara_en_binario[j*8+6]=1;
			else if((b&1)>0)  mascara_en_binario[j*8+7]=1;     
		}
		
		// 1.3 Comprobamos que todos los bits a 1 estan juntos (patron 01 no permitido)
		for(i=1;i<32;i++)
		  if(mascara_en_binario[i-1]==0 && mascara_en_binario[i]==1)
		     throw new IllegalArgumentException("Mascara no valida");
    }
    
    
    
    /**
     * Devuelve el numero de bits a 1 de la mascara
     * @return Numero de bits a 1
     */
    public int NumBits()
    {
    	int numBits=0;
    	int bits[]=getBits();
    	
    	// 1. Contamos los bits hasta llegar a un 0, o acabar
    	for(int i=0;bits[i]!=0 && i<Longitud();i++)
    		numBits++;
    	
    	return(numBits);
    }
}
