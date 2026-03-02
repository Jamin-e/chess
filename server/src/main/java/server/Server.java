package server;
import dataaccess.DataAccess;

import dataaccess.MemoryDataAccess;
import io.javalin.*;

public class Server {

    private final Javalin javalin;
    private final DataAccess dataAccess = new MemoryDataAccess();

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/session", new LoginHandler(dataAccess).login);
        javalin.post("/user", new RegistrationHandler(dataAccess).register);
        javalin.delete("/session", new LogoutHandler(dataAccess).logout);
        javalin.get("/game", new ListHandler(dataAccess).list);
        javalin.post("/game", new CreateHandler(dataAccess).create);
        javalin.put("/game", new JoinHandler(dataAccess).join);
        javalin.delete("/db", new ClearHandler(dataAccess).clear);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
