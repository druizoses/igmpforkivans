package NetworkProtocols.IGMP.util;

public interface Mostrable {
	/**
	 * Método invocado para avisarle a la clase encargada de mostrar
	 * los datos, qué ha sucedido algo, y tiene que mostrase.
	 * @param log
	 */
	public void agregarLog(String log);
	/**
	 * Método envocado cuando se agrega o se quita un grupo.
	 */
	public void cambiosEnGrupos();
}
