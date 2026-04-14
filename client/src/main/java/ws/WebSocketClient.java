package ws;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import jakarta.websocket.*;
import java.net.URI;

public class WebSocketClient {
    private final Gson gson = new Gson();
    private final WebSocketMessageHandler messageHandler;

    private Session session;

    private String authToken;
    private Integer gameID;
    private boolean connected = false;

    public WebSocketClient(WebSocketMessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }

    public void connect(String baseUrl, String authToken, Integer gameID, boolean isPlayer){
        try {
            URI uri = URI.create(baseUrl + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            ClientEndpointConfig config = ClientEndpointConfig.Builder.create().build();

            container.connectToServer(new Endpoint() {
                @Override
                public void onOpen(Session session, EndpointConfig endpointConfig) {
                    WebSocketClient.this.session = session;
                    connected = true;

                    session.addMessageHandler((MessageHandler.Whole<String>) raw -> {
                        ServerMessage message = gson.fromJson(raw, ServerMessage.class);
                        messageHandler.handle(message);
                    });

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
    private void handleIncoming(String rawJSON){
        ServerMessage message = gson.fromJson(rawJSON, ServerMessage.class);
        messageHandler.handle(message);
    }

    public boolean isConnected() {
        return connected;
    }

    private void onOpen() {
        connected = true;
    }
    private void onClose() {
        connected = false;
    }
    private void onError(Exception e) {
        // surface or log websocket errors
    }
}
