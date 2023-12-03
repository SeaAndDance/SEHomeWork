package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.UserMapper;
import io.github.FlyingASea.entity.UserEntity;
import io.github.FlyingASea.util.Pair;
import io.github.FlyingASea.util.RandomUtils;
import jakarta.annotation.Resource;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service("UserAuthenticationService")
public class UserAuthenticationService {

    @Resource
    private UserService userService;

    @Resource
    private UserMapper userRepository;

    @Cacheable(key = "#id + ':'", value = "userSession#604800")
    public String createOrGetSession(String id) {
        return RandomUtils.randomString();
    }

    @CachePut(key = "#id + ':'", value = "userSession#604800")
    public String createSession(String id) {
        return RandomUtils.randomString();
    }

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

    public void newUser(String id, String name, String password) {
        userRepository.createUser(id, name, passwordEncoded(password));
    }

    public boolean changePassword(UserEntity entity, String password) {
        if (isInvalidPassword(password))
            return false;
        String newPass = passwordEncoded(password);
        userRepository.modifyPassword(entity.getId(), newPass);
        entity.setPassword(newPass);
        return true;
    }
}
