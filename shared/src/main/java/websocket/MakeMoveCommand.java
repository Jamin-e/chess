package websocket;
import chess.ChessMove;
import websocket.commands.UserGameCommand;

public class MakeMoveCommand extends UserGameCommand {
    public ChessMove move;


    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move){
        super(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
    }
}
