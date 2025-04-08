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
            
            switch(command.getCommandType()) {
                case CONNECT -> {
                }
                case MAKE_MOVE -> {
                }
                case LEAVE -> {
                }
                case RESIGN -> {
                }
                default -> throw new IllegalStateException("Unexpected value: " + command.getCommandType());
            }

        } catch(Exception e) {

        }
    }

    private void connect(String username) {

    }

    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }
}
