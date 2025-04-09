package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
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
                    return move(params);
                }
                case "leave" -> {
                    server.leave(authToken, gameID);
                    return "quit";
                }
                case "resign" -> {
                    server.resign(authToken, gameID);
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
                move <col&row> <col&row> (promotion)
                leave
                resign
                redraw
                possibilities <col&row>  - show legal moves
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

    private String move(String... params) throws ResponseException {
        if (params.length != 2 && params.length != 3) {
            return "Please provide a starting position and ending position";
        }

        ChessPiece.PieceType promotion = null;
        if (params.length == 3) {
            try {
                promotion = ChessPiece.PieceType.valueOf(params[2].toUpperCase());
            } catch (IllegalArgumentException e) {
                return "Invalid promotion type";
            }
        }

        ChessMove chessMove = new ChessMove(getChessPositionFromText(params[0]),
                getChessPositionFromText(params[1]), promotion);

        server.move(authToken, gameID, chessMove);

        return "";
    }

    private ChessPosition getChessPositionFromText(String text) throws ResponseException {
        if (text == null) {
            throw new ResponseException(500, "Really bad input");
        }
        if (text.length() < 2) {
            throw new ResponseException(500, "Needs a column and row");
        }

        int col = Integer.parseInt(text.substring(0,1));
        int row = Integer.parseInt(text.substring(1,2));

        return new ChessPosition(row, col);
    }
}
