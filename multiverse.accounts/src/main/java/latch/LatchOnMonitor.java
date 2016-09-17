/**
 * 
 */
package latch;

/**
 * @author Simeon
 *
 */
public class LatchOnMonitor {

	private boolean open = false;
	
	public synchronized void doOpen() {
		open = true;
		this.notifyAll();
	}
	
	public synchronized void await() throws InterruptedException{
		while(!open) {//spurious wake up
			this.wait();
		}
	}
}
