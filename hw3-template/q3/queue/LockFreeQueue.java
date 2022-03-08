package queue;

import java.util.concurrent.locks.ReentrantLock;

public class LockFreeQueue implements MyQueue {
// you are free to add members
	
	ReentrantLock enqLock, deqLock;
	Node head;
	Node tail;
	int size;

  public LockFreeQueue() {
	// implement your constructor here
    head = new Node(null);
    tail = head;
    enqLock = new ReentrantLock();
    deqLock = new ReentrantLock();
  }

  public boolean enq(Integer value) {
	// implement your enq method here
    if (value == null) throw new NullPointerException();
    enqLock.lock();
    try {
      Node e = new Node(value);
      tail.next = e;
      tail = e;
    } finally {
      enqLock.unlock();
    }
    return true;
  }
  
  public Integer deq() {
	// implement your deq method here
    Integer result = null;
    deqLock.lock();
    try {
      if (head.next == null) {
        throw new Exception();
      }
      result = head.next.value;
      head = head.next;
    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} finally {
      deqLock.unlock();
    }
    return result;
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
