package com.rwth.analysisweb.Entity.StructureEntity;


import java.util.Objects;


public class AuthorsEntity {
    private int id;
    private String paperId;
    private String authorId;
    private String authorName;
    private String org;
    private int paperYear;

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }


    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }


    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }


    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }


    public int getPaperYear() {
        return paperYear;
    }

    public void setPaperYear(int paperYear) {
        this.paperYear = paperYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorsEntity that = (AuthorsEntity) o;
        return paperYear == that.paperYear && id==that.id&&Objects.equals(paperId, that.paperId) && Objects.equals(authorId, that.authorId) && Objects.equals(authorName, that.authorName) && Objects.equals(org, that.org);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paperId, authorId, id,authorName, org, paperYear);
    }
}
