package q1.a;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class PIncrement implements Runnable {

	public static AndersonLock lock;
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
    	// Parallel increment for (a) Anderson Lock

    	// Initialize values
    	num = c;
    	int m = 120000;
    	numIncrements = m / numThreads;
    	lock = new AndersonLock(numThreads);
    	
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
		System.out.println("Time taken A: "+ timeElapsed.toMillis() +" milliseconds");
		
		return num;
    }
    
    public static class AndersonLock {
    	
    	public int numThreads;
    	public boolean[] available;
    	public AtomicInteger tailSlot;
    	public ThreadLocal<Integer> mySlot;
    	
    	public AndersonLock(int numThreads) {
    		
			this.numThreads = numThreads;
			tailSlot = new AtomicInteger(0);
			available = new boolean[numThreads];
			mySlot = new ThreadLocal<Integer>();
			mySlot.set(0);
			
			// Set all of available to false except 0
			for ( int i = 0; i < available.length; i++ ) {
				if ( i == 0 ) {
					available[i] = true;
				} else {
					available[i] = false;
				}
				
			}
    	}

		public void lock(int pid) {
			mySlot.set( tailSlot.getAndIncrement() % this.numThreads );
			while ( !available[mySlot.get()] ) {
				
			}
    	}
    	
    	public void unlock(int pid) {
    		available[mySlot.get()] = false;
    		available[(mySlot.get()+1) % this.numThreads] = true;
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
