package com.rwth.analysisweb.Service.TopicService.Interface;

import com.rwth.analysisweb.Entity.StructureEntity.PapersShow;
import com.rwth.analysisweb.Entity.TopicEntity.DynamicTopicEntity;
import com.rwth.analysisweb.Entity.TopicEntity.TimesliceEntity;
import com.rwth.analysisweb.Entity.TopicEntity.TopicEntity;
import com.rwth.analysisweb.Entity.TopicEntity.WordEntity;

import java.util.List;
import java.util.Map;

public interface TopicService {

    void setOrg(String o);
    List<DynamicTopicEntity> listTopics(String org);
    DynamicTopicEntity listTopic(int topicId,String org);
    TopicEntity getTopicIn(int timeId,int topicId,String org);
    List<TopicEntity> searchTopics(String word,String org);
    List<Map<String,Object>> findRelevantPapers(int year,int topicId, String org);
    List<TopicEntity> getTopicsIn(int timeId,String org);
    List<TimesliceEntity> returnYears(String org);


}
