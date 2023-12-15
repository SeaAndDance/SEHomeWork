package io.github.FlyingASea;

import com.sun.net.httpserver.HttpServer;
import io.github.FlyingASea.Handler.ControlHandler;

import io.github.FlyingASea.util.SHA256withRSAUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;


public class Server {
    private static final int PORT = 10000;
    public static final Map<String, String> keys = SHA256withRSAUtil.createRSAKeys();

    public static void main(String[] args) throws IOException {
        for (int i = 1; i < 6; i++) {
            HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT + i), 0);
            server.createContext("/api/control", new ControlHandler());
            server.start();
            System.out.println("Server started on port " + (PORT + i));
        }
    }
}
