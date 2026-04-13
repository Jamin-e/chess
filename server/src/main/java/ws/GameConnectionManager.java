package ws;

import websocket.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameConnectionManager {
    private final Map<Integer, Set<Object>> gameConnections = new HashMap<>();

    public void addConnection(Integer gameID, Object session){
        gameConnections.computeIfAbsent(gameID, k -> new HashSet<>()).add(session);
    }

    public void removeConnection(Integer gameID, Object session){
        Set <Object> sessions = gameConnections.get(gameID);
        if(sessions == null){
            return;
        }
        sessions.remove(session);
        if (sessions.isEmpty()){
            gameConnections.remove(gameID);
        }
    }

    public void broadcastToGame(Integer gameID, ServerMessage message){
        Set <Object> sessions = gameConnections.get(gameID);
        if (sessions == null){
            return;
        }
        for(Object session : sessions){
            send(session, message);
        }
    }

    public void broadcastToRoot(Object session, Object message){
        send(session, message);
    }

    public void send(Object session, ServerMessage message){
        // serialize message and send via websocket session
    }
}
