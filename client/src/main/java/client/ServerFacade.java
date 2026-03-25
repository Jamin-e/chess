package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import server.*;
import dataaccess.*;
import service.*;

import javax.xml.crypto.Data;
import java.net.http.HttpClient;
import java.util.List;

public class ServerFacade {
    private final String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();


    public ServerFacade(int port) {
        this.baseUrl = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws DataAccessException {
        var request = new RegisterRequest(username,password,email);
        return post("/user", request, AuthData.class,null);
    }
    public AuthData login(String username, String password) throws DataAccessException{
        var request = new LoginRequest(username, password);
        return post("/session", request, AuthData.class, null);
    }
    public void logout(String authToken) throws DataAccessException{
        delete("/session", Void.class, authToken);
    }
    public List<GameData> listGames(String authToken) throws DataAccessException{
        ListGamesResponse response = get("/game", ListGamesResponse.class, authToken);
        return response.games();
    }
    public GameData createGame(String authToken, String gameName) throws DataAccessException{
        var request = new CreateGameRequest(gameName);
        return post("/game", request, GameData.class, authToken);
    }
    public void joinGame(String authToken, int gameID, String color) throws DataAccessException{
        var request = new JoinGameRequest(gameId, color);
        put("/game", request, Void.class, authToken);
    }
    public GameData observeGame(String authToken, int gameID) throws DataAccessException{
        return null;
    }
    public void clear() throws DataAccessException{
        delete("/db", Void.class, null);
    }
}

