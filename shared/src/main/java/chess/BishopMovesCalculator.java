package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator {
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();

        int currentRow = position.getRow();
        int currentCol = position.getColumn();
        
        // upper right diagonal moves
        for (int i = 1; i < 8; ++i) {
            int endRow = currentRow + i;
            int endCol = currentCol + i;

            // Check if out of bounds
            if (endRow > 8 || endCol > 8) {
                break;
            }

            ChessPosition endPosition = new ChessPosition(endRow, endCol);

            // Check if end square blocked
            ChessPiece endPositionPiece = board.getPiece(endPosition);
            if (endPositionPiece != null) {
                if (endPositionPiece.getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                break;
            }

            moves.add(new ChessMove(position, endPosition, null));
        }

        // upper left diagonal moves
        for (int i = 1; i < 8; ++i) {
            int endRow = currentRow + i;
            int endCol = currentCol - i;

            // Check if out of bounds
            if (endRow > 8 || endCol < 1) {
                break;
            }

            ChessPosition endPosition = new ChessPosition(endRow, endCol);

            // Check if end square blocked
            ChessPiece endPositionPiece = board.getPiece(endPosition);
            if (endPositionPiece != null) {
                if (endPositionPiece.getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                break;
            }

            moves.add(new ChessMove(position, endPosition, null));
        }

        // lower right diagonal moves
        for (int i = 1; i < 8; ++i) {
            int endRow = currentRow - i;
            int endCol = currentCol + i;

            // Check if out of bounds
            if (endRow < 1 || endCol > 8) {
                break;
            }

            ChessPosition endPosition = new ChessPosition(endRow, endCol);

            // Check if end square blocked
            ChessPiece endPositionPiece = board.getPiece(endPosition);
            if (endPositionPiece != null) {
                if (endPositionPiece.getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                break;
            }

            moves.add(new ChessMove(position, endPosition, null));
        }

        // lower left diagonal moves
        for (int i = 1; i < 8; ++i) {
            int endRow = currentRow - i;
            int endCol = currentCol - i;

            // Check if out of bounds
            if (endRow < 1 || endCol < 1) {
                break;
            }

            ChessPosition endPosition = new ChessPosition(endRow, endCol);

            // Check if end square blocked
            ChessPiece endPositionPiece = board.getPiece(endPosition);
            if (endPositionPiece != null) {
                if (endPositionPiece.getTeamColor() != board.getPiece(position).getTeamColor()) {
                    moves.add(new ChessMove(position, endPosition, null));
                }
                break;
            }

            moves.add(new ChessMove(position, endPosition, null));
        }

        return moves;
    };
}
