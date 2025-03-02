package service;

import dataaccess.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import resultsandrequests.*;

public class GameServiceTests {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;
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
        gameDAO = new MemoryGameDAO();
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(authDAO, gameDAO);
        username1 = "Alice";
        username2 = "Bob";
        password1 = "supersecretpassword";
        password2 = "abcdefg";
        email1 = "alice_32@mail.org";
        email2 = "bobbyboy15@themail.com";
    }

    @Test
    @DisplayName("Create game")
    public void successCreateGame() throws DataAccessException {
        RegisterResult registerResult1 = userService.register(new RegisterRequest(username1, password1, email1));

        String authToken1 = registerResult1.getAuthToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("game1");
        createGameRequest.setAuthToken(authToken1);

        Assertions.assertEquals(0, gameService.listGames(new ListGamesRequest(authToken1)).getGames().size(),
                "List of games does not have 0 element");

        CreateGameResult createGameResult = gameService.createGame(createGameRequest);

        Assertions.assertNotNull(createGameResult.getGameID(),
                "Response has no game id");
        Assertions.assertNull(createGameResult.getMessage(),
                "Response has error message");
        Assertions.assertEquals(1, gameService.listGames(new ListGamesRequest(authToken1)).getGames().size(),
                "List of games does not have 1 element");
    }

    @Test
    @DisplayName("Incorrect create game")
    public void failCreateGame() throws DataAccessException {
        RegisterResult registerResult1 = userService.register(new RegisterRequest(username1, password1, email1));

        String authToken1 = registerResult1.getAuthToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("game1");
        createGameRequest.setAuthToken("this is not an authtoken");

        Assertions.assertEquals(0, gameService.listGames(new ListGamesRequest(authToken1)).getGames().size(),
                "List of games does not have 0 element");

        CreateGameResult createGameResult = gameService.createGame(createGameRequest);

        Assertions.assertNull(createGameResult.getGameID(),
                "Response has game id");
        Assertions.assertNotNull(createGameResult.getMessage(),
                "Response has no error message");
        Assertions.assertEquals(0, gameService.listGames(new ListGamesRequest(authToken1)).getGames().size(),
                "List of games does not have 0 elements");
    }

    @Test
    @DisplayName("List games")
    public void successListGames() throws DataAccessException {
        RegisterResult registerResult1 = userService.register(new RegisterRequest(username1, password1, email1));

        String authToken1 = registerResult1.getAuthToken();

        CreateGameRequest createGameRequest = new CreateGameRequest("game1");
        createGameRequest.setAuthToken(authToken1);

        Assertions.assertEquals(0, gameService.listGames(new ListGamesRequest(authToken1)).getGames().size(),
                "List of games does not have 0 element");

        CreateGameResult createGameResult = gameService.createGame(createGameRequest);

        Assertions.assertNotNull(createGameResult.getGameID(),
                "Response has no game id");
        Assertions.assertNull(createGameResult.getMessage(),
                "Response has error message");
        Assertions.assertEquals(1, gameService.listGames(new ListGamesRequest(authToken1)).getGames().size(),
                "List of games does not have 1 element");
    }
}
