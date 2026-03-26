package server;

import com.google.gson.Gson;
import dataaccess.Exception;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.LogoutRequest;
import service.LogoutResult;
import service.UserService;


public class LogoutHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public LogoutHandler(UserService userService){
        this.userService = userService;
    }

    public Handler logout = ctx -> handleLogout(ctx);

    private void handleLogout(Context ctx){
        try{
            String authToken = ctx.header("authorization");

            LogoutRequest logoutRequest = new LogoutRequest(authToken);

            LogoutResult result = userService.logout(logoutRequest);

            ctx.status(200);
            ctx.contentType("application/json");
            ctx.result("{ }");
        }
        catch(Exception e) {
            String message = e.getMessage();
            int status;
            if ("Error: unauthorized".equals(message)) {
                status = 401;
            } else {
                status = 500;
                message = "Error: " + message;
            }

            ctx.status(status);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(new LogoutHandler.ErrorResponse(message)));
        }
    }

    private record ErrorResponse(String message) {}

}
