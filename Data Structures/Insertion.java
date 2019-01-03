import edu.princeton.cs.algs4.*;
import java.util.*;

public class Insertion {
    
    /** Sorts an array of comparable elements in ascending order using the
     * insertion sort algorithm
     * @param a an array of comparable elements */
    public static void sort( Comparable[] a ){
        
        int n = a.length;
        
        for (int i = 1; i < n; i++){
            for (int j = i; j > 0 && less(a[j], a[j-1]); j--)
               exch (a, j, j-1);   
        }
        
    }
    
    /** Sorts an array of comparable elements in descending order using the
     * insertion sort algorithm
     * @param a an array of comparable elements */
    public static void reverseSort( Comparable[] a ){
        
        int n = a.length;
        
        for (int i = 1; i < n; i++){
            for (int j = i; j > 0 && less(a[j-1], a[j]); j--)
               exch (a, j, j-1);
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
        for(int i = 1; i < a.length; i++) {
            if( less(a[i], a[i-1]) ) return false;
        }
        return true;
    }
    
    /** Returns whether the array is reverse sorted
     * @param a an array of comparable elements
     * @return true if the array is reverse sorted, false otherwise */
    public static boolean isReverseSorted( Comparable[] a ){
         for(int i = 1; i < a.length; i++){
             if( less(a[i-1], a[i]) ) return false;
         }
         return true;
    }
    
    /** Prints the contents of the array to standard output (terminal)
     * @param a an array of comparable elements */
    public static void show( Comparable [] a ){
        for (int i = 0; i < a.length; i++){
            StdOut.println( a[i] );
        }
    }
    
    /* -------------------------------------------------------------------------
     * EXTRA CREDIT:
     * [2 points] Implement sortAndDraw(), which draws a rendering of values at 
     *            each iteration
     * [2 points] Highlight the values being compared in red
     * ---------------------------------------------------------------------- */
    
    /** Draws a rendering of values at each iteration of insertion sort
     * @param a an array of comparable elements */
    public static void sortAndDraw( Comparable[] a ){
        
        int n = a.length;
        
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize(500,500);
        StdDraw.setXscale(0,n-1);
        StdDraw.setYscale(0,1);
       
        for (int i = 1; i < n; i++){
         for (int j = i; j > 0 && less(a[j], a[j-1]); j--)
               exch (a, j, j-1);
                                            
         StdDraw.clear();
         for (int j = 1; j < n; j++){
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(j,0,j,(double)a[j]);
         }
         StdDraw.show();
         StdDraw.pause(20);
        }
        
    }
    
    /* -------------------------------------------------------------------------
     * MAIN
     * ---------------------------------------------------------------------- */
    public static void main( String[] args ){
        int[] size = {100};
         
        Stopwatch timer;
        
        Double[] a, b = null;
        String[] label = {"Ascending (Unsorted)","Ascending (Sorted)","Reverse Sort"};
        double[][] executionTime = new double[label.length][size.length];
         
        for( int i = 0; i < size.length; i++ ){
            int N = size[i];
             
            // Create an array of size N and assign random values
            a = new Double[N];
            for( int j = 0; j < N; j++ )
                a[j] = StdRandom.uniform();
             
            // Run and time sorting executions
            for( int j = 0; j < label.length; j++ ){
                if( j != 2 ){
                    timer = new Stopwatch();
                    sortAndDraw( a );
                    Insertion.sort( a );
                }else{
                    b = Arrays.copyOf(a, a.length);
                    timer = new Stopwatch();
                    Insertion.reverseSort( b );
                }
                executionTime[j][i] = timer.elapsedTime();
            }
             
            
            StdOut.print("Ascending sort: ");
            StdOut.println(Insertion.isSorted(a) ? "SUCCESS" : "FAILED");
            Selection.show( a );
             
            StdOut.print("\nDescending sort: ");
            StdOut.println(Insertion.isReverseSorted(b) ? "SUCCESS" : "FAILED");
            Selection.show( b );
            

        }
         
        // Print execution times
        for(int i = 0; i < executionTime.length; i++){
            StdOut.println( label[i] );
            for(int j = 0; j < executionTime[i].length; j++){
                StdOut.printf("%-10stime = %.06f s\n", "N = " + size[j],
                        executionTime[i][j]);
                     
            }
        }
    }
     
    private static class Stopwatch{
        private final long start;
        public Stopwatch(){ start = System.nanoTime(); }
        public double elapsedTime(){
            return (double)(System.nanoTime() - start) * 1e-9;
        }
    }
}
