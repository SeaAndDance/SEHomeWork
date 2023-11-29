package io.github.FlyingASea.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class RoomData implements Serializable {
    private String id;
    private int temperature;
    private int wind_speed;
    private int is_on;
    private Timestamp last_update;
}
