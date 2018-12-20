/**
 * This class determines and displays the number of entries in "query.txt" that
 * match entries in "database.txt" by using a sequential search symbol table.
 * Search times are reported for 10, 100, 1000, and 10000 queries.
 */

import edu.princeton.cs.algs4.*;

public class SequentialSearchST<Key>{
    public static void main(String[]args){
        // Sequential search symbol table to store database entries
        SequentialSearchST<String> st = new SequentialSearchST<String>();
        
        double searchTime = 0.0;    // Total time spent searching
        int counter = 0;
        int[] N = {10, 100, 1000};
        
        In db = new In("database.txt");
        
        while (db.hasNextLine()){
         String item = db.readString();
         st.put(item);            
        }
        db.close();
        
        for (int i = 0; i<N.length; i++){
         
         In qu = new In("query.txt");
         
         for (int z = 0; z<N[i]; z++){
            String check = qu.readString();
            Stopwatch timer = new Stopwatch();
            if (st.contains(check)){
            counter++;
            }
            searchTime += timer.elapsedTime();
         }
         StdOut.println(counter);
         StdOut.println(searchTime);
         counter = 0;
         qu.close();
        }
           
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
     * CLASS MEMBERS
     * ---------------------------------------------------------------------- */
    
    private int N;          // Number of elements in this symbol table
    private Node first;     // The first element in this symbol table
    
    /** Helper Node class */
    private class Node{
        Key key;
        Node next;
        
        public Node(Key key, Node next){
            this.key = key;
            this.next = next;
        }
    }
    
    /** Initializes an empty symbol table */
    public SequentialSearchST(){ }
    
    /** Returns the number of entries in this symbol table
     * @return the number of entries in this symbol table */
    public int size(){ return N; }
    
    /** Returns whether this symbol table is empty
     * @return true if this symbol table is empty, false otherwise */
    public boolean isEmpty(){ return first == null; }
    
    /** Returns whether this symbol table contains the specified key
     * @param key the key to search for within this symbol table
     * @return true if the symbol table contains the search key, false otherwise */
    public boolean contains(Key key){
        for(Node n = first; n != null; n = n.next)
            if(key.equals(n.key)) return true;
        return false;
    }
    
    /** Inserts the specified key into the symbol table. This assumes that all
     * entries are unique and so does not check if a key already exists in the
     * symbol table.
     * @param key the key to insert into the symbol table */
    public void put(Key key){
        first = new Node(key, first);
        N++;
    }
    
    /** Returns an iterator for the entries in this symbol table
     * @return an iterator for the entries in this symbol table */
    public Iterable<Key> keys(){
        Queue<Key> queue = new Queue<Key>();
        for(Node n = first; n != null; n = n.next)
            queue.enqueue(n.key);
        return queue;
    }
}