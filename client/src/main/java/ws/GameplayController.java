package ws;

import chess.ChessMove;
import websocket.MakeMoveCommand;
import websocket.commands.UserGameCommand;

public class GameplayController {
    private final WebSocketClient webSocketClient;

    public GameplayController(WebSocketClient webSocketClient){
        this.webSocketClient = webSocketClient;
    }

    public void connect(String authToken, Integer gameID){
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        webSocketClient.sendCommand(command);
    }

    public void makeMove(String authToken, Integer gameID, ChessMove move){
        MakeMoveCommand command = new MakeMoveCommand(authToken, gameID, move);
        webSocketClient.sendCommand(command);
    }

    public void leave(String authToken, Integer gameID){
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        webSocketClient.sendCommand(command);
    }

    public void resign(String authToken, Integer gameID){
        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        webSocketClient.sendCommand(command);
    }
}
