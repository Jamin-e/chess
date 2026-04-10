package ws;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

public class WebSocketClient {
    Gson gson = new Gson();
    final WebSocketMessageHandler messageHandler = new WebSocketMessageHandler();
    final WebSocketHandler WSHandler = new WebSocketHandler();
    public void connect(String authToken, Integer gameID, boolean isPlayer){
        //open ws connection
        //send CONNECT command
        //transition to gameplay UI
    }

    public void sendCommand(UserGameCommand command){
        //serialize and send
    }

    public void closeConnection(){
        //close connection
    }
}
