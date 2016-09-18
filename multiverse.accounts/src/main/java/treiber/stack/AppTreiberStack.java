/**
 * 
 */
package treiber.stack;

import org.multiverse.api.StmUtils;

/**
 * @author Simeon
 *
 */
public class AppTreiberStack {

	
	public static void main(String[] args) {
		
		final TreiberTxnStack<String> stack0 = new TreiberTxnStack<>();
		final TreiberTxnStack<String> stack1 = new TreiberTxnStack<>();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
					
					StmUtils.atomic(new Runnable() {

						@Override
						public void run() {
							if(Math.random() > 0.5) {
								stack0.push("A");
							}else {
								stack1.push("B");
							}
							
						}});
				}
				
			}}).start();;
		
		while(true) {
			StmUtils.atomic(new Runnable() {

				@Override
				public void run() {
					//take if at least one stack has values in it
					if(!stack0.isEmpty()) {
						System.out.println(stack0.pop());
					}else if(!stack1.isEmpty()) {
						System.out.println(stack1.pop());
					}else {
						StmUtils.retry();
					}
					
				}});
		}
	}
}
