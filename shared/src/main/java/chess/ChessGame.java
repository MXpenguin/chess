package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor currentTeam;
    private ChessBoard board;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();

        currentTeam = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) return null;

        Collection<ChessMove> possibleMoves
                = piece.pieceMoves(board, startPosition);

        Collection<ChessMove> validMoves = new ArrayList<ChessMove>();
        for (ChessMove move : possibleMoves) {
            ChessPosition endPosition = move.getEndPosition();

            //ChessBoard tempBoard = new ChessBoard(board);

            if (move.getPromotionPiece() != null) {
                board.addPiece(endPosition, new ChessPiece(piece.getTeamColor(), move.getPromotionPiece()));
            } else {
                board.addPiece(endPosition, piece);
            }
            board.addPiece(startPosition, null);

            if (!isInCheck(piece.getTeamColor())) {
                validMoves.add(move);
            }

            board.addPiece(endPosition, null);
            board.addPiece(startPosition, piece);

            //board = tempBoard;
        }

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPos = move.getStartPosition();
        ChessPosition endPos = move.getEndPosition();
        if (validMoves(startPos) != null && validMoves(startPos).contains(move)
                && board.getPiece(startPos).getTeamColor() == currentTeam) {

            if (move.getPromotionPiece() != null) {
                ChessPiece piece = new ChessPiece(board.getPiece(startPos).getTeamColor(),
                        move.getPromotionPiece());
                board.addPiece(endPos, piece);
            } else {
                board.addPiece(endPos, board.getPiece(startPos));
            }

            board.addPiece(startPos, null);
            if (currentTeam == TeamColor.WHITE) {
                currentTeam = TeamColor.BLACK;
            } else {
                currentTeam = TeamColor.WHITE;
            }
        } else {
            throw new InvalidMoveException("Move is not valid");
        }
    }

    private ChessPosition getKingPosition(TeamColor teamColor) {
        ChessPiece king = new ChessPiece(teamColor, ChessPiece.PieceType.KING);
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                ChessPosition pos = new ChessPosition(i,j);
                if (king.equals(board.getPiece(pos))) {
                    return pos;
                }
            }
        }
        return null;
    }

    private Collection<ChessPosition> getFriendPositions(TeamColor teamColor) {
        Collection<ChessPosition> friendPositions = new ArrayList<>();
        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                ChessPosition pos = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    friendPositions.add(pos);
                }
            }
        }
        return friendPositions;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = getKingPosition(teamColor);
        if (kingPos == null) return false;

        for (int i = 1; i <= 8; ++i) {
            for (int j = 1; j <= 8; ++j) {
                ChessPosition startPos = new ChessPosition(i,j);

                ChessPiece piece = board.getPiece(startPos);
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor) continue;
                    Collection<ChessMove> moves = piece.pieceMoves(board, startPos);
                    for (ChessMove move : moves) {
                        if (kingPos.equals(move.getEndPosition())) return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) return false;
        Collection<ChessPosition> positions = getFriendPositions(teamColor);

        for (ChessPosition pos : positions) {
            if (!validMoves(pos).isEmpty()) return false;
        }

        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) return false;
        Collection<ChessPosition> positions = getFriendPositions(teamColor);

        for (ChessPosition pos : positions) {
            if (!validMoves(pos).isEmpty()) return false;
        }

        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
