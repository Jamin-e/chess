package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.LogoutRequest;
import service.LogoutResult;
import service.UserService;


public class LogoutHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public LogoutHandler(DataAccess dataAccess){
        this.userService = new UserService(dataAccess);
    }

    public Handler logout = ctx -> handleLogout(ctx);

    private void handleLogout(Context ctx){
        try{
            var logoutRequest = gson.fromJson(ctx.body(),LogoutRequest.class);

            LogoutResult result = userService.logout(logoutRequest);

            ctx.status(200);
            ctx.json(result);
        }
        catch(DataAccessException e){}
            String message = ctx.result();
        if ("Error: unauthorized".equals(message)) {
            ctx.status(401);
        } else {
            ctx.status(500);
            message = "Error: " + message;
        }

        ctx.json(new ErrorResponse(message));

    }

    private record ErrorResponse(String message) {}

}
