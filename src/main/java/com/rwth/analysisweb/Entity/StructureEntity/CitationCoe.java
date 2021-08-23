package com.rwth.analysisweb.Entity.StructureEntity;

public class CitationCoe {
    private String org;
    private String period;
    private int vertices;
    private int edges;
    private double clustering;
    private double maximum;
    private double largest;
    private int diameter;
    private double average;

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getVertices() {
        return vertices;
    }

    public void setVertices(int vertices) {
        this.vertices = vertices;
    }

    public int getEdges() {
        return edges;
    }

    public void setEdges(int edges) {
        this.edges = edges;
    }

    public double getClustering() {
        return clustering;
    }

    public void setClustering(double clustering) {
        this.clustering = clustering;
    }

    public double getMaximum() {
        return maximum;
    }

    public void setMaximum(double maximum) {
        this.maximum = maximum;
    }

    public double getLargest() {
        return largest;
    }

    public void setLargest(double largest) {
        this.largest = largest;
    }

    public int getDiameter() {
        return diameter;
    }

    public void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    public double getAverage() {
        return average;
    }

    public void setAverage(double average) {
        this.average = average;
    }
}
