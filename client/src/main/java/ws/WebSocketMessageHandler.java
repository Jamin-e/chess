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
        //update local board state and redraw
    }

    private void handleError(ServerMessage message){
        //show error to root client only
    }

    private void handleNotification(ServerMessage message){
        //show notification
    }
}
