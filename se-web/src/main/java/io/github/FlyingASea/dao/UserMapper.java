package io.github.FlyingASea.dao;

import io.github.FlyingASea.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    UserEntity findUserById(@Param("id") String id);

    void createUser(@Param("id") int id, @Param("name") String name, @Param("password") String password);

    void modifyName(@Param("id") String id, @Param("name") String name);

    void modifyPassword(@Param("id") String id, @Param("password") String password);
}
