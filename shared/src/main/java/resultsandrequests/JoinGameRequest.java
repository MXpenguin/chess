package resultsandrequests;

public class JoinGameRequest {
    private final int gameID;
    private String authToken;

    public JoinGameRequest(int gameID) {
        this.gameID = gameID;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getGameID() {
        return gameID;
    }

    public String getAuthToken() {
        return authToken;
    }
}
