package com.rwth.analysisweb.Service.StructureService.Interface;

import com.rwth.analysisweb.Algs.structure.SNA.CitationNetwork;
import com.rwth.analysisweb.Dao.StructureDao.OrgAnalysisMapper;
import com.rwth.analysisweb.Dao.StructureDao.OrgUtilMapper;
import com.rwth.analysisweb.Entity.StructureEntity.CitationEntity;
import com.rwth.analysisweb.Entity.StructureEntity.AuthorPair;
import com.rwth.analysisweb.Entity.StructureEntity.PaperDetails;
import com.rwth.analysisweb.Service.StructureService.Interface.CitationService;
import com.rwth.analysisweb.Service.StructureService.Interface.CoAuthorService;
import com.rwth.analysisweb.Service.StructureService.Interface.NetworkService;
import com.rwth.analysisweb.Algs.structure.SNA.CoauthorshipNetwork;
import com.rwth.analysisweb.Algs.structure.alg.CentralityResult;
import com.rwth.analysisweb.Algs.structure.util.GraphBuilder;
import com.rwth.analysisweb.Service.StructureService.Interface.OrgsAnalysisService;
import org.jgrapht.graph.DefaultEdge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("AnalysisService")
public class AnalysisServiceImpl<V> implements NetworkService, CoAuthorService, OrgsAnalysisService, CitationService {
    private ArrayList<GraphBuilder.NewWeightedEdge<String>> CoAuthoredges;
    private List<AuthorPair> CoAuthordata;
    private CoauthorshipNetwork coAuthorNetwork;
    private CitationNetwork citationNetwork;
    private ArrayList<String> citationNodes;
    private String org;
    private int start;
    private int end;

    @Autowired
    OrgAnalysisMapper analysisMapper;

    @Autowired
    OrgUtilMapper orgUtilMapper;

    @Override
    public void chooseOrg(String org,int start,int end){
        this.org=org;
        this.start=start;
        this.end=end;
    }

    @Override
    public int buildCitationNetwork(String org,int start, int end) {
        ArrayList<String> nodes= new ArrayList<>();
        List<CitationEntity> res=analysisMapper.getCitationNodes(org,start,end);
        System.err.println("---------------------------"+"  " +res.size());
        if(res.size()==0) return 0;
        for(int i=0;i< res.size();i++){
            nodes.add(res.get(i).getPaperId());
            nodes.add(res.get(i).getReference());
        }
        this.citationNodes=nodes;
        this.citationNetwork=new CitationNetwork(nodes);
        return 1;
    }

    @Override
    public int buildCoAuthorNetwork(String org,int start, int end) {
        CoAuthordata = analysisMapper.getAuthorsNodes(org, start, end);
        if (CoAuthordata.size()==0) return 0;
        HashMap<String, Double> map = new HashMap<>();
        CoAuthoredges = new ArrayList<>();
        for (AuthorPair nodes : CoAuthordata) {
            String  pair=nodes.getNode1()+"+"+nodes.getNode2();
            String pair_reverse = nodes.getNode2()+"+"+nodes.getNode1();
            if (map.containsKey(pair)) map.put(pair, map.get(pair) + 1.0);
            else if (map.containsKey(pair_reverse)) map.put(pair_reverse, map.get(pair_reverse) + 1.0);
            else map.put(pair, 1.0);
        }
        for (Map.Entry<String, Double> e : map.entrySet()) {
            int i=e.getKey().indexOf("+");
            String src=e.getKey().substring(0,i);
            String dst=e.getKey().substring(i+1);
            double weight = e.getValue();
            GraphBuilder.NewWeightedEdge edge = new GraphBuilder.NewWeightedEdge(src, dst, weight);
            CoAuthoredges.add(edge);
        }

        if(CoAuthoredges.size()!=0){
        this.coAuthorNetwork = new CoauthorshipNetwork(CoAuthoredges);}
        return 1;
    }


    @Override
    public int NodesCount(int i) {
        if(i==0){
            if(coAuthorNetwork==null) return 0;
            else {
                System.err.println("-------------------" +coAuthorNetwork.getSizeOfVertices());
                return coAuthorNetwork.getSizeOfVertices();
            }
        }
        else {
            if(citationNetwork==null) return 0;
            else return citationNetwork.getSizeOfVertices();
        }
    }

    @Override
    public int EdgesCount(int i) {
        if(i==0){
            if(coAuthorNetwork==null) return 0;
            else return coAuthorNetwork.getSizeOfEdges();}
        else {
            if(citationNetwork==null) return 0;
            else return citationNetwork.getSizeOfEdges();
        }
    }

    @Override
    public double AveragePathLength(int i) {
        if(i==0){
            if(coAuthorNetwork==null) return 0;
            else return coAuthorNetwork.AveragePathLength();}
        else {
            if(citationNetwork==null) return 0;
            else return citationNetwork.AveragePathLength();
        }
    }

    @Override
    public double GlobalClusteringCoefficient(int i) {
        if(i==0){
            if(coAuthorNetwork==null) return 0;
            else return coAuthorNetwork.GlobalClusteringCoefficient();}
        else {
            if(citationNetwork==null) return 0;
            else return citationNetwork.GlobalClusteringCoefficient();
        }
    }

    @Override
    public double Diameter(int i) {
        if(i==0){
            if(coAuthorNetwork==null) return 0;
            else return coAuthorNetwork.Diameter();}
        else {
            if(citationNetwork==null) return 0;
            else return citationNetwork.Diameter();
        }
    }

    @Override
    public double MaximumBetweenness(int i) {
        if(i==0){
            if(coAuthorNetwork==null) return 0;
            else return coAuthorNetwork.MaximumBetweenness();}
        else {
            if(citationNetwork==null) return 0;
            else return citationNetwork.MaximumBetweenness();
        }
    }

    @Override
    public double LargestConnectedComponent(int i) {
        if(i==0){
            if(coAuthorNetwork==null) return 0;
            else return coAuthorNetwork.LargestConnectedComponent();}
        else {
            if(citationNetwork==null) return 0;
            else return citationNetwork.LargestConnectedComponent();
        }
    }

    @Override
    public CentralityResult<String> DegreeCentrality(int i) {
        if(i==0){
            if(coAuthorNetwork==null) return null;
            else return coAuthorNetwork.DegreeCentrality();}
        else {
            if(citationNetwork==null) return null;
            else return citationNetwork.DegreeCentrality();
        }

    }

    @Override
    public CentralityResult<String> BetwennnessCentrality(int i) {
        if(i==0){
            if(coAuthorNetwork!=null) return null;
            else return coAuthorNetwork.BetwennnessCentrality();}
        else {
            if(citationNetwork!=null) return null;
            else return citationNetwork.BetwennnessCentrality();
        }
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
    public List<PaperDetails> paperByTwoAuthors(String id1, String id2){
        List<PaperDetails> papers=new ArrayList<>();
        for(AuthorPair pair:CoAuthordata){
            if(pair.getNode1().equals(id1)&&pair.getNode2().equals(id2))
            {
                papers.add(orgUtilMapper.getPaperOfOrg(org,pair.getPaperId()));
            }
        }
        return papers;
    }


    @Override
    public Map<String,Object> getCoAuthorNet(String org){
        Map<String,Object> res=new HashMap<>();
        List<Map<String,Object>> edges=new ArrayList<>();
        List<Map<String,Object>> nodes=new ArrayList<>();



        for(String node:coAuthorNetwork.nodesSet()){
            Map<String,Object> nodesMap=new HashMap<>();
            nodesMap.put("id",node);
            nodesMap.put("shape","dot");
            nodesMap.put("color",randomColor());
            nodesMap.put("label",orgUtilMapper.getAuthor(org,node));
            nodesMap.put("url",".../a");
            nodes.add(nodesMap);
        }


        for(GraphBuilder.NewWeightedEdge<String> e:CoAuthoredges){
            Map<String,Object> edgesMap=new HashMap<>();

            edgesMap.put("from",e.getSrc());
            edgesMap.put("to",e.getDst());
            edgesMap.put("value",e.getW());
            edgesMap.put("label",e.getW());

            edges.add(edgesMap);

        }
        res.put("nodes",nodes);
        res.put("edges",edges);
        res.put("NodesCount",NodesCount(0));
        res.put("EdgesCount",EdgesCount(0));

        return res;
    }

    @Override
    public Map<String,Object> getCitationNet(String org){
        Map<String,Object> res=new HashMap<>();
        List<Map<String,Object>> edges=new ArrayList<>();
        List<Map<String,Object>> nodes=new ArrayList<>();



        for(String node:citationNetwork.nodesSet()){
            Map<String,Object> nodesMap=new HashMap<>();
            nodesMap.put("id",node);
            nodesMap.put("shape","dot");
            nodesMap.put("color",randomColor());
            //nodesMap.put("label",orgUtilMapper.getTitle(org,node));
            nodesMap.put("url","paperT?id="+node);
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

        return res;
    }
    @Override
    public int[] getNumCitation(){
        return null;
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




}
