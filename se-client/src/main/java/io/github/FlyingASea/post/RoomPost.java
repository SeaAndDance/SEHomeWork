package io.github.FlyingASea.post;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.FlyingASea.Server;
import io.github.FlyingASea.util.RandomUtils;
import io.github.FlyingASea.util.WebUtil;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import io.github.FlyingASea.util.SHA256withRSAUtil;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

import static io.github.FlyingASea.Client.schemaURL;

public class RoomPost {
    public static void SchemaControl(int id, String operation, int data) throws IOException {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String unique_id = RandomUtils.Random();
        String signature = SHA256withRSAUtil.sign(Server.keys.get("privateKey"), operation + unique_id + data + timestamp);
        RoomPost.ClientControl(Map.of(
                "operation", operation,
                "data", String.valueOf(data),
                "time", String.valueOf(timestamp),
                "unique_id", unique_id,
                "signature", signature

        ), schemaURL + id, "POST");
    }

    public static void ClientControl(Map<String, String> data, String URL, String type) throws IOException {
        HttpUriRequest request;
        switch (type) {
            case "PUT" -> {
                request = new HttpPut(URL);
                handler(data, (HttpPut) request);
            }
            case "GET" -> {
                request = new HttpGet(URL);
                handler((HttpGet) request);
            }
            case "DELETE" -> {
                request = new HttpDelete(URL);
                handler((HttpDelete) request);
            }
            case "POST" -> {
                request = new HttpPost(URL);
                handler(data, (HttpPost) request);
            }
        }

    }

    public static void handler(HttpGet request) throws IOException {
        JsonObject body = WebUtil.fetchDataInJson(request).getAsJsonObject();
        if (body.getAsJsonPrimitive("errorCode") != null) {
            System.out.println(body);
        }
    }

    public static void handler(Map<String, String> data, HttpPut request) throws IOException {
        request.setEntity(new StringEntity(new Gson().toJson(data), ContentType.APPLICATION_JSON));
        JsonObject body = WebUtil.fetchDataInJson(request).getAsJsonObject();
        if (body.getAsJsonPrimitive("errorCode") != null) {
            System.out.println(body);
        }
    }

    public static void handler(Map<String, String> data, HttpPost request) throws IOException {
        request.setEntity(new StringEntity(new Gson().toJson(data), ContentType.APPLICATION_JSON));
        if (WebUtil.fetchDataInJson(request) != null) {
            JsonObject body = WebUtil.fetchDataInJson(request).getAsJsonObject();
            if (body.getAsJsonPrimitive("errorCode") != null) {
                System.out.println(body);
            }
        }
    }

    public static void handler(HttpDelete request) throws IOException {
        JsonObject body = WebUtil.fetchDataInJson(request).getAsJsonObject();
        if (body.getAsJsonPrimitive("errorCode") != null) {
            System.out.println(body);
        }
    }

}
