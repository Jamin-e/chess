package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public List BishopMoves(ChessPosition myPosition) {
        List moves = new ArrayList<>();

        int i = myPosition.getRow()+1;
        int j = myPosition.getColumn()+1;
        while (i <= 8 && j <= 8) {
            moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            i++;
            j++;
        }

        i = myPosition.getRow()-1;
        j = myPosition.getColumn()+1;
        while (i >= 1 && j <= 8) {
            moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            i--;
            j++;
        }

        i = myPosition.getRow()+1;
        j = myPosition.getColumn()-1;
        while (i <= 8 && j >= 1) {
            moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            i++;
            j--;
        }

        i = myPosition.getRow()-1;
        j = myPosition.getColumn()-1;
        while (i >= 1 && j >= 1) {
            moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            i--;
            j--;

        }
        return moves;
    }

    public List RookMoves(ChessPosition myPosition){
        List moves = new ArrayList<>();

        int i = myPosition.getRow()+1;
        int j = myPosition.getColumn();
        while (i <= 8) {
            moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            i++;
        }

        i = myPosition.getRow()-1;
        j = myPosition.getColumn();
        while (i >= 1) {
            moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            i--;
        }

        i = myPosition.getRow();
        j = myPosition.getColumn()+1;
        while (j <= 8) {
            moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            j++;
        }

        i = myPosition.getRow();
        j = myPosition.getColumn()-1;
        while (j >= 1) {
            moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
            j--;
        }


        return moves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.BISHOP){
            return BishopMoves(myPosition);
        }
        if (piece.getPieceType() == PieceType.ROOK){
            return RookMoves(myPosition);
        }
        return List.of();
    }
}
