package ws;

import websocket.commands.UserGameCommand;

public class GameMessageRouter {
    private final GameService gameService = new GameService();

    public void route(UserGameCommand command){
        switch (command.getCommandType()){
            case CONNECT -> gameService.handleConnect(command);
            case MAKE_MOVE -> gameService.handleMakeMove(command);
            case LEAVE -> gameService.handleLeave(command);
            case RESIGN -> gameService.handleResign(command);
        }
    }
}
