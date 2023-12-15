package io.github.FlyingASea.dao;

import io.github.FlyingASea.entity.DeviceEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DeviceMapper {
    void delRoomById(@Param("id") String id);
    void createRoom(@Param("id") String id, @Param("privateKey") String privateKey, @Param("port") String port);
    DeviceEntity findRoomById(@Param("id") String id);
    List<String> getAllRoom();
    void updatePort(@Param("id") String id, @Param("port")String port);
    String getPrivateKey(@Param("id") String id);

    String getPort(@Param("id")String id);
}
