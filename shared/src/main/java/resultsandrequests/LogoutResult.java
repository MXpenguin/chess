package resultsandrequests;

/**
 * Response object with results of logout user request
 */
public class LogoutResult {
    private final String message;

    public LogoutResult() {
        message = null;
    }

    public LogoutResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
