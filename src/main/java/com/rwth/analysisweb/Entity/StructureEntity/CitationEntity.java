package com.rwth.analysisweb.Entity.StructureEntity;





public class CitationEntity {
    private Long id;
    private int paperYear;
    private String paperId;
    private String reference;


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    public int getPaperYear() {
        return paperYear;
    }

    public void setPaperYear(int paperYear) {
        this.paperYear = paperYear;
    }


    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CitationEntity that = (CitationEntity) o;

        if (paperYear != that.paperYear) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (paperId != null ? !paperId.equals(that.paperId) : that.paperId != null) return false;
        if (reference != null ? !reference.equals(that.reference) : that.reference != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = paperYear;
        result = 31 * result + (paperId != null ? paperId.hashCode() : 0);
        result = 31 * result + (reference != null ? reference.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
