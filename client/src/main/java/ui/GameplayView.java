package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import jakarta.websocket.MessageHandler;
import websocket.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.util.Collection;
import java.util.Scanner;

import static websocket.messages.ServerMessage.ServerMessageType.*;

public class GameplayView {
    private ChessGame currentGame;
    private boolean whiteAtBottom;
    private ServerFacade serverFacade;
    private String authToken;
    private int gameID;
    private boolean running = false;

    public void setServerFacade(ServerFacade serverFacade){
        this.serverFacade = serverFacade;
    }

    public void setAuthToken (String authToken){
        this.authToken = authToken;
    }

    public void setGameID(int gameID){
        this.gameID = gameID;
    }

    public void setGame(ChessGame game){
        this.currentGame = game;
    }

    public void setWhiteAtBottom(boolean whiteAtBottom){
        this.whiteAtBottom = whiteAtBottom;
    }

    public void start() {
        running = true;
        showHelp();
        drawBoard();

        Scanner scanner = new Scanner(System.in);
        while(running){
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            handleCommand(line);
        }
    }

    public void handleCommand(String line){
        if (line.isEmpty()){
            return;
        }

        String[] parts = line.split("\\s+");
        String command = parts[0].toLowerCase();

        switch (command) {
            case "help" -> showHelp();
            case "redraw" -> drawBoard();
            case "leave" -> handleLeave();
            case "resign" -> handleResign();
            case "move" -> handleMove(parts);
            case "highlight" -> handleHighlight(parts);
            default -> showError("Error: Unknown command.");
        }

    }

    public void handleMove(String[] parts){
        if (parts.length != 3){
            showError("Usage: move <start> <end>");
            return;
        }
        ChessPosition start = parsePosition(parts[1]);
        ChessPosition end = parsePosition(parts[2]);

        if(start == null || end == null){
            showError("Invalid position format");
            return;
        }
        ChessMove move = new ChessMove(start,end,null);
        serverFacade.makeMove(authToken,gameID,move);
    }

    public void handleHighlight(String[] parts){
        if (parts.length != 2){
            showError("Usage: highlight <square>");
            return;
        }
        ChessPosition position = parsePosition(parts[1]);
        if (position == null){
            showError("Invalid position format");
            return;
        }
        highlightLegalMoves(position);
    }

    public void handleLeave(){
        serverFacade.leaveGame(authToken,gameID);
        running = false;
    }

    public void handleResign(){
        System.out.print("resign? y/n");
        Scanner scanner = new Scanner(System.in);
        String in = scanner.nextLine().trim().toLowerCase();

        if(in.equals("y") || in.equals("yes")){
            serverFacade.resignGame(authToken,gameID);
        }
    }

    public void notify(ServerMessage message) {
        case LOAD_GAME -> {
            setGame(extractGame(message));
            drawBoard();
        case NOTIFICATION -> showNotification(message.message);
        case ERROR -> showError(message.errorMessage);
        }
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
        if (currentGame == null) {
            showError("No game loaded");
            return;
        }
        Collection<ChessMove> moves = currentGame.validMoves(position);
        Renderer.drawBoardWithHighlights(currentGame, position, moves, whiteAtBottom ? "WHITE" : "BLACK");
    }

    private ChessPosition parsePosition(String text){
        if (text.length() !=2 ) return null;
        char file = Character.toLowerCase(text.charAt(0));
        char rank = text.charAt(1);
        int col = file - 'a' + 1;
        int row = Character.getNumericValue(rank);
        if (col < 1 || col > 8 || row < 1 || row > 8){
            return null;
        }
        return new ChessPosition(row,col);
    }
}
