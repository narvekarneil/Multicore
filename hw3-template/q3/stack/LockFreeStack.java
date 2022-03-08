package stack;
import java.util.concurrent.atomic.AtomicReference;

public class LockFreeStack implements MyStack {
	// you are free to add members
	AtomicReference<Node> top = new AtomicReference<Node>(null);
	
  public LockFreeStack() {
	  // implement your constructor here
  }
	
  public boolean push(Integer value) {
	  // implement your push method here
	Node node = new Node(value);
	while(true) {
	   Node oldTop = top.get();
	   node.next = oldTop;
	   if (top.compareAndSet(oldTop, node)) {
		   return true;
	   }
	   else{
		   Thread.yield();
	   }
	 }
  }
  
  public Integer pop() throws EmptyStack {
	  // implement your pop method here
	while(true) {
	    Node oldTop = top.get();
	    if(oldTop == null) throw new EmptyStack();
	    Integer val = oldTop.value;
	    Node newTop = oldTop.next;
	    if(top.compareAndSet(oldTop, newTop)) {
	    	return val;
	    }
	    else {
	    	Thread.yield();
	    }
	}
  }
  
  protected class Node {
	  public Integer value;
	  public Node next;
		    
	  public Node(Integer x) {
		  value = x;
		  next = null;
	  }
  }
}
