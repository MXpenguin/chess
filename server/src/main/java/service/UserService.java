package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import resultsandrequests.*;

import javax.xml.crypto.Data;
import java.util.UUID;

public class UserService {

    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;

    public UserService(UserDAO userDataAccess, AuthDAO authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public void clear() throws DataAccessException {
        userDataAccess.clear();
        authDataAccess.clear();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();
        String password = registerRequest.password();
        String email = registerRequest.email();

        if (username == null || password == null || email == null) {
            return new RegisterResult("Error: bad request");
        }

        if (userDataAccess.getUser(username) != null) {
            return new RegisterResult("Error: already taken");
        }

        UserData user = new UserData(username, password, email);
        userDataAccess.createUser(user);

        String authToken = generateAuthToken(username);

        return new RegisterResult(username, authToken);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        String username = loginRequest.username();
        UserData user = userDataAccess.getUser(username);

        if (user == null || !loginRequest.password().equals(user.password())) {
            return new LoginResult("Error: unauthorized");
        }

        String authToken = generateAuthToken(username);

        return new LoginResult(username, authToken);
    }

    public LogoutResult logout(LogoutRequest logoutRequest) throws DataAccessException {
        String authToken = logoutRequest.authToken();

        if (authDataAccess.getAuth(authToken) == null) {
            return new LogoutResult("Error: unauthorized");
        }

        authDataAccess.deleteAuth(authToken);

        return new LogoutResult();
    }

    private String generateAuthToken(String username) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authDataAccess.createAuth(authData);
        return authToken;
    }
}
