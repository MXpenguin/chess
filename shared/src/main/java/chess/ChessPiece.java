package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch(type) {
            case KING -> {
                KingMovesCalculator kingMovesCalc = new KingMovesCalculator();
                yield kingMovesCalc.pieceMoves(board, myPosition);
            }
            case QUEEN -> {
                QueenMovesCalculator queenMovesCalc = new QueenMovesCalculator();
                yield queenMovesCalc.pieceMoves(board, myPosition);
            }
            case BISHOP -> {
                BishopMovesCalculator bishopMovesCalc = new BishopMovesCalculator();
                yield bishopMovesCalc.pieceMoves(board, myPosition);
            }
            case KNIGHT -> {
                KnightMovesCalculator knightMovesCalc = new KnightMovesCalculator();
                yield knightMovesCalc.pieceMoves(board, myPosition);
            }
            case ROOK -> {
                RookMovesCalculator rookMovesCalc = new RookMovesCalculator();
                yield rookMovesCalc.pieceMoves(board, myPosition);
            }
            case PAWN -> {
                PawnMovesCalculator pawnMovesCalc = new PawnMovesCalculator();
                yield pawnMovesCalc.pieceMoves(board, myPosition);
            }
            default -> throw new RuntimeException("No type");
        };
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    @Override
    public String toString() {
        String whiteKing = " ♔ ";
        String whiteQueen = " ♕ ";
        String whiteBishop = " ♗ ";
        String whiteKnight = " ♘ ";
        String whiteRook = " ♖ ";
        String whitePawn = " ♙ ";
        String blackKing = " ♚ ";
        String blackQueen = " ♛ ";
        String blackBishop = " ♝ ";
        String blackKnight = " ♞ ";
        String blackRook = " ♜ ";
        String blackPawn = " ♟ ";

        String pieceToString;
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            pieceToString = switch(type) {
                case KING -> whiteKing;
                case QUEEN -> whiteQueen;
                case BISHOP -> whiteBishop;
                case KNIGHT -> whiteKnight;
                case ROOK -> whiteRook;
                case PAWN -> whitePawn;
            };
        } else {
            pieceToString = switch(type) {
                case KING -> blackKing;
                case QUEEN -> blackQueen;
                case BISHOP -> blackBishop;
                case KNIGHT -> blackKnight;
                case ROOK -> blackRook;
                case PAWN -> blackPawn;
            };
        }
        return pieceToString;
    }
}
