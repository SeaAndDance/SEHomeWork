package io.github.FlyingASea.Handler;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import io.github.FlyingASea.exception.ErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ControlHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        InputStream input = exchange.getRequestBody();
        JsonObject body = JsonParser.parseString(new String(input.readAllBytes(), StandardCharsets.UTF_8)).getAsJsonObject();
        JsonElement operation = body.get("operation");
        JsonElement data = body.get("data");
        if (data == null || operation == null) {
            ErrorCode.errorResponse(100, 401, exchange);
        }
        exchange.sendResponseHeaders(204, -1);
    }

}