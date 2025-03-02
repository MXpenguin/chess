package dataaccess;

import model.GameData;
import resultsandrequests.GameInformation;

import java.util.Collection;

public interface GameDAO {

    void clear() throws DataAccessException;

    void createGame(GameData gameData) throws DataAccessException;
//    GameData getGame(String gameID) throws DataAccessException;
    Collection<GameData> listGames() throws DataAccessException;
    void updateGame(int gameID, String playerColor, String username) throws DataAccessException;
}
