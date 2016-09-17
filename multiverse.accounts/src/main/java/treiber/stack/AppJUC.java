package treiber.stack;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class AppJUC {

	public static void main(String[] args) throws InterruptedException {
		final BlockingQueue<String> q0 = new ArrayBlockingQueue<>(64);
		final BlockingQueue<String> q1 = new ArrayBlockingQueue<>(64);
		
		
		Runnable task = ()-> {
			while(true) {
				try {
					Thread.sleep(1000);
					if(Math.random() > 0.5) {
						q0.put("0");
					}else {
						q1.put("1");
					}
				}catch(InterruptedException ignore) {}
			}
		};
		//producer
		new Thread(task).start();
		
		//consumer
		while(true) {
			
			String result = q0.poll();
			if(result != null) {
				//bingo!
			}else {
				result = q1.poll();
				if(result != null) {
					//bingo!
				}else {
					result = q0.take();
					//.....
					System.out.println("What to do???");
				}
			}

		}
	}
}
