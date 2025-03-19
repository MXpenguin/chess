package server;

import resultsandrequests.*;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest request) {
        return null;
    }

    public LoginResult login(LoginRequest request) {
        return null;
    }

    public LogoutResult logout(LogoutRequest request) {
        return null;
    }

    public ListGamesResult listGames(ListGamesRequest request) {
        return null;
    }

    public CreateGameResult createGame(CreateGameRequest request) {
        return null;
    }

    public JoinGameResult joinGame(JoinGameRequest request) {
        return null;
    }
}
