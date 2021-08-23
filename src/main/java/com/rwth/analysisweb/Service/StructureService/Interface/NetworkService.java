package com.rwth.analysisweb.Service.StructureService.Interface;

import com.rwth.analysisweb.Algs.structure.alg.CentralityResult;

import java.util.Map;


public interface NetworkService {
    /*0-coAuthorship network
    1-citationNetwork
     */


    int EdgesCount(int i);
    int NodesCount(int i);
    double AveragePathLength(int i);
    double GlobalClusteringCoefficient(int i);
    double Diameter(int i);
    double MaximumBetweenness(int i);
    double LargestConnectedComponent(int i);
    CentralityResult<String> DegreeCentrality(int i);
    CentralityResult<String> BetwennnessCentrality(int i);
















}
