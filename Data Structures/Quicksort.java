import edu.princeton.cs.algs4.*;
import java.util.*;

public class Quicksort{
    private static final int CUTOFF = 10;
    
    /* -------------------------------------------------------------------------
     * ASCENDING QUICKSORT
     * ---------------------------------------------------------------------- */
    /** Sorts an array of comparable elements in ascending order using the
     * quicksort algorithm and array randomization
     * @param a an array of comparable elements */
    public static void sort( Comparable[] a ){
        StdRandom.shuffle(a);
        sort(a, 0, a.length-1);
    }
    
    /** Performs ascending quicksort on the subarray a[lo..hi]
     * @param a an array of comparable elements
     * @param lo the leftmost (lowest) index of the subarray
     * @param hi the rightmost (highest) index of the subarray */
    private static void sort( Comparable[] a, int lo, int hi ){
        
        if ( hi <= lo ) return;
   
        int j = partition ( a, lo, hi );
        sort ( a, lo, j-1 );
        sort ( a, j+1, hi );
        
    }
    
    /** Partitions the subarray a[lo..hi] such that:
     * a[lo..j-1] <= a[j] <= a[j+1..hi]
     * @param a an array of comparable elements
     * @param lo the leftmost (lowest) index in the subarray
     * @param hi the rightmost (highest) index in the subarray
     * @return the partition index j */
    private static int partition( Comparable[] a, int lo, int hi ){
        
        int i = lo;
        int j = hi + 1;
        Comparable v = a[lo];

        while (true) { 
            // find item on lo to swap
            while (less(a[++i], v))
                if (i == hi) break;

            // find item on hi to swap
            while (less(v, a[--j]))
                if (j == lo) break;

            // check if pointers cross
            if (i >= j) break;

            exch(a, i, j);
        }

        // put partitioning item v at a[j]
        exch(a, lo, j);

        // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j;

        
    }
    
    /* -------------------------------------------------------------------------
     * DESCENDING QUICKSORT
     * ---------------------------------------------------------------------- */
    
    /** Sorts an array of comparable elements in descending order using the
     * quicksort algorithm
     * @param a an array of comparable elements */
    public static void reverseSort( Comparable[] a ){
        StdRandom.shuffle(a);
        reverseSort(a, 0, a.length-1);
    }
    
    /** Performs descending quicksort on the subarray a[lo..hi]
     * @param a an array of comparable elements
     * @param lo the leftmost (lowest) index of the subarray
     * @param hi the rightmost (highest) index of the subarray */
    private static void reverseSort( Comparable[] a, int lo, int hi ){
        
        if ( hi <= lo ) return;
   
        int j = reversePartition ( a, lo, hi );
        reverseSort ( a, lo, j-1 );
        reverseSort ( a, j+1, hi );
        
    }
    
    /** Partitions the subarray a[lo..hi] such that:
     * a[lo..j-1] >= a[j] >= a[j+1..hi]
     * @param a an array of comparable elements
     * @param lo the leftmost (lowest) index in the subarray
     * @param hi the rightmost (highest) index in the subarray
     * @return the partition index j */
    private static int reversePartition( Comparable[] a, int lo, int hi ){
        
        int i = lo;
        int j = hi + 1;
        Comparable v = a[lo];

        while (true) { 
            // find item on lo to swap
            while (less(a[--j], v))
                if (j == lo) break;

            // find item on hi to swap
            while (less(v, a[++i]))
                if (i == hi) break;
                
            // check if pointers cross
            if (i >= j) break;

            exch(a, i, j);
        }

        // put partitioning item v at a[j]
        exch(a, lo, j);

        // now, a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
        return j;
        
    }
    
    /* -------------------------------------------------------------------------
     * INSERTION SORT FOR SUBARRAYS
     * ---------------------------------------------------------------------- */
    /** Sorts a subarray using insertion sort for use in
     * sort(Comparable[] a, int lo, int hi).
     * @param a an array of comparable elements
     * @param lo the leftmost (lowest) index of the sort
     * @param hi the rightmost (highest) index of the sort */
    private static void insertionSort(Comparable[] a, int lo, int hi){
        for(int i = lo + 1; i <= hi; i++)
            for(int j = i; j > lo && less(a[j],a[j-1]); j--)
                exch(a, j, j-1);
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
     * @param i the index of the first element to exchange
     * @param j the index of the second element to exchange */
    private static void exch( Comparable[] a, int i, int j ){
        Comparable t = a[i]; a[i] = a[j]; a[j] = t;
    }
    
    /* -------------------------------------------------------------------------
     * TEST METHODS (useful for debugging)
     * ---------------------------------------------------------------------- */
    
    /** Returns whether the array is sorted in ascending order
     * @param a an array of comparable elements
     * @return true if the array is sorted, false otherwise */
    public static boolean isSorted( Comparable[] a ){
        for(int i = 1; i < a.length; i++)
            if( less(a[i], a[i-1]) ) return false;
        return true;
    }
    
    /** Returns whether the array is sorted in descending order
     * @param a an array of comparable elements
     * @return true if the array is sorted, false otherwise */
    public static boolean isReverseSorted( Comparable[] a ){
        for(int i = 1; i < a.length; i++)
            if( less(a[i-1], a[i]) ) return false;
        return true;
    }
    
    /** Prints the contents of the array to standard output (terminal)
     * @param a an array of comparable elements */
    public static void show( Comparable[] a ){
        for(int i = 0; i < a.length; i++)
            StdOut.println( a[i] );
    }
    
    /* -------------------------------------------------------------------------
     * MAIN
     * ---------------------------------------------------------------------- */
    public static void main(String[]args){
        /* BEGIN DEBUG: comment when algorithm passes both tests ------------ */
        Double[] asc = new Double[10];
        for(int i = 0; i < asc.length; i++)
            asc[i] = StdRandom.uniform();
        Double[] desc = Arrays.copyOf(asc, asc.length);
        
        Quicksort.sort(asc);
        Quicksort.reverseSort(desc);
        
        StdOut.print("Ascending Quicksort: ");
        StdOut.println(Quicksort.isSorted(asc) ? "SUCCESS :)": "NOT QUITE :(");
        Quicksort.show(asc);
        
        StdOut.print("Descending Quicksort: ");
        StdOut.println(Quicksort.isReverseSorted(desc) ? "SUCCESS :)" : "NOT QUITE :(");
        Quicksort.show(desc);
        /* END DEBUG: comment when algorithm passes both tests -------------- */
        
        
        /* BEGIN EXPERIMENTS ------------------------------------------------ */
        int[] N = {1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000};
        double[][] execTime = new double[2][N.length];
        Stopwatch timer;
        Double[] a = null, b = null;
        
        for(int i = 0; i < N.length; i++){
            // Generate an array of N[i] random double values and pre-sort
            a = new Double[ N[i] ];
            for(int j = 0; j < N[i]; j++)
                a[j] = StdRandom.uniform();
            Arrays.sort(a);
            
            // Run and time quicksort on an already sorted array
            timer = new Stopwatch();
            Quicksort.sort(a);
            execTime[0][i] = timer.elapsedTime();
            
            // Run and time quicksort of an array in reverse order
            b = Arrays.copyOf(a, a.length);
            timer = new Stopwatch();
            Quicksort.reverseSort(b);
            execTime[1][i] = timer.elapsedTime();
        }
        
        // Print execution times
        for(int i = 0; i < execTime.length; i++){
            StdOut.println((i == 0) ? "Sorted Array" : "\nReverse Sorted Array");
            for(int j = 0; j < execTime[i].length; j++){
                StdOut.printf("%-10stime = %.06f s\n", "N = " + N[j],
                        execTime[i][j]);
            }
        }
        /* END EXPERIMENTS -------------------------------------------------- */
    }
    
    /* -------------------------------------------------------------------------
     * STOPWATCH
     * ---------------------------------------------------------------------- */
    private static class Stopwatch{
        private final long start;
        public Stopwatch(){ start = System.nanoTime(); }
        public double elapsedTime(){
            return (double)(System.nanoTime() - start) * 1e-9;
        }
    }
}