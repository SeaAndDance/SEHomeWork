package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.UserMapper;
import io.github.FlyingASea.entity.UserEntity;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserService {
    @Resource
    private UserMapper userRepository;

    public UserEntity getUser(String id) {
        return userRepository.findUserById(id);
    }

    public void changeName(UserEntity entity, String name) {
        userRepository.modifyName(entity.getId(), name);
        entity.setName(name);
    }

    public void changePassword(UserEntity entity, String password){

    }
}
