package websocket;

import websocket.messages.ServerMessage;

public class ErrorMessage extends ServerMessage {
    public String errorMessage;

    public ErrorMessage() {
        super(ServerMessageType.ERROR);
    }

    public ErrorMessage(String errorMessage){
        super(ServerMessageType.ERROR);
        this.errorMessage = errorMessage;
    }
}
