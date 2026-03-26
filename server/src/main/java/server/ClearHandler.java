package server;

import com.google.gson.Gson;
import dataaccess.Exception;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.ClearRequest;
import service.UserService;


public class ClearHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public ClearHandler(UserService userService){
        this.userService = userService;
    }

    public Handler clear = ctx -> handleClear(ctx);

    private void handleClear(Context ctx){
        try{
            var clearRequest = gson.fromJson(ctx.body(),ClearRequest.class);

            userService.clear(clearRequest);

            ctx.status(200);
        }
        catch(Exception e) {
            String message = e.getMessage();
            ctx.status(500);
            message = "Error: " + message;

            ctx.contentType("application/json");
            ctx.result(gson.toJson(new ErrorResponse(message)));

        }

    }

    private record ErrorResponse(String message) {}

}
