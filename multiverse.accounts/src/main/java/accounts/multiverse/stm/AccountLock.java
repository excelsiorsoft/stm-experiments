/**
 * 
 */
package accounts.multiverse.stm;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Simeon
 *
 */
public class AccountLock {

	private final static AtomicInteger idGenerator = new AtomicInteger();
	public final int id = idGenerator.getAndIncrement();
	public final Lock lock = new ReentrantLock();
	private int balance;
	
	public AccountLock(int balance) {
		this.balance = balance;
	}
	
	public boolean chgBalance(int amount) {
		if(balance + amount>=0) {
			balance += amount;
			return true;
		}else {
			return false;
		}
	}
	
	
	public int getBalance() {
		return balance;
	}
	
	@Override
	public String toString() {
		return "AccountLock [id=" + id + ", balance=" + balance + "]";
	}

}
