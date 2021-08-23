package com.rwth.analysisweb.Algs.structure.alg;
import org.jgrapht.Graph;
import java.util.*;

import org.jgrapht.alg.scoring.BetweennessCentrality;

public class BrandesBetweennessCentrality <V, E> implements CentralityMeasure {
    private boolean isDirected;
    private boolean isWeighted;
    private double maximum=0.0;
    Map<V, Double> CB = new HashMap<V, Double>();
    private CentralityResult<V> results;


    private static <V> Map<V, List<V>> createP(Set<V> nodes) {
        Map<V, List<V>> P = new HashMap<V, List<V>>();
        for (V v : nodes) {
            P.put(v, new ArrayList<V>());
        }
        return P;
    }

    private static <V> Map<V, Double> initIntMap(Collection<V> nodes, V s, double defval, double selval) {
        Map<V, Double> m = new HashMap<V, Double>();
        for (V v : nodes) {
            m.put(v, v.equals(s) ? selval : defval);
        }
        return m;
    }

    private Graph<V, E> graph;

    public BrandesBetweennessCentrality(Graph<V, E> graph,boolean directed) {
        this.graph = graph;
        this.isDirected=directed;
        calScore();
    }

    public CentralityResult<V> calculate() {

            Set<V> V = graph.vertexSet();


            Map<V, Double> CB = new HashMap<V, Double>();
            for (V v : V)
                CB.put(v, 0.0);

            for (V s : V) {

                Stack<V> S = new Stack<V>();
                Map<V, List<V>> P = createP(V);
                //num shortest paths from our vertex to v
                Map<V, Double> rho = initIntMap(V, s, 0.0, 1.0);
                //distance of paths from s to v
                Map<V, Double> d = initIntMap(V, s, -1.0, 0.0);

                Queue<V> Q = new LinkedList<V>();

                Q.add(s);

                while (!Q.isEmpty()) {

                    V v = Q.poll();
                    S.push(v);

                    for (E edge : graph.edgesOf(v)) {
                        //w:neighbor of v
                        V w = graph.getEdgeSource(edge);
                        if (w.equals(v)) {
                            w = graph.getEdgeTarget(edge);
                        }

                        // System.out.println("v neighbour w: " + v + ", " + w + " d[w] = " + d.get(w));

                        // w found for the first time?
                        if (d.get(w) < 0) {
                            Q.add(w);
                            d.put(w, d.get(v) + graph.getEdgeWeight(edge));
                        }
                        // shortest path to w via v?
                        if (d.get(w) == d.get(v) + graph.getEdgeWeight(edge)) {
                            rho.put(w, rho.get(w) + rho.get(v));
                            P.get(w).add(v);
                        }
                    }
                }

                Map<V, Double> delta = new HashMap<V, Double>();
                for (V v : V)
                    delta.put(v, 0.0);

                while (!S.isEmpty()) {
                    V w = S.pop();
                    for (V v : P.get(w)) {
                        double tmp = delta.get(v) + (((double) rho.get(v) / rho.get(w)) * (1 + delta.get(w)));

                        delta.put(v, tmp);
                    }

                    if (!w.equals(s)) {
                        CB.put(w, CB.get(w) + delta.get(w));
                    }
                }
            }

            // normalize

		double H = (V.size() - 1)*(V.size() - 2)/2;
		for (V n : CB.keySet()) {
			CB.put(n, CB.get(n) / H);
		}


		CentralityResult<V> r = new CentralityResult<V>(CB, true);

		//CentralityResult<V> r = new CentralityResult<V>(FuzzyUtil.minMaxNormalize(CB), true);
        return r;
        }

    public void calScore(){
        BetweennessCentrality betweennessCentrality=new BetweennessCentrality(graph);
        Map<V,Double> score = new HashMap<>();
        score=betweennessCentrality.getScores();
        double H = (graph.vertexSet().size() - 1)*(graph.vertexSet().size() - 2);
        if(isDirected) H=H/2;
        for (Map.Entry<V, Double> e : score.entrySet()) {
            double val=e.getValue()/H;
            score.put(e.getKey(),val);
            maximum=maximum>val?maximum:val;
        }
        results= new CentralityResult<V>(score, true);
    }
     public Double getMaximum(){
        return maximum;
     }

     public CentralityResult<V> getScores(){
        return results;
     }



    public double ScoreOfVertex(V v){
        BetweennessCentrality betweennessCentrality=new BetweennessCentrality(graph);
        Double score=betweennessCentrality.getVertexScore(v);
        double H = (graph.vertexSet().size() - 1)*(graph.vertexSet().size() - 2);
        if(isDirected) H=H/2;
        score=score/H;
        return score;
    }


    public void printResult(){
        CentralityResult<V> r=calculate();
        for (Map.Entry<V, Double> e :r.getRaw().entrySet()) {
            System.out.println(e.getValue());
        }

    }

}
