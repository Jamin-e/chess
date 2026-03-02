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

    public RegistrationHandler(DataAccess dataAccess){
        this.userService = new UserService(dataAccess);
    }

    public Handler register = ctx -> handleRegistration(ctx);

    private void handleRegistration(Context ctx){
        try{
            var registerRequest = gson.fromJson(ctx.body(),RegisterRequest.class);

            RegisterResult result = userService.register(registerRequest);

            ctx.status(200);
            ctx.json(result);
        }
        catch(DataAccessException e){}
            String message = ctx.result();
        if ("Error: username already taken".equals(message)) {
            ctx.status(403);
        }

        ctx.json(new ErrorResponse(message));

    }

    private record ErrorResponse(String message) {}

}
