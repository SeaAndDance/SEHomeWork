package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.DataMapper;
import io.github.FlyingASea.dao.RoomMapper;
import io.github.FlyingASea.entity.RoomData;
import io.github.FlyingASea.entity.RoomEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("RoomService")
public class RoomService {
    @Resource
    private RoomMapper roomRepository;


    public void newRoom(String id, String public_key) {
        roomRepository.createRoom(id, public_key);
    }

    public void delRoom(String id) {
        roomRepository.delRoomById(id);
    }

    public RoomEntity getRoom(String id) {
       return roomRepository.findRoomById(id);
    }
}
