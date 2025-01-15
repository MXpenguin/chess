package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        for (int i = 1; i <= 8; ++i) {
            if (currentRow + i <= 8 && currentCol + i <= 8) {
                ChessPosition endPosition = new ChessPosition(currentRow+i, currentCol+i);
                moves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
            }
            if (currentRow - i >= 1 && currentCol - i >= 1) {
                ChessPosition endPosition = new ChessPosition(currentRow-i, currentCol-i);
                moves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
            }
            if (currentRow + i <= 8 && currentCol - i >= 1) {
                ChessPosition endPosition = new ChessPosition(currentRow+i, currentCol-i);
                moves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
            }
            if (currentRow - i >= 1 && currentCol + i <= 8) {
                ChessPosition endPosition = new ChessPosition(currentRow-i, currentCol+i);
                moves.add(new ChessMove(position, endPosition, ChessPiece.PieceType.BISHOP));
            }
        }

        return moves;
    };
}
