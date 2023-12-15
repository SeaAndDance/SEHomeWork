package io.github.FlyingASea.post;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.FlyingASea.util.WebUtil;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.Map;

public class RoomPost {
    public static int ClientControl(Map<String, String> data, String URL) throws IOException {
        HttpPost httpPost = new HttpPost(URL);
        httpPost.setEntity(new StringEntity(new Gson().toJson(data), ContentType.APPLICATION_JSON));
        if (WebUtil.fetchDataInJson(httpPost) == null) {
            return -1;
        }
        JsonObject body = WebUtil.fetchDataInJson(httpPost).getAsJsonObject();
        if (body.getAsJsonPrimitive("errorCode") == null)
            return -1;
        else {
            return body.getAsJsonPrimitive("errorCode").getAsInt();
        }
    }

}
