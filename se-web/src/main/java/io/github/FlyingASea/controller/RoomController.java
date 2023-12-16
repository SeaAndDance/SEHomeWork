package io.github.FlyingASea.controller;

import io.github.FlyingASea.result.ApiException;
import io.github.FlyingASea.result.Errors;
import io.github.FlyingASea.service.DataAndStateService;
import io.github.FlyingASea.service.StateService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Resource
    private DataAndStateService dataAndStateService;

    @Resource
    private StateService stateService;

    private final double[] TEMPERATURE = {32, 28, 30, 29, 35};

    @NeedAuthenticated
    @PostMapping("/check_in")
    public ResponseEntity<Map<String, Object>> check_in(@RequestBody Map<String, String> data) {
        String id = data.get("room");
        if (id == null)
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        if (stateService.getState(id) != null)
            throw new ApiException(Errors.ROOM_ALREADY_EXIST);

        LocalDateTime dateTime = LocalDateTime.now();
        Timestamp last_update = Timestamp.valueOf(dateTime);
        int WIND_SPEED = 2;
        if (Integer.parseInt(id) - 1 < TEMPERATURE.length) {
            dataAndStateService.changeOrCreateStateAndData(Map.of(
                    "id", id,
                    "temperature", TEMPERATURE[Integer.parseInt(id) - 1],
                    "wind_speed", WIND_SPEED,
                    "is_on", 0,
                    "last_update", last_update,
                    "begin", last_update));
        } else {
            dataAndStateService.changeOrCreateStateAndData(Map.of(
                    "id", id,
                    "temperature", TEMPERATURE[new SecureRandom().nextInt(TEMPERATURE.length)],
                    "wind_speed", WIND_SPEED,
                    "is_on", 0,
                    "last_update", last_update,
                    "begin", last_update));
        }
        return ResponseEntity.ok(Map.of(
                "room", id
        ));
    }

    @NeedAuthenticated
    @PostMapping("/check_out")
    public ResponseEntity<Map<String, Object>> check_out(@RequestBody Map<String, String> data) {
        String id = data.get("room");
        if (id == null)
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        if (stateService.getState(id) == null)
            throw new ApiException(Errors.ROOM_IS_NOT_EXISTS);
        Map<String, Object> result = dataAndStateService.generateBill(id);
        return ResponseEntity.ok(result);
    }

    @NeedAuthenticated
    @PostMapping("/all")
    public ResponseEntity<Map<String, Object>> getHistory(@RequestBody Map<String, String> data) {

        String id = null;
        Timestamp timestamp = null;
        if (data != null) {
            id = data.get("room");
        }
        if (id == null) {
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        }
        if (stateService.getState(id) == null)
            throw new ApiException(Errors.ROOM_IS_NOT_EXISTS);
        if (data.get("begin") != null) {
            timestamp = Timestamp.valueOf(LocalDateTime.parse(data.get("begin"), DateTimeFormatter.ISO_DATE_TIME));
        }
        Map<String, Object> result = dataAndStateService.getHistory(id, timestamp);
        return ResponseEntity.ok(result);
    }

    @NeedAuthenticated
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllHistory() {
        Map<String, Object> result = dataAndStateService.getAllHistory();
        return ResponseEntity.ok(result);
    }

}
