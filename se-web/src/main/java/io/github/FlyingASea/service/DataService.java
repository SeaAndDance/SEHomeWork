package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.DataMapper;
import io.github.FlyingASea.dao.StateMapper;
import io.github.FlyingASea.entity.DataEntity;
import io.github.FlyingASea.entity.StateEntity;
import io.github.FlyingASea.util.Pair;
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
        DataEntity dataEntity = new DataEntity();
        dataEntity.setRoom(room);
        dataEntity.setTemperature(temperature);
        dataEntity.setWind_speed(wind_speed);
        dataEntity.setIs_on(is_on);
        dataEntity.setLast_update(last_update);
        dataRepository.createData(dataEntity);
    }

    public void createState(StateEntity stateEntity) {
        DataEntity dataEntity = new DataEntity();
        dataEntity.setRoom(stateEntity.getId());
        dataEntity.setTemperature(stateEntity.getTemperature());
        dataEntity.setWind_speed(stateEntity.getWind_speed());
        dataEntity.setIs_on(stateEntity.getIs_on());
        dataEntity.setLast_update(stateEntity.getLast_update());
        dataRepository.createData(dataEntity);
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
            DataEntity dataEntity = new DataEntity();
            dataEntity.setRoom((String) room);
            dataEntity.setTemperature((int) temperature);
            dataEntity.setWind_speed((int) wind_speed);
            dataEntity.setIs_on((int) is_on);
            dataEntity.setLast_update((Timestamp) last_update);
            dataRepository.createData(dataEntity);
            return true;
        } else {
            return false;
        }
    }

    public Map<String, Object> generateBill(String id, Timestamp begin) {
        DataEntity[] datas = dataRepository.selectDatasFromDate(id, begin);
        long total_cost = 0L, total_time = 0L;
        Map<String, Object> report = new HashMap<>();
        List<Map<String, Object>> items = new ArrayList<>();

        for (int i = 0; i < datas.length - 1; i++) {
            Timestamp start = datas[i].getLast_update();
            Timestamp end = datas[i + 1].getLast_update();
            int temperature = datas[i].getTemperature();
            int wind_speed = datas[i].getWind_speed();
            int is_on = datas[i].getIs_on();

            long duration = Duration.between(start.toInstant(), end.toInstant()).getSeconds();
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
