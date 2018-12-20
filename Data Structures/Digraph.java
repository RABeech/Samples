import java.util.*;                 // HashMap, HashSet, Set
import edu.princeton.cs.algs4.*;    // In, StdOut, Out
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

/**
 * Adjacency list representation of a directed graph.
 * 
 * Method Summary:
 * int      V()                 returns number of vertices in this graph
 * int      E()                 returns number of edges in this graph
 * void     addVertex(V v)      adds the isolated vertex v to this graph
 * void     addEdge(V v, V w)   adds the directed edge (v -> w) to this graph
 * Set<V>   vertices()          returns the set of all vertices in this graph
 * Set<V>   adj(V v)            returns the set of vertices adjacent to vertex v
 * 
 * @param <V> the generic type of vertices in this graph
 */
public class Digraph<V>{
    private static final int MAX_LEN = 5;   // maximum string length of vertices
    
    public static void main(String[]args){
        Stopwatch timer = new Stopwatch();
        Digraph<String> graph = new Digraph<String>();
        
        Queue<String> source = new Queue<String>(); // source vertices
        Queue<String> dest = new Queue<String>();   // destination vertices
        
        In in = new In("vertices.txt");
        String[] words = in.readAllStrings();
        in.close();
        
        for(String v : words){
            if(v.length() <= MAX_LEN){
                graph.addVertex(v);
                for(String w : graph.vertices()){
                    // Check if an edge can be formed between the new vertex
                    // and all other existing vertices in the graph
                    if(isValidEdge(v,w)) graph.addEdge(v,w);
                    else if(isValidEdge(w,v)) graph.addEdge(w,v);
                }
                // Check if the vertex is a potential source/destination vertex
                if(v.length() == MAX_LEN) source.enqueue(v);
                else if(v.length() == 1) dest.enqueue(v);
            }
        }
        
        StdOut.println("Graph built with " + graph.V() + " vertices and " +
            graph.E() + " edges (" + timer.elapsedTime() + " seconds)");
        
        Out out = new Out("paths.txt");
        int numPaths = 0;
        
        for(String s : source)
            for(String d : dest)
                if(graph.hasPath(s,d))
                    // If the path from s to d exists, get all possible paths
                    for(Stack<String> stack : graph.paths(s,d)){
                        numPaths++;
                        while(!stack.isEmpty()){
                            out.print(stack.pop());
                            if(!stack.isEmpty()) out.print(" -> ");
                        }
                        out.println();
                    }
        out.close();
        
        StdOut.println("Total paths found: " + numPaths + " (" +
            timer.elapsedTime() + " seconds)");
    }
    
    /* -------------------------------------------------------------------------
     * VALID EDGE UTILITY
     * ---------------------------------------------------------------------- */
    
    /** Returns whether the String v can be formed by adding a letter to the
     * String w (i.e. whether the edge v -> w is valid)
     * @param v the tail vertex in the potential edge
     * @param w the head vertex in the potential edge
     * @return true if the directed edge (v -> w) is valid */
    public static boolean isValidEdge(String v, String w){
        if(v.length() != w.length()+1) return false;
        // Remove one letter from the string w and check if it equals v
        for(int i = 0; i < v.length(); i++){
            String sub = v.substring(0,i) + v.substring(i+1,v.length());
            if(sub.equals(w)) return true;
        }
        return false;
    }
    
    /* -------------------------------------------------------------------------
     * CLASS MEMBERS
     * ---------------------------------------------------------------------- */
    private HashMap<V,Set<V>> adjList;  // adjacency list
    private int vertices = 0;           // number of vertices in this graph
    private int edges = 0;              // number of edges in this graph
    
    /** Initializes an empty directed graph */
    public Digraph(){ adjList = new HashMap<V,Set<V>>(); }
    
    /** Returns the number of vertices in this graph
     * @return the number of vertices in this graph */
    public int V(){ return vertices; }
    
    /** Returns the number of edges in this graph
     * @return the number of edges in this graph */
    public int E(){ return edges; }
    
    /** Adds a new isolated vertex (a vertex with degree 0, having no edges) if
     * it does not already exist in this graph
     * @param v the vertex */
    public void addVertex(V v){
        if(!adjList.containsKey(v)){
            adjList.put(v, new HashSet<V>());
            vertices++;
        }
    }
    
    /** Adds the directed edge (v -> w) to this graph. If a vertex is not in
     * this graph, it will be added
     * @param v the tail vertex in the directed edge
     * @param w the head vertex in the directed edge */
    public void addEdge(V v, V w){
        addVertex(v);   // add vertex v [contains check handled in addVertex()]
        addVertex(w);   // add vertex w [contains check handled in addVertex()]
        
        if(!adj(v).contains(w)){    // add w to v's set of adjacent vertices
            adj(v).add(w);
            edges++;
        }
    }
    
    /** Returns an iterable set of all vertices in this graph. To iterate over
     * the vertices, use the foreach notation, e.g.:
     *      for(String v : graph.vertices()){ }
     * @return an iterable set of vertices in this graph */
    public Set<V> vertices(){ return adjList.keySet(); }
    
    /** Returns the set of vertices adjacent to the vertex v. To iterate over
     * the adjacent vertices, use the foreach notation, e.g.:
     *      for(String w : grpah.adj(v)){ }
     * @param v the vertex
     * @return an iterable set of vertices adjacent to the vertex v */
    public Set<V> adj(V v){ return adjList.get(v); }
    
    /* -------------------------------------------------------------------------
     * DFS PATH UTILITIES
     * ---------------------------------------------------------------------- */
    
    /* HAS PATH */
    
    private HashMap<V,Boolean> hasPath; // is there a path to v from source?
    private V source;                   // source vertex
    
    /** Returns whether a path exists from vertex v to vertex w by a depth-first
     * traversal of the vertices in this graph starting from the source vertex v
     * @param v the source vertex in the path
     * @param w the destination vertex in the path
     * @return true if a path exists from v to w, false otherwise */
    public boolean hasPath(V v, V w){
        if(!v.equals(source)){
            source = v;
            hasPath = new HashMap<V,Boolean>();
            for(V x : vertices()) hasPath.put(x,false);
            dfs(v);
        }
        return hasPath.get(w);
    }
    
    // Runs a dfs traversal from vertex v
    private void dfs(V v){
        hasPath.put(v,true);
        for(V w : adj(v))
            if(!hasPath.get(w)) dfs(w);
    }
    
    /* GET PATH */
    
    private HashMap<V,Boolean> visited;     // has the vertex been visited?
    private Stack<V> path;                  // DFS path
    
    /** Returns an iterable set of paths from the vertex v to the vertex w. To
     * iterate over each path, use the nested foreach notation, e.g.:
     *      for(Stack<String> stack : graph.paths(v,w))
     *          for(String pathVertex : stack){ }
     * @param s the source vertex in the paths to retrieve
     * @param d the destination vertex in the paths to retrieve
     * @return an iterable set of paths from the vertex v to the vertex w */
    public Set<Stack<V>> paths(V s, V d){
        /* Initialize a new set of paths, set all vertices in the graph as not
         * yet visited, and build the path starting from the source vertex s */
        Set<Stack<V>> setOfPaths = new HashSet<Stack<V>>();
        visited = new HashMap<V,Boolean>();
        path = new Stack<V>();
        
        for(V v : vertices()) visited.put(v, false);
        paths(setOfPaths, s, d);
        
        return setOfPaths;
    }
    
    /* Builds the set of paths from the vertex s to the vertex w using a depth-
     * first traversal
     * @param setOfPaths the set of paths to add to
     * @param s the source vertex
     * @param d the destination vertex */
    private void paths(Set<Stack<V>> setOfPaths, V s, V d){
        

        visited.put(s, true);
        path.push(s);
        
        if (s == d){
         Stack<V> S = new Stack<V>();{
            for (V v : path){
               S.push(v);
            }
            setOfPaths.add(S);
         }
        }else{
         for (V v : adj(s)){
            if (visited.get(v) != true){
               paths(setOfPaths, v, d);
            }
         }
        }
        
        path.pop();
        visited.put(s, false);
        
        
    }
}