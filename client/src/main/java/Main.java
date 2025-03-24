import chess.*;
import client.PreLoginClient;
import client.Repl;
import model.GameData;
import ui.DrawChessBoard;

import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) {
        var serverUrl = "http://localhost:8080";
        if (args.length == 1) {
            serverUrl = args[0];
        }

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client:" + piece);

        new Repl(new PreLoginClient(serverUrl)).run();

        //TODO
//        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_DARK_GREY + BLACK_KING + SET_BG_COLOR_LIGHT_GREY + WHITE_KING + RESET_BG_COLOR);
//        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_LIGHT_GREY + BLACK_KING + SET_BG_COLOR_DARK_GREY + WHITE_KING + RESET_BG_COLOR);
//        System.out.println(SET_TEXT_COLOR_BLACK + SET_BG_COLOR_DARK_GREY + EMPTY + SET_BG_COLOR_LIGHT_GREY + EMPTY + RESET_BG_COLOR);
//        System.out.println(SET_TEXT_COLOR_WHITE + RESET_BG_COLOR + " a \u2009" + "\u2009 b ");
//        System.out.print(new DrawChessBoard(new GameData(0,null,null,"the game", new ChessGame())).drawWhitePerspective());
//        System.out.println();
//        System.out.print(new DrawChessBoard(new GameData(0,null,null,"the game", new ChessGame())).drawBlackPerspective());

    }
}