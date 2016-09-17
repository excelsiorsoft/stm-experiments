package latch;

import static org.multiverse.api.StmUtils.atomic;
import static org.multiverse.api.StmUtils.retry;

import org.multiverse.api.StmUtils;
import org.multiverse.api.references.TxnBoolean;

public class LatchOnSTM {

	private TxnBoolean open = StmUtils.newTxnBoolean(false);
	
	
	public boolean isOpen() {
		return atomic(()-> open.get());
	}
	
	public void doOpen() {
		atomic(()-> open.set(true));
	}
	
	public void await() {
		//this is not a busy waiting (spin lock), not a "while(!volatileFlag)"
		StmUtils.atomic(() -> {if(!open.get())retry();});
	}
}
