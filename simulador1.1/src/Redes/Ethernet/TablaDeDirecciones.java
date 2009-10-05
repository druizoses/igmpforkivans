package Redes.Ethernet;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import Equipos.Equipo;

public class TablaDeDirecciones {
	
	private final int tMax = 20; 
	private Hashtable tabla = null;
	private Hashtable temporizador = null;
	private String mac = null;
	private Enumeration claves = null;
	private int instante = -1;
	
	private Equipo equipo = null;
	
	public TablaDeDirecciones(Equipo equipo, int maxTemp){
		//puertos = new Vector();
		//Direcciones = new Vector();
		tabla = new Hashtable();
		temporizador = new Hashtable();
		this.equipo = equipo;
	}
	
	/**
	 * Actualiza la tabla que contiene la relación de direcciones y puertos asociados a ellas.
	 * Mediante esta tabla se puede conocer por que puerto hay que reenviar una trama.
	 * 
	 * @param direccion Direccion Ethernet asociada a la trama que ha llegado al Switch.
	 * @param puerto Numero de puerto(Interfaz) por el cual nos ha llegado la trama.
	 * @return
	 */
	public boolean actualizarTabla(DireccionEthernet direccion, int puerto, int instante){
		boolean actualizada = false;
		// Primero de todo comprobamos si la tabla de direcciones esta o no vacia.
		if(tabla.size() == 0){
			tabla.put((String)direccion.toString(), new Integer(puerto));
			temporizador.put((String)direccion.toString(), new Integer(0));
			this.instante = instante;
		}else{
			if(this.instante != instante){
				claves = temporizador.keys();
				while(claves.hasMoreElements()){
					mac = claves.nextElement().toString();
					if(mac.compareTo(direccion.toString()) != 0){
						Integer aux = (Integer) temporizador.get(mac);
						if(aux.intValue() >= tMax){
							tabla.remove((String)direccion.toString());
							temporizador.remove((String)direccion.toString());
						}else{
							aux = aux.valueOf(Integer.toString(aux.intValue() + 1));
							temporizador.put(mac, aux);
						}
					}else{
						temporizador.put(mac, new Integer(0));
						actualizada = true;
					}
				}
				this.instante = instante;
			}
			if(!actualizada){
				if(tabla.contains(direccion))
					temporizador.put(mac, new Integer(0));
				else{
					tabla.put((String)direccion.toString(), new Integer(puerto));
					temporizador.put((String)direccion.toString(), new Integer(0));
					actualizada = true;
				}
			}
		}
		return actualizada;
	}
		
    /**
     * @param direccion
     * @return
     */
	public int puertoAsociado(DireccionEthernet direccion){
		Integer puerto = new Integer(-1);
		if(tabla.containsKey((String)direccion.toString()))
			puerto = (Integer) tabla.get((String)direccion.toString());
		return puerto.intValue();
	}
	
	/**
	 * Mantiene las relaciones que se esten usando durante un determinado periodo
	 */
	public void refrescarDatos(){
		
	}
	
	/**
	 * 
	 *
	 */
	public void limpiarTabla(){
		if(tabla != null)
			tabla.clear();
		tabla = null;
	}

}
