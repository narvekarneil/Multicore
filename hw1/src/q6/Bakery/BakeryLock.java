package q6.Bakery;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BakeryLock implements Lock {
	
	private static boolean[] choosing;
	private static int[] number;
	private int numThreads;

	final static ReentrantLock lock = new ReentrantLock();
	final static Condition waitHere = lock.newCondition(); 

	
    public BakeryLock(int numThreads){
    	this.numThreads = numThreads;
    	
    	if ( choosing == null ) { 
    		choosing = new boolean[numThreads];
    	}
    	
    	if ( number == null ) {
    		number = new int[numThreads];
    	}
    	
    }

	@Override
	public void lock(int pid) {
		// Doorway
		choosing[pid] = true;
		for ( int j = 0; j < numThreads; j++ ){
			if ( number[j] > number[pid] ) {
				number[pid] = number[j];
			}
		}
		number[pid]++;
		choosing[pid] = false;
		
		System.out.println("Thread " + pid + " number is " + number[pid] + " numbers are " + number[0] + " " + number[1]);
		 
		// Checking my # is smallest
		int x = 0;
		for ( int j = 0; j < numThreads; j++ ) {
			
			if ( j == pid ) {
				continue;
			}
			
			while ( choosing[j] ) {
				
			}
			while ( number[j] != 0 && ( number[j] < number[pid] || (number[j] == number[pid] && j < pid))) {
			}
		}
		//System.out.println("Thread " + pid + " got lock");
		
	}

	@Override
	public void unlock(int pid) {
		number[pid] = 0;
	}
}
