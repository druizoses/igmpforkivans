package Equipos;

import Redes.Dato;
import Redes.Interfaz;
import Redes.IPv4.NivelIPv4;
import Redes.IPv4.ARP.ModuloARP;
import Redes.IPv4.ICMP.ModuloICMP;
import Redes.IPv4.IGMP.MensajeIGMP;
import Redes.IPv4.IGMP.ModuloIGMP;
import Redes.IPv4.IGMP.ModuloIGMPRouter;

public class RouterMultiCast extends Router{

	/**
     * Modulo IGMP
     */
    ModuloIGMP moduloIGMP;
    
    /**
     * Constructor
     */
    public RouterMultiCast()
    {
        super();
        moduloIGMP=new ModuloIGMPRouter(this);
        moduloIGMP.setNivelInferior(nivelIPv4);
        nivelIPv4.setNivelSuperior(moduloIGMP);
        nivelIPv4.setModuloIGMP(moduloIGMP);
    }
    
    protected void iniciar() {
    	super.iniciar();
    	moduloIGMP.iniciar();
    }
    
    
    @Override
    public void setInterfaz(Interfaz interfaz) {
    	super.setInterfaz(interfaz);
    	moduloIGMP.addInterfaz(interfaz);
    }
    /**
     * Procesa los paquetes programados para un determinado instante
     * @param instante Instante
     */
    public void Procesar(int instante)
    {  
    	if (encendido){
	        // 2. Comprobamos si hay algo que procesar en el modulo IGMP
	        moduloIGMP.Procesar(instante);
	
	        // 1. Comprobamos si hay algo que procesar en el modulo ICMP
	        super.Procesar(instante);
    	}
     }
    
    @Override
    public int Pendientes() {
    	if (encendido)
    		return super.Pendientes()+moduloIGMP.Pendientes();
    	else
    		return 0;
    }
    
    @Override
    public void ProgramarSalida(Dato dato) {
    	// 3. Mensaje IGMP
        if(dato.paquete instanceof MensajeIGMP)
        {
            if(moduloIGMP.ProgramarSalida(dato))
                NuevoEvento('E',dato.instante,dato.paquete,"Envio de datos programado en IGMP");
        } else {
        	super.ProgramarSalida(dato);
        }
    }
    
    @Override
    public boolean SimularError(int nivelID, String flag, boolean activar) {
    	if (nivelID == Equipo.kIGMP)
        {
            return moduloIGMP.SimularError(flag,activar);
        }
    	return super.SimularError(nivelID, flag, activar);
    }
    
    @Override
    public void ConfiguraPila(int nivelID, String parametro, Object valor) {
		if (nivelID == Equipo.kIGMP)
		{
		    moduloIGMP.parametros.setValor(parametro,valor);
		} else 
			super.ConfiguraPila(nivelID, parametro, valor);
    }
}
