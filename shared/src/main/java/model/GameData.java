package model;

import chess.ChessGame;

import java.util.Objects;

/**
 * Contains data of a game
 */
public final class GameData {
    private final int gameID;
    private final String whiteUsername;
    private final String blackUsername;
    private final String gameName;
    private final transient ChessGame game;

    public GameData(int gameID, String whiteUsername, String blackUsername,
                    String gameName, ChessGame game) {
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }

    public int gameID() {
        return gameID;
    }

    public String whiteUsername() {
        return whiteUsername;
    }

    public String blackUsername() {
        return blackUsername;
    }

    public String gameName() {
        return gameName;
    }

    public ChessGame game() {
        return game;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null || obj.getClass() != this.getClass()) {
            return false;
        }
        var that = (GameData) obj;
        return this.gameID == that.gameID &&
                Objects.equals(this.whiteUsername, that.whiteUsername) &&
                Objects.equals(this.blackUsername, that.blackUsername) &&
                Objects.equals(this.gameName, that.gameName) &&
                Objects.equals(this.game, that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, whiteUsername, blackUsername, gameName, game);
    }

    @Override
    public String toString() {
        return "GameData[" +
                "gameID=" + gameID + ", " +
                "whiteUsername=" + whiteUsername + ", " +
                "blackUsername=" + blackUsername + ", " +
                "gameName=" + gameName + ", " +
                "game=" + game + ']';
    }

}
