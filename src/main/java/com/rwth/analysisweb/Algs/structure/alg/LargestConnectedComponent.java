package com.rwth.analysisweb.Algs.structure.alg;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;

import java.util.*;

public class LargestConnectedComponent<V,E> {
    private Graph<V,E> graph;
    private ConnectivityInspector conn;
    public LargestConnectedComponent(Graph g){
        this.graph=g;
        conn=new ConnectivityInspector(g);
    }

    public Set<V> connectedSetOfVertex(V v){
        return conn.connectedSetOf(v);
    }

    public Double LargestComponent(){
        List<Set<V>> list=conn.connectedSets();
        double max=0.0;
        for(Set s:list){
            max=max>s.size()?max:s.size();
        }
        max=max/graph.vertexSet().size();
        return max;
    }


}
