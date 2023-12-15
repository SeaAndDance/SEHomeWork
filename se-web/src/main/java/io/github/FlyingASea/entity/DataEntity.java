package io.github.FlyingASea.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DataEntity {
    private String room;
    private Double temperature;
    private int wind_speed;
    private int is_on;
    private Timestamp last_update;
}
