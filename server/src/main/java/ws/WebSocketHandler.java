package ws;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import jakarta.websocket.*;

public class WebSocketHandler {
    private final Gson gson = new Gson();
    private final GameMessageRouter router = new GameMessageRouter();
    private final GameConnectionManager connectionManager = new GameConnectionManager();

    public void onOpen(Integer ID, Session session, EndpointConfig config){
        connectionManager.addConnection(ID, session);

        session.addMessageHandler((MessageHandler.Whole<String>) rawJson -> {
            UserGameCommand command = gson.fromJson(rawJson, UserGameCommand.class);
            router.route(command);
        });
    }

    public void onMessage(String rawJSON){
        UserGameCommand command = gson.fromJson(rawJSON, UserGameCommand.class);
        router.route(command);
    }

    public void OnClose(Integer gameID, Session session){
        connectionManager.removeConnection(gameID, session);
    }

    public void onError(Object session, Throwable error) {
        // log or print error
    }
}
