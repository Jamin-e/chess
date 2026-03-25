package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import ui.EscapeSequences;

public class renderer {

    public static void drawBoard(ChessGame game, ChessGame.TeamColor perspective){
        ChessBoard board = game.getBoard();
        if(perspective == ChessGame.TeamColor.WHITE){
            drawWhite(board);
        }else{
            drawBlack(board);
        }
    }

    private static String pieceToStringWhite(ChessPiece piece){
        String c;
        switch(piece.getPieceType()){
            case KING -> c = EscapeSequences.WHITE_KING;
            case QUEEN -> c = EscapeSequences.WHITE_QUEEN;
            case ROOK -> c = EscapeSequences.WHITE_ROOK;
            case BISHOP -> c = EscapeSequences.WHITE_BISHOP;
            case KNIGHT -> c = EscapeSequences.WHITE_KNIGHT;
            case PAWN -> c = EscapeSequences.WHITE_PAWN;
            default -> c = "?";
        }
        return c;
    }

    private static String pieceToStringBlack(ChessPiece piece){
        String c;
        switch(piece.getPieceType()){
            case KING -> c = EscapeSequences.BLACK_KING;
            case QUEEN -> c = EscapeSequences.BLACK_QUEEN;
            case ROOK -> c = EscapeSequences.BLACK_ROOK;
            case BISHOP -> c = EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> c = EscapeSequences.BLACK_KNIGHT;
            case PAWN -> c = EscapeSequences.BLACK_PAWN;
            default -> c = "?";
        }
        return c;
    }

    private static void drawWhite(ChessBoard board){
        System.out.println("   a b c d e f g h");
        for(int row = 8; row >= 1; row--){
            System.out.print(" " + row + " ");
            for (int col = 1; col <= 8; col++){
                var pos = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(pos);
                String pieceStr = pieceToStringWhite(piece);
                System.out.print(EscapeSequences.EMPTY + pieceStr + EscapeSequences.EMPTY);
            }
            System.out.println();
        }
    }

    private static void drawBlack(ChessBoard board) {
        System.out.println("   h g f e d c b a");
        for (int row = 1; row <= 8; row++) {
            System.out.print(" " + row + " ");
            for (int col = 8; col >= 1; col--) {
                var pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                String pieceStr = pieceToStringBlack(piece);
                System.out.print(EscapeSequences.EMPTY + pieceStr + EscapeSequences.EMPTY);
            }
            System.out.println();
        }
    }
}