package com.rwth.analysisweb.Dao.TopicDao;

import com.rwth.analysisweb.Entity.TopicEntity.TimesliceEntity;
import com.rwth.analysisweb.Entity.TopicEntity.TopicDistributionEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TopicDistributionMapper {

    @Select("select * from ${org}_topicsDistribution where paper_year>=#{start} and paper_year<#{end}")
    List<TopicDistributionEntity> getDistribution(@Param("org") String org,@Param("start") int start,@Param("end") int end);

    @Select("SELECT paper_id,topic${id} FROM ${org}_topicsDistribution where paper_year=#{year} and topic${id}>0 order by topic${id} desc;")
    List<TopicDistributionEntity>findPapersContainTopics(@Param("org") String org,@Param("year") int year,@Param("id") int id);


    @Select("select * from ${org}_topicsDistribution where paper_id=#{id}")
    TopicDistributionEntity getTopicsOfPaper(@Param("org") String org,@Param("id") String id);

}
