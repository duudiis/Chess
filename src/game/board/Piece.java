package game.board;

import game.board.pieces.King;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public abstract class Piece implements Serializable {

    private final String name;
    private Color color;
    transient private BufferedImage image;

    public Piece(String name, Color color) {
        this.name = name;
        this.color = color;

        loadImage();
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Image getImage() {
        if (this.image == null) { loadImage(); }
        return this.image;
    }

    private void loadImage() {
        try {
            this.image = ImageIO.read(
                    Objects.requireNonNull(
                            Piece.class.getResourceAsStream("/pieces/" + (this.color == Color.WHITE ? "w" : "b") + "_" + this.name.toLowerCase() + ".png")
                    )
            );
        } catch (Exception e) {
            this.image =  null;
        }
    }

    public ArrayList<Square> getValidMoves(Square start, Square[][] squares) {
        ArrayList<Square> validMoves = new ArrayList<>();

        for (Square[] squaresRow : squares) {
            for (Square square : squaresRow) {
                if (isValidMove(start, square)) {
                    validMoves.add(square);
                }
            }
        }

        return validMoves;
    }

    public ArrayList<Square> getValidMovesWithoutCheck(Square start, Square[][] squares, Square kingSquare) {

        ArrayList<Square> validMovesWithoutCheck = new ArrayList<>();

        King king = (King) kingSquare.getPiece();

        boolean isKingCheck = king.isCheck(kingSquare, squares);

        ArrayList<Square> validMoves = start.getPiece().getValidMoves(start, squares);

        boolean isKingSelected = start.getPiece() instanceof King;

        if (isKingCheck && !isKingSelected) {

            for (Square validMove : validMoves) {

                Piece piece = validMove.getPiece();

                validMove.setPiece(start.getPiece());

                if (!king.isCheck(kingSquare, squares)) {
                    validMovesWithoutCheck.add(validMove);
                }

                validMove.setPiece(piece);

            }

        } else if (isKingSelected) {

            Piece originalPiece = start.getPiece();

            start.setPiece(null);

            for (Square validMove : validMoves) {

                Piece piece = validMove.getPiece();

                validMove.setPiece(start.getPiece());

                if (!king.isCheck(validMove, squares)) {
                    validMovesWithoutCheck.add(validMove);
                }

                validMove.setPiece(piece);

            }

            start.setPiece(originalPiece);

        } else {

            Piece originalPiece = start.getPiece();

            start.setPiece(null);

            for (Square validMove : validMoves) {

                Piece piece = validMove.getPiece();

                validMove.setPiece(start.getPiece());

                if (!king.isCheck(kingSquare, squares)) {
                    validMovesWithoutCheck.add(validMove);
                }

                validMove.setPiece(piece);

            }

            start.setPiece(originalPiece);

        }

        return validMovesWithoutCheck;

    }

    public abstract boolean isValidMove(Square start, Square end);

}
