package com.rwth.analysisweb.Algs.structure.alg;

import org.jgrapht.Graph;

import java.util.HashMap;
import java.util.Map;


public class DegreeCentrality<V, E> implements CentralityMeasure<V> {

    Graph<V, E> graph;

    public DegreeCentrality(Graph<V, E> graph) {
        this.graph = graph;
    }

    public CentralityResult<V> calculate() {

        Map<V, Double> r = new HashMap<V, Double>();

        int n_1 = graph.vertexSet().size() - 1;
        for (V n : graph.vertexSet()) {
            double sum = 0.0;
            for (E e : graph.edgesOf(n)) {
                sum += graph.getEdgeWeight(e);
            }
            r.put(n, sum / n_1);
        }

        return new CentralityResult<V>(r, true);
    }

}
