package com.rwth.analysisweb.Service.StructureService.Interface;

import com.rwth.analysisweb.Algs.structure.util.GraphBuilder;
import com.rwth.analysisweb.Entity.StructureEntity.PaperDetails;

import java.util.*;

public interface CoAuthorService {
    Queue<GraphBuilder.NewWeightedEdge<String>> frequentCoAuthots();
    List<PaperDetails> paperByTwoAuthors(String id1,String id2);
}
