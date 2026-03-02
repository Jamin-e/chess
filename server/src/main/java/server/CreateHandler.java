package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
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

            CreateRequest createRequest = new CreateRequest(authToken, ctx.body());

            CreateResult result = userService.create(createRequest);


            ctx.status(200);
            ctx.result(gson.toJson(result));

        } catch (DataAccessException e) {
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

}
