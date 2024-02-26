package game.board.pieces;

import game.board.Board;
import game.board.Piece;
import game.board.Square;

import java.awt.*;

public class Bishop extends Piece {

    transient private final Board board;

    public Bishop(Board board, Color color) {
        super("Bishop", color);

        this.board = board;
    }

    @Override
    public boolean isValidMove(Square start, Square end) {

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());

        if (x != y) { return false; }

        int xDir = (start.getX() < end.getX()) ? 1 : -1;
        int yDir = (start.getY() < end.getY()) ? 1 : -1;

        for (int i = 1; i <= x; i++) {
            Piece piece = board.getSquare(start.getX() + (i * xDir), start.getY() + (i * yDir)).getPiece();

            if (piece != null) {

                if (piece.getColor() == this.getColor()) {
                    return false;
                }

                if (piece.getColor() != this.getColor() && i != x) {
                    return false;
                }

            }
        }

        return true;

    }

}
