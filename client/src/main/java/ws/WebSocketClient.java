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
        UserGameCommand connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        sendCommand(connectCommand);
        connected = true;
    }

    public void sendCommand(UserGameCommand command){
        if (!connected){
            throw new IllegalStateException("Not connected");
        }
        String json = gson.toJson(command);
        //send json through socket
    }

    public void closeConnection(){
        UserGameCommand disconnectCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        connected = false;
    }
    private void handleIncoming(String rawJSON){
        ServerMessage message = gson.fromJson(rawJSON, ServerMessage.class);
        messageHandler.handle(message);
    }

    private void onOpen() {
        connected = true;
    }
    private void onClose() {
        connected = false;
    }
    private void onError(Exception e) {
        // surface or log websocket errors
    }
}
