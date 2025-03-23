package client;

import server.ServerFacade;

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
    public String eval(String eval) {
        return "";
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
}
