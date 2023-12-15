package io.github.FlyingASea.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class DeviceEntity {
    private String id;
    private String privateKey;
    private String port;
}