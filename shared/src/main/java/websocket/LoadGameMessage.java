package websocket;

import websocket.messages.ServerMessage;

public class LoadGameMessage extends ServerMessage {
    public Object game;

    public LoadGameMessage(){
        super(ServerMessageType.LOAD_GAME);
    }

    public LoadGameMessage(Object game){
        super(ServerMessageType.LOAD_GAME);
        this.game=game;
    }

}
