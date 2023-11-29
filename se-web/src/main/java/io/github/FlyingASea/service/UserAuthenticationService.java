package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.UserMapper;
import io.github.FlyingASea.entity.UserEntity;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("UserAuthenticationService")
public class UserAuthenticationService {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userRepository;

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(10);

    public boolean isInvalidID(String id) {
        return !id.matches("[a-zA-Z_][a-zA-Z0-9_]{2,15}");
    }

    public boolean isInvalidPassword(String password) {
        return password.length() < 6 || password.length() > 30;
    }

    public boolean isWrongPassword(String id, String password) {
        UserEntity entity = userService.getUser(id);
        return entity == null || !PASSWORD_ENCODER.matches(password, entity.getPassword());
    }

    public String passwordEncoded(String password) {
        return PASSWORD_ENCODER.encode(password);
    }

    public void newUser(int id, String name, String password) {
        userRepository.createUser(id, name, passwordEncoded(password));
    }

}
