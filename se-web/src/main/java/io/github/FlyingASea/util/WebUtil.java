package io.github.FlyingASea.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Random;

public class WebUtil {

    public static final String[] VIEWER_USER_AGENTS = new String[]{
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:99.0) Gecko/20100101 Firefox/99.0",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36 Edg/96.0.1054.62"
    };

    private static final Random UA_RANDOM = new Random();

    public static String chooseRandomUA() {
        return VIEWER_USER_AGENTS[UA_RANDOM.nextInt(VIEWER_USER_AGENTS.length)];
    }

    public static JsonElement fetchDataInJson(HttpUriRequest request) throws IOException {
        return fetchDataInJson(request, chooseRandomUA());
    }

    public static JsonElement fetchDataInJson(HttpUriRequest request, String UA) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .disableCookieManagement()
                .setUserAgent(UA).build();
        CloseableHttpResponse response = httpClient.execute(request);
        if (response == null)
            return null;
        int status = response.getStatusLine().getStatusCode();
        if (status / 100 == 2)
            return null;
        else {
            HttpEntity httpEntity = response.getEntity();
            String json = EntityUtils.toString(httpEntity, "UTF-8");
            EntityUtils.consume(httpEntity);
            if (json == null || json.isEmpty())
                return null;
            try {
                return JsonParser.parseString(json);
            } catch (Exception e) {
                System.out.println(e);
                return null;
            }
        }
    }
}
