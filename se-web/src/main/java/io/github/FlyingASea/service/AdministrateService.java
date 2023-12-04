package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.RoomMapper;
import io.github.FlyingASea.entity.RoomEntity;
import io.github.FlyingASea.entity.UserEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("AdministrateService")
public class AdministrateService {
    @Resource
    private RoomMapper roomRepository;

    public void newRoom(String id, String public_key) {
        roomRepository.createRoom(id, public_key);
    }

    public RoomEntity getRoom(String id) {
        return roomRepository.findRoomById(id);
    }

    public void delRoom(String id){
        roomRepository.delRoomById(id);
    }

    public List<String> getAllRoom(){
       return roomRepository.getAllRoom();
    }
}
