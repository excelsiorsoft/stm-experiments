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
		
		//will succeed
		AccountService.transfer(account1, account2, 50);
		System.out.println(account1.getBalance());
		System.out.println(account2.getBalance());
		
		//should fail
		AccountService.transfer(account1, account2, 5000);
		System.out.println(account1.getBalance());
		System.out.println(account2.getBalance());
	}
}
