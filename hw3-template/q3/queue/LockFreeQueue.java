package queue;

import java.util.concurrent.atomic.AtomicReference;

import queue.LockQueue.Queue;

public class LockFreeQueue implements MyQueue {
// you are free to add members
	
	Node dummy = new Node(null, new AtomicReference<Node>(null));
	AtomicReference<Node> head = new AtomicReference<>(dummy);
	AtomicReference<Node> tail = new AtomicReference<>(dummy);
	
  public LockFreeQueue() {
	// implement your constructor here
//	Node node = new Node(null);		// Allocate a free node
//	node.next.ptr = null;	// Make it the only node in the linked list
//	Q.head.ptr = Q.tail.ptr = node;	// Both Head and Tail point to it
  }

  public boolean enq(Integer value) {
	// implement your enq method here
	Node newNode = new Node(value, new AtomicReference<Node>(null));
	while (true ) {
		
		Node currentTail = tail.get();
		Node tailNext = currentTail.next.get();
		if ( currentTail == tail.get()) {
			if ( tailNext != null ) {
				tail.compareAndSet(currentTail, tailNext);
			} else {
				if ( currentTail.next.compareAndSet(null, newNode) ) {
					tail.compareAndSet(currentTail, newNode);
					return true;
				}
			}
		}
	}
//	Node newNode = new Node(value, new AtomicReference<Node>(null));
//	boolean success;
//	do {
//		Node curTail = tail.get();
//		success = curTail.next.compareAndSet(null,  newNode);
//		tail.compareAndSet(curTail, curTail.next.get());
//	} while (!success);
//	return true;
  }
  
  public Integer deq() {
	// implement your deq method here
	while (true ) {
		Node first = head.get();
		Node last = tail.get();
		Node next = first.next.get();
		if ( first == head.get()) {
			if ( first == last ) {
				if ( next == null ) {
					return null;
				}
				tail.compareAndSet(last, next);
			} else {
				Integer item = next.value;
				if ( head.compareAndSet(first, next)) {
					return item;
				}
			}
		}
	}
  }
  
  protected class Node {
	  public Integer value;
	  public AtomicReference<Node> next = new AtomicReference<Node>(null);
		    
	  public Node(Integer x, AtomicReference<Node> next) {
		  this.value = x;
		  this.next = next;
	  }
  }
  
}
