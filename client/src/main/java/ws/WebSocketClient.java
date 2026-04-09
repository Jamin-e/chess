package ws;

import websocket.commands.UserGameCommand;

public class WebSocketClient {
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
