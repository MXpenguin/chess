package resultsandrequests;

/**
 * Response object with results of register user request
 */
public class RegisterResult {

    private final String username;
    private final String authToken;
    private final String message;

    public RegisterResult(String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
        message = null;
    }
    
    public RegisterResult(String message) {
        this.message = message;
        username = null;
        authToken = null;
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

    @Override
    public String toString() {
        return "username: " + username + "\n" + "authtoken: " + authToken + "\n" + "message: " + message;
    }
}
