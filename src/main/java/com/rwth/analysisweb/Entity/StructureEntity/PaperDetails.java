package com.rwth.analysisweb.Entity.StructureEntity;


import java.util.Arrays;
import java.util.Objects;


public class PaperDetails {
    private Long id;
    private String paperId;
    private String paperTitle;
    private int paperYear;
    private Integer nCitation;
    private String paperDoi;
    private String language;
    private byte[] abstraction;
    private byte[] paperAbstraction;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }


    public String getPaperTitle() {
        return paperTitle;
    }

    public void setPaperTitle(String paperTitle) {
        this.paperTitle = paperTitle;
    }


    public int getPaperYear() {
        return paperYear;
    }

    public void setPaperYear(int paperYear) {
        this.paperYear = paperYear;
    }


    public Integer getnCitation() {
        return nCitation;
    }

    public void setnCitation(Integer nCitation) {
        this.nCitation = nCitation;
    }


    public String getPaperDoi() {
        return paperDoi;
    }

    public void setPaperDoi(String paperDoi) {
        this.paperDoi = paperDoi;
    }


    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


    public byte[] getAbstraction() {
        return abstraction;
    }

    public void setAbstraction(byte[] abstraction) {
        this.abstraction = abstraction;
    }


    public byte[] getPaperAbstraction() {
        return paperAbstraction;
    }

    public void setPaperAbstraction(byte[] paperAbstraction) {
        this.paperAbstraction = paperAbstraction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaperDetails orgEntity = (PaperDetails) o;
        return paperYear == orgEntity.paperYear && Objects.equals(id, orgEntity.id) && Objects.equals(paperId, orgEntity.paperId) && Objects.equals(paperTitle, orgEntity.paperTitle) && Objects.equals(nCitation, orgEntity.nCitation) && Objects.equals(paperDoi, orgEntity.paperDoi) && Objects.equals(language, orgEntity.language) && Arrays.equals(abstraction, orgEntity.abstraction) && Arrays.equals(paperAbstraction, orgEntity.paperAbstraction);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, paperId, paperTitle, paperYear, nCitation, paperDoi, language);
        result = 31 * result + Arrays.hashCode(abstraction);
        result = 31 * result + Arrays.hashCode(paperAbstraction);
        return result;
    }
}
