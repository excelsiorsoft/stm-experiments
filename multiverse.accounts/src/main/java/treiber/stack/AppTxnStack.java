/**
 * 
 */
package treiber.stack;

import org.multiverse.api.StmUtils;
import org.multiverse.api.collections.TxnStack;

/**
 * @author Simeon
 *
 */
public class AppTxnStack {

	
	public static void main(String[] args) {
		
		final TxnStack<String> stack0 = StmUtils.newTxnStack();
		final TxnStack<String> stack1 = StmUtils.newTxnStack();
		
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
					//take only when both stacks have values in them (and merge them)
					if(!stack0.isEmpty() && !stack1.isEmpty()) {
						System.out.println(stack0.pop() + ":" +stack1.pop());
					
					}else {
						StmUtils.retry();
					}
					
				}});
		}
	}
}
