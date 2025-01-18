package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        int currentRow = position.getRow();
        int currentCol = position.getColumn();

        BishopMovesCalculator bishopMovesCalculator = new BishopMovesCalculator();
        RookMovesCalculator rookMovesCalculator = new RookMovesCalculator();

        moves = bishopMovesCalculator.pieceMoves(board, position);
        moves.addAll(rookMovesCalculator.pieceMoves(board, position));

        return moves;
    };
}
