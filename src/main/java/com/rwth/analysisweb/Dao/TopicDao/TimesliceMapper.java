package com.rwth.analysisweb.Dao.TopicDao;

import com.rwth.analysisweb.Entity.TopicEntity.TimesliceEntity;
import com.rwth.analysisweb.Entity.TopicEntity.WordEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface TimesliceMapper {
    @Results({
            @Result(property = "year", column = "year"),
            @Result(property = "count", column = "paperNums")
    })
    @Select("select year,paperNums from ${org}_timeslices")
    List<TimesliceEntity> getWords(@Param("org") String org);

    @Select("select timesliceID from ${org}_timeslices where year=#{year}")
    int getID(@Param("org") String org,@Param("year") int year);

    @Select("select year from ${org}_timeslices where timesliceID=#{ID}")
    int getYear(@Param("org") String org,@Param("ID") int id);
}
