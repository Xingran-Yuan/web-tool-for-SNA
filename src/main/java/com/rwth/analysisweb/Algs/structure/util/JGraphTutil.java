package com.rwth.analysisweb.Algs.structure.util;
import org.jgrapht.*;
import com.rwth.analysisweb.Algs.structure.alg.FloydWarshallAllShortestPath;
import java.util.*;

import org.jgrapht.alg.shortestpath.FloydWarshallShortestPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultUndirectedGraph;

public class JGraphTutil {


    public static <V, E> double[] averagePathLength(Graph<V, E> graph) {
        double sum = 0.0;
        double max=0.0;
        double[] res=new double[2];
        FloydWarshallShortestPaths paths=new FloydWarshallShortestPaths(graph);
        int n = graph.vertexSet().size();
        for (V v : graph.vertexSet()) {
            for (V u : graph.vertexSet()) {
                if (v == u)
                    continue;
                double p = paths.getPathWeight(v,u);
                if (Double.isInfinite(p))
                    continue;
                sum += p;
                max=max>p?max:p;
            }
        }

        res[0]=sum / ((graph.vertexSet().size()-1)*graph.vertexSet().size());
        res[1]=max;
        return res;
    }

    public static <V, E> boolean isDirected(Graph<V, E> graph) {
        if (graph instanceof DefaultDirectedGraph)
            return true;
        else
            return false;
    }

    public static <V, E> int maxEdges(Graph<V, E> graph) {
        int vs = graph.vertexSet().size();

        if (graph instanceof DefaultUndirectedGraph) {
            return vs * (vs - 1) / 2;
        }
        else if (graph instanceof DefaultDirectedGraph) {
            return vs * (vs - 1);
        }
        else {
            throw new RuntimeException("Unknown graph type");
        }
    }


    public static <V, E> void alphacut(DefaultDirectedWeightedGraph<V, E> g, double alphaCut, Graph<V, E> alphaCutGraph) {

        if (!(alphaCutGraph instanceof DefaultDirectedWeightedGraph)) {
            throw new IllegalArgumentException("alphaCutGraph must be weighted");
        }

        // clone the graph
        Graphs.addGraph(alphaCutGraph, g);

        // remove edges with a weight less than the cut
        Iterator<E> edgeIterator = alphaCutGraph.edgeSet().iterator();
        while (edgeIterator.hasNext()) {
            E e = edgeIterator.next();
            double w = alphaCutGraph.getEdgeWeight(e);
            if (w < alphaCut)
                edgeIterator.remove();
        }
    }


    public static <V, E> double density(Graph<V, E> graph) {
        int es = graph.edgeSet().size();
        return (double) es / maxEdges(graph);
    }


    public static <V, E> ArrayList<V> diameterVertices(Graph<V, E> graph) {
        FloydWarshallAllShortestPath<V, E> fwsp = new FloydWarshallAllShortestPath(graph);
        return diameterVertices(graph, fwsp);
    }

    public static <V, E> ArrayList<V> diameterVertices(Graph<V, E> graph, FloydWarshallAllShortestPath<V, E> fwsp) {

        List<V> V = new ArrayList<V>(graph.vertexSet());

        double diameter = fwsp.getDiameter();

        double max = 0.0;

        ArrayList<V> res=new ArrayList<>();
        //VertexPair<V> res = null;
        for (int i = 0; i < V.size(); i++) {
            V v_i = V.get(i);
            for (int j = i + 1; j < V.size(); j++) {
                V v_j = V.get(j);

                double p = fwsp.shortestDistance(v_i, v_j);
                // System.out.println("sp(" + v_i + ", " + v_j + ") = " + p);
                if (p != Double.POSITIVE_INFINITY && p > max) {
                    res.add(v_i);
                    res.add(v_j);
                    max = p;
                }
            }
        }
        return res;
    }
    public static <V, E> double effectivediameter(Graph<V, E> graph, FloydWarshallAllShortestPath<V, E> fwsp) {

        List<V> V = new ArrayList<V>(graph.vertexSet());

        // find "true" diameter.
        List<Double> diameters = new ArrayList<Double>();
        for (V v : V) {
            for (V u : V) {
                if (v == u)
                    continue;
                double p = fwsp.shortestDistance(v, u);
                if (!Double.isInfinite(p))
                    diameters.add(p);
            }
        }

        // sort the diameters
        Collections.sort(diameters);

        // cut away the first 0.9
        List<Double> effective = diameters.subList((int) (0.9 * diameters.size()), diameters.size() - 1);

        // grab the 0.9 diameter
        return effective.get(0);
    }

    public static <V, E> AdjacencyMatrix<V> adjacencyMatrix(Graph<V, E> graph) {
        List<V> V = new ArrayList<V>(graph.vertexSet());
        int n = V.size();
        // build map (faster lookup)
        Map<V, Integer> vMap = new HashMap<V, Integer>();
        for (int i = 0; i < n; i++)
            vMap.put(V.get(i), i);

        double[][] A = new double[n][n];

        boolean undirected = !isDirected(graph);

        for (E e : graph.edgeSet()) {
            V src = graph.getEdgeSource(e);
            V dst = graph.getEdgeTarget(e);
            double w = graph.getEdgeWeight(e);

            A[vMap.get(src)][vMap.get(dst)] = w;
            if (undirected) {
                A[vMap.get(dst)][vMap.get(src)] = w;
            }
        }

        AdjacencyMatrix<V> r = new AdjacencyMatrix<V>();
        r.A = A;
        r.M = vMap;
        r.V = V;
        r.N = n;

        return r;
    }

    public static class AdjacencyMatrix<V> {
        public Map<V, Integer> M;
        public List<V> V;
        public double[][] A;
        public int N;
    }
}
