package queue;

import java.util.concurrent.locks.ReentrantLock;

public class LockQueue implements MyQueue {
// you are free to add members

	Queue Q = new Queue();
	
  public LockQueue() {
	// implement your constructor here
	  
	  
	Node node = new Node(null);		// Allocate a free node
	node.next = null;          // Make it the only node in the linked list
	Q.head = node;	// Both Head and Tail point to it
	Q.tail = node;
	//Q->H_lock = Q->T_lock = FREE	// Locks are initially free
  }
  
  public boolean enq(Integer value) {
	// implement your enq method here
	Node node = new Node(value);	        // Allocate a new node from the free list
    //node->value = value		// Copy enqueued value into node
    node.next = null;          // Set next pointer of node to NULL
    Q.tailLock.lock(); // Acquire T_lock in order to access Tail
    Q.tail.next = node; // Link node at the end of the linked list
    Q.tail = node;		// Swing Tail to node
    // System.out.println("enq: " + value);
    Q.tailLock.unlock(); // Release T_lock
    return true;
  }
  
  public Integer deq() {
	// implement your deq method here   
	Q.headLock.lock(); // Acquire H_lock in order to access Head
    Node node = Q.head;		// Read Head
    Node new_head = node.next;	// Read next pointer
    if (new_head == null) {	// Is queue empty?
       Q.headLock.unlock(); // Release H_lock before return
       return null;		// Queue was empty
    }
    Integer pvalue = new_head.value;
    Q.head = new_head; // Swing Head to next node
    Q.headLock.unlock(); // Release H_lock
    return pvalue;		// Queue was not empty, dequeue succeeded
  }
  
  protected class Queue {
	  public Node head;
	  public Node tail;
	  public ReentrantLock headLock = new ReentrantLock();
	  public ReentrantLock tailLock = new ReentrantLock();
	  
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
