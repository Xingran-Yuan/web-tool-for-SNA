package com.rwth.analysisweb.Algs.structure.util;
import org.jgrapht.Graph;

public class ToStringNodeFinder<V, K> implements NodeFinder<V,K> {
    @Override
    public V find(Graph<V, ?> graph, K key) {
        String keyStr = key.toString();
        for (V node : graph.vertexSet()) {
            String nodeStr = node.toString();
            if (nodeStr.equals(keyStr))
                return node;
        }
        return null;
    }
}
