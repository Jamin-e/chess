package server;

import com.google.gson.Gson;
import dataaccess.Exception;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.*;


public class JoinHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public JoinHandler(UserService userService){
        this.userService = userService;
    }

    public Handler join = ctx -> handleJoin(ctx);

    private void handleJoin(Context ctx){
        try{

            String authToken = ctx.header("authorization");
            Body body = gson.fromJson(ctx.body(), Body.class);

            JoinRequest joinRequest = new JoinRequest(authToken, body.playerColor, body.gameID);

            JoinResult result = userService.join(joinRequest);


            ctx.status(200);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(result));

        }
        catch(Exception e) {
            String message = e.getMessage();
            int status;
            if ("Error: bad request".equals(message)) {
                status = 400;
            } else if ("Error: unauthorized".equals(message)) {
                status = 401;
            } else if ("Error: already taken".equals(message)){
                status = 403;
                message = "Error: already taken";
            }
            else{
                status = 500;
                message = "Error" + e.getMessage();
            }

            ctx.status(status);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(new JoinHandler.ErrorResponse(message)));
        }

    }

    private record ErrorResponse(String message) {}
    private static class Body {
        String gameID;
        String playerColor;
    }

}
