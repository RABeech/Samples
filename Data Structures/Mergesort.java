import edu.princeton.cs.algs4.*;
import java.util.*;

public class Mergesort{
    
    /* -------------------------------------------------------------------------
     * ASCENDING MERGESORT
     * ---------------------------------------------------------------------- */
    
    /** Sorts an array of comparable elements in ascending order using the
     * mergesort algorithm
     * @param a an array of comparable elements */
    public static void sort(Comparable[] a){
        Comparable[] aux = new Comparable[a.length];
        sort(a, aux, 0, a.length-1);
    }
    
    /** Performs mergesort on a[lo..hi] using auxiliary array aux[lo..hi]
     * @param a an array of comparable elements
     * @param aux the auxiliary array of comparable elements
     * @param lo the leftmost (lowest) index of the sort
     * @param hi the rightmost (highest) index of the sort */
    private static void sort(Comparable[] a, Comparable[] aux, int lo, int hi){
        
        if (hi <= lo) return;
         
         int mid = lo+(hi-lo)/2;
         sort (a, aux, lo, mid);
         sort (a, aux, mid+1, hi);
         
         if (a[mid].compareTo(a[mid+1]) <=0)
                  
         merge (a, aux, lo, mid, hi);
        
    }
    
    /** Merges a[lo..mid] with a[mid+1..hi] by first copying into the auxiliary
     * array aux[], then merging back to a[].
     * @param a an array of comparable elements
     * @param aux the auxiliary array of comparable elements
     * @param lo the leftmost (lowest) index of the merge
     * @param mid the middle index of the merge
     * @param hi the rightmost (highest) index of the merge */
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi){
        
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k]; 
        }

        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)              a[k] = aux[j++];
            else if (j > hi)               a[k] = aux[i++];
            else if (less(aux[j], aux[i])) a[k] = aux[j++];
            else                           a[k] = aux[i++];
        }
        
    }
    
    /* -------------------------------------------------------------------------
     * DESCENDING MERGESORT
     * ---------------------------------------------------------------------- */
    
    /** Sorts an array of comparable elements in descending order using the
     * mergesort algorithm
     * @param a an array of comparable elements */
    public static void reverseSort(Comparable[] a){
        Comparable[] aux = new Comparable[a.length];
        reverseSort(a, aux, 0, a.length-1);
    }
    
    /** Performs mergesort on a[lo..hi] using auxiliary array aux[lo..hi]
     * @param a an array of comparable elements
     * @param aux the auxiliary array of comparable elements
     * @param lo the leftmost (lowest) index of the sort
     * @param hi the rightmost (highest) index of the sort */
    private static void reverseSort(Comparable[] a, Comparable[] aux, int lo, int hi){
        
        if (hi <= lo) return;
         
         int mid = lo+(hi-lo)/2;
         reverseSort (a, aux, lo, mid);
         reverseSort (a, aux, mid+1, hi);
         reverseMerge (a, aux, lo, mid, hi);
        
    }
    
    /** Merges a[lo..mid] with a[mid+1..hi] by first copying into the auxiliary
     * array aux[], then merging back to a[].
     * @param a an array of comparable elements
     * @param aux the auxiliary array of comparable elements
     * @param lo the leftmost (lowest) index of the merge
     * @param mid the middle index of the merge
     * @param hi the rightmost (highest) index of the merge */
    private static void reverseMerge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi){
        
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k]; 
        }

        int i = lo, j = mid+1;
        for (int k = lo; k <= hi; k++) {
            if      (i > mid)              a[k] = aux[j++];
            else if (j > hi)               a[k] = aux[i++];
            else if (less(aux[i], aux[j])) a[k] = aux[j++];
            else                           a[k] = aux[i++];
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
     * MAIN
     * ---------------------------------------------------------------------- */
    public static void main(String[]args){
        /* BEGIN DEBUG: comment when algorithm passes ----------------------- */
        Double[] ascending = new Double[10];
        for(int i = 0; i < ascending.length; i++)
            ascending[i] = StdRandom.uniform();
        Double[] descending = Arrays.copyOf(ascending, ascending.length);
        
        Mergesort.sort(ascending);
        Mergesort.reverseSort(descending);
        
        StdOut.print("Ascending Mergesort: ");
        StdOut.println( isSorted(ascending) ? "PASS" : "FAIL" );
        Mergesort.show(ascending);
        
        StdOut.print("\nDescending Mergesort: ");
        StdOut.println( isReverseSorted(descending) ? "PASS" : "FAIL" );
        Mergesort.show(descending);
        /* END DEBUG: comment when algorithm passes ------------------------- */
        
        
        /* BEGIN EXECUTION TIMES -------------------------------------------- */
        int[] numElem = {125000, 250000, 500000, 1000000, 2000000};
        double[][] execTime = new double[2][numElem.length];
        Stopwatch timer;
        int N;
        
        Double[] a = null, b = null;
        
        for(int i = 0; i < numElem.length; i++){
            N = numElem[i];
            // Generate an array of N random double values & create a copy
            a = new Double[N];
            for(int j = 0; j < N; j++)
                a[j] = StdRandom.uniform();
            b = Arrays.copyOf(a, a.length);
            
            // Run and time mergesort and shellsort algorithms
            timer = new Stopwatch();
            Mergesort.sort(a);
            execTime[0][i] = timer.elapsedTime();
            
            timer = new Stopwatch();
            Shell.sort(b);
            execTime[1][i] = timer.elapsedTime();
        }
        
        // Print execution times
        for(int i = 0; i < execTime.length; i++){
            StdOut.println((i == 0 ? "Mergesort" : "\nShellsort"));
            for(int j = 0; j < execTime[i].length; j++){
                StdOut.printf("%-14stime = %.06f s\n", "N == " + numElem[j],
                        execTime[i][j]);
            }
        }
        /* END EXECUTION TIMES ---------------------------------------------- */
    }
    
    /* -------------------------------------------------------------------------
     * STOPWATCH & SHELLSORT ALGORITHM
     * ---------------------------------------------------------------------- */
    private static class Stopwatch{
        private final long start;
        public Stopwatch(){ start = System.nanoTime(); }
        public double elapsedTime(){
            return (double)(System.nanoTime() - start) * 1e-9;
        }
    }
    
    private static class Shell{
        public static void sort( Comparable[] a ){
            int N = a.length, h = 1;
            while( h < N/3 ) h = 3*h + 1;
            while( h >= 1 ){
                for(int i = h; i < N; i++)
                    for(int j = i; j >= h && less(a[j],a[j-h]); j -= h)
                        exch(a, j, j-h);
                h = h/3;
            }
        }
    }
}
