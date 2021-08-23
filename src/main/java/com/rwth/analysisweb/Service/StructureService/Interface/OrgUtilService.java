package com.rwth.analysisweb.Service.StructureService.Interface;

import com.rwth.analysisweb.Entity.StructureEntity.*;

import java.util.*;

public interface OrgUtilService<Intger> {
    //count how many papers
    void setOrg(String org);

    int PapersCount();

    //count how many authors
    int AuthorsCount();

    //count how many english papers
    int EnglishPapersCount();

    //Citation count
    int CitationCount();

    //get  top ten most cited paper
    List<PapersShow> GetTopTenPapers(String org);

    //get top ten authors
    List<Map<String,Integer>> GetTopTenAuthors(String org);


    //get the number of papers which have n authors
    int PapersWithNauthors(String org,Integer StartYear, Integer EndYear, Integer n);

    int AuthorsWithNpapers(String org,Integer StartYear, Integer EndYear, Integer n);

    //get papers which write by an author
    List<AuthorsEntity> getAuthorsData(String org,String name);

    //get data of a paper
    PaperDetails getPaperData(String org,String id);

    //get papers in specific timeslices
    List<PaperDetails> getPapersIntime(String org,int start, int end);

    //List<PaperDetails> returnAll(String org,int limits,int page);

    Map<String,Object> returnStatistics(String org);

    //Paper Years
    List<Integer>getTime(String org);

    //Get All papers
    List<PapersShow> getAllPapers(String org);

    //get authors of one paper
    List<AuthorOrg> authorOfPaper(String id);

    Org orgProfile(String name);


}
