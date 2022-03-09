#include <stdio.h>
#include <stdlib.h>
#include <omp.h>
#include <math.h>

static int arr[100000000 + 1];
int Sieve(int N, int threads){
    int primeCount = 0;
    
    omp_set_num_threads(threads); 
    // initialize all as 1 (true)
    #pragma omp parallel for shared(arr)
    for ( int i = 0; i < N + 1; i ++){
        arr[i] = 1;
    }

    double sqrtN = sqrt(N);
    //#pragma omp parallel for shared(arr, sqrtN) 
    for (int i = 2; i <= sqrtN; i++){
        // if arr[i] is 1 then it is prime
        if (arr[i] == 1){
            // update multiples of this value to be not prime
            int i_squared = i*i;
            for (int j = i_squared; j <= N; j += i)
                arr[j] = 0;
        }
    }

    
    // print and count all primes
    #pragma omp parallel for shared(arr, primeCount) 
    for (int i = 2; i <= N; i++) {
        if (arr[i] == 1) {
            //printf("%d\n", p);
            primeCount++;
        }
    }

    printf("%d\n", primeCount);
    return primeCount;
    

}

void main(void) {
    int num_primes;
    int num_threads = 1;
    
    // find all primes to 100000000
    num_primes = Sieve(100000000, num_threads);

}


