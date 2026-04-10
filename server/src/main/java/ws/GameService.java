package ws;

import websocket.commands.UserGameCommand;

public class GameService {
    public void handleConnect(UserGameCommand command){
        //validate token and gameID
        //register socket
        //send LOAD_GAME
        //notify others
    }

    public void handleMakeMove(UserGameCommand command){
        //validate, verify move, update DB, broadcast
    }

    public void handleLeave(UserGameCommand command){
        //remove player/observer, broadcast notification
    }

    public void handleResign(UserGameCommand command){
        //mark game over, broadcast ewsigna
    }
}
