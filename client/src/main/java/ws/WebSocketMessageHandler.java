package ws;

import ui.GameplayView;
import websocket.ErrorMessage;
import websocket.LoadGameMessage;
import websocket.NotificationMessage;
import websocket.messages.ServerMessage;

public class WebSocketMessageHandler {
    private final GameplayView gameplayView;

    public WebSocketMessageHandler(GameplayView view){
        this.gameplayView = view;
    }

    public void handle(ServerMessage message){
        switch(message.getServerMessageType()){
            case LOAD_GAME -> handleLoadGame((LoadGameMessage) message);
            case ERROR -> handleError((ErrorMessage) message);
            case NOTIFICATION -> handleNotification((NotificationMessage) message);
        }
    }

    private void handleLoadGame(LoadGameMessage message){
        gameplayView.setGame(message.game);
        gameplayView.drawBoard();
    }

    private void handleError(ErrorMessage message){
        gameplayView.showError(message.errorMessage);
    }

    private void handleNotification(NotificationMessage message){
        gameplayView.showNotification(message.message);
    }
}
