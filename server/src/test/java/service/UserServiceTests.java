package service;

import dataaccess.*;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.*;
import resultsandrequests.*;
import spark.utils.Assert;

public class UserServiceTests {

    private UserDAO userDAO;
    private AuthDAO authDAO;
    //private GameDAO gameDAO;
    private UserService userService;
    private GameService gameService;
    private String username1;
    private String username2;
    private String password1;
    private String password2;
    private String email1;
    private String email2;

    @BeforeEach
    public void setUp() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        //gameDAO = new MemoryGameDAO();
        userService = new UserService(userDAO, authDAO);
        //gameService = new GameService(authDAO, gameDAO);
        username1 = "Alice";
        username2 = "Bob";
        password1 = "supersecretpassword";
        password2 = "abcdefg";
        email1 = "alice_32@mail.org";
        email2 = "bobbyboy15@themail.com";
    }

    @Test
    @DisplayName("Register user")
    public void successRegister() throws DataAccessException {
        RegisterResult result1 = userService.register(new RegisterRequest(username1, password1, email1));
        RegisterResult result2 = userService.register(new RegisterRequest(username2, password2, email2));

        Assertions.assertEquals(username1, result1.getUsername(),
                "Registration response had different username");
        Assertions.assertEquals(username2, result2.getUsername(),
                "Registration response had different username");
        Assertions.assertNotNull(result1.getAuthToken(),
                "Registration response did not have authToken");
        Assertions.assertNotNull(result2.getAuthToken(),
                "Registration response did not have authToken");
        Assertions.assertNull(result1.getMessage(),
                "Error message is not empty");
        Assertions.assertNull(result2.getMessage(),
                "Error message is not empty");
    }

    @Test
    @DisplayName("Twice username registration")
    public void failRegister() throws DataAccessException {
        RegisterResult result1 = userService.register(new RegisterRequest(username1, password1, email1));
        RegisterResult result2 = userService.register(new RegisterRequest(username1, password2, email2));

        Assertions.assertNotNull(result2.getMessage(),
                "Response has no error message for second registration of username");
        Assertions.assertEquals("Error: already taken", result2.getMessage(),
                "Attempted second registration of username did not give correct error message");
    }

    @Test
    @DisplayName("Logout user")
    public void successLogout() throws DataAccessException {
        RegisterResult registerResult1 = userService.register(new RegisterRequest(username1, password1, email1));
        RegisterResult registerResult2 = userService.register(new RegisterRequest(username2, password2, email2));

        String authToken1 = registerResult1.getAuthToken();
        String authToken2 = registerResult2.getAuthToken();

        LogoutResult logoutResult1 = userService.logout(new LogoutRequest(authToken1));

        Assertions.assertNull(authDAO.getAuth(authToken1),
                "User not logged out");
        Assertions.assertNull(logoutResult1.getMessage(),
                "Logout response has error message");
        Assertions.assertNotNull(authDAO.getAuth(authToken2),
                "Unintended user logged out");
    }

    @Test
    @DisplayName("Incorrect logout")
    public void failLogout() throws DataAccessException {
        RegisterResult registerResult1 = userService.register(new RegisterRequest(username1, password1, email1));
        RegisterResult registerResult2 = userService.register(new RegisterRequest(username2, password2, email2));

        String authToken1 = registerResult1.getAuthToken();
        String authToken2 = registerResult2.getAuthToken();

        LogoutResult logoutResult1 = userService.logout(new LogoutRequest(""));

        Assertions.assertNotNull(authDAO.getAuth(authToken1),
                "User logged out without authorization");
        Assertions.assertNotNull(logoutResult1.getMessage(),
                "Response for unauthorized logout has no error message");
        Assertions.assertNotNull(authDAO.getAuth(authToken2),
                "Unintended user logged out");
    }

    @Test
    @DisplayName("Login user")
    public void successLogin() throws DataAccessException {
        RegisterResult registerResult1 = userService.register(new RegisterRequest(username1, password1, email1));
        RegisterResult registerResult2 = userService.register(new RegisterRequest(username2, password2, email2));

        String authToken1 = registerResult1.getAuthToken();
        String authToken2 = registerResult2.getAuthToken();

        LogoutResult logoutResult1 = userService.logout(new LogoutRequest(authToken1));
        LogoutResult logoutResult2 = userService.logout(new LogoutRequest(authToken2));

        LoginResult loginResult1 = userService.login(new LoginRequest(username1, password1));

        Assertions.assertEquals(username1, loginResult1.getUsername(),
                "Login response does not have username");
        Assertions.assertNotNull(loginResult1.getAuthToken(),
                "Login response has no authToken string");
        Assertions.assertNull(loginResult1.getMessage(),
                "Login response has error message");
    }

    @Test
    @DisplayName("Incorrect login")
    public void failLogin() throws DataAccessException {
        RegisterResult registerResult1 = userService.register(new RegisterRequest(username1, password1, email1));
        RegisterResult registerResult2 = userService.register(new RegisterRequest(username2, password2, email2));

        String authToken1 = registerResult1.getAuthToken();

        LogoutResult logoutResult1 = userService.logout(new LogoutRequest(authToken1));

        LoginResult loginResult1 = userService.login(new LoginRequest(username1, password2));

        Assertions.assertNull(loginResult1.getUsername(),
                "Login with incorrect password has username");
        Assertions.assertNull(loginResult1.getAuthToken(),
                "Login with incorrect password has authToken string");
        Assertions.assertNotNull(loginResult1.getMessage(),
                "Login with incorrect password has no error message");
        Assertions.assertEquals("Error: unauthorized", loginResult1.getMessage(),
                "Login with incorrect password has incorrect error message");
    }

    @Test
    @DisplayName("Clear data")
    public void testClear() {

    }
}
