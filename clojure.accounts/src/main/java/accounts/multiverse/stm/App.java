/**
 * 
 */
package accounts.multiverse.stm;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Could see different executions: 
 * 
 * 1000
1000
Transfering... 200
Transfering... 300
Transfering... 200

or 

1000
1000
Transfering... 300
Transfering... 200
Transfering... 200

or 

Transfering... 300
1000
1000
Transfering... 200
Transfering... 200

or 

1000
1000
Transfering... 200
Transfering... 300
Transfering... 300

or 

1000
1000
Transfering... 300
Transfering... 200
Transfering... 300
 */

//https://youtu.be/F7v077SDwcE
//Title1=Taming Shared Mutability with Software Transactional Memory
public class App {

	public static void main(String[] args) throws InterruptedException {

		Account account1 = new Account(1000);
		Account account2 = new Account(1000);

		
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		Thread.sleep(2000);

		executorService.submit(() -> AccountService.transfer(account1, account2, 300));
		executorService.submit(() -> AccountService.transfer(account1, account2, 200));

		System.out.println(account1.getBalance());
		System.out.println(account2.getBalance());

		executorService.shutdown();

	}
	
}