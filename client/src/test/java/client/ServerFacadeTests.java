package client;

import dataaccess.DataAccessException;
import dataaccess.SQLAuthDAO;
import dataaccess.SQLGameDAO;
import dataaccess.SQLUserDAO;
import model.GameData;
import org.junit.jupiter.api.*;
import resultsandrequests.*;
import server.ResponseException;
import server.Server;
import server.ServerFacade;

import java.util.Collection;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    private final String username1 = "Alice";
    private final String username2 = "Bob";
    private final String password1 = "mY_sEcure_PAsswORd";
    private final String password2 = "other_secure_word";
    private final String email1 = "myEmail@mail.com";
    private final String email2 = "chessPlayer@chess.com";
    private final String gameName = "The Game of all Games";

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach void clearDatabase() throws DataAccessException {
        new SQLUserDAO().clear();
        new SQLAuthDAO().clear();
        new SQLGameDAO().clear();
    }

    @Test
    @DisplayName("Register")
    public void successRegister() throws ResponseException {
        RegisterResult result = facade.register(new RegisterRequest(
                username1, password1, email1));
        Assertions.assertEquals(username1, result.getUsername(),
                "username does in result does not match request");
        Assertions.assertNotNull(result.getAuthToken(),
                "result has no authToken");
        Assertions.assertNull(result.getMessage(),
                "result has error message");
    }

    @Test
    @DisplayName("Fail register")
    public void failRegister() throws ResponseException {
        ResponseException exception = Assertions.assertThrows(ResponseException.class,
                () -> facade.register(new RegisterRequest(username1, password1, null)),
                "did not throw ResponseException");
        Assertions.assertEquals("Error: bad request", exception.getMessage(),
                "error message is not correct");

        RegisterResult result = facade.register(new RegisterRequest(
                username1, password1, email1));

        ResponseException exception2 = Assertions.assertThrows(ResponseException.class,
                () -> facade.register(new RegisterRequest(
                        username1, password2, email2)),
                "did not throw ResponseException");
        Assertions.assertEquals("Error: already taken", exception2.getMessage(),
                "error message is not correct");
    }

    @Test
    @DisplayName("Login")
    public void successLogin() throws ResponseException {
        facade.register(new RegisterRequest(
                username1, password1, email1));

        LoginResult result = facade.login(new LoginRequest(username1, password1));

        Assertions.assertNull(result.getMessage(),
                "result has error message");
        Assertions.assertEquals(username1, result.getUsername(),
                "result has incorrect username");
        Assertions.assertNotNull(result.getAuthToken(),
                "result has no authToken");
    }

    @Test
    @DisplayName("Fail login")
    public void failLogin() throws ResponseException {
        ResponseException exception = Assertions.assertThrows(ResponseException.class,
                () -> facade.login(new LoginRequest(username1, password1)),
                "did not throw ResponseException on unauthorized login");

        Assertions.assertEquals("Error: unauthorized", exception.getMessage(),
                "incorrect error message upon empty login");
    }

    @Test
    @DisplayName("Logout")
    public void successLogout() throws ResponseException {
        String auth = facade.register(new RegisterRequest(
                username1, password1, email1)).getAuthToken();

        LogoutResult result = facade.logout(new LogoutRequest(auth));

        Assertions.assertNull(result.getMessage(),
                "result has error message");
    }

    @Test
    @DisplayName("Fail logout")
    public void failLogout() throws ResponseException {
        ResponseException exception = Assertions.assertThrows(ResponseException.class,
                () -> facade.logout(new LogoutRequest("not even an authToken")),
                "did not throw ResponseException on unauthorized login");

        Assertions.assertEquals("Error: unauthorized", exception.getMessage(),
                "incorrect error message upon attempting empty logout");
    }

    @Test
    @DisplayName("Create game")
    public void successCreateGame() throws ResponseException {
        String auth = facade.register(new RegisterRequest(
                username1, password1, email1)).getAuthToken();

        CreateGameRequest request = new CreateGameRequest(gameName);
        request.setAuthToken(auth);
        CreateGameResult result = facade.createGame(request);

        Assertions.assertNull(result.getMessage(),
                "failed to create game");

        Assertions.assertNotNull(result.getGameID(),
                "failed to provide game id");
    }

    @Test
    @DisplayName("Fail create game")
    public void failCreateGame() throws ResponseException {
        ResponseException exception = Assertions.assertThrows(ResponseException.class,
                () -> facade.createGame(new CreateGameRequest(gameName)));

        Assertions.assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    @DisplayName("List games")
    public void successListGames() throws ResponseException {
        String auth = facade.register(new RegisterRequest(
                username1, password1, email1)).getAuthToken();

        CreateGameRequest request = new CreateGameRequest(gameName);
        request.setAuthToken(auth);
        CreateGameResult createGameResult = facade.createGame(request);

        ListGamesResult listGamesResult = facade.listGames(new ListGamesRequest(auth));

        Assertions.assertNull(listGamesResult.getMessage(),
                "result has error message");
        Assertions.assertNotNull(listGamesResult.getGames(),
                "result has no games collection");
        Assertions.assertEquals(1, listGamesResult.getGames().size(),
                "result does not have one game");

        Assertions.assertEquals(createGameResult.getGameID(), listGamesResult.getGames().iterator().next().gameID(),
                "game id in collection does not match game id that was added");
    }
}
