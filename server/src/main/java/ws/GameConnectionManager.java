package ws;

import java.util.HashMap;
import java.util.Map;

public class GameConnectionManager {
    private final Map<Integer,Object> gameConnections = new HashMap<>();

    public void addConnection(Integer gameID, Object session){
        gameConnections.put(gameID, session);
    }

    public void removeConnection(Integer gameID, Object session){
        gameConnections.remove(gameID,session);
    }

    public void broadcastToGame(Integer session, Object message){
        //send to all sessions
    }

    public void broadcastToRoot(Object session, Object message){
        //send only to root client
    }
}
