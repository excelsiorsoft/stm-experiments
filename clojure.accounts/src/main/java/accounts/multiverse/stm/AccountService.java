package accounts.multiverse.stm;

import clojure.lang.LockingTransaction;

public class AccountService {

	public static void transfer(final Account from, final Account to,
			final int amount) /*throws Exception*/ {

		try {
			LockingTransaction.runInTransaction(() -> {
				System.out.println("Transfering... " + amount);
				to.deposit(amount);
				from.withdraw(amount);
				return null;
			});
		} catch (Exception e) {
			System.out.println("oops: " + e);
		}

	}

}
