import edu.princeton.cs.algs4.*;
import edu.princeton.cs.algs4.BST;

public class RedBlackBST<Key extends Comparable<? super Key>, Value> {
    public static void main(String[]args){
        
        BST<Integer,Integer> bst = new BST<Integer,Integer>();
        RedBlackBST<Integer,Integer> rbBST = new RedBlackBST<Integer,Integer>();
        
        int[] N = {10, 100, 1000, 10000, 100000, 1000000};
        
        for(int i = 0; i<N.length; i++){
            int [] rand = StdRandom.permutation(N[i]);
            for( int k = 0; k<rand.length; k++){
                  bst.put(rand[k], rand[k]);
                  rbBST.put(rand[k], rand[k]);
            }
         System.out.println("N = " + N[i] + "\n" + "BST Height = " + bst.height() + "\n" + "RedBlackBST Height = " + rbBST.height() + "\n" + "  ");
        }
       
        
    }
    
    /* -------------------------------------------------------------------------
     * CLASS MEMBERS
     * ---------------------------------------------------------------------- */
    private static final boolean RED   = true;
    private static final boolean BLACK = false;
    
    private Node root;     // root of the BST
    
    // BST helper node data type
    private class Node{
        private Key key;           // key
        private Value val;         // associated data
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        private int size;          // subtree count
        
        public Node(Key key, Value val, boolean color, int size){
            this.key = key;
            this.val = val;
            this.color = color;
            this.size = size;
        }
    }
    
    /** Initializes an empty BST. */
    public RedBlackBST(){ }
    
    /** Returns whether the Node x is red
     * @param x the Node to check
     * @return true if the Node x is red, false otherwise */
    private boolean isRed(Node x){
        if (x == null) return false;
        return x.color == RED;
    }
    
    /** Returns the number of key-value pairs in this BST.
     * @return the number of key-value pairs in this BST */
    public int size() {
        return size(root);
    }
    
    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }
    
    /** Returns whether the BST is empty
     * @return true if this BST is empty; false otherwise */
    public boolean isEmpty() {
        return root == null;
    }
    
    /** Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key */
    public Value get(Key key) {
        
        if(key == null) return null;
        return get(root, key);
        
    }
    
    // value associated with the given key in subtree rooted at x
    private Value get(Node x, Key key) {
        
        while (x != null){
         int cmp = key.compareTo(x.key);
         if (cmp < 0) x = x.left;
         else if (cmp > 0) x = x.right;
         else return x.val;
        }
        return null;
        
    }
    
    /** Returns whether the BST contains the given key
     * @param key the key
     * @return true if this BST contains the key; false otherwise */
    public boolean contains(Key key) {
        return get(key) != null;
    }
    
    /** Inserts the specified key-value pair into the BST, overwriting the old 
     * value with the new value if the BST already contains the specified key.
     * @param key the key
     * @param val the value */
    public void put(Key key, Value val) {
        
        if (key == null || val == null) return;
        root = put(root, key, val);
        root.color = BLACK;
        
    }

    // insert the key-value pair in the subtree rooted at h
    private Node put(Node h, Key key, Value val) { 
        
        if (h == null) return new Node(key, val, RED, 1);
        
        int cmp = key.compareTo(h.key);
        if (cmp < 0) h.left = put(h.left, key, val);
        else if (cmp > 0) h.right = put(h.right, key, val);
        else h.val = val;
        
        // fix-up any right-leaning links
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        h.size = size(h.left) + size(h.right) +1;
        
        return h;
        
    }
    
    /** Removes the smallest key and associated value from the BST. */
    public void deleteMin() {
        if (isEmpty()) return;
        
        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;
        
        root = deleteMin(root);
        if (!isEmpty()) root.color = BLACK;
    }
    
    // delete the key-value pair with the minimum key rooted at h
    private Node deleteMin(Node h) { 
        if (h.left == null)
            return null;
        
        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);
        
        h.left = deleteMin(h.left);
        return balance(h);
    }
    
    /** Removes the largest key and associated value from the BST. */
    public void deleteMax() {
        if (isEmpty()) return;
        
        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;
        
        root = deleteMax(root);
        if (!isEmpty()) root.color = BLACK;
    }

    // delete the key-value pair with the maximum key rooted at h
    private Node deleteMax(Node h) { 
        if (isRed(h.left))
            h = rotateRight(h);
        
        if (h.right == null)
            return null;
        
        if (!isRed(h.right) && !isRed(h.right.left))
            h = moveRedRight(h);
        
        h.right = deleteMax(h.right);
        
        return balance(h);
    }

    /** Removes the specified key and its associated value from this BST        
     * @param  key the key */
    public void delete(Key key) { 
        if (key == null || !contains(key)) return;
        
        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;
        
        root = delete(root, key);
        if (!isEmpty()) root.color = BLACK;
    }

    // delete the key-value pair with the given key rooted at h
    private Node delete(Node h, Key key) { 
        if (key.compareTo(h.key) < 0)  {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = delete(h.left, key);
        } else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.val = x.val;
                // h.val = get(h.right, min(h.right).key);
                // h.key = min(h.right).key;
                h.right = deleteMin(h.right);
            }
            else h.right = delete(h.right, key);
        }
        return balance(h);
    }
    
    // make a left-leaning link lean to the right
    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = x.right.color;
        x.right.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }
    
    // make a right-leaning link lean to the left
    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = x.left.color;
        x.left.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }
    
    // flip the colors of a node and its two children
    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }
    
    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (isRed(h.right.left)) { 
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private Node moveRedRight(Node h) {
        flipColors(h);
        if (isRed(h.left.left)) { 
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }
    
    // restore red-black tree invariant
    private Node balance(Node h) {
        if (isRed(h.right))                      h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))     flipColors(h);
        
        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }
    
    /** Returns the height of the BST.
     * @return the height of the BST (a 1-node tree has height 0) */
    public int height() {
        return height(root);
    }
    
    // returns the height of the BST rooted at x
    private int height(Node x) {
        if (x == null) return -1;
        return 1 + Math.max(height(x.left), height(x.right));
    }
    
    /** Returns the smallest key in the BST.
     * @return the smallest key in the BST */
    public Key min() {
        if (isEmpty()) return null;
        return min(root).key;
    } 
    
    // the smallest key in subtree rooted at x; null if no such key
    private Node min(Node x) { 
        if (x.left == null) return x; 
        else                return min(x.left); 
    } 
    
    /** Returns the largest key in the BST.
     * @return the largest key in the BST */
    public Key max() {
        if (isEmpty()) return null;
        return max(root).key;
    } 
    
    // the largest key in the subtree rooted at x; null if no such key
    private Node max(Node x) { 
        if (x.right == null) return x; 
        else                 return max(x.right); 
    }
}