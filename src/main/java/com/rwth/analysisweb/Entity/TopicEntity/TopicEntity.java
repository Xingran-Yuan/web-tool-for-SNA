package com.rwth.analysisweb.Entity.TopicEntity;

import java.util.List;

public class TopicEntity {
    private int year;
    private int topicId;
    private List<WordEntity> words;
    private double revelance;

    public int getYear() {
        return year;
    }

    public int getTopicId() {
        return topicId;
    }

    public List<WordEntity> getWords() {
        return words;
    }

    public void setYear(int Year) {
        year = Year;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public void setWords(List<WordEntity> words) {
        this.words = words;
    }

    public double getRevelance() {
        return revelance;
    }

    public void setRevelance(double revelance) {
        this.revelance = revelance;
    }

    public TopicEntity(int topicId) {
        this.topicId = topicId;
    }
}