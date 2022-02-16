package q1.c;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


public class PIncrement implements Runnable {

	public static MCSLock lock;
	private static int num;
	private static int numIncrements;
	ThreadLocal<Integer> pid;
	
	public PIncrement(int id) {
		this.pid = new ThreadLocal<Integer>() {
	        @Override protected Integer initialValue() {
	            return id;
	        }
		};
	}
	
    public static int parallelIncrement(int c, int numThreads) {
    	// Parallel increment for (C) MCS Lock

    	// Initialize values
    	num = c;
    	int m = 120000;
    	numIncrements = m / numThreads;
    	lock = new MCSLock(numThreads);
    	
		List<Thread> threadList = new ArrayList<Thread>();
		Instant start = Instant.now();
		for ( int i = 0; i < numThreads; i++ ) {
			PIncrement R1 = new PIncrement(i);
			Thread t = new Thread(R1);
			t.start();
			threadList.add(t);
		}		
		for ( int i = 0; i < numThreads; i++ ) {
			try {
				threadList.get(i).join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Time taken C: "+ timeElapsed.toMillis() +" milliseconds");
		
		return num;
    }
 
	public static class MCSLock {
		
		class QNode {
			boolean locked;
			QNode next;
			QNode() {
				locked = true;
				next = null;
			}
		}
		AtomicReference<QNode> tailNode = new AtomicReference<QNode>(null);
		ThreadLocal<QNode> myNode;
		
		public MCSLock(int numThreads) {
			myNode = new ThreadLocal<QNode> () {
				protected QNode initialValue() {
					return new QNode();
				}
			};
		}
		
		public void lock() {
			QNode pred = tailNode.getAndSet(myNode.get());
			if ( pred != null ) {
				myNode.get().locked = true;
				pred.next = myNode.get();
				while ( myNode.get().locked ) {
					Thread.yield();
				}
			}
		}
    	
    	public void unlock() {
    		if ( myNode.get().next == null ) {
    			if ( tailNode.compareAndSet(myNode.get(), null)) {
    				return;
    			}
    			while ( myNode.get().next == null ) {
    				Thread.yield();
    			}
    		}
    		myNode.get().next.locked = false;
    		myNode.get().next = null;
    	}
    }
	

	@Override
	public void run() {
		int threadID = pid.get();
		for ( int i = 0; i < numIncrements; i++ ) {
			lock.lock( );
			num++;
			//System.out.println("Num is " + num);
			lock.unlock( );
		}
	}

}
