package com.rwth.analysisweb.Dao.StructureDao;

import com.rwth.analysisweb.Entity.StructureEntity.AuthorPair;
import com.rwth.analysisweb.Entity.StructureEntity.CitationEntity;
import com.rwth.analysisweb.Entity.StructureEntity.CoAuthorEntity;
import org.apache.ibatis.annotations.*;

import java.util.*;

@Mapper
public interface OrgAnalysisMapper {

    @Results({
            @Result(property = "paperId", column = "paper_id"),
            @Result(property = "node1", column = "author_id1"),
            @Result(property = "node2", column = "author_id2"),
            @Result(property = "paperYear", column = "paper_year")
    })
    @Select("select * from co_author_${org} where paper_year>= #{StartYear} and paper_year<#{EndYear} order by paper_year")
    List<AuthorPair> getAuthorsNodes(@Param("org") String org, @Param("StartYear") Integer StartYear, @Param("EndYear") Integer EndYear);

    @Results({
            @Result(property = "paperId", column = "paper_id"),
            @Result(property = "reference", column = "reference"),
            @Result(property = "id", column = "id"),
            @Result(property = "paperYear", column = "paper_year")
    })
    @Select("select * from citation_${org} where paper_year>= #{StartYear} and paper_year<#{EndYear} order by paper_year")
    List<CitationEntity> getCitationNodes(@Param("org") String org, @Param("StartYear") Integer StartYear, @Param("EndYear") Integer EndYear);


    @Results({
            @Result(property = "paperId", column = "author_id"),
            @Result(property = "authorId1", column = "author_id2"),
            @Result(property = "authorId2", column = "author_id1"),
            @Result(property = "paperYear", column = "paper_year")
    })
    @Select("select * from co_author_${org} where paper_year>= #{StartYear} and paper_year<#{EndYear} order by paper_year")
    List<CoAuthorEntity> testMap(@Param("org") String org, @Param("StartYear") Integer StartYear, @Param("EndYear") Integer EndYear);



}
