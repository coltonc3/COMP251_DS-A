import java.util.*;


/* NO COLLABORATORS */
public class Kruskal{

    /* take a graph object of the class WGraph as an input
    * @return another WGraph object which will be the MST of the input graph
    */
    public static WGraph kruskal(WGraph g){
        WGraph mst = new WGraph();
        DisjointSets sets = new DisjointSets(g.getNbNodes());
        // iterate through edges in monotonic increasing order of weight so we get visit light edges first
        for (Edge e : g.listOfEdgesSorted()) {
            if (IsSafe(sets, e)) {
                // add edge to the growing MST
                mst.addEdge(e);
                // union the sets containing the edge's nodes
                sets.union(e.nodes[0], e.nodes[1]);
            }
        }
        return mst;
    }

    /* considers a partition of the nodes p and an edge e
    * @return True if it is safe to add the edge e to the MST, and False otherwise
    */
    public static Boolean IsSafe(DisjointSets p, Edge e){
        /* e is not safe to add if it creates a cycle; if e's nodes are in the same set, adding e will create a cycle */
        return p.find(e.nodes[0]) == p.find(e.nodes[1]) ? false : true;
    }

    public static void main(String[] args){

        String file = args[0];
        WGraph g = new WGraph(file);
        WGraph t = kruskal(g);
        System.out.println(t);

   } 
}
