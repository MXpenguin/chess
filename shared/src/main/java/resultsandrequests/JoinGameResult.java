package resultsandrequests;

/**
 * Response object with results of join game request
 */
public class JoinGameResult {
    private final String message;

    public JoinGameResult() {
        this.message = null;
    }

    public JoinGameResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
