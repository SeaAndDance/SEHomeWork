package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.RoomMapper;
import io.github.FlyingASea.entity.RoomEntity;
import io.github.FlyingASea.util.SHA256withRSAUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Map;

@Service("RoomService")
public class RoomService {
    @Resource
    private RoomMapper roomRepository;

    public RoomEntity getRoom(String id) {
        return roomRepository.findRoomById(id);
    }

    public boolean updateRoomPort(String id, String port,
                                  String unique_id,
                                  String signature) {
        String data = id + unique_id + port;
        boolean flag = SHA256withRSAUtil.verifySign(roomRepository.getPublic_key(id), data, signature);
        if (flag)
            roomRepository.updatePort(id, port);
        return flag;
    }

    public String getPort(String id){
        return roomRepository.getPort(id);
    }

    public boolean checkSign(String operation, String unique_id,
                             String data, String time, String id, String signature) {
        return SHA256withRSAUtil.verifySign(roomRepository.getPublic_key(id), operation + unique_id + data + time, signature);
    }

    public boolean checkSign(Map<String, String> map) {
        return checkSign(map.get("operation"), map.get("unique_id"), map.get("data"), map.get("time"), map.get("id"), map.get("signature"));
    }


}
