package io.github.FlyingASea.dao;

import io.github.FlyingASea.entity.DataEntity;
import io.github.FlyingASea.entity.StateEntity;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Mapper
public interface StateMapper {
    void createState(@Param("id") String id, @Param("temperature") Double temperature, @Param("wind_speed") int wind_speed,
                     @Param("is_on") int is_on, @Param("last_update") Timestamp last_update,
                     @Param("begin")Timestamp begin);

    StateEntity findStateById(@Param("id") String id);

    void modifyState(@Param("id") String id, @Param("temperature") Double temperature, @Param("wind_speed") int wind_speed,
                     @Param("is_on") int is_on, @Param("last_update") Timestamp last_update);

    void modifyTemperature(@Param("id") String id, @Param("temperature") Double temperature, @Param("last_update") Timestamp last_update);

    void modifyWindSpeed(@Param("id") String id, @Param("wind_speed") int wind_speed, @Param("last_update") Timestamp last_update);

    void modifyIsOn(@Param("id") String id, @Param("is_on") int is_on, @Param("last_update") Timestamp last_update);

    void removeState(@Param("id")String id);
    @MapKey("id")
    Map<String, Object> getAllState();

    @MapKey("id")
    Map<String, Object> getAllRoomState();
}
