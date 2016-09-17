package benchmarking;

import static org.multiverse.api.StmUtils.atomic;

import org.multiverse.api.StmUtils;
import org.multiverse.api.references.TxnInteger;

public class Benchmark {

	
	
	public static void main(String[] args) {
		
		final TxnInteger shared = StmUtils.newTxnInteger(); //shared memory
		
		Thread th1 = new Thread(()->{
			
			long t0 = System.nanoTime();
			int sum = 0;
			
			for(int k = 0; k < 10_000_000; k++) {
				sum += atomic(()->shared.set(1));//write
			}
			
			long t1 = System.nanoTime();
			System.out.println("th1: "+(t1 - t0) / 1_000_000 +" msec; sum1: "+sum);
		}); th1.start();
		
		Thread th2 = new Thread(()->{
			
			long t0 = System.nanoTime();
			int sum = 0;
			
			for(int k = 0; k < 10_000_000; k++) {
				sum += atomic(()->shared.get());//read
			}
			
			long t1 = System.nanoTime();
			System.out.println("th2: "+(t1 - t0) / 1_000_000+" msec; sum2: "+sum);
		}); th2.start();
	}
}
