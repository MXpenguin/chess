package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLGameDAO implements GameDAO{

    public SQLGameDAO() throws DataAccessException {
        DatabaseManager.configureDatabase("""
            CREATE TABLE IF NOT EXISTS  gameTable (
              `id` int NOT NULL AUTO_INCREMENT,
              `gameID` int NOT NULL,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `chessGame` TEXT NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(gameID)
            )
            """);
    }

    @Override
    public void clear() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : dropStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to truncate tables: %s", ex.getMessage()));
        }
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        int gameID = gameData.gameID();
        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();
        String gameName = gameData.gameName();
        ChessGame chessGame = gameData.game();

        String gameJson = new Gson().toJson(chessGame);

        var statement = "INSERT INTO gameTable (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?, ?)";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    statement, RETURN_GENERATED_KEYS)) {
                preparedStatement.setInt(1, gameID);
                preparedStatement.setString(2, whiteUsername);
                preparedStatement.setString(3, blackUsername);
                preparedStatement.setString(4, gameName);
                preparedStatement.setString(5, gameJson);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to add game to database: %s", ex.getMessage()));
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> collectionGames = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM gameTable";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    while(resultSet.next()) {
                        int gameID = resultSet.getInt("gameID");
                        String whiteUsername = resultSet.getString("whiteUsername");
                        String blackUsername = resultSet.getString("blackUsername");
                        String gameName = resultSet.getString("gameName");
                        String gameJson = resultSet.getString("chessGame");

                        ChessGame chessGame = new Gson().fromJson(gameJson, ChessGame.class);

                        collectionGames.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
                    }
                    return collectionGames;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Failed to read games data: %s", e.getMessage()));
        }
    }

    @Override
    public void updateGame(int gameID, String playerColor, String username) throws DataAccessException {
        String colorForTable;
        if ("WHITE".equals(playerColor)) {
            colorForTable = "whiteUsername";
        } else if ("BLACK".equals(playerColor)) {
            colorForTable = "blackUsername";
        } else {
            throw new DataAccessException("bad color input");
        }

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE gameTable SET " + colorForTable + "=? WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);

                preparedStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Failed to update game data: %s", e.getMessage()));
        }
    }

    private final String[] dropStatements = {
            """
            TRUNCATE TABLE gameTable
            """
    };
}
