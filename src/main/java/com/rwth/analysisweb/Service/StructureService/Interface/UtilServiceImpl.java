package com.rwth.analysisweb.Service.StructureService.Interface;

import com.rwth.analysisweb.Dao.StructureDao.OrgUtilMapper;
import com.rwth.analysisweb.Entity.StructureEntity.*;
import com.rwth.analysisweb.Service.StructureService.Interface.OrgUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UtilServiceImpl implements OrgUtilService {
    private String org;
    @Autowired
    OrgUtilMapper OrgUtilMapper;

    @Override
    public void setOrg(String org){
        this.org=org;
    }

    @Override
    public List<PapersShow> getAllPapers(String org){
        return OrgUtilMapper.getAllPapers(org);
    }

    @Override
    public int PapersCount(){
        return OrgUtilMapper.findPapersCount(org,1900,2020);
    };

    @Override
    public List<PapersShow> GetTopTenPapers(String org){
        return OrgUtilMapper.findTopTenPapers(org);
    };

    @Override
    public List<Map<String,Integer>> GetTopTenAuthors(String org){
        return OrgUtilMapper.getTopTen(org);
    }

    @Override
    public int AuthorsCount(){
        return OrgUtilMapper.findAuthorsCount(org,1900,2020);}

    @Override
    public int PapersWithNauthors(String org,Integer StartYear, Integer EndYear, Integer n) {
        return OrgUtilMapper.findPapersWithNauthors(org,StartYear,EndYear,n);
    }

    @Override
    public List<AuthorsEntity> getAuthorsData(String org,String name){
        return OrgUtilMapper.getData(name,org);

    }

    //get data of a paper
    @Override
    public PaperDetails getPaperData(String org,String paper_id){
        return OrgUtilMapper.getPaperOfOrg(org,paper_id);
    };

    @Override
    public List<AuthorOrg> authorOfPaper(String id){
        return OrgUtilMapper.getAuthorsOfPaper(id);
    }

    //get papers in specific timeslices
    @Override
    public List<PaperDetails> getPapersIntime(String org,int start, int end){
        return OrgUtilMapper.getPapersIn(org,start,end);
    };

    //count how many english papers
    @Override
    public int EnglishPapersCount(){
        return OrgUtilMapper.findEnglishPapersCount(org,1900,2020);
    }

    //Citation count
    @Override
    public int CitationCount(){
        return OrgUtilMapper.citationCount(org,1900,2020);
    }

    @Override
    public Map<String,Object>returnStatistics(String org){

        Map<String,Object> map=new HashMap<>();
        List<Integer>papers=new ArrayList<>();
        List<Integer>englishPapers=new ArrayList<>();
        List<Integer>authors=new ArrayList<>();
        List<Integer>citation=new ArrayList<>();
        int[] years={1960,1970,1980,1990,2000,2005,2010,2013,2016,2020};

        for(int i=0;i<years.length-1;i++){
            papers.add(OrgUtilMapper.findPapersCount(org,years[i],years[i+1]));
            System.err.println(papers);
            englishPapers.add(OrgUtilMapper.findEnglishPapersCount(org,years[i],years[i+1]));
            System.err.println(englishPapers);
            authors.add(OrgUtilMapper.findAuthorsCount(org,years[i],years[i+1]));
            System.err.println(authors);
            citation.add(OrgUtilMapper.citationCount(org,years[i],years[i+1]));
            System.err.println(citation);
        }

        map.put("years",years);
        map.put("papers",papers);
        map.put("englishPapers",englishPapers);
        map.put("citations",citation);
        map.put("authors",authors);


        return map;
    }

    @Override
    public List<Integer>getTime(String org){
        return OrgUtilMapper.getYears(org);
    }

    @Override
    public int AuthorsWithNpapers(String org,Integer StartYear, Integer EndYear, Integer n){
        return OrgUtilMapper.findAuthorsWriteNpapers(org,StartYear,EndYear,n);
    }

    @Override
    public Org orgProfile(String name){
        return OrgUtilMapper.orgProfile(name);
    }



}
