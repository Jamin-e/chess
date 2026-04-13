package ws;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

public class WebSocketHandler {
    private final Gson gson = new Gson();
    private final GameMessageRouter router = new GameMessageRouter();
    private final GameConnectionManager connectionManager = new GameConnectionManager();

    public void onOpen(){
        //initialize if needed
    }

    public void onMessage(String rawJSON){
        UserGameCommand command = gson.fromJson(rawJSON, UserGameCommand.class);
        router.route(command);
    }

    public void OnClose(Object session){
        connectionManager.removeSession(session);
    }

    public void onError(Object session, Throwable error) {
        // log or print error
    }
}
