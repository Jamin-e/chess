package websocket;

import chess.ChessGame;
import websocket.messages.ServerMessage;

public class LoadGameMessage extends ServerMessage {
    public ChessGame game;

    public LoadGameMessage(){
        super(ServerMessageType.LOAD_GAME);
    }

    public LoadGameMessage(ChessGame game){
        super(ServerMessageType.LOAD_GAME);
        this.game=game;
    }

}
