package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import javax.xml.crypto.Data;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SQLUserDAO implements UserDAO{

    public SQLUserDAO() throws DataAccessException {
        configureDatabase();
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
    public void createUser(UserData userData) throws DataAccessException {
        String username = userData.username();
        String password = userData.password();
        String email = userData.email();

        var statement = "INSERT INTO userTable (username, password, email) VALUES (?, ?, ?)";
//        var json = new Gson().toJson(pet);
//        var id = executeUpdate(statement, pet.name(), pet.type(), json);
//        return new Pet(id, pet.name(), pet.type());
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO userTable (username, password, email) VALUES(?, ?, ?)",
                    RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, password);//TODO
                preparedStatement.setString(3, email);

                preparedStatement.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to configure database: %s", ex.getMessage()));
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        String password;
        String email;
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT password, email FROM userTable WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, username);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        password = resultSet.getString("password");
                        email = resultSet.getString("email");
                        return new UserData(username, password, email);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Failed to read data: %s", e.getMessage()));
        }
        return null;
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userTable (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX(username)
            )
            """//ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            //"""
    };

    private final String[] dropStatements = {
            """
            TRUNCATE TABLE userTable
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Failed to configure database: %s", ex.getMessage()));
        }
    }
}
