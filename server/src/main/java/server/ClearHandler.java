package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ClearRequest;
import service.ClearResult;
import service.UserService;


public class ClearHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public ClearHandler(DataAccess dataAccess){
        this.userService = new UserService(dataAccess);
    }

    public Handler clear = ctx -> handleClear(ctx);

    private void handleClear(Context ctx){
        try{
            var clearRequest = gson.fromJson(ctx.body(),ClearRequest.class);

            userService.clear(clearRequest);

            ctx.status(200);
        }
        catch(DataAccessException e){}
            String message = ctx.result();
            ctx.status(500);
            message = "Error: " + message;

        ctx.json(new ErrorResponse(message));

    }

    private record ErrorResponse(String message) {}

}
