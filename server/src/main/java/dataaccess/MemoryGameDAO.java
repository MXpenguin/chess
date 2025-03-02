package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryGameDAO implements GameDAO {
    private final Collection<GameData> gameDataCollection;

    public MemoryGameDAO() {
        gameDataCollection = new ArrayList<GameData>();
    }

    @Override
    public void clear() throws DataAccessException {
        gameDataCollection.clear();
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        gameDataCollection.add(gameData);
    }

//    @Override
//    public GameData getGame(String gameID) throws DataAccessException {
//
//    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return gameDataCollection;
    }

    @Override
    public void updateGame(String gameID, String update) throws DataAccessException {

    }
}
