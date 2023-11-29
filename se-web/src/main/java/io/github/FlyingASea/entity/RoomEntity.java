package io.github.FlyingASea.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RoomEntity {
    public static final RoomEntity ANONYMOUS = new RoomEntity();

    private String id;
    private String public_key;

    public boolean isAnonymous() {
        return this == ANONYMOUS;
    }
}
