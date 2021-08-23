package com.rwth.analysisweb.Entity.TopicEntity;

import java.util.ArrayList;
import java.util.List;

public class DynamicTopicEntity {
    List<TopicEntity> topics=new ArrayList<>();
    double relevance;
    int topicID;

    public List<TopicEntity> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicEntity> topics) {
        this.topics = topics;
    }

    public double getRelevance() {
        return relevance;
    }

    public void setRelevance(double relevance) {
        this.relevance = relevance;
    }

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public DynamicTopicEntity(int topicID) {
        this.topicID = topicID;
    }
}
