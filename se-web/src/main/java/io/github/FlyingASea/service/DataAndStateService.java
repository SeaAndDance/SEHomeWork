package io.github.FlyingASea.service;

import io.github.FlyingASea.entity.StateEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("DataAndStateService")
public class DataAndStateService {
    @Resource
    private DataService dataService;

    @Resource
    private StateService stateService;

    private Map<String, Object> convert(Map<String, String> data) {
        Map<String, Object> stateEntity = new HashMap<>();
        stateEntity.put("id", data.get("id"));
        stateEntity.put("temperature", Integer.parseInt(data.get("temperature")));
        stateEntity.put("wind_speed", Integer.parseInt(data.get("wind_speed")));
        stateEntity.put("is_on", Integer.parseInt(data.get("is_on")));
        stateEntity.put("last_update", new Timestamp(Date.from(Instant.parse(data.get("last_update"))).getTime()));
        return stateEntity;
    }

    public void changeSome(String type, String id, String data) {
        StateEntity past = stateService.getState(id);
        LocalDateTime dateTime = LocalDateTime.now();
        Timestamp last_update = Timestamp.valueOf(dateTime);
        stateService.changeState(type, data, past.getId(), last_update);
        dataService.createState(stateService.getState(id));
    }

    public void changeStateAndData(Map<String, String> data, String id) {
        StateEntity now = stateService.getState(id);
        Map<String, Object> temp_data = convert(data);

        if (now == null) {
            LocalDateTime dateTime = LocalDateTime.now();
            Timestamp last_update = Timestamp.valueOf(dateTime);
            stateService.createState(temp_data, last_update);
        } else {
            stateService.changeState(temp_data);
        }
        dataService.createState(temp_data);
    }

    public Map<String, Object> generateBill(String id) {
        StateEntity now = stateService.getState(id);
        return dataService.generateBill(id, now.getBegin());
    }

}
