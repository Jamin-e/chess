package client;

import java.util.Scanner;

public class ClientApp {

    private enum State {PRELOGIN, POSTLOGIN}

    private final Scanner scanner = new Scanner(System.in);
    private State state = State.PRELOGIN;

    public void run() {
        System.out.println("Chess program start");
        while(true){
            printPrompt();
            var line = scanner.nextLine();
            if(!handleLine(line)){
                break;
            }
        }
    }

    private void printPrompt(){
        if(state == State.PRELOGIN){
            System.out.print("[PRELOGIN] >>> ");
        }else{
            System.out.print("[POSTLOGIN] >>>");
        }
    }

    private boolean handleLine(String line){
        line = line.trim();
        if(line.isEmpty()){return true;}

        var tokens = line.split("\\s+");
        var cmd = tokens[0].toLowerCase();
        try{
           if(state == State.PRELOGIN){
               return handlePrelogin(cmd, tokens);
           }else{
               return handlePostlogin(cmd,tokens);
           }
        }catch(Exception e){
            System.out.println("An error has occurred: "+ e);
            return true;
        }

    }

    private boolean handlePrelogin(String cmd, String[] tokens){
        switch (cmd){
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

    private boolean handlePostlogin(String cmd, String[] tokens){
        switch (cmd){
            case "help" -> printPostLoginHelp();
            case "logout" ->{handleLogout(tokens);}
            case "create" ->{handleCreate(tokens);}
            case "list" ->{handleList(tokens);}
            case "play" ->{handlePlay(tokens);
            }
            case "observe" ->{handleObserve(tokens);}
            default -> System.out.println("Unknown command. Type 'help' for list of commands");
        }
        return true;
    }

    private void printPreloginHelp(){
        System.out.println("Commands:");
        System.out.println("login - login into an existing account");
        System.out.println("register - create a new user");
        System.out.println("quit - exit the application");
    }

    private void printPostLoginHelp(){
        System.out.println("Commands:");
        System.out.println("logout - end user session");
        System.out.println("create - create a new game session");
        System.out.println("list - list games already created");
        System.out.println("play - join game");
        System.out.println("observe - watch a game already in play");
    }

    private void handleLogin(String[] args){
        if(args.length != 3){
            System.out.println("Invalid number of arguments");
        }else{
            //connect to server
        }
    }

    private void handleRegister(String[] args){
        if(args.length != 4){
            System.out.println("Invalid number of arguments");
        }else{
            //connect to server
        }
    }

    private void handleLogout(String[] args){
        if(args.length != 2){
            System.out.println("Invalid number of arguments");
        }else{
            //connect to server
        }
    }

    private void handleList(String[] args){
        if(args.length != 2){
            System.out.println("Invalid number of arguments");
        }else{
            //connect to server
        }
    }

    private void handleCreate(String[] args){
        if(args.length != 3){
            System.out.println("Invalid number of arguments");
        }else{
            //connect to server
        }
    }

    private void handlePlay(String[] args){
        if(args.length != 4){
            System.out.println("Invalid number of arguments");
        }else{
            //connect to server
        }
    }

    private void handleObserve(String[] args){
        if(args.length != 3){
            System.out.println("Invalid number of arguments");
        }else{
            //connect to server
        }
    }

}
