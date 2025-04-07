package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;

import java.io.IOException;

@WebSocket
public class WebsocketHandler {

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);


        } catch(Exception e) {

        }
    }
}
