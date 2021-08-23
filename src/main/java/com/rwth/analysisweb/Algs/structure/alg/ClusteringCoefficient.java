package com.rwth.analysisweb.Algs.structure.alg;
import com.rwth.analysisweb.Algs.structure.util.*;
import java.util.*;
import org.jgrapht.*;


public class ClusteringCoefficient<V, E> {

    /**
     * Calculates the global clustering coefficient given the output from the clustering-coefficient mesure.
     * @param clusteringCoefficients output from
     * @param <V> node type
     * @return global clustering coefficient
     */
    public static <V> double globalClusteringCoefficient(CentralityResult<V> clusteringCoefficients) {
        int n = clusteringCoefficients.getRaw().size();
        double C = 0.0;
        for (Double C_i : clusteringCoefficients.getRaw().values())
            C += C_i;
        return C / n;
    }

    private Graph<V, E> graph;

    public ClusteringCoefficient(Graph<V, E> graph) {
        this.graph = graph;
    }

    @SuppressWarnings("unused")
    public CentralityResult<V> calculate_FastUntested() {

        Map<V, Double> r = new HashMap<V, Double>();

        JGraphTutil.AdjacencyMatrix<V> A = JGraphTutil.adjacencyMatrix(graph);

        for (int i = 0; i < A.N; i++) {

            List<Integer> nbr = new ArrayList<Integer>();

            // find neighbours
            for (int j = 0; j < A.N; j++) {
                if (i == j)
                    continue;
                if (A.A[i][j] > 0.0)
                    nbr.add(j);
            }

            int[] nbr_i = new int[nbr.size()];
            for (int j = 0; j < nbr_i.length; j++)
                nbr_i[j] = nbr.get(j);

            int sum = 0;
            for (int p = 0; p < nbr_i.length; p++) {
                for (int q = 0; q < nbr_i.length; q++) {
                    if (p == q)
                        continue;
                    if (A.A[p][q] > 0.0)
                        sum++;
                }
            }

            double C_i = (2.0 * sum) / (nbr_i.length * (nbr_i.length - 1.0));

            r.put(A.V.get(i), C_i);
        }

        return new CentralityResult<V>(r, true);
    }

    public CentralityResult<V> calculate() {

        Map<V, Double> r = new HashMap<V, Double>();

        for (V n : graph.vertexSet()) {

            List<V> nbr = Graphs.neighborListOf(graph, n);
            int nbr_n = nbr.size();
            int sum_e_jk = 0;

            if (nbr.size() < 2) {
                r.put(n, 0.0);
                continue;
            }

            for (int i = 0; i < nbr_n; i++) {
                for (int j = i + 1; j < nbr_n; j++) {
                    if (graph.containsEdge(nbr.get(i), nbr.get(j)))
                        sum_e_jk++;
                }
            }

            double C_i = (2.0 * sum_e_jk) / (nbr_n * (nbr_n - 1.0));

            r.put(n, C_i);
        }

        return new CentralityResult<V>(r, true);
    }
}
