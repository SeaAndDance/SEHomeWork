package io.github.FlyingASea.Handler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class ControlHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        JsonObject body = JsonParser.parseString(new String(input.readAllBytes(), StandardCharsets.UTF_8)).getAsJsonObject();
//        JsonElement operation = body.get("operation");
//        JsonElement data = body.get("data");
        System.out.println(body);
        exchange.sendResponseHeaders(HttpURLConnection.HTTP_NO_CONTENT, -1);
        OutputStream outputStream = exchange.getResponseBody();
        outputStream.close();
        exchange.close();
    }

}
