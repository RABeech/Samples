import edu.princeton.cs.algs4.*;
import java.util.StringTokenizer;

public class LinearProbingHashST<Key, Value> {
    public static void main(String[]args){
        
        LinearProbingHashST<String,String> st;
        st = new LinearProbingHashST<String,String>();
        
        In in = new In("keys.txt");
        String[] keys = in.readAllStrings();
        in.close();
        
        Stopwatch timer = new Stopwatch();
        for(String k : keys)
            st.put(k,k);
        double time = timer.elapsedTime();
        
        StdOut.printf("Hash table contains %d keys in %d bins.\n" + 
                      "Time taken = %.09f seconds", st.n, st.m, time);
        
    }
    
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
    
    private static final int INIT_CAPACITY = 4;  // default initial capacity
    
    private int n;           // number of key-value pairs in the symbol table
    private int m;           // size of linear probing table
    private Key[] keys;      // the keys
    private Value[] vals;    // the values
    
    /** Initializes an empty symbol table with the default initial capacity. */
    public LinearProbingHashST(){ this(INIT_CAPACITY); }
    
    /** Initializes an empty symbol table with the specified initial capacity.
     * @param capacity the initial capacity */
    public LinearProbingHashST(int capacity){
        m = capacity;
        n = 0;
        keys = (Key[])   new Object[m];
        vals = (Value[]) new Object[m];
    }
    
    /** Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table */
    public int size(){ return n; }
    
    /** Returns whether this symbol table is empty.
     * @return true if this symbol table is empty; false otherwise */
    public boolean isEmpty(){ return n == 0; }
    
    /** Returns true if this symbol table contains the specified key.
     * @param  key the key
     * @return true if this symbol table contains the key; false otherwise */
    public boolean contains(Key key){
        if (key == null) return false;
        return get(key) != null;
    }
    
    /* hash function for keys - returns value between 0 and M-1
     * @param key the key to compute the hash value for
     * @return the hash value for the given key */
    private int hash(Key key){
        return (key.hashCode() & 0x7fffffff) % m;
    }
    
    /* resizes the hash table to the new capacity by re-hashing all of the keys
     * @param capacity the target size of the hash table */
    private void resize(int capacity){
        LinearProbingHashST<Key, Value> temp;
        temp = new LinearProbingHashST<Key, Value>(capacity);
        
        for(int i = 0; i < m; i++)
            if(keys[i] != null)
                temp.put(keys[i], vals[i]);
        
        keys = temp.keys;
        vals = temp.vals;
        m    = temp.m;
    }
    
    /** Inserts the specified key-value pair into the symbol table, overwriting
     * the old value with the new value if the symbol table already contains the
     * specified key.
     * @param key the key
     * @param val the value */
    public void put(Key key, Value val){
        if (key == null || val == null) return;
        
        if (n >= m/2) resize(2*m);  // double table size if 50% full
        
        int i = hash(key);
        for( ; keys[i] != null; i = (i+5) % m)
            if(keys[i].equals(key)){
                vals[i] = val;
                return;
            }
        
        keys[i] = key;
        vals[i] = val;
        n++;
    }
    
    /** Returns the value associated with the specified key.
     * @param key the key
     * @return the value associated with the key or null if the key is not found
     *         in the symbol table */
    public Value get(Key key){
        if (key == null) return null;
        for (int i = hash(key); keys[i] != null; i = (i+1) % m)
            if (keys[i].equals(key))
                return vals[i];
        return null;
    }
    
    /** Removes the specified key and its associated value from this symbol
     * table (if the key is in this symbol table).    
     * @param  key the key */
    public void delete(Key key){
        if (key == null || !contains(key)) return;
        
        // find position i of key
        int i = hash(key);
        while( !key.equals(keys[i]) )
            i = (i + 1) % m;
        
        // delete key and associated value
        keys[i] = null;
        vals[i] = null;
        
        // rehash all keys in same cluster
        i = (i + 1) % m;
        while (keys[i] != null) {
            // delete keys[i] an vals[i] and reinsert
            Key   keyToRehash = keys[i];
            Value valToRehash = vals[i];
            keys[i] = null;
            vals[i] = null;
            n--;
            put(keyToRehash, valToRehash);
            i = (i + 1) % m;
        }
        
        n--;
        
        // halves size of array if it's 12.5% full or less
        if (n > 0 && n <= m/8) resize(m/2);
    }
    
    /** Returns all keys in this symbol table as an Iterable. To iterate over
     * all of the keys in the symbol table, use the foreach notation:
     *     for(Key key : st.keys())
     * @return all keys in this symbol table */
    public Iterable<Key> keys(){
        Queue<Key> queue = new Queue<Key>();
        for (int i = 0; i < m; i++)
            if (keys[i] != null) queue.enqueue(keys[i]);
        return queue;
    }
}