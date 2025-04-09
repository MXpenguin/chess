package client;

import chess.ChessGame;
import model.GameData;
import serverfacade.ResponseException;
import serverfacade.ServerFacade;
import serverfacade.ServerMessageObserver;
import serverfacade.WebsocketCommunicator;
import ui.DrawChessBoard;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.Arrays;

public class GamePlayClient implements Client, ServerMessageObserver {
    private final WebsocketCommunicator server;
    private final String serverUrl;
    private final String username;
    private final String authToken;
    private final Integer gameID;
    private final String color;
    private DrawChessBoard drawChessBoard;

    public GamePlayClient(String serverUrl, String username, String authToken, Integer gameID, String color) throws ResponseException {
        this.serverUrl = serverUrl;
        this.username = username;
        this.authToken = authToken;
        this.gameID = gameID;
        this.color = color;

        server = new WebsocketCommunicator(serverUrl, this);

        server.connect(authToken, gameID);
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "move" -> {
                    move(params);
                }
                case "leave" -> {

                }
                case "resign" -> {

                }
                case "redraw" -> {

                }
                case "possibilities" -> {

                }
                default -> {
                    return help();
                }
            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }

        return "";
    }

    @Override
    public String help() {
        return """
                move <col&row>
                leave
                resign
                redraw
                possibilities  - show legal moves
                """;
    }

    @Override
    public String welcome() {
        return "";
    }

    @Override
    public void notify(ServerMessage message) {
        switch(message.getServerMessageType()) {
            case LOAD_GAME -> {
                ChessGame game = message.getGame();
                drawChessBoard = new DrawChessBoard(game);
                if ("black".equals(color)) {
                    System.out.println();
                    System.out.println(drawChessBoard.drawBlackPerspective());
                } else {
                    System.out.println();
                    System.out.println(drawChessBoard.drawWhitePerspective());
                }
            }
            case ERROR -> {
                System.out.println(message.getErrorMessage());
            }
            case NOTIFICATION -> {
                System.out.println(message.getMessage());
            }
            default -> throw new IllegalStateException("Unexpected value: " + message.getServerMessageType());
        }
    }

    private void move(String... params) throws ResponseException {

    }
}
