package io.github.FlyingASea.dao;

import io.github.FlyingASea.entity.DataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import javax.xml.crypto.Data;
import java.sql.Timestamp;

@Mapper
public interface DataMapper {
    void createData(@Param("entity") DataEntity entity);

    DataEntity[] findDatasById(@Param("room") String room);

    DataEntity[] selectDatasFromDate(@Param("room") String room,
                                     @Param("begin") Timestamp begin
    );
    DataEntity[] getAllData();
}
