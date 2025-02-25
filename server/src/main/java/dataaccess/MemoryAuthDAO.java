package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryAuthDAO implements AuthDAO {

    private final Collection<AuthData> authDataCollection;

    public MemoryAuthDAO() {
        authDataCollection = new ArrayList<>();
    }

    @Override
    public void clear() throws DataAccessException {
        authDataCollection.clear();
    }

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        authDataCollection.add(authData);
    }
}
