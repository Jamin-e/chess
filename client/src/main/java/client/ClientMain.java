package client;

import chess.*;
import server.Server;

public class ClientMain {
    public static void main(String[] args) {
        var app = new ClientApp(8080);
        Server server = new Server();
        server.run(8080);
        app.run();
    }
}
