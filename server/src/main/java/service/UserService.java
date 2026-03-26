package service;

import dataaccess.DataAccess;
import dataaccess.Exception;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;


import java.util.UUID;

public class UserService {
    private final DataAccess dataAccess;


    public UserService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }




    private record ErrorResponse(String message) {}

    public RegisterResult register(RegisterRequest registerRequest) throws Exception {
        if(registerRequest == null
        || registerRequest.username() == null || registerRequest.username().isBlank()
        || registerRequest.password() == null || registerRequest.password().isBlank()
        || registerRequest.email() == null || registerRequest.email().isBlank()){
            throw new Exception("Error: bad request");
        }

        UserData user = dataAccess.getUser(registerRequest.username());
        if(user != null){
            throw new Exception("Error: username already taken");
        }

        String hashedPassword = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
        UserData newUser = new UserData(registerRequest.username(), hashedPassword, registerRequest.email());

        dataAccess.createUser(newUser);

        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, newUser.username());
        dataAccess.createAuth(auth);

        return new RegisterResult(newUser.username(),token);

    }

    public LoginResult login(LoginRequest loginRequest) throws Exception {
        if(loginRequest == null
        || loginRequest.password() == null || loginRequest.password().isBlank()
        || loginRequest.username() == null || loginRequest.username().isBlank()){
            throw new Exception("Error: bad request");
        }
        UserData user =  dataAccess.getUser(loginRequest.username());
        if(user == null){
            throw new Exception("Error: unauthorized");
        }


        if(!BCrypt.checkpw(loginRequest.password(), user.password())){
            throw new Exception("Error: unauthorized");
        }

        String token = UUID.randomUUID().toString();
        AuthData auth = new AuthData(token, user.username());
        dataAccess.createAuth(auth);

        return new LoginResult(user.username(), token);
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws Exception {
        if (logoutRequest == null
        || logoutRequest.authToken() == null || logoutRequest.authToken().isEmpty()){
            throw new Exception("Error: bad request");
        }
        String authToken = logoutRequest.authToken();
        if(dataAccess.getAuth(authToken) == null){
            throw new Exception("Error: unauthorized");
        }

        dataAccess.deleteAuth(authToken);
        return new LogoutResult();

    }

    public CreateResult create(CreateRequest createRequest) throws Exception {
        if(createRequest == null
        || createRequest.authToken() == null || createRequest.authToken().isBlank()
        || createRequest.gameName() == null || createRequest.gameName().isBlank()){
            throw new Exception("Error: bad request");
        }

        AuthData auth = dataAccess.getAuth(createRequest.authToken());
        if(auth == null){
            throw new Exception("Error: unauthorized");
        }

        int gameID = dataAccess.createGame(createRequest.gameName());

        return new CreateResult(gameID);
    }

    public JoinResult join(JoinRequest joinRequest) throws Exception {
        if(joinRequest == null
            || joinRequest.authToken() == null || joinRequest.authToken().isBlank()
            || joinRequest.gameID() == null || joinRequest.gameID().isBlank()
            || joinRequest.playerColor() == null || joinRequest.playerColor().isBlank()) {
            throw new Exception("Error: bad request");
        }

        AuthData auth = dataAccess.getAuth(joinRequest.authToken());
        if(auth == null){
            throw new Exception("Error: unauthorized");
        }

        if((!joinRequest.playerColor().equals("WHITE")) && (!joinRequest.playerColor().equals("BLACK"))){
           throw new Exception("Error: bad request");
        }

        GameData game = dataAccess.getGame(Integer.parseInt(joinRequest.gameID()));

        JoinResult joinResult = dataAccess.joinGame(game, joinRequest.playerColor(),auth.username());
        if(joinResult == null){
            throw new Exception("Error: already taken");
        }

        return joinResult;

    }

    public void clear(ClearRequest clearRequest) throws Exception {
        dataAccess.clear();
    }

    public ListResult list(ListRequest listRequest) throws Exception {
        if (listRequest == null
                || listRequest.authToken() == null || listRequest.authToken().isEmpty()){
            throw new Exception("Error: bad request");
        }
        String authToken = listRequest.authToken();
        if(dataAccess.getAuth(authToken) == null){
            throw new Exception("Error: unauthorized");
        }

        var games = dataAccess.listGames();

        return new ListResult(games);
    }
}
