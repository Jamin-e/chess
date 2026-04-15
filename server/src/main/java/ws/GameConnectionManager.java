package ws;

import com.google.gson.Gson;
import websocket.LoadGameMessage;
import websocket.messages.ServerMessage;
import jakarta.websocket.Session;
import io.javalin.websocket.WsContext;
import java.util.concurrent.ConcurrentHashMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameConnectionManager {
    private final Map<Integer, Set<WsContext>> gameConnections = new ConcurrentHashMap<>();
    private final Gson gson = new Gson();

    public void addConnection(Integer gameID, WsContext ctx){
        gameConnections.computeIfAbsent(gameID, k -> ConcurrentHashMap.newKeySet()).add(ctx);
    }

    public void removeConnection(Integer gameID, WsContext ctx){
        Set <WsContext> sessions = gameConnections.get(gameID);
        if(sessions == null){
            return;
        }
        sessions.remove(ctx);
        if (sessions.isEmpty()){
            gameConnections.remove(gameID);
        }
    }

    public void broadcastToGame(Integer gameID, ServerMessage message) throws IOException {
        Set <WsContext> sessions = gameConnections.get(gameID);
        if (sessions == null){
            return;
        }
        String json = gson.toJson(message);
        for(WsContext ctx : sessions){
            ctx.send(json);
        }
    }

    public void broadcastToOthers(Integer gameID, WsContext exclude, ServerMessage message) throws IOException {
        Set<WsContext> sessions = gameConnections.get(gameID);
        if (sessions == null){
            return;
        }
        String json = gson.toJson(message);
        for (WsContext ctx : sessions){
            if (ctx != exclude) {
                ctx.send(json);
            }
        }
    }

    public void broadcastToRoot(WsContext ctx, ServerMessage message) throws IOException {
        send(ctx, message);
    }

    public void send(WsContext ctx, ServerMessage message) throws IOException {
        ctx.send(gson.toJson(message));
    }
}
