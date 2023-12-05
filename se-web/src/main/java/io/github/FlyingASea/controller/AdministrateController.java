package io.github.FlyingASea.controller;

import io.github.FlyingASea.result.ApiException;
import io.github.FlyingASea.result.Errors;
import io.github.FlyingASea.service.AdministrateService;
import io.github.FlyingASea.service.DataAndStateService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/admin")
public class AdministrateController {

    @Resource
    private AdministrateService administrateService;

    @Resource
    private DataAndStateService dataAndStateService;

    @NeedAuthenticated
    @PutMapping("/device")
    public ResponseEntity<Map<String, Object>> addDevice(@RequestBody Map<String, String> data, HttpServletResponse response) {
        String room = data.get("room");
        String public_key = data.get("public_key");
        if (room == null || public_key == null)
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        if (administrateService.getRoom(room) != null)
            throw new ApiException(Errors.USER_ALREADY_EXIST);
        administrateService.newRoom(room, public_key);
        return ResponseEntity.ok(Map.of(
                "room", room
        ));
    }

    @NeedAuthenticated
    @PostMapping("/device")
    public ResponseEntity<Map<String, Object>> deleteDevice(@RequestBody Map<String, String> data, HttpServletResponse response) {
        String room = data.get("room");
        if (room == null)
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        if (administrateService.getRoom(room) == null)
            throw new ApiException(Errors.USER_ALREADY_EXIST);
        administrateService.delRoom(room);
        return ResponseEntity.ok(Map.of(
                "room", room
        ));
    }

    @NeedAuthenticated
    @GetMapping("/devices")
    public ResponseEntity<List<String>> getAllDevice() {
        return ResponseEntity.ok(administrateService.getAllRoom());
    }

    @NeedAuthenticated
    @PostMapping("/device/{room_id}")
    public ResponseEntity<Map<String, Object>> controlDevice(@RequestBody Map<String, String> data,
                                                             @PathVariable("room_id") String id) {
        String operation = data.get("operation");
        String num = data.get("data");
        if (id == null || operation == null || num == null)
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        if (administrateService.getRoom(id) == null)
            throw new ApiException(Errors.USER_ALREADY_EXIST);
        dataAndStateService.changeSome(operation, id, num);
        return ResponseEntity.ok(Map.of(
                "room", id
        ));
    }
}