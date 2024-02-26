package game.board.pieces;

import game.board.Board;
import game.board.Piece;
import game.board.Square;

import java.awt.*;

public class Knight extends Piece {

    transient private final Board board;

    public Knight(Board board, Color color) {
        super("Knight", color);

        this.board = board;
    }

    @Override
    public boolean isValidMove(Square start, Square end) {

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());

        if (end.getPiece() != null && end.getPiece().getColor() == start.getPiece().getColor()) {
            return false;
        }

        return ((x == 2 && y == 1) || (x == 1 && y == 2));

    }

}
