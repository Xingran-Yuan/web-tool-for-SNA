package com.rwth.analysisweb.Algs.structure.SNA;

import com.rwth.analysisweb.Algs.structure.util.GraphBuilder;
import com.rwth.analysisweb.Algs.structure.util.GraphBuilder.NewWeightedEdge;
import java.util.ArrayList;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;

public class CoauthorshipNetwork extends ComplexNetwork{
    private ArrayList<NewWeightedEdge<String>> edges = new ArrayList();

    public CoauthorshipNetwork(ArrayList<NewWeightedEdge<String>> e) {
        this.isDirected = false;
        this.edges = e;
        this.graph = this.BuildGraph();
    }

    protected Graph<String, DefaultEdge> BuildGraph() {
        Graph<String, NewWeightedEdge<String>> g = new DefaultUndirectedGraph(DefaultEdge.class);
        this.graphBuilder = new GraphBuilder(g);
        this.graphBuilder.addWeightedEdges(this.edges);
        return this.graphBuilder.graph();
    }


}
