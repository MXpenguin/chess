package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        for (int i = -1; i <= 1; ++i) {
            for (int j = -1; j <= 1; ++j) {
                ChessPosition endPosition = new ChessPosition(currentRow + i, currentCol + j);
                if (isValidPosition(endPosition) && !isBlockedByFriend(board, position, endPosition)) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
            }
        }

        return moves;
    };

    boolean isValidPosition(ChessPosition pos) {
        int row = pos.getRow();
        int col = pos.getColumn();
        return 1 <= row && row <= 8 && 1 <= col && col <= 8;
    };

    boolean isBlockedByFriend(ChessBoard board, ChessPosition currentPos, ChessPosition endPos) {
        if (board.getPiece(endPos) == null) {
            return false;
        }
        ChessGame.TeamColor currentColor = board.getPiece(currentPos).getTeamColor();
        ChessGame.TeamColor endColor = board.getPiece(endPos).getTeamColor();
        return currentColor == endColor;
    };
}
