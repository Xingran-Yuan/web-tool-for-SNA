package com.rwth.analysisweb.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rwth.analysisweb.Entity.StructureEntity.PapersShow;
import com.rwth.analysisweb.Service.StructureService.Interface.OrgUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CoAuthorAnalysis {
    @Autowired
    OrgUtilService orgUtilService;

    private int[] timeslices = {1960, 1970, 1980, 1990, 2000, 2005, 2010, 2013, 2016, 2019};

    @RequestMapping("/coAuthorData")
    @ResponseBody
    public String getCoauthorData() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String,Object>> data=new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        for (int i = 1; i <= 10; i++) {
            Map<String,Object> author=new HashMap<>();
            for (int j = 0; j < timeslices.length-1; j++) {
                author.put("author",firstClomun(i));
                author.put(j+1+"",orgUtilService.PapersWithNauthors(OrgUtil.org, timeslices[j], timeslices[j + 1], i));
            }
            data.add(author);
        }
        map.put("code", 0);    // 成功的状态码，默认：0
        map.put("msg", "");
        map.put("count", data.size());//总结果数
        map.put("data", data);

        String str = mapper.writeValueAsString(map);
        return str;
    }

    @RequestMapping("/authorProductivity")
    @ResponseBody
    public String getAuthorProductivity(@RequestParam int page, @RequestParam int limit) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String,Object>> data=new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        for (int i = 1; i <= 30; i++) {
            Map<String,Object> author=new HashMap<>();
            for (int j = 0; j < timeslices.length-1; j++) {
                author.put("Articles",i);
                author.put(j+1+"",orgUtilService.AuthorsWithNpapers(OrgUtil.org, timeslices[j], timeslices[j + 1], i));
            }
            data.add(author);
        }
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

    @RequestMapping("/top10papers")
    @ResponseBody
    public String gettop10papers() throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        List<PapersShow> papers=orgUtilService.GetTopTenPapers(OrgUtil.org);
        List<Integer>citations=new ArrayList<>();
        List<String> names= new ArrayList<>();
        for(PapersShow paper:papers){
            citations.add(paper.getCitation());
            names.add(paper.getTitle());
        }
        map.put("citation",citations);
        map.put("name",names);

        return mapper.writeValueAsString(map);

    }
    @RequestMapping("/top10authors")
    @ResponseBody
    public String gettop10authors() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> authors = orgUtilService.GetTopTenAuthors(OrgUtil.org);
        List<Object> nums = new ArrayList<>();
        List<Object> names = new ArrayList<>();

        for (Map<String, Object> author : authors) {
            int i=0;
            for (Map.Entry<String, Object> entry : author.entrySet()) {
                if(i%2==0)names.add(entry.getValue());
                else nums.add(entry.getValue());
                i++;
            }

        }

        map.put("nums", nums);
        map.put("name", names);

        return mapper.writeValueAsString(map);
    }



    private String firstClomun(int i){
        switch(i){
            case 1: return "Single";
            case 2: return "Double";
            case 3: return "Triple";
            case 4: return "Quadruple";
            case 5: return "Quintuple";
            case 6: return "Sextuple";
            case 7: return "Septule";
            case 8: return "Octuple";
            case 9: return "Nonuple";
            case 10: return "Decuple";
        }
        return null;
 }

}

