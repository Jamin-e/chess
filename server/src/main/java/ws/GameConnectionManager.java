package ws;

import com.google.gson.Gson;
import websocket.LoadGameMessage;
import websocket.messages.ServerMessage;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameConnectionManager {
    private final Map<Integer, Set<Session>> gameConnections = new HashMap<>();
    private final Gson gson = new Gson();

    public void addConnection(Integer gameID, Session session){
        gameConnections.computeIfAbsent(gameID, k -> new HashSet<>()).add(session);
    }

    public void removeConnection(Integer gameID, Session session){
        Set <Session> sessions = gameConnections.get(gameID);
        if(sessions == null){
            return;
        }
        sessions.remove(session);
        if (sessions.isEmpty()){
            gameConnections.remove(gameID);
        }
    }

    public void broadcastToGame(Integer gameID, ServerMessage message) throws IOException {
        Set <Session> sessions = gameConnections.get(gameID);
        if (sessions == null){
            return;
        }
        for(Session session : sessions){
            send(session, message);
        }
    }

    public void broadcastToOthers(Integer gameID, Session excludeSession, ServerMessage message) throws IOException {
        Set<Session> sessions = gameConnections.get(gameID);
        if (sessions == null){
            return;
        }
        for (Session session: sessions){
            if(!session.equals(excludeSession)){
                send(session,message);
            }
        }
    }

    public void broadcastToRoot(Session session, ServerMessage message) throws IOException {
        send(session, message);
    }

    public void send(Session session, ServerMessage message) throws IOException {
        session.getBasicRemote().sendText(gson.toJson(message));
    }
}
