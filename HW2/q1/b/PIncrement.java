package q1.b;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;




public class PIncrement implements Runnable {

	public static CLHLock lock;
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
		// Parallel increment for (B) CLH Lock

    	// Initialize values
    	num = c;
    	int m = 120000;
    	numIncrements = m / numThreads;
    	lock = new CLHLock(numThreads);
    	
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
		System.out.println("Time taken B: "+ timeElapsed.toMillis() +" milliseconds");
		
		return num;
    }
    
public static class CLHLock {
    	
		class Node {
			boolean locked;
		}
    	public int numThreads;
    	AtomicReference<Node> tailNode;
    	ThreadLocal<Node> myNode;
    	ThreadLocal<Node> pred;
    	
    	public CLHLock(int numThreads) {
    		tailNode = new AtomicReference<Node>(new Node());
    		myNode = new ThreadLocal<Node>() {
    	        @Override protected Node initialValue() {
    	            return new Node();
    	        }
    		};
    		pred = new ThreadLocal<Node>() {
    	        @Override protected Node initialValue() {
    	            return new Node();
    	        }
    		};
    	}

		public void lock(int pid) {
			myNode.get().locked = true;
			pred.set( tailNode.getAndSet(myNode.get()));
			while ( pred.get().locked) {
				
			}
    	}
    	
    	public void unlock(int pid) {
    		myNode.get().locked = false;
    		myNode.set( pred.get() );
    	}
    }

	@Override
	public void run() {
		int threadID = pid.get();
		for ( int i = 0; i < numIncrements; i++ ) {
			lock.lock( threadID );
			num++;
			//System.out.println("Num is " + num);
			lock.unlock( threadID );
		}
	}

}
