import edu.princeton.cs.algs4.*;
import java.util.*;

public class Maze{
    public static void main(String[]args){
        int n = 40;
        Stopwatch timer = new Stopwatch();
        Maze maze = new Maze(n);
        StdOut.println("Time to generate mase = " + timer.elapsedTime());
        StdDraw.enableDoubleBuffering();
        maze.draw();
        timer = new Stopwatch();
        maze.solve();
        StdOut.println("Time to solve maze = " + timer.elapsedTime());
    }
    
    /* -------------------------------------------------------------------------
     * CLASS MEMBERS
     * ---------------------------------------------------------------------- */
    
    private int n;                  // dimension of maze (n x n)
    private boolean[][] N,E,S,W;    // is there a wall to N/E/S/W of cell i,j
    private boolean[][] visited;    // has the cell i,j been visited
    private int[][] cellLabel;      // label of cell i,j
    private boolean done = false;
    
    /** Initialize maze
     * @param n the dimension of this maze (n x n) */
    public Maze(int n){
        this.n = n;
        StdDraw.setXscale(0, n+2);
        StdDraw.setYscale(0, n+2);
        init();
        generate();
    }
    
    /** Initialize maze borders, walls, and cell labels */
    private void init(){
        N = new boolean[n+2][n+2];
        S = new boolean[n+2][n+2];
        E = new boolean[n+2][n+2];
        W = new boolean[n+2][n+2];
        visited = new boolean[n+2][n+2];
        cellLabel = new int[n+2][n+2];
        
        // Initialize border cells
        for(int i = 0; i < n+2; i++){
            visited[i][0] = visited[i][n+1] = true; // left and right borders
            visited[0][i] = visited[n+1][i] = true; // top and bottom borders
            cellLabel[i][0] = cellLabel[i][n+1] = 0;
            cellLabel[0][i] = cellLabel[n+1][i] = 0;
        }
        
        // Initialize all walls as present
        for(int x = 0; x < n+2; x++)
            for(int y = 0; y < n+2; y++)
                N[x][y] = E[x][y] = S[x][y] = W[x][y] = true;
        
        // Initialize cell labels (initially disjoint)
        int label = 1;
        for(int x = 1; x < n+1; x++)
            for(int y = 1; y < n+1; y++)
                cellLabel[x][y] = label++;
    }
    
    /* -------------------------------------------------------------------------
     * MAZE GENERATION
     * ---------------------------------------------------------------------- */
    
    /** Generate the maze using randomized Kruskal's algorithm*/
    private void generate(){
        
        int wallsRemoved = 0;
        int cells = (n*n);
        ArrayList<Integer> neighbor = new ArrayList<Integer>();
        
        
        while (wallsRemoved < cells - 1){//Filler
        
           int x = StdRandom.uniform(1, n+1);
           int y = StdRandom.uniform(1, n+1);
           
           if (1<2){//Filler
               int x2 = StdRandom.uniform(list.size());
               int y2 = StdRandom.uniform(list.size());
               
               if(x != x2 || y != y2){//Filler
               
               wallsRemoved = wallsRemoved +1;
               }
           }
        }
        
    }
    
    /* -------------------------------------------------------------------------
     * MAZE GENERATION UTILITIES
     * ---------------------------------------------------------------------- */
    
    /** Determines if the neighbor of cell x,y has a wall and is not a border
     * cell. The direction determines the neighbor as follows:
     *     0 --> north neighbor (x, y+1)
     *     1 --> east neighbor  (x+1, y)
     *     2 --> south neighbor (x, y-1)
     *     3 --> west neighbor  (x-1, y)
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @param dir the direction of the neighbor cell
     * @return true if the neighbor is valid, false otherwise */
    private boolean isValidNeighbor(int x, int y, int dir){
        return((dir == 0 && N[x][y] && !visited[x][y+1]) ||
               (dir == 1 && E[x][y] && !visited[x+1][y]) ||
               (dir == 2 && S[x][y] && !visited[x][y-1]) ||
               (dir == 3 && W[x][y] && !visited[x-1][y]));
    }
    
    /** Produces a list of neighbors that are valid neighbors of cell x,y
     *     0 --> north neighbor (x, y+1)
     *     1 --> east neighbor  (x+1, y)
     *     2 --> south neighbor (x, y-1)
     *     3 --> west neighbor  (x-1, y)
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell
     * @return an iterable list of cells that are valid neighbors of x,y */
    private ArrayList<Integer> validNeighbors(int x, int y){
        ArrayList<Integer> neighbors = new ArrayList<Integer>();
        for(int dir = 0; dir < 4; dir++)
            if(isValidNeighbor(x,y,dir)) neighbors.add(dir);
        return neighbors;
    }
    
    /** Performs a union of the disjoint components a and b
     * @param a the label of a component
     * @param b the label of a component */
    private void union(int a, int b){
        if(a > b)           // use the smaller of the labels
            union(b, a);
        else{
            // For any cell with the label b, change it to label a
            for(int x = 0; x < n+2; x++)
                for(int y = 0; y < n+2; y++)
                    if(cellLabel[x][y] == b) cellLabel[x][y] = a;
        }
    }
    
    /* -------------------------------------------------------------------------
     * MAZE SOLVER
     * ---------------------------------------------------------------------- */
    
    /** Solve this maze starting from the start state */
    public void solve(){
        for(int x = 1; x <= n; x++)
            for(int y = 1; y <= n; y++)
                visited[x][y] = false;
        done = false;
        solve(1,1);
    }
    
    /** Solve the maze using depth-first search from cell x,y
     * @param x the x coordinate of the cell
     * @param y the y coordinate of the cell */
    private void solve(int x, int y){
        if(x == 0 || y == 0 || x == n+1 || y == n+1) return;    // maze border
        if(done || visited[x][y]) return;
        
        // mark current cell as visited
        visited[x][y] = true;
        
        // draw current location
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
        StdDraw.show();
        StdDraw.pause(30);
        
        if(x == n/2 && y == n/2) done = true;   // reached goal (middle)
        
        if(!N[x][y]) solve(x, y+1); // dfs from north neighbor
        if(!E[x][y]) solve(x+1, y); // dfs from east neighbor
        if(!S[x][y]) solve(x, y-1); // dfs from south neighbor
        if(!W[x][y]) solve(x-1, y); // dfs from west neighbor
        
        if(done) return;
        
        // draw backtracked location
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.filledCircle(x + 0.5, y + 0.5, 0.25);
        StdDraw.show();
        StdDraw.pause(30);
    }
    
    /* -------------------------------------------------------------------------
     * DRAW MAZE UTILITY
     * ---------------------------------------------------------------------- */
    
    /** Draw the maze */
    public void draw(){
        // Draw start location & goal
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.filledCircle(n/2.0 + 0.5, n/2.0 + 0.5, 0.375);  // center
        StdDraw.filledCircle(1.5, 1.5, 0.375);                  // start
        
        // Draw walls
        StdDraw.setPenColor(StdDraw.BLACK);
        for(int x = 1; x <= n; x++)
            for(int y = 1; y <= n; y++){
                if(S[x][y]) StdDraw.line(x, y, x+1, y);     // south wall
                if(N[x][y]) StdDraw.line(x, y+1, x+1, y+1); // north wall
                if(W[x][y]) StdDraw.line(x, y, x, y+1);     // west wall
                if(E[x][y]) StdDraw.line(x+1, y, x+1, y+1); // east wall
            }
        StdDraw.show();
        StdDraw.pause(1000);
    }
}