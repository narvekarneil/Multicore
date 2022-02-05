package q6.Bakery;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import q6.Bakery.BakeryLock;


public class PIncrement implements Runnable{
	
	private static int num;
	private static int numIncrements;
	private static BakeryLock lock;
	private int pid;
	
	public PIncrement ( int pid ) {
		this.pid = pid;
	}
	
    public static int parallelIncrement(int c, int numThreads){
    	// Parallel increment for (a) Lamport’s Bakery Algorithm.

    	// Initialize values
    	num = c;
    	int m = 1200000;
    	numIncrements = m / numThreads;
    	lock = new BakeryLock(numThreads);
    	
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

	@Override
	public void run() {
		for ( int i = 0; i < numIncrements; i++ ) {
			lock.lock(pid);
			num++;
			//System.out.println("Num is " + num);
			lock.unlock(pid);
		}		
	}
}
