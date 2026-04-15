package dataaccess;
import model.AuthData;
import model.UserData;
import model.GameData;
import service.JoinResult;
import chess.ChessGame;

import java.util.Collection;

public interface DataAccess {
    void clear() throws Exception;

    void updateGame(int gameID, ChessGame game) throws Exception;

    void leaveGame(int gameID, String username) throws Exception;

    //users
    UserData getUser(String username) throws Exception;
    void createUser(UserData user) throws Exception;

    //auth
    void createAuth(AuthData auth) throws Exception;
    AuthData getAuth(String authToken) throws Exception;
    void deleteAuth(String authToken) throws Exception;

    //games
    int createGame(String gameName) throws Exception;
    GameData getGame(int gameID) throws Exception;
    JoinResult joinGame(GameData game, String color, String username) throws Exception;
    Collection<GameData> listGames() throws Exception;
}
