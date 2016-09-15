/**
 * 
 */
package multiverse.accounts;

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
public class AppLock {

	static Random rnd = new Random();
	static ExecutorService exec = Executors.newCachedThreadPool();

	public static void main(String[] args) throws Exception {

		final AccountLock[] accounts = new AccountLock[] {
				new AccountLock(100), new AccountLock(0), 
				new AccountLock(100), new AccountLock(0), 
				new AccountLock(100), new AccountLock(0),
				new AccountLock(100), new AccountLock(0), 
				new AccountLock(100), new AccountLock(0), };

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
					
					/*() ->{
						while (true) {

							int from = rnd.nextInt(accounts.length);
							int to = rnd.nextInt(accounts.length);
							if (from != to) {
								int delta = rnd.nextInt(50);
								  transfer(accounts[from], accounts[to], delta);
							}
						}
					});*/
			
					/*new Runnable() {
				{setDaemon(true);}
				public void run() {
					while (true) {

						int from = rnd.nextInt(accounts.length);
						int to = rnd.nextInt(accounts.length);
						if (from != to) {
							int delta = rnd.nextInt(50);
							  transfer(accounts[from], accounts[to], delta);
						}
					}
				}

				

			}*/
					
					
		}
		
		Thread.sleep(1000);
		System.out.println(sum(accounts));
		System.out.println(toStr(accounts));
		
	}
	
	private static void transfer(AccountLock from, AccountLock to, int amount) {
		
		AccountLock fst = (from.id < to.id) ? from : to;
		AccountLock snd = (from.id >= to.id) ? from : to;
		
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
	
	public static int sum(final AccountLock[] accounts) throws Exception {
		final AccountLock[] tmp = accounts.clone();
		final Comparator<AccountLock> comparator = (acc1,  acc2) -> {return acc1.id - acc2.id;};
		Arrays.sort(tmp, comparator);
		/*Arrays.sort(tmp, new Comparator<AccountLock>() {
			public int compare(AccountLock acc1, AccountLock acc2) {
				return acc1.id - acc2.id;
			}
			
		});*/
		
		
		
		return lockRecursively(tmp, () -> {
			int result = 0;
			for(AccountLock acc : tmp) {
				result += acc.getBalance();
			}
			return result;
		}
				
				
				/*new Callable<Integer>() {
			public Integer call() throws Exception {
				int result = 0;
				for(AccountLock acc : tmp) {
					result += acc.getBalance();
				}
				return result;
			}
			
		}*/);

		
	}
	
	public static String toStr(final AccountLock[] accounts) throws Exception {
		final AccountLock[] tmp = accounts.clone();
		Arrays.sort(tmp, (AccountLock acc1, AccountLock acc2) -> (acc1.id - acc2.id)
				
				/*new Comparator<AccountLock>() {

			public int compare(AccountLock acc1, AccountLock acc2) {
				return acc1.id - acc2.id;
			}}*/);
		
		return lockRecursively(tmp, () ->  Arrays.toString(tmp)
				
				/*new Callable<String>() {

			public String call() throws Exception {
				return Arrays.toString(tmp);
			}
			
		}*/);
	}
	
	private static <T> T lockRecursively(AccountLock[] accounts, Callable<T>callable) throws Exception {
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
