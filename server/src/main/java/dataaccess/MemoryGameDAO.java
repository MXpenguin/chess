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
    public void updateGame(int gameID, String playerColor, String username) throws DataAccessException {
        GameData game = getGame(gameID);
        gameDataCollection.remove(game);
        if ("WHITE".equals(playerColor)) {
            assert game != null;
            gameDataCollection.add(new GameData(game.gameID(), username, game.blackUsername(),
                    game.gameName(), game.game()));
        } else if ("BLACK".equals(playerColor)) {
            assert game != null;
            gameDataCollection.add(new GameData(game.gameID(), game.whiteUsername(), username,
                    game.gameName(), game.game()));
        }
    }

    private GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : gameDataCollection) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }
}
