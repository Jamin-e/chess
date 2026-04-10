package ws;

import com.google.gson.Gson;
import websocket.LoadGameMessage;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class WebSocketClient {
    private final Gson gson = new Gson();
    private final WebSocketMessageHandler messageHandler;

    private Object socket;

    private String authToken;
    private Integer gameID;
    private boolean connected = false;

    public WebSocketClient(WebSocketMessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }

    public void connect(String authToken, Integer gameID, boolean isPlayer){
        this.authToken = authToken;
        this.gameID = gameID;
        // 1. Open websocket connection to /ws
        // 2. When open, send CONNECT command
        // 3. Set connected = true
        // 4. Transition to gameplay UI after LOAD_GAME arrives
    }

    public void sendCommand(UserGameCommand command){
        if (!connected){
            throw new IllegalStateException("Not connected");
        }
        String json = gson.toJson(command);
        //send json through socket
    }

    public void closeConnection(){
        //close connection
        connected = false;
    }
    private void handleIncoming(String rawJSON){
        ServerMessage message = gson.fromJson(rawJSON, ServerMessage.class);
        messageHandler.handle(message);
    }
}
