package q5;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Frequency implements Callable<Integer> {
	private static int num_to_count;
	private static int[] arr;
	private static int freq;
	
	private List<Integer> part;
	
	public Frequency ( int num_to_count, List<Integer> list ) {
		this.part = list;
	}
	
    public static int parallelFreq(int x, int[] A, int numThreads){
        //your implementation goes here, return -1 if the input is not valid.
    	
    	num_to_count = x;
    	arr = A;
    	
		// Break up into subarrays
		// From: https://stackoverflow.com/questions/13678387/how-to-split-array-list-into-equal-parts
		List<Integer> A_arrlist = new ArrayList<Integer>(); // Make it into arraylist to use .sublist
		for (int i = 0; i < A.length; i++) {
			A_arrlist.add(A[i]);
		}
		
		int partitionSize = A.length / numThreads;
		ArrayList<List<Integer>> partitions = new ArrayList<List<Integer>>();
		int numLoops = 0;
		for (int i = 0; i < A_arrlist.size(); i += partitionSize) {
			// If this is last subarray being made, add all rest of elements
			if (numLoops == numThreads-1){
				partitions.add(A_arrlist.subList(i, A.length));
				break;
			}
			// Add partitions as normal
			else {
				partitions.add(A_arrlist.subList(i, i + partitionSize));
			}
			numLoops++;

		}
    	
    	ExecutorService es = Executors.newFixedThreadPool(numThreads);
    	List<Future<Integer>> futures = new ArrayList<Future<Integer>>();
    	for ( int i = 0; i < partitions.size(); i++ ) {
	        Frequency f = new Frequency(num_to_count, partitions.get(i));
	        Future<Integer> future = es.submit(f);
	        futures.add(future);
    	}
    	int total_freq = 0;
    	for ( int i = 0; i < futures.size(); i++ ) {
    		try {
				total_freq = total_freq + futures.get(i).get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
    	}
    	
    	
    	return total_freq;
    }

	@Override
	public Integer call() throws Exception {
		// Called when exec service called
		System.out.println(part);
		int freq = 0;
		for ( int i = 0; i < part.size(); i++ ) {
			if ( part.get(i) == num_to_count ) {
				freq++;
			}
		}
		return freq;	
	}  
}
