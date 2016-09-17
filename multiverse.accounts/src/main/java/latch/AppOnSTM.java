package latch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class AppOnSTM {

	
	public static void main(String[] args) {
		final LatchOnSTM latch = new LatchOnSTM();
		
		new Thread(() -> {try {
			Thread.sleep(2000);
			latch.doOpen();
		} catch (InterruptedException ignore) {}
			
		}).start();
		
		ExecutorService exec = Executors.newCachedThreadPool();
		AtomicInteger cntr = new AtomicInteger();
		for(int k = 0; k < 5; k++) {
			exec.submit(() -> {latch.await(); //waiting until opens
								System.out.println("Hello"+cntr.incrementAndGet());
								return null;
								}
						);
		}
	}
}
