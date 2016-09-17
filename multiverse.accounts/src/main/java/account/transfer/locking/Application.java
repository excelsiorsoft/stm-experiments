/**
 * 
 */
package account.transfer.locking;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Simeon
 *
 */
public class Application {

	static Random rnd = new Random();
	static ExecutorService exec = Executors.newCachedThreadPool();

	public static void main(String[] args) throws Exception {


		final Account[] accounts = new Account[] {
				new Account(100), new Account(0), 
				new Account(100), new Account(0), 
				new Account(100), new Account(0),
				new Account(100), new Account(0), 
				new Account(100), new Account(0), };

		final Callable<Void> work = () ->
		{
			
			while (true) {
				int from = rnd.nextInt(accounts.length);
				int to = rnd.nextInt(accounts.length);
				if (from != to) {
					int delta = rnd.nextInt(50);
					  transfer(accounts[from], accounts[to], delta);
				}
			}
		 };
		
		for (int k = 0; k < 4 * Runtime.getRuntime().availableProcessors(); k++) {
			exec.submit(work);
		}
		
		Thread.sleep(1000);
		System.out.println(sum(accounts));
		System.out.println(toStr(accounts));
		
	}
	
	private static void transfer(Account from, Account to, int amount) {
		
		Account fst = (from.id < to.id) ? from : to;
		Account snd = (from.id >= to.id) ? from : to;
		
		fst.lock.lock();
		try {
			snd.lock.lock();
			try {
				if(from.chgBalance(-amount)) {
					if(!to.chgBalance(+amount)) {
						from.chgBalance(+amount);
					}
				}
			}finally {
				snd.lock.unlock();
			}
		}finally {
			fst.lock.unlock();
		}
	}
	
	public static int sum(final Account[] accounts) throws Exception {
		final Account[] tmp = accounts.clone();
		final Comparator<Account> comparator = (acc1,  acc2) -> {return acc1.id - acc2.id;};
		Arrays.sort(tmp, comparator);
		
		
		
		return lockRecursively(tmp, () -> {
			int result = 0;
			for(Account acc : tmp) {
				result += acc.getBalance();
			}
			return result;
		});

		
	}
	
	public static String toStr(final Account[] accounts) throws Exception {
		final Account[] tmp = accounts.clone();
		
		Arrays.sort(tmp, (Account acc1, Account acc2) -> (acc1.id - acc2.id));
		
		return lockRecursively(tmp, () ->  Arrays.toString(tmp));
	}
	
	private static <T> T lockRecursively(Account[] accounts, Callable<T>callable) throws Exception {

		if(accounts.length > 0) {
			accounts[0].lock.lock();
			try {
				return lockRecursively(Arrays.copyOfRange(accounts, 1, accounts.length), callable);
			}finally {
				accounts[0].lock.unlock();
			}
			
		}else {
			return callable.call();
		}
	}
		
}
