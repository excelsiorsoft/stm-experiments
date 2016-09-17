/**
 * 
 */
package accounts.multiverse.stm;

import static org.multiverse.api.StmUtils.abort;

import org.multiverse.api.StmUtils;
import org.multiverse.api.references.TxnInteger;

/**
 * @author Simeon
 *
 */
public class Account {

	
	private final TxnInteger balance;
	
	public Account(int balance) {
		this.balance = StmUtils.newTxnInteger(balance);
	}
	
	public void chgBalance(final int amount) {
		balance.increment(amount);
		if(balance.get() < 0) {
			abort();
		}
	}
	
	
	public int getBalance() {
		return balance.get();
	}
	
	@Override
	public String toString() {
		return "AccountLock [balance=" + balance + "]";
	}

}
