package NetworkProtocols;


public interface ProtocolInterface  {
  /**
   * Agrega un evento en la cola de eventos remotos
   */
	public void addRem(N2N3Indication x); 
  
	/**
	 * Agrega un evento en la cola de eventos locales
	 */
	public void addLoc(Object x); 

}
