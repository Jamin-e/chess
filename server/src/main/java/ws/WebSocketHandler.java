package ws;

import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

public class WebSocketHandler {
    private final Gson gson = new Gson();
    private final GameMessageRouter router = new GameMessageRouter();

    public void onOpen(){
        //initialize if needed
    }

    public void onMessage(String rawJSON){
        UserGameCommand command = gson.fromJson(rawJSON, UserGameCommand.class);
        router.route(command);
    }

    public void OnClose(){
        //cleanup socket from connection manager
    }
}
