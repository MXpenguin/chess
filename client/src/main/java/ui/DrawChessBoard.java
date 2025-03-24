package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private final ChessGame game;
    private final ChessBoard board;

    private final String DARK_SQUARE_COLOR;
    private final String LIGHT_SQUARE_COLOR;
    private final String DARK_PIECE_COLOR;
    private final String LIGHT_PIECE_COLOR;

    public DrawChessBoard(GameData gameData) {
        this.game = gameData.game();
        this.board = game.getBoard();

        this.DARK_SQUARE_COLOR = SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_DARK_GREY;
        this.LIGHT_SQUARE_COLOR = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_LIGHT_GREY;
        this.DARK_PIECE_COLOR = SET_TEXT_COLOR_BLACK;
        this.LIGHT_PIECE_COLOR = SET_TEXT_COLOR_WHITE;
    }

    public String drawWhitePerspective() {
        StringBuilder boardBuilder = new StringBuilder();
        boardBuilder.append(SET_TEXT_COLOR_WHITE).append("   a   b  \u2009c  \u2009d   e  \u2009f   g  \u2009h\n");
        for (int row = 8; row >= 1; --row) {
            boardBuilder.append(SET_TEXT_COLOR_WHITE).append(row).append(" ");
            for (int col = 1; col <= 8; ++col) {
                boardBuilder.append(drawSquare(row, col));
            }
            boardBuilder.append(RESET_BG_COLOR).append(SET_TEXT_COLOR_WHITE).append(" ").append(row);
            boardBuilder.append("\n");
        }
        boardBuilder.append(SET_TEXT_COLOR_WHITE).append("   a   b  \u2009c  \u2009d   e  \u2009f   g  \u2009h");

        return boardBuilder.toString();
    }

    public String drawBlackPerspective() {
        StringBuilder boardBuilder = new StringBuilder();
        boardBuilder.append(SET_TEXT_COLOR_WHITE).append("   h   g  \u2009f  \u2009e   d  \u2009c   b  \u2009a\n");
        for (int row = 1; row <= 8; ++row) {
            boardBuilder.append(SET_TEXT_COLOR_WHITE).append(row).append(" ");
            for (int col = 8; col >= 1; --col) {
                boardBuilder.append(drawSquare(row, col));
            }
            boardBuilder.append(RESET_BG_COLOR).append(SET_TEXT_COLOR_WHITE).append(" ").append(row);
            boardBuilder.append("\n");
        }
        boardBuilder.append(SET_TEXT_COLOR_WHITE).append("   h   g  \u2009f  \u2009e   d  \u2009c   b  \u2009a\n");

        return boardBuilder.toString();
    }

    private String drawSquare(int row, int col) {
        StringBuilder squareBuilder = new StringBuilder();

        //black or white
        squareBuilder.append((row+col) % 2 == 0 ? DARK_SQUARE_COLOR : LIGHT_SQUARE_COLOR);

        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece != null) {
            squareBuilder.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE
                    ? LIGHT_PIECE_COLOR : DARK_PIECE_COLOR);
            squareBuilder.append(piece);
        } else {
            squareBuilder.append(BLACK_PAWN);//Empty square
        }

        return squareBuilder.toString();
    }

    private String intToChar(int integer) {
        String[] chars = {"a","b","c","d","e","f","g","h"};
        return chars[integer-1];
    }
}
