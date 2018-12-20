import edu.princeton.cs.algs4.*;

public class Fibonacci{
    public static void main(String[]args){
        /* TASK 1:
         * Compare the speeds of the recursive and memoized Fibonacci methods
         * to compute 5, 10, 15, ... , 40 numbers in the Fibonacci sequence.
         * That is, compute each number in the Fibonacci sequence (n = 1 to 40),
         * and print the current total runtime at n = 5, 10, 15, ... , 40.
         * 
         * TASK 2:
         * Use the recursive Fibonacci method to compute f(50). Print this 
         * number and the time taken to compute it.
         * 
         * TASK 3:
         * Use the memoized Fibonacci method to compute f(90). Print this 
         * number and the time taken to compute it.
         */
        
        Stopwatch timer;            // stopwatch
        double recursiveTime = 0.0; // recursive method runtime
        double memoizedTime = 0.0;  // memoized method runtime
        long f;                     // fibonacci number
        
        
        for (int i = 0; i <= 40; i++){
           
           timer = new Stopwatch();
           recursiveFibonacci(i);
           recursiveTime += timer.elapsedTime();
           
           if (i % 5 == 0){
              StdOut.println("Recursive RecFibonacci Number " + i + ": " + recursiveTime);
           }
           
           timer = new Stopwatch();
           memoizedFibonacci(i);
           memoizedTime += timer.elapsedTime();
           
           if (i % 5 == 0){
              StdOut.println("Memo RecFibonacci Number " + i + ": " + memoizedTime);
              StdOut.println();
           }
        }
        
        timer = new Stopwatch();
        f = recursiveFibonacci(50);
        recursiveTime = timer.elapsedTime();
        StdOut.println();
        StdOut.println("Recursive RecFibonacci Number 50: " + recursiveTime);
        StdOut.println("Fib number : " + f);
        
        timer = new Stopwatch();
        f = memoizedFibonacci(90);
        memoizedTime = timer.elapsedTime();
        StdOut.println();
        StdOut.println("Memo RecFibonacci Number 90: " + memoizedTime);
        StdOut.println("Fib number : " + f);

        
            
    }
    
    /* -------------------------------------------------------------------------
     * STOPWATCH CLASS - nanosecond resolution
     * ---------------------------------------------------------------------- */
    private static class Stopwatch{
        private static long start;
        public Stopwatch(){ start = System.nanoTime(); }
        public double elapsedTime(){
            return (double)(System.nanoTime() - start) * 1e-9;
        }
    }
    
    /* -------------------------------------------------------------------------
     * RECURSIVE FIBONACCI
     * ---------------------------------------------------------------------- */
    
    /** Recursively computes the n-th Fibonacci number
     * @param n the Fibonacci number to compute
     * @return the n-th Fibonacci number */
    public static long recursiveFibonacci(int n){
        
        long f;
        
        if (n <= 2){
         f = 1;
        }
        else{
         f = recursiveFibonacci(n-1) + recursiveFibonacci(n-2);
        }
        
        return f;
        
    }
    
    /* -------------------------------------------------------------------------
     * MEMOIZED FIBONACCI
     * ---------------------------------------------------------------------- */
    
    /** Computes the n-th Fibonacci number via memoized dynamic programming
     * @param n the Fibonacci number to compute
     * @return the n-th Fibonacci number */
    public static long memoizedFibonacci(int n){
        long[] mem = new long[n+1];         // init. mem[n] = 0 for all n
        return memoizedFibonacci(mem, n);
    }
    
    private static long memoizedFibonacci(long[] mem, int n){
        
        long f;
        
        if (mem[n]!=0){
         return mem[n];
        }
        
        if (n <= 2){
         f = 1;
        }else{
         f = memoizedFibonacci(mem, n-1) + memoizedFibonacci(mem, n-2); 
        }
        mem[n] = f;
        return f;
        
    }
}