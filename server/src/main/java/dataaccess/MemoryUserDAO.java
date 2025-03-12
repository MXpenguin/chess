package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Memory implementation of user database
 */
public class MemoryUserDAO implements UserDAO {
    private final Collection<UserData> userDataCollection;

    public MemoryUserDAO() {
        userDataCollection = new ArrayList<>();
    }

    /**
     * Clear database
     */
    @Override
    public void clear() throws DataAccessException {
        userDataCollection.clear();
    }

    /**
     * Create user
     *
     * @param userData: user data object to add
     */
    @Override
    public void createUser(UserData userData) {
        userDataCollection.add(userData);
    }

    /**
     * Return requested user data object
     *
     * @param username: user whose data is requested
     * @return user's data
     */
    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (userDataCollection.isEmpty()) {
            return null;
        }
        for (UserData userData : userDataCollection) {
            if (username.equals(userData.username())) {
                return userData;
            }
        }
        return null;
    }

    @Override
    public boolean verifyPasswords(String plainPassword, String databasePassword) {
        if (plainPassword == null) {
            return false;
        }
        return plainPassword.equals(databasePassword);
    }
}
