package q2;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monkey {

	public static ReentrantLock lock = new ReentrantLock();
	public static Condition ropeFull  = lock.newCondition(); 
	public static AtomicInteger monkeysOnRope;
	public static AtomicInteger ropeDirection;
	
    public Monkey() {
    	monkeysOnRope = new AtomicInteger(0);
    	ropeDirection = new AtomicInteger(-1);

    }

    public void ClimbRope(int direction) throws InterruptedException {
    	
    	// Check
    	lock.lock();
    	System.out.println("HERE");
    	// Failed condition, too many monkeys or rope direction is wrong away
    	while ( monkeysOnRope.get() >= 3 || ropeDirection.get() != direction ) {
    		ropeFull.await();
    	}
    	System.out.print(direction);
    	ropeDirection.set(direction);
    	monkeysOnRope.incrementAndGet();
    	
    	lock.unlock();
    	
    	
    }

    public void LeaveRope() {
    	
    	// If you are the last monkey to leave rope, set direction to -1
    	lock.lock();
    	if ( monkeysOnRope.decrementAndGet() == 0 ) {
    		ropeDirection.set(-1);
    	}
    	System.out.println("leaving");
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
    	return monkeysOnRope.get();
    }

}
