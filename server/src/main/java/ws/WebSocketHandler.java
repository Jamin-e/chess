package ws;

import com.google.gson.Gson;
import dataaccess.DataAccess;
import io.javalin.websocket.*;

import websocket.commands.UserGameCommand;

import java.util.concurrent.ConcurrentHashMap;

public class WebSocketHandler {
    private final Gson gson = new Gson();
    private final GameMessageRouter router;
    private final GameConnectionManager connectionManager = new GameConnectionManager();
    private final GameService service;

    private final ConcurrentHashMap<WsContext, Integer> socketGame = new ConcurrentHashMap<>();

    public WebSocketHandler(DataAccess dataAccess){
        this.service = new GameService(dataAccess, connectionManager);
        this.router = new GameMessageRouter(service);
    }


    public void onMessage(WsMessageContext ctx) throws Exception{
        UserGameCommand command = gson.fromJson(ctx.message(), UserGameCommand.class);
        if (command != null && command.getCommandType() == UserGameCommand.CommandType.CONNECT && command.getGameID() != null) {
            socketGame.put(ctx, command.getGameID());
        }

        router.route(command, ctx);
    }

    public void onClose(WsCloseContext ctx) {
        Integer gameID = socketGame.remove(ctx);
        if (gameID != null) {
            connectionManager.removeConnection(gameID, ctx);
        }
    }

    public void onError(WsErrorContext ctx) {
        System.out.println(ctx.toString());
    }

    public void onConnect(WsConnectContext ctx){
    }
}
