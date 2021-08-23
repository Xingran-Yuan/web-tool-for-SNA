package com.rwth.analysisweb.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rwth.analysisweb.Dao.TopicDao.TimesliceMapper;
import com.rwth.analysisweb.Entity.StructureEntity.PapersShow;
import com.rwth.analysisweb.Entity.TopicEntity.DynamicTopicEntity;
import com.rwth.analysisweb.Entity.TopicEntity.TimesliceEntity;
import com.rwth.analysisweb.Entity.TopicEntity.TopicEntity;
import com.rwth.analysisweb.Entity.TopicEntity.WordEntity;
import com.rwth.analysisweb.Service.TopicService.Interface.PaperService;
import com.rwth.analysisweb.Service.TopicService.Interface.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TopicController {
    @Autowired
    TopicService topicService;

    @Autowired
    PaperService paperService;

    @Autowired
    TimesliceMapper timesliceMapper;

    private int year;



    @RequestMapping("/Topic")
    public String returnTopicsDistributionPage() {
        return "TopicsPages/topicsDistribution";
    }

    @RequestMapping("/yearsTopic")
    public String selectYears(HttpServletRequest request)throws Exception {
        year=Integer.valueOf(request.getParameter("year"));
        return "TopicsPages/topicsDistribution";
    }



    @RequestMapping("/topics")
    @ResponseBody
    public String listTopics() throws JsonProcessingException {
        topicService.setOrg(OrgUtil.org);
        ObjectMapper mapper = new ObjectMapper();
        int id=timesliceMapper.getID(OrgUtil.org,year);
        Map<String,Object> data=new HashMap<>();
        List<TopicEntity> topicEntities=topicService.getTopicsIn(id,OrgUtil.org);
        for(int i=1;i<topicEntities.size()+1;i++){
            List<WordEntity> wordEntities=topicEntities.get(i-1).getWords();
            StringBuffer sb=new StringBuffer();
            sb.append(i+": ");
            for(int j=0;j<Math.min(5,wordEntities.size());j++) {
                sb.append(wordEntities.get(j).getWord() + " ");
            }
            data.put("t"+i,sb.toString());
        }
        return mapper.writeValueAsString(data);
    }

    @RequestMapping("/getPapersContain")
    @ResponseBody
    public String getAllPapers(@RequestParam int page,@RequestParam int limit,int id) throws JsonProcessingException{
        ObjectMapper mapper=new ObjectMapper();
        Map<String,Object> map=new HashMap<>();
        List<Map<String,Object>> data=topicService.findRelevantPapers(year,id,OrgUtil.org);
        System.err.println("okk");
        int size=data.size();
        if(page*limit<=size){
            data=data.subList((page-1)*limit, page*limit);
        }else{
            data=data.subList((page-1)*limit, size);
        }
        map.put("code", 0);    // 成功的状态码，默认：0
        map.put("msg", "");
        map.put("count", size);//总结果数
        map.put("data", data);


        String str = mapper.writeValueAsString(map);
        return str;


    }

    @RequestMapping("/evolution")
    @ResponseBody
    public String evolutionData(String ids)throws JsonProcessingException{
        topicService.setOrg(OrgUtil.org);
        List<TimesliceEntity> timeslices=topicService.returnYears(OrgUtil.org);
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> data=new HashMap<>();

        String[] IDs = ids.split(",");
        List<Integer> years=new ArrayList<>();

        for(String s:IDs){
            List<Double> list=new ArrayList<>();
            int id=Integer.valueOf(s);
            for(int timeId=0;timeId<timeslices.size();timeId++){
                list.add(topicService.getTopicIn(timeId,id,OrgUtil.org).getRevelance());
            }
            data.put(id+"th",list);
        }
        for(TimesliceEntity timesliceEntity:timeslices){
            years.add(timesliceEntity.getYear());
        }
        data.put("years",years);
        return mapper.writeValueAsString(data);

    }

    @RequestMapping("/topicOfpaper")
    @ResponseBody
    public String getTopicsOfpaper(String paperId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object>data=paperService.topicsOfPaper(paperId,OrgUtil.org);


        return mapper.writeValueAsString(data);
    }

}
