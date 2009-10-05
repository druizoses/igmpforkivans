/**
 * 
 */
package Equipos;

import Proyecto.ListaParametros;
import Proyecto.Parametro;
import Redes.Interfaz;
import Redes.Dato;
import Redes.Ethernet.DireccionEthernet;
import Redes.Ethernet.Ethernet;
import Redes.Ethernet.NivelEthernet;
import Redes.Ethernet.TablaDeDirecciones;
import Redes.Ethernet.TramaEthernet;

import java.util.Vector;
import java.util.Hashtable;

/**
 * @author Oscar Ferrer Ballester
 *
 */
public class Switch extends Equipo {
	/**
	*	Caracteristicas genéricas (globales) de un switch
	*/
	private static ListaParametros caracteristicas = null;
	private Vector MemoriaIntermedia = null;
	private boolean procesando = false;
    private TablaDeDirecciones tabla = null;
    private int defaultTemp = 10;
    private final String INTERFAZ = "Interfaz";
    private final String ID = "Id";
    // Numero de puerto por el que ha accedido el dato al Switch.
    private int puerto = 0;
	
	/**
	 * Inicializador de la clase
	 */
    static
	{
    	caracteristicas=new ListaParametros();
    	caracteristicas.add(new Parametro("nombre","Nombre generico","java.lang.String"));
        caracteristicas.setValor("nombre",new String("Switch"));
        caracteristicas.add(new Parametro("imagen","Icono asociado a la clase","java.lang.String"));
        caracteristicas.setValor("imagen","---");
	}
    
    /**
     * Constructor de la clase
     */
    
    public Switch(){
    	int temp = 0;
        MemoriaIntermedia = new Vector();
       /* if(caracteristicas.getValor("temporizador")!= null)
        	temp = Integer.parseInt(caracteristicas.getValor("temporizador").toString());
        else
        	temp = defaultTemp;*/
        tabla = new TablaDeDirecciones(this, temp);
    }
    
    public void setInterfaz(Interfaz interfaz){
    	super.setInterfaz(interfaz);
    	//interfaz.getNivelEnlace().setNivelSuperior(nivelIPv4);
    }
	
	
	/**
	 * Debido a que opera en el subnivel Fisico/MAC no precisa usar de los niveles superiores
	 * como son el ARP, ICMP e IP
	 * @see Equipos.Equipo#ConfiguraPila(int, java.lang.String, java.lang.Object)
	 */
	public void ConfiguraPila(int nivel, String parametro, Object valor) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see Equipos.Equipo#ProgramarEntrada(Redes.Dato)
	 */
	
	//Vamos a reprogramar esta funcion de manera que trabaje de una manera diferente al del resto
	// de equipos que trabajan con la pila IP.
	public void ProgramarEntrada(Dato dato) {
		// TODO Auto-generated method stub
        if(!procesando && dato != null && dato.interfaz != null && dato.paquete instanceof TramaEthernet){
            int i = 0;
            for(i = 0; i < NumInterfaces(); i++)
                if(dato.ctrlSwitch != null){
                	if(dato.ctrlSwitch.get(this.getNombre()+INTERFAZ) == getInterfaz(i))
                      return;
                }else
                	if(dato.ctrlSwitch == null){
                		dato.ctrlSwitch = new Hashtable();
                		i = NumInterfaces();
                	}
            
            for(i = 0; i < NumInterfaces(); i++)
                if(dato.red == getInterfaz(i).getRed()){
                     dato.ctrlSwitch.put(this.getNombre()+INTERFAZ, getInterfaz(i));
                     dato.ctrlSwitch.put(this.getNombre()+ID, new Integer(0));
                     getInterfaz(i).getNivelEnlace().ProgramarEntrada(dato);
                }
        }else if(procesando){
			MemoriaIntermedia.add(dato);
			ActualizarTabla();
		}else{
		    System.out.println("Error al programar dato de entrada en el Switch: ERROR necesaria trama Ethernet e interfaz");
        }
	}

	/**
	 * @see Equipos.Equipo#ProgramarSalida(Redes.Dato)
	 */
	public void ProgramarSalida(Dato dato) {
		// Al programar la salida almacenamos el dato en la memoria intermedia para que así la 
		// lógica del Switch estudie posteriormente cual es el mejor puerto para ser enviado.
		
		if(dato == null){
			return;
        }else if(!(dato.paquete instanceof TramaEthernet)){
            return;
        }
        // Si no se ha especificado un interfaz de entrada para el dato, por defecto se le asignara el primero
        if(dato.interfaz == null)
            dato.interfaz = getInterfaz(0);
		if(MemoriaIntermedia == null)
			MemoriaIntermedia = new Vector();
		MemoriaIntermedia.add(dato);
		NuevoEvento('R',dato.instante,dato.paquete,"Envio de datos programado al SWITCH");

	}

	/**
	 * @see Equipos.Equipo#SimularError(int, java.lang.String, boolean)
	 */
	public boolean SimularError(int nivel, String flag, boolean activar) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see Proyecto.Objeto#Caracteristicas()
	 */
	public ListaParametros Caracteristicas() {
		// TODO Auto-generated method stub
		return caracteristicas;
	}

	/**
	 * @see Proyecto.Objeto#Pendientes()
	 */
	public int Pendientes() {
		int pendientes = 0;
		
		//Observa si existen datos pendientes a procesar en las interfaces pertenecientes al switch
		for(int i = 0; i < NumInterfaces(); i++)
			pendientes += getInterfaz(i).getNivelEnlace().Pendientes();
		
		//Comprueba si existe algún dato dentro de la memoria intermedia del switch que enviar.
		pendientes += NumPendientes();
		
		return pendientes;
	}
	
	private int NumPendientes(){
		if(MemoriaIntermedia == null)
			return 0;
		return MemoriaIntermedia.size();
	}

	/**
	 * @see Proyecto.Objeto#Procesar(int)
	 */
	public void Procesar(int instante) {
		// TODO Auto-generated method stub
		procesando = true;
		for(int i = 0; i < NumInterfaces() ; i++){
			puerto = i;
			getInterfaz(i).getNivelEnlace().Procesar(instante);
        }
		RedirigirTramas();
		puerto = 0;
		procesando = false;
	}
	
	/**
	 * Redirige las tramas que han entrado hacia las salidas que correspondan según la tabla de direcciones
	 * dirección de destino y 
	 *
	 */
	private void RedirigirTramas(){
		int numInterfaz = -1;
		Dato datoAux = null;
		for(int i = 0; MemoriaIntermedia.size() > 0 && i < Interfaces.size(); i++){
			Dato dato = (Dato) MemoriaIntermedia.remove(MemoriaIntermedia.size() - 1);
            dato.instante++;
			if(dato.direccion != null && dato.direccion instanceof DireccionEthernet){
				numInterfaz = tabla.puertoAsociado((DireccionEthernet)dato.direccion);
				if(numInterfaz != -1){
                    getInterfaz(numInterfaz).getNivelEnlace().ProgramarSalida(dato);
				}else{
					for(int j = 0; j < Interfaces.size(); j++){
						if(dato.interfaz.getRed() != getInterfaz(j).getRed())
							getInterfaz(j).getNivelEnlace().ProgramarSalida(dato);
					}
				}
			}else{
				for(int j = 0; j < Interfaces.size(); j++){
					getInterfaz(j).getNivelEnlace().ProgramarSalida(dato);
                }
			}
		}
	}
	
	private void ActualizarTabla(){
        TramaEthernet trama = null;
        Dato dato =(Dato) MemoriaIntermedia.get(MemoriaIntermedia.size()-1);
        trama = (TramaEthernet) dato.paquete;
        tabla.actualizarTabla((DireccionEthernet)trama.getOrigen(), puerto, dato.instante);
	}

}
