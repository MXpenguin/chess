package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import resultsandrequests.*;

import java.util.UUID;

public class GameService {
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public GameService(AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        String authToken = createGameRequest.getAuthToken();

        if (authDataAccess.getAuth(authToken) == null) {
            return new CreateGameResult("Error: unauthorized");
        }

        String gameName = createGameRequest.getGameName();

        return new CreateGameResult(generateGameID(gameName));
    }

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws DataAccessException {
        String authToken = listGamesRequest.authToken();

        if (authDataAccess.getAuth(authToken) == null) {
            return new ListGamesResult("Error: unauthorized");
        }

        return new ListGamesResult(gameDataAccess.listGames());
    }

    private int generateGameID(String gameName) throws DataAccessException {
        int gameID = UUID.randomUUID().toString().hashCode();
        GameData gameData = new GameData(gameID, "", "", gameName, new ChessGame());
        gameDataAccess.createGame(gameData);
        return gameID;
    }
}
