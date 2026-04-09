package ws;

import websocket.messages.ServerMessage;

public class WebSocketMessageHandler {
    public void handle(ServerMessage message){
        switch(message.getServerMessageType()){
            case LOAD_GAME -> handleLoadGame(message);
            case ERROR -> handleError(message);
            case NOTIFICATION -> handleNotification(message);
        }
    }

    private void handleLoadGame(ServerMessage message){
        //tbi
    }

    private void handleError(ServerMessage message){
        //tbi
    }

    private void handleNotification(ServerMessage message){
        //tbi
    }
}
