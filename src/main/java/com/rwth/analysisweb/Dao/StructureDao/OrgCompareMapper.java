package com.rwth.analysisweb.Dao.StructureDao;

import com.rwth.analysisweb.Entity.StructureEntity.AuthorPair;
import com.rwth.analysisweb.Entity.StructureEntity.CitationCoe;
import com.rwth.analysisweb.Entity.StructureEntity.CoauthorCoe;
import com.rwth.analysisweb.Entity.StructureEntity.PaperPair;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrgCompareMapper {
    @Results({
            @Result(property = "node1", column = "author1"),
            @Result(property = "node2", column = "author2"),
            @Result(property = "paperId", column="paper_id"),
            @Result(property = "paperYear",column="paper_year")
    })
    @Select("select a.paper_id as paper_id,a.author_id as author1,b.author_id as author2 from authors_${A} a, authors_${B} b where a.paper_id=b.paper_id;")
    List<AuthorPair> getCoAuthor(@Param("A") String org1, @Param("B") String org2);

    @Select("select distinct a.paper_id from authors_${A} a, authors_${B} b where a.paper_id=b.paper_id;")
    @ResultType(Map.class)
    List<String> getCoPapers(@Param("A") String org1,@Param("B") String org2);

    @Select("select count(b.id) from(select distinct a.paper_id as id from authors_${A} a, authors_${B} b where a.paper_id=b.paper_id and a.paper_year>=#{start} and a.paper_year<#{end})b;")
    int coAuthorCount(@Param("A") String org1,@Param("B") String org2,@Param("start") int start,@Param("end") int end);



    @Results({
            @Result(property = "node1", column = "paper_id"),
            @Result(property = "node2", column = "reference"),
            @Result(property = "paperYear",column="paper_year")
    })
    @Select("select distinct a.paper_id as paper_id,b.paper_id as reference from all_citations_${A} a,all_citations_${B} b where a.reference=b.paper_id and a.paper_id not in(select distinct a.paper_id from all_citations_${A} a,all_citations_${B} b where a.paper_id=b.paper_id);")
    List<PaperPair>getCitation(@Param("A") String org1,@Param("B") String org2);

    @Select("select * from coe where org=#{org}")
    List<CoauthorCoe> getCoauthorcoe(@Param("org") String org1);

    @Select("select * from coe_citation where org=#{org}")
    List<CitationCoe> getCitationcoe(@Param("org") String org1);




}

