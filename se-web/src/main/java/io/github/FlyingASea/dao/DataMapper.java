package io.github.FlyingASea.dao;

import io.github.FlyingASea.entity.DataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

@Mapper
public interface DataMapper {

    void createData(DataEntity dataEntity);

    DataEntity findDataById(@Param("room") String room);

    DataEntity[] selectDatasFromDate(@Param("id") String id,
                                     @Param("begin") Timestamp begin
    );
}
