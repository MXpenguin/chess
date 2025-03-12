package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SQLGameDAOTests {
    private GameDAO gameDAO;

    private int gameID;
    private String whiteUsername;
    private String blackUsername;
    private String gameName;
    private ChessGame chessGame;
    private GameData gameData;

    @BeforeEach
    void setUp() {
        try {
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        gameID = 324;
        whiteUsername = "alice";
        blackUsername = "bob";
        gameName = "name of game";
        chessGame = new ChessGame();
        gameData = new GameData(gameID, null, null, gameName, chessGame);
    }

    @Test
    @DisplayName("Clear")
    public void successClear() throws DataAccessException {
        gameDAO.clear();

        Assertions.assertTrue( gameDAO.listGames().isEmpty(),
                "database not empty initially");
        gameDAO.createGame(gameData);
        Assertions.assertEquals(1, gameDAO.listGames().size(),
                "failed to add game to database");

        gameDAO.clear();
        Assertions.assertTrue( gameDAO.listGames().isEmpty(),
                "database did not clear");
    }

    @Test
    @DisplayName("Create game")
    public void successCreateGame() throws DataAccessException {
        gameDAO.clear();

        Assertions.assertTrue( gameDAO.listGames().isEmpty(),
                "database not empty initially");
        gameDAO.createGame(gameData);
        Assertions.assertEquals(1, gameDAO.listGames().size(),
                "failed to add game to database");
    }

    @Test
    @DisplayName("Fail game creation")
    public void failCreateGame() throws DataAccessException {
        gameDAO.clear();

        Assertions.assertThrows(DataAccessException.class,
                () -> gameDAO.createGame(new GameData(0, null, null, null, null)),
                "did not throw exception upon attempting to add null values to table");
    }

    @Test
    @DisplayName("List games")
    public void successListGames() throws DataAccessException {
        gameDAO.clear();

        Assertions.assertNotNull(gameDAO.listGames(),
                "games list is nonexistent");

        Assertions.assertTrue(gameDAO.listGames().isEmpty(),
                "database not empty initially");
        gameDAO.createGame(gameData);
        Assertions.assertEquals(1, gameDAO.listGames().size(),
                "failed to add game to database");
    }

    @Test
    @DisplayName("Fail to list games")
    public void failGetAuth() throws DataAccessException {
        gameDAO.clear();

        Assertions.assertNotNull(gameDAO.listGames(),
                "games list is nonexistent");

        Assertions.assertTrue(gameDAO.listGames().isEmpty(),
                "database not empty initially");
        gameDAO.createGame(gameData);
        Assertions.assertEquals(1, gameDAO.listGames().size(),
                "failed to add game to database");

        gameDAO.createGame(gameData);
        Assertions.assertEquals(2, gameDAO.listGames().size(),
                "failed to add game to database");
    }

    @Test
    @DisplayName("Update game")
    public void successUpdateGame() throws DataAccessException {
        gameDAO.clear();

        gameDAO.createGame(gameData);
        Assertions.assertEquals(1, gameDAO.listGames().size(),
                "failed to add game to database");
        Assertions.assertDoesNotThrow(() -> gameDAO.updateGame(gameID, "WHITE", whiteUsername));
    }

    @Test
    @DisplayName("Fail to update game")
    public void failUpdateGame() throws DataAccessException {
        gameDAO.clear();

        gameDAO.createGame(gameData);
        Assertions.assertEquals(1, gameDAO.listGames().size(),
                "failed to add game to database");
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(gameID, "BLUE", whiteUsername));
    }
}
