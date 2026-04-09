package ws;

public class WebSocketHandler {
    private final GameMessageRouter router = new GameMessageRouter();

    public void onOpen(){
        //initialize if needed
    }

    public void onMessage(String rawJSON){
        //deserialize to UserGameCommand
        //route command
    }

    public void OnClose(){
        //cleanup socket from connection manager
    }
}
