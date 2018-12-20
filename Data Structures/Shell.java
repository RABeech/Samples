/**
 * Created by mcshin on 2/2/17.
 */

import edu.princeton.cs.algs4.*;
import java.util.*;

public class Shell {
    /** Sorts an array of comparable elements in ascending order using the
     * shell sort algorithm
     * @param a an array of comparable elements */
    public static void sort( Comparable[] a ){
        
        int N = a.length;
        int h = 1;
      
        while (h < N/3) h = 3*h + 1;
      
        while (h >= 1)
        {
          for (int i = h; i < N; i++)
          {
            for (int j = i; j >= h && less(a[j], a[j-h]); j -= h)
               exch(a,j,j-h);
          }
          h = h/3;
        }
         
    }
    
    /* -------------------------------------------------------------------------
     * HELPER SORTING METHODS
     * ---------------------------------------------------------------------- */
    
    /** Returns whether the first parameter element is less than the second
     * @param v a comparable element
     * @param w a comparable element
     * @return true if v is less than w, false otherwise */
    private static boolean less( Comparable v, Comparable w ){
        return v.compareTo(w) < 0;
    }
    
    /** Exchanges the elements located at the specified indices in the array
     * @param a an array of comparable elements
     * @param i the index of the first element
     * @param j the index of the second element */
    private static void exch( Comparable [] a, int i, int j ){
        Comparable t = a[i]; a[i] = a[j]; a[j] = t;
    }
    
    /* -------------------------------------------------------------------------
     * TEST METHODS (useful for debugging)
     * ---------------------------------------------------------------------- */
    
    /** Returns whether the array is sorted
     * @param a an array of comparable elements
     * @return true if the array is sorted, false otherwise */
    public static boolean isSorted( Comparable[] a ){
        for(int i = 1; i < a.length; i++){
            if( less(a[i], a[i-1]) ) return false;
        }
        return true;
    }
    
    /** Prints the contents of the array to standard output (terminal)
     * @param a an array of comparable elements */
    public static void show( Comparable [] a ){
        for(int i = 1; i < a.length; i++){
            StdOut.println( a[i] );
        }
    }
    
    /** Draws a rendering of values at each iteration of shell sort
     * @param a an array of comparable elements */
    public static void sortAndDraw( Comparable[] a ){
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize( 500, 500 );
        StdDraw.setXscale(0, a.length - 1);
        StdDraw.setYscale(0, 1 );

        int N = a.length;

        // Find max h to start with
        int h = 1;
        while ( h < N/3 )
            h = 3*h + 1;

        while ( h >= 1 ) {
            for ( int i = h; i < N; i++ ) {
                for ( int j = i; j >= h && less(a[j], a[j-h]); j -= h )
                    exch ( a, j, j-h );

                StdDraw.clear();

                String txt = "h = " + h + "   i = " + i;
                StdDraw.textLeft ( 10, 0.9, txt);

                for (int j = 0; j < N; j++ ) {
                    StdDraw.line(j, 0, j, (double) a[j]);
                }

                StdDraw.show();
                StdDraw.pause ( 20 );
            }
            h = h/3;
        }
    }
    
    /* -------------------------------------------------------------------------
     * MAIN
     * ---------------------------------------------------------------------- */
    public static void main( String[] args ){
    
    int[] size = {1000, 2000, 4000, 8000, 16000, 32000, 64000};
        
        Double[] a, b, c = null;
        double stotal = 0;
        double itotal = 0;
        double sltotal = 0;
        
        double sfinal = 0;
        double ifinal = 0;
        double slfinal = 0;
         
            for( int i = 0; i < size.length; i++ ){
               int N = size[i];
               
               for (int k = 1; k <= 10; k++){
               
               sfinal = 0;
               ifinal = 0;
               slfinal = 0;
             
               // Create an array of size N and assign random values
               a = new Double[N];
               for( int j = 0; j < N; j++ )
                   a[j] = StdRandom.uniform();
                    
                    // Create copy of arrays for Insertion/Selection Sort
                    b = Arrays.copyOf(a, a.length);
                    c = Arrays.copyOf(a, a.length);
                    
                    // Start sorts
                    Stopwatch timer = new Stopwatch();
                    Shell.sort( a );
                    double elapsedTime = timer.elapsedTime();
                    stotal += elapsedTime;
                    
                    Stopwatch itimer = new Stopwatch();
                    Insertion.sort( b );
                    double ielapsedTime = timer.elapsedTime();
                    itotal += ielapsedTime;
                    
                    Stopwatch stimer = new Stopwatch();
                    Selection.sort( c );
                    double selapsedTime = timer.elapsedTime();
                    sltotal += selapsedTime;
                    
               }
               sfinal = stotal/10;
               ifinal = itotal/10;
               slfinal = sltotal/10;
       
               System.out.println(size[i]);
               System.out.println(sfinal);
               System.out.println(ifinal);
               System.out.println(slfinal);     
              
            } 
       
          
    }// End main
    // check out removing mulit elapsed time
        
        
        
    
    
    /* -------------------------------------------------------------------------
     * STOPWATCH & PREVIOUS SORTING ALGORITHMS
     * ---------------------------------------------------------------------- */
 
    private static class Stopwatch{
        private final long start;
        public Stopwatch(){ start = System.nanoTime(); }
        public double elapsedTime(){
            return (double)(System.nanoTime() - start) * 1e-9;
        }
    }
    
    private static class Insertion{
        public static void sort( Comparable[] a ){
            int N = a.length;
            for(int i = 0; i < N; i++)
                for(int j = i; j > 0 && less(a[j],a[j-1]); j--)
                    exch(a,j,j-1);
        }
    }
    
    private static class Selection{
        public static void sort( Comparable[] a ){
            int N = a.length;
            for(int i= 0; i < N; i++){
                int min = i;
                for(int j = i+1; j < N; j++)
                    if(less(a[j],a[min])) min = j;
                exch(a,i,min);
            }
        }
    }
}
