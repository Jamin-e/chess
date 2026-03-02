package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.JoinRequest;
import service.JoinResult;
import service.UserService;


public class JoinHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public JoinHandler(DataAccess dataAccess){
        this.userService = new UserService(dataAccess);
    }

    public Handler join = ctx -> handleJoin(ctx);

    private void handleJoin(Context ctx){
        try{
            var joinRequest = gson.fromJson(ctx.body(),JoinRequest.class);

            userService.join(joinRequest);

            ctx.status(200);
        }
        catch(DataAccessException e){}
            String message = ctx.result();
        if ("Error: bad request".equals(message)) {
            ctx.status(400);
        } else if ("Error: unauthorized".equals(message)) {
            ctx.status(401);
        } else {
            ctx.status(403);
            message = "Error: already taken";
        }

        ctx.result(gson.toJson(new JoinHandler.ErrorResponse(message)));

    }

    private record ErrorResponse(String message) {}

}
