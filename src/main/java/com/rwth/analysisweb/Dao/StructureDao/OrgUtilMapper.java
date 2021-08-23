package com.rwth.analysisweb.Dao.StructureDao;


import com.rwth.analysisweb.Entity.StructureEntity.*;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrgUtilMapper {

    @Select("select * from ${org} limit #{limits},#{page}")
    List<PaperDetails>returnAllPapers(@Param("org") String org,@Param("limits") int limits, @Param("page") int page);

    //number of authors of org
    @Select("select count(b.id) from (SELECT distinct author_id as id FROM authors_${org} where paper_year>= #{StartYear} and paper_year<#{EndYear} )b")
    int findAuthorsCount(@Param("org") String org,@Param("StartYear") Integer StartYear, @Param("EndYear") Integer EndYear);


    //get the number of papers which have n authors
    @Select("select COUNT(m.id) from (select paper_id as id,count(*) as count from authors_${org} where paper_year>= #{StartYear} and paper_year < #{EndYear} group by paper_id having count= #{n})m")
    int findPapersWithNauthors(@Param("org") String org,@Param("StartYear") Integer StartYear, @Param("EndYear") Integer EndYear,@Param("n") Integer n);


    //get the top ten authors who writes more papers

    @Select("SELECT author_name,count(author_id) as num from authors_${org} group by author_name having count(author_id)>1 ORDER BY num desc limit 10")
    @ResultType(List.class)
    List<Map<String,Integer>> getTopTen(@Param("org") String org);


    @Select("select COUNT(m.id) from (select author_id as id,count(*) as count from authors_${org} where paper_year>= #{StartYear} and paper_year < #{EndYear} group by author_id having count= #{n})m")
    int findAuthorsWriteNpapers(@Param("org") String org,@Param("StartYear") Integer StartYear, @Param("EndYear") Integer EndYear,@Param("n") Integer n);


    @Select("Select distinct author_name from authors_${org} where author_id=#{id}")
    String getAuthor(@Param("org") String org,@Param("id") String id);



    @Results({
            @Result(property = "authorId", column = "author_id"),
            @Result(property = "authorName", column = "author_name"),
            @Result(property = "authorId", column = "author_id"),
            @Result(property="id", column="id"),
            @Result(property = "org", column = "org"),
            @Result(property = "paperYear", column = "paper_year")
    })
    @Select("SELECT * FROM authors_${org}  where author_name=#{name}")
    List<AuthorsEntity> getData(@Param("name") String name, @Param("org") String org);

    //number of papers
    @Select("select count(*) from ${org} where paper_year>= #{StartYear} and paper_year < #{EndYear}")
    int findPapersCount(@Param("org") String org,@Param("StartYear") Integer StartYear, @Param("EndYear") Integer EndYear);

    //number of english papers
    @Select("select count(*) from ${org} where (language='null' or language='en') and paper_year>= #{StartYear} and paper_year < #{EndYear}")
    int findEnglishPapersCount(@Param("org") String org,@Param("StartYear") Integer StartYear, @Param("EndYear") Integer EndYear);

    //number of citation
    @Select("SELECT sum(n_citation) FROM ${org} where paper_year>= #{StartYear} and paper_year < #{EndYear}")
    Integer citationCount(@Param("org") String org,@Param("StartYear") Integer StartYear, @Param("EndYear") Integer EndYear);

    //get top ten most cited paper
    @Results({
            @Result(property = "id", column = "paper_id"),
            @Result(property = "title", column = "paper_title"),
            @Result(property = "year", column = "paper_year"),
            @Result(property = "citation", column = "n_citation")
    })
    @Select("SELECT paper_id,n_citation,paper_title FROM ${org} order by n_citation desc limit 10")
    @ResultType(Map.class)
    List<PapersShow> findTopTenPapers(@Param("org") String org);



    @Results({
            @Result(property = "paperId", column = "paper_id"),
            @Result(property = "id", column = "id"),
            @Result(property = "paperTitle", column = "paper_title"),
            @Result(property = "nCitation", column = "n_citation"),
            @Result(property = "paperYear", column = "paper_year"),
            @Result(property = "paperDoi", column = "paper_doi"),
            @Result(property = "language", column = "language"),
            @Result(property = "abstraction",column="abstraction"),
            @Result(property = "paperAbstraction",column = "paper_abstraction")
    })
    @Select("SELECT * FROM ${org} where paper_id=#{id}")
    PaperDetails getPaperOfOrg(@Param("org") String org, @Param("id") String id);


    @Results({
            @Result(property = "paperId", column = "paper_id"),
            @Result(property = "id", column = "id"),
            @Result(property = "paperTitle", column = "paper_title"),
            @Result(property = "nCitation", column = "n_citation"),
            @Result(property = "paperDoi", column = "paper_doi"),
            @Result(property = "language", column = "language"),
            @Result(property = "abstraction",column="abstraction"),
            @Result(property = "paperAbstraction",column = "paper_abstraction")
    })
    @Select("SELECT * FROM ${org} where paper_year>= #{StartYear} and paper_year < #{EndYear}")
    List<PaperDetails> getPapersIn(@Param("org") String org, @Param("StartYear") Integer StartYear, @Param("EndYear") Integer EndYear);


    //years
    @Select("Select distinct paper_year from ${org} order by paper_year asc")
    List<Integer> getYears(@Param("org") String org);

    @Select("Select paper_title from ${org} where paper_id=#{id}")
    String getTitle(@Param("org") String org, @Param("id") String id);

    @Results({
            @Result(property = "id", column = "paper_id"),
            @Result(property = "title", column = "paper_title"),
            @Result(property = "year", column = "paper_year"),
            @Result(property = "citation", column = "n_citation")
    })
    @Select("SELECT * FROM ${org} order by paper_year desc")
    List<PapersShow> getAllPapers(@Param("org") String org);

    @Results({
            @Result(property = "name", column = "author_name"),
            @Result(property = "org", column = "org")
    })
    @Select("SELECT AUTHOR_NAME,ORG FROM authors WHERE PAPER_ID=#{id}")
    List<AuthorOrg>getAuthorsOfPaper(@Param("id") String id);

    @Select("select * from OrgProfile where name=#{name}")
    Org orgProfile(@Param("name") String name);

    @Select("select paper_year from ${org} where paper_id=#{id}")
    int getYear(@Param("org") String org, @Param("id") String id);

    @Results({
            @Result(property = "id", column = "paper_id"),
            @Result(property = "title", column = "paper_title"),
            @Result(property = "year", column = "paper_year"),
            @Result(property = "citation", column = "n_citation")
    })
    @Select("SELECT * FROM ${org} where paper_id=#{id}")
    PapersShow getPaperByID(@Param("org") String org,@Param("id") String id);

}
