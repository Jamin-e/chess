package server;
import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import service.UserService;
import service.LoginRequest;
import service.LoginResult;
import io.javalin.http.Context;
import io.javalin.http.Handler;


public class LoginHandler{
    private final UserService userService;
    private final Gson gson = new Gson();

    public LoginHandler(DataAccess dataAccess){
        this.userService = new UserService(dataAccess);
    }

    public Handler login = ctx -> handleLogin(ctx);

    private void handleLogin(Context ctx){
        try{
            var loginRequest = gson.fromJson(ctx.body(),LoginRequest.class);

            LoginResult result = userService.login(loginRequest);

            ctx.status(200);
            ctx.result(gson.toJson(result));
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

        ctx.result(gson.toJson(new LoginHandler.ErrorResponse(message)));

    }

    private record ErrorResponse(String message) {}

}
