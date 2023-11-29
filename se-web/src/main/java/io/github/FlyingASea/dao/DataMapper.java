package io.github.FlyingASea.dao;

import io.github.FlyingASea.entity.RoomData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;

@Mapper
public interface DataMapper {

    void createData(@Param("id") String id, @Param("temperature") int temperature, @Param("wind_speed") int wind_speed,
                    @Param("is_on") int is_on, @Param("last_update") Timestamp last_update);

    RoomData findDataById(@Param("id") String id);
}
