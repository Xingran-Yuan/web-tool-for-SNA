package com.rwth.analysisweb.Algs.structure.util;
import org.jgrapht.*;

import java.util.ArrayList;


public class GraphBuilder<V,E,K> {
    private Graph<V,E> graph;
    private NodeFinder<V, K> nodeFinder = new ToStringNodeFinder<V, K>();

    public GraphBuilder(Graph<V,E> graph){
        this.graph=graph;
    }

    public GraphBuilder<V, E, K> nodeFinder(NodeFinder<V, K> newNodeFinder) {
        this.nodeFinder = newNodeFinder;
        return this;
    }

    //add vertices
    public GraphBuilder<V, E, K> addVertices(ArrayList<V> nodes) {
        for (int i=0;i<nodes.size();i++) {
            graph.addVertex(nodes.get(i));
        }
        return this;
    }

    //add edges
    public GraphBuilder<V,E,K>addEdges(ArrayList<V> nodePairs){
        if (nodePairs.size() % 2 != 0)
            throw new IllegalArgumentException("Nodepairs must be pairs (uneven number of nodes given)");

        for (int i = 0; i < nodePairs.size(); i += 2) {
            V src = nodePairs.get(i);
            V dst = nodePairs.get(i+1);
            E edge = graph.addEdge(src, dst);
        }

        return this;
    }

    public GraphBuilder<V, E, K> addWeightedEdges(ArrayList<NewWeightedEdge> edges) {
        int i=0;
        for (NewWeightedEdge<V> e : edges) {
            graph.addVertex(e.src);
            graph.addVertex(e.dst);
            double weight = e.w;

            V src=e.src;
            V dst=e.dst;
            E edge=graph.addEdge(src,dst);
            /*if(weight!=1) {
                graph.setEdgeWeight(edge, weight);
            }*/
            i++;
        }
        return this;
    }

    public V node(K key) {
        // delegate to nodefinder.
        return nodeFinder.find(graph, key);
    }



    public Graph<V, E> graph() {
        return graph;
    }

    public static <V> NewWeightedEdge<V> WE(V src, V dst, double w) {
        return new NewWeightedEdge<V>(src, dst, w);
    }

    public static class NewWeightedEdge<V> {
        private V src;
        private V dst;
        private double w;

        public NewWeightedEdge(V src, V dst, double w) {
            this.src = src;
            this.dst = dst;
            this.w = w;
        }

        public double getW(){
            return w;
        }
        public V getSrc(){
            return src;
        }

        public V getDst(){
            return dst;
        }
    }


}
