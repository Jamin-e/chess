package client;

import chess.ChessBoard;
import chess.ChessGame;

public class renderer {

    public static void drawBoard(ChessGame game, ChessGame.TeamColor perspective){
        ChessBoard board = game.getBoard();
        if(perspective == ChessGame.TeamColor.WHITE){
            drawWhite(board);
        }else{
            drawBlack(board);
        }
    }
}
