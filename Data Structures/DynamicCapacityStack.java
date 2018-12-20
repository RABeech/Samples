import edu.princeton.cs.algs4.*;
import java.util.*;

public class DynamicCapacityStack<E> implements Iterable<E>{

   private static final int CAP = 2;
   
   private E[] stack;
   private int N = 0;
   
   public DynamicCapacityStack(){
      stack = (E[]) new Object[CAP];
   }
   
   public boolean isEmpty(){ return N == 0; }
   
   public int size(){ return N; };
   
   public void push(E item){
      if( N == stack.length )
         resize(2*stack.length);
      stack[N++] = item;   
   }
   
   public E pop(){
   
      if ((N-1) > 0 && (N-1) == stack.length/4) resize (stack.length/2);
   
      return isEmpty() ? null : stack[--N];
   }
   
   public E peek(){
   
      return stack[N-1];
   }
   
   
   private void resize(int capacity){
      E[] temp = (E[]) new Object[capacity];
      for(int i = 0; i < N; i++)
         temp[i] = stack[i];
      stack = temp;   
   }
   
   public Iterator<E> iterator(){ return new StackIterator(); }
   
   private class StackIterator implements Iterator<E>{
      private int i;
      
      public StackIterator(){
      
      //Code Here
      
      }
      public boolean hasNext(){
      
      // code here
      return false;
      }
      public void remove(){ /* do not implement */ }
      public E next(){
         if(!hasNext()) throw new NoSuchElementException();
         
         return null;
      }
      }
      
      public static void main(String[] args){
      
      Out out = new Out("output.txt");
      
      DynamicCapacityStack<String> stack = new DynamicCapacityStack<String>();
      
      In in = new In("input.txt");
      
      while(in.hasNextLine()){
         String item = in.readString();
         if(!item.equals("-"))
            stack.push(item);
         else
            out.print(stack.pop() + " ");   
      }
      
      out.println();
      out.println("The last remaining item on the stack is: " + stack.peek());
      out.println(stack.size() + " items left on the stack");
      
      in.close();
      out.close();
      
      
   }   
}    