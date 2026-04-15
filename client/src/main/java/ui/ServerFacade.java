package ui;

import chess.ChessMove;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ws.WebSocketClient;
import ws.WebSocketMessageHandler;
import websocket.commands.UserGameCommand;

public class ServerFacade {
    private final String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    private WebSocketClient webSocketClient;
//    private WebSocketMessageHandler wsHandler;

    public ServerFacade(int port) {
        this.baseUrl = "http://localhost:" + port;
    }

    public void setWebSocketHandler(WebSocketMessageHandler handler){
//        this.wsHandler = handler;
        this.webSocketClient = new WebSocketClient(handler);
    }

    public void connectGame(String authToken, int gameID, boolean isPlayer){
        if (webSocketClient == null){
            throw new IllegalStateException("Websocket handler not set");
        }
        webSocketClient.connect(baseUrl, authToken, gameID, isPlayer);
    }

    public void sendMove(String authToken, int gameID, ChessMove move){
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID);
        command.move = move;
        webSocketClient.sendCommand(command);
    }

    public void leaveGame(String authToken, int gameID){
        webSocketClient.sendCommand(new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID));
        webSocketClient.closeConnection();
    }

    public void resignGame(String authToken, int gameID) {
        webSocketClient.sendCommand(new UserGameCommand(
                UserGameCommand.CommandType.RESIGN,
                authToken,
                gameID
        ));
    }

    public AuthData register(String username, String password, String email) throws Exception {
        var request = new java.util.HashMap<String, Object>();
        request.put("username", username);
        request.put("password", password);
        request.put("email", email);
        return post("/user", request, AuthData.class, null);
    }

    public AuthData login(String username, String password) throws Exception {
        var request = new java.util.HashMap<String, Object>();
        request.put("username", username);
        request.put("password", password);
        return post("/session", request, AuthData.class, null);
    }

    public void logout(String authToken) throws Exception {
        delete("/session", Void.class, authToken);
    }

    public List<GameData> listGames(String authToken) throws Exception {
        ListResult response = get("/game", ListResult.class, authToken);
        if (response == null || response.getGames() == null) {
            return Collections.emptyList();
        }
        return response.getGames();
    }

    public GameData createGame(String authToken, String gameName) throws Exception {
        var request = new java.util.HashMap<String, Object>();
        request.put("gameName", gameName);
        return post("/game", request, GameData.class, authToken);
    }

    public void joinGame(String authToken, int gameID, String color) throws Exception {
        var request = new java.util.HashMap<String, Object>();
        request.put("playerColor", color);
        request.put("gameID", gameID);
        put("/game", request, Void.class, authToken);
    }


    public void clear(){
        try{delete("/db", Void.class, null);}
        catch (Exception e){
            System.out.println(extractMessage(e.getMessage()));
        }
    }

    private <T, R> R post(String path, T request, Class<R> responseType, String authToken) throws Exception {
        try {
            var body = gson.toJson(request);
            var builder = request(path, authToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            return send(builder, responseType);
        } catch (java.lang.Exception e) {
            throw new Exception(extractMessage(e.getMessage()));
        }
    }

    private <R> R get(String path, Class<R> responseType, String authToken) throws Exception {
        try {
            var builder = request(path, authToken).GET().build();

            return send(builder, responseType);
        } catch (java.lang.Exception e) {
            throw new Exception(extractMessage(e.getMessage()));
        }
    }

    private <T, R> R put(String path, T requestBody, Class<R> responseType, String authToken) throws Exception {
        try {
            String json = gson.toJson(requestBody);
            var builder = request(path, authToken)
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            return send(builder, responseType);
        } catch (java.lang.Exception e) {
            throw new Exception(extractMessage(e.getMessage()));
        }
    }

    private <R> R delete(String path, Class<R> responseType, String authToken) throws Exception {
        try {
            var builder = request(path, authToken).DELETE().build();

            return send(builder,responseType);
        } catch (java.lang.Exception e) {
            throw new Exception(extractMessage(e.getMessage()));
        }
    }
    public static class ListResult {
        private List<GameData> games;

        public List<GameData> getGames() {
            return games;
        }
    }

    private String extractMessage(String json) {
        try {
            Map<?, ?> map = new Gson().fromJson(json, Map.class);
            return map.get("message").toString();
        } catch (Exception e) {
            return json;
        }
    }

    private HttpRequest.Builder request(String path, String authToken) {
        var b = HttpRequest.newBuilder().uri(URI.create(baseUrl + path));
        if (authToken != null) b.header("Authorization", authToken);
        return b;
    }

    private <R> R send(HttpRequest req, Class<R> responseType) throws Exception {
        try {
            var resp = client.send(req, HttpResponse.BodyHandlers.ofString());

            if (resp.statusCode() / 100 != 2) {
                throw new Exception(resp.statusCode() + " " + extractMessage(resp.body()));
            }
            if (responseType == Void.class) return null;
            return gson.fromJson(resp.body(), responseType);
        } catch (Exception e) {
            throw new Exception(extractMessage(e.getMessage()));
        }
    }
}
