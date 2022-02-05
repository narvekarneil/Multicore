package q6.AtomicInteger;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PIncrement implements Runnable{
	
	private int c;
	private static int numIncrements;
	public static AtomicInteger num = null;
	
	public PIncrement() {
	}
	
    public static int parallelIncrement(int c, int numThreads){
    	// Parallel increment for (b) Java’s AtomicInteger (with compareAndSet method).
		
		// Set values that will be used by threads
		int m = 1200000;
		numIncrements = m / numThreads;
		num = new AtomicInteger(c);
		
		List<Thread> threadList = new ArrayList<Thread>();
		Instant start = Instant.now();
		for ( int i = 0; i < numThreads; i++ ) {
			PIncrement R1 = new PIncrement();
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
		
		return num.get();
    }

	@Override
	public void run() {
		   for ( int i = 0; i < numIncrements; i++ ) {
			   // Use compareAndSet to increment i
			   boolean notIncremented = true;
			    while ( notIncremented ) {
			        int current = num.get();
			        int next = current + 1;
			        if (num.compareAndSet(current, next))
			            notIncremented = false;
			    }
		   }
		
	}
}
