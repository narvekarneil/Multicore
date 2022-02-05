package q6.ReentrantLock;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


public class PIncrement implements Runnable{
	
	private static ReentrantLock lock = new ReentrantLock();
	private static int num;
	private static int numIncrements;
	
    public static int parallelIncrement(int c, int numThreads){
    	// Parallel increment for (d) Java’s Reentrant Lock
    	
    	// Initialize values
    	num = c;
    	int m = 1200000;
    	numIncrements = m / numThreads;
    	
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
		System.out.println("Time taken D: "+ timeElapsed.toMillis() +" milliseconds");
		
		return num;
    }

	@Override
	public void run() {
		for ( int i = 0; i < numIncrements; i++ ) {
	        lock.lock();
	        try
	        {
	            num++;
	        }
	        catch(Exception e)
	        {
	            e.printStackTrace();
	        }
	        finally
	        {
	            lock.unlock();
	        }		
		}

   }
}

