package resultsandrequests;

/**
 * Response object with results of login user request
 */
public class LoginResult {
    private final String username;
    private final String authToken;
    private final String message;

    public LoginResult(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
        this.message = null;
    }

    public LoginResult(String message) {
        this.username = null;
        this.authToken = null;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return username;
    }

    public String getAuthToken() {
        return authToken;
    }
}
