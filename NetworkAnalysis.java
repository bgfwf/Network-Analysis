// i. Dijkstra's???
// ii. Connected using only copper cables??? *FIXED*
// iii. Prim's???
// iv. Pair of vertices break connectedness??? *FIXED*
// v. Quit program

// Write contains method for Bag.java???

/*OFFICE HOURS QUESTIONS ****
1. Dijkstra is broken, fix and find path for i.???
2. Iterate through every possible pair for v.???
3. Use BFS with contains for v.???
*/

import java.io.*;
import java.util.*;

public class NetworkAnalysis {
	public static void main (String[] args) throws Exception {
		//READ IN GRAPH
		File file = new File(args[0]);
		BufferedReader infile = new BufferedReader(new FileReader(file));
		int vertices = Integer.parseInt(infile.readLine());
		NetworkGraph network = new NetworkGraph(vertices);
		while (infile.ready())
		{
			String[] tokens = infile.readLine().split("\\s+");
			int v1 = Integer.parseInt(tokens[0]);
			//System.out.println(v1);
			int v2 = Integer.parseInt(tokens[1]);
			String material = tokens[2];
			int bandwidth = Integer.parseInt(tokens[3]);
			int length = Integer.parseInt(tokens[4]);
			Edge e = new Edge(v1, v2, length, bandwidth, material);
			network.addEdge(e);
		}
		//System.out.println(network);
		//System.out.println();
		//System.out.println(copperBFS(network, 0));
		//System.out.println();
		// Terminal-based driver
		Scanner input = new Scanner(System.in);
		while (true) {
			System.out.println();
			System.out.println("i. Find lowest latency path");
			System.out.println("ii. Is the graph still connected only using its copper cables?");
			System.out.println("iii. Find lowest average latency spanning tree");
			System.out.println("iv. Does there exist two vertices, that if removed, break the connectedness of the graph?");
			System.out.println("v. Quit");
			String option = input.next();
			input.nextLine();
			
			if (option.equals("v"))
				break;
			else if (option.equals("i"))	
			{
				System.out.println("Vertex 1: ");
				int v1 = Integer.parseInt(input.next());
				input.nextLine();
				System.out.println("Vertex 2: ");
				int v2 = Integer.parseInt(input.next());
				input.nextLine();
				Dijkstra lowestLatency = new Dijkstra(network, v1);
				int minBandwidth = Integer.MAX_VALUE;
				// Fix Dijkstra, path to v2 = lowestLatency path
				// Check each edge traversed in lowestLatency path, keep track of edge with lowest
				// bandwidth, return that bandwidth
				LinkedList<Edge> path = new LinkedList<Edge>();
				int via = v2;
				while (via != v1)
				{
					Edge e = lowestLatency.edgeTo[via];
					path.addFirst(e);
					int edgeV1 = e.either();
					int edgeV2 = e.other(edgeV1);
					via = edgeV1;
					if(edgeV1 == v2) {
					via = edgeV2;
					}
				}
				System.out.println();
				System.out.println("Lowest latency path from vertex " + v1 + " to vertex " + v2 + ":");
				for (Edge edge : path) {
					System.out.println("Edge between vertex " + edge.v1() + " and vertex " + edge.v2());
					if (edge.bandwidth() < minBandwidth)
						minBandwidth = edge.bandwidth();
				}
				// PRINT LOWEST LATENCY PATH HERE!!!
				double dist = lowestLatency.distTo[v2];
				//System.out.println("Best way to " + v2 + " is via " + via + " with a latency of " + dist);
				//System.out.println(lowestLatency);
				System.out.println("Bandwidth: " + minBandwidth);
			}
			else if (option.equals("ii"))
			{
				if (connectedByCopper(network))
					System.out.println("The network remains connected only using copper cables");
				else
					System.out.println("The network is not fully connected with only copper cables");
			}				
			else if (option.equals("iii"))	
			{
				System.out.println("Lowest average latency spanning tree: ");
				Prims prims = new Prims(network);
				System.out.println(prims);
			}
			else if (option.equals("iv"))	
			{
				if (twoFailedVertices(network))
					System.out.println("There are two vertices that, if removed, will leave the network with a disconnection");
				else
					System.out.println("Removing any two vertices leaves the network connected");
			}
		}
	}
	
	public static boolean connectedByCopper(NetworkGraph g)
	{
		return copperBFS(g, 0) == g.V();
	}
	
	public static boolean twoFailedVertices(NetworkGraph g)
	{
	    for (int i = 0; i < g.V(); i++) {
			for (int j = i+1; j < g.V(); j++) {
				if (j != i) {
					if (removedVerticesBFS(g, 0, i, j) != g.V() - 2)
						return true;
				}
			}
		}
		return false;
	}
	
	
	
	public static int copperBFS(NetworkGraph g, int start) {
		int[] marked = new int[g.V()];
		int count = 0;
		for (int i = 0; i < g.V(); i++)
			marked[i] = 0;
		Queue<Integer> queue = new Queue<Integer>();
		queue.enqueue(start);
		marked[start] = 1;
		while (!queue.isEmpty())
		{
			int v = queue.dequeue();
			count++;
			for (Edge e : g.adj(v)) {
				int v1 = e.either();
				if (e.getMaterial().equals("optical"))
					continue;
				if (e.either() == v) {
					//System.out.println("!!!");
					if (marked[e.other(v1)] != 1) {
						//System.out.println("!!!");
						queue.enqueue(e.other(v1));
						marked[e.other(v1)] = 1;
					}
				}
				if (e.other(v1) == v) {
					//System.out.println("???");
					if (marked[e.either()] != 1) {
						//System.out.println("???");
						queue.enqueue(e.either());
						marked[e.either()] = 1;
					}
				}
			}
		}
		return count;
	}
	
	public static int removedVerticesBFS(NetworkGraph g, int start, int removedVertex1, int removedVertex2) {
		int[] marked = new int[g.V()];
		int count = 0;
		for (int i = 0; i < g.V(); i++)
			marked[i] = 0;
		Queue<Integer> queue = new Queue<Integer>();
		marked[removedVertex1] = 1;
		marked[removedVertex2] = 1;
		while (marked[start] == 1) {
			start += 1;
		}
		queue.enqueue(start);
		marked[start] = 1;
		while (!queue.isEmpty())
		{
			int v = queue.dequeue();
			count++;
			for (Edge e : g.adj(v)) {
				int v1 = e.either();
				if (e.either() == v) {
					//System.out.println("!!!");
					if (marked[e.other(v1)] != 1) {
						//System.out.println("!!!");
						queue.enqueue(e.other(v1));
						marked[e.other(v1)] = 1;
					}
				}
				if (e.other(v1) == v) {
					//System.out.println("???");
					if (marked[e.either()] != 1) {
						//System.out.println("???");
						queue.enqueue(e.either());
						marked[e.either()] = 1;
					}
				}
			}
		}
		return count;
	}
		
		
	/*public static int removedVerticesBFS(NetworkGraph g, int start, int removedVertex1, int removedVertex2) {
		int[] marked = new int[g.V()];
		int count = 0;
		for (int i = 0; i < g.V(); i++)
			marked[i] = 0;
		Queue<Integer> queue = new Queue<Integer>();
		queue.enqueue(start);
		marked[start] = 1;
		while (!queue.isEmpty())
		{
			int v = queue.dequeue();
			count++;
			for (Edge e : g.adj(v)) {
				int v1 = e.either();
				int v2 = e.other(v1);
				if (v1 == removedVertex1 || v1 == removedVertex2 || v2 == removedVertex1 || v2 == removedVertex2)
					continue;
				if (v1 == v) {
					if (marked[v] != 1) {
						queue.enqueue(v2);
						marked[v2] = 1;
					}					
				}
				if (v2 == v) {
					if (marked[v] != 1) {
						queue.enqueue(v1);
						marked[v1] = 1;
					}
				}
			}
		}
		return count;
	}*/
}