package Proyecto;

import Redes.*;
import Redes.IPv4.*;
import Equipos.*;
import Redes.IPv4.ICMP.*;

/**
 * M�s de 35 ejemplos del uso de la API de Simulaci�n de redes IP
 */
public class Ejemplos
{
	/**
	 * Programa principal
	 * @param args Argumentos de la linea de comandos
	 */
	public static void main(String[] args) 
	{
		System.out.println("Iniciando simulacion");
		
		// 0. Registramos los componentes
		RegistrarComponentes();
		Simulacion4();
		/*
		// 1. Una red ethernet con solo un ordenador, que se envia a si mismo un
		//    datagrama sin fragmentacion
		Simulacion1();
	
		// 1b. Con un hub
		Simulacion1B();

		// 1c. Con switch
		Simulacion1C();
		
		// 2. Una red ethernet con solo un ordenador, que envia un datagrama a un
		//    ordenador inexistente
		Simulacion2();
		
		// 2b. Con hub
		Simulacion2B();
		
		// 2c. Con switch
		Simulacion2C();
		
		// 2d. Con la ruta a la red local en la tabla de rutas
		Simulacion2D();
		
		// 3. Una red ethernet con dos ordenadores, en la que uno envia un datagrama
		//    al otro ordenador (pero con las tablas de rutas vacias)
		Simulacion3();
		
		// 4. Una red ethernet con dos ordenadores, en la que uno envia un datagrama
		//    al otro ordenador (con las tablas de rutas con una entrada para la red)
		Simulacion4();
		
		// 4b. Con hub
		Simulacion4B();
		
		// 4c. Con switch
		Simulacion4C();
		
		// 5. Una red ethernet con dos ordenadores, en la que uno envia un datagrama
		//    al otro ordenador (con las tablas de rutas con una entrada para la red)
		//    Con las direcciones MAC identicas en los dos ordenadores
		Simulacion5();
		
		// 6. Una red con tres ordenadores, en la que uno envia un datagrama al
		//    tercer ordenador
		Simulacion6();
		
		// 6b. con switch
		Simulacion6B();
		
		// 7. Una red con dos ordenadores, en la que uno envia un datagrama de tama�o
		//    superior a la MTU de la red al otro ordenador
		Simulacion7();
		
		// 7b. Sin permitir fragmentacion
		Simulacion7B();
		
		// 8. Una red con un ordenador, en la que se envia un datagrama de tama�o
		//    superior a la MTU de la red a un ordenador inexistente de la misma red
		Simulacion8();
		
		// 9. Dos redes, en la que un ordenador de una red envia un datagrama
		//    a un ordenador existente en la otra red, pero sin ruta para la otra
		//    red en las tablas de rutas de los PCs y sin rutas por defecto
		Simulacion9();
		
		// 10. Dos redes, en la que un ordenador de una red envia un datagrama a 
		//     un ordenador existente en la otra red, pero sin ruta para la otra
		//     red en la tabla de rutas de los PCs, y con rutas por defecto al GW
		Simulacion10();
		
		// 11. Dos redes con distinta MTU, y un equipo envia desde una red un
		// datagrama que puede pasar por su red pero que no puede pasar por la otra
		Simulacion11();
		
		// 11b. Igual que 11 pero forzando un nivel mas de refragmentacion, con
		//      tres redes
		Simulacion11B();
		
		// 11c. Tres redes, donde un ordenador de la primera red envia un datagrama
		// a un ordenador de la tercera red pero al NO permitirse la fragmentacion
		// el dtgrm no puede pasar por la tercera red (al tener una mtu peque�a)
		// y se envia al origen un mensaje icmp
		Simulacion11C();
		
		// 12. Bucle entre dos routers
		Simulacion12();
		
		// 13. Dos ordenadores conectados punto a punto donde uno envia datos al otro
		Simulacion13();
		
		// 13B. Igual que el anterior pero con fragmentacion
		Simulacion13B();
		
		// 14. Spoofing de IP. Una maquina envia a otra un datagrama con una direccion
		// IP de origen falsa
		Simulacion14();
		
		// 15. Ejemplo de prueba
		SimulacionPACONET();
		
		// 16. Envio de un datagrama a la direccion 127.0.0.1
		Simulacion16();
		
		// 17. Envio de un mensaje ICMP de un ordenador a otro de la misma red
		Simulacion17();
		
		// 18. Envio de un mensaje ICMP de un ordenador a otro inexistente en la misma red
		Simulacion18();
		
		// 19. Ping de un ordenador a otro de otra red
		SimulacionPACONET2();
		
		// 20. Ping entre redes con fragmentacion
		Simulacion20();
		
		// 21. Ping en una misma red sin fragmentacion
		Simulacion21();
		*/
		System.out.println();
	}



    /**
     * Registra los distintos equipos y redes
     */
    private static void RegistrarComponentes()
    {
		try
		{	
			// registramos los tipos de equipos
			LocalizadorEquipos.Registrar("Ordenador");
			LocalizadorEquipos.Registrar("Router");

			// registramos los tipos de redes
			LocalizadorRedes.Registrar("Ethernet.Ethernet");
			LocalizadorRedes.Registrar("Ethernet.SwitchEthernet");
			LocalizadorRedes.Registrar("Ethernet.HubEthernet");
			LocalizadorRedes.Registrar("Ethernet.PuenteEthernet");
			LocalizadorRedes.Registrar("PuntoAPunto.PuntoAPunto");
		}
		catch(Exception e)
		{
			System.out.println("Error registrando los componentes");
			e.printStackTrace();
		}
    }
    
    
    
    private static void Simulacion1()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n1. Autoenvio de un datagrama");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
                
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.1");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);
	       
	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }
    
    
    
    private static void Simulacion1B()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n1b. Autoenvio de un datagrama (version HUB)");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.HubEthernet","hub1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
                
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.1");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);
	       
	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);    
	       
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }

    
    
    private static void Simulacion1C()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n1b. Autoenvio de un datagrama (version SWITCH)");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.SwitchEthernet","switch1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
                
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.1");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);
	       
	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);    
	       
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }

    
    
    private static void Simulacion2()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n2. Envio de un datagrama sin fragmentacion a un destino inexistente");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
                
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  
	    
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }
    
    

    private static void Simulacion2B()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n2b. Envio de un datagrama sin fragmentacion a un destino inexistente");
    	System.out.println("(version HUB)");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.HubEthernet","hub1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
                
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  
	    
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }
    
    

    private static void Simulacion2C()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n2b. Envio de un datagrama sin fragmentacion a un destino inexistente");
    	System.out.println("(version SWITCH)");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.SwitchEthernet","switch1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
                
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  
	    
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }
    

    
    private static void Simulacion2D()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n2D. Envio de un datagrama sin fragmentacion a un destino inexistente");
    	System.out.println("Con la ruta a la red local en la tabla de rutas");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","eth0");
                
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  
	    
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }
    
    
    private static void Simulacion3()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n3. Envio de un datagrama sin fragmentacion a un destino existente en la misma red");
    	System.out.println("pero sin entrada en la tabla de rutas para la red");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
                
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.2","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc2.setInterfaz(interfaz);
           
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  

	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);

	       
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }

    
    
    private static void Simulacion4()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n4. Envio de un datagrama sin fragmentacion a un destino existente en la misma red");
    	System.out.println("CON entrada en la tabla de rutas para la red");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.1"));
           
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.2","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  

	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);

	       
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }


    
    private static void Simulacion4B()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n4B. Envio de un datagrama sin fragmentacion a un destino existente en la misma red");
    	System.out.println("CON entrada en la tabla de rutas para la red (version HUB)");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.HubEthernet","hub1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.1"));
           
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.2","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  

	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);

	       
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }

    
    
    private static void Simulacion4C()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n4C. Envio de un datagrama sin fragmentacion a un destino existente en la misma red");
    	System.out.println("CON entrada en la tabla de rutas para la red (version SWITCH)");
    	System.out.println("NOTA: Comprobar que una vez que el switch sabe por que puerto esta");
    	System.out.println("      cada direccion no reenvia las tramas por los demas puertos!");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.SwitchEthernet","switch1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.1"));
           
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.2","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  

	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);

	       
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }

    

    private static void Simulacion5()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n5. Envio de un datagrama sin fragmentacion a un destino existente en la misma red");
    	System.out.println("CON entrada en la tabla de rutas para la red");
    	System.out.println("con repeticion de direcciones MAC");
    	System.out.println("Explicacion: el PC2 intenta enviar una trama con direccion MAC");
		System.out.println("             igual a la del interfaz por la que envia la trama");
		System.out.println("             lo cual indica que en el nivel superior hay algun error");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.1"));
           
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.2","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  

	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	    
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }


    
    private static void Simulacion6()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n6. Envio de un datagrama sin fragmentacion a un destino existente en la misma red");
    	System.out.println("CON entrada en la tabla de rutas para la red");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.1"));
           
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.2","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           
           // PC3
           Equipo pc3=LocalizadorEquipos.New("Ordenador","PC3");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.3","255.255.255.0","00:00:00:00:00:03","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc3.setInterfaz(interfaz);
           pc3.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.3"));
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevoEquipo(pc3);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.3");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  

	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc3.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	    
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }

    

    private static void Simulacion6B()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n6b. Envio de un datagrama sin fragmentacion a un destino existente en la misma red");
    	System.out.println("CON entrada en la tabla de rutas para la red (con SWITCH)");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.SwitchEthernet","switch1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.1"));
           
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.2","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           
           // PC3
           Equipo pc3=LocalizadorEquipos.New("Ordenador","PC3");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.3","255.255.255.0","00:00:00:00:00:03","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc3.setInterfaz(interfaz);
           pc3.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.3"));
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevoEquipo(pc3);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1000);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.3");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  

	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc3.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	    
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }
    
    
    
    private static void Simulacion7()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n7. Envio de un datagrama CON fragmentacion a un destino existente en la misma red");
    	System.out.println("CON entrada en la tabla de rutas para la red");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.1"));
           
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.2","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(ethernet1.getMTU()+1); // 1501 bytes
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  

	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);	       
	       
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }
    
    
    
    private static void Simulacion7B()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n7b. Envio de un datagrama CON fragmentacion a un destino existente en la misma red");
    	System.out.println("CON entrada en la tabla de rutas para la red");
    	System.out.println("NO se permite fragmentacion IP");
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.1"));
           
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.2","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(ethernet1.getMTU()+1); // 1501 bytes
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	       dato.fragmentable=false; // No permitimos fragmentacion
	       
	       pc1.ProgramarSalida(dato);  

	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);	       
	       
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }
 
    
    
    private static void Simulacion8()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n8. Envio de un datagrama CON fragmentacion a un destino INexistente en la misma red");
    	System.out.println("CON entrada en la tabla de rutas para la red");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.1"));           
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevaRed(ethernet1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(ethernet1.getMTU()+1); // 1501 bytes
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  
	       
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }


    
    private static void Simulacion9()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n9. Envio de un datagrama SIN fragmentacion a un destino existente en otra red");
    	System.out.println("SIN entrada en la tabla de rutas de los PCs para la otra red");
    	System.out.println("y SIN ruta por defecto");
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
           Red ethernet2=LocalizadorRedes.New("Ethernet.Ethernet","ethernet2");
           
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","eth0"); //ruta local
           
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.2.1","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet2);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.2.0","255.255.255.0","directa","eth0");
          
           // Router con dos interfaces
           Equipo router=LocalizadorEquipos.New("Router","router");
           interfaz=LocalizadorRedes.NewInterfaz("con0","10.0.1.2","255.255.255.0","00:00:00:00:00:ff","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           router.setInterfaz(interfaz);
           interfaz=LocalizadorRedes.NewInterfaz("con1","10.0.2.2","255.255.255.0","00:00:00:00:00:ef","Ethernet.Ethernet");
           interfaz.Conectar(ethernet2);
           router.setInterfaz(interfaz);
           router.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           router.tablaDeRutas.Anadir("10.0.2.0","255.255.255.0","directa",new DireccionIPv4("10.0.2.2"));
           
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevoEquipo(router);
	       simulador.NuevaRed(ethernet1);
	       simulador.NuevaRed(ethernet2);
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(200);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.2.1");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato); 
	       
	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }


    
    private static void Simulacion10()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n10. Envio de un datagrama SIN fragmentacion a un destino existente en otra red");
    	System.out.println("SIN entrada en la tabla de rutas de los PCs para la otra red pero");
    	System.out.println("CON ruta por defecto a router (GW)");
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
           Red ethernet2=LocalizadorRedes.New("Ethernet.Ethernet","ethernet2");
           
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","eth0"); //ruta local
           pc1.tablaDeRutas.Anadir("por defecto","por defecto","10.0.1.2","eth0");
           
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.2.1","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet2);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.2.0","255.255.255.0","directa","eth0");
           pc2.tablaDeRutas.Anadir("por defecto","por defecto","10.0.2.2","eth0");
           
           // Router con dos interfaces
           Equipo router=LocalizadorEquipos.New("Router","router");
           interfaz=LocalizadorRedes.NewInterfaz("con0","10.0.1.2","255.255.255.0","00:00:00:00:00:ff","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           router.setInterfaz(interfaz);
           interfaz=LocalizadorRedes.NewInterfaz("con1","10.0.2.2","255.255.255.0","00:00:00:00:00:ef","Ethernet.Ethernet");
           interfaz.Conectar(ethernet2);
           router.setInterfaz(interfaz);
           router.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           router.tablaDeRutas.Anadir("10.0.2.0","255.255.255.0","directa",new DireccionIPv4("10.0.2.2"));
           
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevoEquipo(router);
	       simulador.NuevaRed(ethernet1);
	       simulador.NuevaRed(ethernet2);
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(200);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.2.1");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);
	       
	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	    
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }
    
    
    private static void Simulacion11()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n11. Envio de un datagrama SIN fragmentacion a un destino existente en otra red");
    	System.out.println("SIN entrada en la tabla de rutas de los PCs para la otra red pero");
    	System.out.println("NO se permite fragmentacion, y la mtu de la segunda red no permite el paso del");
    	System.out.println("datagrama");
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
           Red ethernet2=LocalizadorRedes.New("Ethernet.Ethernet","ethernet2");
           ethernet2.setMTU(1000);
           
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","eth0"); //ruta local
           pc1.tablaDeRutas.Anadir("por defecto","por defecto","10.0.1.2","eth0");
           
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.2.1","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet2);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.2.0","255.255.255.0","directa","eth0");
           pc2.tablaDeRutas.Anadir("por defecto","por defecto","10.0.2.2","eth0");
           
           // Router con dos interfaces
           Equipo router=LocalizadorEquipos.New("Router","router");
           interfaz=LocalizadorRedes.NewInterfaz("con0","10.0.1.2","255.255.255.0","00:00:00:00:00:ff","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           router.setInterfaz(interfaz);
           interfaz=LocalizadorRedes.NewInterfaz("con1","10.0.2.2","255.255.255.0","00:00:00:00:00:ef","Ethernet.Ethernet");
           interfaz.Conectar(ethernet2);
           router.setInterfaz(interfaz);
           router.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           router.tablaDeRutas.Anadir("10.0.2.0","255.255.255.0","directa",new DireccionIPv4("10.0.2.2"));
           
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevoEquipo(router);
	       simulador.NuevaRed(ethernet1);
	       simulador.NuevaRed(ethernet2);
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1300);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.2.1");
	       dato.protocolo=0;
	       dato.fragmentable=false;
	    
	       pc1.ProgramarSalida(dato);  
	       
	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	    
	       while(simulador.SimularUnPaso());
	       
	       System.out.println();
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }

    
    
    
    private static void Simulacion11B()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n11b. Tres redes con MTUs distintas, que obligan a fragmentar");
    	System.out.println("los fragmentos del datagrama. Se permite la fragmentacion");
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
           Red ethernet2=LocalizadorRedes.New("Ethernet.Ethernet","ethernet2");
           ethernet2.setMTU(1000);
           Red ethernet3=LocalizadorRedes.New("Ethernet.Ethernet","ethernet3");
           ethernet3.setMTU(400);
          
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","eth0"); //ruta local
           pc1.tablaDeRutas.Anadir("por defecto","por defecto","10.0.1.2","eth0");
           
           // PC3
           Equipo pc3=LocalizadorEquipos.New("Ordenador","PC3");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.3.1","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet3);
           pc3.setInterfaz(interfaz);
           pc3.tablaDeRutas.Anadir("10.0.3.0","255.255.255.0","directa","eth0");
           pc3.tablaDeRutas.Anadir("por defecto","por defecto","10.0.3.2","eth0");

           
           // Router con dos interfaces
           Equipo router1=LocalizadorEquipos.New("Router","router1");
           interfaz=LocalizadorRedes.NewInterfaz("con0","10.0.1.2","255.255.255.0","00:00:00:00:00:ff","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           router1.setInterfaz(interfaz);
           interfaz=LocalizadorRedes.NewInterfaz("con1","10.0.2.2","255.255.255.0","00:00:00:00:00:ef","Ethernet.Ethernet");
           interfaz.Conectar(ethernet2);
           router1.setInterfaz(interfaz);
           router1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           router1.tablaDeRutas.Anadir("10.0.2.0","255.255.255.0","directa",new DireccionIPv4("10.0.2.2"));
           router1.tablaDeRutas.Anadir("por defecto","por defecto","10.0.2.3",new DireccionIPv4("10.0.2.2")); 
           
           // Router con dos interfaces
           Equipo router2=LocalizadorEquipos.New("Router","router2");
           interfaz=LocalizadorRedes.NewInterfaz("con0","10.0.2.3","255.255.255.0","00:00:00:00:f0:ff","Ethernet.Ethernet");
           interfaz.Conectar(ethernet2);
           router2.setInterfaz(interfaz);
           interfaz=LocalizadorRedes.NewInterfaz("con1","10.0.3.2","255.255.255.0","00:00:00:00:f0:ef","Ethernet.Ethernet");
           interfaz.Conectar(ethernet3);
           router2.setInterfaz(interfaz);
           router2.tablaDeRutas.Anadir("10.0.3.0","255.255.255.0","directa",new DireccionIPv4("10.0.3.2"));
           router2.tablaDeRutas.Anadir("10.0.2.0","255.255.255.0","directa",new DireccionIPv4("10.0.2.3"));
           router2.tablaDeRutas.Anadir("por defecto","por defecto","10.0.2.2",new DireccionIPv4("10.0.2.3"));
           
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc3);
	       simulador.NuevoEquipo(router1);
	       simulador.NuevoEquipo(router2);
	       simulador.NuevaRed(ethernet1);
	       simulador.NuevaRed(ethernet2);
	       simulador.NuevaRed(ethernet3);
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(1800);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.3.1");
	       dato.protocolo=0;
	       dato.fragmentable=true;
	    
	       pc1.ProgramarSalida(dato);  
	       
	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc3.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	    
	       while(simulador.SimularUnPaso());
	       
	       System.out.println();
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }    
    
    
    
    private static void Simulacion11C()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n11b. Tres redes con MTUs distintas, el datagrama pasa por la primera");
    	System.out.println("y la segunda red, pero como NO se permite fragmentacion, no pasa por la");
    	System.out.println("tercera y se envia un mensaje ICMP al origen (atravesando dos redes de vuelta)");
        
    	try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
           Red ethernet2=LocalizadorRedes.New("Ethernet.Ethernet","ethernet2");
           ethernet2.setMTU(1000);
           Red ethernet3=LocalizadorRedes.New("Ethernet.Ethernet","ethernet3");
           ethernet3.setMTU(400);
          
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","eth0"); //ruta local
           pc1.tablaDeRutas.Anadir("por defecto","por defecto","10.0.1.2","eth0");
           
           // PC3
           Equipo pc3=LocalizadorEquipos.New("Ordenador","PC3");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.3.1","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet3);
           pc3.setInterfaz(interfaz);
           pc3.tablaDeRutas.Anadir("10.0.3.0","255.255.255.0","directa","eth0");
           pc3.tablaDeRutas.Anadir("por defecto","por defecto","10.0.3.2","eth0");

           
           // Router con dos interfaces
           Equipo router1=LocalizadorEquipos.New("Router","router1");
           interfaz=LocalizadorRedes.NewInterfaz("con0","10.0.1.2","255.255.255.0","00:00:00:00:00:ff","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           router1.setInterfaz(interfaz);
           interfaz=LocalizadorRedes.NewInterfaz("con1","10.0.2.2","255.255.255.0","00:00:00:00:00:ef","Ethernet.Ethernet");
           interfaz.Conectar(ethernet2);
           router1.setInterfaz(interfaz);
           router1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           router1.tablaDeRutas.Anadir("10.0.2.0","255.255.255.0","directa",new DireccionIPv4("10.0.2.2"));
           router1.tablaDeRutas.Anadir("por defecto","por defecto","10.0.2.3",new DireccionIPv4("10.0.2.2")); 
           
           // Router con dos interfaces
           Equipo router2=LocalizadorEquipos.New("Router","router2");
           interfaz=LocalizadorRedes.NewInterfaz("con0","10.0.2.3","255.255.255.0","00:00:00:00:f0:ff","Ethernet.Ethernet");
           interfaz.Conectar(ethernet2);
           router2.setInterfaz(interfaz);
           interfaz=LocalizadorRedes.NewInterfaz("con1","10.0.3.2","255.255.255.0","00:00:00:00:f0:ef","Ethernet.Ethernet");
           interfaz.Conectar(ethernet3);
           router2.setInterfaz(interfaz);
           router2.tablaDeRutas.Anadir("10.0.3.0","255.255.255.0","directa",new DireccionIPv4("10.0.3.2"));
           router2.tablaDeRutas.Anadir("10.0.2.0","255.255.255.0","directa",new DireccionIPv4("10.0.2.3"));
           router2.tablaDeRutas.Anadir("por defecto","por defecto","10.0.2.2",new DireccionIPv4("10.0.2.3"));
           
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc3);
	       simulador.NuevoEquipo(router1);
	       simulador.NuevoEquipo(router2);
	       simulador.NuevaRed(ethernet1);
	       simulador.NuevaRed(ethernet2);
	       simulador.NuevaRed(ethernet3);
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(500);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.3.1");
	       dato.protocolo=0;
	       dato.fragmentable=false;
	    
	       pc1.ProgramarSalida(dato);  
	       
	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc3.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	    
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }    

    
    
    private static void Simulacion12()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n12. Bucle");
        
    	try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
           Red ethernet2=LocalizadorRedes.New("Ethernet.Ethernet","ethernet2");
          
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","eth0"); //ruta local
           pc1.tablaDeRutas.Anadir("por defecto","por defecto","10.0.1.2","eth0");
           
           // Router con dos interfaces
           Equipo router1=LocalizadorEquipos.New("Router","router1");
           interfaz=LocalizadorRedes.NewInterfaz("con0","10.0.1.2","255.255.255.0","00:00:00:00:00:ff","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           router1.setInterfaz(interfaz);
           interfaz=LocalizadorRedes.NewInterfaz("con1","10.0.2.1","255.255.255.0","00:00:00:00:00:ef","Ethernet.Ethernet");
           interfaz.Conectar(ethernet2);
           router1.setInterfaz(interfaz);
           router1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa",new DireccionIPv4("10.0.1.2"));
           router1.tablaDeRutas.Anadir("10.0.2.0","255.255.255.0","directa",new DireccionIPv4("10.0.2.1"));
           router1.tablaDeRutas.Anadir("por defecto","por defecto","10.0.2.2",new DireccionIPv4("10.0.2.1")); 
           
           // Router con dos interfaces
           Equipo router2=LocalizadorEquipos.New("Router","router2");
           interfaz=LocalizadorRedes.NewInterfaz("con0","10.0.2.2","255.255.255.0","00:00:00:00:f0:ff","Ethernet.Ethernet");
           interfaz.Conectar(ethernet2);
           router2.setInterfaz(interfaz);
           router2.tablaDeRutas.Anadir("por defecto","por defecto","10.0.2.1",new DireccionIPv4("10.0.2.2"));
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(router1);
	       simulador.NuevoEquipo(router2);
	       simulador.NuevaRed(ethernet1);
	       simulador.NuevaRed(ethernet2);
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(500);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.3.1"); //direccion inexistente
	       dato.protocolo=0;
	       dato.fragmentable=false;
	    
	       pc1.ProgramarSalida(dato);  
	       
	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	    
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }    
   
    
    
    private static void Simulacion13()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n13. Envio de un datagrama sin fragmentacion a un destino existente en la misma red");
    	System.out.println("pero con entrada en la tabla de rutas para la red");
    	System.out.println("ATENCION: NO Se usa ARP");
    	
        try
		{
    	   // redes
           Red puntoapunto1=LocalizadorRedes.New("PuntoAPunto.PuntoAPunto","puntoapunto1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("pap0","10.0.1.1","255.255.255.0","1","PuntoAPunto.PuntoAPunto");
           interfaz.Conectar(puntoapunto1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","pap0");
                
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("pap0","10.0.1.2","255.255.255.0","2","PuntoAPunto.PuntoAPunto");
           interfaz.Conectar(puntoapunto1);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","pap0");
           
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevaRed(puntoapunto1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(200);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  
	       
	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
            
	       while(simulador.SimularUnPaso());
		}
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }    	
    }


    private static void Simulacion13B()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n13B. Envio de un datagrama sin fragmentacion a un destino existente en la misma red");
    	System.out.println("pero con entrada en la tabla de rutas para la red");
    	System.out.println("CON fragmentacion");
    	
        try
		{
    	   // redes
           Red puntoapunto1=LocalizadorRedes.New("PuntoAPunto.PuntoAPunto","puntoapunto1");
        
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("pap0","10.0.1.1","255.255.255.0","1","PuntoAPunto.PuntoAPunto");
           interfaz.Conectar(puntoapunto1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","pap0");
                
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("pap0","10.0.1.2","255.255.255.0","2","PuntoAPunto.PuntoAPunto");
           interfaz.Conectar(puntoapunto1);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","pap0");
           
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevaRed(puntoapunto1);	
	    
	       // paquete que vamos a enviar
	       Buffer buffer=new Buffer(puntoapunto1.getMTU()+10);
	       for(int k=0;k<buffer.Tam();k++)
	          buffer.setByte(k,k%255);
	       Dato dato=new Dato(0,buffer);
	       dato.direccion=new DireccionIPv4("10.0.1.2");
	       dato.protocolo=0;
	    
	       pc1.ProgramarSalida(dato);  
	       
	       // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	       // de transporte 
	       pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	       pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
            
	       while(simulador.SimularUnPaso());
		}
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }    	
    }
    
    
    private static void Simulacion14()
    {
    	try
		{
    	    Interfaz interfaz=null;
    	
    	    System.out.println("\n14. Spoofing IP. Una maquina envia un datagrama IP");
		    System.out.println("a otra maquina pero con una direccion IP de origen arbitraria.");
    
            Red ethernet=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
            Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","192.168.0.1","255.0.0.0","00:00:00:00:00:01","Ethernet.Ethernet");
            interfaz.Conectar(ethernet);
            pc1.setInterfaz(interfaz);
            pc1.tablaDeRutas.Anadir("192.168.0.0","255.0.0.0","directa","eth0");

        
            Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","192.168.0.50","255.0.0.0","00:00:00:00:00:dd","Ethernet.Ethernet");
            interfaz.Conectar(ethernet);
            pc2.setInterfaz(interfaz);
            pc2.tablaDeRutas.Anadir("192.168.0.0","255.0.0.0","directa","eth0");
        
            Simulador simulador=new Simulador();
            simulador.MaximoNumeroDePasos(500);
            simulador.NuevoEquipo(pc1);
            simulador.NuevoEquipo(pc2);
            simulador.NuevaRed(ethernet);
        
            Buffer buffer=new Buffer(100);
            for(int i=0;i<buffer.Tam();i++)
            	buffer.setByte(i,65+i);
            DatagramaIPv4 datagrama=new DatagramaIPv4("10.0.0.1","192.168.0.50",buffer);
            Dato dato=new Dato(0,datagrama);
            dato.direccion=datagrama.getDestino();
            dato.fragmentable=true;
            dato.protocolo=0;
            pc1.ProgramarSalida(dato);
            
            while(simulador.SimularUnPaso());

            for(int i=0;i<6;i++)
            {
                Buffer paquete=pc2.getEvento(i).paquete;
                System.out.println(paquete.Contenido());
            }
            System.out.println();
		}
    	catch(Exception e)
		{
 		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");	
		}
    }
    
    
    
    
    private static void SimulacionPACONET()
    {
        Interfaz interfaz=null;
        
        System.out.println("\n15. Ejemplo con tablas de rutas");
        
        try
		{
            Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
            Red ethernet2=LocalizadorRedes.New("Ethernet.Ethernet","ethernet2");
        
            Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","10.1.0.3","255.255.0.0","00:00:00:00:00:01","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet1);
		    pc1.setInterfaz(interfaz);
		    pc1.tablaDeRutas.Anadir("10.1.0.0","255.255.0.0","255.255.255.255","eth0");
		    pc1.tablaDeRutas.Anadir("0.0.0.0","255.255.255.255","10.1.0.10","eth0");
		
            Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","10.1.0.2","255.255.0.0","00:00:00:00:00:02","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet1);
		    pc2.setInterfaz(interfaz);
		    pc2.tablaDeRutas.Anadir("10.1.0.0","255.255.0.0","255.255.255.255","eth0");
		    pc2.tablaDeRutas.Anadir("0.0.0.0","255.255.255.255","10.1.0.10","eth0");
		    
            Equipo pc3=LocalizadorEquipos.New("Ordenador","PC3");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","10.2.0.1","255.255.0.0","00:00:00:00:00:03","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet2);
		    pc3.setInterfaz(interfaz);
		    pc3.tablaDeRutas.Anadir("10.2.0.0","255.255.0.0","255.255.255.255","eth0");
		    pc3.tablaDeRutas.Anadir("0.0.0.0","255.255.255.255","10.2.0.10","eth0");
		    
		    Equipo router1=LocalizadorEquipos.New("Router","R1");
		    interfaz=LocalizadorRedes.NewInterfaz("eth1","10.1.0.10", "255.255.0.0","00:00:00:00:00:f0","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet1);
		    router1.setInterfaz(interfaz);
		    interfaz=LocalizadorRedes.NewInterfaz("eth0","10.2.0.10", "255.255.0.0","00:00:00:00:00:f1","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet2);
		    router1.setInterfaz(interfaz);
		    router1.tablaDeRutas.Anadir("10.1.0.0","255.255.0.0", "255.255.255.255","eth1");
		    router1.tablaDeRutas.Anadir("10.2.0.0","255.255.0.0", "255.255.255.255","eth0");

	        // simulador
   	        Simulador simulador=new Simulador();
	        simulador.MaximoNumeroDePasos(4000);
	        simulador.NuevoEquipo(pc1);
	        simulador.NuevoEquipo(pc2);
	        simulador.NuevoEquipo(pc3);
	        simulador.NuevoEquipo(router1);
	        simulador.NuevaRed(ethernet1);
	        simulador.NuevaRed(ethernet2);
		    
	        // paquete que vamos a enviar
	        Buffer buffer=new Buffer(200);
	        for(int k=0;k<buffer.Tam();k++)
	           buffer.setByte(k,k%255);
	        Dato dato=new Dato(0,buffer);
	        dato.direccion=new DireccionIPv4("10.2.0.1");
	        dato.protocolo=0;
	     
	        pc1.ProgramarSalida(dato);  
	       
	        // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	        // de transporte 
	        pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	        pc3.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	        
            
	        while(simulador.SimularUnPaso());
		}
        catch(Exception e)
		{
            e.printStackTrace();
            System.out.println("Error: "+e.getMessage()+"\n");
		}
    }



    private static void Simulacion16()
    {
        Interfaz interfaz=null;
        
        System.out.println("\n16. Envio de un datagrama a 127.0.0.1");
        
        try
		{
            Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
            Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","10.1.0.3","255.255.0.0","00:00:00:00:00:01","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet1);
		    pc1.setInterfaz(interfaz);
		    pc1.tablaDeRutas.Anadir("10.1.0.0","255.255.0.0","255.255.255.255","eth0");
		    pc1.tablaDeRutas.Anadir("0.0.0.0","255.255.255.255","10.1.0.10","eth0");
		
	        // simulador
   	        Simulador simulador=new Simulador();
	        simulador.MaximoNumeroDePasos(4000);
	        simulador.NuevoEquipo(pc1);
	        simulador.NuevaRed(ethernet1);
		    
	        // paquete que vamos a enviar
	        Buffer buffer=new Buffer(200);
	        for(int k=0;k<buffer.Tam();k++)
	           buffer.setByte(k,k%255);
	        Dato dato=new Dato(0,buffer);
	        dato.direccion=new DireccionIPv4("127.0.0.1");
	        dato.protocolo=0;
	     
	        pc1.ProgramarSalida(dato);  
	       
	        // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	        // de transporte 
	        pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);

	        
	        while(simulador.SimularUnPaso());
		}
        catch(Exception e)
		{
            e.printStackTrace();
            System.out.println("Error: "+e.getMessage()+"\n");
		}
    }


    private static void Simulacion17()
    {
    	try
		{
    	    Interfaz interfaz=null;
    	
    	    System.out.println("\n17. Ping de un ordenador a otro de la misma red");
    
            Red ethernet=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
            Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","192.168.0.1","255.0.0.0","00:00:00:00:00:01","Ethernet.Ethernet");
            interfaz.Conectar(ethernet);
            pc1.setInterfaz(interfaz);
            pc1.tablaDeRutas.Anadir("192.168.0.0","255.0.0.0","directa","eth0");
        
            Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","192.168.0.2","255.0.0.0","00:00:00:00:00:dd","Ethernet.Ethernet");
            interfaz.Conectar(ethernet);
            pc2.setInterfaz(interfaz);
            pc2.tablaDeRutas.Anadir("192.168.0.0","255.0.0.0","directa","eth0");
        
            Simulador simulador=new Simulador();
            simulador.MaximoNumeroDePasos(500);
            simulador.NuevoEquipo(pc1);
            simulador.NuevoEquipo(pc2);
            simulador.NuevaRed(ethernet);
        
            // Preparamos la carga de datos del mensaje ICMP Echo
            Buffer buffer=new Buffer(100);
            for(int i=0;i<buffer.Tam();i++)
            	buffer.setByte(i,65+i);
            
            // Montamos el mensaje ICMP Echo
            MensajeICMP echo=new MensajeICMP(buffer,8,0);
            
            Dato dato=new Dato(0,echo);
            dato.direccion=new DireccionIPv4("192.168.0.2");
            dato.fragmentable=true;
            dato.protocolo=0;
            pc1.ProgramarSalida(dato);
            
            while(simulador.SimularUnPaso());
		}
    	catch(Exception e)
		{
 		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");	
		}
    }

    
    
    private static void Simulacion18()
    {
    	try
		{
    	    Interfaz interfaz=null;
    	
    	    System.out.println("\n18. Ping de un ordenador a otro de la misma red, pero");
            System.out.println("que no existe.");
    	    
            Red ethernet=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
        
            Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","192.168.0.1","255.0.0.0","00:00:00:00:00:01","Ethernet.Ethernet");
            interfaz.Conectar(ethernet);
            pc1.setInterfaz(interfaz);
            pc1.tablaDeRutas.Anadir("192.168.0.0","255.0.0.0","directa","eth0");
        
            Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","192.168.0.2","255.0.0.0","00:00:00:00:00:dd","Ethernet.Ethernet");
            interfaz.Conectar(ethernet);
            pc2.setInterfaz(interfaz);
            pc2.tablaDeRutas.Anadir("192.168.0.0","255.0.0.0","directa","eth0");
        
            Simulador simulador=new Simulador();
            simulador.MaximoNumeroDePasos(500);
            simulador.NuevoEquipo(pc1);
            simulador.NuevoEquipo(pc2);
            simulador.NuevaRed(ethernet);
        
            // Preparamos la carga de datos del mensaje ICMP Echo
            Buffer buffer=new Buffer(100);
            for(int i=0;i<buffer.Tam();i++)
            	buffer.setByte(i,65+i);
            
            // Montamos el mensaje ICMP Echo
            MensajeICMP echo=new MensajeICMP(buffer,8,0);
            
            Dato dato=new Dato(0,echo);
            dato.direccion=new DireccionIPv4("192.168.0.255");
            dato.fragmentable=true;
            dato.protocolo=0;
            pc1.ProgramarSalida(dato);
            
            while(simulador.SimularUnPaso());
		}
    	catch(Exception e)
		{
 		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");	
		}
    }


    
    private static void SimulacionPACONET2()
    {
        Interfaz interfaz=null;
        
        System.out.println("\nPN2. Ejemplo de ping con tablas de rutas, entre distintas redes");
        
        try
		{
            Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
            Red ethernet2=LocalizadorRedes.New("Ethernet.Ethernet","ethernet2");
        
            Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","10.1.0.3","255.255.0.0","00:00:00:00:00:01","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet1);
		    pc1.setInterfaz(interfaz);
		    pc1.tablaDeRutas.Anadir("10.1.0.0","255.255.0.0","255.255.255.255","eth0");
		    pc1.tablaDeRutas.Anadir("0.0.0.0","255.255.255.255","10.1.0.10","eth0");
		
            Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","10.1.0.2","255.255.0.0","00:00:00:00:00:02","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet1);
		    pc2.setInterfaz(interfaz);
		    pc2.tablaDeRutas.Anadir("10.1.0.0","255.255.0.0","255.255.255.255","eth0");
		    pc2.tablaDeRutas.Anadir("0.0.0.0","255.255.255.255","10.1.0.10","eth0");
		    
            Equipo pc3=LocalizadorEquipos.New("Ordenador","PC3");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","10.2.0.1","255.255.0.0","00:00:00:00:00:03","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet2);
		    pc3.setInterfaz(interfaz);
		    pc3.tablaDeRutas.Anadir("10.2.0.0","255.255.0.0","255.255.255.255","eth0");
		    pc3.tablaDeRutas.Anadir("0.0.0.0","255.255.255.255","10.2.0.10","eth0");
		    
		    Equipo router1=LocalizadorEquipos.New("Router","R1");
		    interfaz=LocalizadorRedes.NewInterfaz("eth1","10.1.0.10","255.255.0.0","00:00:00:00:00:f0","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet1);
		    router1.setInterfaz(interfaz);
		    interfaz=LocalizadorRedes.NewInterfaz("eth0","10.2.0.10","255.255.0.0","00:00:00:00:00:f1","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet2);
		    router1.setInterfaz(interfaz);
		    router1.tablaDeRutas.Anadir("10.1.0.0","255.255.0.0","255.255.255.255","eth1");
		    router1.tablaDeRutas.Anadir("10.2.0.0","255.255.0.0","255.255.255.255","eth0");

	        // simulador
   	        Simulador simulador=new Simulador();
	        simulador.MaximoNumeroDePasos(4000);
	        simulador.NuevoEquipo(pc1);
	        simulador.NuevoEquipo(pc2);
	        simulador.NuevoEquipo(pc3);
	        simulador.NuevoEquipo(router1);
	        simulador.NuevaRed(ethernet1);
	        simulador.NuevaRed(ethernet2);
		    
            // Preparamos la carga de datos del mensaje ICMP Echo
            Buffer buffer=new Buffer(100);
            for(int i=0;i<buffer.Tam();i++)
            	buffer.setByte(i,65+i);
            
            // Montamos el mensaje ICMP Echo
            MensajeICMP echo=new MensajeICMP(buffer,8,0);
            
            Dato dato=new Dato(0,echo);
            dato.direccion=new DireccionIPv4("10.2.0.1");
            dato.fragmentable=true;
            dato.protocolo=0;
            pc1.ProgramarSalida(dato); 
	       
	        // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	        // de transporte 
	        pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	        pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	        pc3.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
            
	        while(simulador.SimularUnPaso());
		}
        catch(Exception e)
		{
            e.printStackTrace();
            System.out.println("Error: "+e.getMessage()+"\n");
		}
    }

    
    
    private static void Simulacion21()
    {
    	Interfaz interfaz=null;
    	
    	System.out.println("\n21. Envio de un ICMP Echo a un destino existente en la misma red");
    	System.out.println("CON fragmentacion");
    	
        try
		{
    	   // redes
           Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
           ethernet1.setMTU(100);
           
           // PC1
           Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.1","255.255.255.0","00:00:00:00:00:01","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc1.setInterfaz(interfaz);
           pc1.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","eth0");
           
           // PC2
           Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
           interfaz=LocalizadorRedes.NewInterfaz("eth0","10.0.1.2","255.255.255.0","00:00:00:00:00:02","Ethernet.Ethernet");
           interfaz.Conectar(ethernet1);
           pc2.setInterfaz(interfaz);
           pc2.tablaDeRutas.Anadir("10.0.1.0","255.255.255.0","directa","eth0");
           
	       // simulador
   	       Simulador simulador=new Simulador();
	       simulador.MaximoNumeroDePasos(4000);
	       simulador.NuevoEquipo(pc1);
	       simulador.NuevoEquipo(pc2);
	       simulador.NuevaRed(ethernet1);	
	    
           // Preparamos la carga de datos del mensaje ICMP Echo
           Buffer buffer=new Buffer(120);
           for(int i=0;i<buffer.Tam();i++)
           	buffer.setByte(i,65+(i%20));
           
           // Montamos el mensaje ICMP Echo
           MensajeICMP echo=new MensajeICMP(buffer,8,0);
           
           Dato dato=new Dato(0,echo);
           dato.direccion=new DireccionIPv4("10.0.1.2");
           dato.fragmentable=true;
           dato.protocolo=995;  // no importa, pq al ser un mensajeICMP esta info no se usa
           pc1.ProgramarSalida(dato); 
	       
	       while(simulador.SimularUnPaso());
	   }
	   catch(Exception e)
	   {
		   e.printStackTrace(System.out);
		   System.out.println("Error: "+e.getMessage()+"\n");
	   }
    }

    
    private static void Simulacion20()
    {
        Interfaz interfaz=null;
        
        System.out.println("\n20. Ejemplo de ping con tablas de rutas, entre distintas redes");
        System.out.println("CON fragmentacion");
        
        try
		{
            Red ethernet1=LocalizadorRedes.New("Ethernet.Ethernet","ethernet1");
            Red ethernet2=LocalizadorRedes.New("Ethernet.Ethernet","ethernet2");
        
            Equipo pc1=LocalizadorEquipos.New("Ordenador","PC1");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","10.1.0.3","255.255.0.0","00:00:00:00:00:01","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet1);
		    pc1.setInterfaz(interfaz);
		    pc1.tablaDeRutas.Anadir("10.1.0.0","255.255.0.0","255.255.255.255","eth0");
		    pc1.tablaDeRutas.Anadir("0.0.0.0","255.255.255.255","10.1.0.10","eth0");
		
            Equipo pc2=LocalizadorEquipos.New("Ordenador","PC2");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","10.1.0.2","255.255.0.0","00:00:00:00:00:02","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet1);
		    pc2.setInterfaz(interfaz);
		    pc2.tablaDeRutas.Anadir("10.1.0.0","255.255.0.0","255.255.255.255","eth0");
		    pc2.tablaDeRutas.Anadir("0.0.0.0","255.255.255.255","10.1.0.10","eth0");
		    
            Equipo pc3=LocalizadorEquipos.New("Ordenador","PC3");
            interfaz=LocalizadorRedes.NewInterfaz("eth0","10.2.0.1","255.255.0.0","00:00:00:00:00:03","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet2);
		    pc3.setInterfaz(interfaz);
		    pc3.tablaDeRutas.Anadir("10.2.0.0","255.255.0.0","255.255.255.255","eth0");
		    pc3.tablaDeRutas.Anadir("0.0.0.0","255.255.255.255","10.2.0.10","eth0");
		    
		    Equipo router1=LocalizadorEquipos.New("Router","R1");
		    interfaz=LocalizadorRedes.NewInterfaz("eth1","10.1.0.10","255.255.0.0","00:00:00:00:00:f0","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet1);
		    router1.setInterfaz(interfaz);
		    interfaz=LocalizadorRedes.NewInterfaz("eth0","10.2.0.10","255.255.0.0","00:00:00:00:00:f1","Ethernet.Ethernet");
		    interfaz.Conectar(ethernet2);
		    router1.setInterfaz(interfaz);
		    router1.tablaDeRutas.Anadir("10.1.0.0","255.255.0.0","255.255.255.255","eth1");
		    router1.tablaDeRutas.Anadir("10.2.0.0","255.255.0.0","255.255.255.255","eth0");

	        // simulador
   	        Simulador simulador=new Simulador();
	        simulador.MaximoNumeroDePasos(4000);
	        simulador.NuevoEquipo(pc1);
	        simulador.NuevoEquipo(pc2);
	        simulador.NuevoEquipo(pc3);
	        simulador.NuevoEquipo(router1);
	        simulador.NuevaRed(ethernet1);
	        simulador.NuevaRed(ethernet2);
		    
            // Preparamos la carga de datos del mensaje ICMP Echo
            Buffer buffer=new Buffer(1800);
            for(int i=0;i<buffer.Tam();i++)
            	buffer.setByte(i,65+(i%20));
            
            // Montamos el mensaje ICMP Echo
            MensajeICMP echo=new MensajeICMP(buffer,8,0);
            
            Dato dato=new Dato(0,echo);
            dato.direccion=new DireccionIPv4("10.2.0.1");
            dato.fragmentable=true;
            dato.protocolo=0;
            pc1.ProgramarSalida(dato); 
	       
	        // Evitamos el 'Protocol Unreachable' al no haberse implementado el nivel
	        // de transporte 
	        pc1.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	        pc2.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
	        pc3.SimularError(Equipo.kICMP,"IGNORAR_ICMP_3_2",true);
            
	        while(simulador.SimularUnPaso());
		}
        catch(Exception e)
		{
            e.printStackTrace();
            System.out.println("Error: "+e.getMessage()+"\n");
		}
    }
}

