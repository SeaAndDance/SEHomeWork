package io.github.FlyingASea.entity;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class UserEntity {

    public static final UserEntity ANONYMOUS = new UserEntity();

    private String id;
    private String name;
    private String password;

    public boolean isAnonymous() {
        return this == ANONYMOUS;
    }
}
