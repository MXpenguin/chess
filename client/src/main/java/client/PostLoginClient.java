package client;

import model.GameData;
import resultsandrequests.CreateGameRequest;
import resultsandrequests.CreateGameResult;
import resultsandrequests.ListGamesRequest;
import resultsandrequests.ListGamesResult;
import server.ResponseException;
import server.ServerFacade;

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
                case "quit" -> "quit";
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
                quit
                help
                """;
    }

    @Override
    public String welcome() {
        return "Welcome " + username + ".";
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
}
