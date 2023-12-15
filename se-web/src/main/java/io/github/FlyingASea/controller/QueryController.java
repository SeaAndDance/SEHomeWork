package io.github.FlyingASea.controller;


import io.github.FlyingASea.entity.StateEntity;
import io.github.FlyingASea.entity.TaskEntity;
import io.github.FlyingASea.post.Schema;
import io.github.FlyingASea.result.ApiException;
import io.github.FlyingASea.result.Errors;
import io.github.FlyingASea.service.StateService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/status")
public class QueryController {
    @Resource
    private StateService stateService;

    @NeedAuthenticated
    @GetMapping("/")
    public ResponseEntity<List<Map<String, Object>>> getDeviceShort() {
        return ResponseEntity.ok(stateService.getAllState());
    }

    @NeedAuthenticated
    @GetMapping("/{room_id}")
    public ResponseEntity<Map<String, Object>> getADevice(@PathVariable("room_id") String id) {
        StateEntity room = stateService.getState(id);
        if (room == null)
            throw new ApiException(Errors.WRONG_USER_ID);
        if (!Schema.map.isEmpty() && Schema.map.get(id) != null) {
            TaskEntity state = Schema.map.get(id);
            return ResponseEntity.ok(Map.of(
                    "room", state.getId(),
                    "temperature", state.nowT,
                    "wind_speed", state.speed,
                    "is_on", state.getIs_on() == 1,
                    "last_update", state.last_update
            ));
        }
        return ResponseEntity.ok(Map.of(
                "room", room.getId(),
                "temperature", room.getTemperature(),
                "wind_speed", room.getWind_speed(),
                "is_on", room.getIs_on() == 1,
                "last_update", room.getLast_update()
        ));
    }

    @NeedAuthenticated
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllState() {
        return ResponseEntity.ok(stateService.getAllRoomState());
    }
}
