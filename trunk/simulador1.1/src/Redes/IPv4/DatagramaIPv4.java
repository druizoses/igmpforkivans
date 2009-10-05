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

import Redes.Buffer;

/**
 * Datagrama IP
 */
public class DatagramaIPv4 extends Buffer
{
	/**
	 * Atributo de la clase, que se utiliza para tener identificadores
	 * unicos para cada datagrama
	 */
	private static int ID=0;

	// -------------------------------------------------------------------------
	// Opciones IP, no implementado de momento, aunque hay 5 opciones definidas
	// -------------------------------------------------------------------------
	
	
	
	/**
	 * Constructor
	 * @param origen direccion IPv4 de origen
	 * @param destino direccion IPv4 de destino
	 * @param datos datos contenidos
	 * @throws IllegalArgumentException si alguno de los valores de entrada es nulo
	 */
	public DatagramaIPv4(DireccionIPv4 origen,DireccionIPv4 destino,Buffer datos)
	{
        // Comprobacion inicial de parametros
	    if(origen==null || destino==null || datos==null || datos.Tam()>65535-20)
		    throw new IllegalArgumentException("Valores no validos para crear un datagrama");
		
		// Redimensionamos el buffer
		Redimensiona(20+datos.Tam());
		
		// Modificamos el de identificadores 
		ID++;
		
		// Version IP
		setVersion(4);

		// Longitud de la cabecera IP contada en cantidades de 32 bits, como no se ha implementado los campos de
		// opciones tenemos que vale 20bytes/32bits=5		
        setIHL(5);
		
        // Campo TOS
        setPrecedence(0,0,0);     //precedencia
        setD(0);                  //delay
        setT(0);                  //throughput
        setR(0);                  //relibility
		
		// Longitud total del datagrama
		setTotalLength(20+datos.Tam()); //campo opciones no implementado
		
		// Identificador de datagrama
		setID(ID);
		
		// Bit Don't Fragment
        setDF(0);
        
        // Bit More Fragments
	    setMF(0);
	    
	    // Desplazamiento del fragmento en el datagrama
	    setFragmentOffset(0);
	    
	    // Time To Live
		setTTL(128);
		
		// Campo Protocol
		setProtocol(0);
		
		// Direccion IP del origen
		setOrigen(origen);
		
		// Direccion IP del destino
		setDestino(destino);

		// Calculamos la suma de comprobacion
		// (calculo no implementado -> Checksum=0x0000)
        setHeaderChecksum(CalculaSumaDeComprobacion());
        
        // Finalmente copiamos los datos
        for(int i=0;i<datos.Tam();i++)
        	setByte(getIHL()*4+i,datos.getByte(i));
	}
	
	
	
	/**
	 * Constructor
	 * @param origen Direccion origen del datagrama
	 * @param destino Direccion de destino
	 * @param datos Carga de datos del datagrama
	 */
	public DatagramaIPv4(String origen, String destino, Buffer datos)
	{
		this(new DireccionIPv4(origen),new DireccionIPv4(destino),datos);
	}
	
	
	
	/**
	 * Constructor
	 * Crea un datagrama. Su informacion util se limita
	 * a los datos de la cabecera. Este tipo de 'pseudo-datagrama' se usa solo
	 * con propositos de reensamblado de fragmentos a traves de la clase derivada
	 * CabeceraIPv4.
	 * @param tam Tamaño de la cabecera en bytes
	 * @throws IllegalArgumentException si el tamaño especificado no es valido
	 */
	protected DatagramaIPv4(int tam)
	{
		super(tam);
	}
	
	
	
	/**
	 * Constructor
	 * @param datagrama Datagrama del que se copiara la cabecera
	 * @param datos Bytes del campo de datos
	 * @throws IllegalArgumentException si alguno de los valores de entrada es nulo
	 */
	public DatagramaIPv4(DatagramaIPv4 datagrama, Buffer datos)
	{
		// 0. Comprobaciones previas
	    if(datagrama==null || datos==null || datos.Tam()>65535-20)
	        throw new IllegalArgumentException("Valores nulos no validos para crear un datagrama");
		
	    // 1. Redimensionamos el datagrama
		Redimensiona(datagrama.getIHL()*4+datos.Tam());
		
		// 2. Copiamos la cabecera
		for(int i=0;i<datagrama.getIHL()*4;i++)
			setByte(i,datagrama.getByte(i));
		
		// 3. Copiamos los datos
		for(int i=0;i<datos.Tam();i++)
		    setByte(i+getIHL()*4,datos.getByte(i));
	}
	
	
	
	/**
	 * Constructor, crea un paquete copiando bit a bit el contenido de otro
	 * @param buffer Paquete que se quiere copiar
	 */
	public DatagramaIPv4(Buffer buffer)
	{
	    super(buffer);
	}
	
	
	
	/**
	 * Pone un valor en el campo Version de la cabecera IP
	 * @param numVersion Numero de version, 4 para IPv4
	 * @throws IllegalArgumentException si el numero de version no es valido
	 */
	public void setVersion(int numVersion)
	{
		// 0. Comprobaciones previas
        if(numVersion<0 || numVersion>15)
           throw new IllegalArgumentException("Version IP no valida");
		
        // 1. Actualizacion del campo 'version'
		setByteH(0,numVersion);
	}
	
	
	
	/**
	 * Devuelve el contenido del campo Version de la cabecera IP
	 * @return Version de IP usada en el datagrama
	 */
	public int getVersion()
	{
		return(getByteH(0));
	}
	
	
	
	/**
	 * Pone un valor en el campo IHL (Internet Header Length/Longitud de la cabecera IP)
	 * @param longitud Longitud de la cabecera IP
	 * @throws IllegalArgumentException si la longitud especificada no es valida
	 */
	public void setIHL(int longitud)
	{
		// 0. Comprobaciones previas
		if(longitud<0 || longitud>15)
		   throw new IllegalArgumentException("Longitud de la cabercera IP no valida");
		   
		// 1. Actualizacion del campo
		setByteL(0,longitud);
	}
	
	
	
	/**
	 * Devuelve el la longitud total de la cabecera IP
	 * @return Longitud de la cabecera
	 */
	public int getIHL()
	{
		return(getByteL(0));
	}
	
	
	
	/**
	 * Pone un valor en el campo TOS.Precedence (012)
	 * @param p0 valor para el bit 0
	 * @param p1 valor para el bit 1
	 * @param p2 valor para el bit 2
	 * @throws IllegalArgumentException si algun bit especificado no es correcto
	 */
	public void setPrecedence(int p0, int p1, int p2)
	{
		// 0. Comprobaciones previas
		if(p0<0 ||p0>1 || p1<0 || p1>1 || p2<0 || p2>1)
		   throw new IllegalArgumentException("Valor no valido para el campo TOS.Precedence");
        
		// 1. Actualizacion del campo
        setBit(1,0,p0);
        setBit(1,1,p1);
        setBit(1,2,p2);		
	}

	
	
	/**
	 * Devuelve el bit 0 del campo TOS.Precedence
	 * @return Bit 0
	 */
	public int getPrecedence0()
	{
		return(getBit(1,0));
	}
	
	
	
	/**
	 * Devuelve el bit 1 del campo TOS.Precedence
	 * @return Bit 1
	 */
	public int getPrecedence1()
	{
		return(getBit(1,1));
	}
	
	
	
	/**
	 * Devuelve el bit 2 del campo TOS.Precedence
	 * @return Bit 2
	 */
	public int getPrecedence2()
	{
		return(getBit(1,2));
	}
	
    
    
    /**
     * Pone un valor en el campo TOS.TypeOfService.Delay (retardo)
     * @param D Valor
     * @throws IllegalArgumentException si el valor del bit no es valido
     */
	public void setD(int D)
	{
		// 0. Comprobaciones previas
		if(D<0 || D>1)
		   throw new IllegalArgumentException("Valor no valido para el campo TOS.tos.Delay");
	
		// 1. Actualizacion del campo
	    setBit(1,3,D);
	}

	
	
	/**
	 * Devuelve el valor del campo TOS.TypeOfService.Delay
	 * @return Valor del campo
	 */
	public int getD()
	{
		return(getBit(1,3));
	}
	

	
	/**
	 * Pone un valor en el campo TOS.TypeOfService.Throughput (rendimiento)
	 * @param T Valor
	 * @throws IllegalArgumentException si el valor del bit no es correcto
	 */
	public void setT(int T)
	{
		// 0. Comprobaciones previas
		if(T<0 || T>1)
		   throw new IllegalArgumentException("Valor no valido para el campo TOS.tos.Throughput");
	
		// 1. Actualizacion del campo
		setBit(1,4,T);
	}
	
	
	
	/**
	 * Devuelve el valor del campo TOS.TypeOfService.Throughput
	 * @return Valor del campo
	 */
	public int getT()
	{
		return(getBit(1,4));
	}
	
	
	
	/**
	 * Pone un valor en el campo TOS.TypeOfService.Reliability (confiabilidad)
	 * @param R Valor
	 * @throws IllegalArgumentException si el valor del bit no es valido
	 */
	public void setR(int R)
	{
		// 0. Comprobaciones previas
		if(R<0 || R>1)
		   throw new IllegalArgumentException("Valor no valido para el campo TOS.tos.Reliability");
	
		// 1. Actualizacion del campo
		setBit(1,5,R);
	}	

	
	
	/**
	 * Devuelve el calor del campo TOS.TypeOfService.Reliability
	 * @return Valor del campo
	 */
	public int getR()
	{
		return(getBit(1,5));
	}

	
	
	/**
	 * Cambia el tamaño del campo 'Longitud total' del datagrama
	 * @param longitud Longitud total del datagrama
	 * @throws IllegalArgumentException si el valor de longitud no es correcto
	 */
	public void setTotalLength(int longitud)
	{
		// 0. Comprobaciones previas
		if(longitud<0 || longitud>65535)
		   throw new IllegalArgumentException("La longitud total no es valida");
		   
		//Byte 2 -> byte alto
		setByte(2,(longitud>>8)&0x00FF);
		
		//Byte 3 -> byte bajo
		setByte(3,longitud&0x00FF);
	}

	
	
	/**
	 * Devuelve el valor del campo Total Length
	 * @return Longitud total del datagrama en bytes
	 */
	public int getTotalLength()
	{
		return((getByte(2)<<8)+getByte(3));
	}
	
	
	
	/**
	 * Cambia el valor del campo ID
	 * @param id Identificador de grupo de fragmentos (datagrama)
	 * @throws IllegalArgumentException si el valor de identificador no es valido
	 */
	public void setID(int id)
	{
		// 0. Comprobaciones previas
		if(id<0 || id>65535)
		   throw new IllegalArgumentException("El valor del campo id no es valido");
		
		//Byte 4 -> byte alto
		setByte(4,(id>>8)&0x00FF);
		
		//Byte 5 -> byte bajo
		setByte(5,id&0x00FF);
	}    
    
	
	
	/**
	 * Devuelve el identificador de datagrama
	 * @return ID del datagrama
	 */
	public int getID()
	{
		return((getByte(4)<<8)+getByte(5));
	}

	
	
	/**
	 * Cambia el valor del bit 'Don't Fragment'
	 * @param DF Valor del bit
	 * @throws IllegalArgumentException si el valor del bit no es valido
	 */
	public void setDF(int DF)
	{
		// 0. Comprobaciones previas
		if(DF<0 || DF>1)
		   throw new IllegalArgumentException("Valor no valido para el campo Dont't Fragment");
	
		// 1. Actualizacion del campo
		setBit(6,1,DF);
	}

	
	
	/**
	 * Devuelve el valor el bit Don't Fragment
	 * @return Valor del bit DF
	 */
	public int getDF()
	{
		return(getBit(6,1));
	}

	
	
	/**
	 * Cambia el valor del bit 'More Fragments'
	 * @param MF Valor del bit
	 * @throws IllegalArgumentException si el valor del bit no es valido
	 */
	public void setMF(int MF)
	{
		// 0. Comprobaciones previas
		if(MF<0 || MF>1)
		   throw new IllegalArgumentException("Valor no valido para el campo More Fragments");
	
		// 1. Actualizacion del campo
		setBit(6,2,MF);
	}	
	
	
	
	/**
	 * Devuelve el valor del bit 'More Fragments'
	 * @return Valor del bit MF
	 */
	public int getMF()
	{
		return(getBit(6,2));
	}
	
	
	
	/**
	 * Pone un valor en el campo 'Fragment Offset' (Desplazamiento del fragmento)
	 * (este desplazamiento se cuenta en unidades de 8 bytes)
	 * @param desplazamiento Desplazamiento del fragmento dentro de datagrama original
	 * @throws IllegalArgumentException si el valor es incorrecto
	 */
	public void setFragmentOffset(int desplazamiento)
	{
		// 0. Comprobaciones previas
		if(desplazamiento<0 || desplazamiento>8191)
	       throw new IllegalArgumentException("El valor del campo 'Fragment Offset' debe esta en [0-8192]");
	
	    // Byte 6, bits 34567 -> parte alta
	    if((desplazamiento&4096)>0) setBit(6,3,1); else setBit(6,3,0);
	    if((desplazamiento&2048)>0) setBit(6,4,1); else setBit(6,4,0);
	    if((desplazamiento&1024)>0) setBit(6,5,1); else setBit(6,5,0);
	    if((desplazamiento&512)>0)  setBit(6,6,1); else setBit(6,6,0);
	    if((desplazamiento&256)>0)  setBit(6,7,1); else setBit(6,7,0);
	   
	    // Byte 7 -> parte baja
	    setByte(7,desplazamiento&0x00FF);
	}
	
	
	
	/**
	 * Devuelve el desplazamiento del fragmento dentro del datagrama
	 * @return Desplazamiento en unidades de 8 bytes
	 */
	public int getFragmentOffset()
	{
		int desplazamiento=0;
		
		desplazamiento+=getBit(6,3)*4096;
		desplazamiento+=getBit(6,4)*2048;
		desplazamiento+=getBit(6,5)*1024;
		desplazamiento+=getBit(6,6)*512;
		desplazamiento+=getBit(6,7)*256;
		desplazamiento+=getByte(7);
		
		return(desplazamiento);
	}

	
	
	/**
	 * Cambia el valor del campo TTL
	 * @param ttl Tiempo de vida del datagrama
	 * @throws IllegalArgumentException si el valor esta fuera del rango [0,255]
	 */
	public void setTTL(int ttl)
	{
		// 0. Comprobamos el parametro
		if(ttl<0 || ttl>255)
		   throw new IllegalArgumentException("El valor del campo ttl no es valido");
		   
		// 1. Actualizamos el campo
		setByte(8,ttl);
	}

	
	
	/**
	 * Devuelve el tiempo de vida del datagrama
	 * @return Valor del campo TTL
	 */
	public int getTTL()
	{
		return(getByte(8));
	}
	
	
	
	/**
	 * Cambia el valor del campo 'protocol' 
	 * @param protocolo Protocolo del nivel de transporte al que pertenece el datagrama
	 * @throws IllegalArgumentException si el valor esta fuera del rango [0,255]
	 */
	public void setProtocol(int protocolo)
	{
		// 0. Comprobamos el parametro 
		if(protocolo<0 || protocolo>255)
		   throw new IllegalArgumentException("El protocolo no es valido");
		
		// 1. Actualizamos el campo
		setByte(9,protocolo);
	}
	
	
	
	/**
	 * Devuelve el protocolo usado por la porcion de datos del datagrama
	 * @return Protocolo de los datos
	 */
	public int getProtocol()
	{
		return(getByte(9));
	}

	
	
	/**
	 * Calcula la suma de comprobacion de la cabecera
	 * @return Suma de comprobacion
	 */
	public int CalculaSumaDeComprobacion()
	{ 
	    /*int sum=0;
	    int longitud=getIHL()*4;
	    
	    for (int i=0; i<longitud-1; i=i+2)
	    {
	        sum += getByte(i)<<8+getByte(i+1);
	        if (sum > 0xffff)
	        {
	            sum++;
	            sum &= 0xffff;
	        }
	    }
	    //System.out.println("Checksum: 0x"+Integer.toHexString(sum));
	    
		return(sum);*/
	    return(0);
	}
	
	
	
	/**
	 * Cambia el valor del campo 'header checksum'
	 * @param suma Nuevo valor
	 * @throws IllegalArgumentException si el valor de la suma esta fuera de rango
	 */
	public void setHeaderChecksum(int suma)
	{
		// 0. Comprobaciones previas
		if(suma<0 || suma>65535)
		   throw new IllegalArgumentException("El valor del campo suma de comprobacion no es valido");
		
		//Byte 10 -> byte alto
		setByte(10,(suma>>8)&0x00FF);
		
		//Byte 11 -> byte bajo
		setByte(11,suma&0x00FF);
	}
	
	
	
	/**
	 * Devuelve la el valor de la suma de comprobacion de la cabecera IP
	 * @return Suma de comprobacion de la cabecera (Header Checksum)
	 */
    public int getHeaderChecksum()
    {
    	return((getByte(10)<<8)+getByte(11));
    }
    
    
    
	/**
	 * Cambia la direccion IP de origen de un datagrama
	 * @param origen Direccion IPv4 de origen del datagrama
	 */
	public void setOrigen(DireccionIPv4 origen)
	{
		setByte(12,origen.getByte(0));
		setByte(13,origen.getByte(1));
		setByte(14,origen.getByte(2));
		setByte(15,origen.getByte(3));
	}
	
	
	
	/**
	 * Devuelve la direccion IP del origen
	 * @return Direccion IPv4 del equipo que origino el datagrama
	 * @throws IllegalArgumentException si los bytes de la direccion de origen de la
	 *         cabecera son incorrectos
	 */
	public DireccionIPv4 getOrigen()
	{
		return(new DireccionIPv4(getByte(12),getByte(13),getByte(14),getByte(15)));
	}
	
	
	
	/**
	 * Cambia la direccion IP de destino de un datagrama
	 * @param destino Direccion IPv4 de destino del datagrama
	 */	
	public void setDestino(DireccionIPv4 destino)
	{
		setByte(16,destino.getByte(0));
		setByte(17,destino.getByte(1));
		setByte(18,destino.getByte(2));
		setByte(19,destino.getByte(3));
	}

	
	
	/**
	 * Devuelve la direccion IP del equipo al que va dirigido el datagrama
	 * @return Direccion IPv4 del destino
	 * @throws IllegalArgumentException si los bytes de la cabecera asociados a la
	 *         direccion de destino son incorrectos
	 */
    public DireccionIPv4 getDestino()
    {
    	return(new DireccionIPv4(getByte(16),getByte(17),getByte(18),getByte(19)));
    }
    
    
    
    /**
     * Comprueba el datagrama
     * @return Cierto si el datagrama es correcto
     */
    public boolean EsCorrecto()
    {
        boolean correcto=true;
        
        try
        {
            // 0. Comprobamos que el tamaño del buffer cumpla con el minimo minimo
            if(Tam()<28)
                correcto=false;
                
            // 1. Comprobamos la suma de control
            /* ... algoritmo ... */
            
            // 2. Direccion de destino valida
            else if(getDestino()==null)    // si hay error, excepcion
                correcto=false;
                
            // 3. Version 4
            if(getVersion()!=4)
                correcto=false;
                    
            // 4. Longitud de la cabecera
            if(getIHL()*4<20 || getIHL()*4>60)
                correcto=false;
            
            // 5. Longitud total del datagrama
            if(getTotalLength()<getIHL()*4+8 || getTotalLength()>65535)
                correcto=false;
            
            // 6. Comprobacion del protocolo
            //...siempre correcto...
        }
        catch(Exception e)
        {
            correcto=false;
        }
        
        return(correcto);
    }
    
    
    /**
     * Devuelve una cadena de texto informativa sobre el datagrama
     * @return Cadena de texto con el contenido del datagrama
     */
    public String Contenido()
    {
    	String valor,valorAux=null;
    	String contenido;
    	int numlinea,i;
    	
        // Mostramos la cabecera basica, sin opciones
        contenido="----------------------------------------------------------------------------\n";
        contenido+="Datagrama IPv4\n";
        contenido+="Version: "+getVersion()+"\n";
        contenido+="IHL: "+getIHL()+" ("+(getIHL()*4)+" bytes)"+"\n";
        contenido+="TOS: Precedence="+getPrecedence0()+getPrecedence1()+getPrecedence2()+" D="+getD()+" T="+getT()+" R="+getR()+" 00="+getBit(1,6)+getBit(1,7)+"\n";
        contenido+="Total Length: "+getTotalLength()+" bytes\n";
        contenido+="ID: "+getID()+"\n";
        contenido+="Flags: 0="+getBit(6,0)+" DF="+getDF()+" MF="+getMF()+"\n";
        contenido+="Fragment Offset: "+getFragmentOffset()+" [primer byte: "+getFragmentOffset()*8+"]"+"\n";
        contenido+="TTL: "+getTTL()+"\n";
        contenido+="Protocolo: "+getProtocol()+"\n";
        contenido+="Header Checksum: "+Integer.toHexString(getHeaderChecksum())+"\n";
        contenido+="IP Origen: "+getOrigen().getIP()+"\n";
        contenido+="IP Destino: "+getDestino().getIP()+"\n";
        contenido+="-  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -  -";
        int pos_inicial=getIHL()*4;
        for(i=pos_inicial;i<getTotalLength();i++)
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
