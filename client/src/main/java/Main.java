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
    }
}