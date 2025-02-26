package resultsandrequests;

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

    @Override
    public String toString() {
        return "username: " + username + "\n" + "authtoken: " + authToken + "\n" + "message: " + message;
    }
}
