package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    public final ConcurrentHashMap<Integer, ConnectionManager> games = new ConcurrentHashMap<>();

    public void add(Integer gameID, String clientName, Session session) {
        if (games.get(gameID) != null) {
            games.get(gameID).add(clientName, session);
        } else {
            games.put(gameID, new ConnectionManager());
            games.get(gameID).add(clientName, session);
        }
    }

    public void broadcast(Integer gameID, String excludeClientName, ServerMessage message) throws IOException {
        games.get(gameID).broadcast(excludeClientName, message);
    }

    public void send(Integer gameID, String clientName, ServerMessage message) throws IOException {
        games.get(gameID).send(clientName, message);
    }
}
