package server;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import service.RegisterRequest;
import service.RegisterResult;
import service.UserService;


public class RegistrationHandler {
    private final UserService userService;
    private final Gson gson = new Gson();

    public RegistrationHandler(UserService userService){
        this.userService = userService;
    }

    public Handler register = ctx -> handleRegistration(ctx);

    private void handleRegistration(Context ctx){
        try{
            RegisterRequest registerRequest = gson.fromJson(ctx.body(),RegisterRequest.class);

            RegisterResult result = userService.register(registerRequest);

            ctx.status(200);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(result));
        }
        catch(DataAccessException e) {
            String message = e.getMessage();
            int status;

            if ("Error: bad request".equals(message)) {
                status = 400;
            } else if ("Error: username already taken".equals(message)) {
                status = 403;
            } else {
                status = 500;
                message = "Error: " + e.getMessage();
            }
            ctx.status(status);
            ctx.contentType("application/json");
            ctx.result(gson.toJson(new RegistrationHandler.ErrorResponse(message)));
        }
    }

    private record ErrorResponse(String message) {}

}
