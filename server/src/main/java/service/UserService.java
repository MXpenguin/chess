package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import resultsandrequests.LoginRequest;
import resultsandrequests.LoginResult;
import resultsandrequests.RegisterRequest;
import resultsandrequests.RegisterResult;

import java.util.UUID;

public class UserService {

    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;

    public UserService(UserDAO userDataAccess, AuthDAO authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public void clear() {
        userDataAccess.clear();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        String username = registerRequest.username();
        if (userDataAccess.getUser(username) != null) {
            return new RegisterResult("already taken");
        }

        String password = registerRequest.password();
        String email = registerRequest.email();

        UserData user = new UserData(username, password, email);
        userDataAccess.createUser(user);

        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        authDataAccess.createAuth(authData);

        return new RegisterResult(username, authToken);
    }

//    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
//        String username = loginRequest.username();
//        UserData user = userDataAccess.getUser(username);
//        if (user == null) {
//
//        }
//        LoginResult loginResult = new LoginResult(user.username(), );
//    }
}
