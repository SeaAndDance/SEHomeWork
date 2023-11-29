package io.github.FlyingASea.controller;

import io.github.FlyingASea.result.ApiException;
import io.github.FlyingASea.result.Errors;
import io.github.FlyingASea.service.UserAuthenticationService;
import io.github.FlyingASea.util.Pair;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.github.FlyingASea.service.UserService;

import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserAuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> newUser(@RequestBody Map<String, String> data) {
        String id = data.get("id");
        String password = data.get("password");
        String name = data.get("name");
        if (id == null || password == null || name == null)
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        if (authenticationService.isInvalidID(id))
            throw new ApiException(Errors.INVALID_ID);
        if (authenticationService.isInvalidPassword(password))
            throw new ApiException(Errors.INVALID_PASSWORD);
        if (userService.getUser(id) != null)
            throw new ApiException(Errors.USER_ALREADY_EXIST);
        return ResponseEntity.noContent().build();
    }





}
