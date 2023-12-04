package io.github.FlyingASea.dao;

import io.github.FlyingASea.entity.RoomEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoomMapper {
    void delRoomById(@Param("id") String id);
    void createRoom(@Param("id") String id, @Param("public_key") String public_key);
    RoomEntity findRoomById(@Param("id") String id);
    String[] getAllRoom();
}
