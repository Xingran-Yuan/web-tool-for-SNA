package com.rwth.analysisweb.Algs.structure.util;

import org.jgrapht.*;
public interface NodeFinder<V,K>{
    /**
     * Find a node by a key in a graph
     * @param graph The graph
     * @param key The key
     * @return The node, or <code>null</code> if no matching node was found.
     */
    public V find(Graph<V, ?> graph, K key);
}
