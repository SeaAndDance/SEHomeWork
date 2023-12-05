package io.github.FlyingASea;

import com.sun.net.httpserver.HttpServer;
import io.github.FlyingASea.Handler.ControlHandler;

import java.io.IOException;
import java.net.InetSocketAddress;


public class Application {

    private static final int PORT = 8000;

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/control", new ControlHandler());
        server.start();
        System.out.println("Server started on port " + PORT);
    }


}
