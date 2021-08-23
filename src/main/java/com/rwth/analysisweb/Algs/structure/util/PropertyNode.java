package com.rwth.analysisweb.Algs.structure.util;
//import org.jgrapht.VertexFactory;
import java.util.HashMap;
import java.util.Map;
public class PropertyNode {
    public static PropertyNodeContinousFactory FACTORY() {
        return new PropertyNodeContinousFactory();
    }

    public static class PropertyNodeContinousFactory  {
        private long n;

        public PropertyNodeContinousFactory() {
            this(1);
        }

        public PropertyNodeContinousFactory(long start) {
            this.n = start;
        }

        public PropertyNode createVertex() {
            return new PropertyNode(n++);
        }
    }

    private Map<String, Object> attr = new HashMap<String, Object>();
    private Long id;

    public PropertyNode(long id) {
        this.id = id;
    }

    public <T> T getProperty(Class<T> clazz, String key) {
        return (T) attr.get(key);
    }

    public <T> void setProperty(String key, T property) {
        attr.put(key, property);
    }

    public Iterable<String> propertyNames() {
        return attr.keySet();
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        else if (!(obj instanceof PropertyNode))
            return false;
        else {
            PropertyNode that = (PropertyNode) obj;
            return this.id.equals(that.id);
        }
    }

    public int hashCode() {
        return this.id.hashCode() ^ 414213;
    }
}
