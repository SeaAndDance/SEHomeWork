package io.github.FlyingASea.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class StateEntity implements Serializable {
    private String id;
    private int temperature;
    private int wind_speed;
    private int is_on;
    private Timestamp last_update;
    private Timestamp begin;
}
