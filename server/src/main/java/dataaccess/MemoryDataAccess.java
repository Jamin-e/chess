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

    public void createUser(UserData user) throws Exception {
        if(users.containsKey(user.username())){
            throw new Exception("user already exists");
        }
        users.put(user.username(),user);
    }

    public UserData getUser(String username) throws Exception {
        if(!users.containsKey(username)){
            return null;
        }
        return users.get(username);
    }

    public void createAuth(AuthData auth) throws Exception {
        if(auths.containsKey(auth.authToken())){
            throw new Exception("auth already exists");
        }
        auths.put(auth.authToken(),auth);
    }

    public AuthData getAuth(String authToken) throws Exception {
        if(!auths.containsKey(authToken)){
            return null;
        }
        return auths.get(authToken);
    }

    public void deleteAuth(String authToken) throws Exception {
        if(!auths.containsKey(authToken)){
            throw new Exception("auth does not exist");
        }
        auths.remove(authToken);
    }

    public int createGame(String gamename) throws Exception {
        int gameID = games.size() + 1;
        games.put(gameID, new GameData(gameID,null,null, gamename, new ChessGame()));

                return gameID;
    }

    public GameData getGame(int gameID) throws Exception {
        if(!games.containsKey(gameID)){
            throw new Exception("game does not exist");
        }
        return games.get(gameID);
    }

    public JoinResult joinGame(GameData game, String color, String username) throws Exception {
        if(!games.containsKey(game.gameID())){
            throw new Exception("game does not exist");
        }

        if (Objects.equals(color, "WHITE")){
            if(game.whiteUsername() == null) {
                GameData updated = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                games.put(game.gameID(),updated);
                return new JoinResult();
            }
            else{
                return null;
            }
        }
        else if(Objects.equals(color, "BLACK")){
            if(game.blackUsername() == null) {
                GameData updated = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                games.put(game.gameID(),updated);
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
