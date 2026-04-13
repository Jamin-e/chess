package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.Collection;

public class GameplayView {
    private ChessGame currentGame;
    private boolean whiteAtBottom;

    public void setGame(ChessGame game){
        this.currentGame = game;
    }

    public void setWhiteAtBottom(boolean whiteAtBottom){
        this.whiteAtBottom = whiteAtBottom;
    }

    public void drawBoard(){
        if (currentGame == null){
            System.out.println("No game loaded");
            return;
        }
        if(whiteAtBottom) {
            Renderer.drawBoard(currentGame, "WHITE");
        } else{
            Renderer.drawBoard(currentGame, "BLACK");
        }
    }

    public void drawBoard(ChessGame game, boolean whiteAtBottom) {
        this.currentGame = game;
        this.whiteAtBottom = whiteAtBottom;
        drawBoard();
    }

    public void showHelp(){
        System.out.println("Commands:");
        System.out.println("help - display this help text");
        System.out.println("redraw - redraw the board");
        System.out.println("leave - leave the game");
        System.out.println("move - make a move");
        System.out.println("resign - resign the game");
        System.out.println("highlight - highlight legal moves for a piece");
    }

    public void showNotification(String text){
        System.out.println(text);
    }

    public void showError(String text){
        System.out.println(text);
    }

    public void highlightLegalMoves(ChessPosition position) {
        if (currentGame == null) return;
        Collection<ChessMove> moves = currentGame.validMoves(position);
        // redraw board with highlighted squares
    }
}
