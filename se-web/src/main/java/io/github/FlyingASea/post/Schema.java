package io.github.FlyingASea.post;

import io.github.FlyingASea.entity.StateEntity;
import io.github.FlyingASea.entity.TaskEntity;
import io.github.FlyingASea.service.DataAndStateService;
import io.github.FlyingASea.service.RoomService;
import io.github.FlyingASea.service.StateService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

@Component
public class Schema {
    public static Queue<TaskEntity> readyQueue = new PriorityQueue<>(Comparator.comparing(TaskEntity::getStartTime));
    public static Queue<TaskEntity> waitingQueue = new PriorityQueue<>(Comparator.comparing(TaskEntity::getStartTime));
    public static Queue<TaskEntity> serviceQueue = new PriorityQueue<>(Comparator.comparing(TaskEntity::getStartTime));
    public static Queue<TaskEntity> closeQueue = new LinkedList<>();
    public static Map<String, TaskEntity> map = new HashMap<>();
    public static Map<String, Queue<TaskEntity>> history = new HashMap<>();
    public static Map<String, Timestamp> START = new HashMap<>();
    public static Queue<String> endQueue = new ArrayDeque<>();
    @Resource
    private DataAndStateService dataAndStateServices;
    @Resource
    private StateService stateServices;

    @Resource
    private RoomService roomServices;

    private static DataAndStateService dataAndStateService;

    private static StateService stateService;

    private static RoomService roomService;

    @PostConstruct
    public void init() {
        dataAndStateService = dataAndStateServices;
        stateService = stateServices;
        roomService = roomServices;
    }


    private static final String URLHEAD = "http://localhost:";

    private static final String URLROUTER = "/api/control";

    public static void addToReadyQueue(String id, String operation, double data) {
        if (!map.isEmpty() && map.get(id) != null) {
            Handler(id, operation, data);
        } else {
            addToReadyQueue(newTask(id, operation, data));
        }
    }

    public static void addToReadyQueue(TaskEntity task) {
        if (task != null) {
            map.put(task.id, task);
            readyQueue.add(task);
        }
    }

    public static TaskEntity newTask(String id, String operation, double data) {
        StateEntity state = stateService.getState(id);

        TaskEntity entity = null;

        if (operation.equals("start")) {
            entity = new TaskEntity(id, data,
                    state.getTemperature(), state.getWind_speed(), 1, state.getTemperature());
            System.out.println("start" + entity);
            START.put(id, entity.last_update);
            dataAndStateService.changeOrCreateStateAndData(Map.of(
                    "id", entity.id,
                    "temperature", entity.nowT,
                    "wind_speed", entity.speed,
                    "is_on", 1,
                    "last_update", entity.last_update,
                    "begin", entity.last_update
            ));
        }

        return entity;
    }

    public static void Handler(String id, String operation, double data) {
        TaskEntity entity = map.get(id);
        switch (operation) {
            case "temperature" -> {
                entity.aimT = data;
                entity.last_update = new Timestamp(System.currentTimeMillis());

            }
            case "wind_speed" -> {
                entity.speed = (int) data;
                entity.last_update = new Timestamp(System.currentTimeMillis());
            }
            case "start" -> {
                entity.is_on = 1;
                entity.last_update = new Timestamp(System.currentTimeMillis());
            }
            case "stop" -> {
                entity.is_on = 0;
                entity.last_update = new Timestamp(System.currentTimeMillis());
            }
            case "end" -> {
                entity.is_on = 0;
                entity.last_update = new Timestamp(System.currentTimeMillis());
                System.out.println("come to end: " + entity);
                Queue<TaskEntity> entities = history.get(entity.id);
                endQueue.add(entity.id);
                if (entities != null) {
                    entities.add(entity);
                    System.out.println(" Tasks history: " + entities);
                    Timestamp start = new Timestamp(System.currentTimeMillis());

                    if (START.get(entity.id) != null) {
                        start = START.get(entity.id);
                    }

                    for (TaskEntity i : entities) {
                        System.out.println("will to change the Task" + i);

                        dataAndStateService.changeOrCreateStateAndData(Map.of(
                                "id", i.id,
                                "temperature", i.nowT,
                                "wind_speed", i.speed,
                                "is_on", i.is_on,
                                "last_update", i.last_update,
                                "begin", start
                        ));
                    }
                }
            }
        }
    }

    public static void print() {
        System.out.println("readyQueue: ");
        for (TaskEntity i : readyQueue)
            System.out.println("id: " + i.id + " nowT: " + i.nowT + " aimT: " + i.aimT
                    + " speed: " + i.speed + " is_on: " + i.is_on + " count: " + i.count()
                    + " roomTemperature " + i.roomTemperature
            );

        System.out.println("\nwaitingQueue: ");
        for (TaskEntity i : waitingQueue) {
            System.out.println("id: " + i.id + " nowT: " + i.nowT + " aimT: " + i.aimT
                    + " speed: " + i.speed + " is_on: " + i.is_on + " count: " + i.count()
                    + " roomTemperature " + i.roomTemperature
            );
        }
        System.out.println("\nserviceQueue: ");
        for (TaskEntity i : serviceQueue)
            System.out.println("id: " + i.id + " nowT: " + i.nowT + " aimT: " + i.aimT
                    + " speed: " + i.speed + " is_on: " + i.is_on + " count: " + i.count()
                    + " roomTemperature " + i.roomTemperature
            );
        System.out.println("\ncloseQueue: ");
        for (TaskEntity i : closeQueue)
            System.out.println("id: " + i.id + " nowT: " + i.nowT + " aimT: " + i.aimT
                    + " speed: " + i.speed + " is_on: " + i.is_on + " count: " + i.count()
                    + " roomTemperature " + i.roomTemperature
            );
        System.out.print("\n");
    }


    public void judge() {
        handleClose();
        handleReady();
        handleWait();
        handleService();
    }

    private void handleService() {
        List<TaskEntity> serviceExit = new ArrayList<>();
        if (!serviceQueue.isEmpty()) {
            for (TaskEntity i : serviceQueue) {
                if (i.is_on == 0) {
                    serviceExit.add(i);
                    i.startTime = System.currentTimeMillis();
                    closeQueue.add(i);
                }

                if (i.nowT > i.aimT) {
                    if (i.nowT - 0.5 < i.aimT) {
                        i.nowT = i.aimT;
                    } else {
                        i.nowT -= 0.5;
                    }
                }

                addToHistory(i);
            }
        }

        if (!serviceExit.isEmpty())
            for (TaskEntity i : serviceExit)
                serviceQueue.remove(i);
    }

    private void handleWait() {
        List<TaskEntity> waitingExit = new ArrayList<>();

        if (!waitingQueue.isEmpty()) {
            for (TaskEntity i : waitingQueue) {
                if (i.is_on == 0) {
                    waitingExit.add(i);
                    i.startTime = System.currentTimeMillis();
                    closeQueue.add(i);

                }

                if (i.nowT < i.roomTemperature) {
                    if (i.nowT + 0.5 > i.roomTemperature) {
                        i.nowT = i.roomTemperature;
                    } else {
                        i.nowT += 0.5;
                    }
                }

                addToHistory(i);
            }
        }

        if (!waitingExit.isEmpty())
            for (TaskEntity i : waitingExit)
                waitingQueue.remove(i);
    }

    private void handleReady() {
        List<TaskEntity> readyExit = new ArrayList<>();

        if (!readyQueue.isEmpty()) {
            for (TaskEntity i : readyQueue) {
                if (i.is_on == 0) {
                    readyExit.add(i);
                    i.startTime = System.currentTimeMillis();
                    closeQueue.add(i);
                }

                if (i.nowT < i.roomTemperature) {
                    if (i.nowT + 0.5 > i.roomTemperature) {
                        i.nowT = i.roomTemperature;
                    } else {
                        i.nowT += 0.5;
                    }
                    addToHistory(i);
                }
            }
        }
        if (!readyExit.isEmpty())
            for (TaskEntity i : readyExit)
                readyQueue.remove(i);
    }

    private void handleClose() {
        List<TaskEntity> exit = new ArrayList<>();
        for (TaskEntity i : closeQueue) {
            if (i.is_on == 1) {
                exit.add(i);
                i.startTime = System.currentTimeMillis();
                readyQueue.add(i);
            }
        }

        if (!exit.isEmpty())
            for (TaskEntity i : exit)
                closeQueue.remove(i);

        if (!closeQueue.isEmpty()) {
            for (TaskEntity i : closeQueue) {
                if (i.nowT < i.roomTemperature) {
                    if (i.nowT + 0.5 > i.roomTemperature) {
                        i.nowT = i.roomTemperature;
                    } else {
                        i.nowT += 0.5;
                    }
                }
                addToHistory(i);
            }
        }
    }

    private void addToHistory(TaskEntity i) {
        if (history.get(i.id) != null) {
            history.get(i.id).add(new TaskEntity(i));
        } else {
            Queue<TaskEntity> list = new PriorityQueue<>(TaskEntity::last);
            list.add(i);
            history.put(i.id, list);
        }
    }

    public static void post() throws IOException {
        for (Map.Entry<String, TaskEntity> i : map.entrySet()) {
            TaskEntity state = i.getValue();
            String port = roomService.getPort(state.id);
            int code = RoomPost.ClientControl(Map.of(
                    "room", state.getId(),
                    "temperature", String.valueOf(state.nowT),
                    "wind_speed", String.valueOf(state.speed),
                    "is_on", String.valueOf(state.getIs_on() == 1),
                    "last_update", String.valueOf(state.last_update)
            ), URLHEAD + port + URLROUTER);
            if (code != -1)
                throw new IOException("errorCode: " + code);
        }
    }

    public static void postBill() throws IOException {
        for (TaskEntity i : closeQueue) {
            String port = roomService.getPort(i.id);
            Map<String, Object> result = dataAndStateService.generateBill(i.id);
            int code = RoomPost.ClientControl(result, URLHEAD + port + URLROUTER);
            if (code != -1)
                throw new IOException("errorCode: " + code);
        }
    }

    @Scheduled(cron = "0/10 * * * * ?")
    public void schema() throws IOException {
        if (!readyQueue.isEmpty() || !waitingQueue.isEmpty() || !serviceQueue.isEmpty()) {
            judge();
            if (!readyQueue.isEmpty()) {
                readyToWait();
            }
            if (!waitingQueue.isEmpty()) {
                waitToService();
            } else {
                fillService();
            }
            addToDB();
            post();
            print();
        } else {
            if (closeQueue.size() == 5) {
                addToDB();
                closeQueue.clear();
            }
        }
    }

    private void addToDB() {
        if (!history.isEmpty()) {
            for (Map.Entry<String, Queue<TaskEntity>> i : history.entrySet()) {
                if (!i.getValue().isEmpty()) {
                    for (TaskEntity j : i.getValue()) {
                        System.out.println("Tasks history: " + j);
                        Timestamp start = new Timestamp(System.currentTimeMillis());

                        if (START.get(j.id) != null) {
                            start = START.get(j.id);
                        }

                        System.out.println("will to change the Task" + i);

                        dataAndStateService.changeOrCreateStateAndData(Map.of(
                                "id", j.id,
                                "temperature", j.nowT,
                                "wind_speed", j.speed,
                                "is_on", j.is_on,
                                "last_update", j.last_update,
                                "begin", start
                        ));
                    }
                    i.getValue().clear();
                }
            }
        }
    }

    private static void readyToWait() {
        TaskEntity longestTask = readyQueue.peek();
        long elapsedTime = 0;
        if (longestTask != null) {
            elapsedTime = System.currentTimeMillis() - longestTask.getStartTime();
        }
        if (elapsedTime >= 10 * 1000) {
            TaskEntity task = readyQueue.poll();
            waitingQueue.offer(task);
            if (task != null) {
                System.out.println("Task scheduled from ready queue to waiting queue: " + task.id);
            }
        }
    }

    private static void waitToService() {
        TaskEntity first = waitingQueue.peek();
        if (serviceQueue.size() == 3) {
            TaskEntity last = serviceQueue.peek();
            if (first != null) {
                System.out.println("first id: " + first.id + " count " + first.count());
            }
            System.out.println("last id: " + last.id + " count " + last.count());
            if (first != null && first.compareTo(last) >= 0) {
                last.startTime = System.currentTimeMillis();
                readyQueue.add(serviceQueue.poll());
                first.startTime = System.currentTimeMillis();
                serviceQueue.add(waitingQueue.poll());
            }
        } else {
            if (first != null) {
                first.startTime = System.currentTimeMillis();
                serviceQueue.add(waitingQueue.poll());
            }
        }
    }

    private static void fillService() {
        if (serviceQueue.size() < 3) {
            int size = 3 - serviceQueue.size();
            for (int i = 0; i < size; i++) {
                TaskEntity last = readyQueue.poll();
                if (last != null) {
                    last.startTime = System.currentTimeMillis();
                    serviceQueue.add(last);
                }
            }
        }
    }


}
