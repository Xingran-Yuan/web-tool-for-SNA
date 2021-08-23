package com.rwth.analysisweb.Service.TopicService.Interface;

import com.rwth.analysisweb.Dao.StructureDao.OrgUtilMapper;
import com.rwth.analysisweb.Dao.TopicDao.TimesliceMapper;
import com.rwth.analysisweb.Dao.TopicDao.TopicDistributionMapper;
import com.rwth.analysisweb.Dao.TopicDao.WordMapper;
import com.rwth.analysisweb.Entity.StructureEntity.PaperDetails;
import com.rwth.analysisweb.Entity.StructureEntity.PapersShow;
import com.rwth.analysisweb.Entity.TopicEntity.*;
import com.rwth.analysisweb.Service.TopicService.Interface.PaperService;
import com.rwth.analysisweb.Service.TopicService.Interface.TopicService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TopicServiceImpl implements TopicService, PaperService {
    private String org;
    private List<TimesliceEntity> timeslices;
    private List<DynamicTopicEntity> topics;


    @Autowired
    WordMapper wordMapper;

    @Autowired
    TimesliceMapper timesliceMapper;

    @Autowired
    TopicDistributionMapper topicDistributionMapper;

    @Autowired
    OrgUtilMapper orgUtilMapper;

    @Override
    public void setOrg(String o){
        this.org=o;
        this.timeslices=timesliceMapper.getWords(o);

    }

    @Override
    public TopicEntity getTopicIn(int timeId,int topicId,String org){
        TopicEntity topicEntity=new TopicEntity(topicId);
        topicEntity.setWords(wordMapper.getWords(org,timeId,topicId));
        System.out.println(timeslices.size());
        int year=timeslices.get(timeId).getYear();
        topicEntity.setYear(year);
        topicEntity.setRevelance(calculateRelevance(year,year+1,topicId));
        return topicEntity;
    }


    @Override
    public DynamicTopicEntity listTopic(int topicId,String org){
        List<TopicEntity> topics=new ArrayList<>();
        for(int timeId=0;timeId<timeslices.size();timeId++){
            topics.add(getTopicIn(timeId,topicId,org));
        }
        DynamicTopicEntity dynamicTopicEntity=new DynamicTopicEntity(topicId);
        dynamicTopicEntity.setTopics(topics);
        double revalance=calculateRelevance(timeslices.get(0).getYear(),timeslices.get(timeslices.size()-1).getYear(),topicId);
        dynamicTopicEntity.setRelevance(revalance);

        return dynamicTopicEntity;
    }

    @Override
    public List<DynamicTopicEntity> listTopics(String org){
        List<DynamicTopicEntity> res=new ArrayList<>();
        for(int i=0;i<20;i++){
            res.add(listTopic(i,org));
        }
        this.topics=res;
        return res;
    }

    private double calculateRelevance(int start,int end,int topicId){
        double revelance=0;
        List<TopicDistributionEntity> probabilities=topicDistributionMapper.getDistribution(org,start,end);
        for(int i=0;i<probabilities.size();i++){
            revelance+=probabilities.get(i).getTopic(topicId);
        }
        BigDecimal bd=new BigDecimal(revelance/probabilities.size());
        return bd.setScale(5,BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    @Override
    public List<TopicEntity> searchTopics(String word,String org){

        List<TopicEntity> res=new ArrayList<>();
        for(int i=0;i<20;i++){
            List<TopicEntity> t=topics.get(i).getTopics();
            for(TopicEntity topic:t){
                List<WordEntity> words=topic.getWords();
                for(WordEntity w:words){
                    if(w.getWord().contains(word)){
                        //System.out.println(w.getWord());
                        res.add(topic);
                    }
                }
            }
        }
        return res;
    }

    @Override
    public List<Map<String,Object>> findRelevantPapers(int year,int topicId, String org){
        List<TopicDistributionEntity> list=topicDistributionMapper.findPapersContainTopics(org,year,topicId);
        List<Map<String,Object>> data=new ArrayList<>();
        for(TopicDistributionEntity topicDistributionEntity:list){
            Map<String,Object> map=new HashMap<>();
            map.put("Paper_ID",topicDistributionEntity.getPaper_id());

            map.put("Probability",topicDistributionEntity.getTopic(topicId));
            PapersShow papersShow=orgUtilMapper.getPaperByID(org,topicDistributionEntity.getPaper_id());

            map.put("title",papersShow.getTitle());
            data.add(map);
        }
        return data;
    }

    @Override
    public Map<String,Object> topicsOfPaper(String paperId,String org){
        List<TopicEntity> res=new ArrayList<>();
        Map<String,Object> data=new HashMap<>();
        TopicDistributionEntity topicsOfPaper=topicDistributionMapper.getTopicsOfPaper(org,paperId);
        int i=0;
        int id=timesliceMapper.getID(org,topicsOfPaper.getPaper_year());
        List<String> topics= new ArrayList<>();
        while(i<20){
            if(topicsOfPaper.getTopic(i)!=0){
                StringBuffer sb=new StringBuffer();
                List<WordEntity> wordEntities=wordMapper.getWords(org,id,i);
                for(int j=0;j<5;j++){
                    sb.append(wordEntities.get(j).getWord()+" ");
                }
                data.put(sb.toString(),topicsOfPaper.getTopic(i));
                topics.add(sb.toString());
            }
            i++;
        }
        data.put("topics",topics);
        return data;
    }

    @Override
    public List<TopicEntity> getTopicsIn(int timeId,String org){
        List<TopicEntity> topics=new ArrayList<>();
        for(int i=0;i<20;i++){
            topics.add(getTopicIn(timeId,i,org));
        }
        return topics;
    }

    @Override
    public List<TimesliceEntity> returnYears(String org){
        return timeslices;
    }






}
