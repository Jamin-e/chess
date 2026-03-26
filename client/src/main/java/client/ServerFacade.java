package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import server.*;
import dataaccess.*;
import service.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;

public class ServerFacade {
    private final String baseUrl;
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    public ServerFacade(int port) {
        this.baseUrl = "http://localhost:" + port;
    }

    public AuthData register(String username, String password, String email) throws DataAccessException {
        var request = new RegisterRequest(username, password, email);
        return post("/user", request, AuthData.class, null);
    }

    public AuthData login(String username, String password) throws DataAccessException {
        var request = new LoginRequest(username, password);
        return post("/session", request, AuthData.class, null);
    }

    public void logout(String authToken) throws DataAccessException {
        delete("/session", Void.class, authToken);
    }

    public List<GameData> listGames(String authToken) throws DataAccessException {
        ListResult response = get("/game", ListResult.class, authToken);
        if (response == null){
            return Collections.emptyList();
        }
        return (List<GameData>) response.games();
    }

    public GameData createGame(String authToken, String gameName) throws DataAccessException {
        var request = new CreateRequest(authToken, gameName);
        return post("/game", request, GameData.class, authToken);
    }

    public void joinGame(String authToken, String gameID, String color) throws DataAccessException {
        var request = new JoinRequest(authToken, color, gameID);
        put("/game", request, Void.class, authToken);
    }


    public void clear() throws DataAccessException {
        delete("/db", Void.class, null);
    }

    private <T, R> R post(String path, T request, Class<R> responseType, String authToken) throws DataAccessException {
        try {
            var body = gson.toJson(request);
            var builder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .header("Content-Type", "application/json");
            if (authToken != null) {
                builder.header("Authorization", authToken);
            }
            var httpRequest = builder.build();
            var response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                throw new DataAccessException("Error:" + response.statusCode() + " " + response.body());
            }
            if (responseType == Void.class) {
                return null;
            }
            return gson.fromJson(response.body(), responseType);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private <R> R get(String path, Class<R> responseType, String authToken) throws DataAccessException {
        try {
            var builder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .GET();

            if (authToken != null) {
                builder.header("Authorization", authToken);
            }

            var request = builder.build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new DataAccessException("Error:" + response.statusCode() + " " + response.body());
            }

            if (responseType == Void.class) {
                return null;
            }
            return gson.fromJson(response.body(), responseType);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private <T, R> R put(String path, T requestBody, Class<R> responseType, String authToken) throws DataAccessException {
        try {
            String json = gson.toJson(requestBody);

            var builder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .PUT(HttpRequest.BodyPublishers.ofString(json))
                    .header("Content-Type", "application/json");

            if (authToken != null) {
                builder.header("Authorization", authToken);
            }

            var request = builder.build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new DataAccessException("Error:" + response.statusCode() + " " + response.body());
            }

            if (responseType == Void.class) {
                return null;
            }
            return gson.fromJson(response.body(), responseType);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private <R> R delete(String path, Class<R> responseType, String authToken) throws DataAccessException {
        try {
            var builder = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .DELETE();

            if (authToken != null) {
                builder.header("Authorization", authToken);
            }

            var request = builder.build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new DataAccessException("Error:" + response.statusCode() + " " + response.body());
            }

            if (responseType == Void.class) {
                return null;
            }
            return gson.fromJson(response.body(), responseType);
        } catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}