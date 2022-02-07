package q6.Bakery;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class BakeryLock implements Lock {
	
	private static boolean[] choosing;
	private static int[] number;
	private int numThreads;
	
	private static ArrayList<AtomicBoolean> choosingAtomic;
	private static ArrayList<AtomicInteger> numberAtomic;
	

	
    public BakeryLock(int numThreads){
    	this.numThreads = numThreads;
    	
    	if ( choosing == null ) { 
    		choosing = new boolean[numThreads];
    	}
    	
    	if ( number == null ) {
    		number = new int[numThreads];
    	}
    	
    	if ( choosingAtomic == null ) {
    		choosingAtomic = new ArrayList<AtomicBoolean>();
            for (int j = 0; j < numThreads; j++) {
                choosingAtomic.add(new AtomicBoolean(false));
                
            }	
    	}

        
        if ( numberAtomic == null ) {
        	numberAtomic = new ArrayList<AtomicInteger>();
            for (int j = 0; j < numThreads; j++) {
            	numberAtomic.add(new AtomicInteger(0));
            }
        }

    	
    	
    }

	@Override
	public void lock(int pid) {
//		// Doorway
//		choosing[pid] = true;
//		for ( int j = 0; j < numThreads; j++ ){
//			if ( number[j] > number[pid] ) {
//				number[pid] = number[j];
//			}
//		}
//		number[pid]++;
//		choosing[pid] = false;
//		
//		//System.out.println("Thread " + pid + " number is " + number[pid] + " numbers are " + number[0] + " " + number[1]);
//		 
//		// Checking my # is smallest
//		for ( int j = 0; j < numThreads; j++ ) {
//			
//			if ( j == pid ) {
//				continue;
//			}
//			
//			while ( choosing[j] ) {
//				
//			}
//			while ((number[j] != 0) && 
//					(( number[j] < number[pid] ) || 
//					(( number[j] == number[pid] ) && j < pid))) {
//				try {
//					Thread.sleep(0);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		//System.out.println("Thread " + pid + " got lock");
		
//		// Doorway
//		choosing[pid] = true;
//		for ( int j = 0; j < numThreads; j++ ){
//			if ( number[j] > number[pid] ) {
//				number[pid] = number[j];
//			}
//		}
//		number[pid]++;
//		choosing[pid] = false;
//		
//		//System.out.println("Thread " + pid + " number is " + number[pid] + " numbers are " + number[0] + " " + number[1]);
//		 
//		// Checking my # is smallest
//		for ( int j = 0; j < numThreads; j++ ) {
//			
//			if ( j == pid ) {
//				continue;
//			}
//			
//			while ( choosing[j] ) {
//				
//			}
//			while ((number[j] != 0) && 
//					(( number[j] < number[pid] ) || 
//					(( number[j] == number[pid] ) && j < pid))) {
//				try {
//					Thread.sleep(0);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		//System.out.println("Thread " + pid + " got lock");
		
		// Doorway
		choosingAtomic.get(pid).set(true);
		for ( int j = 0; j < numThreads; j++ ){
			if ( numberAtomic.get(j).get() > numberAtomic.get(pid).get() ) {
				numberAtomic.get(pid).set(numberAtomic.get(j).get());
			}
		}
		numberAtomic.get(pid).set(numberAtomic.get(pid).get() + 1);
		choosingAtomic.get(pid).set(false);
		
		//System.out.println("Thread " + pid + " number is " + number[pid] + " numbers are " + number[0] + " " + number[1]);
		 
		// Checking my # is smallest
		int myNumber = numberAtomic.get(pid).get();
		for ( int j = 0; j < numThreads; j++ ) {
			
			if ( j == pid ) {
				continue;
			}
			
			while ( choosingAtomic.get(j).get() ) {
				
			}
			while ((numberAtomic.get(j).get() != 0) && 
					(( numberAtomic.get(j).get() < myNumber ) || 
					(( numberAtomic.get(j).get() == myNumber ) && j < pid))) {

			}
		}
		//System.out.println("Thread " + pid + " got lock");
		
	}

	@Override
	public void unlock(int pid) {
		//number[pid] = 0;
		numberAtomic.get(pid).set(0);
	}
}
