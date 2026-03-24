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
            System.out.println("An error has occured"+ e.toString());
            return true;
        }

    }
}
