package com.rwth.analysisweb.Algs.structure.alg;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.rwth.analysisweb.Algs.structure.util.MapSortingHelper;

public class CentralityResult<V> {
    private Map<V, Double> raw;
    private List<Entry<V, Double>> sorted = null;
    private List<V> sortedNodes = null;
    private boolean inverse;

    public CentralityResult(Map<V, Double> raw, boolean inverse) {
        this.inverse = inverse;
        this.raw = raw;
    }

    /**
     * Get result for a node
     * @param v the node
     * @return the result value
     */
    public Double get(V v) {
        return raw.get(v);
    }

    /**
     * Access to the map of results.
     * @return the map
     */
    public Map<V, Double> getRaw() {
        return raw;
    }

    /**
     * Get a sorted version of the entries and their values.
     * @return the entries, first is most central
     */
    public List<Entry<V, Double>> getSorted() {
        if (sorted == null) {
            sorted = MapSortingHelper.sortedListD(raw);
            if (!inverse)
                Collections.reverse(sorted);
        }
        return sorted;
    }

    /**
     * Get a sorted version of the nodes only.
     * @return the nodes, first is most central
     */
    public List<V> getSortedNodes() {
        if (sortedNodes == null) {
            sortedNodes = MapSortingHelper.stripValues(getSorted());
        }
        return sortedNodes;
    }

    /**
     * Inverse results are results where the first entry is the one with the highest centrality value.
     * @return if this is inverse or not
     */
    public boolean isInverse() {
        return inverse;
    }

    @Override
    public String toString() {
        return "[CentralityResult " + getSorted() + "]";
    }
}
