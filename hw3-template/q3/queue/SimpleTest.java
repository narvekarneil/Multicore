package queue;
import org.junit.Assert;
import org.junit.Test;
import queue.LockFreeQueue;
import queue.LockQueue;
import stack.EmptyStack;
import stack.LockFreeStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;

public class SimpleTest {

    public static void main(String[] args)
    {
        SimpleTest t = new SimpleTest();
         t.testLockFreeQueue();
        //t.testLockQueue();
        //t.testLockFreeStack();
    }

    public void testLockQueue() {
        List<Thread> list = new ArrayList<>();
        LockQueue q = new LockQueue();
        for(int i = 0; i < 8; i++) {
            int finalI = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    q.enq(finalI);
                    q.enq(finalI);
                    int dq = q.deq();
                    System.out.println("deq: " + dq);
                }
            });
            t.start();
            list.add(t);
        }

        for(Thread t : list) {
            try {
                t.join();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void testLockFreeQueue() {
        List<Thread> list = new ArrayList<>();
        LockFreeQueue q = new LockFreeQueue();
        for(int i = 0; i < 3; i++) {
            int finalI = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    q.enq(finalI);
                    q.enq(finalI);
                    q.enq(finalI);
                    int dq = q.deq();
                    System.out.println("deq: " + dq);
                    dq = q.deq();
                    System.out.println("deq: " + dq);
                    dq = q.deq();
                    System.out.println("deq: " + dq);
                }
            });
            t.start();
            list.add(t);
        }

        for(Thread t : list) {
            try {
                t.join();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void testLockFreeStack() {
        List<Thread> list = new ArrayList<>();
        LockFreeStack stack = new LockFreeStack();
        for(int i = 0; i < 2; i++) {
            int finalI = i;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    int val = (int)(Math.random() * 24);
                    stack.push(val);
                    val = (int)(Math.random() * 24);
                    stack.push(val);
                    val = (int)(Math.random() * 24);
                    stack.push(val);
                    val = (int)(Math.random() * 24);
                    stack.push(val);
                    val = (int)(Math.random() * 24);
                    stack.push(val);
                    try {
                        stack.pop();
                        stack.pop();
                        stack.pop();
                    } catch (EmptyStack emptyStack) {

                    }
                }
            });
            t.start();
            list.add(t);
        }

        for(Thread t : list) {
            try {
                t.join();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

    }


}