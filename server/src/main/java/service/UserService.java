package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Map;
import java.util.UUID;

public class UserService {
    private final DataAccess dataAccess;

    public UserService(DataAccess dataAccess){
        this.dataAccess = dataAccess;
    }

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

        UserData newUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());

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
        UserData user =  dataAccess.getUser(loginRequest.username());
        if(user == null){
            throw new DataAccessException("Error: bad request");
        }

        if(!user.password().equals(loginRequest.password())){
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

        int gameID = dataAccess.createGame(createRequest.gameName());

        return new CreateResult(gameID);
    }

    public void join(JoinRequest joinRequest) throws DataAccessException{
        if(joinRequest == null
            || joinRequest.authToken() == null || joinRequest.authToken().isBlank()
            || joinRequest.gameID() == null || joinRequest.gameID().isBlank()
            || joinRequest.playerColor() == null || joinRequest.playerColor().isBlank()) {
            throw new DataAccessException("Error: bad request");
        }

        AuthData auth = dataAccess.getAuth(joinRequest.authToken());

        GameData game = dataAccess.getGame(Integer.parseInt(joinRequest.gameID()));

        dataAccess.updateGame(game, joinRequest.playerColor(),auth.username());

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

        Map<Integer, GameData> games = dataAccess.listGames();

        return new ListResult(games);
    }
}
