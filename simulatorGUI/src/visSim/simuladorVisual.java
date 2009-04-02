/** @author: tlfs & afzs
*/
package visSim;

// TODO: Arreglar las punto a punto

import java.util.Vector;

import objetoVisual.listaObjetos;
import objetoVisual.switchVisual;
import util.ordenaVector;
import Equipos.Equipo;
import Equipos.LocalizadorEquipos;
import Proyecto.Evento;
import Proyecto.Objeto;
import Proyecto.Simulador;
import Redes.Buffer;
import Redes.Dato;
import Redes.Interfaz;
import Redes.LocalizadorRedes;
import Redes.Red;
import Redes.IPv4.DireccionIPv4;

public class simuladorVisual
{
	private Vector redes, equipos, listaEnvios;
	Simulador simulador;
	
	public simuladorVisual(listaObjetos lista)
	{
		try
		{
			int i, j;
			String cadenaSimulador;
			listaInterfaces interfaces = new listaInterfaces();
			redes = new Vector();
			equipos = new Vector();
			Vector indices = new Vector();
			listaEnvios = new Vector(lista.getlistaEnvios());
			
			// registramos los tipos de redes
			LocalizadorRedes.Registrar("Ethernet.Ethernet");
			//LocalizadorRedes.Registrar("Ethernet.SwitchEthernet");
			LocalizadorRedes.Registrar("Ethernet.HubEthernet");
			LocalizadorRedes.Registrar("Ethernet.PuenteEthernet");
			LocalizadorRedes.Registrar("PuntoAPunto.PuntoAPunto");
			
			// registramos los tipos de equipos
			LocalizadorEquipos.Registrar("Ordenador");
			LocalizadorEquipos.Registrar("Router");
			LocalizadorEquipos.Registrar("Switch");
			
			for (i=0; i<lista.tam(); i++)
				indices.add(new Integer(0));
			
			// Registramos redes y maquinas de la topologia
			Vector errores;
			for (i=0; i<lista.tam(); i++)
			{
				cadenaSimulador = lista.getCadenaSimulador(i);
				
				if (cadenaSimulador.length()>0)
				{
					//System.out.println("Registrando " + lista.getNombre(i));

					if (cadenaSimulador.startsWith("Ethernet") || cadenaSimulador.startsWith("Anillo"))
					{
						redes.add(LocalizadorRedes.New(cadenaSimulador, lista.getNombre(i)));
						indices.set(i, new Integer(redes.size()-1));
					}
					else
					{
						equipos.add(LocalizadorEquipos.New(cadenaSimulador, lista.getNombre(i)));
						indices.set(i, new Integer(equipos.size()-1));

						// Ponemos la simulacion de errores de la maquina
						errores = new Vector(lista.getNombresSeleccionesErrores(i));
						
						for (j=0; j<errores.size(); j+=3)
							((Equipo)equipos.elementAt(equipos.size()-1)).SimularError(((Integer)errores.elementAt(j+1)).intValue(), (String)errores.elementAt(j), new Boolean((String)errores.elementAt(j+2)).booleanValue());
					}
				}
			}
			
			// Ponemos las interfaces de cada uno de los equipos
			for (i=0; i<lista.tam(); i++)
				if (lista.getCadenaSimulador(i).length()>0)
				{
					//System.out.println("Anyadiendo interfaces. " + lista.getNombre(i));
				
					interfaces = lista.getInterfaces(i);
				
					// Recorrer el bucle anterior solo para maquinas, nada de buses o anillos
				
					for (j=0; j<interfaces.tam(); j++)
					{
						cadenaSimulador = lista.getCadenaSimulador(interfaces.getconecta(j));

						//System.out.println("   conecta con " + cadenaSimulador);
					
						// Si la interfaz conecta con una red
						if (cadenaSimulador.startsWith("Ethernet") || cadenaSimulador.startsWith("Anillo"))
						{
							Interfaz inte = LocalizadorRedes.NewInterfaz(interfaces.getNombre(j), interfaces.getIP(j), interfaces.getMascara(j), interfaces.getDirEnlace(j), cadenaSimulador);
							
							Equipo conectado = (Equipo) equipos.elementAt( ((Integer)indices.elementAt(lista.buscaEquipo(interfaces.getconecta(j)))).intValue() );
							
							inte.Conectar((Red)redes.elementAt( ((Integer)indices.elementAt(lista.buscaEquipo(interfaces.getconecta(j)))).intValue() ));

							((Equipo)equipos.elementAt( ((Integer)indices.elementAt(i)).intValue() )).setInterfaz(inte);
						}else if(cadenaSimulador.startsWith("Ordenador") || cadenaSimulador.startsWith("Switch"))
						{
							// Creo la interfaz que va conectada al equipo en cuestion.
							Interfaz inte = LocalizadorRedes.NewInterfaz(interfaces.getNombre(j), interfaces.getIP(j), interfaces.getMascara(j), interfaces.getDirEnlace(j), "Ethernet.Ethernet");
							
							// Obtenemos el equipo que esta conectado directamente a esta interfaz
							Equipo conectado = (Equipo) equipos.elementAt( ((Integer)indices.elementAt(lista.buscaEquipo(interfaces.getconecta(j)))).intValue() );
							lista.getNombre(i);
							//Obtenemos el equipo al que pertenece esta interfaz.
							Equipo equipoInterfaz = (Equipo) equipos.elementAt( ((Integer)indices.elementAt(i)).intValue() );
							if(conectado != null){
								
								//Interfaces que pertenecen al equipo al que conecta la interfaz.
								listaInterfaces intConectado = lista.getInterfaces(((Integer)lista.buscaEquipo(conectado.getNombre())).intValue());
								
								//Para cada una de las interfaces vemos si esta conectada con la interfaz del equipo que estamos estudiando.
								// si es asi realizamos la conexion de la interfaz al equipo final, usando los datos de la interfaz de este
								// ultimo
								for(int k=0;k<intConectado.size();k++)
									if(((interfazVisual)intConectado.get(k)).getconecta().compareTo(equipoInterfaz.getNombre()) == 0){
										if(lista.buscaEquipo(conectado.getNombre()) > lista.buscaEquipo(equipoInterfaz.getNombre())){
											interfazVisual aux = (interfazVisual) intConectado.get(k);
											inte.Conectar(conectado, aux.getNombre()+";"+aux.getIP()+";"+aux.getMascara()+";"+aux.getDirEnlace()+";Ethernet.Ethernet");
											equipoInterfaz.setInterfaz(inte);
										}
									}
							}
						}
					}
				}

			// Anyadimos las tablas de rutas
			listaRutas rutasTemp;
			for (i=0; i<lista.tam(); i++)
			{
				cadenaSimulador = lista.getCadenaSimulador(i);
				
				if (cadenaSimulador.length()>0)
					if (!(cadenaSimulador.startsWith("Ethernet") || cadenaSimulador.startsWith("Anillo")))
					{
						//System.out.println("Anyadiendo rutas. " + lista.getNombre(i));
						rutasTemp = lista.getRutas(i);
						if(!cadenaSimulador.startsWith("Switch"))
							for (j=0; j<rutasTemp.tam(); j++)
							{
								//System.out.println("   " + rutasTemp.toString(j));
								System.out.println("Equipo al que insertamos una ruta: "+lista.getNombre(i));
								((Equipo)equipos.elementAt(((Integer)indices.elementAt(i)).intValue())).tablaDeRutas.Anadir(rutasTemp.getDestino(j), rutasTemp.getMascara(j), rutasTemp.getGateway(j), rutasTemp.getNombreInterfaz(j));
							}
					}
			}
			
			// Preparamos el simulador
			simulador=new Simulador();
			
			simulador.MaximoNumeroDePasos(400);
			
			// Metemos todos los equipos para la simulacion
			for (i=0; i<equipos.size(); i++)
				simulador.NuevoEquipo((Equipo)equipos.elementAt(i));
			
			// Metemos todas las redes para la simulacion
			for (i=0; i<redes.size(); i++)
				simulador.NuevaRed((Red)redes.elementAt(i));

			
			String nombrePC1, IP2;
			int indiceSim, tamano, envios;
			
			for (i=0; i<listaEnvios.size(); i+=5)
			{
				nombrePC1 = (String)listaEnvios.elementAt(i);
				nombrePC1 = nombrePC1.substring(0, nombrePC1.indexOf(" "));
				
				IP2 = (String)listaEnvios.elementAt(i+1);
				if(IP2.indexOf('(') != -1)
					IP2 = IP2.substring(IP2.indexOf("(")+1, IP2.lastIndexOf(")"));
				
				
				//System.out.println(nombrePC1 + " enviando a " + IP2);
				
				indiceSim = ((Integer)indices.elementAt(lista.buscaEquipo(nombrePC1))).intValue();
				
				tamano = (new Integer((String)listaEnvios.elementAt(i+2))).intValue();
				envios = (new Integer((String)listaEnvios.elementAt(i+3))).intValue();

				// Preparamos un buffer para enviar
				Buffer buffer=new Buffer(tamano);
				
				for(j=0; j<tamano; j++)
					buffer.setByte(j, i%255);
				
				Dato dato=new Dato(0, buffer);
				dato.protocolo=0;
				
				// Programamos la salida para el envio
				dato.direccion = new DireccionIPv4(IP2);
				
				// Ponemos la fragmentacion del dato
				dato.fragmentable = ((listaEnvios.elementAt(i+4)).toString().compareTo("true")==0);
				
				// Preparamos todos los envios entre este par de maquinas
				for (j=0; j<envios; j++)
					((Equipo)equipos.elementAt(indiceSim)).ProgramarSalida(dato);
			}
		}
		catch(Exception e)
		{
			System.out.println("Error al preparar la simulacion.");
			System.out.println("Error: "+e.getMessage()+"\n");
		}
	}
	
	public boolean darUnPaso()
	{
		if (simulador != null)
			return simulador.SimularUnPaso();
		
		return false;
	}
	
	public Vector getEventos()
	{
		Vector dev = new Vector();
		String nombre;
		Evento evt;
		int i, j, numEventos;

		// Eventos de los equipos
		for (i=0; i<equipos.size(); i++)
		{
			nombre =((Objeto)equipos.elementAt(i)).getNombre(); 
			numEventos = ((Objeto)equipos.elementAt(i)).NumEventos();
			
			for (j=0; j<numEventos; j++)
			{
				evt = ((Objeto)equipos.elementAt(i)).getEvento(j); 
				dev.add(evt.instante+"\t["+evt.indicador+"]\t"+nombre+"\t"+evt.mensaje);
			}
		}
		
		// Eventos de las redes
		for (i=0; i<redes.size(); i++)
		{
			nombre =((Objeto)redes.elementAt(i)).getNombre(); 
			numEventos = ((Objeto)redes.elementAt(i)).NumEventos();
			
			for (j=0; j<numEventos; j++)
			{
				evt = ((Objeto)redes.elementAt(i)).getEvento(j); 
				dev.add(evt.instante+"\t["+evt.indicador+"]\t"+nombre+"\t"+evt.mensaje);
			}
		}
		
		// Ordenamos el vector segun el instante y lo devolvemos
		return new Vector(ordenaVector.getOrdenadoNumero(dev));
	}
	
	public void simulacionCompleta()
	{
		while(simulador.SimularUnPaso());

		System.gc();
		System.runFinalization();
	}
}
