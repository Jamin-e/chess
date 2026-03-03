package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;
import service.JoinResult;

import java.util.*;

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
            return null;
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
        int gameID = games.size() + 1;
        games.put(gameID, new GameData(gameID,null,null, gamename, new ChessGame()));

                return gameID;
    }

    public GameData getGame(int gameID) throws DataAccessException{
        if(!games.containsKey(gameID)){
            throw new DataAccessException("game does not exist");
        }
        return games.get(gameID);
    }

    public JoinResult updateGame(GameData game, String color, String username) throws DataAccessException{
        if(!games.containsKey(game.gameID())){
            throw new DataAccessException("game does not exist");
        }

        if (Objects.equals(color, "WHITE")){
            if(game.whiteUsername() == null) {
                game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                return new JoinResult();
            }
            else{
                return null;
            }
        }
        else if(Objects.equals(color, "BLACK")){
            if(game.blackUsername() == null) {
                game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                return new JoinResult();
            }
            else{
                return null;
            }
        }
        return null;
    }

    public Collection<GameData> listGames(){
        return games.values();
    }
}
