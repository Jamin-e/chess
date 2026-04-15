package ws;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import websocket.ErrorMessage;
import websocket.LoadGameMessage;
import websocket.NotificationMessage;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import jakarta.websocket.*;
import java.net.URI;

public class WebSocketClient {
    private final Gson gson = new Gson();
    private final WebSocketMessageHandler messageHandler;
    private Session session;
    private boolean connected = false;

    public WebSocketClient(WebSocketMessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }

    public void connect(String baseUrl, String authToken, Integer gameID, boolean isPlayer){
        try {
            String wsBaseUrl = baseUrl;
            if (wsBaseUrl.startsWith("https://")) {
                wsBaseUrl = "wss://" + wsBaseUrl.substring("https://".length());
            } else if (wsBaseUrl.startsWith("http://")) {
                wsBaseUrl = "ws://" + wsBaseUrl.substring("http://".length());
            }
            URI uri = URI.create(wsBaseUrl + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

            container.connectToServer(new Endpoint() {
                @Override
                public void onOpen(Session session, EndpointConfig endpointConfig) {
                    WebSocketClient.this.session = session;
                    connected = true;

                    session.addMessageHandler((MessageHandler.Whole<String>) WebSocketClient.this::handleIncoming);

                    sendCommand(new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID));
                }

                @Override
                public void onClose(Session session, CloseReason closeReason) {
                    connected = false;
                    WebSocketClient.this.session = null;
                }

                @Override
                public void onError(Session session, Throwable thr) {
                    connected = false;
                }
            }, config, uri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendCommand(UserGameCommand command){
        if (!connected || session == null || !session.isOpen()){
            throw new IllegalStateException("Not connected");
        }
        try {
            String json = gson.toJson(command);
            session.getBasicRemote().sendText(json);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public void closeConnection(){
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            connected = false;
            session = null;
        }
    }
    private void handleIncoming(String rawJson){
        try {
            JsonObject obj = JsonParser.parseString(rawJson).getAsJsonObject();
            if (!obj.has("serverMessageType")) {
                messageHandler.handle(new ErrorMessage("Missing serverMessageType"));
                return;
            }
            ServerMessage.ServerMessageType type =
                    gson.fromJson(obj.get("serverMessageType"), ServerMessage.ServerMessageType.class);
            if (type == null) {
                messageHandler.handle(new ErrorMessage("Unknown serverMessageType"));
                return;
            }
            ServerMessage message = switch (type) {
                case LOAD_GAME -> gson.fromJson(rawJson, LoadGameMessage.class);
                case NOTIFICATION -> gson.fromJson(rawJson, NotificationMessage.class);
                case ERROR -> gson.fromJson(rawJson, ErrorMessage.class);
            };

            messageHandler.handle(message);
        }catch(Exception e){
            messageHandler.handle(new ErrorMessage("Wbsocket error: "+ e.getMessage()));
        }
    }
}
