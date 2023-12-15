package io.github.FlyingASea.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Timestamp;
import java.util.Map;

@Data
@Accessors(chain = true)
public class TaskEntity {
    public String id;
    public long startTime;
    public double aimT;
    public double nowT;
    public int speed;
    public double roomTemperature;
    public int is_on;

    public Timestamp last_update;

    public TaskEntity(String id, double aimT, double nowT, int speed, int is_on, double roomTemperature) {
        this.startTime = System.currentTimeMillis();
        this.aimT = aimT;
        this.speed = speed;
        this.nowT = nowT;
        this.is_on = is_on;
        this.id = id;
        this.roomTemperature = roomTemperature;
        this.last_update = new Timestamp(System.currentTimeMillis());
    }
    public TaskEntity(TaskEntity entity){
        this.startTime = System.currentTimeMillis();
        this.aimT = entity.aimT;
        this.speed = entity.getSpeed();
        this.nowT = entity.nowT;
        this.is_on = entity.is_on;
        this.id = entity.id;
        this.roomTemperature = entity.roomTemperature;
        this.last_update = new Timestamp(System.currentTimeMillis());
    }


    public int compareTo(TaskEntity o) {
        double get = this.is_on * (this.speed * 100000L + this.nowT * 10L);
        double o_get = o.is_on * (o.speed * 100000L + o.nowT * 10L);
        return Double.compare(get, o_get);
    }

    public double count() {
        return this.is_on * (this.speed * 100000L + this.nowT * 10L);
    }

    public int reverseCompare(TaskEntity o) {
        double get = this.is_on * (this.speed * 100000L + this.nowT * 10L);
        double o_get = o.is_on * (o.speed * 100000L + o.nowT * 10L);
        return Double.compare(o_get, get);
    }

    @Override
    public boolean equals(Object task) {
        if (this == task)
            return true;
        if (task instanceof TaskEntity)
            return ((TaskEntity) task).is_on == this.is_on && ((TaskEntity) task).speed == this.speed && ((TaskEntity) task).aimT == this.aimT && ((TaskEntity) task).nowT == this.nowT;
        return false;
    }

}
