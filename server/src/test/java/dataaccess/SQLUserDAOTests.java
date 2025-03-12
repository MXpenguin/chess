package dataaccess;

import org.junit.jupiter.api.BeforeEach;

public class SQLUserDAOTests {
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        try {
            userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
