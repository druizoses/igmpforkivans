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
 * Clase base para todos los tipos de paquetes de datos  
 */
public class Buffer 
{
    /**
     * Buffer que contiene todos los bytes del buffer. Los bytes se 
     * numeran 0123456789, y los bits dentro de un byte 76543210
     */
    int[] Datos;
    
    
    /**
     * Longitud en bytes del buffer
     */
    int longitud;

    
    
    
    /**
     * Constructor
     * @param tam Tamaño inicial del buffer
     * @throws IllegalArgumentException si el tamaño indicado no es valido
     */
    public Buffer(int tam)
    {
    	// 0. Comprobacion previa
    	if(tam<=0)
    	   throw new IllegalArgumentException("Tamaño del buffer demasiado pequeño");
    	
    	// 1. Creamos el buffer interno
    	Datos=new int[tam];
    	longitud=tam;
    	
    	// 2. Inicializamos el buffer a 0
    	for(int i=0;i<tam;i++)
    	   Datos[i]=0;
    }

    
    
    /**
     * Constructor
     * @param cadena Datos del paquete
     * @throws IllegalArgumentException si la cadena de texto no es valida
     */
    public Buffer(String cadena)
	{
    	// 0. Comprobaciones previas
    	if(cadena==null || cadena.length()<=0)
    		throw new IllegalArgumentException("Cadena de texto no valida");
    	
    	// 1. Creamos el buffer interno
    	longitud=cadena.length();
    	Datos=new int[longitud];
    	
    	// 2. Rellenamos el buffer con los datos proporcionados
    	for(int i=0;i<longitud;i++)
    		Datos[i]=cadena.charAt(i);
    }    
    
    
    
    /**
     * Constructor de copia
     * @param datos Datos contenidos en el paquete
     * @throws IllegalArgumentException si el buffer de datos no es valido
     */
    public Buffer(Buffer datos)
    {
    	// 0. Comprobaciones previas
		if(datos==null || datos.Tam()<=0)
		   throw new IllegalArgumentException("Buffer no valido para ser copiado");
    	
		// 1. Creamos el buffer interno
		longitud=datos.Tam();
		Datos=new int[longitud];
    	
        // 2. Rellenamos el buffer con los datos proporcionados
		for(int i=0;i<longitud;i++)
            Datos[i]=datos.getByte(i);
     }
    
    
    
    /**
     * Crea un Buffer, contenedor de otros Buffers (solo usar en clases derivadas)
     */
    public Buffer()
    {
    	longitud=0;
    	Datos=null;
    }
    
    
    
    /**
     * Cambia el tamaño del buffer de datos
     * @param tam Nuevo tamaño del buffer
     * @throws IllegalArgumentException si el tamaño no es valido
     */
    public void Redimensiona(int tam)
	{
    	// 0. Comprobaciones previas
        if(tam<=0)
            throw new IllegalArgumentException("Tamaño demasiado pequeño para un buffer");
            
        // 1. Creamos el buffer interno
    	Datos=new int[tam];
    	longitud=tam;

    	// 2. Inicializamos el buffer a 0
    	for(int i=0;i<tam;i++)
    	   Datos[i]=0;
	}
    
    
    
    /**
     * Devuelve el tamaño del buffer
     * @return tamaño del buffer en bytes
     */
    public int Tam()
    {
    	return(longitud);
    }
    
    
    
    /**
     * Pone un determinado valor en un byte del buffer
     * @param numByte posicion del byte dentro del buffer
     * @param valor nuevo valor
     * @throws IllegalArgumentException si los valores de los parametros no son validos
     */
    public void setByte(int numByte, int valor)
    {
    	// 0. Condiciones de error
    	if(numByte<0 || numByte>=longitud)
    	   throw new IllegalArgumentException("Posicion no valida en el buffer");
    	else if(valor<0 || valor>255)
    	   throw new IllegalArgumentException("Valor de byte no valido "+valor);
    
    	// 1. Actualizamos el valor del byte indicado
        Datos[numByte]=valor;
    }
    
    
    
    /**
     * Pone un valor en la parte alta (bits 4567) del byte especificado
     * @param numByte posicion del byte dentro del buffer
     * @param valor nuevo valor (4 bits)
     * @throws IllegalArgumentException si alguno de los parametros tiene valor incorrecto
     */
    public void setByteH(int numByte, int valor)
    {
    	int valorAux;
    	
    	// 0. Comprobaciones previas
    	if(valor<0 || valor>15)
    	   throw new IllegalArgumentException("El valor del nibble no es correcto (de 0 a 15)");
    	
    	// 1. Actualizamos el valor
    	valorAux=getByte(numByte);    // obtenemos el byte
    	valorAux=valorAux&0x0F;       // reseteamos el byte alto
    	valorAux=valorAux|(valor<<4); // ponemos el valor en la parte alta 
    	setByte(numByte,valorAux);
    }
    

    
	/**
	 * Pone un valor en la parte baja del byte especificado
	 * @param numByte posicion del byte dentro del buffer
	 * @param valor nuevo valor (4 bits)
	 * @throws IllegalArgumentException si alguno de los paramentro tiene valor incorrecto 
	 */
	public void setByteL(int numByte, int valor)
	{
		int valorAux;
		
		// 0. Comprobaciones previas
		if(valor<0 || valor>15)
		   throw new IllegalArgumentException("El valor del nibble no es correcto (de 0 a 15)");
    	
		// 1. Actualizamos el valor
    	valorAux=getByte(numByte);    // obtenemos el byte
    	valorAux=valorAux&0xF0;       // reseteamos la parte baja
    	valorAux=valorAux|valor;      // ponemos el valor en la parte baja
		setByte(numByte,valorAux);
	}
	
	
	
	/**
	 * Pone un valor en el bit del byte especificado
	 * @param numByte posicion del byte dentro del buffer
	 * @param numBit bit del byte a modificar
	 * @param valor nuevo valor (4 bits)
	 * @throws IllegalArgumentException si los valores que se pasan no son validos
	 */
	public void setBit(int numByte, int numBit, int valor)
	{
		// 0. Comprobaciones previas
		if(valor<0 || valor>1 || numBit<0 || numBit>7)
		   throw new IllegalArgumentException("El valor del bit no es correcto (0 o 1)");
    	
		// 1. Actualizamos el valor
		int valorAux=getByte(numByte);
		
    	//    a) Poner a 1
    	if(valor==1)
    	{
    	    if(numBit==0 && (valorAux & 128)==0) valorAux+=128;
    	    if(numBit==1 && (valorAux &  64)==0) valorAux+=64;
    	    if(numBit==2 && (valorAux &  32)==0) valorAux+=32;
    	    if(numBit==3 && (valorAux &  16)==0) valorAux+=16;
    	    if(numBit==4 && (valorAux &   8)==0) valorAux+=8;
    	    if(numBit==5 && (valorAux &   4)==0) valorAux+=4;
    	    if(numBit==6 && (valorAux &   2)==0) valorAux+=2;
    	    if(numBit==7 && (valorAux &   1)==0) valorAux+=1;
    	    setByte(numByte,valorAux);
    	}
    	
    	//    b) Poner a 0
		else
		{	    
		    if(numBit==0 && (valorAux & 128)>0) valorAux-=128;
		    if(numBit==1 && (valorAux &  64)>0) valorAux-=64;
		    if(numBit==2 && (valorAux &  32)>0) valorAux-=32;
		    if(numBit==3 && (valorAux &  16)>0) valorAux-=16;
		    if(numBit==4 && (valorAux &   8)>0) valorAux-=8;
		    if(numBit==5 && (valorAux &   4)>0) valorAux-=4;
		    if(numBit==6 && (valorAux &   2)>0) valorAux-=2;
		    if(numBit==7 && (valorAux &   1)>0) valorAux-=1;
		    setByte(numByte,valorAux);
		}
	}
    
    
    
    /**
     * Recupera un byte del buffer
     * @param numByte posicion del byte a recuperar
     * @return Valor del byte solicitado
     * @throws IllegalArgumentException si la posicion del byte indicado no es valida
     */
    public int getByte(int numByte)
    {
    	// 0. Condiciones de error
    	if(numByte<0 || numByte>=longitud)
    	   throw new IllegalArgumentException("Numero de byte incorecto");
    	  
    	return(Datos[numByte]);
    }
    
    
    
    /**
     * Devuelve la parte alta del byte especificado
     * @param numByte Posicion del byte
     * @return Parte alta del byte indicado
     * @throws IllegalArgumentException si el byte indicado no existe
     */
    public int getByteH(int numByte)
    {
    	return(getByte(numByte)>>4);
    }
    
    
    
    /**
     * Devuelve un bit del byte que se indique
     * @param numByte Posicion que ocupa el byte
     * @param numBit posicion del Bit (01234567)
     * @return Valor del bit (0 o 1)
     * @throws IllegalArgumentException si el byte especificado no existe
     */
    public int getBit(int numByte,int numBit)
    {	
    	int valor=getByte(numByte);
    	
    	// Hay que hacer la correccion de 7-numBit para que el orden de bits parezca
    	// ser 01234567 en lugar del formato en que se guardan 76543210, es decir, que 
    	// parezca que el bit mas significativo (el primero por la izq.) sea el 0 y
    	// no el 7 (ver RFC 791, 'APPENDIX B:  Data Transmission Order')
    	if((valor&(1<<(7-numBit)))>0)
    	   return(1);
    	return(0);
    }
    
    
    
	/**
	 * Devuelve la parte baja del byte especificado
	 * @param numByte Posicion del byte
	 * @return Parte baja del byte
	 * @throws IllegalArgumentException si el byte indicado no existe
	 */
    public int getByteL(int numByte)
    {
    	return(getByte(numByte)&0x0F);
    }
    
    
    
    /**
     * Devuelve una cadena de texto informativa sobre buffer
     * @return Cadena de texto con el contenido buffer
     */
    public String Contenido()
    {
    	String contenido,valor,valorAux=null;
    	int i;
    	
    	contenido="----------------------------------------------------------------------------\n";
        contenido+="Datos sin filtrar\n";
    	for(i=0;i<Tam();i++)
        {
        	if(i%16 == 0)
        	{
        		if(valorAux!=null)
        			contenido+="    "+valorAux;
        		valorAux="";
        		contenido+="\n";
        		if(i<1000) contenido+="0";
        		if(i<100)  contenido+="0";
        		if(i<10)   contenido+="0";
        	    contenido+=Integer.toString(i)+":  ";
        	}
        	
        	valor=Integer.toHexString(getByte(i));
        	if(valor.length()==1)
        		contenido+=("0");
        	contenido+=valor+" ";
        	
        	valorAux+=imprimible(getByte(i));
        }
    	if(i%16!=0)
    	{
    		for(int j=0;j<16-(i%16);j++)
    			contenido+="   ";
    		contenido+="    "+valorAux;
    	}
    	contenido+="\n----------------------------------------------------------------------------";
    
        return(contenido);
    }
    
    
    
    /**
     * Devuelve el equivalente imprimible del entero
     * @param i entero (valor de 0 a 255)
     * @return caracter imprimible equivalente
     */
    protected char imprimible(int i)
    {
    	if(i>' ' && i<'Z'+1)
    		return((char)i);
	    return('.');
    }
}
