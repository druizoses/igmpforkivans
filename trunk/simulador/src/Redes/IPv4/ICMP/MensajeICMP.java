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
 * Creado el 01-ene-2004
 */
 
package Redes.IPv4.ICMP;

import Redes.Buffer;
import Redes.IPv4.DatagramaIPv4;
import Redes.IPv4.DireccionIPv4;

/**
 * Mensaje ICMP
 */
public class MensajeICMP extends Buffer 
{   
	/**
	 * Codigo 'identificador' para los mensajes 'Echo'
	 */
	private static int numEcho=0;
   
    
    
    /**
     * Constructor
     * @param tipo Tipo de mensaje ICMP
     * @param codigo Codigo del mensaje
     * @param datagrama Datagrama que provoca el envio del mensaje ICMP
     */
    public MensajeICMP(int tipo, int codigo, DatagramaIPv4 datagrama)
    {
        super();
        
        if(datagrama!=null)
        {
            // 1. Creamos el mensaje segun el tipo especificado
            switch(tipo)
            {
                // Destino inalcanzable
                case 3:
                {
                    DestinationUnreachable(codigo,datagrama);
                    break;
                }
                
                // Time Exceeded
                case 11:
                {
                    TimeExceeded(codigo,datagrama);
                    break;
                }
            }
        }
        
        // 2. Si se ha producido algun error en el proceso, lanzamos una excepcion
        if(datagrama==null)
        {
            throw new IllegalArgumentException("Error creando mensaje ICMP");
        }
    }

    
    
    /**
     * Constructor de copia
     * @param buffer Buffer a copiar
     */
    public MensajeICMP(Buffer buffer)
    {
        super(buffer);
    }
    
    
    
    /**
     * Crea un mensaje ICMP de tipo y codigo dados, pero rellenando el campo de datos
     * con el buffer especificado
     * @param tipo Tipo de mensaje
     * @param codigo Codigo del mensaje
     * @param buffer Buffer de datos
     */
    public MensajeICMP(Buffer buffer, int tipo, int codigo)
	{
        super();
        
        if(buffer!=null)
        {
            // 1. Creamos el mensaje segun el tipo especificado
            switch(tipo)
            {
                // Echo reply            	
                case 0:
                {
                	// No se puede responder a algo que no sea un mensaje ICMP
                	if(buffer instanceof MensajeICMP)
                	{
                       numEcho++;
                	   
                       // no usamos el codigo, ya que solo puede valer 0 (implicito)
                       EchoReply((MensajeICMP)buffer);
                	}
                	break;                 	
                }
                
                // Echo
            	case 8:
                {
                    Echo(buffer);
                    break;
                }
            }
        }
    }
    

    
    /**
     * Constructor para mensajes de tipo ICMP Redirect, donde hace falta la direccion del Gateway
     * @param tipo Tipo de mensaje ICMP
     * @param codigo Codigo del mensaje
     * @param datagrama Datagrama que provoca el envio del mensaje ICMP
     * @param direccion Direccion IP del gateway
     */
    public MensajeICMP(int tipo, int codigo, DatagramaIPv4 datagrama, DireccionIPv4 direccion)
    {
        super();
        
        if(datagrama!=null)
        {
            // 1. Creamos el mensaje segun el tipo especificado
            switch(tipo)
            {
                // Redirect            	
                case 5:
                {
                	Redirect(codigo,datagrama,direccion);
                }

                default:
                {
                	System.out.println("Mensaje ICMP no creado");
                	System.out.flush();
                }
            }
        }    	
    }
    
    
    
    /**
     * Pone un valor en el campo 'tipo' de la cabecera ICMP
     * @param tipo Tipo de mensaje
     */
    public void setType(int tipo)
    {       
        setByte(0,tipo);
    }
    
    
    
    /**
     * Recupera el valor del campo 'tipo'
     * @return Tipo de mensaje ICMP
     */
    public int getType()
    {
        return(getByte(0));
    }
    
    
    
    /**
     * Pone un valor en el campo 'codigo' de la cabecera ICMP
     * @param codigo Codigo del mensaje
     */
    public void setCode(int codigo)
    {
        setByte(1,codigo);
    }

    
    
    /**
     * Devuelve el valor del campo codigo de la cabecera
     * @return Codigo del mensaje ICMP
     */
    public int getCode()
    {
        return(getByte(1));
    }
    
    
    
    /**
     * Pone un valor en el campo 'checksum' (suma de comprobacion)
     * @param suma Suma de comprobacion
     */
    public void setChecksum(int suma)
    {
        if(suma<0 || suma>65535)
            throw new IllegalArgumentException("El valor del campo suma de comprobacion no es valido");
        
        //Byte 2 -> byte alto
        setByte(2,(suma>>8)&0x00FF);
        
        //Byte 3 -> byte bajo
        setByte(3,suma&0x00FF);    
    }
    
    
    
    /**
     * Devuelve la el valor de la suma de comprobacion 
     * @return Suma de comprobacion
     */
    public int getChecksum()
    {
        return((getByte(2)<<8)+getByte(3));
    }
    

    
    /**
     * Calcula la suma de comprobacion
     * @return Suma de comprobacion
     */
    public int CalculaSumaDeComprobacion()
    { 
        return(0);
    }    
    
    
    
    /**
     * Devuelve la descripcion de un tipo de mensaje ICMP
     * @param tipo Tipo de mensaje
     * @param codigo Codigo del mensaje
     * @return Cadena de texto explicativa del proposito del mensaje ICMP
     */
    public static String Descripcion(int tipo,int codigo)
    {
    	switch(tipo)
		{
    	    // Echo Reply
    	    case 0:
    	    {
    	    	if(codigo==0)
    	    		return("Respuesta Eco");
    	    	break;
    	    }
    	
    	    // Destination unreachable
		    case 3:
    	    {
    	    	switch(codigo)
				{
	                case 0: { return("Destino inalcanzable, red inalcanzable"); }
	                case 1: { return("Destino inalcanzable, host inalcanzable"); }
	                case 2: { return("Destino inalcanzable, protocolo inalcanzable"); }
	                case 3: { return("Destino inalcanzable, puerto inalcanzable"); }
	                case 4: { return("Destino inalcanzable, se necesita fragmentar pero el bit DF esta activo"); }
	                case 5: { return("Destino inalcanzable, fallo en enrutamiento de origen"); }
				}
    	        break;
    	    }

    	    // Redirect    
    	    case 5:
    	    {
                switch(codigo)
                {
                    case 0: { return("Redireccion, enviar a la red especificada"); }
                    case 1: { return("Redireccion, enviar al host especificado"); }
                    case 2: { return("Redireccion, enviar a la red y con el TOS especificados"); }
                    case 3: { return("Redireccion, enviar al host con y con el TOS especificados"); }
                }
                break;
    	    }
    	    
    	    // Echo
    	    case 8:
    	    {
    	    	if(codigo==0)
    	    		return("Eco");
    	    	break;
    	    }
    	    
    	    // Time Excedeed
    	    case 11:
    	    {
    	        switch(codigo)
		        {
		            case 0: { return("Tiempo excedido, TTL excedido"); }
		            case 1: { return("Tiempo excedido, plazo de reensamblado excedido"); }
		        }
    	        break;
    	    }
		}
    	
    	return("Tipo de mensaje ICMP y codigo desconocido");
    }
    
    
    
    
    
    /* ---------------------- DESTINATION UNREACHABLE ------------------------------ */
    /**
     * Crea un mensaje de tipo 'Destino Inalcanzable' con el codigo indicado
     * @param codigo Codigo del mensaje
     * @param datagrama Datagrama que provoca el envio del mensaje ICMP
     * @return Cierto, si se ha produdido algun error
     */
    private boolean DestinationUnreachable(int codigo,DatagramaIPv4 datagrama)
    {
        boolean error=false;

        // 0. Inicializamos el buffer
        Redimensiona(8+datagrama.getIHL()*4+8);
        
        // 1. Tipo
        setType(3);
         
        // 2. Codigo
        switch(codigo)
        {
            case 0: { setCode(0); break; }
            case 1: { setCode(1); break; }
            case 2: { setCode(2); break; }
            case 3: { setCode(3); break; }
            case 4: { setCode(4); break; }
            case 5: { setCode(5); break; }
             
            // codigo no valido para mensaje 'destino inalcanzable'
            default: { error=true; break; }
        }
        
        // 3. Datos
        if(error==false)
            for(int i=0;i<datagrama.getIHL()*4+8;i++)
                setByte(8+i,datagrama.getByte(i));
               
        // 4. Devolvemos el estado
        return(error);
    }


    
    /* -------------------------------- REDIRECT ------------------------------ */
    /**
     * Crea un mensaje de tipo 'Redirect' con el codigo indicado
     * @param codigo Codigo del mensaje
     * @param datagrama Datagrama que provoca el envio del mensaje ICMP
     * @param gateway Direccion del gateway que se deberia usar en los siguientes envios
     * @return Cierto, si se ha produdido algun error
     */
    private boolean Redirect(int codigo,DatagramaIPv4 datagrama, DireccionIPv4 gateway)
    {
        boolean error=false;

        if(datagrama!=null && gateway!=null)
        {
            // 0. Inicializamos el buffer
            Redimensiona(8+datagrama.getIHL()*4+8);
        
            // 1. Tipo
            setType(5);
         
            // 2. Codigo
            switch(codigo)
            {
                case 0: { setCode(0); break; }
                case 1: { setCode(1); break; }
                case 2: { setCode(2); break; }
                case 3: { setCode(3); break; }
             
                // codigo no valido para mensaje 'redireccion'
                default: { error=true; break; }
            }
        
            // 3. Gateway
            setRedirectGateway(gateway);
            
            // 4. Datos
            if(error==false)
                for(int i=0;i<datagrama.getIHL()*4+8;i++)
                    setByte(8+i,datagrama.getByte(i));
        }
        else
        {
        	error=false;
        }
        
        // 5. Devolvemos el estado
        return(error);
    }
    
    
    
    /**
     * Pone la direccion especificada en el campo reservado para el gateway
     * en un mensaje ICMP Redirect
     * @param gateway Direccion del gateway
     */
    public void setRedirectGateway(DireccionIPv4 gateway)
    {
    	if(gateway!=null && getType()==5)
    	{
            setByte(4,gateway.getByte(0));
            setByte(5,gateway.getByte(1));
            setByte(6,gateway.getByte(2));
            setByte(7,gateway.getByte(3));    		
    	}
    	else
    	{
    		System.out.println("Error, uso no valido de MensajeICMP::setRedirectGateway()");
    	}
    }
    
    
    
    /**
     * Devuelve la direccion del gateway en un mensaje ICMP Redirect
     * @return Direccion del campo 'gateway' del mensaje ICMP
     */
    DireccionIPv4 getRedirectGateway()
    {
    	DireccionIPv4 gateway=null;
    	
    	if(getType()==5)
    		gateway=new DireccionIPv4(getByte(4),getByte(5),getByte(6),getByte(7));
    	
    	return(gateway);
    }
    
    
    
    /* ------------------------- TIME EXCEEDED --------------------------------- */
    /**
     * Crea un mensaje de tipo 'Time Exceeded' con el codigo indicado
     * @param codigo Codigo del mensaje
     * @param datagrama Datagrama que provoca el envio del mensaje ICMP
     * @return Cierto, si se ha produdido algun error
     */
    private boolean TimeExceeded(int codigo,DatagramaIPv4 datagrama)
    {
        boolean error=false;

        // 0. Inicializamos el buffer
        Redimensiona(8+datagrama.getIHL()*4+8);
        
        // 1. Tipo
        setType(11);
        
        // 2. Codigo
        switch(codigo)
        {
            case 0: { setCode(0); break; }
            case 1: { setCode(1); break; }
        
            // codigo no valido para mensaje 'plazo de tiempo superado'
            default: { error=true; break; }
        }
        
        // 3. Datos
        if(error==false)
            for(int i=0;i<datagrama.getIHL()*4+8;i++)
                setByte(8+i,datagrama.getByte(i));
        
        // 4. Devolvemos el estado
        return(error);
    }


    
    /* ----------------------------- ECHO ------------------------------------ */
    /**
     * Crea un mensaje de tipo 'Echo'
     * @param buffer Datos del cuerpo del mensaje
     * @return Cierto, si se ha produdido algun error (siempre devuelve false)
     */
    private boolean Echo(Buffer buffer)
    {
        // 0. Inicializamos el buffer
        Redimensiona(8+buffer.Tam());
        
        // 1. Tipo
        setType(8);
        
        // 2. Codigo
        setCode(0);
        
        // 3. Identificador
        setByte(4,(numEcho>>8)&0x00FF);
        setByte(5,numEcho&0x00FF);  

        // 4. Numero de secuencia (como no se permite el envio de varios ICMP echo en rafaga,
        //    siempre valdra 0)
        setByte(6,0);
        setByte(7,0);
        
        // 5. Datos (carga)
        for(int i=0;i<buffer.Tam();i++)
            setByte(8+i,buffer.getByte(i));
        
        // 6. Devolvemos el estado
        return(false);
    }

    

    /* ----------------------------- ECHO REPLY------------------------------------ */
    /**
     * Crea un mensaje de tipo 'Echo Reply'
     * @param buffer Mensaje ICMP al que vamos a responder
     * @return Cierto, si se ha produdido algun error (siempre devuelve false)
     */
    private boolean EchoReply(MensajeICMP buffer)
    {
        // 0. Inicializamos el buffer
        Redimensiona(8+buffer.Tam()-8);
        
        // 1. Tipo
        setType(0);

        // 2. Codigo
        setCode(0);

        // 3. Identificador
        setByte(4,buffer.getByte(4));
        setByte(5,buffer.getByte(5));
        
        // 4. Numero de secuencia
        setByte(6,buffer.getByte(6));
        setByte(7,buffer.getByte(7));
        
        // 5. Copiamos el campo de datos
        for(int i=8;i<buffer.Tam();i++)
           setByte(i,buffer.getByte(i));
          
        // 6. Devolvemos el estado
        return(false);
    }
}

