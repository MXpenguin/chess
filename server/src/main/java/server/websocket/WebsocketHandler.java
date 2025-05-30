package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.*;
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

    private SQLGameDAO gameDAO = null;
    private UserDAO userDAO = null;
    private AuthDAO authDAO = null;

    public WebsocketHandler() {
        try {
            authDAO = new SQLAuthDAO();
            userDAO = new SQLUserDAO();
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws IOException {
        try {
            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

            Integer gameID = command.getGameID();

            AuthData authData = authDAO.getAuth(command.getAuthToken());
            if (authData == null) {
                session.getRemote().sendString(error("Error: unauthorized").toString());
            }
            String username = authData.username();

            ChessMove move = command.getMove();

            switch(command.getCommandType()) {
                case CONNECT -> connect(gameID, username, session);
                case MAKE_MOVE -> makeMove(gameID, username, move);
                case LEAVE -> leave(gameID, username);
                case RESIGN -> resign(gameID, username);
                default -> throw new IllegalStateException("Unexpected value: " + command.getCommandType());
            }

        } catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
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

        if (chessGame == null) {
            gameConnections.send(gameID, username, error("Error: no game"));
            return;
        }

        gameConnections.broadcast(gameID, username, notification(username + " has connected to the game as " + typeOfPlayer));
        gameConnections.send(gameID, username, loadGame(chessGame));
    }

    private void makeMove(Integer gameID, String username, ChessMove move) throws DataAccessException, IOException {
        ChessGame chessGame = null;
        ChessGame.TeamColor playerColor = null;
        for (GameData game : gameDAO.listGames()) {
            if (game.gameID() == gameID) {
                chessGame = game.game();
                if (username.equals(game.whiteUsername())) {
                    playerColor = ChessGame.TeamColor.WHITE;
                } else if (username.equals(game.blackUsername())) {
                    playerColor = ChessGame.TeamColor.BLACK;
                }
                break;
            }
        }

        if (chessGame == null) {
            gameConnections.send(gameID, username, error("Error: there is no game"));
            return;
        }

        if (chessGame.isGameOver()) {
            gameConnections.send(gameID, username, error("Error: game is over"));
            return;
        }

        if (playerColor != chessGame.getTeamTurn()) {
            gameConnections.send(gameID, username, error("Error: not your turn"));
            return;
        }

        if (!chessGame.moveIsCorrectColor(move)) {
            gameConnections.send(gameID, username, error("Error: wrong color"));
            return;
        }

        try {
            chessGame.makeMove(move);
            gameDAO.updateChessGame(gameID, chessGame);
        } catch (InvalidMoveException e) {
            gameConnections.send(gameID, username, error("Error: invalid move"));
            return;
        }

        // LOAD_GAME
        gameConnections.broadcast(gameID, "", loadGame(chessGame));

        // NOTIFY MOVE
        String moveText = move.toString();
        gameConnections.broadcast(gameID, username, notification(username + " moved " + moveText));

        //Checkmate
        if (chessGame.isInCheckmate(ChessGame.TeamColor.BLACK)) {
            gameConnections.broadcast(gameID, "", notification("black is in checkmate"));
            return;
        } else if (chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
            gameConnections.broadcast(gameID, "", notification("white is in checkmate"));
            return;
        }

        //Check
        if (chessGame.isInCheck(ChessGame.TeamColor.BLACK)) {
            gameConnections.broadcast(gameID, "", notification("black is in check"));
        } else if (chessGame.isInCheck(ChessGame.TeamColor.WHITE)) {
            gameConnections.broadcast(gameID, "", notification("white is in check"));
        }

        //Stalemate
        if (chessGame.getTeamTurn() == ChessGame.TeamColor.BLACK && chessGame.isInStalemate(ChessGame.TeamColor.BLACK)) {
            gameConnections.broadcast(gameID, "", notification("black is in stalemate"));
        } else if (chessGame.getTeamTurn() == ChessGame.TeamColor.WHITE && chessGame.isInStalemate(ChessGame.TeamColor.WHITE)) {
            gameConnections.broadcast(gameID, "", notification("white is in stalemate"));
        }
    }

    private void leave(Integer gameID, String username) throws IOException, DataAccessException {
        for (GameData game : gameDAO.listGames()) {
            if (game.gameID() == gameID) {
                if (username.equals(game.whiteUsername())) {
                    gameDAO.updateGame(gameID, "WHITE", null);
                } else if (username.equals(game.blackUsername())) {
                    gameDAO.updateGame(gameID, "BLACK", null);
                }
                break;
            }
        }

        gameConnections.remove(gameID, username);
        gameConnections.broadcast(gameID, "", notification(username + " left the game"));
    }

    private void resign(Integer gameID, String username) throws IOException, DataAccessException {
        ChessGame chessGame = null;
        for (GameData game : gameDAO.listGames()) {
            if (game.gameID() == gameID) {
                chessGame = game.game();
                if (chessGame.isGameOver()) {
                    gameConnections.send(gameID, username, error("Error: game is already over"));
                    return;
                }
                if (!username.equals(game.whiteUsername()) && !username.equals(game.blackUsername())) {
                    gameConnections.send(gameID, username, error("Error: observers can't resign"));
                    return;
                }
                chessGame.setGameOver();
                gameDAO.updateChessGame(gameID, chessGame);
                break;
            }
        }
        gameConnections.broadcast(gameID, "", notification(username + " has resigned"));
    }

    private ServerMessage notification(String message) {
        return new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION,
                null, null, message);
    }

    private ServerMessage loadGame(ChessGame game) {
        return new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME,
                game, null, null);
    }

    private ServerMessage error(String errorMessage) {
        return new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                null, errorMessage, null);
    }
}
