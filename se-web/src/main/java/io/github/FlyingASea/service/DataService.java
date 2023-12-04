package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.DataMapper;
import io.github.FlyingASea.entity.DataEntity;
import io.github.FlyingASea.entity.StateEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("DataService")
public class DataService {
    @Resource
    private DataMapper dataRepository;

    public DataEntity getData(String id) {
        return dataRepository.findDataById(id);
    }


    public void createState(String room, int temperature, int wind_speed, int is_on, Timestamp last_update) {
        dataRepository.createData(last_update, room, temperature, wind_speed, is_on);
    }

    public void createState(StateEntity stateEntity) {
        dataRepository.createData(stateEntity.getLast_update(), stateEntity.getId(), stateEntity.getTemperature()
                , stateEntity.getWind_speed(), stateEntity.getIs_on());
    }

    public boolean createState(Map<String, Object> data) {
        Object room = data.get("id");
        Object temperature = data.get("temperature");
        Object wind_speed = data.get("wind_speed");
        Object is_on = data.get("is_on");
        Object last_update = data.get("last_update");
        if (room == null || temperature == null || wind_speed == null || is_on == null || last_update == null)
            return false;
        if (room instanceof String && temperature instanceof Integer && wind_speed instanceof Integer &&
                is_on instanceof Integer && last_update instanceof Timestamp) {
            dataRepository.createData((Timestamp) last_update, (String) room,
                    (int) temperature, (int) wind_speed, (int) is_on);
            return true;
        } else {
            return false;
        }
    }

    public Map<String, Object> generateBill(String id, StateEntity now) {

        dataRepository.createData(now.getLast_update(), now.getId(),
                now.getTemperature(), now.getWind_speed(), now.getIs_on());

        DataEntity[] datas = dataRepository.selectDatasFromDate(id, now.getBegin());

        for (DataEntity i : datas) {
            System.out.println(i);
        }

        long total_cost = 0L, total_time = 0L;
        Map<String, Object> report = new HashMap<>();
        List<Map<String, Object>> items = new ArrayList<>();

        System.out.println(datas.length);

        for (int i = 0; i < datas.length - 1; i++) {
            Timestamp start = datas[i].getLast_update();
            Timestamp end = datas[i + 1].getLast_update();
            int temperature = datas[i].getTemperature();
            int wind_speed = datas[i].getWind_speed();
            int is_on = datas[i].getIs_on();

            long duration = Math.abs(Duration.between(end.toInstant(), start.toInstant()).getSeconds());
            long cost = 3 * temperature * wind_speed * is_on * duration / 60;

            total_time += duration;
            total_cost += cost;

            Map<String, Object> temp = new HashMap<>();
            temp.put("start_time", start);
            temp.put("end_time", end);
            temp.put("temperature", temperature);
            temp.put("wind_speed", wind_speed);
            temp.put("duration", duration);
            temp.put("cost", cost);

            items.add(temp);
        }

        report.put("total_cost", total_cost);
        report.put("total_time", total_time);
        report.put("details", items.toArray());
        return report;
    }

}
