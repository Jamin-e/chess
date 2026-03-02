package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryDataAccess implements DataAccess {
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<String, AuthData> auths = new HashMap<>();
    private final Map<Integer, GameData> games = new HashMap<>();
    public void clear(){
        users.clear();
        auths.clear();
        games.clear();
    }

    public void createUser(UserData user) throws DataAccessException{
        if(users.containsKey(user.username())){
            throw new DataAccessException("user already exists");
        }
        users.put(user.username(),user);
    }

    public UserData getUser(String username) throws DataAccessException{
        if(!users.containsKey(username)){
            return null;
        }
        return users.get(username);
    }

    public void createAuth(AuthData auth) throws DataAccessException {
        if(auths.containsKey(auth.authToken())){
            throw new DataAccessException("auth already exists");
        }
        auths.put(auth.authToken(),auth);
    }

    public AuthData getAuth(String authToken) throws DataAccessException{
        if(!auths.containsKey(authToken)){
            throw new DataAccessException("auth does not exist");
        }
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        if(!auths.containsKey(authToken)){
            throw new DataAccessException("auth does not exist");
        }
        auths.remove(authToken);
    }

    public int createGame(String gamename) throws DataAccessException{
        int gameID = games.size();
        games.put(gameID, new GameData(gameID,null,null, gamename, new ChessGame()));

                return gameID;
    }

    public GameData getGame(int gameID) throws DataAccessException{
        if(!games.containsKey(gameID)){
            throw new DataAccessException("game does not exist");
        }
        return games.get(gameID);
    }

    public void updateGame(GameData game, String color, String username) throws DataAccessException{
        if(!games.containsKey(game.gameID())){
            throw new DataAccessException("game does not exist");
        }
        if (color == "white"){
            if(game.whiteUsername() == null) {
                game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            }
            else{
                throw new DataAccessException("color already taken");
            }
        }
        else{
            if(game.blackUsername() == null) {
                game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            }
            else{
                throw new DataAccessException("color already taken");
            }
        }
    }

    public Map<Integer, GameData> listGames(){
        return games;
    }
}
