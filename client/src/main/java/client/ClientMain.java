package client;

import chess.*;

public class ClientMain {
    public static void main(String[] args) {
        var app = new ClientApp(8080);
        app.run();
    }
}