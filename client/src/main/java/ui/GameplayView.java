package ui;

import chess.ChessGame;

public class GameplayView {
    public void drawBoard(ChessGame game, boolean whiteAtBottom){
        if(whiteAtBottom) {
            Renderer.drawBoard(game, "WHITE");
        } else{
            Renderer.drawBoard(game, "BLACK");
        }
    }

    public void showHelp(){
        //print command help
    }

    public void showNotification(String text){
        System.out.println(text);
    }
}
