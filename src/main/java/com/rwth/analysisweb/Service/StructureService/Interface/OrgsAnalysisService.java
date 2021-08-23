package com.rwth.analysisweb.Service.StructureService.Interface;

import java.util.Map;

public interface OrgsAnalysisService {
    void chooseOrg(String org,int start,int end);
    int buildCitationNetwork(String org,int start, int end);
    int buildCoAuthorNetwork(String org,int start, int end);
    Map<String,Object> getCitationNet(String org);
    Map<String,Object> getCoAuthorNet(String org);


}
