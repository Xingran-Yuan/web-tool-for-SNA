package com.rwth.analysisweb.Controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rwth.analysisweb.Entity.StructureEntity.AuthorOrg;
import com.rwth.analysisweb.Entity.StructureEntity.Org;
import com.rwth.analysisweb.Entity.StructureEntity.PaperDetails;
import com.rwth.analysisweb.Entity.StructureEntity.PapersShow;
import com.rwth.analysisweb.Service.StructureService.Interface.OrgUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrgUtil {
    static String org="RWTH";
    @Autowired
    OrgUtilService orgUtilService;



    @RequestMapping("/")
    public String loginPage(HttpServletRequest request)throws Exception {
        org = request.getParameter("org");
        return "login";
    }

    @RequestMapping("/index")
    public String indexPage(){
        return "index";
    }

    @GetMapping(value = "/homepage")
    public String homePage(Model model) {
        System.err.println(org);
        orgUtilService.setOrg(org);
        String papersCount=orgUtilService.PapersCount()+"";
        System.err.println(papersCount);
        String englishPapers=orgUtilService.EnglishPapersCount()+"";
        String citationCount=orgUtilService.CitationCount()+"";
        String authorsCount=orgUtilService.AuthorsCount()+"";

        Org orgProfile=orgUtilService.orgProfile(org);


        model.addAttribute("papersCount", papersCount);
        model.addAttribute("englishPapers",englishPapers);
        model.addAttribute("citationCount",citationCount);
        model.addAttribute("authorsCount",authorsCount);
        model.addAttribute("uni",orgProfile.getUni());
        model.addAttribute("dep",orgProfile.getDep());
        model.addAttribute("url",orgProfile.getUrl_uni());



        return "homepage";
    }


    @GetMapping(value = "/orgProfile")
    public String orgProfile(Model model,String organization) {
        org=organization;
        orgUtilService.setOrg(org);
        String papersCount=orgUtilService.PapersCount()+"";
        String englishPapers=orgUtilService.EnglishPapersCount()+"";
        String citationCount=orgUtilService.CitationCount()+"";
        String authorsCount=orgUtilService.AuthorsCount()+"";

        Org orgProfile=orgUtilService.orgProfile(org);

        model.addAttribute("papersCount", papersCount);
        model.addAttribute("englishPapers",englishPapers);
        model.addAttribute("citationCount",citationCount);
        model.addAttribute("authorsCount",authorsCount);
        model.addAttribute("uni",orgProfile.getUni());
        model.addAttribute("dep",orgProfile.getDep());
        model.addAttribute("url",orgProfile.getUrl_uni());



        return "homepage";
    }

    @RequestMapping("/statistics")
    @ResponseBody
    public String getStatistics() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        orgUtilService.setOrg(org);
        Map<String,Object>data=orgUtilService.returnStatistics(org);
        String str = mapper.writeValueAsString(data);
        return str;
    }

    @RequestMapping("/getAllPapers")
    @ResponseBody
    public String getAllPapers(@RequestParam int page,@RequestParam int limit) throws JsonProcessingException{
        ObjectMapper mapper=new ObjectMapper();
        Map<String,Object> map=new HashMap<>();

        List<PapersShow> data= orgUtilService.getAllPapers(org);
        System.out.println("okk");
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


    //author page
    @GetMapping("/authors")
    @ResponseBody
    public String authorById(String author_id,Model model){


        return "/structurePages/AuthorPage";
    }

    //paper page
    @GetMapping("/paper")
    public String paperById(String org,Model model,String id){
        PaperDetails paper=orgUtilService.getPaperData(org,id);
        List<AuthorOrg> authors=orgUtilService.authorOfPaper(id);
        model.addAttribute("title",paper.getPaperTitle());
        model.addAttribute("year",paper.getPaperYear());
        model.addAttribute("cite",paper.getnCitation());
        model.addAttribute("language",paper.getLanguage());
        model.addAttribute("doi",paper.getPaperDoi());
        if(paper.getAbstraction()==null){
            model.addAttribute("abstraction","NULL");
        }
        else{
            String str=new String(paper.getPaperAbstraction());

            model.addAttribute("abstraction",str);}

        StringBuffer sb=new StringBuffer();
        for(AuthorOrg author:authors){
            sb.append(author.getName());
            sb.append("(");
            sb.append(author.getOrg());
            sb.append(")");
            sb.append(",");
        }
        model.addAttribute("authors",sb.toString());


        return "/structurePages/PaperPage";
    }

    //paper page
    @GetMapping("/paperT")
    public String paperById(Model model,String id){
        PaperDetails paper=orgUtilService.getPaperData(org,id);
        List<AuthorOrg> authors=orgUtilService.authorOfPaper(id);
        model.addAttribute("title",paper.getPaperTitle());
        model.addAttribute("year",paper.getPaperYear());
        model.addAttribute("cite",paper.getnCitation());
        model.addAttribute("language",paper.getLanguage());
        model.addAttribute("doi",paper.getPaperDoi());
        if(paper.getAbstraction()==null){
            model.addAttribute("abstraction","NULL");
        }
        else{
            String str=new String(paper.getAbstraction());

            model.addAttribute("abstraction",str);}

        StringBuffer sb=new StringBuffer();
        for(AuthorOrg author:authors){
            sb.append(author.getName());
            sb.append("(");
            sb.append(author.getOrg());
            sb.append(")");
            sb.append(",");
        }
        model.addAttribute("authors",sb.toString());


        return "page/PaperPage";
    }

    @RequestMapping("/changeOrg")
    public String chooseOrgsCitation(HttpServletRequest request)throws Exception {
        org=request.getParameter("org");
        return "index";
    }

}

