package com.rwth.analysisweb.Entity.StructureEntity;


import java.util.Objects;


public class CoAuthorEntity {
    private String paperId;
    private Integer paperYear;
    private String authorId1;
    private String authorId2;


    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }


    public Integer getPaperYear() {
        return paperYear;
    }

    public void setPaperYear(Integer paperYear) {
        this.paperYear = paperYear;
    }


    public String getAuthorId1() {
        return authorId1;
    }

    public void setAuthorId1(String authorId1) {
        this.authorId1 = authorId1;
    }


    public String getAuthorId2() {
        return authorId2;
    }

    public void setAuthorId2(String authorId2) {
        this.authorId2 = authorId2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoAuthorEntity that = (CoAuthorEntity) o;
        return Objects.equals(paperId, that.paperId) && Objects.equals(paperYear, that.paperYear) && Objects.equals(authorId1, that.authorId1) && Objects.equals(authorId2, that.authorId2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paperId, paperYear, authorId1, authorId2);
    }
}
