public class Prims {
    private Edge[] edgeTo; //Shortest edge from tree vertex to non-tree vertex
    private double[] distTo;      // distTo[v] = weight of shortest such edge
    private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
    private IndexMinPQ<Double> pq;

    /**
     * Compute a minimum spanning tree (or forest) of an edge-weighted graph.
     * @param G the edge-weighted graph
     */
    public Prims(NetworkGraph G) {
        edgeTo = new Edge[G.V()];
        distTo = new double[G.V()];
        marked = new boolean[G.V()];
        pq = new IndexMinPQ<Double>(G.V());
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;

        for (int v = 0; v < G.V(); v++)      // run from each vertex to find
            if (!marked[v]) prim(G, v);      // minimum spanning forest
    }

    // run Prim's algorithm in graph G, starting from vertex s
    private void prim(NetworkGraph G, int s) {
        distTo[s] = 0.0;
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            scan(G, v);
        }
    }

    // scan vertex v
    private void scan(NetworkGraph G, int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            int w = e.other(v);
            if (marked[w]) continue;         // v-w is obsolete edge
            if (e.latency() < distTo[w]) {
                distTo[w] = e.latency();
                edgeTo[w] = e;
                if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
                else                pq.insert(w, distTo[w]);
            }
        }
    }

    public String toString() {
        String s = "";
        for(int i = 0; i < edgeTo.length; i++) {
            Edge e = edgeTo[i];
            if(e == null)
            continue;
            int v1 = e.either();
            int v2 = e.other(v1);
            int via = v1;
            if(v1 == i) {
                via = v2;
            }
            double dist = distTo[i];
            s += "Best way to " + i + " is via " + via + " with a latency of " + dist + ".\n";
        }
        return s;
    }

    public static void main(String[] args) {
        NetworkGraph graph = new NetworkGraph(5);
        graph.addEdge(new Edge(0, 2, 10, 10000, "optical"));
        graph.addEdge(new Edge(0, 3, 10, 10000, "optical"));
        graph.addEdge(new Edge(1, 2, 10, 10000, "optical"));
        graph.addEdge(new Edge(1, 3, 10, 10000, "optical"));
        graph.addEdge(new Edge(0, 4, 10, 100, "copper"));
        graph.addEdge(new Edge(1, 4, 10, 100, "copper"));
		graph.addEdge(new Edge(2, 4, 10, 100, "copper"));
        graph.addEdge(new Edge(3, 4, 10, 100, "copper"));
        Dijkstra p = new Dijkstra(graph, 0);
        System.out.println(p);
        System.out.println("------------");
        /*graph = new NetworkGraph(5);
        graph.addEdge(new Edge(0, 1, 1));
        graph.addEdge(new Edge(1, 2, 1));
        graph.addEdge(new Edge(2, 3, 1));
        graph.addEdge(new Edge(3, 4, 1));
        graph.addEdge(new Edge(0, 4, 2));
        p = new Prims(graph);
        System.out.println(p);*/
    }
}