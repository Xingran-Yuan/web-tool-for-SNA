package com.rwth.analysisweb.Algs.structure.util;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class FuzzyUtil {
    public static interface NormalizedHandler<V> {
        public void normalized(V element, double value);
    }

    public static <V> void minMaxNormalize(Map<V, Double> map, NormalizedHandler<V> handler) {
        double min = Double.POSITIVE_INFINITY;
        double max = Double.NEGATIVE_INFINITY;
        for (Double d : map.values()) {
            if (d > max)
                max = d;
            if (d < min)
                min = d;
        }
        double max_min = max - min;
        if (Double.compare(max_min, 0.0) == 0)
            max_min = 1.0;

        for (Entry<V, Double> e : map.entrySet()) {
            handler.normalized(e.getKey(), (e.getValue() - min) / (max_min));
        }
    }

    public static <V> Map<V, Double> minMaxNormalize(Map<V, Double> map) {
        final Map<V, Double> res = new HashMap<V, Double>();
        minMaxNormalize(map, new NormalizedHandler<V>() {
            @Override
            public void normalized(V element, double value) {
                res.put(element, value);
            }
        });
        return res;
    }

    public static<V> Map<V, Double> betweennessNormalize(Map<V,Double> map,boolean isDirected){

        int size= map.size();
        int u=(size-1)*(size-2);
        if(!isDirected){
            u=u/2;
        }
        for (Entry<V, Double> e : map.entrySet()) {
            map.put(e.getKey(),e.getValue()/u);
        }
        return map;
    }
}
