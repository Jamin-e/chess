package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.net.http.HttpClient;
import java.util.List;

public class ServerFacade {
    private final String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();


    public ServerFacade(int port) {
        this.baseUrl = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws Exception{
        return null;
    }
    public AuthData login(String username, String password) throws Exception{
        return null;
    }
    public void logout(String authToken) throws Exception{
        //tbi
    }
    public List<GameData> listGames(String authToken) throws Exception{
        return null;
    }
    public GameData createGame(String authToken) throws Exception{
     return null;
    }
    public void joinGame(String authToken, int gameID, String color) throws Exception{
        //tbi
    }
    public void clear() throws Exception{
        //tbi
    }
}

