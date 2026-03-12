package service;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;
import server.ClearHandler;


import java.util.Map;
import java.util.UUID;

public class UserService {
    private final DataAccess dataAccess;


    public UserService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }




    private record ErrorResponse(String message) {}

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException{
        if(registerRequest == null
        || registerRequest.username() == null || registerRequest.username().isBlank()
        || registerRequest.password() == null || registerRequest.password().isBlank()
        || registerRequest.email() == null || registerRequest.email().isBlank()){
            throw new DataAccessException("Error: bad request");
        }

        UserData user = dataAccess.getUser(registerRequest.username());
        if(user != null){
            throw new DataAccessException("Error: username already taken");
        }

        String hashedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        UserData newUser = new UserData(registerRequest.username(), hashedPassword, registerRequest.email());

        dataAccess.createUser(newUser);

        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, newUser.username());
        dataAccess.createAuth(auth);

        return new RegisterResult(newUser.username(),token);

    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException{
        if(loginRequest == null
        || loginRequest.password() == null || loginRequest.password().isBlank()
        || loginRequest.username() == null || loginRequest.username().isBlank()){
            throw new DataAccessException("Error: bad request");
        }
        String hashedPassword = BCrypt.hashpw(loginRequest.username(), BCrypt.gensalt());
        UserData user =  dataAccess.getUser(hashedPassword);
        if(user == null){
            throw new DataAccessException("Error: unauthorized");
        }


        if(!user.password().equals(hashedPassword)){
            throw new DataAccessException("Error: unauthorized");
        }

        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, user.username());
        dataAccess.createAuth(auth);

        return new LoginResult(user.username(), token);
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException{
        if (logoutRequest == null
        || logoutRequest.authToken() == null || logoutRequest.authToken().isEmpty()){
            throw new DataAccessException("Error: bad request");
        }
        String authToken = logoutRequest.authToken();
        if(dataAccess.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }

        dataAccess.deleteAuth(authToken);
        return new LogoutResult();

    }

    public CreateResult create(CreateRequest createRequest) throws DataAccessException{
        if(createRequest == null
        || createRequest.authToken() == null || createRequest.authToken().isBlank()
        || createRequest.gameName() == null || createRequest.gameName().isBlank()){
            throw new DataAccessException("Error: bad request");
        }

        AuthData auth = dataAccess.getAuth(createRequest.authToken());
        if(auth == null){
            throw new DataAccessException("Error: unauthorized");
        }

        int gameID = dataAccess.createGame(createRequest.gameName());

        return new CreateResult(gameID);
    }

    public JoinResult join(JoinRequest joinRequest) throws DataAccessException{
        if(joinRequest == null
            || joinRequest.authToken() == null || joinRequest.authToken().isBlank()
            || joinRequest.gameID() == null || joinRequest.gameID().isBlank()
            || joinRequest.playerColor() == null || joinRequest.playerColor().isBlank()) {
            throw new DataAccessException("Error: bad request");
        }

        AuthData auth = dataAccess.getAuth(joinRequest.authToken());
        if(auth == null){
            throw new DataAccessException("Error: unauthorized");
        }

        if((!joinRequest.playerColor().equals("WHITE")) && (!joinRequest.playerColor().equals("BLACK"))){
           throw new DataAccessException("Error: bad request");
        }

        GameData game = dataAccess.getGame(Integer.parseInt(joinRequest.gameID()));

        JoinResult joinResult = dataAccess.joinGame(game, joinRequest.playerColor(),auth.username());
        if(joinResult == null){
            throw new DataAccessException("Error: already taken");
        }

        return joinResult;

    }

    public void clear(ClearRequest clearRequest) throws DataAccessException{
        dataAccess.clear();
    }

    public ListResult list(ListRequest listRequest) throws DataAccessException{
        if (listRequest == null
                || listRequest.authToken() == null || listRequest.authToken().isEmpty()){
            throw new DataAccessException("Error: bad request");
        }
        String authToken = listRequest.authToken();
        if(dataAccess.getAuth(authToken) == null){
            throw new DataAccessException("Error: unauthorized");
        }

        var games = dataAccess.listGames();

        return new ListResult(games);
    }
}
