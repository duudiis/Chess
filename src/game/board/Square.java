package game.board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Square {

    private final Color LIGHT_BROWN = new Color(230, 214, 185);
    private final Color DARK_BROWN = new Color(156, 101, 63);

    private final Board board;
    public final JPanel square = new JPanel();

    private final int x;
    private final int y;

    private final Color color;

    private Boolean selected = false;
    private Boolean highlighted = false;

    private Piece piece;

    private final JLabel pieceIcon;

    public Square(Board board, int x, int y) {
        this.board = board;

        this.x = x;
        this.y = y;

        square.setPreferredSize(new Dimension(80, 80));

        this.color = (x + y) % 2 == 0 ? LIGHT_BROWN : DARK_BROWN;
        square.setBackground(color);

        this.piece = null;

        // Sets the piece icon (for when a piece is on this square)
        this.pieceIcon = new JLabel();

        pieceIcon.setPreferredSize(new Dimension(80, 80));

        pieceIcon.setVerticalAlignment(SwingConstants.CENTER);
        pieceIcon.setHorizontalAlignment(SwingConstants.CENTER);

        square.add(pieceIcon);

        square.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {

                if (piece != null && board.getGame().getTurn() == piece.getColor()) {
                    board.select(Square.this);
                } else {

                    try {
                        board.move(Square.this);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }

                }

            }
        });

        board.add(square);

    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public String getCoordinates() {
        return (char) (this.x + 'A') + String.valueOf(this.y + 1);
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece piece) {

        if (this.piece != null) {
            pieceIcon.setIcon(null);
            pieceIcon.setText(null);
            pieceIcon.setForeground(null);
        }

        this.piece = piece;

        if (piece != null) {
            if (piece.getImage() != null) {
                pieceIcon.setIcon(new ImageIcon(piece.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH)));
            } else {
                pieceIcon.setText(piece.getName());
                pieceIcon.setForeground(piece.getColor());
            }
        }

    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
        this.updateColor();
    }

    public void setHighlighted(Boolean highlighted) {
        this.highlighted = highlighted;
        this.updateColor();
    }

    private void updateColor() {

        if (this.selected) {
            square.setBackground(new Color(255, 255, 100));
            return;
        }

        if (this.highlighted) {
            if (this.color == LIGHT_BROWN) {
                square.setBackground(new Color(179, 210, 255));
            } else {
                square.setBackground(new Color(158, 198, 255));
            }

            return;
        }

        square.setBackground(this.color);

    }

}
