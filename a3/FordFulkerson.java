import java.util.*;
import java.io.File;

public class FordFulkerson {

	/*
	 * finds a path via DFS between nodes "source" and "destination" (inclusive) in the graph,
	 * returns empty ArrayList if no such path exists
	 */
	public static ArrayList<Integer> pathDFS(Integer source, Integer destination, WGraph graph){
		ArrayList<Integer> path = new ArrayList<Integer>();
		ArrayList<Integer> visited = new ArrayList<>();
		ArrayList<Integer> adjacent = new ArrayList<>();

		int cur_node = source;
		path.add(cur_node);
		visited.add(cur_node);

		if (cur_node == destination) {
			return path;
		}

		/* get all adjacent nodes with capacity for flow (includes forward and backward edges) */
		for (Edge e : graph.getEdges()) {
			if (e.nodes[0] == cur_node && e.weight > 0) {
				adjacent.add(e.nodes[1]);
			}
		}
		
		/* run DFS on adjacent nodes until we get get a path to destination */
		for (int a : adjacent) {
			WGraph edges_removed = new WGraph();

			/* initialize graph with some removed edges */
			for (Edge e : graph.getEdges()) {
				if (!visited.contains(e.nodes[0]) && !visited.contains(e.nodes[1])) {
					edges_removed.addEdge(new Edge(e.nodes[0], e.nodes[1], e.weight));
				}
			}

			/* recursively call DFS on adjacents */
			ArrayList<Integer> next_path = pathDFS(a, destination, edges_removed);

			/* check if this is truly a path to the destination and return it if so*/
			if (!next_path.isEmpty() && next_path.get(next_path.size() - 1) == destination) {
				path.addAll(next_path);
				return path;
			}
		}
		
		/* only reached if there is no path from source to vertex */
		return new ArrayList<Integer>();
	}


	/*
	 * computes an integer corresponding to the max flow of the graph as well as the graph
	 * encoding of the assignment associated with this max flow
	 */
	public static String fordfulkerson(WGraph graph) {
		String answer = "";
		int maxFlow = 0;

		/* make copy of graph so we can edit */
		WGraph residual = new WGraph(graph);
		/* initialize all edges (flows) to 0 */
		for (Edge e : residual.getEdges()) {
			e.weight = 0;
		}
		/* make residual graph */
		makeResidual(graph, residual);

		ArrayList<Edge> edge_path = new ArrayList<>();
		ArrayList<Integer> vertex_path = pathDFS(residual.getSource(), residual.getDestination(), residual);
	
		while (!vertex_path.isEmpty()) {
			edge_path.clear();
			for (int i = 0; i < vertex_path.size() - 1; i++) {
				edge_path.add(residual.getEdge(vertex_path.get(i), vertex_path.get(i + 1)));
			}
			if (getBottleneck(graph, edge_path) == 0)
				break;
			augment(graph, residual, edge_path);
			vertex_path = pathDFS(residual.getSource(), residual.getDestination(), residual);
		}
	
		for (Edge e : residual.getEdges()) {
			/* add outoing flow of source to maxFlow */
			if (e.nodes[0] == graph.getDestination()) {
				maxFlow += e.weight;
			}

			/* set weights of all edges in original graph to the weight (flow) of their corresponding backward edge */
			Edge original_edge = graph.getEdge(e.nodes[1], e.nodes[0]);
			if (original_edge != null) {
				graph.setEdge(original_edge.nodes[0], original_edge.nodes[1], e.weight);
			}
		}
	
		answer += maxFlow + "\n" + graph.toString();
		return answer;
	}


	public static void main(String[] args) {
		String file = args[0];
		File f = new File(file);
		WGraph g = new WGraph(file);
		System.out.println(fordfulkerson(g));
	}

	/* method that augments a path along the residual graph, updates all forward and backward edges */
	private static void augment(WGraph original, WGraph residual, ArrayList<Edge> path) {
		int bottleneck = getBottleneck(residual, path);
		if (bottleneck == 0) {
			return;
		}
		else {
			// update edges
			for (Edge e : path) {
				e.weight -= bottleneck;
				// corresponding forward/backward edge also needs to be udpated
				Edge matching_edge = residual.getEdge(e.nodes[1], e.nodes[0]);
				matching_edge.weight += bottleneck;
			}
		}
	}

	/* the bottleneck is the minimum amount of residual capacity along the edges of a path
	 * this method computes the bottleneck using the original graph g to get fixed capacities
	 */
	private static int getBottleneck(WGraph g, ArrayList<Edge> path) {
		int capacity = g.getEdge(path.get(0).nodes[0], path.get(0).nodes[1]).weight;
		int flow = path.get(0).weight;
		int bottleneck = Integer.MAX_VALUE;
		for (Edge e : path) {
			flow = e.weight;
			int residual;
			if (g.getEdge(e.nodes[0], e.nodes[1]) != null) { // forward edge
				residual = flow;
			} 
			else { // backward edge
				capacity = g.getEdge(e.nodes[1], e.nodes[0]).weight;
				residual = capacity - flow;
			}
			if (residual < bottleneck) {
				bottleneck = residual;
			}
		}
		return bottleneck;
	}
	
	/* method to initialize the residual graph */
	private static void makeResidual(WGraph original, WGraph modifiable_graph) {
		for (Edge e : original.getEdges()) {
			Edge modifiable_edge = modifiable_graph.getEdge(e.nodes[0], e.nodes[1]);
			int capacity = e.weight;
			int flow = modifiable_edge.weight;
			if (flow < capacity) {
				modifiable_graph.setEdge(modifiable_edge.nodes[0], modifiable_edge.nodes[1], capacity - flow);
			}
			if (flow >= 0) {
				Edge backward_edge = new Edge(modifiable_edge.nodes[1], modifiable_edge.nodes[0], flow);
				modifiable_graph.addEdge(backward_edge);
			}
		}
	}
}

