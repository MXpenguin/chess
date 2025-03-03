package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Memory implementation of game database
 */
public class MemoryGameDAO implements GameDAO {
    private final Collection<GameData> gameDataCollection;

    public MemoryGameDAO() {
        gameDataCollection = new ArrayList<GameData>();
    }

    /**
     * Clear database
     */
    @Override
    public void clear() throws DataAccessException {
        gameDataCollection.clear();
    }

    /**
     * Create game
     *
     * @param gameData: GameData object
     */
    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        gameDataCollection.add(gameData);
    }

    /**
     * List games
     *
     * @return collection of game data objects
     */
    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return gameDataCollection;
    }

    /**
     * Update game with added user
     *
     * @param gameID: id of game to modify
     * @param playerColor: game color to add player
     * @param username: user to add
     */
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
