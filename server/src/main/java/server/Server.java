package server;
import dataaccess.*;
import dataaccess.Exception;
import service.UserService;

import io.javalin.*;

public class Server {

    private final Javalin javalin;
    //private final DataAccess dataAccess = new MemoryDataAccess();
    private final DataAccess sqlDataAccess = new SQLDataAccess();
    private final UserService userService = new UserService(sqlDataAccess);

    public Server(){
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

    public int run(int desiredPort){
        startup();
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public void startup(){
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTables();
        } catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

