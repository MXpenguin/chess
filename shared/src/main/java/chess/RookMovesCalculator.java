package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        for (int i = 1; i < 8; ++i) {
            ChessPosition endPosition = new ChessPosition(currentRow + i, currentCol);
            if (isBlocked(board, position, endPosition)) {
                break;
            } else {
                moves.add(new ChessMove(position, endPosition, null));
            }
            if (board.getPiece(endPosition) != null) {
                break;
            }
        }
        for (int i = 1; i < 8; ++i) {
            ChessPosition endPosition = new ChessPosition(currentRow - i, currentCol);
            if (isBlocked(board, position, endPosition)) {
                break;
            } else {
                moves.add(new ChessMove(position, endPosition, null));
            }
            if (board.getPiece(endPosition) != null) {
                break;
            }
        }
        for (int i = 1; i < 8; ++i) {
            ChessPosition endPosition = new ChessPosition(currentRow, currentCol + i);
            if (isBlocked(board, position, endPosition)) {
                break;
            } else {
                moves.add(new ChessMove(position, endPosition, null));
            }
            if (board.getPiece(endPosition) != null) {
                break;
            }
        }
        for (int i = 1; i < 8; ++i) {
            ChessPosition endPosition = new ChessPosition(currentRow, currentCol - i);
            if (isBlocked(board, position, endPosition)) {
                break;
            } else {
                moves.add(new ChessMove(position, endPosition, null));
            }
            if (board.getPiece(endPosition) != null) {
                break;
            }
        }

        return moves;
    };

    private boolean isBlocked(ChessBoard board, ChessPosition currentPos, ChessPosition endPos) {
        if (!endPos.isValid()) {
            return true;
        }
        if (board.getPiece(endPos) == null) {
            return false;
        }
        ChessGame.TeamColor currentColor = board.getPiece(currentPos).getTeamColor();
        ChessGame.TeamColor endColor = board.getPiece(endPos).getTeamColor();
        return currentColor == endColor;
    };
}
