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

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (!(o instanceof ChessPiece)) {return false;}
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
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
    public List<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();

        int i = myPosition.getRow()+1;
        int j = myPosition.getColumn()+1;
        while (i <= 8 && j <= 8) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                i++;
                j++;
            }
            else if(otherPiece.pieceColor==this.pieceColor){
                break;
            }
            else{
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            }
        }


        i = myPosition.getRow()-1;
        j = myPosition.getColumn()+1;
        while (i >= 1 && j <= 8) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                i--;
                j++;
            }
            else if(otherPiece.pieceColor==this.pieceColor){
                break;
            }
            else{
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            }
        }

        i = myPosition.getRow()+1;
        j = myPosition.getColumn()-1;
        while (i <= 8 && j >= 1) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                i++;
                j--;
            }
            else if(otherPiece.pieceColor==this.pieceColor){
                break;
            }
            else{
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            }
        }

        i = myPosition.getRow()-1;
        j = myPosition.getColumn()-1;
        while (i >=1 && j >=1) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                i--;
                j--;
            }
            else if(otherPiece.pieceColor==this.pieceColor){
                break;
            }
            else{
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            }
        }

        return moves;
    }

    public List<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition){
        List<ChessMove> moves = new ArrayList<>();

        int i = myPosition.getRow()+1;
        int j = myPosition.getColumn();
        while (i <= 8) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                i++;
            }
            else if(otherPiece.pieceColor==this.pieceColor){
                break;
            }
            else{
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            }
        }

        i = myPosition.getRow()-1;
        j = myPosition.getColumn();
        while (i >= 1) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                i--;
            }
            else if(otherPiece.pieceColor==this.pieceColor){
                break;
            }
            else{
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            }
        }

        i = myPosition.getRow();
        j = myPosition.getColumn()+1;
        while (j <= 8) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                j++;
            }
            else if(otherPiece.pieceColor==this.pieceColor){
                break;
            }
            else{
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            }
        }

        i = myPosition.getRow();
        j = myPosition.getColumn()-1;
        while (j >= 1) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                j--;
            }
            else if(otherPiece.pieceColor==this.pieceColor){
                break;
            }
            else{
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                break;
            }
        }


        return moves;
    }

    public List<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        List<ChessMove> moves = new ArrayList<>();

        int positioni = myPosition.getRow();
        int positionj = myPosition.getColumn();
        ChessPiece otherPiece;
        for (int i = positioni - 1; i <= positioni + 1; i++) {
            for (int j = positionj - 1; j <= positionj + 1; j++) {
                if ((i >= 1 && i <= 8) && (j >= 1 && j <= 8) && !(i == positioni && j == positionj)) {
                    otherPiece = board.getPiece(new ChessPosition(i,j));
                    if (otherPiece == null) {
                        moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                    } else if (otherPiece.pieceColor == this.pieceColor) {
                        break;
                    } else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(i, j), null));
                        break;
                    }
                }
            }


        }
        return moves;
    }

    public List<ChessMove> checkLeft(ChessBoard board, ChessPosition myPosition, int i, int j){
        List<ChessMove> moves = new ArrayList<>();
        if (j-1 >= 1){
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j-1));
            if (otherPiece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j-1), null));
            } else if (otherPiece.pieceColor == this.pieceColor) {
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j-1), null));
            }
        }
        return moves;
    }

    public List<ChessMove> checkRight(ChessBoard board, ChessPosition myPosition, int i, int j){
        List<ChessMove> moves = new ArrayList<>();
        if (j+1 <= 8){
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j+1));
            if (otherPiece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j+1), null));
            } else if (otherPiece.pieceColor == this.pieceColor) {
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(i, j+1), null));
            }
        }
        return moves;
    }

    public List<ChessMove> checkUp(ChessBoard board, ChessPosition myPosition, int i, int j){
        List<ChessMove> moves = new ArrayList<>();
        if (i+1 <= 8){
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i+1, j));
            if (otherPiece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i+1, j), null));
            } else if (otherPiece.pieceColor == this.pieceColor) {
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(i+1, j), null));
            }
        }
        return moves;
    }

    public List<ChessMove> checkDown(ChessBoard board, ChessPosition myPosition, int i, int j){
        List<ChessMove> moves = new ArrayList<>();
        if (i-1 >= 1){
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i-1, j));
            if (otherPiece == null) {
                moves.add(new ChessMove(myPosition, new ChessPosition(i-1, j), null));
            } else if (otherPiece.pieceColor == this.pieceColor) {
            } else {
                moves.add(new ChessMove(myPosition, new ChessPosition(i-1, j), null));
            }
        }
        return moves;
    }

    public List<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition){
        List<ChessMove> moves = new ArrayList<>();

        int i = myPosition.getRow()+2;
        int j = myPosition.getColumn();
        if (i <= 8) {
            moves.addAll(checkRight(board,myPosition,i,j));
            moves.addAll(checkLeft(board,myPosition,i,j));
        }

        i = myPosition.getRow()-2;
        j = myPosition.getColumn();
        if (i >= 1) {
            moves.addAll(checkRight(board,myPosition,i,j));
            moves.addAll(checkLeft(board,myPosition,i,j));
        }

        i = myPosition.getRow();
        j = myPosition.getColumn()+2;
        if (j <= 8) {
            moves.addAll(checkUp(board,myPosition,i,j));
            moves.addAll(checkDown(board,myPosition,i,j));
        }

        i = myPosition.getRow();
        j = myPosition.getColumn()-2;
        if (j >= 1) {
            moves.addAll(checkUp(board,myPosition,i,j));
            moves.addAll(checkDown(board,myPosition,i,j));
        }

        return moves;
    }

    public List<ChessMove> pawnPromote(ChessPosition myPosition, ChessPosition newPosition){
        List<ChessMove> moves = new ArrayList<>();
        moves.add(new ChessMove(myPosition, newPosition, PieceType.KNIGHT));
        moves.add(new ChessMove(myPosition, newPosition, PieceType.ROOK));
        moves.add(new ChessMove(myPosition, newPosition, PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, newPosition, PieceType.QUEEN));
        return moves;
    }

    public List<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition){
        List<ChessMove> moves = new ArrayList<>();
        if(pieceColor == ChessGame.TeamColor.BLACK) {
            if (myPosition.getRow() == 7) {
                ChessPiece otherPiece = board.getPiece(new ChessPosition(5, myPosition.getColumn()));
                ChessPiece otherPiece2 = board.getPiece(new ChessPosition(6, myPosition.getColumn()));
                if (otherPiece == null && otherPiece2 == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 2, myPosition.getColumn()), null));
                }
            }
            ChessPiece otherPiece = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()));
            if (otherPiece == null) {
                if (myPosition.getRow() - 1 == 1) {
                    ChessPosition temp = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn());
                    moves.addAll(pawnPromote(myPosition,temp));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn()), null));
                }
            }
            if(myPosition.getColumn() != 1) {
                otherPiece = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));
                if (otherPiece == null) {} else if (otherPiece.pieceColor == ChessGame.TeamColor.WHITE) {
                    if (myPosition.getRow() - 1 == 1) {
                        ChessPosition temp = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1);
                        moves.addAll(pawnPromote(myPosition,temp));
                    } else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1), null));
                    }
                }
            }
            if(myPosition.getColumn() != 8) {
                otherPiece = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
                if (otherPiece == null) {} else if (otherPiece.pieceColor == ChessGame.TeamColor.WHITE) {
                    if (myPosition.getRow() - 1 == 1) {
                       ChessPosition temp = new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1);
                        moves.addAll(pawnPromote(myPosition,temp));
                    } else {
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1), null));
                    }
                }
            }
        }
        if(pieceColor == ChessGame.TeamColor.WHITE){
            if(myPosition.getRow() == 2){
                ChessPiece otherPiece = board.getPiece(new ChessPosition(4, myPosition.getColumn()));
                ChessPiece otherPiece2 = board.getPiece(new ChessPosition(6, myPosition.getColumn()));
                if(otherPiece == null && otherPiece2 == null) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 2, myPosition.getColumn()), null));
                }
            }
                ChessPiece otherPiece = board.getPiece(new ChessPosition(myPosition.getRow()+1, myPosition.getColumn()));
                if(otherPiece == null) {
                    if(myPosition.getRow() + 1 == 8){
                        ChessPosition temp = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn());
                        moves.addAll(pawnPromote(myPosition,temp));
                    } else{
                        moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn()), null));
                    }
                }
                if(myPosition.getColumn() != 1) {
                    otherPiece = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));
                    if (otherPiece == null) {
                    } else if (otherPiece.pieceColor == ChessGame.TeamColor.BLACK) {
                        if (myPosition.getRow() + 1 == 8) {
                            ChessPosition temp =  new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1);
                            moves.addAll(pawnPromote(myPosition,temp));
                        } else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                        }
                    }
                }
                if(myPosition.getColumn() != 8) {
                    otherPiece = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
                    if (otherPiece == null) {} else if (otherPiece.pieceColor == ChessGame.TeamColor.BLACK) {
                        if (myPosition.getRow() + 1 == 8) {
                            ChessPosition temp = new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1);
                            moves.addAll(pawnPromote(myPosition,temp));
                        } else {
                            moves.add(new ChessMove(myPosition, new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1), null));
                        }
                    }
                }
        }
        return moves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        if (piece.getPieceType() == PieceType.BISHOP){
            return bishopMoves(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.ROOK){
            return rookMoves(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.QUEEN){
            List<ChessMove> temp =  rookMoves(board, myPosition);
            temp.addAll(bishopMoves( board, myPosition));
            return temp;
        }
        if (piece.getPieceType() == PieceType.KING){
            return kingMoves(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.KNIGHT){
            return knightMoves(board, myPosition);
        }
        if (piece.getPieceType() == PieceType.PAWN){
            return pawnMoves(board, myPosition);
        }
        return List.of();
    }
}
