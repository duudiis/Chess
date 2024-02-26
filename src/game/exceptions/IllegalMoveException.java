package game.exceptions;

import game.board.Piece;
import game.board.Square;

public class IllegalMoveException extends Exception {

    private final Piece piece;
    private final Square start;
    private final Square end;

    public IllegalMoveException(Piece piece, Square start, Square end) {
        super("Illegal move: " + piece.getName() + " at " + start.getCoordinates() + " cannot move to " + end.getCoordinates());

        this.piece = piece;
        this.start = start;
        this.end = end;
    }

    public Piece getPiece() {
        return piece;
    }

    public Square getStart() {
        return start;
    }

    public Square getEnd() {
        return end;
    }

}
