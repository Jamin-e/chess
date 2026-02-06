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


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return current_turn == chessGame.current_turn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(current_turn, board);
    }

    public ChessGame() {
    this.board.resetBoard();
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
            current_turn = team;
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
        ArrayList<ChessMove> moves;
        moves = new ArrayList<>();
        ArrayList<ChessMove> moves_to_check;
        moves_to_check = new ArrayList<>();
        if(piece != null){
            //get all moves before check checks
            moves_to_check.addAll(piece.pieceMoves(board, startPosition));
            //make a copy of the board and for all moves make sure it doesn't put own king in check or can't get king out of check
            for(ChessMove move : moves_to_check){
                ChessBoard copy = board.deepCopy();
                makeMovetemp(move, copy);
                if(!isInCheck(piece.getTeamColor(), copy)){
                    moves.add(move);
                }
            }

            return moves;
        }
        return null;
    }

    public void makeMovetemp(ChessMove move, ChessBoard copy) {
        ChessPiece piece = copy.getPiece(move.getStartPosition());
        copy.addPiece(move.getStartPosition(),null);
        if (move.getPromotionPiece() == null) {
            copy.addPiece(move.getEndPosition(), piece);
        }
        else{
            ChessPiece promoted = new ChessPiece(piece.getTeamColor(),move.getPromotionPiece());
            copy.addPiece(move.getEndPosition(),promoted);
        }
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()) == null){
            throw new InvalidMoveException("empty");
        }
        ChessPiece piece = board.getPiece(move.getStartPosition());
        ArrayList valid_moves = new ArrayList<>();
        valid_moves.addAll(validMoves(move.getStartPosition()));
        if(piece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("wrong team");
        }
        if(!valid_moves.contains(move)){
            throw new InvalidMoveException("no valid moves");
        }
        ChessPosition temp = move.getStartPosition();
        board.addPiece(temp,null);
        if (move.getPromotionPiece() == null) {
            board.addPiece(move.getEndPosition(), piece);
        }
        else{
            ChessPiece promoted = new ChessPiece(piece.getTeamColor(),move.getPromotionPiece());
            board.addPiece(move.getEndPosition(),promoted);
        }
        if(piece.getTeamColor() == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        }
        else{
            setTeamTurn(TeamColor.WHITE);
        }
    }


    public ChessPosition findKing(TeamColor teamColor, ChessBoard copy){
        for(int i = 1; i <= 8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPiece temp = copy.getPiece(new ChessPosition(i,j));
                if(temp == null){
                }
                else if(temp.equals(new ChessPiece(teamColor, ChessPiece.PieceType.KING))){
                    return new ChessPosition(i,j);
                }
            }
        }
        return null;
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, board);
    }
    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        //each helper function starts at the kings position and checks along the routes for a piece of that type and opposite color
        ChessPosition king;
        if(teamColor == TeamColor.BLACK){
             king = findKing(TeamColor.BLACK, board);
        }
        else{
            king = findKing(TeamColor.WHITE, board);
        }
        if(BishopCheck(board,king,teamColor)){
            return true;
        }
        if(RookCheck(board,king,teamColor)){
            return true;
        }
        if (KnightCheck(board, king, teamColor)) {
            return true;
        }
        if(PawnCheck(board, king, teamColor)){
            return true;
        }
        if(KingCheck(board, king, teamColor)){
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
            i++;
            j++;
            }
            else if(otherPiece.getTeamColor()!=pieceColor && (otherPiece.getPieceType() == ChessPiece.PieceType.BISHOP || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
            else{
                break;
            }
        }


        i = myPosition.getRow()-1;
        j = myPosition.getColumn()+1;
        while (i >= 1 && j <= 8) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
            i--;
            j++;
            }
            else if(otherPiece.getTeamColor()!=pieceColor && (otherPiece.getPieceType() == ChessPiece.PieceType.BISHOP || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
            else{
                break;
            }
        }

        i = myPosition.getRow()+1;
        j = myPosition.getColumn()-1;
        while (i <= 8 && j >= 1) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                i++;
                j--;
            }
            else if(otherPiece.getTeamColor()!=pieceColor && (otherPiece.getPieceType() == ChessPiece.PieceType.BISHOP || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
            else{
                break;
            }
        }

        i = myPosition.getRow()-1;
        j = myPosition.getColumn()-1;
        while (i >=1 && j >=1) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                i--;
                j--;
            }
            else if(otherPiece.getTeamColor()!=pieceColor && (otherPiece.getPieceType() == ChessPiece.PieceType.BISHOP || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
            else{
                break;
            }
        }

        return false;
    }

    public boolean RookCheck(ChessBoard board, ChessPosition myPosition, TeamColor pieceColor){

        int i = myPosition.getRow()+1;
        int j = myPosition.getColumn();;
        while (i <= 8) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                i++;
            }
            else if(otherPiece.getTeamColor()!=pieceColor && (otherPiece.getPieceType() == ChessPiece.PieceType.ROOK || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
            else{
                break;
            }
        }

        i = myPosition.getRow()-1;
        j = myPosition.getColumn();
        while (i >= 1) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                i--;
            }
            else if(otherPiece.getTeamColor()!=pieceColor && (otherPiece.getPieceType() == ChessPiece.PieceType.ROOK || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
            else{
                break;
            }
        }

        i = myPosition.getRow();
        j = myPosition.getColumn()+1;
        while (j <= 8) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                j++;
            }
            else if(otherPiece.getTeamColor()!=pieceColor && (otherPiece.getPieceType() == ChessPiece.PieceType.ROOK || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
            else{
                break;
            }
        }

        i = myPosition.getRow();
        j = myPosition.getColumn()-1;
        while (j >= 1) {
            ChessPiece otherPiece = board.getPiece(new ChessPosition(i,j));
            if(otherPiece == null) {
                j--;
            }
            else if(otherPiece.getTeamColor()!=pieceColor && (otherPiece.getPieceType() == ChessPiece.PieceType.ROOK || otherPiece.getPieceType() == ChessPiece.PieceType.QUEEN)){
                return true;
            }
            else{
                break;
            }
        }


        return false;
    }

    public boolean KnightCheck(ChessBoard board, ChessPosition myPosition, TeamColor pieceColor){

        int i = myPosition.getRow()+2;
        int j = myPosition.getColumn();
        if (i <= 8) {
            if (j+1 <= 8) {
                ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j + 1));
                if (otherPiece == null) {
                } else if (otherPiece.getTeamColor() != pieceColor && otherPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
            if (j-1 >= 1){
                ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j-1));
                if (otherPiece == null) {
                } else if (otherPiece.getTeamColor()!=pieceColor && otherPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
        }

        i = myPosition.getRow()-2;
        j = myPosition.getColumn();
        if (i >= 1) {
            if (j+1 <= 8){
                ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j+1));
                if (otherPiece == null) {
                } else if (otherPiece.getTeamColor()!=pieceColor && otherPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
            if (j-1 >= 1){
                ChessPiece otherPiece = board.getPiece(new ChessPosition(i, j-1));
                if (otherPiece == null) {
                } else if (otherPiece.getTeamColor()!=pieceColor && otherPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
        }

        i = myPosition.getRow();
        j = myPosition.getColumn()+2;
        if (j <= 8) {
            if (i+1 <= 8){
                ChessPiece otherPiece = board.getPiece(new ChessPosition(i+1, j));
                if (otherPiece == null) {
                } else if (otherPiece.getTeamColor()!=pieceColor && otherPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
            if (i-1 >= 1) {
                ChessPiece otherPiece = board.getPiece(new ChessPosition(i - 1, j));
                if (otherPiece == null) {
                } else if (otherPiece.getTeamColor() != pieceColor && otherPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
        }

        i = myPosition.getRow();
        j = myPosition.getColumn()-2;
        if (j >= 1) {
            if (i+1 <= 8){
                ChessPiece otherPiece = board.getPiece(new ChessPosition(i+1, j));
                if (otherPiece == null) {
                } else if (otherPiece.getTeamColor()!=pieceColor && otherPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
            if (i-1 >= 1) {
                ChessPiece otherPiece = board.getPiece(new ChessPosition(i - 1, j));
                if (otherPiece == null) {
                } else if (otherPiece.getTeamColor() != pieceColor && otherPiece.getPieceType() == ChessPiece.PieceType.KNIGHT) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean PawnCheck(ChessBoard board, ChessPosition myPosition, TeamColor pieceColor){


        if(pieceColor == ChessGame.TeamColor.BLACK) {

            if (myPosition.getRow() != 1) {

                if (myPosition.getColumn() != 1) {
                    ChessPiece otherPiece = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() - 1));

                    if (otherPiece == null) {
                    } else if ((otherPiece.getTeamColor() == ChessGame.TeamColor.WHITE) && (otherPiece.getPieceType() == ChessPiece.PieceType.PAWN)) {
                        return true;
                    }
                }

                if (myPosition.getColumn() != 8) {
                    ChessPiece otherPiece = board.getPiece(new ChessPosition(myPosition.getRow() - 1, myPosition.getColumn() + 1));
                    if (otherPiece == null) {
                    } else if ((otherPiece.getTeamColor() == ChessGame.TeamColor.WHITE) && (otherPiece.getPieceType() == ChessPiece.PieceType.PAWN)) {
                        return true;
                    }
                }
            }
        }


        if(pieceColor == ChessGame.TeamColor.WHITE) {

            if(myPosition.getRow() != 8) {

                if (myPosition.getColumn() != 1) {
                    ChessPiece otherPiece = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() - 1));

                    if (otherPiece == null) {
                    } else if ((otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK) && (otherPiece.getPieceType() == ChessPiece.PieceType.PAWN)) {
                        return true;
                    }
                }

                if (myPosition.getColumn() != 8) {
                    ChessPiece otherPiece = board.getPiece(new ChessPosition(myPosition.getRow() + 1, myPosition.getColumn() + 1));
                    if (otherPiece == null) {
                    } else if ((otherPiece.getTeamColor() == ChessGame.TeamColor.BLACK) && (otherPiece.getPieceType() == ChessPiece.PieceType.PAWN)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean KingCheck(ChessBoard board, ChessPosition myPosition, TeamColor pieceColor) {

        int positioni = myPosition.getRow();
        int positionj = myPosition.getColumn();
        ChessPiece otherPiece = board.getPiece(new ChessPosition(positioni, positionj));
        for (int i = positioni - 1; i <= positioni + 1; i++) {
            for (int j = positionj - 1; j <= positionj + 1; j++) {
                if ((i >= 1 && i <= 8) && (j >= 1 && j <= 8) && !(i == positioni && j == positionj)) {
                    otherPiece = board.getPiece(new ChessPosition(i,j));
                    if (otherPiece == null) {
                    } else if ((otherPiece.getTeamColor() != pieceColor) && otherPiece.getPieceType() == ChessPiece.PieceType.KING) {
                        return true;
                    }
                }
            }


        }
        return false;
    }



    public Collection<ChessMove> getAllValidMoves(TeamColor teamColor){
        ArrayList<ChessMove> moves;
        moves = new ArrayList<>();
        for(int i = 1; i <=8; i++){
            for(int j = 1; j <= 8; j++){
                ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                if(piece == null){
                }
                else if(piece.getTeamColor() == teamColor){
                    moves.addAll(validMoves(new ChessPosition(i,j)));
                }
            }
        }
        return moves;
    }
    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }
        ArrayList<ChessMove> moves;
        moves = new ArrayList<>();
        moves.addAll(getAllValidMoves(teamColor));
        if(moves.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            ArrayList<ChessMove> moves;
            moves = new ArrayList<>();
            moves.addAll(getAllValidMoves(teamColor));
            if(moves.isEmpty()){
                return true;
            }
        }

        return false;
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
