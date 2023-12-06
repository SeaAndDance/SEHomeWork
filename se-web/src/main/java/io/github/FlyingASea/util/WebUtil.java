package io.github.FlyingASea.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.hc.client5.http.classic.methods.HttpUriRequest;
import org.apache.hc.client5.http.impl.classic.BasicHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.protocol.HttpContext;

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
        return HttpClientBuilder.create()
                .disableCookieManagement()
                .setUserAgent(UA).build().execute(request, response -> {
                    HttpEntity httpEntity = response.getEntity();
                    String json = EntityUtils.toString(httpEntity, "UTF-8");
                    EntityUtils.consume(httpEntity);
                    return JsonParser.parseString(json);
                });
    }
}
