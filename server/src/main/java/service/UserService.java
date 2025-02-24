package service;

import dataaccess.UserDAO;
import resultsandrequests.LoginRequest;
import resultsandrequests.LoginResult;

public class UserService {

    private final UserDAO dataAccess;

    public UserService(UserDAO dataAccess) {
        this.dataAccess = dataAccess;
    }

    public LoginResult login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        dataAccess.getUser(username);
    }
}
