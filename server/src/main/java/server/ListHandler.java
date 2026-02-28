package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.LoginRequest;
import service.LoginResult;
import service.UserService;


public class ListHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public ListHandler(DataAccess dataAccess){
        this.userService = new UserService(dataAccess);
    }

    public Handler list = ctx -> handleList(ctx);

    private void handleList(Context ctx){
        try{
            var listRequest = gson.fromJson(ctx.body(),ListRequest.class);

            ListResult result = userService.list(listRequest);

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
