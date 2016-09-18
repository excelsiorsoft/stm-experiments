package accounts.multiverse.stm;

import clojure.lang.LockingTransaction;
import clojure.lang.Ref;

public class Account {

	private Ref balance;
	
	public Account(final int initialBalance) {
		balance = new Ref(initialBalance);
	}
	
	public void deposit(final int amount) throws Exception {
		LockingTransaction.runInTransaction(()->{
			if(amount > 0) {
				balance.set(getBalance() + amount);
				//System.out.println("Deposited "+amount+ " will it stay?");
			}
			return null;
		});
		/*if(amount > 0)
			balance.set(getBalance() + amount);*/
	}
	
	public void withdraw(final int amount) throws Exception {
		
		LockingTransaction.runInTransaction(()->{
			if(amount > 0 && getBalance() >= amount) {
				balance.set(getBalance() - amount);
			}else {
				throw new RuntimeException("not enough money!");
			}
			return null;
		});
		/*if(amount > 0 && getBalance() >= amount) {
			balance.set(getBalance() - amount);
			return true;
		}
		return false;*/
	}

	public  int getBalance() {
		return (Integer) balance.deref();
	}
	
}
