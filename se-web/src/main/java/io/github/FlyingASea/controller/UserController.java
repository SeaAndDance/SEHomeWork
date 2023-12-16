package io.github.FlyingASea.controller;

import io.github.FlyingASea.result.ApiException;
import io.github.FlyingASea.result.Errors;
import io.github.FlyingASea.service.UserAuthenticationService;
import io.github.FlyingASea.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserAuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> newUser(@RequestBody Map<String, String> data, HttpServletResponse response) {
        String id = data.get("username");
        String name = data.get("name");
        String password = data.get("password");
        if (id == null || password == null || name == null) {
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        }
        if (authenticationService.isInvalidID(id)) {
            throw new ApiException(Errors.INVALID_ID);
        }
        if (authenticationService.isInvalidPassword(password)) {
            throw new ApiException(Errors.INVALID_PASSWORD);
        }
        if (userService.getUser(id) != null) {
            throw new ApiException(Errors.USER_ALREADY_EXIST);
        }
        authenticationService.newUser(id, name, password);
        String session = authenticationService.createSession(id);
        Cookie cookie = new Cookie("session", session);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        return ResponseEntity.ok().build();
    }




    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> data, HttpServletResponse response) {
        String id = data.get("username");
            String password = data.get("password");
        if (id == null || password == null)
            throw new ApiException(Errors.INVALID_DATA_FORMAT);
        if (authenticationService.isInvalidID(id))
            throw new ApiException(Errors.INVALID_ID);
        if (authenticationService.isInvalidPassword(password))
            throw new ApiException(Errors.INVALID_PASSWORD);
        if (userService.getUser(id) == null)
            throw new ApiException(Errors.USER_ALREADY_EXIST);

        String session = authenticationService.createSession(id);
        Cookie cookie = new Cookie("session", session);
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok(Map.of(
                "username", id
        ));

    }

    @NeedAuthenticated
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody Map<String, String> data, HttpServletResponse response) {
        authenticationService.dropToken(data.get("id"));
        Cookie cookie = new Cookie("session", "");
        cookie.setMaxAge(0);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }

}
