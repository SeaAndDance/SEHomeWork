package io.github.FlyingASea.exception;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ErrorCode {
    public static void errorResponse(int errorCode, int httpCode, HttpExchange exchange) throws IOException {
        JsonObject responseBody = new JsonObject();
        JsonElement error_code = JsonParser.parseString(String.valueOf(errorCode));
        responseBody.add("error_code", error_code);
        exchange.sendResponseHeaders(httpCode, responseBody.getAsLong());
        OutputStream out = exchange.getResponseBody();
        out.write(responseBody.toString().getBytes(StandardCharsets.UTF_8));
        out.close();
    }
}
