package io.github.FlyingASea.post;

import io.github.FlyingASea.entity.TaskEntity;
import io.github.FlyingASea.service.DataAndStateService;
import io.github.FlyingASea.service.RoomService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

@Component
public class Schema {
    public static Queue<TaskEntity> waitingQueue = new PriorityQueue<>(Comparator.comparing(TaskEntity::getStartTime));

    public static Queue<TaskEntity> receiveQueue = new PriorityQueue<>(Comparator.comparing(TaskEntity::getStartTime));
    public static Queue<TaskEntity> serviceQueue = new PriorityQueue<>(Comparator.comparing(TaskEntity::getStartTime));

    @Resource
    private DataAndStateService dataAndStateService;

    @Resource
    private RoomService roomService;

    private final String URLHEAD = "http://localhost:";

    private final String URLROUTER = "/api/control";

    @Scheduled(cron = "0/10 * * * * ?")
    public void schema() throws IOException {
        waitingQueue.addAll(receiveQueue);
        if (!serviceQueue.isEmpty()) {
            if (!waitingQueue.isEmpty()) {
                TaskEntity longestTask = serviceQueue.peek();
                long elapsedTime = System.currentTimeMillis() - longestTask.getStartTime();
                if (elapsedTime >= 10 * 1000) {
                    TaskEntity task = serviceQueue.poll();
                    if (task != null) {
                        if (dataAndStateService.changeSome("stop", task.getId(), "0"))
                            RoomPost.ClientControl(Map.of(
                                    "operation", "stop",
                                    "data", "0"
                            ), URLHEAD + roomService.getPort(task.getId()) + URLROUTER);
                        task.setStartTime(System.currentTimeMillis());
                        waitingQueue.offer(task);
                    }
                }
            }
        } else {
            if (!waitingQueue.isEmpty()) {
                TaskEntity task = waitingQueue.poll();
                if (task != null) {
                    if (dataAndStateService.changeSome(task.getData().get("operation"), task.getId(), task.getData().get("data")))
                        RoomPost.ClientControl(task.getData(), URLHEAD + roomService.getPort(task.getId()) + URLROUTER);
                    task.setStartTime(System.currentTimeMillis());
                    serviceQueue.add(task);
                }
            }
        }
    }
}
