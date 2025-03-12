package dataaccess;

import model.GameData;

import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO{
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(int gameID, String playerColor, String username) throws DataAccessException {

    }
}
