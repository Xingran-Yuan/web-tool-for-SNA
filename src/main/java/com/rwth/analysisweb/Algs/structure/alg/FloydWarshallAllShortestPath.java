package com.rwth.analysisweb.Algs.structure.alg;
import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.GraphWalk;

public class FloydWarshallAllShortestPath<V,E>{
    private Graph<V, E> graph;
    private boolean doBacktrace;
    private List<V> vertices;
    private int countShortestPaths = 0;
    private double diameter = 0.0;

    private double[][] d = null;
    private int[][] backtrace = null;
    private Map<ArrayList<V>, GraphPath<V,E>> paths = null;

    public FloydWarshallAllShortestPath(Graph<V, E> graph, boolean doBacktrace) {
        this.doBacktrace = doBacktrace;
        this.graph = graph;
        this.vertices = new ArrayList<V>(graph.vertexSet());
    }

    public FloydWarshallAllShortestPath(Graph<V, E> graph) {
        this(graph, true);
    }

    public Graph<V, E> getGraph() {
        return graph;
    }

    public int shortestPathsCount() {
        return countShortestPaths;
    }

    /**
     * Calculates all shortest paths.
     */
    public void lazyCalculate() {

        int n = vertices.size();

        // init the backtrace matrix
        if (doBacktrace) {
            backtrace = new int[n][n];
            for (int i = 0; i < n; i++)
                Arrays.fill(backtrace[i], -1);
        }

        // initialize matrix, 0
        d = new double[n][n];
        for (int i = 0; i < n; i++)
            Arrays.fill(d[i], Double.POSITIVE_INFINITY);

        // initialize matrix, 1
        for (int i = 0; i < n; i++) {
            d[i][i] = 0.0;
        }

        // initialize matrix, 2
        Set<E> edges = graph.edgeSet();
        for (E edge : edges) {
            V v1 = graph.getEdgeSource(edge);
            V v2 = graph.getEdgeTarget(edge);

            int v_1 = vertices.indexOf(v1);
            int v_2 = vertices.indexOf(v2);

            d[v_1][v_2] = graph.getEdgeWeight(edge);
            d[v_2][v_1] = d[v_1][v_2];
        }

        // run fw alg
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    double ik_kj = d[i][k] + d[k][j];
                    if (ik_kj < d[i][j]) {
                        d[i][j] = ik_kj;
                        d[j][i] = ik_kj;
                        if (doBacktrace) {
                            backtrace[i][j] = k;
                            backtrace[j][i] = k;
                        }
                        if (d[i][j] > diameter)
                            diameter = d[i][j];
                    }
                }
            }
        }
    }

    public double averagePathLength() {

        if (d == null)
            lazyCalculate();

        double sum = 0.0;
        double c = 0.0;

        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[i].length; j++) {
                if (!Double.isInfinite(d[i][j])) {
                    sum += d[i][j];
                    c += 1.0;
                }
            }
        }

        return sum / c;
    }

    /**
     * Get the length of a shortest path.
     * @param a
     * @param b
     * @return
     */
    public double shortestDistance(V a, V b) {

        // lazy
        if (d == null)
            lazyCalculate();

        return d[vertices.indexOf(a)][vertices.indexOf(b)];
    }

    public double getDiameter() {
        // lazy
        if (d == null)
            lazyCalculate();

        return diameter;
    }

    private void shortestPathRecur(List<E> edges, int v_a, int v_b) {
        int k = backtrace[v_a][v_b];
        if (k == -1) {
            E edge = graph.getEdge(vertices.get(v_a), vertices.get(v_b));
            if (edge != null)
                edges.add(edge);
        }
        else {
            shortestPathRecur(edges, v_a, k);
            shortestPathRecur(edges, k, v_b);
        }
    }

    /**
     * Get the shortest path between two vertices.
     *
     * Note: The paths are calculated using a recursive algorithm. It *will* give problems on paths longer than
     * the heap allows.
     *
     * @param a From vertice
     * @param b To vertice
     * @return the path
     */
    public GraphPath<V, E> shortestPath(V a, V b) {

        if (!doBacktrace)
            throw new IllegalArgumentException("Backtrace not enabled, cannot find shortest path.");

        if (d == null)
            lazyCalculatePaths();

        int v_a = vertices.indexOf(a);
        int v_b = vertices.indexOf(b);

        List<E> edges = new ArrayList<E>();
        shortestPathRecur(edges, v_a, v_b);

        // no path, return null
        if (edges.size() < 1)
            return null;

        GraphWalk<V, E> path = new GraphWalk<V, E>(graph, a, b, edges, edges.size());

        return path;
    }

    /**
     * Calculate the shortest paths (not done per default)
     * @return the number of shortest paths.
     */
    public int lazyCalculatePaths() {

        // already we have calculated it once.
        if (paths != null) {
            return countShortestPaths;
        }

        // we don't have shortest paths.. lazyCalculate it.
        if (d == null)
            lazyCalculate();

        Map<ArrayList<V>, GraphPath<V, E>> sps = new HashMap<ArrayList<V>, GraphPath<V, E>>();
        int n = vertices.size();

        countShortestPaths = 0;
        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                // don't count this.
                if (i == j)
                    continue;

                V v_i = vertices.get(i);
                V v_j = vertices.get(j);

                GraphPath<V, E> path = shortestPath(v_i, v_j);

                // we got a path
                if (path != null) {
                    ArrayList<V> VertexPair=new ArrayList<>();
                    VertexPair.add(v_i);
                    VertexPair.add(v_j);
                    sps.put(VertexPair, path);
                    countShortestPaths++;
                }
            }
        }

        this.paths = sps;

        return countShortestPaths;
    }

    /**
     * Get shortest paths from a vertex to all other vertices in the graph.
     * @param v the originating vertex
     * @return List of paths
     */
    public List<GraphPath<V, E>> getShortestPaths(V v) {
        if (v == null)
            return null;

        List<GraphPath<V, E>> found = new ArrayList<GraphPath<V, E>>();
        for (ArrayList<V> pair : paths.keySet()) {
            if (pair.contains(v)) {
                found.add(paths.get(pair));
            }
        }

        return found;
    }
}
