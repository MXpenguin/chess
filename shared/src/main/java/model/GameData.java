package model;

import chess.ChessGame;

/**
 * Contains data of a game
 */
public record GameData(int gameID, String whiteUsername, String blackUsername,
                       String gameName, ChessGame game) {
}
