package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.StateMapper;
import io.github.FlyingASea.entity.StateEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service("StateService")
public class StateService {

    @Resource
    private StateMapper stateRepository;

    @Resource
    private DataService dataService;

    public StateEntity getState(String id) {
        return stateRepository.findStateById(id);
    }

    public Map<String, Object> removeState(String id) {
        LocalDateTime dateTime = LocalDateTime.now();
        Timestamp last_update = Timestamp.valueOf(dateTime);
        stateRepository.modifyIsOn(id, 0, last_update);
        StateEntity now = stateRepository.findStateById(id);
        return dataService.generateBill(id, now);
    }

    public void createState(String id, int temperature, int wind_speed, int is_on, Timestamp last_update, Timestamp begin) {
        stateRepository.createState(id, temperature, wind_speed, is_on, last_update, begin);
    }

    public void changeState(String type, Object data,
                            String id, Timestamp last_update) {
        switch (type) {
            case "temperature" -> {
                if (data instanceof Integer) {
                    stateRepository.modifyTemperature(id, (int) data, last_update);
                }
                if (data instanceof String) {
                    stateRepository.modifyTemperature(id, Integer.parseInt((String) data), last_update);
                }
            }
            case "wind_speed" -> {
                if (data instanceof Integer) {
                    stateRepository.modifyWindSpeed(id, (int) data, last_update);
                }
                if (data instanceof String) {
                    stateRepository.modifyWindSpeed(id, Integer.parseInt((String) data), last_update);
                }
            }
            case "start" -> stateRepository.modifyIsOn(id, 1, last_update);
            case "stop" -> stateRepository.modifyIsOn(id, 0, last_update);
        }

    }

    public boolean createState(Map<String, Object> data, Timestamp begin) {
        Object id = data.get("id");
        Object temperature = data.get("temperature");
        Object wind_speed = data.get("wind_speed");
        Object is_on = data.get("is_on");
        Object last_update = data.get("last_update");
        if (id == null || temperature == null || wind_speed == null || is_on == null || last_update == null || begin == null)
            return false;
        if (id instanceof String && temperature instanceof Integer && wind_speed instanceof Integer &&
                is_on instanceof Integer && last_update instanceof Timestamp) {
            stateRepository.createState((String) id, (int) temperature,
                    (int) wind_speed, (int) is_on, (Timestamp) last_update, begin);
            return true;
        } else {
            return false;
        }
    }

    public boolean changeState(Map<String, Object> data) {
        Object id = data.get("id");
        Object temperature = data.get("temperature");
        Object wind_speed = data.get("wind_speed");
        Object is_on = data.get("is_on");
        Object last_update = data.get("last_update");
        if (id == null || temperature == null || wind_speed == null || is_on == null || last_update == null)
            return false;
        if (id instanceof String && temperature instanceof Integer && wind_speed instanceof Integer &&
                is_on instanceof Integer && last_update instanceof Timestamp) {
            stateRepository.modifyState((String) id, (int) temperature,
                    (int) wind_speed, (int) is_on, (Timestamp) last_update);
            return true;
        } else {
            return false;
        }
    }

    public List<Map<String, Object>> getAllState() {
        Map<String, Object> map = stateRepository.getAllState();
        List<Map<String, Object>> remap = new ArrayList<>();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, Object> i : map.entrySet()) {
                Map<String, Object> t = new HashMap<>();
                t.put("room", i.getKey());
                t.put("is_on", i.getValue());
                remap.add(t);
            }
        }
        return remap;
    }
}
