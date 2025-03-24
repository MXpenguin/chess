package ui;

import chess.ChessBoard;
import chess.ChessGame;
import model.GameData;

public class DrawChessBoard {
    ChessGame game;
    ChessBoard board;

    public DrawChessBoard(GameData gameData) {
        this.game = gameData.game();
        this.board = game.getBoard();
    }

    public String drawWhitePerspective() {
        StringBuilder boardBuilder = new StringBuilder();
        return "white perspective";//TODO
    }

    public String drawBlackPerspective() {
        return "black perspective";//TODO
    }
}
