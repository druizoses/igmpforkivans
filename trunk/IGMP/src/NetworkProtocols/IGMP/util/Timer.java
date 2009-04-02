package NetworkProtocols.IGMP.util;

public class Timer extends Thread {
	private int tiempo;
	private int tarea;
	Timerable obj;
	boolean reset=false;
	
	public Timer(int tiempo, int tarea, Timerable obj) {
		super();
		this.tiempo = tiempo;
		this.tarea = tarea;
		this.obj = obj;
		//this.setName("Timer");
		this.start();
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}
	
	private static boolean dormir(int tiempo){
		try {
			Thread.sleep(tiempo);
		} catch (InterruptedException e) {
			return false;
		}
		return true;
	}
	
	public void Resetear(){
		this.reset = true;
		this.interrupt();
	}
	
	public void Cancelar(){
		this.interrupt();
	}
	
	public void run(){
		do{
			reset = false;
			if (dormir(tiempo))
				obj.ejecutarTarea(tarea);
		}while(reset);
	}
}
