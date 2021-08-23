package com.rwth.analysisweb.Algs.structure.util;

public interface PropertyContainer {
    public <T> T getProperty(Class<T> clazz, String key);
    public <T> void setProperty(String key, T property);
    public Iterable<String> propertyNames();
}
