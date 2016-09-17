/**
 * 
 */
package accounts.multiverse.stm;

import static org.multiverse.api.StmUtils.atomic;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.multiverse.api.StmUtils;
import org.multiverse.api.Txn;
import org.multiverse.api.callables.TxnIntCallable;
import org.multiverse.api.exceptions.DeadTxnException;

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
	
	private static boolean transfer(Account from, Account to, int amount) {

		try {
			StmUtils.atomic(
				() -> {
					from.chgBalance(-amount);
					to.chgBalance(+amount);
				}
			);
			return true;
		} catch (DeadTxnException e) {
			return false;
		}
	}
	
	public static int sum(final Account[] accounts) {
		return atomic((Txn txn) ->

		{
			int result = 0;
			for (Account account : accounts) {
				result += account.getBalance();
			}
			return result;
		}

		);
	}
	
	public static String toStr(final Account[] accounts)  {
		return atomic((Txn txn)->{return Arrays.toString(accounts);});
	}
	
	
		
}
