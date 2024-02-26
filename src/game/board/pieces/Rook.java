package game.board.pieces;

import game.board.Board;
import game.board.Piece;
import game.board.Square;

import java.awt.*;

public class Rook extends Piece {

    transient private final Board board;

    private boolean firstMove;

    public Rook(Board board, Color color) {
        super("Rook", color);

        this.board = board;

        this.firstMove = true;
    }

    public boolean isFirstMove() {
        return this.firstMove;
    }

    public void hasMoved() {
        this.firstMove = false;
    }

    @Override
    public boolean isValidMove(Square start, Square end) {

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());

        if (x != 0 && y != 0) { return false; }

        for (int i = 1; i <= x; i++) {
            Piece piece;

            if (start.getX() < end.getX()) {
                piece = board.getSquare(start.getX() + i, start.getY()).getPiece();
            } else {
                piece = board.getSquare(start.getX() - i, start.getY()).getPiece();
            }

            if (piece != null) {
                if (piece.getColor() == this.getColor()) {
                    return false;
                }

                if (piece.getColor() != this.getColor() && i != x) {
                    return false;
                }
            }

        }

        for (int i = 1; i <= y; i++) {
            Piece piece;

            if (start.getY() < end.getY()) {
                piece = board.getSquare(start.getX(), start.getY() + i).getPiece();
            } else {
                piece = board.getSquare(start.getX(), start.getY() - i).getPiece();
            }

            if (piece != null) {
                if (piece.getColor() == this.getColor()) {
                    return false;
                }

                if (piece.getColor() != this.getColor() && i != y) {
                    return false;
                }
            }

        }

        return true;

    }

}
