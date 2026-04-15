package ws;

import chess.ChessGame;
import org.eclipse.jetty.server.Authentication;

public class GameRecord {
    private ChessGame game;

    public ChessGame getGame() {
        return game;
    }

    public String getRoleFor(Authentication.User user){
        return "oberver";
    }
}
