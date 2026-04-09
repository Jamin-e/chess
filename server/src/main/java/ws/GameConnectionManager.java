package ws;

import java.util.HashMap;
import java.util.Map;

public class GameConnectionManager {
    private final Map<Integer,Object> gameConnections = new HashMap<>();

    public void addConnection(Integer gameID, Object session){
        //tbi
    }

    public void removeConnection(Integer gameID, Object session){
        //tbi
    }

    public void broadcastToGame(Integer session, Object message){
        //tbi
    }

    public void broadcastToRoot(Object session, Object message){
        //tbi
    }
}
