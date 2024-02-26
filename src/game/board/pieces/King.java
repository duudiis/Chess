package game.board.pieces;

import game.board.Board;
import game.board.Piece;
import game.board.Square;

import java.awt.*;
import java.util.ArrayList;

public class King extends Piece {

    transient private final Board board;

    private boolean firstMove;

    public King(Board board, Color color) {
        super("King", color);

        this.board = board;

        this.firstMove = true;
    }

    public void hasMoved() {
        this.firstMove = false;
    }

    public boolean isCheck(Square kingSquare, Square[][] squares) {

        Piece originalPiece = kingSquare.getPiece();

        kingSquare.setPiece(this);

        for (Square[] squaresRow : squares) {
            for (Square square : squaresRow) {
                if (square.getPiece() != null && square.getPiece().getColor() != this.getColor()) {
                    if (square.getPiece().isValidMove(square, kingSquare)) {
                        kingSquare.setPiece(originalPiece);
                        return true;
                    }
                }
            }
        }

        kingSquare.setPiece(originalPiece);
        return false;

    }

    public boolean isCheckmate(Square kingSquare, Square[][] squares) {

        boolean isCheckMate = true;

        for (Square[] squaresRow : squares) {
            for (Square square : squaresRow) {
                if (square.getPiece() != null && square.getPiece().getColor() == this.getColor()) {
                    ArrayList<Square> validMovesWithoutCheck = square.getPiece().getValidMovesWithoutCheck(square, squares, kingSquare);
                    if (!validMovesWithoutCheck.isEmpty()) { isCheckMate = false; }
                }
            }
        }

        return isCheckMate;

    }

    public boolean isCastle(Square start, Square end) {

        if (!this.firstMove) {
            return false;
        }

        int row = start.getX();

        Square rookSquare = this.board.getSquare(row, end.getY() == 6 ? 0 : 7);

        if (rookSquare.getPiece() == null || !(rookSquare.getPiece() instanceof Rook rook)) {
            return false;
        }

        if (!rook.isFirstMove()) {
            return false;
        }

        return false;

    }

    @Override
    public boolean isValidMove(Square start, Square end) {

        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());

        if (x == 1 && y == 1) {
            return end.getPiece() == null || end.getPiece().getColor() != this.getColor();
        } else if (x == 1 && y == 0) {
            return end.getPiece() == null || end.getPiece().getColor() != this.getColor();
        } else if (x == 0 && y == 1) {
            return end.getPiece() == null || end.getPiece().getColor() != this.getColor();
        }

        return false;

    }

}