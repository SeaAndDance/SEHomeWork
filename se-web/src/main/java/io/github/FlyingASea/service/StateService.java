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

    public Map<String, Object> generateBill(String id) {
        LocalDateTime dateTime = LocalDateTime.now();
        Timestamp last_update = Timestamp.valueOf(dateTime);
        stateRepository.modifyIsOn(id, 0, last_update);
        StateEntity now = stateRepository.findStateById(id);
        Map<String, Object> bill = dataService.generateBill(id, now);
        stateRepository.removeState(id);
        return bill;
    }

    public void createState(String id, double temperature, int wind_speed, int is_on, Timestamp last_update, Timestamp begin) {
        stateRepository.createState(id, temperature, wind_speed, is_on, last_update, begin);
    }

    public void changeState(String type, Object data,
                            String id, Timestamp last_update) {
        switch (type) {
            case "temperature" -> {
                if (data instanceof Integer) {
                    stateRepository.modifyTemperature(id, (Double) data, last_update);
                }
                if (data instanceof String) {
                    stateRepository.modifyTemperature(id, Double.parseDouble((String) data), last_update);
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
        if (id instanceof String && temperature instanceof Double && wind_speed instanceof Integer &&
                is_on instanceof Integer && last_update instanceof Timestamp) {
            stateRepository.createState((String) id, (Double) temperature,
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

        if (id == null || temperature == null || wind_speed == null || is_on == null || last_update == null){
            System.out.println("it has null!   ---------------------");
            return false;
        }
        if (id instanceof String && temperature instanceof Double && wind_speed instanceof Integer &&
                is_on instanceof Integer && last_update instanceof Timestamp) {
            System.out.println(" yes! ");
            stateRepository.modifyState((String) id, (Double) temperature,
                    (int) wind_speed, (int) is_on, (Timestamp) last_update);
            return true;
        } else {
            System.out.println("it class was  wrong !   ---------------------");
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

    public List<Map<String, Object>> getAllRoomState() {
        Map<String, Object> map = stateRepository.getAllRoomState();
        List<Map<String, Object>> remap = new ArrayList<>();
        if (map != null && !map.isEmpty()) {
            for (Map.Entry<String, Object> i : map.entrySet()) {
                Map<String, Object> t = new HashMap<>();
                Object p = i.getValue();
                String o = p.toString();
                StateEntity entity = getStateEntity(o);
                t.put("room", i.getKey());
                t.put("temperature", entity.getTemperature());
                t.put("wind_speed", entity.getWind_speed());
                t.put("is_on", entity.getIs_on());
                t.put("last_update", entity.getLast_update());
                remap.add(t);
            }
        }
        return remap;
    }

    private static StateEntity getStateEntity(String o) {
        String c = o.substring(1, o.length() - 1);
        StateEntity entity = new StateEntity();
        String[] sps = c.split(", ");
        for (int j = 0; j < sps.length; j++) {
            String[] sp = sps[j].split("=");
            switch (sp[0]) {
                case "last_update" -> entity.setLast_update(new Timestamp(Long.parseLong(sp[1])));
                case "temperature" -> entity.setTemperature(Double.parseDouble(sp[1]));
                case "is_on" -> entity.setIs_on(Integer.parseInt(sp[1]));
                case "wind_speed" -> entity.setWind_speed(Integer.parseInt(sp[1]));
            }
        }
        return entity;
    }
}
