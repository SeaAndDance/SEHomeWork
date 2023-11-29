package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.DataMapper;
import io.github.FlyingASea.entity.RoomData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;

import static io.github.FlyingASea.util.SHA256withRSAUtil.verifySign;

@Service("DataService")
public class DataService {
    @Resource
    private DataMapper dataRepository;

    @Resource
    private RoomData roomData;

    @Resource
    private RoomService roomService;

    private void convert(Map<String,String> data){
        roomData.setId(data.get("id"));
        roomData.setTemperature(Integer.parseInt(data.get("temperature")));
        roomData.setWind_speed(Integer.parseInt(data.get("wind_speed")));
        roomData.setIs_on(Integer.parseInt(data.get("is_on")));
        roomData.setLast_update(Timestamp.valueOf(data.get("last_update")));
    }

    public Map<String,String> recordRoomData(Map<String,String> data){
        roomData.setId(data.get("id"));
        if (data.get("temperature") == null){

        }
        if (data.get("wind_speed") == null){

        }
        if (data.get("is_on") == null){
            data
        }

    }

}
