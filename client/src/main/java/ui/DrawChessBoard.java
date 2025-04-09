package ui;

import chess.*;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class DrawChessBoard {
    private final ChessGame game;
    private final ChessBoard board;

    private final String darkSquareColor;
    private final String lightSquareColor;
    private final String darkPieceColor;
    private final String lightPieceColor;

    private final String darkHighlightSquareColor;
    private final String lightHighlightSquareColor;

    public DrawChessBoard(GameData gameData) {
        this.game = gameData.game();
        this.board = game.getBoard();

        this.darkSquareColor = SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_DARK_GREY;
        this.lightSquareColor = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_LIGHT_GREY;
        this.darkPieceColor = SET_TEXT_COLOR_BLACK;
        this.lightPieceColor = SET_TEXT_COLOR_WHITE;

        this.darkHighlightSquareColor = SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_DARK_GREEN;
        this.lightHighlightSquareColor = SET_BG_COLOR_GREEN + SET_TEXT_COLOR_GREEN;
    }

    public DrawChessBoard(ChessGame game) {
        this.game = game;
        this.board = game.getBoard();

        this.darkSquareColor = SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_DARK_GREY;
        this.lightSquareColor = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_LIGHT_GREY;
        this.darkPieceColor = SET_TEXT_COLOR_BLACK;
        this.lightPieceColor = SET_TEXT_COLOR_WHITE;

        this.darkHighlightSquareColor = SET_BG_COLOR_DARK_GREEN + SET_TEXT_COLOR_DARK_GREEN;
        this.lightHighlightSquareColor = SET_BG_COLOR_GREEN + SET_TEXT_COLOR_GREEN;
    }

    public String drawWhitePerspective() {
        return drawWhitePerspective(null);
    }

    public String drawWhitePerspective(ChessPosition position) {
        Collection<ChessMove> validMoves = null;
        if (position != null) {
            validMoves = game.validMoves(position);
        }
        if (validMoves == null) {
            validMoves = new ArrayList<ChessMove>();
        }

        StringBuilder boardBuilder = new StringBuilder();
        boardBuilder.append(SET_TEXT_COLOR_WHITE).append("   a   b  \u2009c  \u2009d   e  \u2009f   g  \u2009h\n");
        for (int row = 8; row >= 1; --row) {
            boardBuilder.append(SET_TEXT_COLOR_WHITE).append(row).append(" ");
            for (int col = 1; col <= 8; ++col) {
                if (validMoves.contains(new ChessMove(position, new ChessPosition(row, col), null)) ||
                        validMoves.contains(new ChessMove(position, new ChessPosition(row, col), ChessPiece.PieceType.QUEEN))) {
                    boardBuilder.append(drawSquare(row, col, true));
                } else {
                    boardBuilder.append(drawSquare(row, col, false));
                }
            }
            boardBuilder.append(RESET_BG_COLOR).append(SET_TEXT_COLOR_WHITE).append(" ").append(row);
            boardBuilder.append("\n");
        }
        boardBuilder.append(SET_TEXT_COLOR_WHITE).append("   a   b  \u2009c  \u2009d   e  \u2009f   g  \u2009h");

        return boardBuilder.toString();
    }

    public String drawBlackPerspective() {
        return drawBlackPerspective(null);
    }

    public String drawBlackPerspective(ChessPosition position) {
        Collection<ChessMove> validMoves = null;
        if (position != null) {
            validMoves = game.validMoves(position);
        }
        if (validMoves == null) {
            validMoves = new ArrayList<ChessMove>();
        }

        StringBuilder boardBuilder = new StringBuilder();
        boardBuilder.append(SET_TEXT_COLOR_WHITE).append("   h   g  \u2009f  \u2009e   d  \u2009c   b  \u2009a\n");
        for (int row = 1; row <= 8; ++row) {
            boardBuilder.append(SET_TEXT_COLOR_WHITE).append(row).append(" ");
            for (int col = 8; col >= 1; --col) {
                if (validMoves.contains(new ChessMove(position, new ChessPosition(row, col), null)) ||
                        validMoves.contains(new ChessMove(position, new ChessPosition(row, col), ChessPiece.PieceType.KNIGHT))) {
                    boardBuilder.append(drawSquare(row, col, true));
                } else {
                    boardBuilder.append(drawSquare(row, col, false));
                }
            }
            boardBuilder.append(RESET_BG_COLOR).append(SET_TEXT_COLOR_WHITE).append(" ").append(row);
            boardBuilder.append("\n");
        }
        boardBuilder.append(SET_TEXT_COLOR_WHITE).append("   h   g  \u2009f  \u2009e   d  \u2009c   b  \u2009a\n");

        return boardBuilder.toString();
    }

    private String drawSquare(int row, int col, boolean highlighted) {
        String darkSquare;
        String lightSquare;
        if (highlighted) {
            darkSquare = darkHighlightSquareColor;
            lightSquare = lightHighlightSquareColor;
        } else {
            darkSquare = darkSquareColor;
            lightSquare = lightSquareColor;
        }
        StringBuilder squareBuilder = new StringBuilder();

        //black or white
        squareBuilder.append((row+col) % 2 == 0 ? darkSquare : lightSquare);

        ChessPiece piece = board.getPiece(new ChessPosition(row, col));
        if (piece != null) {
            squareBuilder.append(piece.getTeamColor() == ChessGame.TeamColor.WHITE
                    ? lightPieceColor : darkPieceColor);
            squareBuilder.append(piece);
        } else {
            squareBuilder.append(BLACK_PAWN);//Empty square
        }

        return squareBuilder.toString();
    }
}
