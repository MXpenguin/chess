package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {

    void clear();

    void createGame(GameData gameData);
    GameData getGame(String gameID);
    Collection<GameData> listGames();
    void updateGame(String gameID, String update);
}
