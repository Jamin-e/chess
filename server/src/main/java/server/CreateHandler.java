package server;

import com.google.gson.Gson;
import dataaccess.Exception;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.*;


public class CreateHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public CreateHandler(UserService userService){
        this.userService = userService;
    }

    public Handler create = ctx -> handleCreate(ctx);

    private void handleCreate(Context ctx) {
        try {
            String authToken = ctx.header("authorization");
            CreateHandler.Body body = gson.fromJson(ctx.body(), CreateHandler.Body.class);
            CreateRequest createRequest = new CreateRequest(authToken, body.gameName);

            CreateResult result = userService.create(createRequest);


            ctx.status(200);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(result));

        } catch (Exception e) {
            String message = e.getMessage();
            int status;
            if ("Error: bad request".equals(message)) {
                status = 400;
            } else if ("Error: unauthorized".equals(message)) {
                status = 401;
            } else {
                status = 500;
                message = "Error: " + message;
            }
            ctx.status(status);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(new CreateHandler.ErrorResponse(message)));


        }
    }

    private record ErrorResponse(String message) {}

    private static class Body {
        String gameName;
    }
}
