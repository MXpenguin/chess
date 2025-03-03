package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Memory implementation of authentication database
 */
public class MemoryAuthDAO implements AuthDAO {

    private final Collection<AuthData> authDataCollection;

    public MemoryAuthDAO() {
        authDataCollection = new ArrayList<>();
    }

    /**
     * Clear database
     */
    @Override
    public void clear() throws DataAccessException {
        authDataCollection.clear();
    }

    /**
     * Create authentication
     *
     * @param authData: authentication data object
     */
    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        authDataCollection.add(authData);
    }

    /**
     * Get requested authentication data
     *
     * @param authToken: authentication token
     * @return AuthData
     */
    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData auth : authDataCollection) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        return null;
    }

    /**
     * Remove authentication data
     *
     * @param authToken: authentication token
     */
    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authDataCollection.remove(getAuth(authToken));
    }
}
