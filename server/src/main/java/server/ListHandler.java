package server;

import com.google.gson.Gson;
import dataaccess.Exception;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.*;


public class ListHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public ListHandler(UserService userService){
        this.userService = userService;
    }

    public Handler list = ctx -> handleList(ctx);

    private void handleList(Context ctx){
        try{
            String authToken = ctx.header("authorization");

            ListRequest listRequest = new ListRequest(authToken);

            ListResult result = userService.list(listRequest);


            ctx.status(200);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(result));
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
            ctx.result(gson.toJson(new ListHandler.ErrorResponse(message)));
        }

    }

    private record ErrorResponse(String message) {}

}
