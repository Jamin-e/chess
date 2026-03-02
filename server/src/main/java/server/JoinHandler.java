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

    public JoinHandler(UserService userService){
        this.userService = userService;
    }

    public Handler join = ctx -> handleJoin(ctx);

    private void handleJoin(Context ctx){
        try{
            var joinRequest = gson.fromJson(ctx.body(),JoinRequest.class);

            userService.join(joinRequest);

            ctx.status(200);
        }
        catch(DataAccessException e) {
            String message = e.getMessage();
            int status;
            if ("Error: bad request".equals(message)) {
                status = 400;
            } else if ("Error: unauthorized".equals(message)) {
                status = 401;
            } else {
                status = 403;
                message = "Error: already taken";
            }

            ctx.status(status);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(new JoinHandler.ErrorResponse(message)));
        }

    }

    private record ErrorResponse(String message) {}

}
