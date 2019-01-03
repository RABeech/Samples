/**
 * Created by rabeech on 2/8/17.
 */

import edu.princeton.cs.algs4.*;
import java.util.*;

public class Recursion {
    /** Recursively computes and returns the sum from 1 to n.
     * @param n the starting number for the recursive function
     * @return the sum from 1 to n */
    public static int sum( int n ){
        
        if ( n == 1) return 1;
        
        return sum(n-1) + n;
        
    }
    
    /** Recursively computes and returns the factorial n!.
     * @param n the factorial to compute
     * @return the factorial n! */
    public static int fact( int n ){
        
        if ( n == 1) return 1;
        
        return fact(n-1) * n;
        
    }
    
    /** Recursively computes and returns the n-th fibonacci number.
     * @param n the fibonacci number to compute
     * @return the n-th fibonacci number */
    public static long fib( int n ){
        
        if ( n == 1) return 1;
        if ( n == 0) return 0;
        
        return fib(n-1) + fib(n-2);
        
    }
    
    /** Sequentially computes and returns the n-th fibonacci number.
     * @param n the fibonacci number to compute
     * @return the n-th fibonacci number */
    public static long fib_seq( int n ){
        
         if ( n == 1 ) return 1;
         if ( n == 0 ) return 0;
         
         long a = 0;
         long b = 1;
         long temp = 0;
         
         for (int i = 2; i <= n; i++){
             temp = a + b;
             a = b;
             b = temp;                        
         }
         
        return temp;
    }
    
    /** MAIN */
    public static void main(String[] args) {
        // Compute the sum from 1 to n for n = 1 to 10
        StdOut.println("Sum:");
        for (int i = 1; i <= 10; i++) {
            StdOut.print(i + " => " + sum(i) + "\n");
        }
        
        // Compute the factorial n! for n = 1 to 10
        StdOut.println("Factorial:");
        for (int i = 1; i <= 10; i++) {
            StdOut.print(i + " => " + fact(i) + "\n");
        }
        
        // Compute the n-th fibonacci number recursively for n = 1 to 10
        StdOut.println("Fibonacci:");
        Stopwatch timer = new Stopwatch();
        for (int i = 1; i <= 50; i++) {
            StdOut.print(i + " => " + fib(i) + "\n");
        }
        double elapsedTime = timer.elapsedTime();
        System.out.println(elapsedTime);
        
        // Compute the n-th fibonacci number sequentially for n = 1 to 10
        StdOut.println("Fibonacci (seq):");
        Stopwatch stimer = new Stopwatch();
        for (int i = 1; i <= 50; i++) {
            StdOut.print(i + " => " + fib_seq(i) + "\n");
        }
        double selapsedTime = stimer.elapsedTime();
        System.out.println(selapsedTime);
    
    }
    
    /** Helper stopwatch class */
    private static class Stopwatch{
        private final long start;
        public Stopwatch(){ start = System.nanoTime(); }
        public double elapsedTime(){
            return (double)(System.nanoTime() - start) * 1e-9;
        }
    }
}
