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

public class GamePlayClient implements Client, ServerMessageObserver {
    private final WebsocketCommunicator server;
    private final String serverUrl;
    private final String username;
    private final String authToken;
    private final String color;
    private DrawChessBoard drawChessBoard;

    public GamePlayClient(String serverUrl, String username, String authToken, String color) throws ResponseException {
        this.serverUrl = serverUrl;
        this.username = username;
        this.authToken = authToken;
        this.color = color;

        server = new WebsocketCommunicator(serverUrl, this);
    }

    @Override
    public String eval(String input) {
        return "";
    }

    @Override
    public String help() {
        return "";
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
                    System.out.println(drawChessBoard.drawBlackPerspective());
                } else {
                    System.out.println(drawChessBoard.drawWhitePerspective());
                }
            }
            case ERROR -> {
                //TODO
            }
            case NOTIFICATION -> {
                //TODO
            }
            default -> throw new IllegalStateException("Unexpected value: " + message.getServerMessageType());
        }
    }
}
