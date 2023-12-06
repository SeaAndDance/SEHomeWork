package io.github.FlyingASea.controller;

import io.github.FlyingASea.entity.TaskEntity;
import io.github.FlyingASea.post.Schema;
import io.github.FlyingASea.result.ApiException;
import io.github.FlyingASea.result.Errors;
import io.github.FlyingASea.service.DataAndStateService;
import io.github.FlyingASea.service.RoomService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/device")
public class DeviceController {

    @Resource
    private RoomService roomService;

    @Resource
    private DataAndStateService dataAndStateService;

    @Resource
    private Schema schema;

    @PostMapping("/client")
    public ResponseEntity<Map<String, Object>> newUser(@RequestBody Map<String, String> data, HttpServletResponse response) {
        String room = data.get("room_id");
        String port = data.get("port");
        String unique_id = data.get("unique_id");
        String signature = data.get("signature");
        if (room == null || port == null || unique_id == null || signature == null) {
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        }
        if (roomService.getRoom(room) == null) {
            throw new ApiException(Errors.USER_ALREADY_EXIST);
        }
        if (roomService.updateRoomPort(room, port, unique_id, signature))
            return ResponseEntity.ok().build();
        else
            throw new ApiException(Errors.UNKNOWN_ERROR);
    }

    @PostMapping("/client/{room_id}")
    public ResponseEntity<Map<String, Object>> newUser(@RequestBody Map<String, String> body, HttpServletResponse response
            , @PathVariable("room_id") String id) {
        String operation = body.get("operation");
        String data = body.get("data");
        String time = body.get("time");
        String unique_id = body.get("unique_id");
        String signature = body.get("signature");
        if (operation == null || data == null || time == null ||
                unique_id == null || signature == null || id == null) {
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        }
        if (roomService.getRoom(id) == null) {
            throw new ApiException(Errors.USER_ALREADY_EXIST);
        }

        body.put("id", id);

        if (!roomService.checkSign(body))
            throw new ApiException(Errors.USER_ALREADY_EXIST);


        Schema.receiveQueue.add(new TaskEntity(id, Map.of(
                "operation", operation,
                "data", data
                )));
        return ResponseEntity.ok().build();

    }
}
