import edu.princeton.cs.algs4.*;

public class FixedCapacityStack<E>{

   // Maximum number of items this stack can hold
   private static final int CAP = 10;
   
   private E[] stack;   // stack entries
   private int N;       // size
   
   public FixedCapacityStack(){
      stack = (E[]) new Object[CAP];
   }
   
   public boolean isEmpty() {return N == 0;}
   
   public int size() {return N;}
   
   public void push(E item){
      stack[N++] = item;
   }
   
   public E pop() {
      
      if (N.isEmpty()) return stack[--N];
      else return null;
      
         }
   
   public static void main (String [] args){
   
      FixedCapacityStack<String> stack = new FixedCapacityStack<>();
      In in = new In("input.txt");
      
      Out out = new Out("output.txt");
      
      while (in.hasNextLine()){
         String item = in.readString();
         if(!item.equals("-"))
            stack.push(item);
         else
            StdOut.println(stack.pop() + " ");
            
         }   
         
         StdOut.println();
         StdOut.println(stack.size() + " items left on stack");
         
         in.close();
         out.close();
   }

}