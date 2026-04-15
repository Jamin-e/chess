package ws;

import io.javalin.websocket.WsContext;
import websocket.commands.UserGameCommand;

public class GameMessageRouter {
    private final GameService gameService;

    public GameMessageRouter(GameService gameService){
        this.gameService = gameService;
    }

    public void route(UserGameCommand command, WsContext ctx) throws Exception{
        if(command == null || command.getCommandType() == null){
            return;
        }
        switch (command.getCommandType()){
            case CONNECT -> gameService.handleConnect(command, ctx);
            case MAKE_MOVE -> gameService.handleMakeMove(command, ctx);
            case LEAVE -> gameService.handleLeave(command, ctx);
            case RESIGN -> gameService.handleResign(command, ctx);
        }
    }
}
