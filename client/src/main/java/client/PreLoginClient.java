package client;

import server.ServerFacade;

public class PreLoginClient implements Client {

    private final ServerFacade server;
    private final String serverUrl;

    public PreLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    @Override
    public String eval(String eval) {

        return "";
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
}
