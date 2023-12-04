package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.StateMapper;
import io.github.FlyingASea.entity.StateEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service("StateService")
public class StateService {

    @Resource
    private StateMapper stateRepository;

    public StateEntity getState(String id) {
        return stateRepository.findStateById(id);
    }

    public void createState(String id, int temperature, int wind_speed, int is_on, Timestamp last_update, Timestamp begin) {
        stateRepository.createState(id, temperature, wind_speed, is_on, last_update, begin);
    }

    public void changeState(String type, Object data,
                            String id, Timestamp last_update) {
        switch (type) {
            case "temperature":
                if (data instanceof Integer) {
                    stateRepository.modifyTemperature(id, (int) data, last_update);
                }
                if (data instanceof String) {
                    stateRepository.modifyTemperature(id, Integer.parseInt((String) data), last_update);
                }
            case "wind_speed":
                if (data instanceof Integer) {
                    stateRepository.modifyWindSpeed(id, (int) data, last_update);
                }
                if (data instanceof String) {
                    stateRepository.modifyWindSpeed(id, Integer.parseInt((String) data), last_update);
                }
            case "is_on":
                if (data instanceof Integer) {
                    stateRepository.modifyIsOn(id, (int) data, last_update);
                }
                if (data instanceof String) {
                    if (((String) data).contains("1") || ((String) data).contains("0"))
                        stateRepository.modifyIsOn(id, Integer.parseInt((String) data), last_update);
                    stateRepository.modifyIsOn(id, Boolean.getBoolean((String) data) ? 1 : 0, last_update);
                }
                if (data instanceof Boolean) {
                    stateRepository.modifyIsOn(id, (Boolean) data ? 1 : 0, last_update);
                }
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
