package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Objects;

public class Renderer {

    public static void drawBoard(ChessGame game, String perspective){
        ChessBoard board = game.getBoard();
        if(Objects.equals(perspective, "WHITE")){
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
        String empty = EscapeSequences.EMPTY;
        String empty3 = empty + empty + empty;
        System.out.println(empty3 + "a" + empty3 + "b" + empty3 + "c" + empty3 + "d" + empty3 + "e" + empty3 + "f" + empty3 + "g" + empty3 + "h");
        for(int row = 8; row >= 1; row--){
            System.out.print(" " + row + " ");
            for (int col = 1; col <= 8; col++){
                var pos = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(pos);
                String pieceStr;
                if(piece == null){
                    pieceStr = empty;
                }
                else{
                    pieceStr = pieceToStringWhite(piece);
                }

                System.out.print(empty + pieceStr + empty);
            }
            System.out.println();
            System.out.println();
        }
    }

    private static void drawBlack(ChessBoard board) {
        String empty = EscapeSequences.EMPTY;
        String empty3 = empty + empty + empty;
        System.out.println(empty + "  h" + empty3 + "g" + empty3 + "f" + empty3 + "e" + empty3 + "d" + empty3 + "c" + empty3 + "b" + empty3 + "a");
        for (int row = 1; row <= 8; row++) {
            System.out.print(" " + row + " ");
            for (int col = 8; col >= 1; col--) {
                var pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                String pieceStr;
                if(piece == null){
                    pieceStr = empty;
                }
                else{
                    pieceStr = pieceToStringBlack(piece);
                }
                System.out.print(empty + pieceStr + empty);
            }
            System.out.println();
            System.out.println();
        }
    }
}