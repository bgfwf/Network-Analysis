public class Edge implements Comparable<Edge> {
	private int v1;
	private int v2;
	private int length;
	private int bandwidth;
	private String material;
	final private double copperSpeed = 230000000.0;
	final private double opticalSpeed = 200000000.0;
	Edge(int v1, int v2, int length, int bandwidth, String material)
	{
		this.v1 = v1;
		this.v2 = v2;
		this.length = length;
		this.bandwidth = bandwidth;
		this.material = material;
	}
	
	public String getMaterial()
	{
		return this.material;
	}
	
	public int v1()
	{
		return this.v1;
	}
	
	public int v2()
	{
		return this.v2;
	}
	
	public double latency()
	{
		if (this.material.equals("copper"))
			return (this.length/copperSpeed);
		else
			return (this.length/opticalSpeed);
	}
	
	public int compareTo(Edge e)
	{
		if (this.latency() < e.latency())
			return 1;
		if (this.latency() < e.latency())
			return -1;
		else
			return 0;
	}
	
	public int bandwidth()
	{
		return this.bandwidth;
	}
	
	/**
     * Returns either endpoint of this edge.
     *
     * @return either endpoint of this edge
     */
    public int either() {
        return v1;
    }

    /**
     * Returns the endpoint of this edge that is different from the given vertex.
     *
     * @param  vertex one endpoint of this edge
     * @return the other endpoint of this edge
     * @throws IllegalArgumentException if the vertex is not one of the
     *         endpoints of this edge
     */
    public int other(int vertex) {
        if      (vertex == v1) return v2;
        else if (vertex == v2) return v1;
        else throw new IllegalArgumentException("Illegal endpoint");
    }
}