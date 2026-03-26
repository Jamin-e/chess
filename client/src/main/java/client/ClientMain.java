package client;

import chess.*;
import server.Server;

public class ClientMain {
    public static void main(String[] args) {
        var app = new ClientApp(0);
        app.run();
    }
}