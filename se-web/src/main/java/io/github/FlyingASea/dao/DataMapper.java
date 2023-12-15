package io.github.FlyingASea.dao;

import io.github.FlyingASea.entity.DataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

@Mapper
public interface DataMapper {

    void createData(@Param("last_update") Timestamp last_update,
                    @Param("room") String room,
                    @Param("temperature") Double temperature,
                    @Param("wind_speed") int wind_speed,
                    @Param("is_on") int is_on);

    DataEntity findDataById(@Param("room") String room);

    DataEntity[] selectDatasFromDate(@Param("room") String room,
                                     @Param("begin") Timestamp begin
    );
}
