package com.rwth.analysisweb.Service.StructureService.Interface;

import com.rwth.analysisweb.Entity.StructureEntity.PaperDetails;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface OrgsCompareService {
    void chooseOrgs(String org1,String org2);
    void buildCoAuthorNet();
    void buildCitationNet();
    List<PaperDetails> coAuthorPapers();
    Map<String,Object> getCitationNet(String orgA, String orgB);
    Map<String,Object> getCoAuthorNet(String orgA,String orgB);
    int coAuthorCount(String org1, String org2,int start,int end);
    Map<String,Object> citationCount(String org1,String org2);
    Map<String,Object>allCoefficientsA(String org);
    Map<String,Object>allCoefficientsC(String org);


}
