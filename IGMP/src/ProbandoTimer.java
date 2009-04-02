import NetworkProtocols.IGMP.util.Timer;
import NetworkProtocols.IGMP.util.Timerable;


public class ProbandoTimer implements Timerable {

	static Timer t2,t3;
	static ProbandoTimer t;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		t = new ProbandoTimer();
		Timer t1 = new Timer(5000,1,t);
		t2 = new Timer(5000,2,t);
		t3 = new Timer(4000,3,t);
		t1.start();
		t3.start();
	}

	public void ejecutarTarea(int tarea) {
		switch(tarea){
		case 1:
			System.out.println("haciendo tarea 1");
			break;
		case 2:
			System.out.println("haciendo tarea 2");
			t3 = new Timer(4000,3,t);
			t3.start();
			break;
		case 3:
			System.out.println("haciendo tarea 3");
			t2 = new Timer(5000,2,t);
			t2.start();
			break;
		}
		
	}

}
