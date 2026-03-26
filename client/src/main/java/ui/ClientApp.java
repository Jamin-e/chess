package ui;

import dataaccess.DataAccessException;
import model.AuthData;
import model.GameData;

import java.util.List;
import java.util.Scanner;

public class ClientApp {

    private enum State {PRELOGIN, POSTLOGIN}

    private final Scanner scanner = new Scanner(System.in);
    private State state = State.PRELOGIN;
    private final ServerFacade facade;
    private AuthData authData;
    private List<GameData> games = List.of();

    public ClientApp(int port) {
        this.facade = new ServerFacade(port);
    }

    public void run() {
        System.out.println("Chess program start");
        while (true) {
            printPrompt();
            var line = scanner.nextLine();
            if (!handleLine(line)) {
                break;
            }
        }
    }

    private void printPrompt() {
        if (state == State.PRELOGIN) {
            System.out.print("[PRELOGIN] >>> ");
        } else {
            System.out.print("[POSTLOGIN] >>> ");
        }
    }

    private boolean handleLine(String line) {
        line = line.trim();
        if (line.isEmpty()) {
            return true;
        }

        var tokens = line.split("\\s+");
        var cmd = tokens[0].toLowerCase();
        try {
            if (state == State.PRELOGIN) {
                return handlePrelogin(cmd, tokens);
            } else {
                return handlePostlogin(cmd, tokens);
            }
        } catch (Exception e) {
            System.out.println("An error has occurred: " + e);
            return true;
        }

    }

    private boolean handlePrelogin(String cmd, String[] tokens) {
        switch (cmd) {
            case "help" -> printPreloginHelp();
            case "quit" -> {
                System.out.println("Quitting now");
                return false;
            }
            case "login" -> {handleLogin(tokens);}
            case "register" -> {handleRegister(tokens);}
            default -> System.out.println("Unknown command. Type 'help' for list of commands");
        }
        return true;
    }

    private boolean handlePostlogin(String cmd, String[] tokens) {
        switch (cmd) {
            case "help" -> printPostLoginHelp();
            case "logout" -> {handleLogout(tokens);}
            case "create" -> {
                handleCreate(tokens);
            }
            case "list" -> {
                handleList(tokens);
            }
            case "play" -> {
                handlePlay(tokens);
            }
            case "observe" -> {
                handleObserve(tokens);
            }
            default -> System.out.println("Unknown command. Type 'help' for list of commands");
        }
        return true;
    }

    private void printPreloginHelp() {
        System.out.println("Commands:");
        System.out.println("login - login into an existing account");
        System.out.println("register - create a new user");
        System.out.println("quit - exit the application");
    }

    private void printPostLoginHelp() {
        System.out.println("Commands:");
        System.out.println("logout - end user session");
        System.out.println("create - create a new game session");
        System.out.println("list - list games already created");
        System.out.println("play - join game");
        System.out.println("observe - watch a game already in play");
    }

    private void handleLogin(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: login <username> <password>");
        } else {
            try {
                authData = facade.login(args[1], args[2]);
                state = State.POSTLOGIN;
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleRegister(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: register <username> <password> <email>");
        } else {
            try {
                authData = facade.register(args[1], args[2], args[3]);
                state = State.POSTLOGIN;
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleLogout(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: logout");
        } else if (authData == null){
            System.out.println("You are not logged in");
        } else {
            try {
                facade.logout(authData.authToken());
                authData = null;
                state = State.PRELOGIN;
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleList(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: list");
        } else if (authData == null) {
            System.out.println("You must login first");
        } else {
            try {
                games = facade.listGames(authData.authToken());
                for (int i = 0; i < games.size(); i++){
                    var g = games.get(i);
                    System.out.printf("%d: %s (id=%d)%n", i, g.gameName(), g.gameID());
                }
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleCreate(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: create <gameName>");
        } else if (authData == null){
            System.out.println("You must login first");
        } else {
            try {
                GameData game = facade.createGame(authData.authToken(), args[1]);
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handlePlay(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: play <gameID> <WHITE|BLACK>");
        } else if (authData == null) {
            System.out.println("You must login first");
        }
        else {
            try {
                GameData game = facade.joinGame(authData.authToken(), args[1], args[2]);
                Renderer.drawBoard(game.game(),args[2]);
            } catch (DataAccessException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void handleObserve(String[] args) {
        int num = Integer.parseInt(args[1]);
        if (args.length != 2) {
            System.out.println("Usage: observe <indexFromList>");
        } else if (authData == null){
            System.out.println("You must login first");
        } else if (num < 0 || num >= games.size()){
            System.out.println("Invalid game index");
        }
        else {
            try {
                GameData game = games.get(num);
                Renderer.drawBoard(game.game(),"WHITE");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

