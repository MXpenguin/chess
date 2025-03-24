package client;

import resultsandrequests.LoginRequest;
import resultsandrequests.LoginResult;
import resultsandrequests.RegisterRequest;
import resultsandrequests.RegisterResult;
import server.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class PreLoginClient implements Client {

    private final ServerFacade server;
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    @Override
    public String eval(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
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
                register <USERNAME> <PASSWORD> <EMAIL>
                login <USERNAME> <PASSWORD>
                quit
                help
                """;
    }

    @Override
    public String welcome() {
        return "Welcome to chess.";
    }

    private String register(String... params) throws ResponseException {
        if (params.length != 3) {
            return "Please provide a username, password, and email.";
        }
        RegisterResult result = server.register(new RegisterRequest(params[0], params[1], params[2]));
        String username = result.getUsername();
        String authToken = result.getAuthToken();
        new Repl(new PostLoginClient(serverUrl, username, authToken)).run();
        return "";
    }

    private String login(String... params) throws ResponseException {
        if (params.length != 2) {
            return "Please provide your username and password.";
        }
        LoginResult result = server.login(new LoginRequest(params[0], params[1]));
        String username = result.getUsername();
        String authToken = result.getAuthToken();
        new Repl(new PostLoginClient(serverUrl, username, authToken)).run();
        return welcome();
    }
}
