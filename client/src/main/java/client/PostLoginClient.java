package client;

import chess.ChessGame;
import model.GameData;
import resultsandrequests.*;
import server.ResponseException;
import server.ServerFacade;
import ui.DrawChessBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class PostLoginClient implements Client {
    private final ServerFacade server;
    private final String serverUrl;
    private final String username;
    private final String authToken;
    private ArrayList<GameData> gamesList;

    public PostLoginClient(String serverUrl, String username, String authToken) throws ResponseException {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.username = username;
        this.authToken = authToken;

        gamesList = (ArrayList<GameData>) server.listGames(new ListGamesRequest(authToken)).getGames();
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout(params);
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    @Override
    public String help() {
        return """
                create <NAME>
                list
                join <ID> [WHITE|BLACK]
                observe <ID>
                logout
                help
                """;
    }

    @Override
    public String welcome() {
        return "Welcome " + username + ".";
    }

    private String logout(String... params) throws ResponseException {
        server.logout(new LogoutRequest(authToken));
        return "quit";
    }

    private String create(String... params) throws ResponseException {
        if (params.length != 1) {
            return "Game name cannot contain spaces.";
        }
        CreateGameRequest request = new CreateGameRequest(params[0]);
        request.setAuthToken(authToken);
        server.createGame(request);
        return "Created a new chess game with name: " + params[0];
    }

    private String list(String... params) throws ResponseException {
        ListGamesResult result = server.listGames(new ListGamesRequest(authToken));
        gamesList = (ArrayList<GameData>) result.getGames();

        StringBuilder stringGamesListBuilder = new StringBuilder();
        for (int i = 0; i < gamesList.size(); ++i) {
            stringGamesListBuilder.append(i+1);
            stringGamesListBuilder.append(": ");
            GameData game = gamesList.get(i);
            stringGamesListBuilder.append(game.gameName());
            stringGamesListBuilder.append("  WHITE: ");
            stringGamesListBuilder.append(game.whiteUsername() != null ? game.whiteUsername() : "_____");
            stringGamesListBuilder.append("  BLACK: ");
            stringGamesListBuilder.append(game.blackUsername() != null ? game.blackUsername() : "_____");
            stringGamesListBuilder.append("\n");
        }

        return stringGamesListBuilder.toString();
    }

    private String join(String... params) throws ResponseException {
        if (params.length != 2) {
            return "Please provide a game id number and a team color.";
        }

        int id;
        try {
            id = Integer.parseInt(params[0]);
        } catch(NumberFormatException e) {
            return "Please provide a valid id.";
        }

        String color = params[1].toLowerCase();
        if (!"white".equals(color) && !"black".equals(color)) {
            return "Please provide a valid team color.";
        }

        if (id < 1 || id > gamesList.size()) {
            return "Please provide a valid id within the range of games.";
        }

        GameData game = gamesList.get(id-1);

//        System.out.println(game);                   //TODO
//        System.out.println(game.game());
//        System.out.println(game.game().getBoard());

        DrawChessBoard drawChessBoard = new DrawChessBoard(
                new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(),
                        game.gameName(), new ChessGame()));
        if ("white".equals(color)) {
            return drawChessBoard.drawWhitePerspective();
        } else {
            return drawChessBoard.drawBlackPerspective();
        }
    }

    private String observe(String... params) {
        if (params.length != 1) {
            return "Please provide a game id number.";
        }

        int id;
        try {
            id = Integer.parseInt(params[0]);
        } catch(NumberFormatException e) {
            return "Please provide a valid id.";
        }

        if (id < 1 || id > gamesList.size()) {
            return "Please provide a valid id within the range of games.";
        }

        GameData game = gamesList.get(id-1);
        DrawChessBoard boardDrawer = new DrawChessBoard(
                new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(),
                        game.gameName(), new ChessGame()));

        return boardDrawer.drawWhitePerspective();
    }
}
