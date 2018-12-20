/**
 * Created by mcshin on 1/25/17.
 */

import edu.princeton.cs.algs4.*;
import java.util.*;

public class Stack<Item> implements Iterable<Item> {
    private Node<Item> top;     // Top item in the stack
    
    private static class Node<Item> {
        private Item item;          // Item belonging to this node
        private Node<Item> below;   // Next item in the stack
        
    }
    
    /** Initializes an empty stack */
    public Stack() {
        top = null;
    }
    
    /** Returns whether the stack is empty
     * @return true if the stack is empty, false otherwise */
    public boolean isEmpty() { return top == null; }
    
    /** Returns the number of items in this stack
     * @return the number of items in this stack */
    public int size() {
        
        int n = 0;
        
        Node<Item>temp = top;
        
        while (temp!=null){
         n++;
         temp = temp.below;
        }
         
        return n; 
        
    }
    
    /** Adds an item to the top of the stack
     * @param item the item to add to the stack */
    public void push(Item item) {
        
        Node<Item>temp = new Node<Item>();
        temp.item = item;
        temp.below = top;
        top = temp;
        
    }
    
    /** Removes and returns the top item in the stack
     * @return the top item in the stack */
    public Item pop() {
        
        if (isEmpty()) return null;
        
        Item item = top.item;
        top = top.below;
        
        return item;
        
    }
    
    /** Returns the top item in the stack without removing
     * @return the top item in the stack */
    public Item peek() {
        
        if (isEmpty()) return null;
        
        Item item = top.item;
        return item;
        
    }
    
    /** Returns the string representation of this stack
     * @return the string representation of this stack */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item);
            s.append(' ');
        }
        return s.toString();
    }
    
    /** Returns an iterator for this stack that iterates in LIFO order
     * @return an iterator that iterates in LIFO order */
    public Iterator<Item> iterator() {
        return new ListIterator<Item>(top);
    }
 
    /** An iterator, doesn't implement remove() since it's optional */
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;
        
        /** Initializes the current node to the top node in the stack */
        public ListIterator(Node<Item> top) {
            current = top;
        }
        /** Returns whether there are more items in the iteration sequence */
        public boolean hasNext() {
            return current != null;
        }
        /** NOT IMPLEMENTED */
        public void remove() {
            throw new UnsupportedOperationException();
        }
        /** Returns the next item in the iteration sequence */
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.below;
            return item;
        }
    }
 
 
    /**
     * Unit tests the {@code Stack} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        // Add "output.txt" as a parameter to Out() to print output to file
        Out out = new Out("output.txt");
 
        Stack<String> stack = new Stack<String>();  // Stack
        In in = new In("input.txt");                // Input file
        
        // Push/pop items from input file
        while ( in.hasNextLine() ) {
            String item = in.readString();
            if (!item.equals("-"))
                stack.push(item);
            else if (!stack.isEmpty())
                out.print(stack.pop() + " ");
        }
        
        out.println("\nThe last remaining item on the stack is: " + stack.peek());
        out.println("(" + stack.size() + " left on stack)");
 
        for(String stackItem : stack)
            out.print(stackItem + " ");
        out.println(" (remaining on stack)");
        
        // EXTRA CREDIT: reverse the stack and print the results
    }
 
 
}