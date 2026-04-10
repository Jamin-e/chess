package ws;

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
        if (gameConnections.containsKey(gameID)){
            gameConnections.get(gameID).remove(session);
        }
    }

    public void broadcastToGame(Integer session, Object message){
        //send to all sessions
    }

    public void broadcastToRoot(Object session, Object message){
        //send only to root client
    }
}
