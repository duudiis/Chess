package game.board;

import game.Game;
import game.board.pieces.*;
import game.exceptions.IllegalMoveException;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Scanner;

public class Board extends JPanel {

    private final int ROWS = 8;
    private final int COLUMNS = 8;

    private final Game game;

    private final Square[][] squares = new Square[ROWS][COLUMNS];
    private Square selectedSquare = null;

    public Board(Game game) {
        this.game = game;

        this.setPreferredSize(new Dimension(640, 640));

        // Set the layout to a grid of 8x8
        setLayout(new GridLayout(ROWS, COLUMNS));

        // Set up the squares
        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLUMNS; y++) {
                squares[x][y] = new Square(this, x, y);
            }
        }

        // Set up the pieces

        // Set up pawns
        for (int col = 0; col < COLUMNS; col++) {
            squares[1][col].setPiece(new Pawn(this, Color.BLACK));
            squares[6][col].setPiece(new Pawn(this, Color.WHITE));
        }

        // Set up the white pieces
        squares[0][0].setPiece(new Rook(this, Color.BLACK));
        squares[0][1].setPiece(new Knight(this, Color.BLACK));
        squares[0][2].setPiece(new Bishop(this, Color.BLACK));
        squares[0][3].setPiece(new King(this, Color.BLACK));
        squares[0][4].setPiece(new Queen(this, Color.BLACK));
        squares[0][5].setPiece(new Bishop(this, Color.BLACK));
        squares[0][6].setPiece(new Knight(this, Color.BLACK));
        squares[0][7].setPiece(new Rook(this, Color.BLACK));

        // Set up the black pieces
        squares[7][0].setPiece(new Rook(this, Color.WHITE));
        squares[7][1].setPiece(new Knight(this, Color.WHITE));
        squares[7][2].setPiece(new Bishop(this, Color.WHITE));
        squares[7][3].setPiece(new King(this, Color.WHITE));
        squares[7][4].setPiece(new Queen(this, Color.WHITE));
        squares[7][5].setPiece(new Bishop(this, Color.WHITE));
        squares[7][6].setPiece(new Knight(this, Color.WHITE));
        squares[7][7].setPiece(new Rook(this, Color.WHITE));
    }

    public Game getGame() {
        return this.game;
    }

    public Square getSquare(int row, int col) {
        return this.squares[row][col];
    }

    public Square getKingSquare(Color color) {
        for (Square[] row : this.squares) {
            for (Square square : row) {
                if (square.getPiece() instanceof King && square.getPiece().getColor() == color) {
                    return square;
                }
            }
        }

        return null;
    }

    public void highlightSquares(ArrayList<Square> highlightedSquares) {
        for (Square square : highlightedSquares) {
            square.setHighlighted(true);
        }
    }

    public void unhighlightAllSquares() {
        for (int x = 0; x < ROWS; x++) {
            for (int y = 0; y < COLUMNS; y++) {
                squares[x][y].setHighlighted(false);
            }
        }
    }

    public void select(Square square) {

        if (this.selectedSquare != null) { this.selectedSquare.setSelected(false); }
        this.selectedSquare = square;

        this.unhighlightAllSquares();

        if (this.selectedSquare == null) { return; }

        this.selectedSquare.setSelected(true);

        Square kingSquare = this.getKingSquare(this.selectedSquare.getPiece().getColor());

        ArrayList<Square> validMovesWithoutCheck = this.selectedSquare.getPiece().getValidMovesWithoutCheck(this.selectedSquare, this.squares, kingSquare);

        this.highlightSquares(validMovesWithoutCheck);
    }

    public void move(Square square) throws IllegalMoveException {

        if (this.selectedSquare == null || this.selectedSquare.getPiece() == null) { return; }

        Square kingSquare = this.getKingSquare(this.selectedSquare.getPiece().getColor());
        ArrayList<Square> validMovesWithoutCheck = this.selectedSquare.getPiece().getValidMovesWithoutCheck(this.selectedSquare, this.squares, kingSquare);

        if (!validMovesWithoutCheck.contains(square)) {
            throw new IllegalMoveException(this.selectedSquare.getPiece(), this.selectedSquare, square);
        }

        square.setPiece(this.selectedSquare.getPiece());

        if (this.selectedSquare.getPiece() instanceof Pawn) {
            ((Pawn) this.selectedSquare.getPiece()).hasMoved();

            if (square.getX() == 0 || square.getX() == 7) {
                square.setPiece(this.promotion(this.selectedSquare.getPiece().getColor()));
            }
        }

        if (this.selectedSquare.getPiece() instanceof Rook) {
            ((Rook) this.selectedSquare.getPiece()).hasMoved();
        }

        if (this.selectedSquare.getPiece() instanceof King) {
            ((King) this.selectedSquare.getPiece()).hasMoved();
        }

        System.out.println("Move: " +
                (this.selectedSquare.getPiece().getColor() == Color.WHITE ? "White" : "Black") + " " +
                this.selectedSquare.getPiece().getName() + " from " +
                this.selectedSquare.getCoordinates() + " to " + square.getCoordinates()
        );

        this.selectedSquare.setPiece(null);
        this.select(null);

        Square otherKingSquare = this.getKingSquare(game.getTurn() == Color.WHITE ? Color.BLACK : Color.WHITE);
        King otherKing = (King) otherKingSquare.getPiece();

        boolean isOtherKingInCheck = otherKing.isCheck(otherKingSquare, this.squares);
        boolean isOtherKingInCheckmate = otherKing.isCheckmate(otherKingSquare, this.squares);

        if (isOtherKingInCheck && !isOtherKingInCheckmate) {
            System.out.println("Check: " + (otherKing.getColor() == Color.WHITE ? "White" : "Black") + " King is in check");
        }

        if (isOtherKingInCheck && isOtherKingInCheckmate) {
            System.out.println("Checkmate: " + (otherKing.getColor() == Color.WHITE ? "White" : "Black") + " King is in checkmate");
            game.endGame((game.getTurn() == Color.WHITE ? "White" : "Black") + " Wins", (otherKing.getColor() == Color.WHITE ? "White" : "Black") + " is in checkmate");
        }

        if (!isOtherKingInCheck && isOtherKingInCheckmate) {
            System.out.println("Stalemate: " + (otherKing.getColor() == Color.WHITE ? "White" : "Black") + " King is in stalemate");
            game.endGame("Tie", (otherKing.getColor() == Color.WHITE ? "White" : "Black") + " is in stalemate");
        }

        game.swapTurn();
    }

    public Piece promotion(Color pawnColor) {

        int result = JOptionPane.showOptionDialog(this,
                "Choose a promotion piece:", "Pawn Promotion",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                new String[] {"Knight", "Bishop", "Rook", "Queen"}, "Queen"
        );

        return switch (result) {
            case 0 -> new Knight(this, pawnColor);
            case 1 -> new Bishop(this, pawnColor);
            case 2 -> new Rook(this, pawnColor);
            default -> new Queen(this, pawnColor);
        };

    }

    public void save(File outputFile) {

        try (PrintWriter writer = new PrintWriter(outputFile)) {

            final StringBuilder stringBuilder = new StringBuilder();

            stringBuilder
                    .append("TURN").append(",")
                    .append(this.game.getTurn() == Color.WHITE ? "WHITE" : "BLACK").append("\n");

            for (Square[] row : this.squares) {
                for (Square square : row) {
                    if (square.getPiece() != null) {

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

                        Piece piece = square.getPiece();

                        objectOutputStream.writeObject(piece);
                        objectOutputStream.close();

                        String serializedPiece = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());

                        stringBuilder
                                .append("PIECE").append(",")
                                .append(square.getX()).append(",")
                                .append(square.getY()).append(",")
                                .append(square.getPiece().getName()).append(",")
                                .append(square.getPiece().getColor() == Color.WHITE ? "WHITE" : "BLACK").append(",")
                                .append(serializedPiece)
                                .append("\n");
                    }
                }
            }

            writer.write(stringBuilder.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void load(File inputFile) {

        for (Square[] row : this.squares) {
            for (Square square : row) {
                square.setPiece(null);
            }
        }

        this.select(null);

        try (Scanner scanner = new Scanner(inputFile)) {

            while (scanner.hasNextLine()) {

                String[] line = scanner.nextLine().split(",");

                if (line[0].equals("TURN")) {
                    if (this.game.getTurn() == Color.WHITE && line[1].equals("BLACK")) {
                        this.game.swapTurn();
                    } else if (this.game.getTurn() == Color.BLACK && line[1].equals("WHITE")) {
                        this.game.swapTurn();
                    }
                }

                if (line[0].equals("PIECE")) {

                    int x = Integer.parseInt(line[1]);
                    int y = Integer.parseInt(line[2]);

                    String pieceName = line[3];

                    String pieceColorString = line[4];
                    Color pieceColor = pieceColorString.equals("WHITE") ? Color.WHITE : Color.BLACK;

                    byte[] serializedPiece = Base64.getDecoder().decode(line[5]);

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedPiece);
                    ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

                    Piece piece = (Piece) objectInputStream.readObject();

                    switch (pieceName) {
                        case "Pawn" -> piece = new Pawn(this, pieceColor);
                        case "Knight" -> piece = new Knight(this, pieceColor);
                        case "Bishop" -> piece = new Bishop(this, pieceColor);
                        case "Rook" -> piece = new Rook(this, pieceColor);
                        case "Queen" -> piece = new Queen(this, pieceColor);
                        case "King" -> piece = new King(this, pieceColor);
                    }

                    this.squares[x][y].setPiece(piece);

                    objectInputStream.close();

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}