package server;
import dataaccess.DataAccess;
import service.UserService;

import dataaccess.MemoryDataAccess;
import io.javalin.*;

public class Server {

    private final Javalin javalin;
    private final DataAccess dataAccess = new MemoryDataAccess();
    private final UserService userService = new UserService(dataAccess);

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        javalin.post("/session", new LoginHandler(userService).login);
        javalin.post("/user", new RegistrationHandler(userService).register);
        javalin.delete("/session", new LogoutHandler(userService).logout);
        javalin.get("/game", new ListHandler(userService).list);
        javalin.post("/game", new CreateHandler(userService).create);
        javalin.put("/game", new JoinHandler(userService).join);
        javalin.delete("/db", new ClearHandler(userService).clear);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
