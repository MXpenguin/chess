package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebsocketHandler {
    private final GameManager gameConnections = new GameManager();

    private GameDAO gameDAO;
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public WebsocketHandler(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

            Integer gameID = command.getGameID();

            AuthData authData = authDAO.getAuth(command.getAuthToken());
            String username = authData.username();

            ChessMove move = command.getMove();

            switch(command.getCommandType()) {
                case CONNECT -> connect(gameID, username, session);
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

    private void connect(Integer gameID, String username, Session session) throws IOException, DataAccessException {
        gameConnections.add(gameID, username, session);

        ChessGame chessGame = null;
        String typeOfPlayer = "an observer";
        for (GameData game : gameDAO.listGames()) {
            if (game.gameID() == gameID) {
                if (username.equals(game.blackUsername())) {
                    typeOfPlayer = "the black team";
                } else if (username.equals(game.whiteUsername())) {
                    typeOfPlayer = "the white team";
                }
                chessGame = game.game();
                break;
            }
        }
        ServerMessage msg = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                null, null, username + " has connected to the game as " + typeOfPlayer);
        gameConnections.broadcast(gameID, username, msg);

        ServerMessage loadGame = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                new Gson().toJson(chessGame), null, null);

    }

    private void makeMove() {

    }

    private void leave() {

    }

    private void resign() {

    }
}
