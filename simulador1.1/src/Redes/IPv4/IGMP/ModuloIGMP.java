/**
 *  Protocol Description
 *  Multicast routers use IGMP to learn which groups have members on each
 *  of their attached physical networks.  A multicast router keeps a list
 *  of multicast group memberships for each attached network, and a timer
 *  for each membership.  "Multicast group memberships" means the
 *  presence of at least one member of a multicast group on a given
 *  attached network, not a list of all of the members.  With respect to
 *  each of its attached networks, a multicast router may assume one of
 *  two roles: Querier or Non-Querier.  There is normally only one
 *  Querier per physical network.  All multicast routers start up as a
 *  Querier on each attached network.  If a multicast router hears a
 *  Query message from a router with a lower IP address, it MUST become a
 *  Non-Querier on that network.  If a router has not heard a Query
 *  message from another router for [Other Querier Present Interval], it
 *  resumes the role of Querier.  Routers periodically [Query Interval]
 *  send a General Query on each attached network for which this router
 *  is the Querier, to solicit membership information.  On startup, a
 *  router SHOULD send [Startup Query Count] General Queries spaced
 *  closely together [Startup Query Interval] in order to quickly and
 *  reliably determine membership information.  A General Query is
 *  addressed to the all-systems multicast group (224.0.0.1), has a Group
 *  Address field of 0, and has a Max Response Time of [Query Response
 *  Interval].
 *
 *  When a host receives a General Query, it sets delay timers for each
 *  group (excluding the all-systems group) of which it is a member on
 *  the interface from which it received the query.  Each timer is set to
 *  a different random value, using the highest clock granularity
 *  available on the host, selected from the range (0, Max Response Time]
 *  with Max Response Time as specified in the Query packet.  When a host
 *  receives a Group-Specific Query, it sets a delay timer to a random
 *  value selected from the range (0, Max Response Time] as above for the
 *  group being queried if it is a member on the interface from which it
 *  received the query.  If a timer for the group is already running, it
 *  is reset to the random value only if the requested Max Response Time
 *  is less than the remaining value of the running timer.  When a
 *  group's timer expires, the host multicasts a Version 2 Membership
 *  Report to the group, with IP TTL of 1.  If the host receives another
 *  host's Report (version 1 or 2) while it has a timer running, it stops
 *  its timer for the specified group and does not send a Report, in
 *  order to suppress duplicate Reports.
 *  
 *  When a router receives a Report, it adds the group being reported to
 *  the list of multicast group memberships on the network on which it
 *  received the Report and sets the timer for the membership to the
 *  [Group Membership Interval].  Repeated Reports refresh the timer.  If
 *  no Reports are received for a particular group before this timer has
 *  expired, the router assumes that the group has no local members and
 *  that it need not forward remotely-originated multicasts for that
 *  group onto the attached network.
 *  
 *  When a host joins a multicast group, it should immediately transmit
 *  an unsolicited Version 2 Membership Report for that group, in case it
 *  is the first member of that group on the network.  To cover the
 *  possibility of the initial Membership Report being lost or damaged,
 *  it is recommended that it be repeated once or twice after short
 *  delays [Unsolicited Report Interval].  (A simple way to accomplish
 *  this is to send the initial Version 2 Membership Report and then act
 *  as if a Group-Specific Query was received for that group, and set a
 *  timer appropriately).
 *  
 *  When a host leaves a multicast group, if it was the last host to
 *  reply to a Query with a Membership Report for that group, it SHOULD
 *  send a Leave Group message to the all-routers multicast group
 *  (224.0.0.2). If it was not the last host to reply to a Query, it MAY
 *  send nothing as there must be another member on the subnet.  This is
 *  an optimization to reduce traffic; a host without sufficient storage
 *  to remember whether or not it was the last host to reply MAY always
 *  send a Leave Group message when it leaves a group.  Routers SHOULD
 *  accept a Leave Group message addressed to the group being left, in
 *  order to accommodate implementations of an earlier version of this
 *  standard.  Leave Group messages are addressed to the all-routers
 *  group because other group members have no need to know that a host
 *  has left the group, but it does no harm to address the message to the
 *  group.
 *  
 *  When a Querier receives a Leave Group message for a group that has
 *  group members on the reception interface, it sends [Last Member Query
 *  Count] Group-Specific Queries every [Last Member Query Interval] to
 *  the group being left.  These Group-Specific Queries have their Max
 *  Response time set to [Last Member Query Interval].  If no Reports are
 *  received after the response time of the last query expires, the
 *  routers assume that the group has no local members, as above.  Any
 *  Querier to non-Querier transition is ignored during this time; the
 *  same router keeps sending the Group-Specific Queries.
 *  
 *  Non-Queriers MUST ignore Leave Group messages, and Queriers SHOULD
 *  ignore Leave Group messages for which there are no group members on
 *  the reception interface.
 *  
 *  When a non-Querier receives a Group-Specific Query message, if its
 *  existing group membership timer is greater than [Last Member Query
 *  Count] times the Max Response Time specified in the message, it sets
 *  its group membership timer to that value.
 */
package Redes.IPv4.IGMP;



import Equipos.Equipo;
import Redes.Dato;
import Redes.Interfaz;
import Redes.LocalizadorRedes;
import Redes.Nivel;
import Redes.IPv4.DireccionIPv4;


/**
 * Modulo IGMP
 */
public abstract class ModuloIGMP extends Nivel {
	
	//protected Map<Interfaz, Set<DireccionIPv4>> grupos;
	
	/**
	 * Inicializador de la clase
	 */
	static
	{
		// Este nivel conoce el identificador que IPv4 usa para IGMP
		// El ultimo parametro es 2 porque es el codigo que usa IP para distinguir el protocolo (el ICMP es el 1)
		// Ver http://en.wikipedia.org/wiki/List_of_IP_protocol_numbers
		LocalizadorRedes.Registrar("igmp","ipv4",2);
	}
	
	/**
	 * @param equipo
	 */
	public ModuloIGMP(Equipo equipo) {
		super(equipo);
		/*grupos  = new HashMap<Interfaz, Set<DireccionIPv4>>();
		for (int i = 0; i < equipo.NumInterfaces(); i++) {
			Interfaz interfaz = equipo.getInterfaz(i);
			grupos.put(interfaz, new HashSet<DireccionIPv4>());
		}*/
	}

	/**
     * Comprueba que el dato de entrada sea correcto
     * @param dato Dato a comprobar
     * @return Cierto si el dato es correcto, falso en otro caso
     */
	public boolean ComprobarEntrada(Dato dato)
	{
	    boolean correcto=false;
	    if(dato!=null && dato.paquete!=null && dato.instante>=0)
	        correcto=true;
	    return(correcto);
	}

	/**
	 * Comprueba que el dato de salida sea correcto
	 * @param dato Dato a comprobar
	 * @return Cierto, si el dato es correcto, falso, en otro caso
	 */
	public boolean ComprobarSalida(Dato dato)
	{
	    boolean correcto=false;
	    if(dato!=null && dato.instante>=0 && dato.paquete instanceof MensajeIGMP)
	        if(dato.direccion!=null && dato.direccion instanceof DireccionIPv4)
	            correcto=true;
	    return(correcto);
	}
	
	/* (non-Javadoc)
	 * @see Redes.Nivel#ID()
	 */
	@Override
	public String ID() {
		return("igmp");
	}

	/* (non-Javadoc)
	 * @see Redes.Nivel#Pendientes()
	 */
	@Override
	public int Pendientes() {
	    return(colaEntrada.size()+colaSalida.size());	
	}

	/* (non-Javadoc)
	 * @see Redes.Nivel#Procesar(int)
	 */
	@Override
	public void Procesar(int instante) {
        // 1. Procesamos los timers
		ProcesarTimers(instante);
		
		// 2. Procesamos las entradas
        ProcesarEntrada(instante);
		
	    // 3. Procesamos las salidas
        ProcesarSalida(instante);
	}

	protected abstract void ProcesarTimers(int instante);

	/**
	 * Procesa los mensajes de la cola de salida (envios)
	 * @param instante Instante de tiempo
	 */
	private void ProcesarSalida(int instante)
	{
	    for(int i=0;i<colaSalida.size();i++)
	    {
	        Dato dato=(Dato)colaSalida.get(i);
	        
	        if(dato.instante==instante)
	        {
	            // 1. Extraemos el dato de la cola
	            colaSalida.remove(i);
	            i--;
	            // 2. Mensaje IGMP
	            if(dato.paquete instanceof MensajeIGMP)
	            {
	               MensajeIGMP mensajeIGMP=(MensajeIGMP)dato.paquete;
                   // Enviamos el mensaje
                   Nivel nivel=getNivelInferior("ipv4",0);
                   if(nivel!=null)
                   {
                       // Creamos el dato
                       Dato datoAux=new Dato(instante,mensajeIGMP);
                       datoAux.direccion=dato.direccion;
                       datoAux.protocolo=getID(nivel.ID());
                       datoAux.interfaz=dato.interfaz;
                       // Registramos el evento
                       int tipo=mensajeIGMP.getTipo();	                       
                       equipo.NuevoEvento('E',datoAux.instante,mensajeIGMP,"Mensaje IGMP ["+tipo+"] "+MensajeIGMP.Descripcion(mensajeIGMP)+";"+dato.interfaz.getNombre());
                       datoAux.instante+=getRetardo();
                    
                       // Pasamos el dato al nivel inferior
                       //datoAux.protocolo=getID(nivel.ID());
                       nivel.ProgramarSalida(datoAux);
                    }
	            }
	        }
	    }
	}
	
	protected abstract void procesarMensajeEntrante(Dato dato,int instante);
	
	/**
	 * Procesa los mensajes de un determinado instante de tiempo
	 * @param instante Instante de tiempo
	 */
	private void ProcesarEntrada(int instante)
	{
	    for(int i=0;i<colaEntrada.size();i++)
	    {
	        Dato dato=(Dato)colaEntrada.get(i);
	        
	        if(dato.instante==instante)
	        {
	            // 1. Eliminamos el mensaje de la cola
	            colaEntrada.remove(i);
	            i--;
	            // 2. Mensaje IGMP
	            this.procesarMensajeEntrante(dato,instante);           
	        }
	    }
	}
	
	public static int PASOS_POR_SEGUNDO = 100;
	
	/*
	 * The Robustness Variable allows tuning for the expected packet loss on a subnet.
	 * If a subnet is expected to be lossy, the Robustness Variable may be increased
	 */
	public static int ROBUSTNESS_VARIABLE = 2;
	   
	/*
	 * Query Interval - The Query Interval is the interval between General Queries sent  by
	 * the Querier (seconds).
	 */
	public static int QUERY_INTERVAL = 125 * PASOS_POR_SEGUNDO;

	/*
	 * Query Response Interval - The Max Response Time inserted into the periodic General Queries
	 */
	public static int QUERY_RESPONSE_INTERVAL = 10 * PASOS_POR_SEGUNDO;
	
	/*
	 * Group Membership Interval - time elapsed where if a router doesn't receive an IGMP Report,
	 * the router assumes there's no more members in that multicast group on the segment (seconds).
	 * This value MUST be ((the Robustness Variable) times (the Query Interval)) plus (one Query Response Interval)
	 */
	public static int GROUP_MEMBERSHIP_INTERVAL = 260 * PASOS_POR_SEGUNDO;
	
	/*
	 * Other Querier Present Interval - time where non-querier routers don't hear from the querier router,
	 * and then assume the querier router is dead (seconds).
	 * This value MUST be ((the Robustness Variable) times (the Query Interval)) plus (one half of one Query Response Interval)
	 */
	public static int OTHER_QUERIER_PRESENT_INTERVAL = 255 * PASOS_POR_SEGUNDO;
	
	/*
	 * The Startup Query Interval is the interval between General Queries
	 * sent by a Querier on startup.  Default: 1/4 the Query Interval.
	 */
	public static int STARTUP_QUERY_INTERVAL = 31 * PASOS_POR_SEGUNDO;
	
	/*
	 * The Startup Query Count is the number of Queries sent out on startup,
	 * separated by the Startup Query Interval.  Default: the Robustness Variable.
	 */
	public static int STARTUP_QUERY_COUNT = 2;
	
	/*
	 * The Last Member Query Interval is the Max Response Time inserted into
	 * Group-Specific Queries sent in response to Leave Group messages, and
	 * is also the amount of time between Group-Specific Query messages. (seconds).
	 */
	public static int LAST_MEMBER_QUERY_INTERVAL = 1 * PASOS_POR_SEGUNDO;
	
	/*
	 * The Last Member Query Count is the number of Group-Specific Queries
	 * sent before the router assumes there are no local members.  Default:
	 * the Robustness Variable.
	 */
	public static int LAST_MEMBER_QUERY_COUNT = 2;
	
	/*
	 * The Unsolicited Report Interval is the time between repetitions of a
	 * host's initial report of membership in a group. (seconds).
	 */
	public static int UNSOLICITED_REPORT_INTERVAL = 10 * PASOS_POR_SEGUNDO;
	
	/*
	 * Version 1 Router Present Timeout - if an IGMPv2 host doesn't hear an IGMPv1 Query for this time
	 * period, the host assumes he can resume sending IGMPv2 messages (seconds).
	 */
	public static int VERSION_1_ROUTER_PRESENT_TIMEOUT = 400 * PASOS_POR_SEGUNDO;
	
	/*
	 * All systems general query
	 */
	public static DireccionIPv4 ALL_SYSTEMS_MULTICAST_GROUP = new DireccionIPv4("224.0.0.1");
	
	/*
	 * All routers leave message
	 */
	public static DireccionIPv4 ALL_ROUTERS_MULTICAST_GROUP = new DireccionIPv4("224.0.0.2");

	/**
	 * Funcion utilizada por un equipo para saber si el mensaje esta dirijido a él.
	 * @param direccion
	 * @param interfaz
	 * @return
	 */
	public abstract boolean esParaMi(DireccionIPv4 direccion,Interfaz interfaz);
	
	public abstract void addInterfaz(Interfaz interfaz);

}
