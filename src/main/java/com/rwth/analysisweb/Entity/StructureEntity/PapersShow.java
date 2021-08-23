package com.rwth.analysisweb.Entity.StructureEntity;

public class PapersShow {
    private String title;
    private String id;
    private int year;
    private int citation;

    public int getCitation() {
        return citation;
    }

    public void setCitation(int citation) {
        this.citation = citation;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
