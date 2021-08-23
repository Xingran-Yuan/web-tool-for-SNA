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
@RequestMapping("/Compare")
public class OrgCompare {
    String org1="Hannover";
    String org2="KIT";
    private int[] years={1960,1970,1980,1990,2000,2005,2010,2013,2016,2019};



    @Qualifier("CompareService")
    @Autowired
    NetworkService networkService;

    @Qualifier("CompareService")
    @Autowired
    CoAuthorService coAuthorService;

    @Qualifier("CompareService")
    @Autowired
    CitationService citationService;

    @Autowired
    OrgsCompareService orgsCompareService;

    @Autowired
    OrgUtilService orgUtilService;

    @RequestMapping("coAuthorStatistic")
    @ResponseBody
    public String coAuthorNetBuild() throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        orgsCompareService.chooseOrgs(org1,org2);
        orgsCompareService.buildCoAuthorNet();

        Map<String,Object> data=orgsCompareService.getCoAuthorNet(org1,org2);
        String str = mapper.writeValueAsString(data);
        return str;

    }

    @RequestMapping("/citationSta")
    @ResponseBody
    public String getCitationStatistics() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        orgsCompareService.chooseOrgs(org1,org2);
        orgsCompareService.buildCitationNet();
        Map<String,Object> data= orgsCompareService.getCitationNet(org1,org2);
        String str = mapper.writeValueAsString(data);
        return str;
    }

    @GetMapping("/coAuthorNet")
    public String getAuthorNet(){
        return "ComparePages/CoAuthorShipNetwork";
    }

    @GetMapping("/citationNet")
    public String getCitationNet(){
        return "ComparePages/CitationNetwork";
    }

    @RequestMapping("/org")
    public String chooseOrgs(HttpServletRequest request)throws Exception {
        org1=request.getParameter("org1");
        org2=request.getParameter("org2");
        return "structurePages/CoAuthorShipNetwork";
    }

    @RequestMapping("/orgCitation")
    public String chooseOrgsCitation(HttpServletRequest request)throws Exception {
        org1=request.getParameter("org1");
        org2=request.getParameter("org2");
        return "structurePages/CitationNetwork";
    }

    @RequestMapping("/coauthorPattern")
    @ResponseBody
    public String getcoauthorPattern() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map=new HashMap<>();
        List<Integer> list=new ArrayList<>();
        for(int i=0;i<years.length-1;i++){
            list.add(orgsCompareService.coAuthorCount(org1,org2,years[i],years[i+1]));
        }
        map.put("data",list);
        return mapper.writeValueAsString(map);
    }

    @RequestMapping("/citationPattern")
    @ResponseBody
    public String getcitationPattern() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map=orgsCompareService.citationCount(org1,org2);
        return mapper.writeValueAsString(map);
    }

    @RequestMapping("/allCoescoauthor")
    @ResponseBody
    public String getAllParametersOfCoauthorship(String orgs) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map=new HashMap<>();
        String[] orgS = orgs.split(",");;
        for(String org:orgS){
            map.put(org,orgsCompareService.allCoefficientsA(org));
        }
        return mapper.writeValueAsString(map);

    }

    @RequestMapping("/coauthorshipParams")
    public String coauthorshipParamsPage(){
        return  "ComparePages/allParamA";

    }

    @RequestMapping("/allCoescitation")
    @ResponseBody
    public String getAllParametersOfCitation(String orgs) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map=new HashMap<>();
        String[] Orgs = orgs.split(",");;

        for(String org:Orgs){
            map.put(org,orgsCompareService.allCoefficientsC(org));
        }
        return mapper.writeValueAsString(map);

    }

    @RequestMapping("/citationParams")
    public String citationParamsPage(){
        return "ComparePages/allParamC";

    }


}
