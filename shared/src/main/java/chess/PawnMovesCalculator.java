package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        ChessGame.TeamColor currentColor = board.getPiece(position).getTeamColor();

        int promotionRow;
        int stepForward;
        if (currentColor == ChessGame.TeamColor.WHITE) {
            promotionRow = 8;
            stepForward = 1;
        } else {
            promotionRow = 1;
            stepForward = -1;
        }

        ChessPosition leftForward = new ChessPosition(currentRow + stepForward, currentCol - stepForward);
        ChessPosition rightForward = new ChessPosition(currentRow + stepForward, currentCol + stepForward);
        ChessPosition forwardOne = new ChessPosition(currentRow + stepForward, currentCol);
        ChessPosition forwardTwo = new ChessPosition(currentRow + 2*stepForward, currentCol);

        // forward movement
        if (isNotForwardBlocked(board, position, forwardOne)) {
            addMove(moves, position, forwardOne, promotionRow);
            if ((currentRow == 2 || currentRow == 7) && isNotForwardBlocked(board, position, forwardTwo)) {
                addMove(moves, position, forwardTwo, promotionRow);
            }
        }

        // diagonal movement
        if (leftForward.isValid() && board.getPiece(leftForward) != null) {
            if (board.getPiece(leftForward).getTeamColor() != currentColor) {
                addMove(moves, position, leftForward, promotionRow);
            }
        }
        if (rightForward.isValid() && board.getPiece(rightForward) != null) {
            if (board.getPiece(rightForward).getTeamColor() != currentColor) {
                addMove(moves, position, rightForward, promotionRow);
            }
        }

        return moves;
    };

    private void addMove(Collection<ChessMove> moves, ChessPosition start, ChessPosition end, int promoRow) {
        if (end.getRow() == promoRow) {
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(start, end, null));
        }
    }

    private boolean isNotForwardBlocked(ChessBoard board, ChessPosition currentPos, ChessPosition endPos) {
        if (!endPos.isValid()) return false;
        return board.getPiece(endPos) == null;
    };
}
