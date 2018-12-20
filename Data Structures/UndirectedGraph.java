import java.util.*;                 // HashMap, HashSet, Set, StringTokenizer
import edu.princeton.cs.algs4.*;    // In, StdOut
import edu.princeton.cs.algs4.Queue;

/**
 * Adjacency list representation of an undirected graph.
 * 
 * Method summary:
 * int      V()                 returns number of vertices in the graph
 * void     addVertex(V v)      adds the isolated vertex v to the graph
 * void     addEdge(V v, V w)   adds the edge (v-w) to the graph
 * Set<V>   vertices()          returns the set of all vertices in the graph
 * Set<V>   adj(V v)            returns the set of vertices adjacent to vertex v
 * 
 * @param <V> the generic type of vertices in this graph
 */
public class UndirectedGraph<V>{
    private static final String BACON = "Bacon, Kevin";
    
    public static void main(String[]args){
        // Build the graph from movies.txt
        UndirectedGraph<String> graph = new UndirectedGraph<String>();
        In in = new In("movies.txt");
        
        while(in.hasNextLine()){
            StringTokenizer st = new StringTokenizer(in.readLine(),"/");
            String movie = st.nextToken();
            while(st.hasMoreTokens()){
                String actor = st.nextToken();
                graph.addEdge(movie,actor);     // automatically adds vertices
            }
        }
        
        StdOut.println("Number of vertices: " + graph.V());
        
        /* 
         * Find the actor(s)/actress(es) with the highest Bacon number using a
         * BFS starting at the vertex BACON.
         * 
         * Queue<String> q                          - BFS data structure
         * HashMap<String,Integer> distFromBacon    - number of edges from BACON
         * HashMap<String,Boolean> visitedVertex    - set of visited vertices
         */
        
        Queue<String> q = new Queue<String>();
        HashMap<String,Integer> distFromBacon = new HashMap<String,Integer>();
        HashMap<String,Boolean> visitedVertex = new HashMap<String,Boolean>();
        
        /* Initialize all vertices in the graph as unreachable (distance = -1)
         * and not yet visited (visited = false). */
        for(String vertex : graph.vertices()){
            distFromBacon.put(vertex, -1);
            visitedVertex.put(vertex, false);
        }
        
        
        
        /* Determine the distance to vertices via BFS:
         * 
         * add Kevin Bacon to the queue
         * set the distance to Kevin Bacon to 0
         * mark Kevin Bacon as visited
         *
         * while queue is not empty
         *   remove the next vertex (v) from the (FIFO) queue
         *   for each vertex (w) adjacent to vertex (v)
         *     if vertex (w) has not been visited
         *       add it to the queue and mark it as visited
         *       set its distance from the source = dist(v) + 1
         * 
         * Usage Notes:
         *   to add a key-value pair to a HashMap --> hashmap.put(key, value);
         *   to retrieve a value from a HashMap   --> hashmap.get(key)
         *   a constant String for Kevin Bacon has been declared for you: BACON
         */
        
          q.enqueue(BACON);
          distFromBacon.put(BACON, 0);
          visitedVertex.put(BACON, true); 
          
          Out out = new Out("Output.txt");
          
          while (q.isEmpty() != true){
            String temp = q.dequeue();
            for(String adjVertex : graph.adj(temp)){
               if (!visitedVertex.get(adjVertex)){
                  visitedVertex.put(adjVertex, true);
                  distFromBacon.put(adjVertex, distFromBacon.get(temp)+1);
                  q.enqueue(adjVertex);
                  }
               
               }
          }
          
          for ( 

  }
    
    /* -------------------------------------------------------------------------
     * CLASS MEMBERS
     * ---------------------------------------------------------------------- */
    
    // Adjacency list: V (vertex) --> Set<V> (adjacent vertices)
    private HashMap<V,Set<V>> adjList;
    
    /** Initializes an empty undirected graph */
    public UndirectedGraph(){
        adjList = new HashMap<V,Set<V>>();
    }
    
    /** Returns the number of vertices in this graph
     * @return the number of vertices in this graph */
    public int V(){ return adjList.size(); }
    
    /** Adds a new isolated vertex (a vertex with degree 0, having no edges) if
     * it does not already exist in this graph
     * @param v the vertex */
    public void addVertex(V v){
        if(!adjList.containsKey(v))
            adjList.put(v, new HashSet<V>());
    }
    
    /** Adds the undirected edge (v-w) to this graph. If a vertex is not in this
     * graph, it will be added
     * @param v a vertex in the undirected edge to create
     * @param w a vertex in the undirected edge to create */
    public void addEdge(V v, V w){
        addVertex(v);   // add vertex v [contains check handled in addVertex()]
        addVertex(w);   // add vertex w [contains check handled in addVertex()]
        
        if(!adj(v).contains(w)) // add w to v's set of adjacent vertices
            adj(v).add(w);
        if(!adj(w).contains(v)) // add v to w's set of adjacent vertices
            adj(w).add(v);
    }
    
    /** Returns an iterable set of vertices in this graph. To iterate over the
     * vertices, use the foreach notation, e.g.:
     *      for(String vertex : graph.vertices()){ }
     * @return an iterable set of vertices in this graph */
    public Set<V> vertices(){ return adjList.keySet(); }
    
    /** Returns the set of vertices adjacent to the vertex v. To iterate over
     * the adjacent vertices, use the foreach notation, e.g.:
     *      for(String adjVertex : graph.adj(vertex)){ }
     * @return an iterable set of vertices adjacent to the vertex v */
    public Set<V> adj(V v){ return adjList.get(v); }
}