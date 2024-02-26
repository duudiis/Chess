package game.board.pieces;

import game.board.Board;
import game.board.Piece;
import game.board.Square;

import java.awt.*;

public class Pawn extends Piece {

    transient private final Board board;

    private boolean firstMove;

    public Pawn(Board board, Color color) {
        super("Pawn", color);

        this.board = board;

        this.firstMove = true;
    }

    public void hasMoved() {
        this.firstMove = false;
    }

    @Override
    public boolean isValidMove(Square start, Square end) {

        int x = start.getX();
        int y = start.getY();

        int endX = end.getX();
        int endY = end.getY();

        int direction = start.getPiece().getColor() == Color.WHITE ? -1 : 1;

        if (endY == y) {
            return (endX == x + direction || (firstMove && board.getSquare(x + direction, y).getPiece() == null && endX == x + (direction * 2))) && end.getPiece() == null;
        } else {
            return (endY == y + 1 || endY == y - 1) && endX == x + direction && (end.getPiece() != null && end.getPiece().getColor() != start.getPiece().getColor());
        }

    }
}
