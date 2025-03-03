package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        int[] jumpOne = {-1,1};
        int[] jumpTwo = {-2,2};

        for (int i : jumpOne) {
            for (int j : jumpTwo) {
                ChessPosition endPosition = new ChessPosition(currentRow + i, currentCol + j);
                if (!isBlocked(board, position, endPosition)) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                endPosition = new ChessPosition(currentRow + j, currentCol + i);
                if (!isBlocked(board, position, endPosition)) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
            }
        }

        return moves;
    };

    private boolean isValidPosition(ChessPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();
        return 1 <= row && row <= 8 && 1 <= col && col <= 8;
    };

    private boolean isBlocked(ChessBoard board, ChessPosition currentPos, ChessPosition endPos) {
        if (!isValidPosition(endPos)) {
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
