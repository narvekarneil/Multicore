package q2;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monkey {

	public static ReentrantLock lock = new ReentrantLock();
	public static Condition ropeFull  = lock.newCondition(); 
	public static int monkeysOnRope = 0;
	public static int ropeDirection = -2; // When nobody is on rope
	public static boolean kong = false;
	
    public Monkey() {

    }

    public void ClimbRope(int direction) throws InterruptedException {
    	
    	// Check
    	lock.lock();
    	
    	// Kong
    	if ( direction == -1 ) {
    		kong = true;
    		// Wait if monkeys are on rope
    		while ( monkeysOnRope > 0 ) {
    			ropeFull.await();
    		}
    		kong = false;
    	} else {
	    	// Failed condition, too many monkeys or rope direction is wrong away
	    	while ( monkeysOnRope  >= 3 
	    			// Wait if given direction is not -2 (free) or myDirection
	    			|| ( ropeDirection  != direction && ropeDirection != -2 ) 
	    			|| kong ) {
	    		ropeFull.await();
	    	}
    	}
    	ropeDirection = direction;
    	monkeysOnRope++;
    	
    	lock.unlock();
    	
    	
    }

    public void LeaveRope() {
    	
    	// If you are the last monkey to leave rope, set direction to -1
    	lock.lock();
    	monkeysOnRope--;
    	if ( monkeysOnRope == 0 ) {
    		ropeDirection = -2;
    	}
    	ropeFull.signalAll();
    	lock.unlock();
    	

    }

    /**
     * Returns the number of monkeys on the rope currently for test purpose.
     *
     * @return the number of monkeys on the rope
     *
     * Positive Test Cases:
     * case 1: when normal monkey (0 and 1) is on the rope, this value should <= 3, >= 0
     * case 2: when Kong is on the rope, this value should be 1
     */
    public int getNumMonkeysOnRope() {
    	return monkeysOnRope;
    }

}
