package resultsandrequests;

/**
 * Request object to create game
 */
public class CreateGameRequest {
    private final String gameName;
    private transient String authToken;

    public CreateGameRequest(String gameName) {
        this.gameName = gameName;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getGameName() {
        return gameName;
    }

    public String getAuthToken() {
        return authToken;
    }
}
