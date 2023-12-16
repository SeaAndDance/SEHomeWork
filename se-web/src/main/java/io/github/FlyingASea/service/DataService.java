package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.DataMapper;
import io.github.FlyingASea.entity.DataEntity;
import io.github.FlyingASea.entity.StateEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public DataEntity[] getData(String id) {
        return dataRepository.findDatasById(id);
    }


    public void createState(String room, Double temperature, int wind_speed, int is_on, Timestamp last_update) {
        DataEntity entity = new DataEntity();
        entity.setRoom(room);
        entity.setTemperature(temperature);
        entity.setWind_speed(wind_speed);
        entity.setIs_on(is_on);
        entity.setLast_update(last_update);
        dataRepository.createData(entity);
    }

    public void createData(StateEntity now) {
        DataEntity entity = new DataEntity();
        entity.setRoom(now.getId());
        entity.setTemperature(now.getTemperature());
        entity.setWind_speed(now.getWind_speed());
        entity.setIs_on(now.getIs_on());
        entity.setLast_update(now.getLast_update());

        dataRepository.createData(entity);
    }

    public boolean createState(Map<String, Object> data) {
        Object room = data.get("id");
        Object temperature = data.get("temperature");
        Object wind_speed = data.get("wind_speed");
        Object is_on = data.get("is_on");
        Object last_update = data.get("last_update");
        if (room == null || temperature == null || wind_speed == null || is_on == null || last_update == null)
            return false;
        if (room instanceof String && temperature instanceof Double && wind_speed instanceof Integer &&
                is_on instanceof Integer && last_update instanceof Timestamp) {
            DataEntity entity = new DataEntity();
            entity.setRoom((String) room);
            entity.setTemperature((Double) temperature);
            entity.setWind_speed((Integer) wind_speed);
            entity.setIs_on((Integer) is_on);
            entity.setLast_update((Timestamp) last_update);
            dataRepository.createData(entity);
            return true;
        } else {
            return false;
        }
    }

    public Map<String, Object> generateBill(String id, StateEntity now) {

        createData(now);

        DataEntity[] datas = dataRepository.selectDatasFromDate(id, now.getBegin());
        double total_cost = 0, total_time = 0;
        Map<String, Object> report = new HashMap<>();
        List<Map<String, Object>> items = new ArrayList<>();

        for (int i = 0; i < datas.length - 1; i++) {

            Timestamp start = datas[i].getLast_update();
            Timestamp end = datas[i + 1].getLast_update();
            Double temperature = datas[i].getTemperature();
            int wind_speed = datas[i].getWind_speed();
            int is_on = datas[i].getIs_on();

            System.out.println(datas[i]);

            long duration = Math.abs(Duration.between(end.toInstant(), start.toInstant()).getSeconds());
            double cost = (double) (wind_speed * is_on * duration) / (30);

            System.out.println("cost is : " + cost);
            ;

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
