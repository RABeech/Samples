import edu.princeton.cs.algs4.*;
import java.util.*;

public class PriorityQueue<E>{
    /* -------------------------------------------------------------------------
     * MAIN
     * Builds a minimum and maximum priority queue from entries in a text file,
     * then finds the minimum and maximum M entries
     * ---------------------------------------------------------------------- */
    public static void main(String[]args){
        int M = 10;
        MinPQ<Integer> min = new MinPQ<Integer>( M+1 );
        MaxPQ<Integer> max = new MaxPQ<Integer>( M+1 );
        
        In in = new In("input.txt");
        
        min = in.readAllInts();
        max = in.readAllInts();
        
    }
    
    /* -------------------------------------------------------------------------
     * SUPERCLASS FIELDS AND METHODS
     * Fields and methods of the superclass PriorityQueue which are inherited by
     * subclasses MinPQ and MaxPQ
     * ---------------------------------------------------------------------- */
    protected E[] pq;      // The priority queue
    protected int N = 0;   // The number of items in the priority queue
    
    /** Initializes an empty priority queue (can be either min or max heap) with
     * initial capacity cap.
     * @param cap the capcaity of this priority queue */
    public PriorityQueue(int cap){
        pq = (E[]) new Object[cap + 1];
    }
    
    /** Returns whether the priority queue is empty
     * @return true if the priority queue is empty, false otherwise */
    public boolean isEmpty(){ return N == 0; }
    
    /** Returns the number of entries in the priority queue
     * @return the number of entries in the priority queue */
    public int size(){ return N; }
    
    /** Returns whether pq[i] < pq[j]
     * @return true if pq[i] < pq[j], false otherwise */
    public boolean less(int i, int j){
        return ((Comparable<E>)pq[i]).compareTo(pq[j]) < 0;
    }
    
    /** Returns whether pq[i] > pq[j]
     * @return true if pq[i] > pq[j], false otherwise */
    public boolean greater(int i, int j){
        return ((Comparable<E>)pq[i]).compareTo(pq[j]) > 0;
    }
    
    /** Exchanges the entries located at indices i and j in the priority queue
     * @param i the index of an entry in the priority queue
     * @param j the index of an entry in the priority queue */
    protected void exch(int i, int j){
        E t = pq[i]; pq[i] = pq[j]; pq[j] = t;
    }
}

/* -----------------------------------------------------------------------------
 * MIN HEAP PRIORITY QUEUE
 * -------------------------------------------------------------------------- */

class MinPQ<E> extends PriorityQueue<E>{
    /** Initializes an empty min heap priority queue with capacity cap.
     * @param cap the capacity of this priority queue */
    public MinPQ(int cap){
        super(cap);
    }
    
    /** Inserts an item into the min heap priority queue. After insertion, the
     * heap structure is verified bottom-up.
     * @param item the item to insert into the priority queue */
    public void insert(E item){
        
        pq[++N] = E;   
        swim(N);
 
    }
    
    /** Removes and returns the minimum entry in the min heap priority queue.
     * After removal, the heap structure is verified top-down.
     * @return the minimum entry in the min heap priority queue. */
    public E delMin(){
        
        // YOUR CODE HERE
        
    }
    
    /** Top-down reheapify the min heap.
     * @param k the index of the parent entry in the priority queue */
    private void sink(int k){
        
        // YOUR CODE HERE
        
    }
    
    /** Bottom-up reheapify the min heap.
     * @param k the index of the child entry in the priority queue */
    private void swim(int k){
        
        // YOUR CODE HERE
        
    }
    
    /** Returns whether this priority queue is a min heap.
     * @return true if the priority queue is a min heap, false otherwise */
    public boolean isMinHeap(){
        return isMinHeap(1);
    }
    
    /** Returns whether the sub-heap starting at index k is a min heap.
     * @param k the index of the parent entry in the priority queue
     * @return true if the sub-heap at k is a min heap, false otherwise */
    private boolean isMinHeap(int k){
        if(k > N) return true;
        int L = 2*k;
        int R = 2*k + 1;
        if(L <= N && greater(k, L)) return false;
        if(R <= N && greater(k, R)) return false;
        return isMinHeap(L) && isMinHeap(R);
    }
}

/* -----------------------------------------------------------------------------
 * MAX HEAP PRIORITY QUEUE
 * -------------------------------------------------------------------------- */

class MaxPQ<E> extends PriorityQueue<E>{
    /** Initializes an empty max heap priority queue with capacity cap.
     * @param cap the capacity of this priority queue */
    public MaxPQ(int cap){
        super(cap);
    }
    
    /** Inserts an item into the max heap priority queue. After insertion, the
     * heap structure is verified bottom-up.
     * @param item the item to insert into the priority queue */
    public void insert(E item){
        
        pq[++N] = E;   
        swim(N);
        
    }
    
    /** Removes and returns the maximum entry in the max heap priority queue.
     * After removal, the heap structure is verified top-down.
     * @return the maximum entry in the max heap priority queue. */
    public E delMax(){
        
        Key max = pq[1];
        exch(1, N--);
        sink(1);
        pq[N+1] = null;
        
        return max;
        
    }
    
    /** Top-down reheapify the max heap.
     * @param k the index of the parent entry in the priority queue */
    private void sink(int k){
        
        while (2*k <= N) {
           int j = 2*k;
           if (j < N && less(j, j+1)) j++;
           if (!less(k, j)) break;
           exch(k, j);
           k = j;
        }
   
    }
    
    /** Bottom-up reheapify the max heap.
     * @param k the index of the parent entry in the priority queue */
    private void swim(int k){
        
        while (k > 1 && less(k/2, k))
        {
            exch(k,k/2);
            
            k = k/2;
        }
        
    }
    
    /** Returns whether this priority queue is a max heap.
     * @return true if the priority queue is a max heap, false otherwise */
    public boolean isMaxHeap(){
        return isMaxHeap(1);
    }
    
    /** Returns whether the sub-heap starting at index k is a max heap.
     * @param k the index of the parent entry in the priority queue
     * @return true if the sub-heap at k is a max heap, false otherwise */
    private boolean isMaxHeap(int k){
        if(k > N) return true;
        int L = 2*k;
        int R = 2*k + 1;
        if(L <= N && less(k, L)) return false;
        if(R <= N && less(k, R)) return false;
        return isMaxHeap(L) && isMaxHeap(R);
    }
}