package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO implements UserDAO {
    private final Collection<UserData> userDataCollection;

    public MemoryUserDAO() {
        userDataCollection = new ArrayList<>();
    }

    @Override
    public void clear() throws DataAccessException {
        userDataCollection.clear();
    }

    @Override
    public void createUser(UserData userData) {
        userDataCollection.add(userData);
    }

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
}
