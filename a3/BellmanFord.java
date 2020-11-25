/* NO COLLABORATORS */

import java.util.*;

public class BellmanFord{

    private int[] distances = null;
    private int[] predecessors = null;
    private int source;

    class BellmanFordException extends Exception{
        public BellmanFordException(String str){
            super(str);
        }
    }

    class NegativeWeightException extends BellmanFordException{
        public NegativeWeightException(String str){
            super(str);
        }
    }

    class PathDoesNotExistException extends BellmanFordException {
        public PathDoesNotExistException(String str) {
            super(str);
        }
    }
    

    /* Constructor, input a graph and a source
    * Computes the Bellman Ford algorithm to populate the
    * attributes 
    *  distances - at position "n" the distance of node "n" to the source is kept
    *  predecessors - at position "n" the predecessor of node "n" on the path
    *                 to the source is kept
    *  source - the source node
    *
    *  If the node is not reachable from the source, the
    *  distance value must be Integer.MAX_VALUE
    */
    BellmanFord(WGraph g, int source) throws NegativeWeightException {

        /* initialize attributes */
        this.source = source;
        this.distances = new int[g.getNbNodes()];
        this.predecessors = new int[g.getNbNodes()];

        /* initialize distances and predecessors */
        this.distances[source] = 0;
        this.predecessors[source] = -1;
        for (int i = 0; i < distances.length; i++) {
            if (i != source) {
                this.distances[i] = Integer.MAX_VALUE;
                this.predecessors[i] = Integer.MIN_VALUE;
            }
        }

        /* relax edges along paths of increasing size from 0 to |V(G)|-1 */
        for (int i = 0; i < this.distances.length-1; i++) {
            for (Edge e : g.getEdges()) {
                relax(g, e.nodes[0], e.nodes[1], e.weight);
            }
        }

        /* check for negative cycle and throw error if found */
        for (Edge e : g.getEdges()) {
            if (distances[e.nodes[1]] > distances[e.nodes[0]] + e.weight) {
                throw new NegativeWeightException("negative weight");
            }
        }
    }
    

    /*Returns the list of nodes along the shortest path from 
    * the object source to the input destination
    * If no path exists an Error is thrown
    */
    public int[] shortestPath(int destination) throws PathDoesNotExistException{
        ArrayList<Integer> path_backward = new ArrayList<>();
        int cur_node = destination;

        /* backtrace from destination to source using each node's "predecessor" */
        while (cur_node != -1) {
            if (this.predecessors[cur_node] == Integer.MIN_VALUE) {
                throw new PathDoesNotExistException("no patho amigo");
            } else {
                path_backward.add(cur_node);
                cur_node = this.predecessors[cur_node];
            }
        }
        
        /* reverse the backward path list and copy into array */
        int[] path = new int[path_backward.size()];
        for (int i = 0; i < path.length; i++) {
            path[i] = path_backward.get(path.length - (i+1));
        }
        
        return path;
    }

    public void printPath(int destination){
        /*Print the path in the format s->n1->n2->destination
         *if the path exists, else catch the Error and 
         *prints it
         */
        try {
            int[] path = this.shortestPath(destination);
            for (int i = 0; i < path.length; i++){
                int next = path[i];
                if (next == destination){
                    System.out.println(destination);
                }
                else {
                    System.out.print(next + "-->");
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    public static void main(String[] args) {

        String file = args[0];
        WGraph g = new WGraph(file);
        try {
            BellmanFord bf = new BellmanFord(g, g.getSource());
            bf.printPath(g.getDestination());
        } catch (Exception e) {
            System.out.println(e);
        }

    }


    /* helper method for relaxing edges in a graph */
    private void relax(WGraph g, int u, int v, int weight) {
        if (this.distances[v] > this.distances[u] + weight) {
            this.distances[v] = this.distances[u] + weight;
            this.predecessors[v] = u;
        }
    }
}

