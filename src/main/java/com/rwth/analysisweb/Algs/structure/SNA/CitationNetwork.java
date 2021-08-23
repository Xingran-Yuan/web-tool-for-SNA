package com.rwth.analysisweb.Algs.structure.SNA;

import com.rwth.analysisweb.Algs.structure.util.GraphBuilder;
import java.util.ArrayList;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class CitationNetwork extends ComplexNetwork {
    private ArrayList<String> nodePair = new ArrayList();

    public CitationNetwork(ArrayList<String> nodes) {
        this.isDirected = true;
        this.nodePair = nodes;
        this.graph = this.BuildGraph();

    }

    protected Graph<String, DefaultEdge> BuildGraph() {
        Graph<String, DefaultEdge> g = new DefaultDirectedGraph(DefaultEdge.class);
        this.graphBuilder = new GraphBuilder(g);
        this.graphBuilder.addVertices(this.nodePair);
        this.graphBuilder.addEdges(this.nodePair);
        return this.graphBuilder.graph();

    }


}