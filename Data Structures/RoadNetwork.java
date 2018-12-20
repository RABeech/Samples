import edu.princeton.cs.algs4.*;    // In, Out, StdOut, DirectedEdge
import edu.princeton.cs.algs4.Stack;
import java.util.*;                 // Set, HashSet, HashMap, StringTokenizer

public class RoadNetwork{
    public static void main(String[]args){
        // Files
        In in = new In("NCRoadNetwork.txt");
        Out out = new Out("paths.txt");
        
        // Build the NC road network graph
        RoadNetwork graph = new RoadNetwork();
        
        // Get all vertices from file (stops when number of edges found)
        in.readLine(); // discard number of vertices
        StringTokenizer st = new StringTokenizer(in.readLine()," ");
        while(st.countTokens() == 3){
            graph.addVertex(
                Integer.parseInt(st.nextToken()),   // id
                Integer.parseInt(st.nextToken()),   // longitude
                Integer.parseInt(st.nextToken())    // latitude
            );
            st = new StringTokenizer(in.readLine()," ");
        }
        
        // Get all edges from file
        while(in.hasNextLine()){
            st = new StringTokenizer(in.readLine()," ");
            graph.addEdge(
                Integer.parseInt(st.nextToken()),   // source id
                Integer.parseInt(st.nextToken()),   // target id
                Double.parseDouble(st.nextToken())  // edge weight (travel time)
            );
        }
        in.close();
        
        StdOut.println("NC Road Network graph created with " + graph.V() +
                       " vertices and " + graph.E() + " undirected edges\n");
        
        int source = 519603; // Set source vertex to UNCC
        
        String[] destLabel = {"Bank of America Stadium", "Biltmore Estate",
                              "Wright Brothers National Memorial"};
        int[] destID = {517785, 77199, 228758};
        
        // Run Dijkstra's algorithm from source
        graph.runDijkstraSP(source);
        
        // Find and print paths to destination vertices
        for(int i = 0; i < destID.length; i++){
            out.println("Path to " + destLabel[i]);
            if(graph.hasPathTo(destID[i])){
                // Print path to file
                for(DirectedEdge e : graph.pathTo(destID[i]))
                    out.println(graph.get(e.from()) +" -> "+ graph.get(e.to()));
                
                // Print travel time to file and terminal
                StdOut.println("Travel time to " + destLabel[i] + ": " + 
                               graph.distTo(destID[i]));
                
                out.println("Travel Time: " + graph.distTo(destID[i]) + "\n");
            }else{
                StdOut.println("No path found to " + destLabel[i]);
                out.println("No path found.");
            }
        }
        out.close();
    }
    
    /* -------------------------------------------------------------------------
     * DIJKSTRA'S SHORTEST PATH ALGORITHM
     * ---------------------------------------------------------------------- */
    
    private DirectedEdge[] edgeTo;  // last edge on shortest s->v path
    private double[] distTo;        // distance of shortest s->v path
    private IndexMinPQ<Double> pq;  // priority queue of vertices
    
    /** Performs Dijkstra's shortest path algorithm from source vertex s
     * @param s the source vertex */
    public void runDijkstraSP(int s){
         
         edgeTo = new DirectedEdge[V()];
         distTo = new double[V()];
         pq = new IndexMinPQ<Double>(V());
         
         for (int v = 0; v < V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
         distTo[s] = 0.0;
         
         pq.insert(s, 0.0);
         while (!pq.isEmpty()){
            int v = pq.delMin();
            for (DirectedEdge e : adj(v))
               relax(e);
         }
        
    }
    
    // Relax the edge e and update pq if changed
    private void relax(DirectedEdge e){
        
        int v = e.from();
        int w = e.to();
        
        if (distTo[w] > distTo[v] + e.weight()){
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            
            if (pq.contains(w))
               pq.decreaseKey(w, distTo[w]);
            else
               pq.insert(w, distTo[w]);
        }
        
    }
    
    /** Returns the length of the shortest path from the source vertex s to the
     * destination vertex v
     * @param v the destination vertex
     * @return the length of the shortest path from the source vertex s to the
     *         destination vertex v */
    public double distTo(int v){ return distTo[v]; }
    
    /** Returns true if there is a path from the source vertex s to the
     * destination vertex v
     * @param v the destination vertex
     * @return true if there is a path from the source vertex s to the
     *         destination vertex v, false otherwise */
    public boolean hasPathTo(int v){
        return distTo[v] < Double.POSITIVE_INFINITY;
    }
    
    /** Returns a shortest path from the source vertex s to the destination
     * vertex v, or null if no such path exists
     * @param v the destination vertex
     * @return a shortest path from the source vertex s to the destination
     *         vertex v as an iterable list (Stack) of edges or null if no such 
     *         path exists */
    public Iterable<DirectedEdge> pathTo(int v){
        if(!hasPathTo(v)) return null;
        Stack<DirectedEdge> path = new Stack<DirectedEdge>();
        for(DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
            path.push(e);
        return path;
    }
    
    /* -------------------------------------------------------------------------
     * RoadNetwork GRAPH MEMBERS
     * ---------------------------------------------------------------------- */
    
    private HashMap<Integer,Vertex> vertices;   // list of vertices
    private int V = 0;                          // number of vertices
    private int E = 0;                          // number of undirected edges
    
    /** Initializes an empty undirected graph */
    public RoadNetwork(){ vertices = new HashMap<Integer,Vertex>(); }
    
    /** Returns the number of vertices in this road network graph
     * @return the number of vertices in this road network graph */
    public int V(){ return V; }
    
    /** Returns the number of undirected edges in this road network graph
     * @return the number of undirected edges in this road network graph */
    public int E(){ return E; }
    
    /** Adds an isolated vertex to this road network graph
     * @param id the index (id) of the vertex
     * @param longitude the longitude coordinate of the vertex
     * @param latitude the latitude coordinate of the vertex */
    public void addVertex(int id, int longitude, int latitude){
        vertices.put(id, new Vertex(id, longitude, latitude));
        V++;
    }
    
    /** Returns the vertex corresponding to id
     * @return the vertex corresponding to id */
    public Vertex get(int v){
        return vertices.get(v);
    }
    
    /** Adds the undirected edge v<->w to this road network graph
     * @param v the id of a vertex
     * @param w the id of a vertex
     * @param weight the weight of the edge v<->w */
    public void addEdge(int v, int w, double weight){
        vertices.get(v).addEdge(new DirectedEdge(v,w,weight));
        vertices.get(w).addEdge(new DirectedEdge(w,v,weight));
        E++;
    }
    
    /** Returns the set of directed edges from vertex v
     * @param v the vertex
     * @return the set of directed edges from the vertex v */
    public Set<DirectedEdge> adj(int v){ return vertices.get(v).adj(); }
    
    /** Returns all directed edges in this road network graph
     * @return an iterable set of all directed edges in this graph */
    public Set<DirectedEdge> edges(){
        HashSet<DirectedEdge> set = new HashSet<DirectedEdge>();
        for(int v : vertices.keySet())
            for(DirectedEdge e : adj(v))
                set.add(e);
        return set;
    }
    
    /* -------------------------------------------------------------------------
     * HELPER VERTEX CLASS
     * ---------------------------------------------------------------------- */
    
    private class Vertex{
        private int id;         // id of this vertex
        private int longitude;  // longitude coordinate of this vertex
        private int latitude;   // latitude coordinate of this vertex
        Set<DirectedEdge> adj;  // adjacency set of edges
        public Vertex(int id, int longitude, int latitude){
            this.id = id;
            this.longitude = longitude;
            this.latitude = latitude;
            adj = new HashSet<DirectedEdge>();
        }
        public void addEdge(DirectedEdge e){ adj.add(e); }
        public Set<DirectedEdge> adj(){ return adj; }
        public String toString(){
            return "[" + id + " " + longitude + " " + latitude + "]";
        }
    }
}