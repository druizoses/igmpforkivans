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

import Redes.*;

/**
 * Trama Ethernet
 */
public class TramaEthernet extends Buffer
{	
	/**
	 * Byte de preambulo (10101010)
	 */
	public static final int kPREAMBULO=170;
		
	/**
	 * Byte de relleno (00000000)
	 */
	public static final int kRELLENO=0;

	/**
	 * Numero de bytes de relleno -> en proceso de eliminacion...
	 */
	int relleno; 
	

	
    /**
     * Constructor
     * @param origen direccion MAC origen
     * @param destino direccion MAC destino
     * @param p datos contenidos
     * @param tipo tipo de los datos contenidos
     * @throws IllegalArgumentException Si el tipo de datos contenidos no son de tipo conocido
     */
    public TramaEthernet(DireccionEthernet origen,DireccionEthernet destino, Buffer p, int tipo) throws IllegalArgumentException
    {	       
		// Tamaño de la trama
		//if(p.Tam()<46)
		//	Redimensiona(26+46);
		//else
			Redimensiona(26+p.Tam());
		
        // Preambulo
        for(int i=0;i<8;i++)
            setByte(i,kPREAMBULO);

        // Direccion destino
        setDestino(destino);
        
        // Direccion origen
        setOrigen(origen); 

        // Tipo de datos contenidos
        setTipo(tipo);
        
        // Contenido
        for(int i=0;i<p.Tam();i++)
        	setByte(22+i,p.getByte(i));
        
        // Comprobamos si necesita bytes de relleno
    	//relleno=0;
    	//if(p.Tam()<46)
    	//{
    	//   relleno=46-p.Tam();
    	//   for(int i=0;i<relleno;i++)
    	//   	  setByte(22+p.Tam()+i,0);
    	//}
        relleno=0;
    }
    
    
    
    /**
     * Constructor
     * @param trama Buffer con los datos de la trama
     */
    public TramaEthernet(Buffer trama)
    {
        super(trama);
    }
    

    
    /**
     * Cambia la direccion MAC de destino
     * @param destino Direccion MAC de destino
     */
    public void setDestino(DireccionEthernet destino)
    {
		for(int i=8;i<=13;i++)
			setByte(i,destino.getByte(i-8));
    }
    
	
	
	/**
	 * Devuelve la direccion MAC de destino
	 * @return direccion MAC destino
	 */
	public DireccionEthernet getDestino()
	{
		int b0,b1,b2,b3,b4,b5;
		
		b0=getByte(8);
		b1=getByte(9);
		b2=getByte(10);
		b3=getByte(11);
		b4=getByte(12);
		b5=getByte(13);
		
		return(new DireccionEthernet(b0,b1,b2,b3,b4,b5));
	}

	
	
	/**
	 * Cambia la direccion MAC de origen
	 * @param origen Direccion MAC de origen
	 */
	public void setOrigen(DireccionEthernet origen)
	{
		for(int i=14;i<=19;i++)
			setByte(i,origen.getByte(i-14));
	}	
	
    
    
    /**
     * Devuelve la direccion MAC de origen
     * @return direccion MAC origen
     */
    public DireccionEthernet getOrigen()
    {
		int b0,b1,b2,b3,b4,b5;
		
		b0=getByte(14);
		b1=getByte(15);
		b2=getByte(16);
		b3=getByte(17);
		b4=getByte(18);
		b5=getByte(19);
		
		return(new DireccionEthernet(b0,b1,b2,b3,b4,b5));
    }
    
    
    
    /**
     * Cambia el tipo de datos contenidos (tipo de la carga util)
     * @param tipo Tipo
     * @throws IllegalArgumentException si el tipo no esta en el rango [0-65535]
     */
    public void setTipo(int tipo)
    {
    	if(tipo<0 || tipo>65535)
    		throw new IllegalArgumentException("El valor para el tipo no es correcto");
    	
    	// Byte 20 -> byte alto
    	setByte(20,tipo>>8);
    	
    	// Byte 21 -> byte bajo
    	setByte(21,tipo&0xFF);
    }
    
    
    
    /**
     * Devuelve el tipo de los datos contenidos
     * @return Tipo de los datos del campo de datos
     */
    public int getTipo()
    {
    	return((getByte(20)<<8)+getByte(21));
    }
    
    
    
    /**
     * Devuelve el numero de bytes de relleno
     * @return Numero de bytes de relleno o 0
     */
    public int NumBytesRelleno()
    {
    	return(relleno);
    }
    
    
    
    /**
     * Devuelve una cadena de texto informativa sobre la trama
     * @return Cadena de texto con el contenido de la trama
     */
    public String Contenido()
    {
    	String valor,valorAux=null;
    	String contenido;
    	int numlinea,i;
    	
        // Mostramos la cabecera basica, sin opciones
        contenido="----------------------------------------------------------------------------\n";
        contenido+="Trama Ethernet\n";
        contenido+="Preambulo: 8x"+Integer.toHexString(getByte(0))+"\n";
        contenido+="MAC Destino: "+getDestino().toString()+"\n";
        contenido+="MAC Origen: "+getOrigen().toString()+"\n";
        contenido+="Tipo: "+getTipo()+"\n";
        contenido+="-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -";
        int pos_inicial=22;
        for(i=pos_inicial;i<Tam();i++)
        {
        	if((i-pos_inicial)%16 == 0)
        	{
        		if(valorAux!=null)
        			contenido+="    "+valorAux;
        		valorAux="";
        		contenido+="\n";
        		numlinea=i-pos_inicial;
        		if(numlinea<1000) contenido+="0";
        		if(numlinea<100)  contenido+="0";
        		if(numlinea<10)   contenido+="0";
        	    contenido+=Integer.toString(i-pos_inicial)+":  ";
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
}
