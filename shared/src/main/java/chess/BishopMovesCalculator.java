package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> moves = new ArrayList<ChessMove>();

        int myRow = myPosition.getRow();
        int myCol = myPosition.getColumn();

        for (int i = 1; i <= 8; ++i) {
            ChessPosition endPosition = new ChessPosition(myRow + i, myCol + i);
            if (isBlocked(board, myPosition, endPosition)) {
                break;
            } else {
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            if (isEnemy(board, myPosition, endPosition)) break;
        }
        for (int i = 1; i <= 8; ++i) {
            ChessPosition endPosition = new ChessPosition(myRow - i, myCol + i);
            if (isBlocked(board, myPosition, endPosition)) {
                break;
            } else {
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            if (isEnemy(board, myPosition, endPosition)) break;
        }
        for (int i = 1; i <= 8; ++i) {
            ChessPosition endPosition = new ChessPosition(myRow + i, myCol - i);
            if (isBlocked(board, myPosition, endPosition)) {
                break;
            } else {
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            if (isEnemy(board, myPosition, endPosition)) break;
        }
        for (int i = 1; i <= 8; ++i) {
            ChessPosition endPosition = new ChessPosition(myRow - i, myCol - i);
            if (isBlocked(board, myPosition, endPosition)) {
                break;
            } else {
                moves.add(new ChessMove(myPosition, endPosition, null));
            }
            if (isEnemy(board, myPosition, endPosition)) break;
        }

        return moves;
    }

    private boolean isBlocked(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition) {
        if (!endPosition.isValid()) return true;
        if (board.getPiece(endPosition) == null) {
            return false;
        }
        return board.getPiece(myPosition).getTeamColor() == board.getPiece(endPosition).getTeamColor();
    }

    private boolean isEnemy(ChessBoard board, ChessPosition myPosition, ChessPosition endPosition) {
        if (isBlocked(board, myPosition, endPosition)) return false;
        if (board.getPiece(endPosition) == null) {
            return false;
        }
        return board.getPiece(myPosition).getTeamColor() != board.getPiece(endPosition).getTeamColor();
    }
}
