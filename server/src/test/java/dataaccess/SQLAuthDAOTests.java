package dataaccess;

import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SQLAuthDAOTests {
    private AuthDAO authDAO;

    private String username1;
    private String username2;
    private String authToken1;
    private String authToken2;
    private AuthData authData1;
    private AuthData authData2;
    private AuthData authDataEmpty;

    @BeforeEach
    void setUp() {
        try {
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        username1 = "alice";
        username2 = "bob";
        authToken1 = "iamanauthtoken";
        authToken2 = "heoiu325w4hfksdfDSFjdsahf3275FSDf983C2749328lkjhdsf";
        authData1 = new AuthData(authToken1, username1);
        authData2 = new AuthData(authToken2, username2);
        authDataEmpty = new AuthData(null, null);
    }

    @Test
    @DisplayName("Clear")
    public void successClear() throws DataAccessException {
        authDAO.clear();

        Assertions.assertNull(authDAO.getAuth(authToken1),
                "database not empty initially");
        authDAO.createAuth(authData1);
        Assertions.assertNotNull(authDAO.getAuth(authToken1),
                "failed to add auth to database");

        authDAO.clear();
        Assertions.assertNull(authDAO.getAuth(authToken1),
                "database failed to clear");
    }

    @Test
    @DisplayName("Create auth")
    public void successCreateAuth() throws DataAccessException {
        authDAO.clear();

        Assertions.assertNull(authDAO.getAuth(authToken1),
                "database not empty initially");
        authDAO.createAuth(authData1);
        Assertions.assertNotNull(authDAO.getAuth(authToken1),
                "failed to add auth to database");
    }

    @Test
    @DisplayName("Fail auth creation")
    public void failCreateAuth() throws DataAccessException {
        authDAO.clear();

        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(authDataEmpty),
                "did not throw exception upon attempting to add null values to table");
    }

    @Test
    @DisplayName("Get auth")
    public void successGetAuth() throws DataAccessException {
        authDAO.clear();

        Assertions.assertNull(authDAO.getAuth(authToken1),
                "database not empty");
        authDAO.createAuth(authData1);
        Assertions.assertNotNull(authDAO.getAuth(authToken1),
                "failed to add user to database");
        Assertions.assertEquals(username1, authDAO.getAuth(authToken1).username(),
                "user data not correct");
    }

    @Test
    @DisplayName("Fail to get user")
    public void failGetAuth() throws DataAccessException {
        authDAO.clear();

        Assertions.assertNull(authDAO.getAuth(authToken1),
                "somehow got auth that wasn't there");
    }

    @Test
    @DisplayName("Delete auth")
    public void successDeleteAuth() throws DataAccessException {
        authDAO.clear();

        authDAO.createAuth(authData1);
        Assertions.assertNotNull(authDAO.getAuth(authToken1),
                "failed to add auth to database");
        authDAO.deleteAuth(authToken1);
        Assertions.assertNull(authDAO.getAuth(authToken1),
                "failed to delete auth from database");
    }

    @Test
    @DisplayName("Fail to delete auth")
    public void failDeleteAuth() throws DataAccessException {
        authDAO.clear();

        authDAO.createAuth(authData1);
        Assertions.assertNotNull(authDAO.getAuth(authToken1),
                "failed to add auth to database");
        authDAO.deleteAuth(authToken2);
        Assertions.assertNotNull(authDAO.getAuth(authToken1),
                "somehow deleted auth depsite bad input");
    }
}
