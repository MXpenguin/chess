package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

public class SQLAuthDAOTests {
    private UserDAO userDAO;

    private String username1;
    private String username2;
    private String password1;
    private String password2;
    private String email1;
    private String email2;
    private UserData user1;
    private UserData user2;
    private String bademail;
    private UserData badUser;

    @BeforeEach
    void setUp() {
        try {
            userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        username1 = "Alice";
        username2 = "Bob";
        password1 = "supersecretpassword";
        password2 = "abcdefg";
        email1 = "alice_32@mail.org";
        email2 = "bobbyboy15@themail.com";
        user1 = new UserData(username1, password1, email1);
        user2 = new UserData(username2, password2, email2);
        bademail = "'bademail'); TRUNCATE userTable;";
        badUser = new UserData(username2, password2, bademail);
    }

    @Test
    @DisplayName("Verify passwords")
    public void successVerifyPasswords() {
        String hashedPassword1 = BCrypt.hashpw(password1, BCrypt.gensalt());
        String hashedPassword2 = BCrypt.hashpw(password2, BCrypt.gensalt());

        Assertions.assertTrue(userDAO.verifyPasswords(password1, hashedPassword1),
                "password hash check failed");
        Assertions.assertTrue(userDAO.verifyPasswords(password2, hashedPassword2),
                "password hash check failed");
    }

    @Test
    @DisplayName("Check bad passwords")
    public void failVerifyPasswords() {
        String hashedPassword1 = BCrypt.hashpw(password1, BCrypt.gensalt());
        String hashedPassword2 = BCrypt.hashpw(password2, BCrypt.gensalt());

        Assertions.assertFalse(userDAO.verifyPasswords(password1, hashedPassword2),
                "password hash check returned true for bad passwords");
        Assertions.assertFalse(userDAO.verifyPasswords(password2, hashedPassword1),
                "password hash check returned true for bad passwords");
    }

    @Test
    @DisplayName("Clear")
    public void successClear() throws DataAccessException {
        userDAO.clear();

        Assertions.assertNull(userDAO.getUser(username1),
                "database not empty initially");
        userDAO.createUser(user1);
        Assertions.assertNotNull(userDAO.getUser(username1),
                "failed to add user to database");

        userDAO.clear();
        Assertions.assertNull(userDAO.getUser(username1),
                "database failed to clear");
    }

    @Test
    @DisplayName("Create user")
    public void successCreateUser() throws DataAccessException {
        userDAO.clear();

        Assertions.assertNull(userDAO.getUser(username1),
                "database not empty");
        userDAO.createUser(user1);
        Assertions.assertNotNull(userDAO.getUser(username1),
                "failed to add user to database");
        Assertions.assertEquals(email1, userDAO.getUser(username1).email(),
                "user data not correct");

        userDAO.clear();

        Assertions.assertNull(userDAO.getUser(username1),
                "database not empty");
        userDAO.createUser(badUser);
        Assertions.assertNotNull(userDAO.getUser(badUser.username()),
                "bad input truncated table");
        Assertions.assertEquals(bademail, userDAO.getUser(badUser.username()).email(),
                "user data not correct");
    }

    @Test
    @DisplayName("Fail user creation")
    public void failCreateUser() throws DataAccessException {
        userDAO.clear();

        UserData emptyUser = new UserData(null, null, null);

        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(emptyUser),
                "did not throw exception upon attempting to add null values to table");
    }

    @Test
    @DisplayName("Get user")
    public void successGetUser() throws DataAccessException {
        userDAO.clear();

        Assertions.assertNull(userDAO.getUser(username1),
                "database not empty");
        userDAO.createUser(user1);
        Assertions.assertNotNull(userDAO.getUser(username1),
                "failed to add user to database");
        Assertions.assertEquals(email1, userDAO.getUser(username1).email(),
                "user data not correct");
    }

    @Test
    @DisplayName("Fail to get user")
    public void failGetUser() throws DataAccessException {
        userDAO.clear();

        Assertions.assertNull(userDAO.getUser(username2),
                "somehow got user that wasn't there");
    }
}
