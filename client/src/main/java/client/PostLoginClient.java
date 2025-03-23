package client;

import server.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

public class PostLoginClient implements Client {
    private final ServerFacade server;
    private final String serverUrl;
    private final String username;
    private final String authToken;

    public PostLoginClient(String serverUrl, String username, String authToken) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.username = username;
        this.authToken = authToken;
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

    private String create(String... params) {

    }
}
