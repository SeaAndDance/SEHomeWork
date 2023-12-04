package io.github.FlyingASea.controller;

import io.github.FlyingASea.result.ApiException;
import io.github.FlyingASea.result.Errors;
import io.github.FlyingASea.service.DataAndStateService;
import io.github.FlyingASea.service.StateService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/room")
public class RoomController {

    @Resource
    private DataAndStateService dataAndStateService;

    @Resource
    private StateService stateService;

    private final int TEMPERATURE = 25;

    private final int WIND_SPEED = 2;

    @PostMapping("/check_in")
    public ResponseEntity<Map<String, Object>> check_in(@RequestBody Map<String, String> data, HttpServletResponse response) {
        String id = data.get("room");
        if (id == null)
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        if (stateService.getState(id) != null)
            throw new ApiException(Errors.USER_ALREADY_EXIST);
        LocalDateTime dateTime = LocalDateTime.now();
        Timestamp last_update = Timestamp.valueOf(dateTime);
        stateService.createState(id, TEMPERATURE, WIND_SPEED, 0, last_update, last_update);
        return ResponseEntity.ok(Map.of(
                "room", id
        ));
    }

    @PostMapping("/check_out")
    public ResponseEntity<Map<String, Object>> check_out(@RequestBody Map<String, String> data, HttpServletResponse response) {
        String id = data.get("room");
        if (id == null)
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        if (stateService.getState(id) == null)
            throw new ApiException(Errors.USER_ALREADY_EXIST);
        Map<String, Object> result = dataAndStateService.generateBill(id);
        return ResponseEntity.ok(result);
    }
}
