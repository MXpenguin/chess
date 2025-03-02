package resultsandrequests;

public class CreateGameResult {
    private final Integer gameID;
    private final String message;

    public CreateGameResult(int gameID) {
        this.gameID = gameID;
        this.message = null;
    }

    public CreateGameResult(String message) {
        this.gameID = null;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Integer getGameID() {
        return gameID;
    }
}
