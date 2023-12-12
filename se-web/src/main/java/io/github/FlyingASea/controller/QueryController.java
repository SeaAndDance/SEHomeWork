package io.github.FlyingASea.controller;


import io.github.FlyingASea.entity.StateEntity;
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
    public ResponseEntity<Map<String, Object>> deleteDevice(@RequestBody Map<String, String> data,
                                                            HttpServletResponse response,
                                                            @PathVariable("room_id") String id) {
        if (id == null)
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        StateEntity room = stateService.getState(id);
        if (room == null)
            throw new ApiException(Errors.USER_ALREADY_EXIST);

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
