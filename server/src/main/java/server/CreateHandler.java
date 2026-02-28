package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.CreateRequest;
import service.CreateResult;
import service.UserService;


public class CreateHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public CreateHandler(DataAccess dataAccess){
        this.userService = new UserService(dataAccess);
    }

    public Handler create = ctx -> handleCreate(ctx);

    private void handleCreate(Context ctx){
        try{
            var createRequest = gson.fromJson(ctx.body(),CreateRequest.class);

            CreateResult result = userService.create(createRequest);

            ctx.status(200);
            ctx.json(result);
        }
        catch(DataAccessException e){}
            String message = ctx.result();
        if ("Error: bad request".equals(message)) {
            ctx.status(400);
        } else if ("Error: unauthorized".equals(message)) {
            ctx.status(401);
        } else {
            ctx.status(500);
            message = "Error: " + message;
        }

        ctx.json(new ErrorResponse(message));

    }

    private record ErrorResponse(String message) {}

}
