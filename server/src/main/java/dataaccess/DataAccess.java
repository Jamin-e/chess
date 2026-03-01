package dataaccess;
import model.AuthData;
import model.UserData;
import model.GameData;

public interface DataAccess {
    void clear() throws DataAccessException;

    //users
    UserData getUser(String username) throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;

    //auth
    void createAuth(AuthData auth) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;

    //games
    String createGame(String gameName) throws DataAccessException;
    GameData getGame(String gameID) throws DataAccessException;
    void updateGame(GameData game, String color, String username);
}
