package NetworkProtocols.IGMP.util;

public class Semaphore {
	private int value;
	public static boolean terminar = false;

	  public Semaphore() {
	    value = 0;
	  }

	  public Semaphore(int v) {
	    value = v;
	  }

	  /**
	   * Toma el control del semaforo
	   */
	  public synchronized void P() {
	    while (value <= 0 && !terminar) {
	      try {
	        wait();
	      }
	      catch(InterruptedException e) {
	    	  e.printStackTrace();
	      }
	    }
	    value--;
	  }

	  /**
	   * Libera el semaforo
	   */
	  public synchronized void V() {
	    value++;
	    notify();
	  }
	  
	  public synchronized void Terminar(){
		  terminar = true;
		  notifyAll();
	  }
}
