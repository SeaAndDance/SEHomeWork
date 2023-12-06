package io.github.FlyingASea.post;


import com.google.gson.JsonObject;
import io.github.FlyingASea.util.WebUtil;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomPost {
    public static int ClientControl(Map<String, String> data, String URL) throws IOException {
        HttpPost httpPost = new HttpPost(URL);
        List<NameValuePair> paramsList = new ArrayList<>();
        for (Map.Entry<String, String> i : data.entrySet()) {
            paramsList.add(new BasicNameValuePair(i.getKey(), i.getValue()));
        }
        httpPost.setEntity(new UrlEncodedFormEntity(paramsList, StandardCharsets.UTF_8));
        JsonObject body = WebUtil.fetchDataInJson(httpPost).getAsJsonObject();
        if (body == null) {
            return -1;
        } else {
            return body.getAsJsonPrimitive("errorCode").getAsInt();
        }
    }

}
