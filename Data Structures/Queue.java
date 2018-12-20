/**
 * Created by mcshin on 1/27/17.
 */

import edu.princeton.cs.algs4.*;
import java.util.*;

public class Queue<Item> implements Iterable<Item> {
    private Node<Item> front;   // First item in the queue
    private Node<Item> rear;    // Last item in the queue
    
    private static class Node<Item> {
        private Item item;          // The item belonging to this node
        private Node<Item> next;    // The next node in the linked list
    }
    
    /** Initializes an empty queue */
    public Queue() {
        front = null;
    }
    
    /** Returns whether the queue is empty
     * @return true if the queue is empty, false otherwise */
    public boolean isEmpty() {
        return front == null;
    }
    
    /** Returns the number of items in this queue
     * @return the number of items in this queue */
    public int size() {
        
        Node<Item>temp = front;
        int n = 0;
        
        while (temp !=null){
         n++;
         temp = temp.next;
        }
        
        return n;
        
    }
    
    /** Returns the first item in the queue or null if the queue is empty
     * @return the first item in the queue */
    public Item peek() {
        
        if (isEmpty()) return null;
        
        Item item = front.item;
        return item;
        
    }

    /** Adds an item to the queue
     * @param item the item to add to the queue */
    public void enqueue(Item item) {
        
        Node<Item> temp = new Node<Item>();
        temp.item = item;
        
        if (isEmpty()){
         front = temp; rear = temp;
        }else{
         rear.next = temp;
         rear = temp;
        }
        
    }

    /** Removes and returns the first item in the queue or null if the queue is empty
     * @return the first item in the queue */
    public Item dequeue() {
        
        if (isEmpty()) return null;
        Item item = front.item;
        front = front.next;
        if (front == null) rear = null;
        return item;
        
    }

    /** Returns a string representation of this queue.
     * @return the sequence of items in FIFO order, separated by spaces */
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Item item : this) {
            s.append(item);
            s.append(' ');
        }
        return s.toString();
    }

    /** Returns an iterator that iterates over the items in this queue in FIFO order
     * @return an iterator that iterates over the items in this queue in FIFO order */
    public Iterator<Item> iterator()  {
        return new ListIterator<Item>(front);
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public ListIterator(Node<Item> front) {
            current = front;
        }

        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        // After completing your code, add "output.txt" as a parameter to the Out() constructor
        // to print output to file
        Out out = new Out("output.txt");
        
        Queue<String> queue = new Queue<String>();  // Queue
        In in = new In("input.txt");                // Input file
        
        // Read input file
        while (in.hasNextLine()) {
            String item = in.readString();
            if (!item.equals("-"))
                queue.enqueue(item);
            else if (!queue.isEmpty())
                out.print(queue.dequeue() + " ");
        }
        
        out.println("\nThe next remaining item on the queue is: " + queue.peek());
        out.println("(" + queue.size() + " left in queue)");
        
        // Print remaining items in the queue
        for (String queueItem: queue)
            out.print(queueItem+ " ");
        out.println(" (remaining in queue)");
        
        // Remove all remaining items in the queue
        while( !queue.isEmpty() )
            queue.dequeue();
        out.println("There are " + queue.size() + " items left in the queue after removal.");
        
        in.close();
        out.close();
    }


}