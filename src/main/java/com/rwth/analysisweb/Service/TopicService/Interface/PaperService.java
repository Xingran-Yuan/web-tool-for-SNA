package com.rwth.analysisweb.Service.TopicService.Interface;

import com.rwth.analysisweb.Entity.TopicEntity.TopicEntity;

import java.util.List;
import java.util.Map;

public interface PaperService {
    Map<String,Object> topicsOfPaper(String paperId, String org);
}
