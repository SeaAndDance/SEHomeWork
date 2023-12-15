package io.github.FlyingASea;

import io.github.FlyingASea.dao.DeviceMapper;
import io.github.FlyingASea.entity.DeviceEntity;
import io.github.FlyingASea.post.RoomPost;
import io.github.FlyingASea.util.RandomUtils;
import io.github.FlyingASea.util.SHA256withRSAUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Client {
    private static final String addDeviceURL = "http://localhost:9999/api/admin/device";

    private static final String CheckInURL = "http://localhost:9999/api/room/check_in";

    public static final String schemaURL = "http://localhost:9999/api/device/client/";

    public static final String portURL = "http://localhost:9999/api/device/client";

    private static final Map<String, String> Client_1_keys = new HashMap<>();

    public static void main(String[] args) throws IOException, InterruptedException {

        for (int i = 1; i < 6; i++) {
            Client_1_keys.putAll(Server.keys);
            RoomPost.ClientControl(Map.of(
                    "room", String.valueOf(i),
                    "public_key", Client_1_keys.get("publicKey")
            ), addDeviceURL, "PUT");
        }

        for (int i = 1; i < 6; i++) {
            RoomPost.ClientControl(Map.of(
                    "room", String.valueOf(i)
            ), CheckInURL, "POST");
        }

        for (int i = 1; i < 6; i++) {
            int port = 10000 + i;
            String unique_id = RandomUtils.Random();
            String signature = SHA256withRSAUtil.sign(Client_1_keys.get("privateKey"), i + unique_id + port);
            RoomPost.ClientControl(Map.of(
                    "room_id", String.valueOf(i),
                    "port", String.valueOf(port),
                    "unique_id", unique_id,
                    "signature", signature
            ), portURL, "POST");
        }

        for (int i = 0; i < 26; i++) {
            switch (i) {
                case 0 -> RoomPost.SchemaControl(1, "start", 26);

                case 1 -> {
                    RoomPost.SchemaControl(1, "temperature", 18);
                    RoomPost.SchemaControl(2, "start", 26);
                    RoomPost.SchemaControl(5, "start", 26);
                }
                case 2 -> RoomPost.SchemaControl(3, "start", 30);
                case 3 -> {
                    RoomPost.SchemaControl(4, "start", 30);
                    RoomPost.SchemaControl(2, "temperature", 19);
                }
                case 4 -> RoomPost.SchemaControl(5, "temperature", 22);
                case 5 -> RoomPost.SchemaControl(1, "wind_speed", 3);
                case 6, 16 -> RoomPost.SchemaControl(2, "stop", 0);
                case 7 -> {
                    RoomPost.SchemaControl(2, "start", 1);
                    RoomPost.SchemaControl(5, "wind_speed", 3);
                }
                case 9 -> {
                    RoomPost.SchemaControl(1, "temperature", 22);
                    RoomPost.SchemaControl(4, "temperature", 18);
                    RoomPost.SchemaControl(4, "wind_speed", 3);
                }
                case 11 -> RoomPost.SchemaControl(2, "temperature", 22);
                case 12 -> RoomPost.SchemaControl(5, "wind_speed", 1);
                case 14 -> {
                    RoomPost.SchemaControl(1, "stop", 0);
                    RoomPost.SchemaControl(3, "temperature", 24);
                    RoomPost.SchemaControl(3, "wind_speed", 1);
                }
                case 15 -> {
                    RoomPost.SchemaControl(5, "temperature", 20);
                    RoomPost.SchemaControl(5, "wind_speed", 3);
                }
                case 17 -> RoomPost.SchemaControl(3, "wind_speed", 3);
                case 18 -> {
                    RoomPost.SchemaControl(1, "start", 22);
                    RoomPost.SchemaControl(4, "wind_speed", 2);
                    RoomPost.SchemaControl(4, "temperature", 20);
                }
                case 19 -> RoomPost.SchemaControl(2, "start", 22);
                case 20 -> RoomPost.SchemaControl(5, "temperature", 25);
                case 22 -> RoomPost.SchemaControl(3, "end", 22);
                case 23 -> RoomPost.SchemaControl(5, "end", 22);
                case 24 -> RoomPost.SchemaControl(1, "end", 22);
                case 25 -> {
                    RoomPost.SchemaControl(2, "end", 22);
                    RoomPost.SchemaControl(4, "end", 22);
                }
            }
            Thread.sleep(1000 * 10);
        }


    }
}
