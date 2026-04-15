package ui;

import chess.*;

import java.util.Collection;
import java.util.HashSet;
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

    public static Collection<ChessPosition> getSinglePieceMoves(ChessPosition start, Collection<ChessMove> moves){
        if (start == null || moves == null || moves.isEmpty()) {
            return java.util.List.of();
        }

        Collection<ChessPosition> validMoves = new HashSet<>();
        for (ChessMove move : moves) {
            if (start.equals(move.getStartPosition())) {
                validMoves.add(move.getEndPosition());
            }
        }
        return validMoves;
    }

    public static void drawBoardWithHighlights(ChessGame game, ChessPosition start, Collection<ChessMove> legalMoves, String perspective){
        String empty = EscapeSequences.EMPTY;
        Collection<ChessPosition> validSpecificMoves = getSinglePieceMoves(start, legalMoves);
        boolean whitePerspective = Objects.equals(perspective, "WHITE");

        System.out.print("   ");
        if (whitePerspective) {
            for (char c = 'a'; c <= 'h'; c++) {
                System.out.print("  " + c + "   ");
            }
        } else {
            for (char c = 'h'; c >= 'a'; c--) {
                System.out.print("  " + c + "   ");
            }
        }
        System.out.println();

        int rowStart = whitePerspective ? 8 : 1;
        int rowEndExclusive = whitePerspective ? 0 : 9;
        int rowStep = whitePerspective ? -1 : 1;

        for (int row = rowStart; row != rowEndExclusive; row += rowStep) {
            System.out.print(" " + row + " ");

            int colStart = whitePerspective ? 1 : 8;
            int colEndExclusive = whitePerspective ? 9 : 0;
            int colStep = whitePerspective ? 1 : -1;

            for (int col = colStart; col != colEndExclusive; col += colStep) {
                boolean isWhiteSquare = (row + col) % 2 == 0;
                var pos = new ChessPosition(row, col);
                boolean isValidMove = validSpecificMoves.contains(pos);
                boolean isStart = start != null && start.equals(pos);

                ChessPiece piece = game.getBoard().getPiece(pos);
                String pieceStr = (piece == null) ? empty : pieceToString(piece);

                String bg = isWhiteSquare ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY
                        : EscapeSequences.SET_BG_COLOR_BLACK;
                if (isStart) {
                    bg = EscapeSequences.SET_BG_COLOR_YELLOW;
                } else if (isValidMove) {
                    bg = EscapeSequences.SET_BG_COLOR_GREEN;
                }

                System.out.print(bg + " " + pieceStr + " " + EscapeSequences.RESET_BG_COLOR);
            }
            System.out.println();
        }
    }

    private static String pieceToString(ChessPiece piece) {
        return piece.getTeamColor() == ChessGame.TeamColor.WHITE
                ? pieceToStringWhite(piece)
                : pieceToStringBlack(piece);
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
        System.out.print("   ");
        for (char c = 'a'; c <= 'h'; c++) {
            System.out.print("  " + c + "   ");
        }
        System.out.println();
        for(int row = 8; row >= 1; row--){
            System.out.print(" " + row + " ");
            for (int col = 1; col <= 8; col++){
                boolean isWhiteSquare = (row + col) % 2 == 0;
                var pos = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(pos);
                String pieceStr;
                if(piece == null){
                    pieceStr = empty;
                }
                else{
                    pieceStr = pieceToString(piece);
                }
                String bg = isWhiteSquare ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY
                        : EscapeSequences.SET_BG_COLOR_BLACK;
                System.out.print(bg + " " + pieceStr + " " + EscapeSequences.RESET_BG_COLOR);
            }
            System.out.println();
        }
    }

    private static void drawBlack(ChessBoard board) {
        String empty = EscapeSequences.EMPTY;
        System.out.print("   ");
        for (char c = 'h'; c >= 'a'; c--) {
            System.out.print("  " + c + "   ");
        }
        System.out.println();
        for (int row = 1; row <= 8; row++) {
            System.out.print(" " + row + " ");
            for (int col = 8; col >= 1; col--) {
                boolean isWhiteSquare = (row + col) % 2 == 0;
                var pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                String pieceStr;
                if(piece == null){
                    pieceStr = empty;
                }
                else{
                    pieceStr = pieceToString(piece);
                }
                String bg = isWhiteSquare ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY
                        : EscapeSequences.SET_BG_COLOR_BLACK;
                System.out.print(bg + " " + pieceStr + " " + EscapeSequences.RESET_BG_COLOR);
            }
            System.out.println();
        }
    }
}
