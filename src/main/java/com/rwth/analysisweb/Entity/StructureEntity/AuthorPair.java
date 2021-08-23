package com.rwth.analysisweb.Entity.StructureEntity;




public class AuthorPair {
    private String node1;
    private String node2;
    private String paperId;
    private int paperYear;

    public String getNode1(){
        return node1;
    }

    public void setNode1(String node){
        this.node1=node;
    }

    public String getNode2(){
        return node2;
    }

    public void setNode2(String node){
        this.node2=node;
    }

    public String getPaperId(){
        return paperId;
    }

    public void setPaperId(String id){
        paperId=id;
    }

    public void setPaperYear(int i){
        paperYear=i;
    }

    public int getPaperYear(){
        return paperYear;
    }
}
