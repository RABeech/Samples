import edu.princeton.cs.algs4.*;

public class BST<Key extends Comparable<? super Key>, Value> {

    public static void main(String[]args){
        /* Useful function for calculating the value of n for some k:
         * 
         *   Math.pow( double a, double b ) --> double
         *     Returns the result of a^b
         * 
         * Useful functions for generating n unique values:
         * 
         *   StdRandom.permutation( int n ) --> int[]
         *     Returns a uniformly random permutation of n elements
         * 
         *   StdRandom.permutation( int n, int k ) --> int[]
         *     Returns a uniformly random permutation of k of n elements
         *
         * If you choose to write code to compute the ideal height for N:
         * 
         *   Math.log( double a ) --> double
         *     Returns the result of ln(a); i.e. natural log of a
         * 
         *   Math.ceil( double a ) --> double
         *     Returns the ceiling of a (smallest integer >= a)
         */
         
        BST<Integer, Integer> st = new BST<Integer, Integer>();
        int n;
        
        for (int k = 3; k <= 21; k+=2){
            n = (int)Math.pow(2, (double) k) -1;
            System.out.println(n);
        }
        
    }
    
    /* -------------------------------------------------------------------------
     * BST CLASS MEMBERS
     * ---------------------------------------------------------------------- */
    private Node root;             // root of BST
	
    private class Node {
        private Key key;           // key
        private Value val;         // associated data
        private Node left, right;  // left and right subtrees
		
        public Node(Key key, Value val) {
            this.key = key;
            this.val = val;
        }
		
		public String toString(){
			return "[" + this.key + ", " + this.val + "]";
		}
    }
	
    /** Initializes an empty BST. */
    public BST(){ }
	
    /** Returns whether this BST is empty.
     * @return true if this BST is empty; false otherwise */
    public boolean isEmpty(){ return root == null; }
	
    /** Returns the number of key-value pairs in this BST.
     * @return the number of key-value pairs in this BST */
    public int size(){
        return size(root);
    }

    // return number of key-value pairs in BST rooted at x
    private int size(Node x){
        if (x == null) return 0;
        else return 1 + size(x.left) + size(x.right);
    }
	
    /** Returns the height of the BST.
     * @return the height of the BST (a 1-node tree has height 0) */
    public int height(){
        
        return height(root);
        
    }
	
	// Returns the height of the BST rooted at x
    private int height(Node x){
        
        if (x == null) return 0;
        return 1 + size(x.left) + size(x.right);
        
    }

    /** Returns the value associated with the given key.
     * @param key the key to search the BST for
     * @return the value associated with the given key if the key is in the BST
	 *         or null otherwise */
    public Value get(Key key){
        return get(root, key);
    }
	
	// Returns the value associated with the given key in the BST rooted at x
    private Value get(Node x, Key key){
        if (key == null || x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) return get(x.left, key);
        else if (cmp > 0) return get(x.right, key);
        else              return x.val;
    }

    /** Inserts the specified key-value pair into the BST, overwriting the old 
     * value with the new value if the BST already contains the specified key.
     * @param key the key to insert into the BST
     * @param val the value associated with the key to insert */
    public void put(Key key, Value val){
        if (key == null || val == null) return;
        root = put(root, key, val);
    }
	
	// Inserts a key-value pair into the BST rooted at x
    private Node put(Node x, Key key, Value val){
        if (x == null) return new Node(key, val);
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = put(x.left,  key, val);
        else if (cmp > 0) x.right = put(x.right, key, val);
        else              x.val   = val;
        return x;
    }
    
    /** Removes the key and its associated value from this BST if the key is in
     * this BST.    
     * @param key the key to remove from the BST */
    public void delete(Key key){
        if (key == null) return;
        root = delete(root, key);
    }
	
	// Removes the key and its associated value from the BST rooted at x
    private Node delete(Node x, Key key){
        if (x == null) return null;

        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left  = delete(x.left,  key);
        else if (cmp > 0) x.right = delete(x.right, key);
        else { 
            if (x.right == null) return x.left;
            if (x.left  == null) return x.right;
            Node t = x;
            x = min(t.right);
            x.right = deleteMin(t.right);
            x.left = t.left;
        }
        return x;
    }
    
    /** Returns the smallest key in the BST.
     * @return the smallest key in the BST */
    public Key min(){
		if(isEmpty()) return null;
        return min(root).key;
    } 
	
	// Returns the smallest key in the BST rooted at x
    private Node min(Node x){ 
        if (x.left == null) return x; 
        else                return min(x.left); 
    } 
	
    /** Returns the largest key in the BST.
     * @return the largest key in the BST */
    public Key max(){
        if (isEmpty()) return null;
        return max(root).key;
    } 
	
	// Returns the largest key in the BST rooted at x
    private Node max(Node x){
        if (x.right == null) return x; 
        else                 return max(x.right); 
    }
    
    /** Removes the smallest key and associated value from the BST. */
    public void deleteMin(){
        if (isEmpty()) return;
        root = deleteMin(root);
    }
	
	// Removes the smallest key and associated value from the BST rooted at x
    private Node deleteMin(Node x){
        if (x.left == null) return x.right;
        x.left = deleteMin(x.left);
        return x;
    }

    /** Removes the largest key and associated value from the BST. */
    public void deleteMax(){
        if (isEmpty()) return;
        root = deleteMax(root);
    }
	
	// Removes the largest key and associated value from the BST rooted at x
    private Node deleteMax(Node x){
        if (x.right == null) return x.left;
        x.right = deleteMax(x.right);
        return x;
    }
}