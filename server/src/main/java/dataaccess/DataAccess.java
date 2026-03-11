package dataaccess;
import model.AuthData;
import model.UserData;
import model.GameData;
import service.JoinResult;

import javax.xml.crypto.Data;
import java.util.Collection;
import java.util.Map;

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
    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    JoinResult joinGame(GameData game, String color, String username) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
}
