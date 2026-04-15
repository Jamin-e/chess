package ws;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import dataaccess.DataAccess;
import model.AuthData;
import model.GameData;
import websocket.ErrorMessage;
import websocket.LoadGameMessage;
import websocket.NotificationMessage;
import websocket.commands.UserGameCommand;

import io.javalin.websocket.WsContext;

import java.util.concurrent.ConcurrentHashMap;

public class GameService {

    private final DataAccess dataAccess;
    private final GameConnectionManager connections;

    public GameService(DataAccess dataAccess, GameConnectionManager connect) {
        this.dataAccess = dataAccess;
        this.connections = connect;
    }
    private final ConcurrentHashMap<Integer, ChessGame> games = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Object> locks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Boolean> gameOver = new ConcurrentHashMap<>();


    public void handleConnect(UserGameCommand command, WsContext ctx) throws Exception {
        try {
            String username = requireUser(command.getAuthToken());
            GameData gameData = requireGame(command.getGameID());

            // join connection group only on successful validation
            connections.addConnection(command.getGameID(), ctx);

            ChessGame game = games.computeIfAbsent(command.getGameID(), id -> gameData.game());
            connections.send(ctx, new LoadGameMessage(game));

            String role = roleFor(username, gameData);
            connections.broadcastToOthers(command.getGameID(), ctx, new NotificationMessage(username + " joined as " + role));
        } catch (Exception e) {
            connections.send(ctx, new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    public void handleMakeMove(UserGameCommand command, WsContext ctx) throws Exception{
        try {
            int gameID = command.getGameID();
            String username = requireUser(command.getAuthToken());
            GameData gameData = requireGame(gameID);

            if (Boolean.TRUE.equals(gameOver.get(gameID))) {
                throw new Exception("game is over");
            }
            if (command.move == null) {
                throw new Exception("missing move");
            }

            ChessGame.TeamColor playerColor = playerColorFor(username, gameData);
            if (playerColor == null) {
                throw new Exception("observer cannot move");
            }

            Object lock = locks.computeIfAbsent(gameID, k -> new Object());
            synchronized (lock) {
                ChessGame game = games.computeIfAbsent(gameID, id -> gameData.game());

                if (Boolean.TRUE.equals(gameOver.get(gameID))) {
                    throw new Exception("game is over");
                }

                if (game.getTeamTurn() != playerColor) {
                    throw new Exception("not your turn");
                }

                ChessPiece startPiece = game.getBoard().getPiece(command.move.getStartPosition());
                if (startPiece == null || startPiece.getTeamColor() != playerColor) {
                    throw new Exception("cannot move opponent piece");
                }

                try {
                    game.makeMove(command.move);
                } catch (InvalidMoveException ex) {
                    throw new Exception("invalid move");
                }

                dataAccess.updateGame(gameID, game);

                // broadcast updated game to everyone in-game
                connections.broadcastToGame(gameID, new LoadGameMessage(game));

                // notify everyone except sender about the move
                connections.broadcastToOthers(gameID, ctx,
                        new NotificationMessage(username + " moved"));

                // extra notification ONLY for checkmate (passoff expects this)
                if (game.isInCheckmate(game.getTeamTurn())) {
                    gameOver.put(gameID, true);
                    connections.broadcastToGame(gameID, new NotificationMessage("Checkmate"));
                }
            }
        } catch (Exception e) {
            connections.send(ctx, new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    public void handleLeave(UserGameCommand command, WsContext ctx) throws Exception{
            try {
                int gameID = command.getGameID();
                String username = requireUser(command.getAuthToken());
                requireGame(gameID);

                connections.removeConnection(gameID, ctx);

                 dataAccess.leaveGame(gameID, username);

                connections.broadcastToOthers(gameID, ctx, new NotificationMessage(username + " left the game"));
            } catch (Exception e) {
                connections.send(ctx, new ErrorMessage("Error: " + e.getMessage()));
            }
        }

    public void handleResign(UserGameCommand command, WsContext ctx) throws Exception{
        try {
            int gameID = command.getGameID();
            String username = requireUser(command.getAuthToken());
            GameData gameData = requireGame(gameID);

            if (Boolean.TRUE.equals(gameOver.get(gameID))) {
                throw new Exception("game is over");
            }

            ChessGame.TeamColor playerColor = playerColorFor(username, gameData);
            if (playerColor == null) {
                throw new Exception("observer cannot resign");
            }

            gameOver.put(gameID, true);


            connections.broadcastToGame(gameID, new NotificationMessage(username + " resigned"));
        } catch (Exception e) {
            connections.send(ctx, new ErrorMessage("Error: " + e.getMessage()));
        }
    }

    private String requireUser(String authToken) throws Exception {
        if (authToken == null || authToken.isBlank()) {
            throw new Exception("unauthorized");
        }
        AuthData auth = dataAccess.getAuth(authToken);
        if (auth == null) {
            throw new Exception("unauthorized");
        }
        return auth.username();
    }

    private GameData requireGame(Integer gameID) throws Exception {
        if (gameID == null) {
            throw new Exception("bad gameID");
        }
        GameData game = dataAccess.getGame(gameID);
        if (game == null) {
            throw new Exception("bad gameID");
        }
        return game;
    }

    private ChessGame.TeamColor playerColorFor(String username, GameData gameData) {
        if (username == null) { return null; }
        if (username.equals(gameData.whiteUsername())) { return ChessGame.TeamColor.WHITE; }
        if (username.equals(gameData.blackUsername())) { return ChessGame.TeamColor.BLACK; }
        return null;
    }

    private String roleFor(String username, GameData gameData) {
        ChessGame.TeamColor color = playerColorFor(username, gameData);
        if (color == ChessGame.TeamColor.WHITE) { return "WHITE"; }
        if (color == ChessGame.TeamColor.BLACK) { return "BLACK"; }
        return "OBSERVER";
    }
//
//    public void sendError(Session session, String text) throws IOException {
//        connectionManager.broadcastToRoot(session, new ErrorMessage(text));
//    }
//
//    private String buildConnectMessage(User user, GameRecord gameRecord) {
//        return user.getUserIdentity() + " joined as " + gameRecord.getRoleFor(user);
//    }
//
//    private String buildMoveMessage(User user, ChessMove move) {
//        return user.getUserIdentity() + " moved from " + move.getStartPosition() + " to " + move.getEndPosition();
//    }
//
//    private String validateMove(GameRecord gameRecord, User user, ChessMove move) {
//        return null;
//    }
//
//    private void applyMove(GameRecord gameRecord, ChessMove move) {
//    }
//
//    private boolean isCheck(GameRecord gameRecord) {
//        return false;
//    }
//
//    private boolean isCheckmate(GameRecord gameRecord) {
//        return false;
//    }
//
//    private boolean isStalemate(GameRecord gameRecord) {
//        return false;
//    }
//
//    private void removeUserFromGame(GameRecord gameRecord, User user) {
//    }
//
//    private void markGameOver(GameRecord gameRecord) {
//    }
}
