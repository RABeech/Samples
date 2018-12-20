import edu.princeton.cs.algs4.*;
import java.util.*;

public class HeapSortComparison{
    public static void main(String[]args){
        
        // YOUR CODE HERE
        
    }
    
    /* -------------------------------------------------------------------------
     * STOPWATCH CLASS
     * ---------------------------------------------------------------------- */
    private static class Stopwatch{
        private final long start;
        public Stopwatch(){ start = System.nanoTime(); }
        public double elapsedTime(){
            return (double)(System.nanoTime() - start) * 1e-9;
        }
    }
    
    /* -------------------------------------------------------------------------
     * HEAPSORT
     * ---------------------------------------------------------------------- */
    /** Sorts an array of comparable elements in ascending order using the
     * heapsort algorithm.
     * @param a an array of comparable elements */
    public static void heapSort(Comparable[] a){
        
        // YOUR CODE HERE
        
    }
    
    /** Builds heap bottom-up using a max heap sink.
     * @param a an array of comparable elements
     * @param k the node to heapify
     * @param n the number of elements in a[] */
    private static void sink(Comparable[] a, int k, int n){
        
        // YOUR CODE HERE
        
    }
    
    /* -------------------------------------------------------------------------
     * HELPER SORTING METHODS & DEBUG METHODS
     * ---------------------------------------------------------------------- */
    /** Returns whether the element v is less than the element w.
     * @param v a comparable element
     * @param w a comparable element
     * @return true if v is less than w, false otherwise */
    private static boolean less(Comparable v, Comparable w){
        return v.compareTo(w) < 0;
    }
    
    /** Returns whether a[i] < a[j].
     * @param a an array of comparable elements
     * @param i the index of the first element to compare
     * @param j the index of the second element to compare
     * @return true if a[i] < a[j], false otherwise */
    private static boolean less(Comparable[] a, int i, int j){
        return a[i].compareTo(a[j]) < 0;
    }
    
    /** Exchanges the elements in the array a[] at indices i and j.
     * @param a an array of comparable elements
     * @param i the index of the first element to exchange
     * @param j the index of the second element to exchange */
    private static void exch(Comparable[] a, int i, int j){
        Comparable t = a[i]; a[i] = a[j]; a[j] = t;
    }
    
    /** Returns whether the elements in the array a[] are sorted.
     * @param a an array of comparable elements
     * @return true if a[] is in ascending order, false otherwise */
    private static boolean isSorted(Comparable[] a){
        for(int i = 1; i < a.length; i++)
            if(less(a[i], a[i-1])) return false;
        return true;
    }
    
    /** Prints the contents of array a[] to standard output (terminal)
     * @param a an array of comparable elements */
    private static void show(Comparable[] a){
        for(int i = 0; i < a.length; i++)
            StdOut.println( a[i] );
    }
    
    /* -------------------------------------------------------------------------
     * INSERTION SORT
     * ---------------------------------------------------------------------- */
    public static void insertionSort(Comparable[] a){
        for(int i = 0; i < a.length; i++)
            for(int j = i; j > 0 && less(a[j], a[j-1]); j--)
                exch(a, j, j-1);
    }
    
    /* -------------------------------------------------------------------------
     * SELECTION SORT
     * ---------------------------------------------------------------------- */
    public static void selectionSort(Comparable[] a){
        for(int i = 0; i < a.length; i++){
            int min = i;
            for(int j = i+1; j < a.length; j++)
                if(less(a[j], a[min])) min = j;
            exch(a, i, min);
        }
    }
    
    /* -------------------------------------------------------------------------
     * SHELLSORT
     * ---------------------------------------------------------------------- */
    public static void shellSort(Comparable[] a){
        int N = a.length, h = 1;
        while(h < N/3) h = 3*h + 1;
        while(h >= 1){
            for(int i = h; i < N; i++)
                for(int j = i; j >= h && less(a[j], a[j-h]); j -= h)
                    exch(a, j, j-h);
            h /= 3;
        }
    }
    
    /* -------------------------------------------------------------------------
     * MERGESORT
     * ---------------------------------------------------------------------- */
    public static void mergeSort(Comparable[] a){
        Comparable[] aux = new Comparable[a.length];
        mergeSort(a, aux, 0, a.length-1);
    }
    
    private static void mergeSort(Comparable[] a, Comparable[] aux, int lo, int hi){
        if(hi <= lo) return;
        int mid = lo + (hi - lo)/2;
        mergeSort(a, aux, lo, mid);
        mergeSort(a, aux, mid+1, hi);
        merge(a, aux, lo, mid, hi);
    }
    
    private static void merge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi){
        for(int k = lo; k <= hi; k++) aux[k] = a[k];
        int i = lo, j = mid+1;
        for(int k = lo; k <= hi; k++){
            if(i > mid)                     a[k] = aux[j++];
            else if(j > hi)                 a[k] = aux[i++];
            else if(less(aux[j], aux[i]))   a[k] = aux[j++];
            else                            a[k] = aux[i++];
        }
    }
    
    /* -------------------------------------------------------------------------
     * QUICKSORT
     * ---------------------------------------------------------------------- */
    public static void quickSort(Comparable[] a){
        StdRandom.shuffle(a);
        quickSort(a, 0, a.length-1);
    }
    
    private static void quickSort(Comparable[] a, int lo, int hi){
        if(hi <= lo) return;
        int j = partition(a, lo, hi);
        quickSort(a, lo, j-1);
        quickSort(a, j+1, hi);
    }
    
    private static int partition(Comparable[] a, int lo, int hi){
        int i = lo, j = hi+1;
        Comparable v = a[lo];
        while(true){
            while(less(a[++i], v))
                if(i == hi) break;
            while(less(v, a[--j]));
            if(i >= j) break;
            exch(a, i, j);
        }
        exch(a, lo, j);
        return j;
    }
}