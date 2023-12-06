package io.github.FlyingASea.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@Accessors(chain = true)
public class TaskEntity {
    private String id;
    private Map<String,String> data;
    private long startTime;
    public TaskEntity(String id, Map<String,String> data){
        this.id = id;
        this.data = data;
        this.startTime = System.currentTimeMillis();
    }

}
