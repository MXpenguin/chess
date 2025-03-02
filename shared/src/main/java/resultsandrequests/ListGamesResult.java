package resultsandrequests;


import model.GameData;

import java.util.Collection;

public class ListGamesResult {
    private Collection<GameData> games;
    private final String message;

    public ListGamesResult(Collection<GameData> games) {
        this.games = games;
        message = null;
    }

    public ListGamesResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Collection<GameData> getGames() {
        return games;
    }
}
