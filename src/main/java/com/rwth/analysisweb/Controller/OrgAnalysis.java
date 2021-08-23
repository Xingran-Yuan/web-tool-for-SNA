package com.rwth.analysisweb.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rwth.analysisweb.Service.StructureService.Interface.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller

public class OrgAnalysis {

    int from;
    int to;

    public List<Integer> timeslices;



    @Qualifier("AnalysisService")
    @Autowired
    NetworkService networkService;

    @Qualifier("AnalysisService")
    @Autowired
    CoAuthorService coAuthorService;

    @Qualifier("AnalysisService")
    @Autowired
    CitationService citationService;

    @Autowired
    OrgsAnalysisService orgsAnalysisService;

    @Autowired
    OrgUtilService orgUtilService;



    @RequestMapping("/CoAuthorshipSta")
    @ResponseBody
    public String getCoauthorStatistics() throws JsonProcessingException {
        orgsAnalysisService.chooseOrg(OrgUtil.org,from,to);
        ObjectMapper mapper = new ObjectMapper();
        orgsAnalysisService.buildCoAuthorNetwork(OrgUtil.org,from,to);
        Map<String,Object> data=orgsAnalysisService.getCoAuthorNet(OrgUtil.org);
        String str = mapper.writeValueAsString(data);
        return str;
    }

    @RequestMapping("/citationSta")
    @ResponseBody
    public String getCitationStatistics() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        orgsAnalysisService.buildCitationNetwork(OrgUtil.org,from,to);
        Map<String,Object> data= orgsAnalysisService.getCitationNet(OrgUtil.org);
        String str = mapper.writeValueAsString(data);
        return str;
    }


    @RequestMapping("/getYear")
    @ResponseBody
    public String getYear() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> data=new HashMap<>();
        orgUtilService.setOrg(OrgUtil.org);
        data.put("years",orgUtilService.getTime(OrgUtil.org));
        String str = mapper.writeValueAsString(data);
        return str;
    }


    @GetMapping(value = "/coAuthorShip")
    public String coauthorshipNetBuild(){
        return "structurePages/CoAuthorShipNetwork";
    }

    @GetMapping(value = "/coAuthorA")
    public String coauthorshipDevelopment(){
        return "structurePages/coAuthorCoe";
    }

    @GetMapping(value = "/citation")
    public String citationNetBuild(){
        return "structurePages/CitationNetwork";
    }


    @GetMapping(value = "/citationA")
    public String citationDevelopment(){
        return "structurePages/citationCoe";
    }

    @RequestMapping("/years")
    public String setTimeslices(HttpServletRequest request)throws Exception {
        from=Integer.valueOf(request.getParameter("from"));
        to=Integer.valueOf(request.getParameter("to"));
        return "structurePages/CoAuthorShipNetwork";
    }

    @RequestMapping("/yearsCitation")
    public String citationYears(HttpServletRequest request)throws Exception {
        from=Integer.valueOf(request.getParameter("from"));
        to=Integer.valueOf(request.getParameter("to"));
        return "structurePages/CitationNetwork";
    }

    @RequestMapping("/coAuthorcoe")
    @ResponseBody
    public String getParametersCoauthor() throws JsonProcessingException{
        System.err.println("========进来了？");

        ObjectMapper mapper = new ObjectMapper();

        Map<String,Object> map=new HashMap<>();


        List<Integer> nodes=new ArrayList<>();
        List<Integer> edges=new ArrayList<>();
        List<Double>clustering=new ArrayList<>();
        List<Double>betweenness=new ArrayList<>();
        List<Double>component=new ArrayList<>();
        List<Double>diameter=new ArrayList<>();
        List<Double>average=new ArrayList<>();


        System.err.println(nodes);


        int i=0;
        System.err.println(timeslices);
        if(timeslices!=null) {
            while (i < timeslices.size() - 1) {
                orgsAnalysisService.chooseOrg(OrgUtil.org, timeslices.get(i), timeslices.get(i + 1));
                if(orgsAnalysisService.buildCoAuthorNetwork(OrgUtil.org,timeslices.get(i),timeslices.get(i+1))==0) {
                    nodes.add(0);
                    edges.add(0);
                    clustering.add(0.0);
                    betweenness.add(0.0);
                    component.add(0.0);
                    diameter.add(0.0);
                    average.add(0.0);
                }
                else {
                    nodes.add(networkService.NodesCount(0));
                    edges.add(networkService.EdgesCount(0));
                    clustering.add(networkService.GlobalClusteringCoefficient(0));
                    betweenness.add(networkService.MaximumBetweenness(0));
                    component.add(networkService.LargestConnectedComponent(0));
                    diameter.add(networkService.Diameter(0));
                    average.add(networkService.AveragePathLength(0));
                }

                i += 2;
            }
        }


        List<String> time=new ArrayList<>();
        int j=0;
        while(j<timeslices.size()-1){
            time.add(timeslices.get(j)+"-"+timeslices.get(j+1)+"");
            j+=2;
        }



        map.put("years",time);
        map.put("nodes",nodes);
        map.put("edges",edges);
        map.put("clustering",clustering);
        map.put("betweenness",betweenness);
        map.put("component",component);
        map.put("diameter",diameter);
        map.put("average",average);

        String str = mapper.writeValueAsString(map);
        timeslices.clear();

        System.err.println(timeslices);


        System.err.println(nodes);
        return str;




    }

    @RequestMapping("/citationcoe")
    @ResponseBody
    public String getParametersCitation() throws JsonProcessingException{
        System.err.println("========进来了？");

        ObjectMapper mapper = new ObjectMapper();

        Map<String,Object> map=new HashMap<>();


        List<Integer> nodes=new ArrayList<>();
        List<Integer> edges=new ArrayList<>();
        List<Double>clustering=new ArrayList<>();
        List<Double>betweenness=new ArrayList<>();
        List<Double>component=new ArrayList<>();
        List<Double>diameter=new ArrayList<>();
        List<Double>average=new ArrayList<>();


        System.err.println(nodes);


        int i=0;
        System.err.println(timeslices);
        if(timeslices!=null) {
            while (i < timeslices.size() - 1) {
                orgsAnalysisService.chooseOrg(OrgUtil.org, timeslices.get(i), timeslices.get(i + 1));
                if(orgsAnalysisService.buildCitationNetwork(OrgUtil.org,timeslices.get(i),timeslices.get(i+1))==0) {
                    nodes.add(0);
                    edges.add(0);
                    clustering.add(0.0);
                    betweenness.add(0.0);
                    component.add(0.0);
                    diameter.add(0.0);
                    average.add(0.0);
                }
                else {
                    nodes.add(networkService.NodesCount(1));
                    edges.add(networkService.EdgesCount(1));
                    clustering.add(networkService.GlobalClusteringCoefficient(1));
                    betweenness.add(networkService.MaximumBetweenness(1));
                    component.add(networkService.LargestConnectedComponent(1));
                    diameter.add(networkService.Diameter(1));
                    average.add(networkService.AveragePathLength(1));
                }

                i += 2;
            }
        }


        List<String> time=new ArrayList<>();
        int j=0;
        while(j<timeslices.size()-1){
            time.add(timeslices.get(j)+"-"+timeslices.get(j+1)+"");
            j+=2;
        }



        map.put("years",time);
        map.put("nodes",nodes);
        map.put("edges",edges);
        map.put("clustering",clustering);
        map.put("betweenness",betweenness);
        map.put("component",component);
        map.put("diameter",diameter);
        map.put("average",average);

        String str = mapper.writeValueAsString(map);
        timeslices.clear();

        System.err.println(timeslices);


        System.err.println(nodes);
        return str;




    }


    @RequestMapping("/timeslices")
    public String getSlicesCoauthor(HttpServletRequest request)throws Exception {
        this.timeslices=new ArrayList<>();
        String p="t";
        for(int i=1;i<30;i++){
            String s=request.getParameter(p+i);
            if(s==null||s.equals("")) break;
            String temp = s.replaceAll("[^0-9\\s]", " ").trim();
            System.err.println(temp);
            String[] abs = temp.split(" ");

            this.timeslices.add(Integer.valueOf(abs[0]));
            this.timeslices.add(Integer.valueOf(abs[1]));
        }

        System.err.println(timeslices);

        return "structurePages/coAuthorCoe";
    }

    @RequestMapping("/timeslicesCitation")
    public String getSlicesCitation(HttpServletRequest request)throws Exception {
        this.timeslices=new ArrayList<>();
        String p="t";
        for(int i=1;i<30;i++){
            String s=request.getParameter(p+i);
            if(s==null||s.equals("")) break;
            String temp = s.replaceAll("[^0-9\\s]", " ").trim();
            System.err.println(temp);
            String[] abs = temp.split(" ");

            this.timeslices.add(Integer.valueOf(abs[0]));
            this.timeslices.add(Integer.valueOf(abs[1]));
        }

        System.err.println(timeslices);

        return "structurePages/citationCoe";
    }

}
