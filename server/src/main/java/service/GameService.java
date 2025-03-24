package service;

import chess.ChessGame;
import chess.ChessPosition;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import resultsandrequests.*;

import java.util.Collection;
import java.util.UUID;

/**
 * Service layer for game endpoints
 */
public class GameService {
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public GameService(AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    /**
     * Clear game and auth databases
     */
    public void clear() throws DataAccessException {
        authDataAccess.clear();
        gameDataAccess.clear();
    }

    /**
     * Create game
     *
     * @param createGameRequest: Request object
     * @return CreateGameResult: Result object
     */
    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        String authToken = createGameRequest.getAuthToken();

        if (authDataAccess.getAuth(authToken) == null) {
            return new CreateGameResult("Error: unauthorized");
        }

        String gameName = createGameRequest.getGameName();

        return new CreateGameResult(generateGameID(gameName));
    }

    /**
     * List games
     *
     * @param listGamesRequest: Request object
     * @return ListGamesResult: Result object
     */
    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        String authToken = listGamesRequest.authToken();

        if (authDataAccess.getAuth(authToken) == null) {
            return new ListGamesResult("Error: unauthorized");
        }

        return new ListGamesResult(gameDataAccess.listGames());
    }

    /**
     * Join game
     *
     * @param request: Request object
     * @return JoinGameResult: Result object
     */
    public JoinGameResult joinGame(JoinGameRequest request) throws DataAccessException {
        String authToken = request.getAuthToken();

        if (authDataAccess.getAuth(authToken) == null) {
            return new JoinGameResult("Error: unauthorized");
        }

        int gameID = request.getGameID();
        GameData game = getGame(gameID);

        if (game == null) {
            return new JoinGameResult("Error: bad request");
        }

        String playerColor = request.getPlayerColor();

        if ("WHITE".equals(playerColor)) {
            if (!"".equals(game.whiteUsername()) && game.whiteUsername() != null) {
                return new JoinGameResult("Error: already taken");
            }
        } else if ("BLACK".equals(playerColor)) {
            if (!"".equals(game.blackUsername()) && game.blackUsername() != null) {
                return new JoinGameResult("Error: already taken");
            }
        } else {
            return new JoinGameResult("Error: bad request");
        }

        String username = authDataAccess.getAuth(authToken).username();
        gameDataAccess.updateGame(gameID, playerColor, username);

        return new JoinGameResult();
    }

    private GameData getGame(int gameID) throws DataAccessException {
        Collection<GameData> gameList = gameDataAccess.listGames();
        for (GameData game : gameList) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    private int generateGameID(String gameName) throws DataAccessException {
        int gameID = Math.abs(UUID.randomUUID().toString().hashCode());
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDataAccess.createGame(gameData);
        return gameID;
    }
}
