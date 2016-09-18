package treiber.stack;

import org.multiverse.api.StmUtils;
import org.multiverse.api.references.TxnRef;

public class TreiberTxnStack<E> {

	private final TxnRef<Node<E>> top = StmUtils.newTxnRef(null);
	
	public void push(E elem) {
		top.set(new Node<>(top.get(), elem));
	}
	
	public E pop() {
		E elem = top.get().value;
		top.set(top.get().next);
		return elem;
	}
	
	public boolean isEmpty() {
		return top.get() == null;
	}
	
	private static class Node<E>{
		final Node<E> next;
		final E value;
		Node(Node<E> next, E value){
			this.next = next;
			this.value = value;
		}
	}
	
}
