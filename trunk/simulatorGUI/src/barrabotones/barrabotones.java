/** @author: tlfs & afzs */
package barrabotones;

import java.awt.event.MouseListener;

import javax.swing.JToolBar;

import util.colores;
import util.nomiconos;

/** Clase barrabotones para crear el menu barrabotones */
public class barrabotones extends JToolBar
{
	/** boton nuevo archivo */
	private bboton btnN;
	/** boton abrir archivo */
	private bboton btnA;
	/** boton guardar archivo */
	private bboton btnG;

	/** boton imprimir */
	private bboton btnI;
	/** boton buscar */
	private bboton btnB;
	
	/** boton cortar equipos */
	private bboton btnX;
	/** boton copiar equipos */
	private bboton btnC;
	/** boton pegar equipos */
	private bboton btnP;
	
	/** boton comprobar simulacion */
	private bboton btnCompS;
	/** boton configurar acciones */
	private bboton btnSE;
	
	/** boton simular envios */
	private bboton btnSi;
	/** boton simular paso a paso */
	private bboton btnSip;
	/** boton simular evento a evento */
	private bboton btnEvt;
	/** boton detener simulacion */
	private bboton btnDS;
	/** boton mostrar sucesos de la simulacion */
	private bboton btnEvS;

	/** opcion para ir indicando que evento se va procesando */
	private bboton btnInfoEv;
	
	/** boton para separar un menu de otro */
	private bboton btnS;
	/** boton para separar un menu de otro */
	private bboton btnS2;
	/** boton para separar un menu de otro */
	private bboton btnS3;

	
	/** Constructor de barrabotones */
	public barrabotones(MouseListener oyente, String estado)
	{
		// a cada boton le pasamos unos valores determinados:
		// el nombre del boton, la ruta de la imagen, el oyente del boton, los estados en los que va a estar habilitado,
		// el estado actual y el texto que aparece cuando se acerca el raton al boton.
		setBackground(colores.fondoBarras);
		
		btnN= new bboton();
		btnN.setPBoton("btnN", nomiconos.nombotonNuevo, oyente, ",99,", estado,"Nuevo");
		add(btnN);

		btnA= new bboton();
		btnA.setPBoton("btnA", nomiconos.nombotonAbrir, oyente, ",99,", estado,"Abrir");
		add(btnA);

		btnG= new bboton();
		btnG.setPBoton("btnG", nomiconos.nombotonGuardar, oyente, ",18,", estado,"Guardar");
		add(btnG);

		btnS2= new bboton();
		btnS2.setPBoton("btnS2", nomiconos.nombotonSeparador, oyente, "", estado,"");
		add(btnS2);
		
		btnI= new bboton();
		btnI.setPBoton("btnI", nomiconos.nombotonImprimir, oyente, ",1,4,5,6,7,8,9,10,", estado,"Imprimir");
		add(btnI);
		
		btnB= new bboton();
		btnB.setPBoton("btnB", nomiconos.nombotonBuscar, oyente, ",9,10,", estado,"Buscar");
		add(btnB);
		
		btnS= new bboton();
		btnS.setPBoton("btnS", nomiconos.nombotonSeparador, oyente, "", estado,"");
		add(btnS);

		btnX= new bboton();
		btnX.setPBoton("btnX", nomiconos.nombotonCortar, oyente, ",1,7,8,", estado,"Cortar");
		add(btnX);
		
		btnC= new bboton();
		btnC.setPBoton("btnC", nomiconos.nombotonCopiar, oyente, ",1,7,8,", estado,"Copiar");
		add(btnC);
		
		btnP= new bboton();
		btnP.setPBoton("btnP", nomiconos.nombotonPegar, oyente, ",3,4,5,", estado,"Pegar");
		add(btnP);

		btnS3= new bboton();
		btnS3.setPBoton("btnS3", nomiconos.nombotonSeparador, oyente, "", estado,"");
		add(btnS3);

		btnCompS= new bboton();
		btnCompS.setPBoton("btnCompS", nomiconos.nombotonCompruebaSimula, oyente, ",13,", estado,"Comprobar Simulacion");
		add(btnCompS);

		btnSE= new bboton();
		btnSE.setPBoton("btnSE", nomiconos.nombotonSimuEnvio, oyente, ",14,", estado,"Configurar Acciones");
		add(btnSE);

		btnSi= new bboton();
		btnSi.setPBoton("btnSi", nomiconos.nombotonSimulaTodo, oyente, ",15,", estado,"Simular Acciones");
		add(btnSi);

		btnSip= new bboton();
		btnSip.setPBoton("btnSip", nomiconos.nombotonSimulaPaso, oyente, ",15,", estado,"Simular paso a paso");
		add(btnSip);

		btnEvt= new bboton();
		btnEvt.setPBoton("btnEvt", nomiconos.nombotonSimulaEvento, oyente, ",15,", estado,"Simular evento a evento");
		add(btnEvt);

		btnDS= new bboton();
		btnDS.setPBoton("btnDS", nomiconos.nombotonDetieneSimu, oyente, ",17,", estado,"Detener Simulacion");
		add(btnDS);

		btnEvS= new bboton();
		btnEvS.setPBoton("btnEvS", nomiconos.nombotonEventosSimu, oyente, ",18,", estado,"Mostrar Sucesos de la Simulacion");
		add(btnEvS);
		
		btnInfoEv= new bboton();
		btnInfoEv.setPBoton("btnInfo", null, null, ",16,", estado, "");
		
		add(btnInfoEv);
	}
	

	/** Funcion que habilita o deshabilita los estados
	 * y para cada boton se introducen los estados en los que este va a estar habilitado
	 * @param estado estado actual
	 * @param texto la informacion que aparece de los eventos procesados de la simulacion
	 */
	public void habilita(String estado, String texto)
	{
		btnN.establece(",99,",estado);
		btnA.establece(",99,",estado);
		btnG.establece(",18,",estado);
		
		btnX.establece(",1,7,8,",estado);
		btnC.establece(",1,7,8,",estado);
		btnP.establece(",3,4,5,",estado);
		
		btnI.establece(",1,4,5,6,7,8,9,10,",estado);
		btnB.establece(",9,10,",estado);
		
		btnCompS.establece(",13,",estado);
		btnSE.establece(",14,",estado);
		btnSi.establece(",15,",estado);
		btnSip.establece(",15,",estado);
		btnEvt.establece(",15,",estado);
		btnDS.establece(",16,",estado);
		btnEvS.establece(",17,",estado);
		
		btnInfoEv.establece(",16,", estado);
		btnInfoEv.estableceTexto(texto);
	}	
}
