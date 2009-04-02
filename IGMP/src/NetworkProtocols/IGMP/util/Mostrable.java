package NetworkProtocols.IGMP.util;

public interface Mostrable {
	/**
	 * M�todo invocado para avisarle a la clase encargada de mostrar
	 * los datos, qu� ha sucedido algo, y tiene que mostrase.
	 * @param log
	 */
	public void agregarLog(String log);
	/**
	 * M�todo envocado cuando se agrega o se quita un grupo.
	 */
	public void cambiosEnGrupos();
}
