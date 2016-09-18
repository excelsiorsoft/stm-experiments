/**
 * 
 */
package accounts.multiverse.stm;

import clojure.main;

/**
 * @author Simeon
 *
 */
public class App {

	public static void main(String[] args) throws Exception {
		
		Account account1 = new Account(1000);
		Account account2 = new Account(1000);
		
		AccountService.transfer(account1, account2, 500);
		System.out.println(account1.getBalance());
		System.out.println(account2.getBalance());
	}
}
