package chess;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor current_turn = TeamColor.WHITE;
    private ChessBoard board = new ChessBoard();
    List moves;
    private ChessPosition white_king = new ChessPosition(1,5);
    private ChessPosition black_king = new ChessPosition(8,5);

    public ChessGame() {
    this.board.resetBoard();
    moves = new ArrayList<>();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return current_turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        if(team == TeamColor.WHITE){
            current_turn = TeamColor.BLACK;
        }
        else{
            current_turn = TeamColor.WHITE;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return current_turn == chessGame.current_turn && Objects.equals(board, chessGame.board) && Objects.equals(moves, chessGame.moves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(current_turn, board, moves);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "current_turn=" + current_turn +
                ", board=" + board +
                ", moves=" + moves +
                '}';
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if(piece != null){
            moves.addAll(piece.pieceMoves(board, startPosition));
            ChessBoard temp = board.deepCopy();
            for(Object move : moves){
                makeMovetemp((ChessMove) move, temp);
                if(isInCheck(piece.getTeamColor())){
                    moves.remove(move);
                }
            }

            return moves;
        }
        return null;
    }

    public void makeMovetemp(ChessMove move, ChessBoard temp) {
        ChessPiece piece = temp.getPiece(move.getStartPosition());
        temp.addPiece(move.getStartPosition(),null);
        if (move.getPromotionPiece() == null) {
            temp.addPiece(move.getEndPosition(), piece);
        }
        else{
            ChessPiece promoted = new ChessPiece(piece.getTeamColor(),move.getPromotionPiece());
            temp.addPiece(move.getEndPosition(),promoted);
        }
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        board.addPiece(move.getStartPosition(),null);
        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), piece);
            if(piece.getPieceType() == ChessPiece.PieceType.KING){
                if(piece.getTeamColor() == TeamColor.BLACK){
                    black_king = move.getEndPosition();
                }
                else{
                    white_king = move.getEndPosition();
                }

            }
        }
        else{
            ChessPiece promoted = new ChessPiece(piece.getTeamColor(),move.getPromotionPiece());
            board.addPiece(move.getEndPosition(),promoted);
        }
        setTeamTurn(piece.getTeamColor());
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition king;
        if(teamColor == TeamColor.BLACK){
             king = black_king;
        }
        else{
            king = white_king;
        }
        if(BishopCheck(board,king,teamColor) == true){
            return true;
        }
        return false;
    }

    public boolean BishopCheck(ChessBoard board, ChessPosition myPosition, TeamColor pieceColor) {


        int i = myPosition.getRow()+1;
        int j = myPosition.getColumn()+1;
        while (i <= 8 && j <= 8) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
            }
            else if(otherPiece.getTeamColor()!=pieceColor && (otherPiece.getPieceType() == ChessPiece.PieceType.BISHOP || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
        }


        i = myPosition.getRow()-1;
        j = myPosition.getColumn()+1;
        while (i >= 1 && j <= 8) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
            }
            else if(otherPiece.getTeamColor()!=pieceColor && (otherPiece.getPieceType() == ChessPiece.PieceType.BISHOP || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
        }

        i = myPosition.getRow()+1;
        j = myPosition.getColumn()-1;
        while (i <= 8 && j >= 1) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
            }
            else if(otherPiece.getTeamColor()!=pieceColor && (otherPiece.getPieceType() == ChessPiece.PieceType.BISHOP || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
        }

        i = myPosition.getRow()-1;
        j = myPosition.getColumn()-1;
        while (i >=1 && j >=1) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
            }
            else if(otherPiece.getTeamColor()!=pieceColor && (otherPiece.getPieceType() == ChessPiece.PieceType.BISHOP || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
        }

        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
    return board;
    }
}
