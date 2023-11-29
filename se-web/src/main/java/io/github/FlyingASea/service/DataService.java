package io.github.FlyingASea.service;

import io.github.FlyingASea.dao.DataMapper;
import io.github.FlyingASea.entity.RoomData;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Map;

import static io.github.FlyingASea.util.SHA256withRSAUtil.verifySign;

@Service("DataService")
public class DataService {
    @Resource
    private DataMapper dataRepository;

    @Resource
    private RoomData roomData;

    @Resource
    private RoomService roomService;



}
