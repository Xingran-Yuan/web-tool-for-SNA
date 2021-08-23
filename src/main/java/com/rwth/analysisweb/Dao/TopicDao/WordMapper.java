package com.rwth.analysisweb.Dao.TopicDao;

import com.rwth.analysisweb.Entity.StructureEntity.CitationEntity;
import com.rwth.analysisweb.Entity.TopicEntity.WordEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface WordMapper {
    @Results({
            @Result(property = "word", column = "word"),
            @Result(property = "prob", column = "prob")
    })
    @Select("SELECT a.word as word,b.prob as prob from ${org}_dictionary a,(select * from ${org}_topicsEvolution where timesliceID=#{i} AND topic_ID=#{j})b where a.id=b.word_id ORDER BY b.prob desc;")
    List<WordEntity> getWords(@Param("org") String org, @Param("i") int timeSliceId, @Param("j") int topicId );
}
