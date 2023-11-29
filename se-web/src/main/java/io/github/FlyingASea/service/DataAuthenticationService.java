package io.github.FlyingASea.service;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static io.github.FlyingASea.util.SHA256withRSAUtil.verifySign;

@Service("DataAuthenticationService")
public class DataAuthenticationService {
    @Resource
    private RoomService roomService;

    public boolean certificateData(Map<String, String> data, String signature) {
        String public_key = roomService.getRoom(data.get("room")).getPublic_key();
        String sign_text = data.get("room") + data.get("operation") + data.get("time") + data.get("unique_id");
        return verifySign(public_key, sign_text, signature);
    }

    public static Map<String, String> EntryToData(Map<String, String> entry) {
        String operation = entry.get("operation");
        if (operation == null) {
            return null;
        }
        Map<String, String> tempData = new HashMap<>();
        tempData.put("id", entry.get("room"));
        tempData.put("temperature", null);
        tempData.put("wind_speed", null);
        tempData.put("is_on", null);
        tempData.put("last_update", entry.get("time"));
        String[] datas = operation.split("_");

        switch (datas[0]) {
            case "start":
                tempData.put("temperature", datas[1]);
                tempData.put("wind_speed", datas[2]);
                tempData.put("is_on", "1");
            case "temperature":
                tempData.put("temperature", datas[1]);
            case "wind":
                tempData.put("wind_speed", datas[2]);
            case "stop":
                tempData.put("is_on", "0");
        }

        return tempData;

    }
}
