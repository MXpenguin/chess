package resultsandrequests;

/**
 * Request object to register user
 */
public record RegisterRequest(String username, String password, String email) {
}
