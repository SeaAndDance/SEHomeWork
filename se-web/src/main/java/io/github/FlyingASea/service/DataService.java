package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.DataMapper;
import io.github.FlyingASea.dao.StateMapper;
import io.github.FlyingASea.entity.StateEntity;
import io.github.FlyingASea.util.Pair;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Map;

@Service("DataService")
public class DataService {
    @Resource
    private DataMapper dataRepository;

    @Resource
    private StateEntity stateEntity;

    @Resource
    private RoomService roomService;

    @Resource
    private StateMapper stateRepository;

    private void convert(Map<String, String> data) {
        stateEntity.setId(data.get("id"));
        stateEntity.setTemperature(Integer.parseInt(data.get("temperature")));
        stateEntity.setWind_speed(Integer.parseInt(data.get("wind_speed")));
        stateEntity.setIs_on(Integer.parseInt(data.get("is_on")));
        stateEntity.setLast_update(Timestamp.valueOf(data.get("last_update")));
    }

    public Pair<String, String> recordRoomData(Map<String, String> data) {
        String id = data.get("id");
        Timestamp last_update = Timestamp.valueOf(data.get("last_update"));
        StateEntity state = stateRepository.findStateById(id);

        if (data.get("temperature") == null) {
            data.put("temperature", String.valueOf(state.getTemperature()));
        } else {
            stateRepository.modifyTemperature(id, Integer.parseInt(data.get("temperature")), last_update);
        }

        if (data.get("wind_speed") == null) {
            data.put("wind_speed", String.valueOf(state.getWind_speed()));
        } else {
            stateRepository.modifyWindSpeed(id, Integer.parseInt(data.get("wind_speed")), last_update);
        }

        if (data.get("is_on") == null) {
            data.put("is_on", String.valueOf(state.getIs_on()));
        } else {
            stateRepository.modifyIsOn(id, Integer.parseInt(data.get("is_on")), last_update);
        }

        try {
            dataRepository.createData(/*脑子木了，明天再写吧*/,id, Integer.parseInt(data.get("temperature")), Integer.parseInt(data.get("wind_speed")),
                    Integer.parseInt(data.get("is_on")), last_update);
            return new Pair<>("unique_id", data.get("unique_id"));
        } catch (Exception e) {
            return null;
        }

    }

}
