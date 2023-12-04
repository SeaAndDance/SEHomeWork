package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.RoomMapper;
import io.github.FlyingASea.entity.RoomEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("RoomService")
public class RoomService {
    @Resource
    private RoomMapper roomRepository;

    public RoomEntity getRoom(String id){
        return roomRepository.findRoomById(id);
    }

    public void checkInRoom(){

    }

    public void checkOutRoom(){

    }
}
