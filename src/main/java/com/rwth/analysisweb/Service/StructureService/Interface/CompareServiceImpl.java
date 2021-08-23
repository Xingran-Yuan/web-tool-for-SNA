package com.rwth.analysisweb.Service.StructureService.Interface;

import com.rwth.analysisweb.Algs.structure.SNA.CitationNetwork;
import com.rwth.analysisweb.Algs.structure.SNA.CoauthorshipNetwork;
import com.rwth.analysisweb.Algs.structure.alg.CentralityResult;
import com.rwth.analysisweb.Algs.structure.util.GraphBuilder;
import com.rwth.analysisweb.Dao.StructureDao.OrgCompareMapper;
import com.rwth.analysisweb.Dao.StructureDao.OrgUtilMapper;
import com.rwth.analysisweb.Entity.StructureEntity.*;
import com.rwth.analysisweb.Service.StructureService.Interface.CoAuthorService;
import com.rwth.analysisweb.Service.StructureService.Interface.CitationService;
import com.rwth.analysisweb.Service.StructureService.Interface.NetworkService;
import com.rwth.analysisweb.Service.StructureService.Interface.OrgsCompareService;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("CompareService")
public class CompareServiceImpl implements NetworkService, OrgsCompareService, CoAuthorService, CitationService {
    private List<AuthorPair> CoAuthordata;
    private ArrayList<GraphBuilder.NewWeightedEdge<String>> CoAuthoredges;
    private CoauthorshipNetwork coAuthorNetwork;
    private CitationNetwork citationNetwork;
    private String orgA;
    private String orgB;
    private List<PaperPair> AciteB;
    private List<PaperPair> BciteA;


    @Autowired
    OrgCompareMapper Mapper;

    @Autowired
    OrgUtilMapper orgUtilMapper;

    @Override
    public void chooseOrgs(String org1, String org2) {
        this.orgA = org1;
        this.orgB = org2;
    }

    @Override
    public void buildCitationNet() {
        AciteB = Mapper.getCitation(orgA, orgB);
        BciteA = Mapper.getCitation(orgB, orgA);

        ArrayList<String> nodes = new ArrayList<>();
        for (int i = 0; i < AciteB.size(); i++) {
            nodes.add(AciteB.get(i).getNode1());
            nodes.add(AciteB.get(i).getNode2());
        }

        for (int i = 0; i < BciteA.size(); i++) {
            nodes.add(BciteA.get(i).getNode1());
            nodes.add(BciteA.get(i).getNode2());
        }

        this.citationNetwork = new CitationNetwork(nodes);

    }


    @Override
    public void buildCoAuthorNet() {
        CoAuthordata = Mapper.getCoAuthor(orgA, orgB);
        HashMap<String, Double> map = new HashMap<>();
        this.CoAuthoredges = new ArrayList<>();
        for (AuthorPair nodes : CoAuthordata) {
            String pair = nodes.getNode1() + "+" + nodes.getNode2();
            if (map.containsKey(pair)) map.put(pair, map.get(pair) + 1.0);
            else map.put(pair, 1.0);
        }
        for (Map.Entry<String, Double> e : map.entrySet()) {

            int i = e.getKey().indexOf("+");
            String src = e.getKey().substring(0, i);
            String dst = e.getKey().substring(i + 1);
            double weight = e.getValue();
            GraphBuilder.NewWeightedEdge edge = new GraphBuilder.NewWeightedEdge(src, dst, weight);
            CoAuthoredges.add(edge);
        }
        this.coAuthorNetwork = new CoauthorshipNetwork(CoAuthoredges);
    }


    @Override
    public int NodesCount(int i) {
        if (i == 0) return coAuthorNetwork.getSizeOfVertices();
        else return citationNetwork.getSizeOfVertices();

    }

    @Override
    public int EdgesCount(int i) {
        if (i == 0) return coAuthorNetwork.getSizeOfEdges();
        else return citationNetwork.getSizeOfEdges();
    }

    @Override
    public double AveragePathLength(int i) {
        if (i == 0) return coAuthorNetwork.AveragePathLength();
        else return citationNetwork.AveragePathLength();
    }

    @Override
    public double GlobalClusteringCoefficient(int i) {
        if (i == 0) return coAuthorNetwork.GlobalClusteringCoefficient();
        else return citationNetwork.GlobalClusteringCoefficient();
    }

    @Override
    public double Diameter(int i) {
        if (i == 0) return coAuthorNetwork.Diameter();
        else return citationNetwork.Diameter();
    }

    @Override
    public double MaximumBetweenness(int i) {
        if (i == 0) return coAuthorNetwork.MaximumBetweenness();
        else return citationNetwork.MaximumBetweenness();
    }

    @Override
    public double LargestConnectedComponent(int i) {
        if (i == 0) return coAuthorNetwork.LargestConnectedComponent();
        else return citationNetwork.LargestConnectedComponent();
    }

    @Override
    public CentralityResult<String> DegreeCentrality(int i) {
        if (i == 0) return coAuthorNetwork.DegreeCentrality();
        else return citationNetwork.DegreeCentrality();

    }

    @Override
    public CentralityResult<String> BetwennnessCentrality(int i) {
        if (i == 0) return coAuthorNetwork.BetwennnessCentrality();
        else return citationNetwork.BetwennnessCentrality();
    }

    @Override
    public List<PaperDetails> coAuthorPapers() {
        List<String> papers = Mapper.getCoPapers(orgA, orgB);
        List<PaperDetails> res = new ArrayList<>();
        for (String s : papers) {
            PaperDetails org = orgUtilMapper.getPaperOfOrg(orgA, s);
            res.add(org);
        }
        return res;
    }

    @Override
    public Queue<GraphBuilder.NewWeightedEdge<String>> frequentCoAuthots() {
        Queue<GraphBuilder.NewWeightedEdge<String>> pq = new PriorityQueue<>(new Comparator<GraphBuilder.NewWeightedEdge>() {
            @Override
            public int compare(GraphBuilder.NewWeightedEdge e1, GraphBuilder.NewWeightedEdge e2) {

                if (e2.getW() - e1.getW() >= 0) return -1;
                else return 1;
            }
        });

        for (GraphBuilder.NewWeightedEdge<String> edge : CoAuthoredges) {
            if (pq.size() < 10) {
                pq.offer(edge);
            } else if (edge.getW() > pq.peek().getW()) {
                pq.poll();
                pq.offer(edge);
            }
        }
        return pq;
    }

    @Override
    public List<PaperDetails> paperByTwoAuthors(String id1, String id2) {
        List<PaperDetails> papers = new ArrayList<>();
        for (AuthorPair pair : CoAuthordata) {
            if (pair.getNode1() .equals(id1) ) {
                if (pair.getNode2() .equals(id2)) {
                    papers.add(orgUtilMapper.getPaperOfOrg(orgA, pair.getPaperId()));
                }
            }
        }
        return papers;
    }

    @Override
    public int[]getNumCitation(){
        int[] res=new int[2];
        res[0]=AciteB.size();
        res[1]=BciteA.size();
        return res;
    }


    @Override
    public Map<String,Object> getCoAuthorNet(String orgA,String orgB){
        Map<String,Object> res=new HashMap<>();
        List<Map<String,Object>> edges=new ArrayList<>();
        List<Map<String,Object>> nodes=new ArrayList<>();

        for(String node:coAuthorNetwork.nodesSet()){
            Map<String,Object> nodesMap=new HashMap<>();
            nodesMap.put("id",node);
            nodesMap.put("shape","dot");
            String author=orgUtilMapper.getAuthor(orgA,node);
            System.err.println(author);
            if(author==null){
                author=orgUtilMapper.getAuthor(orgB,node);
                nodesMap.put("color","#0000FF");
                nodesMap.put("label",author);
            }
            else{
                nodesMap.put("color","#FF0000");
                nodesMap.put("label",author);
            }
            nodes.add(nodesMap);
        }


        for(DefaultEdge e:coAuthorNetwork.graph.edgeSet()){
            Map<String,Object> edegesMap=new HashMap<>();

            edegesMap.put("from",coAuthorNetwork.graph.getEdgeSource(e));
            edegesMap.put("to",coAuthorNetwork.graph.getEdgeTarget(e));
            edegesMap.put("value",coAuthorNetwork.graph.getEdgeWeight(e));

            edges.add(edegesMap);

        }
        res.put("nodes",nodes);
        res.put("edges",edges);
        res.put("NodesCount",NodesCount(0));
        res.put("EdgesCount",EdgesCount(0));
        res.put("Clustering",coAuthorNetwork.GlobalClusteringCoefficient());
        System.err.println("cluser");
        res.put("Maximum",coAuthorNetwork.MaximumBetweenness());
        System.err.println("maximum");
        res.put("Largest",coAuthorNetwork.LargestConnectedComponent());
        System.err.println("largest");
        res.put("Diameter",coAuthorNetwork.Diameter());
        System.err.println("diameter");
        res.put("Average",coAuthorNetwork.AveragePathLength());
        System.err.println("average");

        return res;
    }

    @Override
    public Map<String,Object> getCitationNet(String orgA,String orgB){
        Map<String,Object> res=new HashMap<>();
        List<Map<String,Object>> edges=new ArrayList<>();
        List<Map<String,Object>> nodes=new ArrayList<>();



        for(String node:citationNetwork.nodesSet()){
            Map<String,Object> nodesMap=new HashMap<>();
            nodesMap.put("id",node);
            nodesMap.put("shape","dot");
            nodesMap.put("color",randomColor());
            String title=orgUtilMapper.getTitle(orgA,node);
            if(title==null){
                nodesMap.put("color","#0000FF");
                nodesMap.put("url","http://localhost:8080/paper?org="+orgB+"&id="+node);
            }else{
                nodesMap.put("color","#FF0000");
                nodesMap.put("url","http://localhost:8080/paper?org="+orgA+"&id="+node);
            }
            //nodesMap.put("label",orgUtilMapper.getTitle(org,node));
            nodes.add(nodesMap);
        }


        for(DefaultEdge e:citationNetwork.graph.edgeSet()){
            Map<String,Object> edgesMap=new HashMap<>();

            edgesMap.put("from",citationNetwork.graph.getEdgeSource(e));
            edgesMap.put("to",citationNetwork.graph.getEdgeTarget(e));
            edgesMap.put("value",citationNetwork.graph.getEdgeWeight(e));
            edgesMap.put("arrows","{to:{ enabled:true,type:vee}");
            edgesMap.put("label",citationNetwork.graph.getEdgeWeight(e));

            edges.add(edgesMap);

        }

        System.err.println("点的数量是"+" "+citationNetwork.graph.vertexSet().size());
        System.err.println("边的数量是"+" "+citationNetwork.graph.edgeSet().size());
        res.put("nodes",nodes);
        res.put("edges",edges);
        res.put("NodesCount",NodesCount(1));
        res.put("EdgesCount",EdgesCount(1));


        res.put("Clustering",citationNetwork.GlobalClusteringCoefficient());
        System.err.println("cluser");
        res.put("Maximum",citationNetwork.MaximumBetweenness());
        System.err.println("maximum");
        res.put("Largest",citationNetwork.LargestConnectedComponent());
        System.err.println("largest");
        res.put("Diameter",citationNetwork.Diameter());
        System.err.println("diameter");
        res.put("Average",citationNetwork.AveragePathLength());
        System.err.println("average");

        return res;
    }

    private String randomColor(){
        String red;
        String green;
        String blue;
        Random random = new Random();
        red = Integer.toHexString(random.nextInt(256)).toUpperCase();
        green = Integer.toHexString(random.nextInt(256)).toUpperCase();
        blue = Integer.toHexString(random.nextInt(256)).toUpperCase();

        red = red.length()==1 ? "0" + red : red ;
        green = green.length()==1 ? "0" + green : green ;
        blue = blue.length()==1 ? "0" + blue : blue ;
        String color = "#"+red+green+blue;
        return color;
    }

    @Override
    public int coAuthorCount(String org1, String org2,int start,int end){
        return Mapper.coAuthorCount(org1,org2,start,end);
    }

    @Override
    public Map<String,Object> citationCount(String org1,String org2){
        List<PaperPair> AciteB=Mapper.getCitation(org1,org2);
        List<PaperPair> BciteA=Mapper.getCitation(org2,org1);
        String [] xname={org1 +" cite "+org2,org2+ " cite "+ org1};

        Map<String,Object> res=new HashMap<>();
        res.put("xName",xname);
        res.put("first",prepareData(AciteB,org1));
        res.put("second",prepareData(BciteA,org2));

        return res;

    }

    private List<Integer> prepareData(List<PaperPair> AciteB,String org1){
        List<Integer> res=new ArrayList<>();
        int first=0,second=0,third=0,fouth=0,fifth=0,sixth=0,seventh=0,eighth=0,nineth=0;

        for(PaperPair pair:AciteB){
            int year=orgUtilMapper.getYear(org1,pair.getNode1());
            switch (setTimeslices(year)){
                case 0:break;
                case 1:
                    first++;
                    break;
                case 2:
                    second++;
                    break;
                case 3:
                    third++;
                    break;
                case 4:
                    fouth++;
                    break;
                case 5:
                    fifth++;
                    break;
                case 6:
                    sixth++;
                    break;
                case 7:
                    seventh++;
                    break;
                case 8:
                    eighth++;
                    break;
                case 9:
                    nineth++;
                    break;
            }

        }

        res.add(first);
        res.add(second);
        res.add(third);
        res.add(fouth);
        res.add(fifth);
        res.add(sixth);
        res.add(seventh);
        res.add(eighth);
        res.add(nineth);

        return res;
    }

    private int setTimeslices(int year){
        if(year<1970) return 1;
        if(year<1980) return 2;
        if(year<1990) return 3;
        if(year<2000) return 4;
        if(year<2005) return 5;
        if(year<2010) return 6;
        if(year<2013) return 7;
        if(year<2016) return 8;
        if(year<2019) return 9;

        return 0;
    }

    public Map<String,Object>allCoefficientsA(String org){
        Map<String,Object> vertices=new HashMap<>();
        Map<String,Object> edges=new HashMap<>();
        Map<String,Object> clustering=new HashMap<>();
        Map<String,Object> maximum=new HashMap<>();
        Map<String,Object> largest=new HashMap<>();
        Map<String,Object> diameter=new HashMap<>();
        Map<String,Object> average=new HashMap<>();
 //       List<Map<String,Object>> res=new ArrayList<>();

 //       String[] orgs = {"KIT","TUBerlin","TUM","RWTH","Hannover","Stuttgart","TUDarmstadt", "TUBraunschweig", "TUDresden"};
//        for(String org:orgs){
            Map<String,Object> map=new HashMap<>();
            List<CoauthorCoe> authorCoes=Mapper.getCoauthorcoe(org);
            //List<CitationCoe> citationCoes=Mapper.getCitationcoe(org);

            List<Integer> verticesA=new ArrayList<>();
            List<Integer> edgesA=new ArrayList<>();
            List<Double> clusteringA=new ArrayList<>();
            List<Double> maximumA=new ArrayList<>();
            List<Double> largestA=new ArrayList<>();
            List<Integer> diameterA=new ArrayList<>();
            List<Double> averageA=new ArrayList<>();

            /*List<Integer> verticesC=new ArrayList<>();
            List<Integer> edgesC=new ArrayList<>();
            List<Double> clusteringC=new ArrayList<>();
            List<Double> maximumC=new ArrayList<>();
            List<Double> largestC=new ArrayList<>();
            List<Integer> diameterC=new ArrayList<>();
            List<Double> averageC=new ArrayList<>();*/

            for(int i=0;i<authorCoes.size();i++){
                CoauthorCoe coauthorCoe=authorCoes.get(i);
                //CitationCoe citationCoe=citationCoes.get(i);

                verticesA.add(coauthorCoe.getVertices());
                //verticesC.add(citationCoe.getVertices());

                edgesA.add(coauthorCoe.getEdges());
                //edgesC.add(citationCoe.getEdges());

                clusteringA.add(coauthorCoe.getClustering());
                //clusteringC.add(citationCoe.getClustering());

                maximumA.add(coauthorCoe.getMaximum());
                //maximumC.add(citationCoe.getMaximum());

                largestA.add(coauthorCoe.getLargest());
                //largestC.add(citationCoe.getLargest());

                diameterA.add(coauthorCoe.getDiameter());
                //diameterC.add(citationCoe.getDiameter());

                averageA.add(coauthorCoe.getAverage());
                //averageC.add(citationCoe.getAverage());

 //           }
            map.put("vertices",verticesA);
            map.put("edges",edgesA);
            map.put("clustering",clusteringA);
            map.put("maximum",maximumA);
            map.put("largest",largestA);
            map.put("diameter",diameterA);
            map.put("average",averageA);


        }

        return map;
    }

    public Map<String,Object>allCoefficientsC(String org){
        Map<String,Object> vertices=new HashMap<>();
        Map<String,Object> edges=new HashMap<>();
        Map<String,Object> clustering=new HashMap<>();
        Map<String,Object> maximum=new HashMap<>();
        Map<String,Object> largest=new HashMap<>();
        Map<String,Object> diameter=new HashMap<>();
        Map<String,Object> average=new HashMap<>();
        //       List<Map<String,Object>> res=new ArrayList<>();

        //       String[] orgs = {"KIT","TUBerlin","TUM","RWTH","Hannover","Stuttgart","TUDarmstadt", "TUBraunschweig", "TUDresden"};
//        for(String org:orgs){
        Map<String,Object> map=new HashMap<>();
        //List<CoauthorCoe> authorCoes=Mapper.getCoauthorcoe(org);
        List<CitationCoe> citationCoes=Mapper.getCitationcoe(org);

        List<Integer> verticesA=new ArrayList<>();
        List<Integer> edgesA=new ArrayList<>();
        List<Double> clusteringA=new ArrayList<>();
        List<Double> maximumA=new ArrayList<>();
        List<Double> largestA=new ArrayList<>();
        List<Integer> diameterA=new ArrayList<>();
        List<Double> averageA=new ArrayList<>();

            /*List<Integer> verticesC=new ArrayList<>();
            List<Integer> edgesC=new ArrayList<>();
            List<Double> clusteringC=new ArrayList<>();
            List<Double> maximumC=new ArrayList<>();
            List<Double> largestC=new ArrayList<>();
            List<Integer> diameterC=new ArrayList<>();
            List<Double> averageC=new ArrayList<>();*/

        for(int i=0;i<citationCoes.size();i++){
            CitationCoe coauthorCoe=citationCoes.get(i);
            //CitationCoe citationCoe=citationCoes.get(i);

            verticesA.add(coauthorCoe.getVertices());
            //verticesC.add(citationCoe.getVertices());

            edgesA.add(coauthorCoe.getEdges());
            //edgesC.add(citationCoe.getEdges());

            clusteringA.add(coauthorCoe.getClustering());
            //clusteringC.add(citationCoe.getClustering());

            maximumA.add(coauthorCoe.getMaximum());
            //maximumC.add(citationCoe.getMaximum());

            largestA.add(coauthorCoe.getLargest());
            //largestC.add(citationCoe.getLargest());

            diameterA.add(coauthorCoe.getDiameter());
            //diameterC.add(citationCoe.getDiameter());

            averageA.add(coauthorCoe.getAverage());
            //averageC.add(citationCoe.getAverage());

            //           }
            map.put("vertices",verticesA);
            map.put("edges",edgesA);
            map.put("clustering",clusteringA);
            map.put("maximum",maximumA);
            map.put("largest",largestA);
            map.put("diameter",diameterA);
            map.put("average",averageA);


        }

        return map;
    }

}

