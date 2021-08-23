package com.rwth.analysisweb.Entity.TopicEntity;

public class WordEntity {
    private String word;
    private double prob;

    public String getWord(){
        return word;
    }

    public double getProb(){
        return prob;
    }

    public void setWord(String w){
        this.word=w;
    }

    public void setProb(double p){
        this.prob=p;
    }


}
