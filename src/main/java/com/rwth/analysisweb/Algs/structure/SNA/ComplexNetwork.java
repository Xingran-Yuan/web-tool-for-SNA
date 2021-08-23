package com.rwth.analysisweb.Algs.structure.SNA;

import com.rwth.analysisweb.Algs.structure.alg.*;
import com.rwth.analysisweb.Algs.structure.util.GraphBuilder;
import com.rwth.analysisweb.Algs.structure.util.JGraphTutil;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.BetweennessCentrality;
import org.jgrapht.graph.DefaultEdge;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

;

public abstract class ComplexNetwork {
    protected GraphBuilder graphBuilder;
    public Graph<String, DefaultEdge> graph;
    public boolean isDirected;
    private int sizeOfEdges;
    private int sizeOfVertices;
    protected double maximumBetweenness;
    private double[] averageAnddiameter;


    public ComplexNetwork() {
    }

    protected abstract Graph<String, DefaultEdge> BuildGraph();

    public double AveragePathLength() {
        BigDecimal bd=new BigDecimal(averageAnddiameter[0]);
        return bd.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double GlobalClusteringCoefficient() {
        ClusteringCoefficient cluster = new ClusteringCoefficient(this.graph);
        BigDecimal bd=new BigDecimal(ClusteringCoefficient.globalClusteringCoefficient(cluster.calculate()));
        return bd.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double Diameter() {
        averageAnddiameter=JGraphTutil.averagePathLength(graph);
        BigDecimal bd=new BigDecimal(averageAnddiameter[1]);
        return bd.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public double MaximumBetweenness() {
        BetweennessCentrality betweennessCentrality=new BetweennessCentrality(graph,true);
        Map<Long,Double> map=betweennessCentrality.getScores();
        Collection<Double> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        double res=Double.parseDouble(obj[obj.length-1].toString());
        BigDecimal bd=new BigDecimal(res);
        return bd.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    public double LargestConnectedComponent() {
        LargestConnectedComponent lcc = new LargestConnectedComponent(this.graph);
        BigDecimal bd=new BigDecimal(lcc.LargestComponent());
        return bd.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue();

    }

    public CentralityResult<String> DegreeCentrality() {
        DegreeCentrality degree = new DegreeCentrality(this.graph);
        return degree.calculate();
    }

    public CentralityResult<String> BetwennnessCentrality() {
        BrandesBetweennessCentrality betweenness = new BrandesBetweennessCentrality(this.graph, this.isDirected);
        this.maximumBetweenness = betweenness.getMaximum();
        return betweenness.getScores();
    }


    public void print() {
        BrandesBetweennessCentrality max = new BrandesBetweennessCentrality(this.graph, true);
        max.printResult();
    }

    public int getSizeOfEdges(){
        return graph.edgeSet().size();
    }

    public int getSizeOfVertices(){
        return graph.vertexSet().size();
    }

    public Set<String> nodesSet(){
        return graph.vertexSet();
    }
}
